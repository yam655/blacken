/* blacken - a library for Roguelike games
 * Copyright © 2012 Steven Black <yam655@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.googlecode.blacken.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.EnumSet;

import javax.swing.JFrame;
import com.googlecode.blacken.terminal.BlackenEventType;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenModifier;
import com.googlecode.blacken.terminal.BlackenMouseEvent;
import com.googlecode.blacken.terminal.BlackenWindowEvent;
import com.googlecode.blacken.terminal.TerminalInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Steven Black
 */
public class SwingTerminalV2 extends ReducedSwingTerminalV2
                    implements ComponentListener {
    static private final Logger LOGGER = LoggerFactory.getLogger(SwingTerminal.class);
    protected EventListenerV2 listener;
    protected JFrame frame;
    protected DropTarget dropTarget;

    /**
     * Create and initialize the function at once.
     *
     * @param name Window name
     * @param rows number of rows (0 is acceptable)
     * @param cols number of columns (0 is acceptable)
     * @param font Font name or path
     * @return new SwingTerminal
     */
    static public SwingTerminal initialize(String name, int rows, int cols, String font) {
        SwingTerminal terminal = new SwingTerminal();
        terminal.init(name, rows, cols, font);
        return terminal;
    }
    private boolean nowFullscreen = false;
    private Rectangle windowedBounds = null;
    private boolean inhibitFullScreen = false;
    private int lastModifier;
    //private BlackenPanelV2 glassGui = null;
    private ReducedSwingTerminalV2 glass = null;

    /**
     * Create a new terminal
     */
    public SwingTerminalV2() {
        super();
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // do nothing
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // do nothing
    }

    @Override
    public void componentResized(ComponentEvent e) {
        listener.loadKey(BlackenKeys.RESIZE_EVENT);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // do nothing
    }

    @Override
    public void disableEventNotice(BlackenEventType event) {
        listener.unsetEnabled(event);
    }

    @Override
    public void disableEventNotices() {
        listener.clearEnabled();
    }

    @Override
    public void enableEventNotice(BlackenEventType event) {
        listener.setEnabled(event);
    }

    @Override
    public void enableEventNotices(EnumSet<BlackenEventType> events) {
        if (events == null) {
            events = EnumSet.allOf(BlackenEventType.class);
        }
        listener.setEnabled(events);
    }

    @Override
    public int getch() {
        // this.refresh();
        int ch = listener.popKey();
        int activeModifier = this.lastModifier;
        if (ch == BlackenKeys.NO_KEY) {
            // LOGGER.debug("Need to do blocking wait.");
            this.refresh();
            try {
                ch = listener.blockingPopKey();
            } catch (InterruptedException e) {
                ch = BlackenKeys.NO_KEY;
            }
        } else {
            // LOGGER.debug("Found key right away.");
        }
        if (BlackenKeys.isModifier(ch)) {
            this.lastModifier = ch;
        } else {
            this.lastModifier = BlackenKeys.NO_KEY;
        }
        if (ch == BlackenKeys.RESIZE_EVENT) {
            this.windowResized();
            if (this.glass != null) {
                this.glass.windowResized();
            }
        } else if (ch == BlackenKeys.KEY_ENTER) {
            // Set<BlackenModifier> mods = BlackenModifier.getAsSet(activeModifier);
            if (activeModifier == BlackenModifier.MODIFIER_KEY_ALT.getAsCodepoint()) {
                if (!this.inhibitFullScreen) {
                    this.setFullScreen(!this.getFullScreen());
                    ch = BlackenKeys.NO_KEY;
                }
            }
        }
        return ch;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.core.TerminalInterface#getLockingStates()
     */
    @Override
    public EnumSet<BlackenModifier> getLockingStates() {
        return listener.getLockingModifiers();
    }

    @Override
    public BlackenMouseEvent getmouse() {
        BlackenMouseEvent e = listener.popMouse();
        if (e == null) {
            return e;
        }
        return e;
    }

    @Override
    public BlackenWindowEvent getwindow() {
        BlackenWindowEvent e = listener.popWindow();
        return e;
    }

    @Override
    public TerminalInterface getGlass() {
        return this.glass;
    }

    @Override
    public TerminalInterface initGlass(int rows, int cols) {
        return initGlass(rows, cols, null);
    }

    @Override
    public TerminalInterface initGlass(int rows, int cols, String font) {
        /*
        if (this.frame == null) {
            this.init("Java", rows, cols, font);
        }
        if (this.glass != null) {
            this.glass.setFont(font);
            this.glass.resize(rows, cols);
            this.glass.setCursorLocation(-1, -1);
            return this.glass;
        }
        glassGui = new BlackenPanelV2(true);
        glassGui.setIgnoreRepaint(false);
        glassGui.setDoubleBuffered(true);
        Rectangle frameBounds = frame.getBounds();
        this.glass = new ReducedSwingTerminalV2(this, glassGui);
        this.glass.getEmpty().setBackground(0x00010000);
        this.glass.init("Glass", rows, cols, font);

        AwtCell empty = new AwtCell();
        if (font == null) {
            font = Font.MONOSPACED;
        }

        // empty.setBackgroundColor(new Color(0x00000000));
        Font fontObj = new Font(font, Font.PLAIN, 1);
        glassGui.init(fontObj, rows, cols, empty);
        glass.setCursorLocation(-1, -1);
        // frame.setGlassPane(glassGui);
        glassGui.setOpaque(true);
        glassGui.setVisible(false);

        // frame.pack();
        // frame.setBounds(frameBounds);
        return glass;
        */
        throw new UnsupportedOperationException("Not complete.");
    }

    @Override
    public void init(String name, int rows, int cols, String font) {
        if (frame != null) {
            setFont(font);
            resize(rows, cols);
            setCursorLocation(-1,-1);
            return;
        }
        super.init(name, rows, cols, font);
        frame = new JFrame(name);
        frame.setIgnoreRepaint(true);
        frame.setFocusTraversalKeysEnabled(false);

        BlackenPanelV2 gui = new BlackenPanelV2(false);
        gui.setIgnoreRepaint(true);
        gui.setDoubleBuffered(true);
        listener = new EventListenerV2(gui);
        this.setGui(gui);

        frame.setSize(600, 450);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gui);
        frame.setCursor(null);
        frame.pack();

        AwtCell empty = new AwtCell();
        if (font == null) {
            font = Font.MONOSPACED;
        }
        Font fontObj = new Font(font, Font.PLAIN, 1);
        gui.init(fontObj, rows, cols, empty);
        setCursorLocation(-1, -1);

        frame.setResizable(true);
        frame.addKeyListener(listener);
        frame.addMouseListener(listener);
        frame.addMouseMotionListener(listener);
        frame.addMouseWheelListener(listener);
        frame.addWindowListener(listener);
        frame.addWindowFocusListener(listener);
        frame.addComponentListener(this);
        frame.addInputMethodListener(listener);

        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null); // places window in center of screen
        frame.setVisible(true);
    }

    @Override
    public boolean setFullScreen(boolean state) {
        if (state == this.nowFullscreen) {
            return this.nowFullscreen;
        }
        if (state) {
            this.windowedBounds = frame.getBounds();
            frame.setVisible(false);
            frame.removeNotify();
            frame.setUndecorated(true);
            frame.addNotify();
            frame.setResizable(false);
            frame.setSize(frame.getToolkit().getScreenSize().width, frame.getToolkit().getScreenSize().height);
            frame.setAlwaysOnTop(true);
            frame.setLocation(0, 0);
            frame.setVisible(true);
        } else {
            frame.setVisible(false);
            frame.removeNotify();
            frame.setUndecorated(false);
            frame.addNotify();
            frame.setResizable(true);
            frame.setAlwaysOnTop(false);
            frame.setBounds(windowedBounds);
            frame.setVisible(true);
            windowedBounds = null;
        }
        this.nowFullscreen = state;
        return this.nowFullscreen;
    }

    @Override
    public boolean getFullScreen() {
        return this.nowFullscreen;
    }

    @Override
    public void inhibitFullScreen(boolean state) {
        this.inhibitFullScreen = state;
    }

    @Override
    public void refresh() {
        getGui().refresh();
        if (this.glass != null) {
            glass.getGui().refresh();
        }
    }

    @Override
    public void quit() {
        frame.dispose();
    }
}
