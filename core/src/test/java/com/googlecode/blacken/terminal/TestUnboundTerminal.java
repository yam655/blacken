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
package com.googlecode.blacken.terminal;

import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.core.Coverage;
import com.googlecode.blacken.core.Covers;
import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.Regionlike;
import java.util.EnumSet;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Steven Black
 */
public class TestUnboundTerminal {

    private TerminalInterface terminal;
    static private int NUM_ROWS = 10;
    static private int NUM_COLS = 10;
    static private String EMPTY_SEQUENCE = ".";

    /**
     * setup the test
     */
    @Before
    public void setUp() {
        setUp(new UnboundTerminal());
    }

    public void setUp(TerminalInterface terminal) {
        this.terminal = terminal;
        TerminalCell empty = new TerminalCell(EMPTY_SEQUENCE);
        this.terminal.init(null, NUM_ROWS, NUM_COLS);
        this.terminal.clear(empty);
    }

    @Test
    @Covers("public TerminalCellLike assign(int,int,TerminalCellLike)")
    public void assign() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());

        TerminalCellLike replacement = terminal.getEmpty().clone();
        TerminalCellLike oldSpot = terminal.get(0, 0);
        assertNotSame(oldSpot, replacement);
        terminal.assign(0, 0, replacement);
        TerminalCellLike newSpot = terminal.get(0, 0);
        assertSame(newSpot, replacement);

        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public UnboundTerminal()")
    public void constructor() {
        UnboundTerminal ut = new UnboundTerminal();
        Regionlike b = ut.getBounds();
        assertEquals(0, b.getY());
        assertEquals(0, b.getX());
        assertEquals(0, b.getHeight());
        assertEquals(0, b.getWidth());
        assertEquals(-1, ut.getCursorX());
        assertEquals(-1, ut.getCursorY());
    }

    @Test
    @Covers("public static UnboundTerminal initialize(String,int,int,String)")
    public void initialize() {
        UnboundTerminal term = UnboundTerminal.initialize("Java", NUM_ROWS,
                                                          NUM_COLS, "Monospace");
        assertNotNull(term.getGrid());
        assertEquals(NUM_ROWS, term.getHeight());
        assertEquals(NUM_COLS, term.getWidth());
        assertEquals(-1, term.getCursorX());
        assertEquals(-1, term.getCursorY());
    }

    @Test
    @Covers("public void clear()")
    public void clear() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                terminal.get(y, x).setSequence("A");
            }
        }
        terminal.clear();
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                assertEquals(EMPTY_SEQUENCE, terminal.get(y, x).getSequence());
            }
        }
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
    }

    @Test
    @Covers("public void clear(TerminalCellLike)")
    public void clear_empty() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                assertEquals(EMPTY_SEQUENCE, terminal.get(y, x).getSequence());
            }
        }
        terminal.clear(new TerminalCell("A"));
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                assertEquals("A", terminal.get(y, x).getSequence());
            }
        }
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
    }

    @Test
    @Covers({"public ColorPalette coerceToPalette(ColorPalette,Integer,Integer)",
             "public ColorPalette setPalette(ColorPalette,int,int)",
            })
    public void coerceToPalette_palette_num_num() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        assertNull(terminal.getPalette());
        ColorPalette palette = new ColorPalette();
        palette.add(0xFF000000);
        palette.add(0xFF808080);
        palette.add(0xFF800000);
        palette.add(0xFF008000);
        palette.add(0xFF000080);
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                terminal.set(y, x, null, palette.get(x % 5), palette.get(y % 5));
            }
        }
        terminal.coerceToPalette(palette, 0, 1);
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                terminal.set(y, x, null, x % 5, y % 5);
            }
        }
        assertSame(palette, terminal.getPalette());
        assertEquals(5, palette.size());
        assertEquals(0xFF000000, (int)palette.get(0));
        assertEquals(0xFF808080, (int)palette.get(1));
        assertEquals(0xFF800000, (int)palette.get(2));
        assertEquals(0xFF008000, (int)palette.get(3));
        assertEquals(0xFF000080, (int)palette.get(4));
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers({"public ColorPalette coerceToPalette(ColorPalette,String,String)",
            })
    public void coerceToPalette_palette_name_name() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        assertNull(terminal.getPalette());
        ColorPalette palette = new ColorPalette();
        palette.add("black", 0xFF000000);
        palette.add("white", 0xFF808080);
        palette.add(0xFF800000);
        palette.add(0xFF008000);
        palette.add(0xFF000080);
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                terminal.set(y, x, null, palette.get(x % 5), palette.get(y % 5));
            }
        }
        terminal.coerceToPalette(palette, "black", "white");
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                terminal.set(y, x, null, x % 5, y % 5);
            }
        }
        assertSame(palette, terminal.getPalette());
        assertEquals(5, palette.size());
        assertEquals(0xFF000000, (int)palette.get(0));
        assertEquals(0xFF808080, (int)palette.get(1));
        assertEquals(0xFF800000, (int)palette.get(2));
        assertEquals(0xFF008000, (int)palette.get(3));
        assertEquals(0xFF000080, (int)palette.get(4));
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public void copyFrom(TerminalViewInterface,int,int,int,int,int,int)")
    public void copyFrom() {
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
        TerminalInterface original = new UnboundTerminal();
        original.init(null, NUM_ROWS, NUM_COLS);
        original.clear(new TerminalCell("Z"));
        terminal.copyFrom(original, NUM_ROWS, NUM_COLS, 0, 0, 0, 0);
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
    }

    @Test
    @Covers("public TerminalCellLike get(int,int)")
    public void get() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                terminal.get(y, x).setSequence(y * (NUM_COLS+1) + x);
            }
        }
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                assertEquals(
                    String.valueOf(Character.toChars(y * (NUM_COLS + 1) + x)),
                    terminal.get(y, x).getSequence());
            }
        }
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Deprecated
    @Covers("public TerminalInterface getBackingTerminalInterface()")
    public void getBackingTerminalInterface() {
        assertNotNull(terminal.getBackingTerminalInterface());
        assertSame(terminal, terminal.getBackingTerminalInterface());
    }

    @Test
    @Covers("public TerminalInterface getBackingTerminal()")
    public void getBackingTerminal() {
        assertNotNull(terminal.getBackingTerminal());
        assertSame(terminal, terminal.getBackingTerminal());
    }

    @Test
    @Covers("public TerminalViewInterface getBackingTerminalView()")
    public void getBackingTerminalView() {
        assertNotNull(terminal.getBackingTerminalView());
        assertSame(terminal, terminal.getBackingTerminalView());
    }

    @Test
    @Covers("public Regionlike getBounds()")
    public void getBounds() {
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
        Regionlike bounds = terminal.getBounds();
        Regionlike boxregion = new BoxRegion(NUM_ROWS, NUM_COLS, 0, 0);
        assertNotSame(bounds, terminal);
        assertEquals(boxregion, bounds);
        assertEquals(NUM_ROWS, bounds.getHeight());
        assertEquals(NUM_COLS, bounds.getWidth());
        assertEquals(0, bounds.getY());
        assertEquals(0, bounds.getX());
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
    }

    @Test
    @Covers("public int getch()")
    public void getch() {
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
        assertEquals(BlackenKeys.NO_KEY, terminal.getch());
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
    }

    @Test
    @Covers("public int getch(int)")
    public void getch_int() {
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
        assertEquals(BlackenKeys.NO_KEY, terminal.getch(1000));
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
    }

    @Test
    @Covers("public boolean keyWaiting()")
    public void keyWaiting() {
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
        assertFalse(terminal.keyWaiting());
        assertEquals(-1, terminal.getCursorX());
        assertEquals(-1, terminal.getCursorY());
    }

    @Test
    @Covers("public int[] getCursorLocation()")
    @Deprecated
    public void getCursorLocation() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        int[] expected = {1, 2};
        terminal.setCursorLocation(expected);
        int[] loc = terminal.getCursorLocation();
        assertArrayEquals(expected, loc);
    }

    @Test
    @Covers("public Positionable getCursorPosition()")
    public void getCursorPosition() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        Point expected = new Point(1, 2);
        terminal.setCursorPosition(expected);
        Positionable loc = terminal.getCursorPosition();
        assertEquals(expected, loc);
    }

    @Test
    @Covers("public int getCursorX()")
    public void getCursorX() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        terminal.setCursorLocation(1, 2);
        assertEquals(1, terminal.getCursorY());
        assertEquals(2, terminal.getCursorX());
    }

    @Test
    @Covers("public int getCursorY()")
    public void getCursorY() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        terminal.setCursorLocation(1, 2);
        assertEquals(1, terminal.getCursorY());
        assertEquals(2, terminal.getCursorX());
    }

    @Test
    @Covers("public TerminalCellLike getEmpty()")
    public void getEmpty() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        TerminalCellLike empty = terminal.getEmpty();
        assertEquals(EMPTY_SEQUENCE, empty.getSequence());
        terminal.clear(new TerminalCell(EMPTY_SEQUENCE));
        assertSame(empty, terminal.getEmpty());
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public Grid<TerminalCellLike> getGrid()")
    public void getGrid() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        assertEquals(NUM_ROWS, terminal.getHeight());
        assertEquals(NUM_COLS, terminal.getWidth());
        Grid<TerminalCellLike> grid = terminal.getGrid();
        assertEquals(NUM_ROWS, grid.getHeight());
        assertEquals(NUM_COLS, grid.getWidth());
        terminal.resize(NUM_ROWS+2, NUM_ROWS+2);
        assertSame(grid, terminal.getGrid());
        assertEquals(NUM_ROWS+2, terminal.getHeight());
        assertEquals(NUM_COLS+2, terminal.getWidth());
        assertEquals(NUM_ROWS+2, grid.getHeight());
        assertEquals(NUM_COLS+2, grid.getWidth());
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers({"public int gridHeight()", "public int getHeight()"})
    public void getHeight() {
        assertEquals(NUM_ROWS, terminal.getHeight());
        assertEquals(terminal.getHeight(), terminal.getGrid().getHeight());
    }

    @Test
    @Covers("public ColorPalette getPalette()")
    public void getPalette() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        assertNull(terminal.getPalette());
        ColorPalette palette = new ColorPalette();
        palette.add(0xFF000000);
        palette.add(0xFF808080);
        palette.add(0xFF800000);
        palette.add(0xFF008000);
        palette.add(0xFF000080);
        terminal.coerceToPalette(palette, 0xFF808080, 0xFF000000);
        assertSame(palette, terminal.getPalette());
        assertEquals(5, palette.size());
        assertEquals(0xFF000000, (int)palette.get(0));
        assertEquals(0xFF808080, (int)palette.get(1));
        assertEquals(0xFF800000, (int)palette.get(2));
        assertEquals(0xFF008000, (int)palette.get(3));
        assertEquals(0xFF000080, (int)palette.get(4));
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public String getString(int,int,int)")
    public void getString() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        assertEquals("", terminal.getString(1, 2, 5));
        assertEquals(1, terminal.getCursorY());
        assertEquals(2, terminal.getCursorX());
    }

    @Test
    @Covers({"public int gridWidth()", "public int getWidth()"})
    public void getWidth() {
        assertEquals(NUM_ROWS, terminal.getWidth());
        assertEquals(terminal.getWidth(), terminal.getGrid().getWidth());
    }

    @Test
    @Covers("public void init(String,int,int)")
    public void init_nameRowsCols() {
        UnboundTerminal term = new UnboundTerminal();
        assertNull(term.getGrid());
        assertEquals(0, term.getHeight());
        assertEquals(0, term.getWidth());
        term.init("Java", NUM_ROWS+1, NUM_COLS+1);
        assertNotNull(term.getGrid());
        assertEquals(NUM_ROWS+1, term.getHeight());
        assertEquals(NUM_COLS+1, term.getWidth());
        assertEquals(-1, term.getCursorX());
        assertEquals(-1, term.getCursorY());
    }

    @Test
    @Covers("public void init(String,int,int,String...)")
    public void init_nameRowsColsFont() {
        UnboundTerminal term = new UnboundTerminal();
        assertNull(term.getGrid());
        assertEquals(0, term.getHeight());
        assertEquals(0, term.getWidth());
        term.init("Java", NUM_ROWS+1, NUM_COLS+1, "Monospace");
        assertNotNull(term.getGrid());
        assertEquals(NUM_ROWS+1, term.getHeight());
        assertEquals(NUM_COLS+1, term.getWidth());
        assertEquals(-1, term.getCursorX());
        assertEquals(-1, term.getCursorY());
    }

    @Test
    @Covers("public void init(String,int,int,TerminalScreenSize)")
    public void init_nameRowsColsSize() {
        UnboundTerminal term = new UnboundTerminal();
        assertNull(term.getGrid());
        assertEquals(0, term.getHeight());
        assertEquals(0, term.getWidth());
        term.init("Java", NUM_ROWS+1, NUM_COLS+1, TerminalScreenSize.SIZE_LARGE);
        assertNotNull(term.getGrid());
        assertEquals(NUM_ROWS+1, term.getHeight());
        assertEquals(NUM_COLS+1, term.getWidth());
        assertEquals(-1, term.getCursorX());
        assertEquals(-1, term.getCursorY());
    }

    @Test
    @Covers("public void init(String,int,int,TerminalScreenSize,String...)")
    public void init_nameRowsColsSizeFont() {
        UnboundTerminal term = new UnboundTerminal();
        assertNull(term.getGrid());
        assertEquals(0, term.getHeight());
        assertEquals(0, term.getWidth());
        term.init("Java", NUM_ROWS+1, NUM_COLS+1, TerminalScreenSize.SIZE_SMALL, "Monospace");
        assertNotNull(term.getGrid());
        assertEquals(NUM_ROWS+1, term.getHeight());
        assertEquals(NUM_COLS+1, term.getWidth());
        assertEquals(-1, term.getCursorX());
        assertEquals(-1, term.getCursorY());
    }

    @Test
    @Covers("public void moveBlock(int,int,int,int,int,int)")
    public void moveBlock() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                assertEquals(EMPTY_SEQUENCE, terminal.get(y, x).getSequence());
            }
        }
        for (int x = 0; x < NUM_COLS; x++) {
            terminal.get(0, x).setSequence(0x30+x);
            assertEquals(0x30+x, terminal.get(0, x).getSequence().codePointAt(0));
        }
        terminal.moveBlock(1, NUM_COLS, 0, 0, 2, 0);
        for (int x = 0; x < NUM_COLS; x++) {
            assertEquals(EMPTY_SEQUENCE, terminal.get(0, x).getSequence());
            assertEquals(0x30+x, terminal.get(2, x).getSequence().codePointAt(0));
        }
        terminal.moveBlock(1, 1, 2, 0, 2, 2);
        assertEquals(EMPTY_SEQUENCE, terminal.get(2, 0).getSequence());
        assertEquals(0x30, terminal.get(2, 2).getSequence().codePointAt(0));
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public void refresh()")
    public void refresh() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        assertFalse(terminal.get(0, 0).isDirty());
        assertFalse(terminal.get(0, 1).isDirty());
        assertFalse(terminal.get(0, 2).isDirty());
        assertFalse(terminal.get(0, 3).isDirty());
        terminal.get(0, 0).setSequence("N");
        terminal.get(0, 1).setSequence("i");
        terminal.get(0, 2).setSequence("c");
        terminal.get(0, 3).setSequence("e");
        assertTrue(terminal.get(0, 0).isDirty());
        assertTrue(terminal.get(0, 1).isDirty());
        assertTrue(terminal.get(0, 2).isDirty());
        assertTrue(terminal.get(0, 3).isDirty());
        terminal.refresh();
        assertFalse(terminal.get(0, 0).isDirty());
        assertFalse(terminal.get(0, 1).isDirty());
        assertFalse(terminal.get(0, 2).isDirty());
        assertFalse(terminal.get(0, 3).isDirty());
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public void resize(int,int)")
    public void resize_y_x() {
        terminal.resize(NUM_ROWS+1, NUM_COLS+1);
        assertEquals(NUM_ROWS+1, terminal.getHeight());
        assertEquals(NUM_COLS+1, terminal.getWidth());
        TerminalCellLike cleanEmpty = terminal.getEmpty().clone();
        cleanEmpty.setDirty(false);
        assertEquals(cleanEmpty, terminal.get(NUM_ROWS, NUM_COLS));
    }

    @Test
    @Covers("public void set(int,int,String,Integer,Integer,EnumSet<TerminalStyle>,EnumSet<CellWalls>)")
    public void set() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        terminal.set(0, 0, "A", 0xed123456, 0xab098765, 
                EnumSet.of(TerminalStyle.STYLE_BOLD), 
                EnumSet.of(CellWalls.BOTTOM));
        TerminalCellLike cell = terminal.get(0, 0);
        assertEquals("A", cell.getSequence());
        assertEquals(0xed123456, cell.getForeground());
        assertEquals(0xab098765, cell.getBackground());
        assertEquals(EnumSet.of(TerminalStyle.STYLE_BOLD), cell.getStyle());
        assertEquals(EnumSet.of(CellWalls.BOTTOM), cell.getCellWalls());
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public void setCursorLocation(int,int)")
    public void setCursorLocation() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        terminal.setCursorLocation(1, 2);
        assertEquals(1, terminal.getCursorY());
        assertEquals(2, terminal.getCursorX());
    }

    @Test
    @Deprecated
    @Covers("public void setCursorLocation(int[])")
    public void setCursorLocation_intArr() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        int[] loc = {1, 2};
        terminal.setCursorLocation(loc);
        assertEquals(1, terminal.getCursorY());
        assertEquals(2, terminal.getCursorX());
    }

    @Test
    @Covers("public void setCursorPosition(Positionable)")
    public void setCursorPosition_positionable() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        terminal.setCursorPosition(new Point(1, 2));
        assertEquals(1, terminal.getCursorY());
        assertEquals(2, terminal.getCursorX());
    }

    @Test
    @Covers("public void setEmpty(TerminalCellLike)")
    public void setEmpty() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                assertEquals(EMPTY_SEQUENCE, terminal.get(y, x).getSequence());
            }
        }
        terminal.setEmpty(new TerminalCell("A"));
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                assertEquals(EMPTY_SEQUENCE, terminal.get(y, x).getSequence());
            }
        }
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public ColorPalette setPalette(ColorPalette)")
    public void setPalette_palette() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        assertNull(terminal.getPalette());
        ColorPalette palette = new ColorPalette();
        palette.add(0xFF000000);
        palette.add(0xFF808080);
        palette.add(0xFF800000);
        palette.add(0xFF008000);
        palette.add(0xFF000080);
        terminal.setPalette(palette);
        assertSame(palette, terminal.getPalette());
        assertEquals(5, palette.size());
        assertEquals(0xFF000000, (int)palette.get(0));
        assertEquals(0xFF808080, (int)palette.get(1));
        assertEquals(0xFF800000, (int)palette.get(2));
        assertEquals(0xFF008000, (int)palette.get(3));
        assertEquals(0xFF000080, (int)palette.get(4));
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }
    
    @Test
    @Covers("public void set(int,int,TerminalCellLike)")
    public void set_Y_X_TerminalCell() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        terminal.set(0, 0, new TerminalCell("A"));
        terminal.set(NUM_ROWS-1, 0, new TerminalCell("B"));
        terminal.set(0, NUM_COLS-1, new TerminalCell("C"));
        terminal.set(NUM_ROWS-1, NUM_COLS-1, new TerminalCell("D"));
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        verifySet();
    }

    @Test
    @Covers("public void set(int,int,String,Integer,Integer)")
    public void set_y_x_Sequence_fg_bg() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        terminal.set(0, 0, "A", null, null);
        terminal.set(NUM_ROWS-1, 0, "B", null, null);
        terminal.set(0, NUM_COLS-1, "C", null, null);
        terminal.set(NUM_ROWS-1, NUM_COLS-1, "D", null, null);
        verifySet();
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }
    @Test
    @Covers("public void set(int,int,String,Integer,Integer,EnumSet<TerminalStyle>,EnumSet<CellWalls>)")
    public void set_y_x_Seq_fg_bg_sty_wal() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        terminal.set(0, 0, "A", null, null, null, null);
        terminal.set(NUM_ROWS-1, 0, "B", null, null, null, null);
        terminal.set(0, NUM_COLS-1, "C", null, null, null, null);
        terminal.set(NUM_ROWS-1, NUM_COLS-1, "D", null, null, null, null);
        verifySet();
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    /**
     * Verify the core mechanic assumed in other tests
     */
    @Test
    public void terminalCell() {
        TerminalCell cellA = new TerminalCell();
        cellA.setSequence("A");
        TerminalCell cellC = cellA.clone();
        assertNotSame(cellA, cellC);
        assertEquals(cellA, cellC);
    }

    @Test
    public void testCoverage() {
        // Parent is abstract, so we need to test it!
        Coverage.checkCoverageDeep(UnboundTerminal.class, this.getClass());
    }

    @Test
    @Covers({"public void disableEventNotice(BlackenEventType)",
             "public void enableEventNotices(EnumSet<BlackenEventType>)",
             "public void disableEventNotices()",
             "public void enableEventNotice(BlackenEventType)",
             "public EnumSet<BlackenModifier> getLockingStates()",
             "public BlackenMouseEvent getmouse()",
             "public BlackenWindowEvent getwindow()",
             "public void setFont(String,boolean) throws FontNotFoundException",
             "public String setFont(String...) throws FontNotFoundException",
             "public void quit()",
             "public boolean setFullScreen(boolean)",
             "public boolean getFullScreen()",
             "public void inhibitFullScreen(boolean)",
             "public void setSize(TerminalScreenSize)",
             "public void setEventNotices(EnumSet<BlackenEventType>)",
             "public EnumSet<BlackenEventType> getEventNotices()",
    })
    public void unsupportedByUnboundTerminal() {
        // unsupported by UnboundTerminal
    }

    private void verifySet() {
        for (int y = 0; y < NUM_ROWS; y++) {
            for (int x = 0; x < NUM_COLS; x++) {
                if (x == 0 && y == 0) {
                    assertEquals("A", terminal.get(y, x).getSequence());
                } else if (x == 0 && y == NUM_ROWS-1) {
                    assertEquals("B", terminal.get(y, x).getSequence());
                } else if (x == NUM_COLS-1 && y == 0) {
                    assertEquals("C", terminal.get(y, x).getSequence());
                } else if (x == NUM_COLS-1 && y == NUM_ROWS-1) {
                    assertEquals("D", terminal.get(y, x).getSequence());
                } else {
                    assertEquals(EMPTY_SEQUENCE, terminal.get(y, x).getSequence());
                }
            }
        }
    }

    @Test
    @Covers("public void setBounds(Regionlike)")
    public void setBounds_Regionlike() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        BoxRegion region = new BoxRegion(NUM_ROWS+1, NUM_COLS+1, 1, 1);
        terminal.setBounds(region);
        assertEquals(NUM_ROWS+1, terminal.getHeight());
        assertEquals(NUM_COLS+1, terminal.getWidth());
        TerminalCellLike cleanEmpty = terminal.getEmpty().clone();
        cleanEmpty.setDirty(false);
        assertEquals(cleanEmpty, terminal.get(NUM_ROWS+1, NUM_COLS+1));
        try {
            terminal.get(0, 0);
            fail("Failed to move the terminal");
        } catch(IndexOutOfBoundsException ex) {
            // expected
        }
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }
    @Test
    @Covers("public void setBounds(int,int,int,int)")
    public void setBounds_h_w_y_x() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        terminal.setBounds(NUM_ROWS+1, NUM_COLS+1, 1, 1);
        assertEquals(NUM_ROWS+1, terminal.getHeight());
        assertEquals(NUM_COLS+1, terminal.getWidth());
        TerminalCellLike cleanEmpty = terminal.getEmpty().clone();
        cleanEmpty.setDirty(false);
        assertEquals(cleanEmpty, terminal.get(NUM_ROWS+1, NUM_COLS+1));
        try {
            terminal.get(0, 0);
            fail("Failed to move the terminal");
        } catch(IndexOutOfBoundsException ex) {
            // expected
        }
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public void doUpdate()")
    public void doUpdate() {
        // can not test with UnboundTerminal
    }
    @Test
    @Covers("public void refresh(int,int)")
    public void refresh_y_x() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        assertFalse(terminal.get(0, 0).isDirty());
        assertFalse(terminal.get(0, 1).isDirty());
        assertFalse(terminal.get(0, 2).isDirty());
        assertFalse(terminal.get(0, 3).isDirty());
        terminal.get(0, 0).setSequence("N");
        terminal.get(0, 1).setSequence("i");
        terminal.get(0, 2).setSequence("c");
        terminal.get(0, 3).setSequence("e");
        assertTrue(terminal.get(0, 0).isDirty());
        assertTrue(terminal.get(0, 1).isDirty());
        assertTrue(terminal.get(0, 2).isDirty());
        assertTrue(terminal.get(0, 3).isDirty());
        terminal.refresh(0, 0);
        terminal.refresh(0, 1);
        terminal.refresh(0, 2);
        terminal.refresh(0, 3);
        assertFalse(terminal.get(0, 0).isDirty());
        assertFalse(terminal.get(0, 1).isDirty());
        assertFalse(terminal.get(0, 2).isDirty());
        assertFalse(terminal.get(0, 3).isDirty());
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers({"public Positionable putString(int,int,String)", "public Positionable putString(Positionable,String)"})
    public void putString() {
        assertEquals(-1, terminal.getCursorY()); assertEquals(-1, terminal.getCursorX());
        Positionable newPos = terminal.putString(new Point(0, 0), "+");
        assertEquals(0, newPos.getY()); assertEquals(1, newPos.getX());
        newPos = terminal.putString(0, 3, "_");
        assertEquals(-1, terminal.getCursorY()); assertEquals(-1, terminal.getCursorX());
        assertEquals(0, newPos.getY()); assertEquals(4, newPos.getX());
        assertEquals("baseline failure", "+", terminal.get(0, 0).getSequence());
        assertEquals("overwrite test failure", "_", terminal.get(0, 3).getSequence());
        newPos = terminal.putString(newPos, "\r...\r");
        assertEquals("CR test failure.", ".", terminal.get(0, 0).getSequence());
        assertEquals("CR test overwrite failure.", "_", terminal.get(0, 3).getSequence());
        assertEquals(-1, terminal.getCursorY()); assertEquals(-1, terminal.getCursorX());
        assertEquals(0, newPos.getY()); assertEquals(0, newPos.getX());
        newPos = terminal.putString(newPos, "ABC");
        assertEquals(-1, terminal.getCursorY()); assertEquals(-1, terminal.getCursorX());
        assertEquals(0, newPos.getY()); assertEquals(3, newPos.getX());
        assertEquals("Simple puts failure","A", terminal.get(0, 0).getSequence());
        assertEquals("Simple puts failure","B", terminal.get(0, 1).getSequence());
        assertEquals("Simple puts failure","C", terminal.get(0, 2).getSequence());
        assertEquals("Simple puts overwrite failure","_", terminal.get(0, 3).getSequence());
        StringBuilder buf = new StringBuilder();
        String a1;
        buf.append('a');
        buf.appendCodePoint(BlackenCodePoints.CODEPOINT_COMBINING_DIAERESIS);
        a1 = buf.toString();
        newPos = terminal.putString(0, 0, a1);
        assertEquals(a1, terminal.get(0, 0).getSequence());
        assertEquals("B", terminal.get(0, 1).getSequence());
        assertEquals("C", terminal.get(0, 2).getSequence());
        assertEquals(-1, terminal.getCursorY()); assertEquals(-1, terminal.getCursorX());
        assertEquals(0, newPos.getY()); assertEquals(1, newPos.getX());

        buf.setLength(0);
        assertEquals("", buf.toString());

        buf.append('b');
        buf.appendCodePoint(BlackenCodePoints.CODEPOINT_COMBINING_RING_ABOVE);
        buf.appendCodePoint(BlackenCodePoints.CODEPOINT_COMBINING_RING_BELOW);
        String b1 = buf.toString();
        newPos = terminal.putString(0, 1, b1);
        assertEquals(b1, terminal.get(0, 1).getSequence());
        assertEquals("C", terminal.get(0, 2).getSequence());
        assertEquals(-1, terminal.getCursorY()); assertEquals(-1, terminal.getCursorX());
        assertEquals(0, newPos.getY()); assertEquals(2, newPos.getX());

        buf.setLength(0);

        buf.append('c');
        buf.appendCodePoint(BlackenCodePoints.CODEPOINT_COMBINING_CEDILLA);
        buf.appendCodePoint(BlackenCodePoints.CODEPOINT_COMBINING_INVERTED_BREVE);
        buf.appendCodePoint(BlackenCodePoints.CODEPOINT_COMBINING_CIRCUMFLEX_ACCENT);
        buf.appendCodePoint(BlackenCodePoints.CODEPOINT_COMBINING_BREVE);
        String c1 = buf.toString();
        newPos = terminal.putString(0, 2, c1);
        assertEquals(c1, terminal.get(0, 2).getSequence());
        assertEquals("_", terminal.get(0, 3).getSequence());
        assertEquals(-1, terminal.getCursorY()); assertEquals(-1, terminal.getCursorX());
        assertEquals(0, newPos.getY()); assertEquals(3, newPos.getX());

        newPos = terminal.putString(1, 3, "_");
        assertEquals(-1, terminal.getCursorY()); assertEquals(-1, terminal.getCursorX());
        assertEquals(1, newPos.getY()); assertEquals(4, newPos.getX());
        assertEquals("_", terminal.get(0, 3).getSequence());
        buf.setLength(0);
        buf.append("\r");
        buf.append(a1);
        buf.append(b1);
        buf.append(c1);
        buf.append("\n");
        newPos = terminal.putString(newPos.getY(), newPos.getX(), buf.toString());
        assertEquals(-1, terminal.getCursorY()); assertEquals(-1, terminal.getCursorX());
        assertEquals(2, newPos.getY()); assertEquals(0, newPos.getX());
        assertEquals(a1, terminal.get(1, 0).getSequence());
        assertEquals(b1, terminal.get(1, 1).getSequence());
        assertEquals(c1, terminal.get(1, 2).getSequence());
        assertEquals("_", terminal.get(1, 3).getSequence());
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public void applyTemplate(int,int,TerminalCellTemplate,int)")
    public void applyTemplate() {
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
        terminal.putString(0, 0, "Test");
        TerminalCellTemplate template = new TerminalCellTemplate(null, 0xFFaaaaaa, 0xFF000000);
        terminal.applyTemplate(0, 0, template, NUM_COLS);
        TerminalCellLike c = terminal.get(0, 0);
        assertEquals(0xFFaaaaaa, c.getForeground());
        assertEquals(0xFF000000, c.getBackground());
        assertEquals("T", c.getSequence());
        c = terminal.get(0, 1);
        assertEquals(0xFFaaaaaa, c.getForeground());
        assertEquals(0xFF000000, c.getBackground());
        assertEquals("e", c.getSequence());
        c = terminal.get(0, 2);
        assertEquals(0xFFaaaaaa, c.getForeground());
        assertEquals(0xFF000000, c.getBackground());
        assertEquals("s", c.getSequence());
        c = terminal.get(0, 3);
        assertEquals(0xFFaaaaaa, c.getForeground());
        assertEquals(0xFF000000, c.getBackground());
        assertEquals("t", c.getSequence());
        c = terminal.get(0, 4);
        assertEquals(0xFFaaaaaa, c.getForeground());
        assertEquals(0xFF000000, c.getBackground());
        assertEquals(EMPTY_SEQUENCE, c.getSequence());
        assertEquals(-1, terminal.getCursorY());
        assertEquals(-1, terminal.getCursorX());
    }

    @Test
    @Covers("public BlackenImageLoader getImageLoader()")
    public void getImageLoader() {
        BlackenImageLoader imageLoader = terminal.getImageLoader();
        assertNotNull(imageLoader);
    }

    @Test
    @Covers("public boolean isRunning()")
    public void isRunning() {
        assertTrue(terminal.isRunning());
    }

    @Test
    @Covers({"public int getX()",
             "public int getY()"
    })
    public void getX() {
        assertEquals(0, terminal.getX());
        assertEquals(0, terminal.getY());
    }
}
