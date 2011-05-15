package com.googlecode.blacken.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.font.GraphicAttribute;
import java.awt.font.TextAttribute;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.terminal.AbstractTerminal;
import com.googlecode.blacken.terminal.BlackenEventType;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenModifier;
import com.googlecode.blacken.terminal.BlackenMouseEvent;
import com.googlecode.blacken.terminal.BlackenWindowEvent;
import com.googlecode.blacken.terminal.CellWalls;
import com.googlecode.blacken.terminal.TerminalCell;
import com.googlecode.blacken.terminal.TerminalCellLike;
import com.googlecode.blacken.terminal.TerminalInterface;
import com.googlecode.blacken.terminal.TerminalStyle;

public class SwingTerminal extends AbstractTerminal
                    implements ComponentListener {

    protected BlackenPanel gui;
    protected HashMap<Integer, Color> swingColor = new HashMap<Integer, Color>();
    protected EventListener listener;
    protected JFrame frame;
    protected HashMap<String, GraphicAttribute> replacement = null;
    protected DropTarget dropTarget;
    
    public SwingTerminal() {
        super();
    }

    @Override
    public void clear() {
        super.clear();
        Font f = gui.getEmpty().getFont();
        AwtCell awtempty = setAwtFromTerminal(gui.getEmpty(), getEmpty());
        awtempty.setDirty(true);
        awtempty.setFont(f);
        awtempty.setForegroundColor(this.getSwingColor(getCurForeground()));
        awtempty.setBackgroundColor(this.getSwingColor(getCurBackground()));
        gui.setEmpty(awtempty);
        gui.clear();
    }
    
    @Override
    public void componentHidden(ComponentEvent e) {
        return;
    }
    
    @Override
    public void componentMoved(ComponentEvent e) {
        return;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        listener.loadKey(BlackenKeys.RESIZE_EVENT);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        return;
    }

    @Override
    public void copyFrom(TerminalInterface oterm, int numRows, int numCols,
                         int startY, int startX, int destY, int destX) {
        if (oterm == this) {
            this.moveBlock(numRows, numCols, startY, startX, destY, destX);
        } else {
            getGrid().copyFrom(oterm.getGrid(), numRows, numCols, startY, startX, 
                           destY, destX, new TerminalCell().new ResetCell());
            forceRefresh(numRows, numCols, destY, destX);
        }
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
    
    private void forceRefresh(int numRows, int numCols, int startY, int startX) {
        Grid<TerminalCellLike> grid = getGrid();
        for (int y = startY; y < numRows + startY; y++) {
            for (int x = startX; x < numCols + startX; x++) {
                this.setAwtFromTerminal(gui.get(y, x), grid.get(y, x));
                grid.get(y, x).setDirty(false);
            }
        }
    }

    @Override
    public int getch() {
        this.refresh();
        int ch = listener.popKey();
        if (ch == BlackenKeys.NO_KEY) {
            this.refresh();
            try {
                ch = listener.blockingPopKey();
            } catch (InterruptedException e) {
                ch = BlackenKeys.NO_KEY;
            }
        }
        if (ch == BlackenKeys.RESIZE_EVENT) {
            gui.windowResized();
            getGrid().setSize(gui.getGridSize());
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

    protected Color getSwingColor(int c) {
        Color clr;
        ColorPalette palette = getPalette();
        if (palette != null) {
            c = palette.getColor(c);
        }
        if (swingColor.containsKey(c)) {
            clr = swingColor.get(c);
        } else {
            clr = new Color(c);
            swingColor.put(c, clr);
        }
        return clr;
    }

    @Override
    public BlackenWindowEvent getwindow() {
        BlackenWindowEvent e = listener.popWindow();
        return e;
    }

    @Override
    public void init(String name, int rows, int cols) {
        super.init(name, rows, cols);
        if (frame == null) {
            frame = new JFrame(name);
        }
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);

        if (gui == null) {
            gui = new BlackenPanel();
        }
        if (listener == null) {
            listener = new EventListener(gui);
        }
        frame.setSize(480, 640);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gui);
        frame.setCursor(null);
        frame.pack();
        
        AwtCell empty = new AwtCell();
        gui.init(font, rows, cols, empty);
        getGrid().reset(rows, cols, getEmpty());

        frame.addKeyListener(listener);
        frame.addMouseListener(listener);
        frame.addMouseMotionListener(listener);
        frame.addMouseWheelListener(listener);
        frame.addWindowListener(listener);
        frame.addWindowFocusListener(listener);
        frame.addComponentListener(this);
        frame.addInputMethodListener(listener);

        gui.resizeFrame(frame, 0);
        frame.setLocationRelativeTo(null); // places window in center of screen
        frame.setResizable(true);
        move(0, 0);

        frame.setVisible(true);
    }

    /**
     * We do not cache the entire dim palette at palette-load as it isn't
     * expected that many applications will make use of it.
     * 
     * @param color standard (opaque) color in an 
     * @return
     */
    protected int makeDim(final int color) {
        return ColorHelper.increaseAlpha(color, -0.20);
    }

    @Override
    public void moveBlock(int numRows, int numCols, int origY, int origX, 
                          int newY, int newX) {
        getGrid().moveBlock(numRows, numCols, origY, origX, newY, newX, 
                       new TerminalCell().new ResetCell());
        gui.moveBlock(numRows, numCols, origY, origX, newY, newX);
    }

    @Override
    public void quit() {
        frame.dispose();
    }

    @Override
    public void refresh() {
        gui.doUpdate();
        gui.repaint();
    }

    @Override
    public void set(int y, int x, String glyph, 
                    Integer foreground, Integer background, 
                    EnumSet<TerminalStyle> style, 
                    EnumSet<CellWalls> walls) {
        TerminalCellLike tcell = getGrid().get(y,x);
        if (walls != null) {
            tcell.setCellWalls(walls);
        }
        if (style != null) {
            tcell.setStyle(style);
        }
        if (foreground != null) {
            tcell.setForeground(foreground);
        }
        if (background != null) {
            tcell.setBackground(background);
        }
        if (glyph != null) {
            tcell.setSequence(glyph);
        }
        gui.set(y, x, this.setAwtFromTerminal(null, tcell));
        tcell.setDirty(true);
    }

    @Override
    public void set(int y, int x, TerminalCellLike tcell) {
        AwtCell acell = gui.get(y, x);
        AwtCell r = this.setAwtFromTerminal(acell, tcell);
        if (acell == null) {
            gui.set(y, x, r);
        }
        Grid<TerminalCellLike> grid = getGrid();
        grid.get(y, x).set(tcell);
        grid.get(y, x).setDirty(false);
    }

    protected AwtCell setAwtFromTerminal(AwtCell awt, final TerminalCellLike term) {
        if (term == null && awt != null) {
            awt.setCell(null);
            return awt;
        }
        if (awt == null) {
            awt = new AwtCell();
            awt.setFont(gui.getEmpty().getFont());
            if (term == null) {
                return awt;
            }
        } else {
            awt.setFont(gui.getEmpty().getFont());
        }
        awt.setSequence(term.getSequence());
        awt.setCellWalls(term.getCellWalls());
        awt.clearTextAttributes();
        Set<TerminalStyle> styles = term.getStyle();
        int fore = term.getForeground();
        int back = term.getBackground();
        if (styles.contains(TerminalStyle.STYLE_REVERSE)) {
            int r = fore;
            fore = back;
            back = r;
        }
        if (styles.contains(TerminalStyle.STYLE_DIM)) {
            fore = makeDim(fore);
        }
        awt.setBackgroundColor(getSwingColor(back));
        awt.setForegroundColor(getSwingColor(fore));
        Map<TextAttribute, Object> attrs = awt.getAttributes();
        // attrs.put(TextAttribute.FAMILY, Font.MONOSPACED);
        if (styles.contains(TerminalStyle.STYLE_LIGHT)) {
            if (styles.contains(TerminalStyle.STYLE_BOLD)) {
                if (styles.contains(TerminalStyle.STYLE_HEAVY)) {
                    // STYLE_LIGHT | STYLE_BOLD | STYLE_HEAVY
                    // This is currently undefined.                
                } else {
                    // STYLE_LIGHT | STYLE_BOLD
                    attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_LIGHT);
                }
            } else if (styles.contains(TerminalStyle.STYLE_HEAVY)) {
                // STYLE_LIGHT | STYLE_HEAVY
                attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_MEDIUM);
            } else {
                // STYLE_LIGHT
                attrs.put(TextAttribute.WEIGHT, 
                          TextAttribute.WEIGHT_EXTRA_LIGHT);
            }
        } else if (styles.contains(TerminalStyle.STYLE_BOLD)) {
            if (styles.contains(TerminalStyle.STYLE_HEAVY)) {
                // STYLE_BOLD | STYLE_HEAVY
                attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);
            } else {
                // STYLE_BOLD
                attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
            }
    
        } else if (styles.contains(TerminalStyle.STYLE_HEAVY)) {
            // STYLE_HEAVY
            attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_HEAVY);
        } 
        for(TerminalStyle style : styles) {
            switch (style) {
            case STYLE_LIGHT:
            case STYLE_BOLD:
            case STYLE_HEAVY:
                break; // handled elsewhere
            
            case STYLE_NARROW:
                attrs.put(TextAttribute.WIDTH, TextAttribute.WIDTH_CONDENSED);
                break;
            case STYLE_WIDE:
                attrs.put(TextAttribute.WIDTH, TextAttribute.WIDTH_EXTENDED);
                break;
            // What is STYLE_NARROW | STYLE_WIDE ?
            
            case STYLE_ITALIC:
                attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                break;
            
            // Mapped to SUPERSCRIPT (possibly *unclean* mapping)
            case STYLE_SUPERSCRIPT: // SUPERSCRIPT_SUPER
                attrs.put(TextAttribute.SUPERSCRIPT, 
                          TextAttribute.SUPERSCRIPT_SUPER);
                break;
            case STYLE_SUBSCRIPT: // SUPERSCRIPT_SUB
                attrs.put(TextAttribute.SUPERSCRIPT, 
                          TextAttribute.SUPERSCRIPT_SUB);
                break;
            // Is there a STYLE_SUPERSCRIPT | STYLE_SUBSCRIPT ?
    
            case STYLE_INVISIBLE:
                awt.setSequence("\u0000");
                break;
            case STYLE_REPLACEMENT:
                awt.setSequence("\uFFFC");
                String s = term.getSequence();
                if (replacement != null && replacement.containsKey(s)) {
                    attrs.put(TextAttribute.CHAR_REPLACEMENT, replacement.get(s));
                }
                break;
            
            // Mapped to UNDERLINE
            case STYLE_UNDERLINE: // UNDERLINE_ON
                attrs.put(TextAttribute.UNDERLINE,
                          TextAttribute.UNDERLINE_ON);
                break;
            
            // Mapped to STRIKETHROUGH
            case STYLE_STRIKETHROUGH: // STRIKETHROUGH_ON
                attrs.put(TextAttribute.STRIKETHROUGH, 
                          TextAttribute.STRIKETHROUGH_ON);
                break;
            
            // Mapped to SWAP_COLORS
            case STYLE_REVERSE: // SWAP_COLORS_ON
                attrs.put(TextAttribute.SWAP_COLORS, 
                          TextAttribute.SWAP_COLORS_ON);
                break;
            }
        }
        awt.setTextAttributes(attrs);
        return awt;
    }

    @Override
    public void setCursorLocation(int y, int x) {
        super.setCursorLocation(y, x);
        if (gui != null) {
            gui.moveCursor(y, x);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.core.AbstractTerminal#setPalette(com.googlecode.blacken.colors.ColorPalette)
     */
    @Override
    public ColorPalette setPalette(ColorPalette palette) {
        ColorPalette old = super.setPalette(palette);
        swingColor.clear();
        if (palette != null) {
            for (int c : palette) {
                swingColor.put(c, new Color(c));
            }
        }
        return old;
    }
}
