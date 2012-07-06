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

import java.awt.Color;
import java.awt.Font;
import java.awt.font.GraphicAttribute;
import java.awt.font.TextAttribute;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.terminal.AbstractTerminal;
import com.googlecode.blacken.terminal.BlackenEventType;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenModifier;
import com.googlecode.blacken.terminal.BlackenMouseEvent;
import com.googlecode.blacken.terminal.BlackenWindowEvent;
import com.googlecode.blacken.terminal.CellWalls;
import com.googlecode.blacken.terminal.TerminalCell;
import com.googlecode.blacken.terminal.TerminalCell.ResetCell;
import com.googlecode.blacken.terminal.TerminalCellLike;
import com.googlecode.blacken.terminal.TerminalInterface;
import com.googlecode.blacken.terminal.TerminalStyle;

/**
 *
 * @author yam655
 */
public class ReducedSwingTerminalV2 extends AbstractTerminal {
    private BlackenPanelV2 gui;
    private ReducedSwingTerminalV2 term;
    protected HashMap<Integer, Color> swingColor = new HashMap<>();
    protected HashMap<String, GraphicAttribute> replacement = null;

    public ReducedSwingTerminalV2() {
        super();
        this.gui = null;
        this.term = this;
    }

    public ReducedSwingTerminalV2(BlackenPanelV2 gui) {
        super();
        this.gui = gui;
        this.term = this;
    }

    public ReducedSwingTerminalV2(ReducedSwingTerminalV2 term, BlackenPanelV2 gui) {
        super();
        this.gui = gui;
        this.term = term;
        swingColor = null;
        replacement = null;
    }

    protected void setGui(BlackenPanelV2 gui) {
        this.gui = gui;
    }
    protected BlackenPanelV2 getGui() {
        return gui;
    }

    @Override
    public TerminalCellLike assign(int y, int x, TerminalCellLike tcell) {
        AwtCell acell = gui.get(y, x);
        AwtCell r = this.setAwtFromTerminal(acell, tcell);
        if (acell == null) {
            gui.set(y, x, r);
        }
        Grid<TerminalCellLike> grid = getGrid();
        tcell.setDirty(false);
        return grid.set(y, x, tcell);
    }

    @Override
    public void clear() {
        super.clear();
        Font f = gui.getEmpty().getFont();
        AwtCell awtempty = setAwtFromTerminal(gui.getEmpty(), getEmpty());
        awtempty.setDirty(true);
        awtempty.setFont(f);
        gui.setEmpty(awtempty);
        gui.clear();
    }

