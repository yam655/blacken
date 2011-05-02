package com.googlecode.blacken.swing;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;

import com.googlecode.blacken.terminal.BlackenEventType;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenModifier;
import com.googlecode.blacken.terminal.BlackenMouseEvent;
import com.googlecode.blacken.terminal.BlackenWindowEvent;
import com.googlecode.blacken.terminal.BlackenWindowState;

public class EventListener implements WindowListener, KeyListener,
        MouseListener, MouseMotionListener, MouseWheelListener, 
        WindowFocusListener, InputMethodListener {

    static final int NUMBER_OF_KEY_EVENTS = 1024;
    private ArrayBlockingQueue<Integer> keyEvents = 
        new ArrayBlockingQueue<Integer>(NUMBER_OF_KEY_EVENTS);
    private ArrayBlockingQueue<BlackenWindowEvent> windowEvents = 
        new ArrayBlockingQueue<BlackenWindowEvent>(16);
    private ArrayBlockingQueue<BlackenMouseEvent> mouseEvents = 
        new ArrayBlockingQueue<BlackenMouseEvent>(64);
    private EnumSet<BlackenEventType> enabled = 
        EnumSet.noneOf(BlackenEventType.class);
    private BlackenMouseEvent lastMouseEvent = null;
    private BlackenPanel gui;
    private boolean variantKeyMode = true;

    public EnumSet<BlackenEventType> getEnabled() {
        return enabled;
    }

    public void clearEnabled() {
        this.enabled = EnumSet.noneOf(BlackenEventType.class);
    }

    public void unsetEnabled(BlackenEventType disabled) {
        if (enabled == null) {
            return;
        } else {
            this.enabled.remove(disabled);
        }
    }

    public void setEnabled(BlackenEventType enabled) {
        if (enabled == null) {
            return;
        } else {
            this.enabled.add(enabled);
        }
    }

    public void setEnabled(EnumSet<BlackenEventType> enabled) {
        if (enabled == null) {
            clearEnabled();
        } else {
            this.enabled = enabled.clone();
        }
    }

    public EventListener(BlackenPanel gui) {
        this.gui = gui;
    }

    public int blockingPopKey() throws InterruptedException {
        return keyEvents.take();
    }

    public int popKey() {
        try {
            return keyEvents.remove();
        } catch (NoSuchElementException e) {
            return BlackenKeys.NO_KEY;
        }
    }

    public BlackenMouseEvent blockingPopMouse() throws InterruptedException {
        return mouseEvents.take();
    }
    public BlackenMouseEvent popMouse() {
        try {
            return mouseEvents.remove();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public BlackenWindowEvent blockingPopWindow() throws InterruptedException {
        return windowEvents.take();
    }
    public BlackenWindowEvent popWindow() {
        try {
            return windowEvents.remove();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public EnumSet<BlackenModifier> getLockingModifiers() {
        return getLockingModifiers(null);
    }

    public EnumSet<BlackenModifier> getLockingModifiers(EnumSet<BlackenModifier> set) {
        if (set == null) {
            set = EnumSet.noneOf(BlackenModifier.class);
        } else {
            set.remove(BlackenModifier.MODIFIER_KEY_CAPS_LOCK);
            set.remove(BlackenModifier.MODIFIER_KEY_KANA_LOCK);
            set.remove(BlackenModifier.MODIFIER_KEY_NUM_LOCK);
            set.remove(BlackenModifier.MODIFIER_KEY_SCROLL_LOCK);
        }
        try {
            int k = KeyEvent.VK_SCROLL_LOCK;
            if (java.awt.Toolkit.getDefaultToolkit().getLockingKeyState(k)) {
                set.add(BlackenModifier.MODIFIER_KEY_SCROLL_LOCK);
            }
        } catch (UnsupportedOperationException e) {
            /* do nothing */;
        }
        try {
            int k = KeyEvent.VK_NUM_LOCK;
            if (java.awt.Toolkit.getDefaultToolkit().getLockingKeyState(k)) {
                set.add(BlackenModifier.MODIFIER_KEY_NUM_LOCK);
            }
        } catch (UnsupportedOperationException e) {
            /* do nothing */;
        }
        try {
            int k = KeyEvent.VK_KANA_LOCK;
            if (java.awt.Toolkit.getDefaultToolkit().getLockingKeyState(k)) {
                set.add(BlackenModifier.MODIFIER_KEY_KANA_LOCK);
            }
        } catch (UnsupportedOperationException e) {
            /* do nothing */;
        }
        try {
            int k = KeyEvent.VK_CAPS_LOCK;
            if (java.awt.Toolkit.getDefaultToolkit().getLockingKeyState(k)) {
                set.add(BlackenModifier.MODIFIER_KEY_CAPS_LOCK);
            }
        } catch (UnsupportedOperationException e) {
            /* do nothing */;
        }
        return set;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // in variantKeyMode all keys are handled here.
        // In the normal key mode, only action keys are handled here.
        if (!this.variantKeyMode) {
            if (!e.isActionKey()) {
                return;
            }
        }
        switch(e.getKeyCode()) {
        // dead keys
        case KeyEvent.VK_DEAD_ABOVEDOT: 
        case KeyEvent.VK_DEAD_ABOVERING: 
        case KeyEvent.VK_DEAD_ACUTE:
        case KeyEvent.VK_DEAD_BREVE:
        case KeyEvent.VK_DEAD_CARON: 
        case KeyEvent.VK_DEAD_CEDILLA: 
        case KeyEvent.VK_DEAD_CIRCUMFLEX: 
        case KeyEvent.VK_DEAD_DIAERESIS:
        case KeyEvent.VK_DEAD_DOUBLEACUTE: 
        case KeyEvent.VK_DEAD_GRAVE: 
        case KeyEvent.VK_DEAD_IOTA:
        case KeyEvent.VK_DEAD_MACRON:
        case KeyEvent.VK_DEAD_OGONEK: 
        case KeyEvent.VK_DEAD_SEMIVOICED_SOUND: 
        case KeyEvent.VK_DEAD_TILDE:
        case KeyEvent.VK_DEAD_VOICED_SOUND:
            // It would be nice to support some feedback for this -- if 
            // the AWT doesn't provide any by default. I'm thinking of 
            // reporting these events -- but not by default.
            break;

        case KeyEvent.VK_F1:
            // Function keys. All these represent real keys
            // F1-F12 are PC-standard
            loadKey(e, BlackenKeys.KEY_F01);
            break;
        case KeyEvent.VK_F2: 
            loadKey(e, BlackenKeys.KEY_F02);
            break;
        case KeyEvent.VK_F3: 
            loadKey(e, BlackenKeys.KEY_F03);
            break;
        case KeyEvent.VK_F4: 
            loadKey(e, BlackenKeys.KEY_F04);
            break;
        case KeyEvent.VK_F5: 
            loadKey(e, BlackenKeys.KEY_F05);
            break;
        case KeyEvent.VK_F6: 
            loadKey(e, BlackenKeys.KEY_F06);
            break;
        case KeyEvent.VK_F7: 
            loadKey(e, BlackenKeys.KEY_F07);
            break;
        case KeyEvent.VK_F8: 
            loadKey(e, BlackenKeys.KEY_F08);
            break;
        case KeyEvent.VK_F9: 
            loadKey(e, BlackenKeys.KEY_F09);
            break;
        case KeyEvent.VK_F10: 
            loadKey(e, BlackenKeys.KEY_F10);
            break;
        case KeyEvent.VK_F11:
            loadKey(e, BlackenKeys.KEY_F11);
            break;
        case KeyEvent.VK_F12:
            loadKey(e, BlackenKeys.KEY_F12);
            break;
        case KeyEvent.VK_F13: 
            // These are not available on PC keyboards -- these are *not*
            // F1-F12 with shift pressed!
            loadKey(e, BlackenKeys.KEY_F13);
            break;
        case KeyEvent.VK_F14: 
            loadKey(e, BlackenKeys.KEY_F14);
            break;
        case KeyEvent.VK_F15: 
            loadKey(e, BlackenKeys.KEY_F15);
            break;
        case KeyEvent.VK_F16: 
            loadKey(e, BlackenKeys.KEY_F16);
            break;
        case KeyEvent.VK_F17: 
            loadKey(e, BlackenKeys.KEY_F17);
            break;
        case KeyEvent.VK_F18: 
            loadKey(e, BlackenKeys.KEY_F18);
            break;
        case KeyEvent.VK_F19:
            loadKey(e, BlackenKeys.KEY_F19);
            break;
        case KeyEvent.VK_F20: 
            loadKey(e, BlackenKeys.KEY_F20);
            break;
        case KeyEvent.VK_F21: 
            loadKey(e, BlackenKeys.KEY_F21);
            break;
        case KeyEvent.VK_F22: 
            loadKey(e, BlackenKeys.KEY_F22);
            break;
        case KeyEvent.VK_F23: 
            loadKey(e, BlackenKeys.KEY_F23);
            break;
        case KeyEvent.VK_F24: 
            loadKey(e, BlackenKeys.KEY_F24);
            break;

        // modifiers -- not action keys!!
        case KeyEvent.VK_ALT:
        case KeyEvent.VK_ALT_GRAPH:
        case KeyEvent.VK_CONTROL:
        case KeyEvent.VK_SHIFT: 
        case KeyEvent.VK_KANA:
        case KeyEvent.VK_META:
            break;
        // lock keys -- Java modifiers, but treated specially here
        case KeyEvent.VK_CAPS_LOCK: 
            loadKey(e, BlackenKeys.KEY_CAPS_LOCK);
            break;
        case KeyEvent.VK_KANA_LOCK:
            loadKey(e, BlackenKeys.KEY_KANA_LOCK);
            break;
        case KeyEvent.VK_NUM_LOCK: 
            loadKey(e, BlackenKeys.KEY_NUM_LOCK);
            break;
        case KeyEvent.VK_SCROLL_LOCK: 
            loadKey(e, BlackenKeys.KEY_SCROLL_LOCK);
            break;

        // Less common action keys
        case KeyEvent.VK_ACCEPT:
            loadKey(e, BlackenKeys.KEY_ACCEPT);
            break;
        case KeyEvent.VK_BEGIN:
            // Constant for the Begin key. 
            loadKey(e, BlackenKeys.KEY_BEGIN);
            break;
        case KeyEvent.VK_CONVERT:
            // Constant for the Convert function key. 
            loadKey(e, BlackenKeys.KEY_CONVERT);
            break;
        case KeyEvent.VK_CODE_INPUT: 
            // Constant for the Code Input function key. 
            loadKey(e, BlackenKeys.KEY_CODE_INPUT);
            break;
        case KeyEvent.VK_COMPOSE:
            // Constant for the Compose function key. 
            loadKey(e, BlackenKeys.KEY_COMPOSE);
            break;
        case KeyEvent.VK_FINAL:
            loadKey(e, BlackenKeys.KEY_FINAL);
            break;
        case KeyEvent.VK_PROPS:
            loadKey(e, BlackenKeys.KEY_PROPS);
            break;
        case KeyEvent.VK_STOP:
            loadKey(e, BlackenKeys.KEY_STOP);
            break;
        case KeyEvent.VK_CANCEL: 
            loadKey(e, BlackenKeys.KEY_CANCEL);
            break;
        case KeyEvent.VK_AGAIN:
            loadKey(e, BlackenKeys.KEY_AGAIN);
            break;

        // Actions PC users expect to not have specific keys
        case KeyEvent.VK_COPY: 
            loadKey(e, BlackenKeys.KEY_COPY);
            break;
        case KeyEvent.VK_CUT:
            loadKey(e, BlackenKeys.KEY_CUT);
            break;
        case KeyEvent.VK_PASTE:
            loadKey(e, BlackenKeys.KEY_PASTE);
            break;
        case KeyEvent.VK_FIND:
            loadKey(e, BlackenKeys.KEY_FIND);
            break;
        case KeyEvent.VK_HELP:
            loadKey(e, BlackenKeys.KEY_HELP);
            break;
        case KeyEvent.VK_UNDO: 
            loadKey(e, BlackenKeys.KEY_UNDO);
            break;

        // Standard PC action keys
        case KeyEvent.VK_BACK_SPACE:
            loadKey(e, BlackenKeys.KEY_BACKSPACE);
            break;
        case KeyEvent.VK_CONTEXT_MENU:
            // Constant for the Microsoft Windows Context Menu key. 
            loadKey(e, BlackenKeys.KEY_CONTEXT_MENU);
            break;
        case KeyEvent.VK_ESCAPE:
            loadKey(e, BlackenKeys.KEY_ESCAPE);
            break;
        case KeyEvent.VK_ENTER:
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_NP_ENTER);
            } else {
                loadKey(e, BlackenKeys.KEY_ENTER);
            }
            break;
        case KeyEvent.VK_PAUSE:
            loadKey(e, BlackenKeys.KEY_PAUSE);
            break;
        case KeyEvent.VK_WINDOWS: 
            // Constant for the Microsoft Windows "Windows" key. 
            loadKey(e, BlackenKeys.KEY_WINDOWS);
            break;
        case KeyEvent.VK_PRINTSCREEN:
            loadKey(e, BlackenKeys.KEY_PRINT_SCREEN);
            break;
        case KeyEvent.VK_TAB:
            loadKey(e, BlackenKeys.KEY_TAB);
            break;

        // Keys which may also be able via the numpad/keypad
        case KeyEvent.VK_INSERT:
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_KP_INSERT);
            } else {
                loadKey(e, BlackenKeys.KEY_INSERT);
            }
            break;
        case KeyEvent.VK_DELETE:
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_KP_DELETE);
            } else {
                loadKey(e, BlackenKeys.KEY_DELETE);
            }
            break;
        case KeyEvent.VK_LEFT:
            // Constant for the non-numpad left arrow key.
            // In case Java decides to deprecate VK_KP_LEFT, support both here.
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_KP_LEFT);
            } else {
                loadKey(e, BlackenKeys.KEY_LEFT);
            }
            break;
        case KeyEvent.VK_KP_LEFT:
            // Constant for the numeric keypad left arrow key. 
            loadKey(e, BlackenKeys.KEY_KP_LEFT);
            break;
        case KeyEvent.VK_RIGHT:
            // Constant for the non-numpad right arrow key. 
            // In case Java decides to deprecate VK_KP_RIGHT, support both here
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_KP_RIGHT);
            } else {
                loadKey(e, BlackenKeys.KEY_RIGHT);
            }
            break;
        case KeyEvent.VK_KP_RIGHT: 
            // Constant for the numeric keypad right arrow key. 
            loadKey(e, BlackenKeys.KEY_KP_RIGHT);
            break;
        case KeyEvent.VK_CLEAR:
            // Also known as KEY_KP_B2
            loadKey(e, BlackenKeys.KEY_KP_CLEAR);
            break;
        case KeyEvent.VK_UP:
            // Constant for the non-numpad up arrow key. 
            // In case Java decides to deprecate VK_KP_UP, support both here
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_KP_UP);
            } else {
                loadKey(e, BlackenKeys.KEY_UP);
            }
            break;
        case KeyEvent.VK_KP_UP:
            // Constant for the numeric keypad up arrow key. 
            loadKey(e, BlackenKeys.KEY_KP_UP);
            break;
        case KeyEvent.VK_DOWN:
            // Constant for the non-numpad down arrow key. 
            // In case Java decides to deprecate KEY_KP_DOWN, support both here
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_KP_DOWN);
            } else {
                loadKey(e, BlackenKeys.KEY_DOWN);
            }
            break;
        case KeyEvent.VK_KP_DOWN:
            // Constant for the numeric keypad down arrow key. 
            loadKey(e, BlackenKeys.KEY_KP_DOWN);
            break;
        case KeyEvent.VK_PAGE_UP:
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_KP_PAGE_UP);
            } else {
                loadKey(e, BlackenKeys.KEY_PAGE_UP);
            }
            break;
        case KeyEvent.VK_PAGE_DOWN: 
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_KP_PAGE_DOWN);
            } else {
                loadKey(e, BlackenKeys.KEY_PAGE_DOWN);
            }
            break;
        case KeyEvent.VK_HOME:
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_KP_HOME);
            } else {
                loadKey(e, BlackenKeys.KEY_HOME);
            }
            break;
        case KeyEvent.VK_END:
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                loadKey(e, BlackenKeys.KEY_KP_END);
            } else {
                loadKey(e, BlackenKeys.KEY_END);
            }
            break;

        // Number pad actions
        case KeyEvent.VK_ADD:
            loadKey(e, BlackenKeys.KEY_NP_ADD);
            break;
        case KeyEvent.VK_DIVIDE:
            loadKey(e, BlackenKeys.KEY_NP_DIVIDE);
            break;
        case KeyEvent.VK_MULTIPLY:
            loadKey(e, BlackenKeys.KEY_NP_MULTIPLY);
            break;
        case KeyEvent.VK_SUBTRACT: 
            loadKey(e, BlackenKeys.KEY_NP_SUBTRACT);
            break;
        case KeyEvent.VK_SEPARATOR: 
            // Constant for the Numpad Separator key. 
            loadKey(e, BlackenKeys.KEY_NP_SEPARATOR);
            break;

        // Number pad / key pad -- when number lock is enabled
        case KeyEvent.VK_NUMPAD0: 
            loadKey(e, BlackenKeys.KEY_NP_0);
            break;
        case KeyEvent.VK_NUMPAD1: 
            loadKey(e, BlackenKeys.KEY_NP_1);
            break;
        case KeyEvent.VK_NUMPAD2: 
            loadKey(e, BlackenKeys.KEY_NP_2);
            break;
        case KeyEvent.VK_NUMPAD3: 
            loadKey(e, BlackenKeys.KEY_NP_3);
            break;
        case KeyEvent.VK_NUMPAD4: 
            loadKey(e, BlackenKeys.KEY_NP_4);
            break;
        case KeyEvent.VK_NUMPAD5: 
            loadKey(e, BlackenKeys.KEY_NP_5);
            break;
        case KeyEvent.VK_NUMPAD6:
            loadKey(e, BlackenKeys.KEY_NP_6);
            break;
        case KeyEvent.VK_NUMPAD7: 
            loadKey(e, BlackenKeys.KEY_NP_7);
            break;
        case KeyEvent.VK_NUMPAD8: 
            loadKey(e, BlackenKeys.KEY_NP_8);
            break;
        case KeyEvent.VK_NUMPAD9:
            loadKey(e, BlackenKeys.KEY_NP_9);
            break;

        // International keys
        case KeyEvent.VK_ALPHANUMERIC:
            // Constant for the Alphanumeric function key. 
            loadKey(e, BlackenKeys.KEY_ALPHANUMERIC);
            break;
        case KeyEvent.VK_FULL_WIDTH:
            // Constant for the Full-Width Characters function key. 
            loadKey(e, BlackenKeys.KEY_FULL_WIDTH);
            break;
        case KeyEvent.VK_HALF_WIDTH: 
            // Constant for the Half-Width Characters function key. 
            loadKey(e, BlackenKeys.KEY_HALF_WIDTH);
            break;
        case KeyEvent.VK_INPUT_METHOD_ON_OFF:
            // Constant for the input method on/off key. 
            loadKey(e, BlackenKeys.KEY_INPUT_METHOD_TOGGLE);
            break;
        case KeyEvent.VK_JAPANESE_HIRAGANA:
            // Constant for the Japanese-Hiragana function key. 
            loadKey(e, BlackenKeys.KEY_JAPANESE_HIRAGANA);
            break;
        case KeyEvent.VK_JAPANESE_KATAKANA:
            // Constant for the Japanese-Katakana function key. 
            loadKey(e, BlackenKeys.KEY_JAPANESE_KATAKANA);
            break;
        case KeyEvent.VK_JAPANESE_ROMAN:
            // Constant for the Japanese-Roman function key. 
            loadKey(e, BlackenKeys.KEY_JAPANESE_ROMAN);
            break;
        case KeyEvent.VK_NONCONVERT: 
            // Constant for the Don't Convert function key. 
            loadKey(e, BlackenKeys.KEY_NONCONVERT);
            break;
        case KeyEvent.VK_ALL_CANDIDATES:
            loadKey(e, BlackenKeys.KEY_ALL_CANDIDATES);
            break;
        case KeyEvent.VK_PREVIOUS_CANDIDATE: 
            // Constant for the Previous Candidate function key. 
            loadKey(e, BlackenKeys.KEY_PREVIOUS_CANDIDATE);
            break;
        case KeyEvent.VK_ROMAN_CHARACTERS:
            // Constant for the Roman Characters function key. 
            loadKey(e, BlackenKeys.KEY_ROMAN_CHARACTERS);
            break;
        case KeyEvent.VK_KANJI:
            loadKey(e, BlackenKeys.KEY_KANJI);
            break;
        case KeyEvent.VK_KATAKANA:
            // Constant for the Katakana function key. 
            loadKey(e, BlackenKeys.KEY_KATAKANA);
            break;
        case KeyEvent.VK_HIRAGANA:
            // Constant for the Hiragana function key. 
            loadKey(e, BlackenKeys.KEY_HIRAGANA);
            break;
        case KeyEvent.VK_MODECHANGE: 
            loadKey(e, BlackenKeys.KEY_MODECHANGE);
            break;


        case KeyEvent.VK_UNDEFINED: 
            // This value is used to indicate that the keyCode is unknown. 
            loadKey(e, BlackenKeys.KEY_UNKNOWN);
            break;
        default:
            if (this.variantKeyMode) {
                int cp = e.getKeyChar();
                if (cp != KeyEvent.CHAR_UNDEFINED) {
                    if (cp < 0x20) {
                        cp += '@';
                    }
                    loadKey(e, cp);
                }
            }
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (this.variantKeyMode) {
            return;
        }
        
        // XXX: this fails to grab characters outside of the BMP.
        int cp = e.getKeyChar();
        if (cp == KeyEvent.CHAR_UNDEFINED) {
            return;
        }

        if (cp >= 0x20 && !e.isControlDown()) {
            int mods = makeModifierNotice(e);
            if (mods != BlackenKeys.NO_KEY &&
                (mods != BlackenModifier.MODIFIER_KEY_SHIFT.getAsCodepoint() ||
                 BlackenKeys.isSpecial(cp))) {
                loadKey(mods);
            }
            loadKey(cp);
        }
    
    }

    public void loadKey(int key) {
        try {
            keyEvents.add(key);
        } catch(IllegalStateException err) {
            // XXX log this
        }
    }
    public synchronized void pushKey(int key) {
        ArrayBlockingQueue<Integer> oldQueue = keyEvents;
        keyEvents = new ArrayBlockingQueue<Integer>(NUMBER_OF_KEY_EVENTS);
        try {
            keyEvents.add(key);
            keyEvents.addAll(oldQueue);
        } catch(IllegalStateException err) {
            keyEvents.addAll(oldQueue);
            // XXX log this
        }
    }

    protected void loadKey(KeyEvent e, int keycode) {
        int k = keycode;
        if (BlackenKeys.isModifier(k)) {
            k = BlackenKeys.removeModifier(k);
        }
        int mods = makeModifierNotice(e);
        if (mods != BlackenKeys.NO_KEY && 
            (mods != BlackenModifier.MODIFIER_KEY_SHIFT.getAsCodepoint() ||
             BlackenKeys.isSpecial(keycode))) {
            loadKey(mods);
        }
        loadKey(k);
    }

    protected void loadMouse(MouseEvent e, BlackenMouseEvent m) {
        int[] p = gui.findPositionForWindow(e.getY(), e.getX());
        m.setPosition(p[0], p[1]);
        m.setClickCount(e.getClickCount());
        m.setModifiers(BlackenModifier.getAsSet(makeModifierNotice(e)));
        int b = e.getButton();
        if (b == MouseEvent.NOBUTTON) {
            b = 0;
        } else if (b == MouseEvent.BUTTON1) {
            b = 1;
        } else if (b == MouseEvent.BUTTON2) {
            b = 2;
        } else if (b == MouseEvent.BUTTON3) {
            b = 3;
        }
        m.setButton(b);
        if (m.equals(this.lastMouseEvent)) {
            return;
        }
        this.lastMouseEvent = m;
        try {
            mouseEvents.add(m);
        } catch(IllegalStateException err) {
            // XXX log this
            return;
        }
        try {
            keyEvents.add(BlackenKeys.MOUSE_EVENT);
        } catch(IllegalStateException err) {
            // XXX log this
            mouseEvents.remove(m);
        }
    }
    
    protected int makeModifierNotice(InputEvent e) {
        int mods = e.getModifiersEx();
        EnumSet<BlackenModifier> mod = EnumSet.noneOf(BlackenModifier.class);
        if ((mods & KeyEvent.ALT_DOWN_MASK) != 0) {
            mod.add(BlackenModifier.MODIFIER_KEY_ALT);
        }
        if ((mods & KeyEvent.ALT_GRAPH_DOWN_MASK) != 0) {
            mod.add(BlackenModifier.MODIFIER_KEY_ALTGR);
        }
        if ((mods & KeyEvent.CTRL_DOWN_MASK) != 0) {
            mod.add(BlackenModifier.MODIFIER_KEY_CTRL);
        }
        if ((mods & KeyEvent.META_DOWN_MASK) != 0) {
            mod.add(BlackenModifier.MODIFIER_KEY_META);
        }
        if ((mods & KeyEvent.SHIFT_DOWN_MASK) != 0) {
            mod.add(BlackenModifier.MODIFIER_KEY_SHIFT);
        }
        if (mod.isEmpty()) return BlackenKeys.NO_KEY;
        return BlackenModifier.getAsCodepoint(mod);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (enabled.contains(BlackenEventType.MOUSE_CLICKED)){
            BlackenMouseEvent m = 
                new BlackenMouseEvent(BlackenEventType.MOUSE_CLICKED);
            loadMouse(e, m);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (enabled.contains(BlackenEventType.MOUSE_DRAGGED)) {
            BlackenMouseEvent m = 
                new BlackenMouseEvent(BlackenEventType.MOUSE_DRAGGED);
            loadMouse(e, m);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (enabled.contains(BlackenEventType.MOUSE_ENTERED)) {
            BlackenMouseEvent m = 
                new BlackenMouseEvent(BlackenEventType.MOUSE_ENTERED);
            loadMouse(e, m);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (enabled.contains(BlackenEventType.MOUSE_EXITED)) {
            BlackenMouseEvent m = 
                new BlackenMouseEvent(BlackenEventType.MOUSE_EXITED);
            loadMouse(e, m);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (enabled.contains(BlackenEventType.MOUSE_MOVED)) {
            BlackenMouseEvent m = 
                new BlackenMouseEvent(BlackenEventType.MOUSE_MOVED);
            loadMouse(e, m);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (enabled.contains(BlackenEventType.MOUSE_PRESSED)) {
            BlackenMouseEvent m = 
                new BlackenMouseEvent(BlackenEventType.MOUSE_PRESSED);
            loadMouse(e, m);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (enabled.contains(BlackenEventType.MOUSE_RELEASED)) {
            BlackenMouseEvent m = 
                new BlackenMouseEvent(BlackenEventType.MOUSE_RELEASED);
            loadMouse(e, m);
        }
    }

    private void loadWindow(WindowEvent e, BlackenWindowEvent w) {
        int newState = e.getNewState();
        int oldState = e.getOldState();
        Window win = e.getWindow();
        String name = null;
        if (win != null) {
            name = win.getName();
        }
        win = e.getOppositeWindow();
        String oppositeName = null;
        if (win != null) {
            oppositeName = e.getOppositeWindow().getName();
        }
        w.setName(name);
        w.setOppositeName(oppositeName);
        EnumSet<BlackenWindowState> set; 
        set = EnumSet.noneOf(BlackenWindowState.class);
        if ((newState & Frame.ICONIFIED) != 0) {
            set.add(BlackenWindowState.ICONIFIED);
        } 
        if ((newState & Frame.MAXIMIZED_HORIZ) != 0) {
            set.add(BlackenWindowState.MAXIMIZED_HORIZ);
        } 
        if ((newState & Frame.MAXIMIZED_VERT) != 0) {
            set.add(BlackenWindowState.MAXIMIZED_VERT);
        }
        w.setNewState(set);
        set = EnumSet.noneOf(BlackenWindowState.class);
        if ((oldState & Frame.ICONIFIED) != 0) {
            set.add(BlackenWindowState.ICONIFIED);
        } 
        if ((oldState & Frame.MAXIMIZED_HORIZ) != 0) {
            set.add(BlackenWindowState.MAXIMIZED_HORIZ);
        } 
        if ((oldState & Frame.MAXIMIZED_VERT) != 0) {
            set.add(BlackenWindowState.MAXIMIZED_VERT);
        }
        w.setOldState(set);
        try {
            windowEvents.add(w);
        } catch(IllegalStateException err) {
            // XXX log this
            return;
        }
        try {
            keyEvents.add(BlackenKeys.WINDOW_EVENT);
        } catch(IllegalStateException err) {
            // XXX log this
            mouseEvents.remove(w);
        }
    }
    
    @Override
    public void windowActivated(WindowEvent e) {
        if (enabled.contains(BlackenEventType.WINDOW_ACTIVATED)) {
            BlackenWindowEvent w =
                new BlackenWindowEvent(BlackenEventType.WINDOW_ACTIVATED);
            loadWindow(e, w);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        if (enabled.contains(BlackenEventType.WINDOW_CLOSED)) {
            BlackenWindowEvent w =
                new BlackenWindowEvent(BlackenEventType.WINDOW_CLOSED);
            loadWindow(e, w);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (enabled.contains(BlackenEventType.WINDOW_CLOSING)) {
            BlackenWindowEvent w =
                new BlackenWindowEvent(BlackenEventType.WINDOW_CLOSING);
            loadWindow(e, w);
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        if (enabled.contains(BlackenEventType.WINDOW_DEACTIVATED)) {
            BlackenWindowEvent w =
                new BlackenWindowEvent(BlackenEventType.WINDOW_DEACTIVATED);
            loadWindow(e, w);
        }
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        if (enabled.contains(BlackenEventType.WINDOW_DEICONIFIED)) {
            BlackenWindowEvent w =
                new BlackenWindowEvent(BlackenEventType.WINDOW_DEICONIFIED);
            loadWindow(e, w);
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {
        if (enabled.contains(BlackenEventType.WINDOW_ICONIFIED)) {
            BlackenWindowEvent w =
                new BlackenWindowEvent(BlackenEventType.WINDOW_ICONIFIED);
            loadWindow(e, w);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        if (enabled.contains(BlackenEventType.WINDOW_OPENED)) {
            BlackenWindowEvent w =
                new BlackenWindowEvent(BlackenEventType.WINDOW_OPENED);
            loadWindow(e, w);
        }
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        if (enabled.contains(BlackenEventType.WINDOW_GAINED_FOCUS)) {
            BlackenWindowEvent w =
                new BlackenWindowEvent(BlackenEventType.WINDOW_GAINED_FOCUS);
            loadWindow(e, w);
        }
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        if (enabled.contains(BlackenEventType.WINDOW_LOST_FOCUS)) {
            BlackenWindowEvent w =
                new BlackenWindowEvent(BlackenEventType.WINDOW_LOST_FOCUS);
            loadWindow(e, w);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        BlackenMouseEvent m = 
            new BlackenMouseEvent(BlackenEventType.MOUSE_WHEEL);
        int rot = e.getWheelRotation();
        if (rot < 0) {
            m.setButton(4);
            rot *= -1;
        } else {
            m.setButton(5);
        }
        int[] p = gui.findPositionForWindow(e.getY(), e.getX());
        m.setPosition(p[0], p[1]);
        m.setClickCount(rot);
        m.setModifiers(BlackenModifier.getAsSet(makeModifierNotice(e)));
        try {
            mouseEvents.add(m);
        } catch(IllegalStateException err) {
            // XXX log this
            return;
        }
        try {
            keyEvents.add(BlackenKeys.MOUSE_EVENT);
        } catch(IllegalStateException err) {
            // XXX log this
            mouseEvents.remove(m);
        }
    }

    @Override
    public void caretPositionChanged(InputMethodEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void inputMethodTextChanged(InputMethodEvent e) {
        // TODO Auto-generated method stub
        
    }

    /**
     * Switch between two modes of processing incoming keys.
     * 
     * In Java you can't have right/left variant keys and use Unicode.
     * 
     * <p>The issue is there are two different types of key events, the
     * low-level "keyPressed" and the higher-level "keyTyped" events.
     * The keyTyped events don't get action keys and can't tell key
     * locations. The keyPressed events don't get the full processing to get
     * the full-set of characters (particularly some of the Unicode characters).
     * </p>
     * 
     * <p>It would be nice if we could process the action keys in keyPressed
     * and the keys that generate good, solid Unicode in keyTyped. 
     * Unfortunately, Java makes that hard. We're stuck with one set of two
     * bad options:</p>
     * <ul>
     * <li>We get no knowledge of keyboard variations for a number of
     *     important keys (like those around the number pad). (When the
     *     variantKeyMode is disabled.)</li>
     * <li>We can't get full unicode processing done automatically. (When the
     *     variantKeyMode is enabled, and the default.)</li>
     * </ul>
     * 
     * <p>For the general Rogue-like use-case, we rarely care about full
     * Unicode support. We need the option, of course, so that we can more
     * easily support native non-English Roguelikes.
     * 
     * @param variantKeyMode the when true limit Unicode characters
     */
    public void setVariantKeyMode(boolean variantKeyMode) {
        this.variantKeyMode = variantKeyMode;
    }

    /**
     * @return the variant key mode state
     */
    public boolean isVariantKeyMode() {
        return variantKeyMode;
    }

}
