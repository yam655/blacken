/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
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
package com.googlecode.blacken.terminal;

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.exceptions.InvalidStringFormatException;
import com.googlecode.blacken.grid.Grid;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * An abstract terminal to handle common termainal functions.
 * 
 * @author yam655
 */
public abstract class AbstractTerminal implements TerminalInterface {

    private ColorPalette palette = null;
    private Grid<TerminalCellLike> grid = null;
    private TerminalCellLike empty = new TerminalCell();

    private int cursorX = 0;
    private int cursorY = 0;
    private int updateX = 0;
    private int updateY = 0;
    private int curForeground = 0xffffffff;
    private int curBackground = 0xff000000;
    private boolean separateCursor = false;

    /**
     * Create a new abstract terminal
     */
    public AbstractTerminal() {
        super();
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#addch(int)
     */
    @Override
    public void addch(int what) {
        if (grid == null) {
            this.init();
        }
        if (updateX >= grid.getWidth()) {
            updateX = grid.getWidth() -1;
        }
        if (updateY >= grid.getHeight()) {
            updateY = grid.getHeight() -1;
        }
        TerminalCellLike cell = null;

        if (what == '\n' || what == BlackenKeys.KEY_ENTER) {
            updateY++;
            updateX = 0;
        } else if (what == '\r') {
            updateX = 0;
        } else if (what == '\b' || what == BlackenKeys.KEY_BACKSPACE) {
            if (updateX > 0) updateX --;
            cell = this.get(updateY, updateX);
            cell.setSequence("\u0000");
            this.set(updateY, updateX, cell);
        } else if (what == '\t' || what == BlackenKeys.KEY_TAB) {
            updateX = updateX + 8;
            updateX -= updateX % 8;
        } else {
            cell = this.get(updateY, updateX);
            cell.setSequence(what);
            cell.setForeground(getCurForeground());
            cell.setBackground(getCurBackground());
            this.set(updateY, updateX, cell);
            updateX++;
        }
        
        if (updateX >= grid.getWidth()) {
            updateX = 0;
            updateY++;
        }
        if (updateY >= grid.getHeight()) {
            this.moveBlock(grid.getHeight() -1, grid.getWidth(), 1, 0, 0, 0);
            updateY = grid.getHeight() -1;
        }
        if (!separateCursor) setCursorLocation(updateY, updateX);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#clear()
     */
    @Override
    public void clear() {
        cursorX = 0; cursorY = 0;
        updateX = 0; updateY = 0;
        empty.setDirty(false);
        grid.clear(empty);
        empty.setDirty(true);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#clear(com.googlecode.blacken.terminal.TerminalCell)
     */
    @Override
    public void clear(TerminalCell empty) {
        this.empty.set(empty);
        clear();
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#copyFrom(com.googlecode.blacken.terminal.TerminalInterface, int, int, int, int, int, int)
     */
    @Override
    public void copyFrom(TerminalInterface oterm, int numRows, int numCols, int startY,
                            int startX, int destY, int destX) {
                                grid.copyFrom(oterm.getGrid(), numRows, numCols, startY, startX, destY, destX, 
                                               new TerminalCell().new ResetCell());
                            
                            }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#disableEventNotice(com.googlecode.blacken.terminal.BlackenEventType)
     */
    @Override
    public abstract void disableEventNotice(BlackenEventType event);

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#disableEventNotices()
     */
    @Override
    public abstract void disableEventNotices();

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#enableEventNotice(com.googlecode.blacken.terminal.BlackenEventType)
     */
    @Override
    public abstract void enableEventNotice(BlackenEventType event);

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#enableEventNotices(java.util.EnumSet)
     */
    @Override
    public abstract void enableEventNotices(EnumSet<BlackenEventType> events);

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#get(int, int)
     */
    @Override
    public TerminalCellLike get(int y, int x) {
        return grid.get(y, x);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getch()
     */
    @Override
    public abstract int getch();

    /**
     * Get the current background
     * @return the current background
     */
    public int getCurBackground() {
        return curBackground;
    }

    /**
     * get the current foreground
     * @return the current foreground
     */
    public int getCurForeground() {
        return curForeground;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getCursorLocation()
     */
    @Override
    public int[] getCursorLocation() {
        int[] ret = {cursorY, cursorX};
        return ret;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getCursorX()
     */
    @Override
    public int getCursorX() {
        return cursorX;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getCursorY()
     */
    @Override
    public int getCursorY() {
        return cursorY;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getEmpty()
     */
    @Override
    public TerminalCellLike getEmpty() {
        return empty;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getGrid()
     */
    @Override
    public Grid<TerminalCellLike> getGrid() {
        return this.grid;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getLockingStates()
     */
    @Override
    public abstract EnumSet<BlackenModifier> getLockingStates();

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getmouse()
     */
    @Override
    public abstract BlackenMouseEvent getmouse();

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getPalette()
     */
    @Override
    public ColorPalette getPalette() {
        return palette;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#gets(int)
     */
    @Override
    public String gets(final int length) {
        String ret = TerminalUtils.getString(this, updateY, updateX, length, null);
        if (!separateCursor) setCursorLocation(updateY, updateX);
        return ret;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getwindow()
     */
    @Override
    public abstract BlackenWindowEvent getwindow();

    @Override
    public int getHeight() {
        if (grid == null) return 0;
        return grid.getHeight();
    }

    @Override
    public int getWidth() {
        if (grid == null) return 0;
        return grid.getWidth();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#gridHeight()
     */
    @Override
    @Deprecated
    public int gridHeight() {
        return getHeight();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#gridWidth()
     */
    @Override
    @Deprecated
    public int gridWidth() {
        return getWidth();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#init()
     */
    @Override
    public void init() {
        init("Java", 25, 80);
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#init(java.lang.String, int, int)
     */
    @Override
    public void init(String name, int rows, int cols) {
        if (grid == null) {
            grid = new Grid<TerminalCellLike>(this.empty, rows, cols);
        } else {
            grid.reset(rows, cols, this.empty);
            
        }
        setCursorLocation(0,0);
        move(0, 0);
    }
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#isSeparateCursor()
     */
    @Override
    public boolean isSeparateCursor() {
        return separateCursor;
    }
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#move(int, int)
     */
    @Override
    public void move(int y, int x) {
        updateX = x; updateY = y;
        if (!separateCursor) setCursorLocation(y, x);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#moveBlock(int, int, int, int, int, int)
     */
    @Override
    public void moveBlock(int numRows, int numCols, int origY, int origX, 
                          int newY, int newX) {
        grid.moveBlock(numRows, numCols, origY, origX, newY, newX, 
                       new TerminalCell().new ResetCell());
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#mv(int, int)
     */
    @Override
    public void mv(int y, int x) {
        move(y, x);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#mvaddch(int, int, int)
     */
    @Override
    public void mvaddch(int y, int x, int what) {
        move(y, x);
        addch(what);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#mvgetch(int, int)
     */
    @Override
    public int mvgetch(int y, int x) {
        setCursorLocation(y, x);
        return getch();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#mvoverlaych(int, int, int)
     */
    @Override
    public void mvoverlaych(int y, int x, int what) {
        TerminalCellLike c = grid.get(y, x);
        c.addSequence(what);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#mvputs(int, int, java.lang.String)
     */
    @Override
    public void mvputs(int y, int x, String str) {
        move(y, x);
        puts(str);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#overlaych(int)
     */
    @Override
    public void overlaych(int what) {
        if (updateX > 0) {
            mvoverlaych(updateY, updateX-1, what);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#puts(java.lang.String)
     */
    @Override
    public void puts(String what) {
        int cp;
        int lastUpX = updateX-1, lastUpY = updateY-1;
        for (int i = 0; i < what.codePointCount(0, what.length()); i++) {
            cp = what.codePointAt(i);
            switch (Character.getType(cp)) {
            case Character.COMBINING_SPACING_MARK:
            case Character.ENCLOSING_MARK:
            case Character.NON_SPACING_MARK:
                if (lastUpX >= 0 && lastUpY >= 0) {
                    mvoverlaych(lastUpY, lastUpX, cp);
                }
                break;
            default:
                lastUpX = updateX; lastUpY = updateY;
                addch(cp);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#quit()
     */
    @Override
    public void quit() {
        /* do nothing */;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#refresh()
     */
    @Override
    public void refresh() {
        /* do nothing */;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#set(int, int, java.lang.String, java.lang.Integer, java.lang.Integer, java.util.EnumSet, java.util.EnumSet)
     */
    @Override
    public void set(int y, int x, String glyph, 
                    Integer foreground, Integer background, 
                    EnumSet<TerminalStyle> style, EnumSet<CellWalls> walls) {
        TerminalCellLike tcell = grid.get(y,x);
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
        tcell.setDirty(true);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#setCurBackground(int)
     */
    @Override
    public void setCurBackground(int c) {
        this.curBackground = c;
    }


    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#setCurBackground(java.lang.String)
     */
    @Override
    public void setCurBackground(String c) {
        if (this.palette == null) {
            try {
                this.curBackground = ColorHelper.neverTransparent(ColorHelper.makeColor(c));
            } catch (InvalidStringFormatException e) {
                throw new NullPointerException(
                           String.format("palette is null, and color was invalid: %s",
                                         e.getMessage()));
            }
        } else {
            this.curBackground = this.palette.indexOfKey(c);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#setCurForeground(int)
     */
    @Override
    public void setCurForeground(int c) {
        this.curForeground = c;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#setCurForeground(java.lang.String)
     */
    @Override
    public void setCurForeground(String c) {
        if (this.palette == null) {
            try {
                this.curForeground = ColorHelper.neverTransparent(ColorHelper.makeColor(c));
            } catch (InvalidStringFormatException e) {
                throw new NullPointerException(
                           String.format("palette is null, and color was invalid: %s",
                                         e.getMessage()));
            }
        } else {
            this.curForeground = this.palette.indexOfKey(c);
        }
    }

    @Override
    public void setCursorLocation(int y, int x) {
        cursorX = x; cursorY = y;
    }

    @Override
    public void setCursorLocation(int[] position) {
        cursorX = position[1]; cursorY = position[0];
    }

    @Override
    public void setEmpty(TerminalCellLike empty) {
        this.empty = empty;
    }

    /**
     * Set the palette to use for the colors
     * 
     * <p>If the passed in palette is null, it does not clear out the palette,
     * but instead reparses the existing palette for changes. Implementations
     * may extend this reparsing to perform back-end cleanup.<p>
     * 
     * @param palette the color palette to use
     */
    @Override
    public ColorPalette setPalette(ColorPalette palette) {
        int white = palette.indexOf(0xFFffffff);
        int black = palette.indexOf(0xFF000000);
        if (black == -1) {
            black = 0;
        }
        if (white == -1) {
            white = 1;
        }
        return this.setPalette(palette, white, black);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#setPalette(com.googlecode.blacken.colors.ColorPalette, int, int)
     */
    @Override
    public ColorPalette setPalette(ColorPalette palette, int white, int black) {
        boolean updateGrid = false;
        boolean paletteTruncation = false;
        ColorPalette oldPalette = this.palette;
        if (this.palette == null) {
            updateGrid = true;
        } else if (palette != null) {
            if (palette.size() < this.palette.size()) {
                paletteTruncation = true;
            }
        }
        if (palette == null) {
            palette = this.palette;
        } else {
            this.palette = palette;
        }
        if (updateGrid) {
            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (int y = 0; y < grid.getHeight(); y++) {
                for (int x = 0; x < grid.getWidth(); x++) {
                    TerminalCellLike cell = get(y, x);
                    int b = cell.getBackground();
                    int f = cell.getForeground();
                    int back = -1;
                    int fore = -1;
                    boolean change = false;

                    if (map != null) {
                        // We only add things to the map when it is a direct
                        // mapping to the palette -- as such we can depend upon
                        // the map never growing too large.
                        if (map.containsKey(b)) {
                            back = map.get(b);
                        }
                        if (map.containsKey(f)) {
                            fore = map.get(f);
                        }
                    }
                    if (back == -1) {
                        back = palette.indexOf(b);
                        if (back != -1 && map != null) {
                            map.put(b, back);
                        }
                    }
                    if (fore == -1) {
                        fore = palette.indexOf(f);
                        if (fore != -1 && map != null) {
                            map.put(f, fore);
                        }
                    }
                    if (back == -1 && fore == -1) {
                        back = black;
                        fore = white;
                        change = true;
                    } else if (back == -1) {
                        ColorHelper.makeVisible(fore, 66, white, black);
                        change = true;
                    } else if (fore == -1) {
                        ColorHelper.makeVisible(back, 66, white, black);
                        change = true;
                    }
                    boolean dirty = cell.isDirty();
                    cell.setBackground(back);
                    cell.setForeground(fore);
                    if (!change && !dirty) {
                        // no real change occured
                        cell.setDirty(false);
                    } else {
                        // make sure the back-end gets updated
                        this.set(y, x, cell);
                    }
                }
            }
        } else if (paletteTruncation) {
            int psize = this.palette.size();
            for (int y = 0; y < grid.getHeight(); y++) {
                for (int x = 0; x < grid.getWidth(); x++) {
                    TerminalCellLike cell = get(y, x);
                    int b = cell.getBackground();
                    int f = cell.getForeground();
                    if (b >= psize && f >= psize) {
                        // FIXME this should have something?
                    }
                }
            }
        }
        return oldPalette;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#setSeparateCursor(boolean)
     */
    @Override
    public void setSeparateCursor(boolean separateCursor) {
        this.separateCursor = separateCursor;
    }

}