    @Override
    public void copyFrom(TerminalInterface oterm, int numRows, int numCols, int startY, int startX, int destY, int destX) {
        if (oterm == this) {
            this.moveBlock(numRows, numCols, startY, startX, destY, destX);
        } else {
            getGrid().copyFrom(oterm.getGrid(), numRows, numCols, startY, startX, destY, destX, new TerminalCell().new ResetCell());
            forceRefresh(numRows, numCols, destY, destX);
        }
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

    protected Color getSwingColor(int c) {
        Color clr;
        ColorPalette palette = getPalette();
        if (palette != null) {
            c = palette.getColor(c);
        }
        if (term.swingColor.containsKey(c)) {
            clr = term.swingColor.get(c);
        } else {
            clr = new Color(c, true);
            term.swingColor.put(c, clr);
        }
        return clr;
    }

    /**
     * We do not cache the entire dim palette at palette-load as it isn't
     * expected that many applications will make use of it.
     *
     * @param color standard (opaque) color in an
     * @return
     */
    protected int makeDim(final int color) {
        return ColorHelper.increaseAlpha(color, -0.2);
    }

    @Override
    public void moveBlock(int numRows, int numCols, int origY, int origX, int newY, int newX) {
        getGrid().moveBlock(numRows, numCols, origY, origX, newY, newX, new TerminalCell().new ResetCell());
        gui.moveBlock(numRows, numCols, origY, origX, newY, newX);
    }

    @Override
    public void refresh() {
        gui.refresh();
    }

    @Override
    public void init(String name, int rows, int cols, String font) {
        super.init(name, rows, cols, font);
    }

    @Override
    public void resize(int rows, int cols) {
        if (this.getCursorX() >= cols || this.getCursorY() >= rows) {
            setCursorLocation(-1, -1);
        }
        getGrid().setSize(rows, cols);
        gui.resizeGrid(rows, cols);
        gui.windowResized();
    }

    @Override
    public void set(int y, int x, String glyph, Integer foreground, Integer background, EnumSet<TerminalStyle> style, EnumSet<CellWalls> walls) {
        TerminalCellLike tcell = getGrid().get(y, x);
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
            awt.set(null);
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
        if (term == null) {
            throw new NullPointerException();
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
                attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRA_LIGHT);
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
        for (TerminalStyle style : styles) {
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
                case STYLE_SUPERSCRIPT:
                    // SUPERSCRIPT_SUPER
                    attrs.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
                    break;
                case STYLE_SUBSCRIPT:
                    // SUPERSCRIPT_SUB
                    attrs.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
                    break;
                    // Is there a STYLE_SUPERSCRIPT | STYLE_SUBSCRIPT ?
                case STYLE_INVISIBLE:
                    awt.setSequence("\u0000"); //$NON-NLS-1$
                    break;
                case STYLE_REPLACEMENT:
                    awt.setSequence("\ufffc"); //$NON-NLS-1$
                    String s = term.getSequence();
                    if (this.term.replacement != null && this.term.replacement.containsKey(s)) {
                        attrs.put(TextAttribute.CHAR_REPLACEMENT, this.term.replacement.get(s));
                    }
                    break;
            // Mapped to UNDERLINE
                case STYLE_UNDERLINE:
                    // UNDERLINE_ON
                    attrs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    break;
            // Mapped to STRIKETHROUGH
                case STYLE_STRIKETHROUGH:
                    // STRIKETHROUGH_ON
                    attrs.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                    break;
            // Mapped to SWAP_COLORS
                case STYLE_REVERSE:
                    // SWAP_COLORS_ON
                    attrs.put(TextAttribute.SWAP_COLORS, TextAttribute.SWAP_COLORS_ON);
                    break;
                case STYLE_DIM:
                    /* handled elsewhere */
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

    @Override
    public void setFont(String font) {
        if (font == null) {
            font = Font.MONOSPACED;
        }
        Font fontObj = new Font(font, Font.PLAIN, 1);
        gui.setFont(fontObj);
    }

    @Override
    public ColorPalette setPalette(ColorPalette palette) {
        if (this != this.term) {
            throw new UnsupportedOperationException("Set the palette on the primary terminal interface");
        }
        ColorPalette old = super.setPalette(palette);
        term.swingColor.clear();
        if (palette != null) {
            for (int c : palette) {
                term.swingColor.put(c, new Color(c, true));
            }
        }
        return old;
    }

    @Override
    public void disableEventNotice(BlackenEventType event) {
        throw new UnsupportedOperationException("Use the primary terminal interface");
    }

    @Override
    public void disableEventNotices() {
        throw new UnsupportedOperationException("Use the primary terminal interface");
    }

    @Override
    public void enableEventNotice(BlackenEventType event) {
        throw new UnsupportedOperationException("Use the primary terminal interface");
    }

    @Override
    public void enableEventNotices(EnumSet<BlackenEventType> events) {
        throw new UnsupportedOperationException("Use the primary terminal interface");
    }

    @Override
    public int getch() {
        if (this.term != this) {
            return this.term.getch();
        }
        return BlackenKeys.NO_KEY;
    }

    @Override
    public EnumSet<BlackenModifier> getLockingStates() {
        if (this.term != this) {
            return this.term.getLockingStates();
        }
        return null;
    }

    @Override
    public BlackenMouseEvent getmouse() {
        if (this.term != this) {
            return this.term.getmouse();
        }
        return null;
    }

    @Override
    public BlackenWindowEvent getwindow() {
        if (this.term != this) {
            return this.term.getwindow();
        }
        return null;
    }

    @Override
    public boolean getFullScreen() {
        if (this.term != this) {
            return this.term.getFullScreen();
        }
        return false;
    }

    public void windowResized() {
        gui.windowResized();
        getGrid().setBounds(gui.getGridBounds());
    }

    @Override
    public TerminalInterface getGlass() {
        if (this.term != this) {
            return this;
        }
        return null;
    }

    @Override
    public TerminalInterface initGlass(int rows, int cols, String font) {
        if (this.term != this) {
            this.init("Java", rows, cols, font);
            return this;
        }
        throw new UnsupportedOperationException("Does not make sense.");
    }
}
