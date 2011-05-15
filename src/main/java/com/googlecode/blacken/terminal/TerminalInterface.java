/**
 * 
 */
package com.googlecode.blacken.terminal;

import java.util.EnumSet;

import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.grid.Grid;

/**
 * @author Steven Black
 *
 */
public interface TerminalInterface {

    /**
     * Write a character
     * @param what
     */
    public void addch(int what);
    public void clear();
    public void clear(TerminalCell empty);
    public void copyFrom(TerminalInterface oterm, int numRows, int numCols,
                  int startY, int startX, int destY, int destX);
    public void disableEventNotice(BlackenEventType event);
    public void disableEventNotices();
    public void enableEventNotice(BlackenEventType event);
    public void enableEventNotices(EnumSet<BlackenEventType> events);
    public TerminalCellLike get(int y, int x);
    /**
     * Get a character without visible user-feedback.
     * 
     * @return character returned.
     */
    public int getch();
    public int[] getCursorLocation();

    public int getCursorX();

    public int getCursorY();
    /**
     * Get the template cell used for new and clear cells.
     * 
     * @return template cell
     */
    public TerminalCellLike getEmpty();
    /**
     * Don't depend on this function.
     * 
     * This exists to facilitate the 
     * {@link #copyFrom(TerminalInterface, int, int, int, int, int, int)} 
     * function. It allows direct modification of the underlying grid, but
     * such uses break the visual interface it is bound to.
     * 
     * @return the underlying grid
     */
    public Grid<TerminalCellLike> getGrid();
    /**
     * Get the current locking states/modifiers
     * 
     * If the locking states are available to the interface, this should
     * return them. It may or may not also include the other modifiers.
     * 
     * @return set of locking states enabled
     */
    public EnumSet<BlackenModifier> getLockingStates();
    /**
     * Get the latest mouse event.
     * 
     * This function should only be called after a KEY_MOUSE keycode 
     * is returned by getch().
     * 
     * @return new mouse event
     */
    public BlackenMouseEvent getmouse();
    /**
     * Get a copy of the palette used.
     * 
     * @return a valid ColorPalette object
     */
    public ColorPalette getPalette();
    public String gets(int length);
    /**
     * Get the latest window event.
     * 
     * This function should only be called after a KEY_WINDOW keycode 
     * is returned by getch().
     * 
     * @return new window event
     */
    public BlackenWindowEvent getwindow();
    public int gridMaxX();
    public int gridMaxY();

    public void init();
    public void init(String name, int rows, int cols);
    boolean isSeparateCursor();
    /**
     * Move the invisible output cursor. 
     * <p>
     * To move the visible cursor used for user-input, use the moveCursor 
     * function.
     * 
     * @param y row
     * @param x column
     */
    public void move(int y, int x);
    public void moveBlock(int numRows, int numCols, int origY, int origX, 
                          int newY, int newX);
    public void mvaddch(int y, int x, int what);
    public int mvgetch(int y, int x);
    /**
     * Overlay a character on to a specific character position.
     * 
     * @param y row
     * @param x column
     * @param what code point
     */
    public void mvoverlaych(int y, int x, int what);
    void mvputs(int y, int x, String str);
    /**
     * Overlay a character on to the last character position.
     * <p>
     * Adds a glyph to the previous character (using y, x-1), and quietly fails 
     * if the cursor is now at the beginning of a line. 
     * @param what code point
     */
    public void overlaych(int what);
    public void puts(String what);
    void quit();
    public void refresh();
    public void set(int y, int x, String glyph, 
                    Integer foreground, Integer background,
                    EnumSet<TerminalStyle> style, EnumSet<CellWalls> walls);
    public void set(int y, int x, TerminalCellLike cell);
    public void setCurBackground(int color);
    public void setCurForeground(int color);
    public void setCursorLocation(int y, int x);
    /**
     * Set the template cell used for new and clear cells.
     * 
     * @param empty new empty cell
     */
    public void setEmpty(TerminalCellLike empty);
    /**
     * Set the palette, performing any additional implementation-specific
     * backing logic as needed.
     * 
     * <p>Expect {@link #setPalette(ColorPalette, int, int)} to be called.
     * Also, if your palette does not have true black (0xFF000000) or true
     * white (0xFFffffff) expect these to default to 0 and 1 respectively.</p>
     * 
     * @param palette new palette
     * @return old palette
     */
    public ColorPalette setPalette(ColorPalette palette);
    /**
     * Set the palette, converting existing colors to the palette or to
     * white or black.
     * 
     * @param palette new palette
     * @param white index to use for white
     * @param black index to use for black
     * @return old palette
     */
    public ColorPalette setPalette(ColorPalette palette, int white, int black);
    public void setSeparateCursor(boolean separateCursor);

}
