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

import java.util.EnumSet;

import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Positionable;

/**
 * The interface for terminal-like views.
 * 
 * @author Steven Black
 */
public interface TerminalInterface {
    /**
     * Set the cell to the value, assigning ownership
     * @since EXPERIMENTAL
     * @param y
     * @param x
     * @param cell
     */
    public TerminalCellLike assign(int y, int x, TerminalCellLike cell);
    /**
     * Clear the screen.
     */
    public void clear();
    /**
     * Clear the screen with a specific cell value.
     * 
     * @param empty new empty cell value.
     */
    public void clear(TerminalCellLike empty);
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
     * get the Grid's bounds
     * @return a concise representation of the bounds
     */
    public Regionlike getBounds();
    /**
     * Get the cursor location
     * 
     * @return cursor location
     * @deprecated migrating to {@link #getCursorPosition()}
     */
    @Deprecated
    public int[] getCursorLocation();
    /**
     * Get cursor position
     * @return cursor location as a concise Positionable
     */
    public Positionable getCursorPosition();
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
     * Get the current terminal max Y size
     * 
     * @return terminal's max Y size
     */
    public int getHeight();

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
    public String getString(int y, int x, int length);

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
    public int getWidth();

    /**
     * Get the current terminal max Y size
     * 
     * @return terminal's max Y size
     * @deprecated Use getHeight() instead.
     */
    @Deprecated
    public int gridHeight();
    /**
     * Get the current terminal max X size
     * 
     * @return terminal's max X size
     * @deprecated Use getWidth() instead.
     */
    @Deprecated
    public int gridWidth();

    /**
     * Initialize the terminal with a specific window name and size.
     * 
     * @param name window name
     * @param rows terminal rows
     * @param cols terminal columns
     */
    public void init(String name, int rows, int cols);

    /**
     * Initialize the terminal with a specific window name and size.
     * 
     * @param name window name
     * @param rows terminal rows
     * @param cols terminal columns
     */
    public void init(String name, int rows, int cols, String font);
    
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
     * Quit the backing window and the application if it was the last one.
     */
    public void quit();
    /**
     * Redraw the terminal
     */
    public void refresh();
    /**
     * Resize the terminal without resizing the window
     * @param rows
     * @param cols 
     */
    public void resize(int rows, int cols);
    /**
     * Set a cell to explicit contents
     * 
     * Any of the arguments can be null. When they are null, that part of the
     * cell remains untouched.
     * 
     * @param y row
     * @param x column
     * @param sequence UTF-16 sequence to display; null to leave untouched
     * @param foreground color index or 0xAARRGGBB value; null to not change
     * @param background color index or 0xAARRGGBB value; null to not change
     * @param style cell terminal style; can be null
     * @param walls cell walls; can be null
     */
    public void set(int y, int x, String sequence, 
                    Integer foreground, Integer background,
                    EnumSet<TerminalStyle> style, EnumSet<CellWalls> walls);

    /**
     * Simplified set cell command.
     * 
     * Any of the arguments can be null. When they are null, that part of the
     * cell remains untouched.
     * 
     * @param y row
     * @param x column
     * @param sequence UTF-16 sequence to display; null to leave untouched
     * @param foreground color index or 0xAARRGGBB value; null to not change
     * @param background color index or 0xAARRGGBB value; null to not change
     */
    public void set(int y, int x, String sequence, Integer foreground, 
            Integer background);
    
    /**
     * Set a cell to explicit contents, using a CellLike
     * 
     * @param y cell Y location
     * @param x cell X location
     * @param cell example cell
     */
    public void set(int y, int x, TerminalCellLike cell);
    /**
     * Set the cursor location.
     * 
     * @param y new Y location
     * @param x new X location
     */
    public void setCursorLocation(int y, int x);
    /**
     * Set the cursor position.
     *
     * @param position [y, x]
     * @deprecated migrating to {@link #setCursorPosition(com.googlecode.blacken.grid.Positionable)}
     */
    @Deprecated
    public void setCursorLocation(int[] position);
    /**
     * Set the cursor position.
     * @param position 
     */
    public void setCursorPosition(Positionable position);
    /**
     * Set the template cell used for new and clear cells.
     * 
     * @param empty new empty cell
     */
    public void setEmpty(TerminalCellLike empty);
    /**
     * Set the font to a name or a path
     * @param font name or path; default MONOSPACE if null
     * @param checkFont when true, do not actually set the font; just check font presence
     * @throws FontNotFoundException requested font was not found
      */
    public void setFont(String font, boolean checkFont) throws FontNotFoundException;
    /**
     * Set the font to a name or a path
     * @param font a set of fonts to try in order; default MONOSPACE if null
     * @return the font used.
     * @throws FontNotFoundException requested font was not found
      */
    public String setFont(String... font) throws FontNotFoundException;

