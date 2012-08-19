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

import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.core.Coverage;
import com.googlecode.blacken.core.Covers;
import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.Regionlike;

import static org.junit.Assert.*;

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
    @Covers({"public ColorPalette coercePalette(ColorPalette,int,int)",
             "public ColorPalette setPalette(ColorPalette,int,int)",
            })
    public void coercePalette_palette_int_int() {
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
        terminal.coercePalette(palette, 0, 1);
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
    @Covers("public void copyFrom(TerminalInterface,int,int,int,int,int,int)")
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
    @Covers("public TerminalInterface getBackingTerminalInterface()")
    public void getBackingTerminalInterface() {
        assertNotNull(terminal.getBackingTerminalInterface());
        assertSame(terminal, terminal.getBackingTerminalInterface());
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
        terminal.coercePalette(palette, 0xFF808080, 0xFF000000);
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
        // unimplemented in UnboundTerminal
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

}
