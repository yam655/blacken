/* blacken - a library for Roguelike games
 * Copyright © 2010-2012 Steven Black <yam655@gmail.com>
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
import com.googlecode.blacken.colors.PaletteTransformer;
import com.googlecode.blacken.colors.transformers.ForcedPaletteTransformer;
import com.googlecode.blacken.colors.transformers.GentlePaletteTransformer;
import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.resources.BlackenConfig;
import com.googlecode.blacken.terminal.editing.SingleLine;
import com.googlecode.blacken.terminal.utils.TerminalUtils;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An abstract terminal to handle common terminal functions.
 * 
 * @author Steven Black
 */
public abstract class AbstractTerminal implements TerminalInterface {

    private ColorPalette palette = null;
    private Grid<TerminalCellLike> grid = null;
    private TerminalCellLike empty = new TerminalCell();
    private int cursorX = -1;
    private int cursorY = -1;
    private boolean is_running = false;

    /**
     * Create a new abstract terminal
     */
    public AbstractTerminal() {
        super();
    }
    
    @Override
    public void clear() {
        cursorX = -1; cursorY = -1;
        empty.setDirty(false);
        grid.clear(empty);
        empty.setDirty(true);
    }

    @Override
    public void clear(TerminalCellLike empty) {
        this.empty.set(empty);
        clear();
    }
    
    @Override
    public void copyFrom(TerminalViewInterface oterm, int numRows, int numCols, int startY,
            int startX, int destY, int destX) {
        grid.copyFrom(oterm.getGrid(), numRows, numCols, startY, startX, destY, destX,
                new TerminalCell.ResetCell());
    }

    @Override
    public abstract void disableEventNotice(BlackenEventType event);

    @Override
    public abstract void disableEventNotices();

    @Override
    public abstract void enableEventNotice(BlackenEventType event);

    @Override
    @Deprecated
    public abstract void enableEventNotices(EnumSet<BlackenEventType> events);

    @Override
    public TerminalCellLike get(int y, int x) {
        return grid.get(y, x);
    }

    @Override
    public abstract int getch();

    @Override
    public abstract int getch(int millis);

    @Override
    public abstract boolean keyWaiting();

    @Override
    @Deprecated
    public int[] getCursorLocation() {
        int[] ret = {cursorY, cursorX};
        return ret;
    }

    @Override
    public int getCursorX() {
        return cursorX;
    }

    @Override
    public int getCursorY() {
        return cursorY;
    }

    @Override
    public Regionlike getBounds() {
        if (grid == null) {
            return new BoxRegion(0,0,0,0);
        }
        return new BoxRegion(grid);
    }
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#getEmpty()
     */
    @Override
    public TerminalCellLike getEmpty() {
        return empty;
    }

    @Override
    public Grid<TerminalCellLike> getGrid() {
        return this.grid;
    }

    @Override
    public abstract EnumSet<BlackenModifier> getLockingStates();

    @Override
    public abstract BlackenMouseEvent getmouse();

    @Override
    public ColorPalette getPalette() {
        return palette;
    }

    @Override
    public String getString(int y, int x, int length) {
        return SingleLine.getString(this, y, x, length, null);
    }

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

    @Override
    @Deprecated
    public int gridHeight() {
        return getHeight();
    }

    @Override
    @Deprecated
    public int gridWidth() {
        return getWidth();
    }
    
    @Override
    public void resize(int rows, int cols) {
        if (this.getCursorX() >= cols || this.getCursorY() >= rows) {
            setCursorLocation(-1,-1);
        }
        getGrid().setSize(rows, cols);
    }

    @Override
    public void init(String name, int rows, int cols) {
        init(name, rows, cols, (TerminalScreenSize)null, (String)null);
    }

    @Override
    public void init(String name, int rows, int cols, TerminalScreenSize size) {
        init(name, rows, cols, size, (String)null);
    }

