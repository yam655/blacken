package com.googlecode.blacken.terminal;

import java.util.EnumSet;
import java.util.HashMap;

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.grid.Grid;

public abstract class AbstractTerminal implements TerminalInterface {

    private ColorPalette palette = null;
    private Grid<TerminalCell> grid = null;
    private TerminalCell empty = new TerminalCell();

    private int cursorX = 0;
    private int cursorY = 0;
    private int updateX = 0;
    private int updateY = 0;
    private int curForeground = 0xffffffff;
    private int curBackground = 0xff000000;
    private boolean separateCursor = false;

    public AbstractTerminal() {
        super();
    }
    
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
        TerminalCell cell = null;

        if (what == '\n' || what == BlackenKeys.KEY_ENTER) {
            updateY++;
            updateX = 0;
        } else if (what == '\r') {
            updateX = 0;
        } else if (what == '\b' || what == BlackenKeys.KEY_BACKSPACE) {
            if (updateX > 0) updateX --;
            cell = this.get(updateY, updateX);
            cell.setGlyph("\u0000");
            this.set(updateY, updateX, cell);
        } else if (what == '\t' || what == BlackenKeys.KEY_TAB) {
            updateX = updateX + 8;
            updateX -= updateX % 8;
        } else {
            cell = this.get(updateY, updateX);
            cell.setGlyph(what);
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

    @Override
    public void init() {
        init("Java", 25, 80);
    }

    @Override
    public void clear() {
        cursorX = 0; cursorY = 0;
        updateX = 0; updateY = 0;
        empty.setDirty(false);
        grid.clear(empty);
        empty.setDirty(true);
    }

    @Override
    public void copyFrom(TerminalInterface oterm, int numRows, int numCols, int startY,
                            int startX, int destY, int destX) {
                                grid.copyFrom(oterm.getGrid(), numRows, numCols, startY, startX, destY, destX, 
                                               new TerminalCell().new ResetCell());
                            
                            }

    @Override
    public abstract void disableEventNotice(BlackenEventType event);

    @Override
    public abstract void disableEventNotices();

    @Override
    public abstract void enableEventNotice(BlackenEventType event);

    @Override
    public abstract void enableEventNotices(EnumSet<BlackenEventType> events);

    @Override
    public TerminalCell get(int y, int x) {
        return grid.get(y, x).copy();
    }

    @Override
    public abstract int getch();

    public int getCurBackground() {
        return curBackground;
    }

    public int getCurForeground() {
        return curForeground;
    }

    @Override
    public Grid<TerminalCell> getGrid() {
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
    public String gets(final int length) {
        int cp = -1;
        StringBuffer out;
        if (length < 0) {
            out = new StringBuffer();
        } else {
            out = new StringBuffer(length);
        }
        int i = 0;
        int lastUpX, lastUpY;
        lastUpX = -1; lastUpY = -1;
        while (cp != '\r' && cp != '\t') {
            cp = getch();
            if (cp == '\r') continue;
            if (cp == '\t') continue;
            // XXX add line-editing capabilities
            // When the limit is reached, we don't force a return.
            // We should be able to incorporate some line-editing
            if (length != -1 && i >= length) continue;
            if (Character.isValidCodePoint(cp)) {
                out.append(Character.toChars(cp));
            }
            switch (Character.getType(cp)) {
            case Character.COMBINING_SPACING_MARK:
            case Character.ENCLOSING_MARK:
            case Character.NON_SPACING_MARK:
                mvoverlaych(lastUpY, lastUpX, cp);
                break;
            default:
                lastUpX = updateX; lastUpY = updateY;
                addch(cp);
            }
            i++;
        }
        return out.toString();
    }

    @Override
    public abstract BlackenWindowEvent getwindow();

    @Override
    public int gridMaxX() {
        if (grid == null) return 0;
        return grid.getWidth();
    }

    @Override
    public int gridMaxY() {
        if (grid == null) return 0;
        return grid.getHeight();
    }

    @Override
    public void init(String name, int rows, int cols) {
        if (grid == null) {
            grid = new Grid<TerminalCell>(this.empty, rows, cols);
        } else {
            grid.reset(rows, cols, this.empty);
            
        }
        setCursorLocation(0,0);
        move(0, 0);
    }

    /**
     * @return the separateCursor
     */
    @Override
    public boolean isSeparateCursor() {
        return separateCursor;
    }

    @Override
    public void move(int y, int x) {
        updateX = x; updateY = y;
        if (!separateCursor) setCursorLocation(y, x);
    }

    @Override
    public void moveBlock(int numRows, int numCols, int origY, int origX, 
                          int newY, int newX) {
        grid.moveBlock(numRows, numCols, origY, origX, newY, newX, 
                       new TerminalCell().new ResetCell());
    }

    @Override @Deprecated
    public void moveCursor(int y, int x) {
        setCursorLocation(y, x);
    }
    
    @Override
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
    public void mvaddch(int y, int x, int what) {
        move(y, x);
        addch(what);
    }

    @Override
    public int mvgetch(int y, int x) {
        setCursorLocation(y, x);
        return getch();
    }

    @Override
    public void mvoverlaych(int y, int x, int what) {
        grid.get(y, x).addGlyph(what);
    
    }

    @Override
    public void mvputs(int y, int x, String str) {
        move(y, x);
        puts(str);
    }

    @Override
    public void overlaych(int what) {
        if (updateX > 0) {
            mvoverlaych(updateY, updateX-1, what);
        }
    }

    @Override
    public void puts(String what) {
        int cp;
        int lastUpX = -1, lastUpY = -1;
        for (int i = 0; i < what.codePointCount(0, what.length()); i++) {
            cp = what.codePointAt(i);
            switch (Character.getType(cp)) {
            case Character.COMBINING_SPACING_MARK:
            case Character.ENCLOSING_MARK:
            case Character.NON_SPACING_MARK:
                mvoverlaych(lastUpY, lastUpX, cp);
                break;
            default:
                lastUpX = updateX; lastUpY = updateY;
                addch(cp);
            }
        }
    }

    @Override
    public void quit() {
        /* do nothing */;
    }

    @Override
    public void refresh() {
        /* do nothing */;
    }

    @Override
    public void set(int y, int x, String glyph, 
                    Integer foreground, Integer background, 
                    EnumSet<TerminalStyle> style, EnumSet<CellWalls> walls) {
        TerminalCell tcell = grid.get(y,x);
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
            tcell.setGlyph(glyph);
        }
        tcell.setDirty(true);
    }

    @Override
    public void setCursorLocation(int y, int x) {
        cursorX = x; cursorY = y;
    }

    public void setCurBackground(int c) {
        this.curBackground = c;
    }

    public void setCurForeground(int c) {
        this.curForeground = c;
    }

    /**
     * This sets the internal palette to use for the functions using an
     * index (and for referencing colors by name).
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
                    TerminalCell cell = get(y, x);
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
                    TerminalCell cell = get(y, x);
                    int b = cell.getBackground();
                    int f = cell.getForeground();
                    if (b >= psize && f >= psize) {
                        
                    }
                }
            }
        }
        return oldPalette;
    }

    /**
     * @param separateCursor the separateCursor to set
     */
    @Override
    public void setSeparateCursor(boolean separateCursor) {
        this.separateCursor = separateCursor;
    }

    @Override
    public TerminalCell getEmpty() {
        return empty;
    }

    @Override
    public void setEmpty(TerminalCell empty) {
        this.empty = empty;
    }

}