    /**
     * Set the palette, performing any additional implementation-specific
     * backing logic as needed.
     * 
     * <p>If the passed in palette is null, it does not clear out the palette,
     * but instead reparses the existing palette for changes. Implementations
     * may extend this reparsing to perform back-end clean-up. This means that
     * if you modify an in-use palette, you may not see changes until you
     * <code>setPalette(null)</code>.</p>
     * 
     * <p>This will try to find raw colors and assign them to the palette.
     * When shrinking the palette, it will either try to find the color in a
     * different location in the new palette or it will convert the indexed
     * color in to a raw color.</p>
     * 
     * @param palette new palette or null
     * @return old palette
     */
    public ColorPalette setPalette(ColorPalette palette);
    /**
     * Use {@link #coercePalette(ColorPalette, int, int)} instead.
     * 
     * <p>This does a lot more than just set the palette. We made that more 
     * explicit by changing the name.</p>
     * 
     * @param palette
     * @param white
     * @param black
     * @deprecated Use {@link #coercePalette(ColorPalette, int, int)} instead
     * @return 
     */
    @Deprecated
    public ColorPalette setPalette(ColorPalette palette, int white, 
            int black);
    /**
     * Set the palette, converting existing colors to the palette or to
     * white or black.
     * 
     * <p>This makes sure that any color present is guaranteed to be a part
     * of the palette.<p>
     * 
     * @param palette new palette
     * @param white index to use for white or null
     * @param black index to use for black or null
     * @return old palette
     */
    public ColorPalette coercePalette(ColorPalette palette, int white, 
            int black);

    /**
     * This attempts to switch to full-screen mode.
     *
     * <p>Not all TerminalInterface implementations can support FullScreen mode.
     * Some TerminalInterfaces are implicitly FullScreen. The return value is
     * a flag indicating the current FullScreen mode state. It is not an
     * error (or even an exceptional condition) to ignore your request.</p>
     *
     * @param state true to request full-screen; false to request windowed
     * @return true if now full-screen; false if now windowed
     * @since 1.1
     */
    public boolean setFullScreen(boolean state);

    /**
     * Get the current full-screen state.
     * @return true if now full-screen; false if now windowed
     * @since 1.1
     */
    public boolean getFullScreen();
    /**
     * Normally, Alt-Enter (or the like) automatically triggers full-screen
     * mode without application intervention. This prohibits that behavior.
     *
     * <p>Not all TerminalInterfaces may be able to support this behavior.</p>
     *
     * @param state true to inhibit Alt-Enter; false to support it.
     * @since 1.1
     */
    public void inhibitFullScreen(boolean state);

    /**
     * We're entering a land of wrapper interfaces. This function is supposed
     * to return the backing TerminalInterface for wrapper classes.
     * @return
     */
    public TerminalInterface getBackingTerminalInterface();

    /*
     * The "glass" TerminalInterface sits on top of the normal one and should
     * be mostly transparent.
     * @return current glass terminal interface (if any) or <code>null</code>
     * @since COMING IN BLACKEN 2.0
     */
    //public TerminalInterface getGlass();

    /*
     * Create a new "glass" TerminalInterface with the default font.
     *
     * @param rows
     * @param cols
     * @since COMING IN BLACKEN 2.0
     * @return
     */
    //public TerminalInterface initGlass(int rows, int cols);

    /*
     * Creates a translucent terminal interface on top of the current one.
     *
     * <p>A different resolution and font size can be used. This allows big
     * a blocky tile-like font for the map with the other text smaller as
     * well as super tiny font for the map and normal text for the rest.</p>
     *
     * @param rows
     * @param cols
     * @param font
     * @since COMING IN BLACKEN 2.0
     * @return
     */
    //public TerminalInterface initGlass(int rows, int cols, String font);

}