    @Override
    public void init(String name, int rows, int cols, String... font) {
        init(name, rows, cols, (TerminalScreenSize)null, font);
    }

    @Override
    public void init(String name, int rows, int cols, TerminalScreenSize size, String... font) {
        if (grid == null) {
            grid = new Grid<>(this.empty, rows, cols);
        } else {
            grid.reset(rows, cols, this.empty);
        }
        setCursorLocation(-1,-1);
        is_running = true;
    }

    @Override
    public boolean isRunning() {
        return is_running;
    }

    @Override
    public void moveBlock(int numRows, int numCols, int origY, int origX, 
                          int newY, int newX) {
        grid.moveBlock(numRows, numCols, origY, origX, newY, newX, 
                       new TerminalCell.ResetCell());
    }

    @Override
    public void quit() {
        this.is_running = false;
    }

    @Override
    public void refresh() {
        Grid<TerminalCellLike> grid = getGrid();
        for (int y = grid.getY(); y < grid.getHeight() + grid.getY(); y++) {
            for (int x = grid.getX(); x < grid.getWidth() + grid.getX(); x++) {
                TerminalCellLike cell = get(y, x);
                if (cell == null) {
                    continue;
                }
                if (cell.isDirty()) {
                    refresh(y, x);
                }
            }
        }
    }

    @Override
    abstract public void refresh(int y, int x);

    @Override
    public void set(int y, int x, String sequence, 
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
        if (sequence != null) {
            tcell.setSequence(sequence);
        }
        tcell.setDirty(true);
    }

    @Override
    public void setCursorLocation(int y, int x) {
        cursorX = x; cursorY = y;
    }

    @Override
    public void setEmpty(TerminalCellLike empty) {
        this.empty = empty;
    }
    
    @Override
    public void setFont(String font, boolean checkFont) throws FontNotFoundException {
        throw new FontNotFoundException("Fonts unsupported here");
    }
    @Override
    public String setFont(String... font) throws FontNotFoundException {
        FontNotFoundException lastEx = null;
        String used = null;
        for (String f : font) {
            try {
                setFont(f, false);
                used = f;
                break;
            } catch(FontNotFoundException ex) {
                lastEx = ex;
            }
        }
        if (used == null) {
            throw new FontNotFoundException("None of the requested fonts were found", lastEx);
        }
        return used;
    }

    @Override
    @Deprecated
    public ColorPalette setPalette(ColorPalette palette, int white, int black) {
        return this.coerceToPalette(palette, white, black);
    }

    public static enum CoerceMethod {
        COERCE_NOTHING,
        COERCE_FOREGROUND,
        COERCE_BACKGROUND,
        COERCE_FORE_AND_BACK,
    }

    public ColorPalette setPalette(ColorPalette newPalette, CoerceMethod method, PaletteTransformer transform) {
        ColorPalette oldPalette = this.palette;
        if (transform == null) {
            if (newPalette != null) {
                this.palette = newPalette;
                this.refresh();
            }
            return oldPalette;
        }
        if (newPalette != null) {
            this.palette = newPalette;
            transform.setPalettes(newPalette, oldPalette);
        } else if (oldPalette != transform.getNewPalette()) {
            // We mean != and <em>not</em> !equals()
            transform.setPalettes(newPalette, oldPalette);
        }
        oldPalette = transform.getOldPalette();
        for (int y = grid.getY(); y < grid.getHeight(); y++) {
            for (int x = grid.getX(); x < grid.getWidth(); x++) {
                TerminalCellLike cell = grid.get(y, x);
                int b = cell.getBackground();
                int f = cell.getForeground();
                // take an oldPalette color and transform it in to a newPalette color
                b = transform.transform(b);
                f = transform.transform(f);
                // Always make sure we redraw it.
                if (b == f) {
                    int clr = newPalette.getColor(f);
                    switch(method) {
                        case COERCE_NOTHING:
                            break;
                        case COERCE_FOREGROUND:
                            f = transform.makeVisible(clr, true);
                            break;
                        case COERCE_BACKGROUND:
                            b = transform.makeVisible(clr, false);
                            break;
                        case COERCE_FORE_AND_BACK:
                            f = transform.makeVisible(clr, true);
                            b = transform.makeVisible(clr, false);
                            break;
                    }
                }
                this.set(y, x, null, f, b);
            }
        }
        return oldPalette;
    }

