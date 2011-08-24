/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
     * Write a character to the current update location
     * @param what codepoint to add to the screen
     */
    public void addch(int what);
    /**
     * Clear the screen.
     */
    public void clear();
    /**
     * Clear the screen with a specific cell value.
     * 
     * @param empty new empty cell value.
     */
    public void clear(TerminalCell empty);
    /**
     * Copy from another TerminalInterface
     * 
     * @param oterm original TerminalInterface
     * @param numRows number of rows to copy
     * @param numCols number of columns to copy
     * @param startY starting Y offset in <code>oterm</code>
     * @param startX starting X offset in <code>oterm</code>
     * @param destY destination Y offset in <code>this</code>
     * @param destX destination X offset in <code>this</code>
     */
    public void copyFrom(TerminalInterface oterm, int numRows, int numCols,
                  int startY, int startX, int destY, int destX);
    /**
     * Disable a type of event notice.
     * 
     * <p>Only <code>event</code> is disabled, other events remain in current 
     * state.</p>
     * 
     * @param event Event type to disable
     */
    public void disableEventNotice(BlackenEventType event);
    /**
     * Disable all event notices
     */
    public void disableEventNotices();
    /**
     * Enable a specific event notice
     * 
     * <p>Only <code>event</code> is enabled, other events remain in current 
     * state.</p>
     * 
     * @param event new event type to enable
     */
    public void enableEventNotice(BlackenEventType event);
    /**
     * Set specific event notices
     * 
     * <p>This changes all event notices to match <code>events</code>.</p>
     *  
     * @param events new event mask
     * TODO change this to setEventNotices
     */
    public void enableEventNotices(EnumSet<BlackenEventType> events);
    /**
     * Get a cell from the terminal
     * 
     * @param y row
     * @param x column
     * @return the terminal cell
     */
    public TerminalCellLike get(int y, int x);
    /**
     * Get a character without visible user-feedback.
     * 
     * @return character returned.
     */
    public int getch();
    /**
     * Get the cursor location
     * 
     * @return cursor location
     */
    public int[] getCursorLocation();

    /**
     * Get the cursor's X location.
     * 
     * @return cursor's X location.
     */
    public int getCursorX();

    /**
     * Get the cursor's Y location.
     * 
     * @return cursor's Y location.
     */
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
    /**
     * Read a string from the screen.
     * 
     * @param length length of string to read
     * @return new string
     */
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
    /**
     * Get the current terminal max X size
     * 
     * @return terminal's max X size
     */
    public int gridMaxX();
    /**
     * Get the current terminal max Y size
     * 
     * @return terminal's max Y size
     */
    public int gridMaxY();

    /**
     * Initialize the terminal
     */
    public void init();
    /**
     * Initialize the terminal with a specific window name and size.
     * 
     * @param name window name
     * @param rows terminal rows
     * @param cols terminal columns
     */
    public void init(String name, int rows, int cols);
    /**
     * Are we using a separate cursor?
     * 
     * @return true if {@link #move(int, int)} does not update cursor
     */
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
    
    /**
     * Move a block of cells
     * 
     * @param numRows number of rows to move
     * @param numCols number of columns to move
     * @param origY origin Y location
     * @param origX origin X location
     * @param newY new Y location
     * @param newX new X location
     */
    public void moveBlock(int numRows, int numCols, int origY, int origX, 
                          int newY, int newX);
    /**
     * Move the update position and write a character.
     * 
     * @param y new Y location
     * @param x new X location
     * @param what codepoint to write
     */
    public void mvaddch(int y, int x, int what);
    /**
     * Move the cursor and get a character
     * 
     * @param y cursor's new Y location
     * @param x cursor's new X location
     * @return codepoint we get from the user
     */
    public int mvgetch(int y, int x);
    /**
     * Overlay a character on to a specific character position.
     * 
     * @param y row
     * @param x column
     * @param what code point
     */
    public void mvoverlaych(int y, int x, int what);
    /**
     * Move the update location and write a string
     * 
     * @param y new Y location
     * @param x new X location
     * @param str string to write
     */
    void mvputs(int y, int x, String str);
    
    /**
     * Overlay a character on to the last character position.
     * 
     * <p>Adds a codepoint to the previous cell (using y, x-1), and 
     * quietly fails if the cursor is now at the beginning of a line.</p>
     *  
     * @param what code point
     */
    public void overlaych(int what);
    /**
     * Write a character sequence to the terminal.
     * 
     * @param what character sequence to write
     */
    public void puts(String what);
    /**
     * Quit the terminal system
     */
    public void quit();
    /**
     * Refresh the terminal window
     */
    public void refresh();
    /**
     * Set a cell to explicit contents
     * 
     * @param y cell Y location
     * @param x cell X location
     * @param sequence character sequence to write
     * @param foreground foreground color index (or value)
     * @param background background color index (or value)
     * @param style cell terminal style
     * @param walls cell walls
     */
    public void set(int y, int x, String sequence, 
                    Integer foreground, Integer background,
                    EnumSet<TerminalStyle> style, EnumSet<CellWalls> walls);
    /**
     * Set a cell to explicit contents, using a CellLike
     * 
     * @param y cell Y location
     * @param x cell X location
     * @param cell example cell
     */
    public void set(int y, int x, TerminalCellLike cell);
    
    /**
     * Set the current background color.
     * 
     * @param color new color index (or value)
     */
    public void setCurBackground(int color);
    /**
     * Set the current foreground color.
     * 
     * @param color new color index (or value)
     */
    public void setCurForeground(int color);
    /**
     * Set the cursor location.
     * 
     * @param y new Y location
     * @param x new X location
     */
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
    /**
     * Separate screen update location from cursor location.
     * 
     * @param separateCursor true to separate; false to join
     */
    public void setSeparateCursor(boolean separateCursor);
    /**
     * Set the background based upon the color name.
     * 
     * @param string color name
     */
    public void setCurBackground(String string);
    /**
     * Set the foreground based upon the color name.
     * 
     * @param string color name
     */
    public void setCurForeground(String string);

}