    @Override
    @Deprecated
    public ColorPalette setPalette(ColorPalette palette) {
        return setPalette(palette, CoerceMethod.COERCE_NOTHING, new GentlePaletteTransformer());
    }

    @Override
    @Deprecated
    public ColorPalette coerceToPalette(ColorPalette palette, String white,
            String black) {
        return setPalette(palette, CoerceMethod.COERCE_FOREGROUND,
                new ForcedPaletteTransformer(white, black));
    }

    @Override
    @Deprecated
    public ColorPalette coerceToPalette(ColorPalette palette, Integer white,
            Integer black) {
        return setPalette(palette, CoerceMethod.COERCE_FOREGROUND,
                new ForcedPaletteTransformer(white, black));
    }

    @Override
    public TerminalCellLike assign(int y, int x, TerminalCellLike tcell) {
        return this.getGrid().set(y, x, tcell);
    }

    @Override
    public void set(int y, int x, String sequence, Integer foreground, Integer background) {
        this.set(y, x, sequence, foreground, background, null, null);
    }

    @Override
    @Deprecated
    public void setCursorLocation(int[] lastCursor) {
        this.setCursorLocation(lastCursor[0], lastCursor[1]);
    }

    @Override
    public Positionable getCursorPosition() {
        return new Point(cursorY, cursorX);
    }

    @Override
    public void setCursorPosition(Positionable position) {
        this.setCursorLocation(position.getY(), position.getX());
    }

    @Override
    public boolean setFullScreen(boolean state) {
        return getFullScreen();
    }

    @Override
    public abstract boolean getFullScreen();

    @Override
    public void inhibitFullScreen(boolean state) {
        if (state) {
            // by default we do nothing -- but we don't want IDEs to complain
        }
    }

    @Override
    @Deprecated
    public TerminalInterface getBackingTerminalInterface() {
        return this;
    }

    @Override
    public TerminalInterface getBackingTerminal() {
        return this;
    }

    @Override
    public TerminalViewInterface getBackingTerminalView() {
        return this;
    }

    @Override
    public void setSize(TerminalScreenSize size) {
        if (size == null) {
            // don't let Netbeans complain
        }
        // doing nothing is safe/valid for the default
    }
    @Override
    public void setBounds(Regionlike bounds) {
        this.resize(bounds.getHeight(), bounds.getWidth());
        this.getGrid().setPosition(bounds.getY(), bounds.getY());
    }

    @Override
    public void setBounds(int rows, int cols, int y1, int x1) {
        this.resize(rows, cols);
        this.getGrid().setPosition(y1, x1);
    }

    @Override
    public void doUpdate() {
        int i = 0;
        if (i > 0) {
            // The default implementation should do nothing.
            // Silence the NetBeans warning.
        }
    }

    @Override
    public Positionable putString(int y, int x, String string) {
        return SingleLine.putString(this, new Point(y, x), null, string, null);
    }

    @Override
    public Positionable putString(Positionable pos, String string) {
        return SingleLine.putString(this, pos, null, string, null);
    }

    @Override
    public void applyTemplate(int y, int x, TerminalCellTemplate template, int length) {
        TerminalUtils.applyTemplate(this, y, x, template, length);
    }

    @Override
    public int getX() {
        return 0;
    }
    @Override
    public int getY() {
        return 0;
    }

    @Override
    public BlackenConfig overrideConfig(String gameName) {
        BlackenConfig ret = new BlackenConfig();
        if (gameName != null) {
            ret.override(gameName);
        }
        return ret;
    }

}
