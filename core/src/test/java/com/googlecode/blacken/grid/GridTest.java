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
package com.googlecode.blacken.grid;

import com.googlecode.blacken.cell.GridCellCopier;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import org.junit.*;

import com.googlecode.blacken.cell.FlexibleCellCopier;
import com.googlecode.blacken.core.Coverage;
import com.googlecode.blacken.core.Covers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * @author Steven Black
 */
public class GridTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GridTest.class);

    private Integer empty = null;
    private Grid<Integer> grid = null;
    private int size_x;
    private int size_y;
    private int start_x;
    private int start_y;
    Grid<Integer> emptygrid = null;
    private Integer empty2;
    TestRegionlike parent;

    public GridTest() {
        parent = new TestRegionlike();
    }
    
    @Test
    public void testCoverage() {
        Coverage.checkCoverage(Grid.class, this.getClass());
    }

    /**
     * Perform some common setup for related tests
     * @param grid 
     * @param empty cell template
     * @param height grid height
     * @param width grid width
     * @param y starting Y coordinate
     * @param x starting X coordinate
     */
    public void setUp(Grid<Integer> grid, Object empty, 
                      int height, int width, int y, int x) {
        this.grid = grid;
        this.size_x = width;
        this.size_y = height;
        this.start_x = x;
        this.start_y = y;
        empty2 = new Integer(100);
        emptygrid = new Grid<>();
        parent.setUp(grid, height, width, y, x);
    }

    @Before
    public void setUp() {
        empty = 0;
        grid = new Grid<>(empty, 25, 80, 5, 10);
        setUp(grid, empty, 25, 80, 5, 10);
    }

    @Test
    @Covers("public Grid()")
    public void Grid_test() {
        assertNotNull(emptygrid);
        assertTrue(emptygrid.getHeight() == 0);
        assertTrue(emptygrid.getWidth() == 0);
        assertTrue(emptygrid.getX() == 0);
        assertTrue(emptygrid.getY() == 0);
        try {
            emptygrid.get(0, 0);
            fail("Should have through IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // do nothing
        }
    }

    @Test
    @Covers("public Grid(Z,int,int)")
    public void Grid_EmptyRowsCols() {
        grid = new Grid<>(empty, size_y, size_x);
        assertNotNull(grid);
        assertTrue(grid.getHeight() == size_y);
        assertTrue(grid.getWidth() == size_x);
        assertTrue(grid.getX() == 0);
        assertTrue(grid.getY() == 0);
        for (int row = 0; row < grid.getHeight(); row ++) {
            for (int col = 0; col < grid.getWidth(); col++) {
                grid.get(row, col).equals(empty);
            }
        }
    }

    @Test
    @Covers("public Grid(Z,int,int,int,int)")
    public void Grid_EmptyRowsColsYX() {
        assertNotNull(grid);
        assertEquals(size_y, grid.getHeight());
        assertEquals(size_x, grid.getWidth());
        assertEquals(start_x, grid.getX());
        assertEquals(start_y, grid.getY());
        for (int y = start_y; y < start_y + grid.getHeight(); y ++) {
            for (int x = start_x; x < start_x + grid.getWidth(); x++) {
                grid.get(y, x).equals(empty);
            }
        }
    }

    @Test
    @Covers("public Grid(Z,int,int,int,int,boolean)")
    public void Grid_EmptyRowsColsY1X1Irregular() {
        grid = new Grid<>(empty, size_y, size_x, start_y, start_x, true);
        assertNotNull(grid);
        assertEquals(size_y, grid.getHeight());
        assertEquals(size_x, grid.getWidth());
        assertEquals(start_x, grid.getX());
        assertEquals(start_y, grid.getY());
        for (int row = 0; row < size_y; row ++) {
            for (int col = 0; col < size_x; col++) {
                assertNull(grid.get(row + start_y, col + start_x));
            }
        }
    }

    @Test
    @Covers("public void addGrid(Grid<Z>)")
    public void addGrid() {
        this.checkSolid(empty, empty);
        Grid<Integer> newgrid = new Grid<>(empty+1, size_y, size_x, start_y, start_x, true);
        grid.addGrid(newgrid);
        this.checkSolid(empty, empty);
        for (int r = 0; r < size_y; r++) {
            for (int c = 0; c < size_x; c++) {
                Integer z = r+c + start_y + start_x;
                newgrid.set(r + start_y, c + start_x, z);
            }
        }
        this.checkPattern(newgrid, size_y, size_x, start_y, start_x, start_y, start_x);
        grid.addGrid(newgrid);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, start_y, start_x);
        newgrid.clear();
        grid.clear();
        this.checkSolid(empty, empty);
        newgrid.makeRegular(empty+1);
        grid.addGrid(newgrid);
        this.checkSolid(empty+1, empty+1);
    }
    
    @Test
    @Covers("public void box(int,int,int,int,Z,Z,Z,Z,Z,Z,Z,Z,Z)")
    public void box_HeightWidthXYLeftRightTopTlTrBlBrI() {
        grid.box(size_y - 2, size_x - 2, start_y+1, start_x+1, 
                 4, 6, 8, 2, 7, 9, 1, 3, 5);
        checkBox();
    }
    
    /**
     * Set a pattern
     * 
     * @param grid
     * @param height
     * @param width
     * @param y1
     * @param x1
     * @param oy
     * @param ox 
     */
    protected void setPattern(Grid<Integer> grid, 
                              int height, int width, int y1, int x1, 
                              int oy, int ox) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid.set(row + y1, col + x1, 
                         new Integer(col + ox + row+ oy));
            }
        }
    }

    /**
     * Check the pattern.
     * 
     * @param grid
     * @param height
     * @param width
     * @param y1 current starting row (for placement)
     * @param x1 current starting column (for placement)
     * @param oy original starting row (for pattern)
     * @param ox original starting column (for pattern)
     */
    protected void checkPattern(Grid<Integer> grid, 
                                int height, int width, int y1, int x1, 
                                int oy, int ox) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Integer d = grid.get(row + y1, col + x1);
                assertNotNull(d);
                assertEquals(String.format("(%d,%d) Offset:(%d,%d)",
                                         row, col, oy, ox), 
                                         col + row + ox + oy, (int)d);
            }
        }
    }

    /**
     * Check a solid block
     * 
     * @param inside
     * @param border 
     */
    protected void checkSolid(Integer inside, Integer border) {
        checkSolid(grid, inside, border, size_y, size_x, start_y, start_x);
    }
    
    /**
     * Check that the grid contains a border and an inside.
     * 
     * @param inside
     * @param border
     */
    protected void checkSolid(Grid<Integer> grid, 
                              Integer inside, Integer border,
                              int size_y, int size_x,
                              int y, int x) {
        int x2 = x + size_x -1;
        int y2 = y + size_y -1;
        int x5 = x + (size_x / 2);
        int y5 = y + (size_y / 2);
        
        // compare a sample that should be outside
        // numbers (ex. "7(%s, %s)") map to location on number pad
        assertEquals(String.format("7(%s, %s)", y, x), border, grid.get(y, x));
        assertEquals(String.format("3(%s, %s)", y2, x2), border, grid.get(y2, x2));
        assertEquals(String.format("9(%s, %s)", y, x2), border, grid.get(y, x2));
        assertEquals(String.format("1(%s, %s)", y2, x), border, grid.get(y2, x));
        assertEquals(String.format("4(%s, %s)", y5, x), border, grid.get(y5, x));
        assertEquals(String.format("8(%s, %s)", y, x5), border, grid.get(y, x5));
        assertEquals(String.format("6(%s, %s)", y5, x2), border, grid.get(y5, x2));
        assertEquals(String.format("2(%s, %s)", y2, x5), border, grid.get(y2, x5));
        
        // compare a sample that should be the box detail
        assertEquals(inside, grid.get(y + 1, x + 1));
        assertEquals(inside, grid.get(y2 - 1, x2 - 1));
        assertEquals(inside, grid.get(y + 1, x2 - 1));
        assertEquals(inside, grid.get(y2 - 1, x + 1));
        assertEquals(inside, grid.get(y5, x + 1));
        assertEquals(inside, grid.get(y + 1, x5));
        assertEquals(inside, grid.get(y5, x2 - 1));
        assertEquals(inside, grid.get(y2 - 1, x5));

        // compare a sample that should be the box inside
        assertEquals(inside, grid.get(y5, x5));
        assertEquals(inside, grid.get(y + 2, x + 2));
        assertEquals(inside, grid.get(y2 - 2, x2 - 2));
        assertEquals(inside, grid.get(y + 2, x2 - 2));
        assertEquals(inside, grid.get(y2 - 2, x + 2));
        assertEquals(inside, grid.get(y5, x + 2));
        assertEquals(inside, grid.get(y + 2, x5));
        assertEquals(inside, grid.get(y5, x2 - 2));
        assertEquals(inside, grid.get(y2 - 2, x5));
    }
    
    /**
     * Check a box shape
     */
    protected void checkBox() {
        int x2 = start_x + size_x -1;
        int y2 = start_y + size_y -1;
        int x5 = start_x + (size_x / 2);
        int y5 = start_y + (size_y / 2);
        
        // compare a sample that should be outside
        assertTrue(grid.get(start_y, start_x) == 0);
        assertTrue(grid.get(y2, x2) == 0);
        assertTrue(grid.get(start_y, x2) == 0);
        assertTrue(grid.get(y2, start_x) == 0);
        assertTrue(grid.get(y5, start_x) == 0);
        assertTrue(grid.get(start_y, x5) == 0);
        assertTrue(grid.get(y5, x2) == 0);
        assertTrue(grid.get(y2, x5) == 0);
        
        // compare a sample that should be the box detail
        assertTrue(grid.get(start_y + 1, start_x + 1) == 7);
        assertTrue(grid.get(y2 - 1, x2 - 1) == 3);
        assertTrue(grid.get(start_y + 1, x2 - 1) == 9);
        assertTrue(grid.get(y2 - 1, start_x + 1) == 1);
        assertTrue(grid.get(y5, start_x + 1) == 4);
        assertTrue(grid.get(start_y + 1, x5) == 8);
        assertTrue(grid.get(y5, x2 - 1) == 6);
        assertTrue(grid.get(y2 - 1, x5) == 2);

        // compare a sample that should be the box inside
        assertTrue(grid.get(y5, x5) == 5);
        assertTrue(grid.get(start_y + 2, start_x + 2) == 5);
        assertTrue(grid.get(y2 - 2, x2 - 2) == 5);
        assertTrue(grid.get(start_y + 2, x2 - 2) == 5);
        assertTrue(grid.get(y2 - 2, start_x + 2) == 5);
        assertTrue(grid.get(y5, start_x + 2) == 5);
        assertTrue(grid.get(start_y + 2, x5) == 5);
        assertTrue(grid.get(y5, x2 - 2) == 5);
        assertTrue(grid.get(y2 - 2, x5) == 5);
    }
    
    @Test
    @Covers("public void box(Regionlike,Z,Z,Z,Z,Z,Z,Z,Z,Z)")
    public void box_RegionlikeLeftRightTopBottomTlTrBrBl() {
        BoxRegion b = new BoxRegion(size_y -2, size_x -2, start_y+1, start_x+1);
        grid.box(b, 
                 4, 6, 8, 2, 7, 9, 1, 3, 5);
        checkBox();
    }

    @Test
    @Covers("public void clear()")
    public void clear_test() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        grid.clear();
        checkSolid(empty, empty);
    }

    @Test
    @Covers("public void clear(Z)")
    public void clear_Empty() {
        assertTrue(grid.getEmpty() == empty);
        assertTrue(empty != empty2);
        grid.clear(empty2);
        checkSolid(empty2, empty2);
    }

    @Test
    @Covers("public boolean contains(int,int)")
    public void contains_YX() {
        assertTrue(grid.contains(start_y, start_x));
        assertTrue(grid.contains(start_y + this.size_y -1, start_x + this.size_x -1));
        assertFalse(grid.contains(start_y - 1, start_x - 1));
        assertFalse(grid.contains(start_y + this.size_y, start_x + this.size_x));
    }

    @Test
    @Covers("public boolean contains(int,int,int,int)")
    public void contains_HeightWidthYX() {
        assertTrue(grid.contains(size_y, size_x, start_y, start_x));
        assertTrue(grid.contains(size_y -2, size_x -2, start_y +1, start_x +1));
        assertFalse(grid.contains(size_y +2, size_x +2, start_y +1, start_x +1));
        assertFalse(grid.contains(size_y, size_x, start_y + size_y, start_x));
        assertFalse(grid.contains(size_y, size_x, start_y+2, start_x));
    }

    @Test
    @Covers("public boolean contains(Positionable)")
    public void contains_Positionable() {
        Point p = new Point(start_y, start_x);
        assertTrue(grid.contains(p));
        p = new Point(start_y + this.size_y -1, start_x + this.size_x -1);
        assertTrue(grid.contains(p));
        p = new Point(start_y - 1, start_x - 1);
        assertFalse(grid.contains(p));
        p = new Point(start_y + this.size_y, start_x + this.size_x);
        assertFalse(grid.contains(p));
    }

    @Test
    @Covers("public boolean contains(int[])")
    public void contains_intArr() {
        int[] p = new int[] {start_y, start_x};
        assertTrue(grid.contains(p));
        p = new int[] {start_y + this.size_y -1, start_x + this.size_x -1};
        assertTrue(grid.contains(p));
        p = new int[] {start_y - 1, start_x - 1};
        assertFalse(grid.contains(p));
        p = new int[] {start_y + this.size_y, start_x + this.size_x};
        assertFalse(grid.contains(p));
    }
    
    @Test
    @Covers("public boolean contains(Regionlike)")
    public void contains_Regionlike() {
        BoxRegion b = new BoxRegion(size_y, size_x, start_y, start_x);
        assertTrue(grid.contains(b));
        assertTrue(grid.contains(size_y -2, size_x -2, start_y +1, start_x +1));
        assertFalse(grid.contains(size_y +2, size_x +2, start_y +1, start_x +1));
        assertFalse(grid.contains(size_y, size_x, start_y + size_y, start_x));
        assertFalse(grid.contains(size_y, size_x, start_y+2, start_x));
    }

    @Test
    @Covers("public Z copyCell(Z)")
    public void copyCell() {
        Integer v = 42;
        Integer a = this.grid.copyCell(v);
        assertSame(v, a);
        assertEquals(v, a);
    }

    @Test
    @Covers("public void copyFrom(Grid<Z>,int,int,int,int,int,int,DirtyGridCell<Z>)")
    public void copyFrom() {
        checkSolid(empty, empty);
        Grid<Integer> newgrid = new Grid<>(empty+1,size_y, size_x, start_y, 
                start_x);
        try {
            grid.copyFrom(newgrid, size_y, size_x, start_y, start_x, start_y, 
                    start_x, null);
        } catch(ArrayIndexOutOfBoundsException e) {
            fail(e.toString());
        }
        checkSolid(empty+1, empty+1);
    }
    
    @Test
    @Covers("public Z get(int,int)")
    public void get_YX() {
        assertNotNull(grid.get(start_y, start_x));
        assertTrue(grid.get(start_y, start_x) == empty);
        assertNotNull(grid.get(start_y + size_y - 1, start_x + size_x - 1));
        assertTrue(grid.get(start_y + size_y - 1, start_x + size_x - 1) == empty);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    @Covers("public Z get(int,int)")
    public void get_YX_Exception1() {
        assertNotNull(grid.get(start_y-1, start_x-1));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    @Covers("public Z get(int,int)")
    public void get_YX_Exception2() {
        assertNotNull(grid.get(start_y + size_y, start_x + size_x));
    }

    @Test
    @Covers("public <T> Z get(T)")
    public void get_Positionable() {
        Point p = new Point(start_y, start_x);
        assertNotNull(grid.get(p));
        assertTrue(grid.get(p) == empty);
        p = new Point(start_y + size_y - 1, start_x + size_x - 1);
        assertNotNull(grid.get(p));
        assertTrue(grid.get(p) == empty);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    @Covers("public <T> Z get(T)")
    public void get_Positionable_Exception1() {
        Point p = new Point(start_y - 1, start_x - 1);
        assertNotNull(grid.get(p));
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    @Covers("public <T> Z get(T)")
    public void get_Positionable_Exception2() {
        Point p = new Point(start_y + size_y, start_x + size_x);
        assertNotNull(grid.get(p));
    }

    @Test
    @Covers("public Regionlike getBounds()")
    public void getBounds_test() {
        assertNotNull(grid.getBounds());
    }

    @Test
    @Covers("public RegionIterator getEdgeIterator()")
    public void getEdgeIterator_test() {
        RegionIterator edge = grid.getEdgeIterator();
        int[] p = new int[4];
        boolean[] pattern;
        int segtype;
        
        while (!edge.isDone()) {
            segtype = edge.currentSegment(p);
            if (segtype == RegionIterator.SEG_BORDER_SOLID || 
                    segtype == RegionIterator.SEG_INSIDE_SOLID) {
                for (int y1 = p[0]; y1 <= p[2]; y1++) {
                    for (int x1 = p[1]; x1 <= p[3]; x1++) {
                        assertTrue(grid.contains(y1, x1));
                        assertTrue(grid.get(y1, x1) == this.empty);
                        grid.set(y1, x1, empty2);
                    }
                }
            } else if (segtype == RegionIterator.SEG_BORDER_PATTERNED || 
                    segtype == RegionIterator.SEG_INSIDE_PATTERNED) {
                int pidx = -1;
                pattern = edge.currentPattern();
                for (int y1 = p[0]; y1 <= p[2]; y1++) {
                    for (int x1 = p[1]; x1 <= p[3]; x1++) {
                        pidx++;
                        if (pidx >= pattern.length) pidx = 0;
                        if (!pattern[pidx]) continue;
                        assertTrue(grid.contains(y1, x1));
                        assertTrue(grid.get(y1, x1) == this.empty);
                        grid.set(y1, x1, empty2);
                    }
                }
            } else if (segtype == RegionIterator.SEG_COMPLETE) {
                // should never happen, but just in case...
                break;
            }
            edge.next();
        }
        
        checkSolid(empty, empty2);
    }

    @Test
    @Covers("public Z getEmpty()")
    public void getEmpty() {
        grid.clear(empty2);
        assertEquals(empty2, grid.getEmpty());
        // Because it is a primative type, it will be the same
        assertSame(empty2, grid.getEmpty());
    }

    @Test
    @Covers("public int getHeight()")
    public void getHeight_test() {
        assertTrue(grid.getHeight() == size_y);
    }

    @Test
    @Covers("public RegionIterator getInsideIterator()")
    public void getInsideIterator_test() {
        RegionIterator edge = grid.getInsideIterator();
        int[] p = new int[4];
        int segtype;
        
        while (!edge.isDone()) {
            segtype = edge.currentSegment(p);
            if (segtype == RegionIterator.SEG_BORDER_SOLID || 
                    segtype == RegionIterator.SEG_INSIDE_SOLID) {
                for (int y1 = p[0]; y1 <= p[2]; y1++) {
                    for (int x1 = p[1]; x1 <= p[3]; x1++) {
                        assertTrue(grid.contains(y1, x1));
                        assertTrue(grid.get(y1, x1) == this.empty);
                        grid.set(y1, x1, empty2);
                    }
                }
            } else if (segtype == RegionIterator.SEG_BORDER_PATTERNED || 
                    segtype == RegionIterator.SEG_INSIDE_PATTERNED) {
                fail("Grid shouldn't be patterned.");
            }
            edge.next();
        }
        checkSolid(empty2, empty);
    }

    @Test
    @Covers("public RegionIterator getNotOutsideIterator()")
    public void getNotOutsideIterator_test() {
        RegionIterator edge = grid.getNotOutsideIterator();
        int[] p = new int[4];
        int segtype;
        
        while (!edge.isDone()) {
            segtype = edge.currentSegment(p);
            if (segtype == RegionIterator.SEG_BORDER_SOLID || 
                    segtype == RegionIterator.SEG_INSIDE_SOLID) {
                for (int y1 = p[0]; y1 <= p[2]; y1++) {
                    for (int x1 = p[1]; x1 <= p[3]; x1++) {
                        assertTrue(grid.contains(y1, x1));
                        assertTrue(grid.get(y1, x1) == this.empty);
                        grid.set(y1, x1, empty2);
                    }
                }
            } else if (segtype == RegionIterator.SEG_BORDER_PATTERNED || 
                    segtype == RegionIterator.SEG_INSIDE_PATTERNED) {
                fail("Grid shouldn't be patterned.");
            }
            edge.next();
        }
        checkSolid(empty2, empty2);
    }

    @Test
    @Covers("public Sizable getSize()")
    public void getSize_test() {
        Sizable size = grid.getSize();
        assertEquals(this.size_y, size.getHeight());
        assertEquals(this.size_x, size.getWidth());
    }

    @Test
    @Covers("public Positionable getPosition()")
    public void getPosition_test() {
        Positionable size = grid.getPosition();
        assertEquals(this.start_y, size.getY());
        assertEquals(this.start_x, size.getX());
    }

    @Test
    @Covers("public int getWidth()")
    public void getWidth_test() {
        assertTrue(this.size_x == grid.getWidth());
    }

    @Test
    @Covers("public int getX()")
    public void getX_test() {
        assertTrue(this.start_x == grid.getX());
    }

    @Test
    @Covers("public int getY()")
    public void getY_test() {
        assertTrue(this.start_y == grid.getY());
    }

    @Test
    @Covers("public boolean intersects(int,int,int,int)")
    public void intersects_HeightWidthYX() {
        assertFalse(grid.intersects(size_y, size_x, start_y, start_x));
        assertFalse(grid.intersects(size_y -2, size_x -2, start_y +1, start_x +1));
        assertTrue(grid.intersects(size_y +2, size_x +2, start_y +1, start_x +1));
        assertFalse(grid.intersects(size_y, size_x, start_y + size_y, start_x));
        assertTrue(grid.intersects(size_y, size_x, start_y+2, start_x));
    }

    @Test
    @Covers("public boolean intersects(Regionlike)")
    public void intersects_Regionlike() {
        BoxRegion b = new BoxRegion(size_y, size_x, start_y, start_x);
        assertFalse(grid.intersects(b));
        b = new BoxRegion(size_y -2, size_x -2, start_y +1, start_x +1);
        assertFalse(grid.intersects(b));
        b = new BoxRegion(size_y +2, size_x +2, start_y +1, start_x +1);
        assertTrue(grid.intersects(b));
        b = new BoxRegion(size_y, size_x, start_y + size_y, start_x);
        assertFalse(grid.intersects(b));
        b = new BoxRegion(size_y, size_x, start_y+2, start_x);
        assertTrue(grid.intersects(b));
    }

    @Test
    @Covers("public Grid<Z> like()")
    public void like_test() {
        grid.wipe(size_y, size_x, start_y, start_x, empty2);
        this.checkSolid(empty2, empty2);
        Grid<Integer> g = grid.like();
        assertTrue(g.getEmpty() == grid.getEmpty());
        assertTrue(g.getHeight() == grid.getHeight());
        assertTrue(g.getWidth() == grid.getWidth());
        assertTrue(g.getX() == grid.getX());
        assertTrue(g.getY() == grid.getY());
        Integer e = grid.getEmpty();
        grid = g;
        this.checkSolid(e, e);
    }

    @Test
    @Covers("public void line(Positionable,Positionable,Z)")
    public void line_PositionablePositionableZ() {
        Positionable a = new Point(start_y, start_x);
        Positionable b = new Point(start_y, start_x + size_x -1);
        for(Integer r = 0; r < size_y; r++) {
            a.setY(start_y + r);
            b.setY(start_y + r);
            grid.line(a, b, r);
        }
        for (Integer r = 0; r < size_y; r++) {
            for (Integer c = 0; c < size_x; c++) {
                assertEquals(r, grid.get(r + start_y, c + start_x));
            }
        }
        a.setPosition(start_y, start_x);
        b.setPosition(start_y + size_y -1, size_x);
        for(Integer c = 0; c < size_x; c++) {
            a.setX(start_x + c);
            b.setX(start_x + c);
            grid.line(a, b, c);
        }
        for (Integer r = 0; r < size_y; r++) {
            for (Integer c = 0; c < size_x; c++) {
                assertEquals(c, grid.get(r + start_y, c + start_x));
            }
        }
    }
    
    @Test
    @Covers("public void line(int,int,int,int,Z)")
    public void line_y1x1x2y2Z() {
        int x1 = start_x;
        int x2 = start_x + size_x -1;
        for(int r = 0; r < size_y; r++) {
            try {
                grid.line(r + start_y, x1, r + start_y, x2, new Integer(r));
            } catch(ArrayIndexOutOfBoundsException e) {
                fail(e.getMessage());
            }
        }
        for (int r = 0; r < size_y; r++) {
            for (int c = 0; c < size_x; c++) {
                assertEquals(r, (int)grid.get(r + start_y, c + start_x));
            }
        }
        int y1 = start_y;
        int y2 = start_y + size_y - 1;
        for(int c = 0; c < size_x; c++) {
            try {
                grid.line(y1, c + start_x, y2, c + start_x, new Integer(c));
            } catch(ArrayIndexOutOfBoundsException e) {
                fail(e.getMessage());
            }
        }
        for (int r = 0; r < size_y; r++) {
            for (int c = 0; c < size_x; c++) {
                assertEquals(c, (int)grid.get(r + start_y, c + start_x));
            }
        }
    }

    @Test
    @Covers("public void makeIrregular()")
    public void makeIrregular() {
        try {
            grid.set(start_y, start_x, null);
            fail("regular grids can't have null entries");
        } catch(NullPointerException e) {
            /* do nothing */
        }
        grid.makeIrregular();
        grid.set(start_y, start_x, null);
    }
    
    @Test
    @Covers("public void makeRegular(Z)")
    public void makeRegular() {
        Grid<Integer> newgrid = new Grid<>();
        newgrid.resize(size_x, size_x, false);
        assertNull(newgrid.get(0, 0));
        newgrid.makeRegular(empty);
        try {
            grid.set(0, 0, null);
            fail("regular grids can't have null entries");
        } catch(NullPointerException e) {
            /* do nothing */
        }
    }
    
    @Test
    @Covers("public void moveBlock(int,int,int,int,int,int,DirtyGridCell<Z>)")
    public void moveBlock() {
        grid.clear(new Integer(0));
        for (int row = 0; row < size_y; row++) {
            for (int col = 0; col < size_x; col++) {
                assertEquals(0, (int)grid.get(row+start_y, col+start_x));
            }
        }
        for (int col = 0; col < size_x; col++) {
            grid.set(0+start_y, col+start_x, new Integer(100 + col));
            assertEquals(100+col, (int)grid.get(0+start_y, col+start_x));
        }
        // LOGGER.debug(String.format("moveBlock(numRows:%s, numCols:%s, origY:%s, origX:%s, newY:%s, newX:%s)", 1, size_x, start_y, start_x, 2+start_y, start_x));
        grid.moveBlock(1, size_x, start_y, start_x, 2+start_y, start_x, null);
        for (int col = 0; col < size_x; col++) {
            assertEquals(String.format("(%s,%s)", start_y, col+start_x), 
                    (int)empty, (int)grid.get(start_y, col+start_x));
            assertEquals(String.format("(%s,%s)", start_y, col+start_x), 
                    100+col, (int)grid.get(2+start_y, col+start_x));
        }
        grid.set(2+start_y, start_x, 0x30);
        grid.moveBlock(1, 1, 2+start_y, start_x, 2+start_y, 2+start_x, null);
        assertEquals(0, (int)grid.get(2+start_y, start_x));
        assertEquals(0x30, (int)grid.get(2+start_y, 2+start_x));
    }

    @Test
    @Covers("public void reset(int,int,Z)")
    public void reset_YXEmpty_ZeroSize() {
        grid.reset(0, 0, empty2);
        assertTrue(grid.getHeight() == 0);
        assertTrue(grid.getWidth() == 0);
        assertTrue(grid.getEmpty() == empty2);
        grid.reset(0, 0, empty2);
        assertTrue(grid.getHeight() == 0);
        assertTrue(grid.getWidth() == 0);
        assertTrue(grid.getEmpty() == empty2);
    }

    @Test
    public void reset_YXEmpty_Null() {
        grid.reset(-1, -1, null);
        assertTrue(grid.getHeight() == this.size_y);
        assertTrue(grid.getWidth() == this.size_x);
        assertTrue(grid.getEmpty() == empty);
        grid.reset(-1, -1, null);
        assertTrue(grid.getHeight() == this.size_y);
        assertTrue(grid.getWidth() == this.size_x);
        assertTrue(grid.getEmpty() == empty);
    }

    @Test
    public void reset_YXEmpty_Half() {
        this.size_y /= 2; this.size_x /= 2;
        grid.reset(this.size_y, this.size_x, empty);
        assertTrue(grid.getHeight() == this.size_y);
        assertTrue(grid.getWidth() == this.size_x);
        assertTrue(grid.getEmpty() == empty);
        this.checkSolid(empty, empty);
        grid.reset(this.size_y, this.size_x, empty2);
        assertTrue(grid.getHeight() == this.size_y);
        assertTrue(grid.getWidth() == this.size_x);
        assertTrue(grid.getEmpty() == empty2);
        this.checkSolid(empty2, empty2);
    }

    @Test
    public void reset_YXEmpty_Double() {
        this.size_y /= 2; this.size_x /= 2;
        grid.reset(this.size_y, this.size_x, empty2);
        assertTrue(grid.getHeight() == this.size_y);
        assertTrue(grid.getWidth() == this.size_x);
        assertTrue(grid.getEmpty() == empty2);
        this.checkSolid(empty2, empty2);
        grid.reset(this.size_y, this.size_x, empty);
        assertTrue(grid.getHeight() == this.size_y);
        assertTrue(grid.getWidth() == this.size_x);
        assertTrue(grid.getEmpty() == empty);
        this.checkSolid(empty, empty);
    }

    @Test
    @Covers("public Z set(int,int,Z)")
    public void set_YXZ() {
        grid.set(start_y, start_x, empty2);
        assertTrue(grid.get(start_y, start_x) == empty2);
        empty2 += 100;
        assertFalse(grid.get(start_y, start_x) == empty2);
    }

    @Test
    @Covers("public <T> Z set(T,Z)")
    public void set_PositionableZ() {
        Point p = new Point(start_y, start_x);
        grid.set(p, empty2);
        assertTrue(grid.get(p) == empty2);
        empty2 += 100;
        assertFalse(grid.get(p) == empty2);
    }

    @Test
    @Covers("public Z setCopy(int,int,Z)")
    public void setCopy_YXZ() {
        grid.setCopy(start_y, start_x, empty2);
        assertTrue(grid.get(start_y, start_x) == empty2);
        empty2 += 100;
        assertFalse(grid.get(start_y, start_x) == empty2);
    }

    @Test
    @Covers("public void setBounds(Regionlike)")
    public void setBounds() {
        assertEquals(size_y, grid.getHeight());
        assertEquals(size_x, grid.getWidth());
        assertEquals(start_y, grid.getY());
        assertEquals(start_x, grid.getX());
        BoxRegion region = new BoxRegion(size_y - 1, size_x - 1, start_y + 1, 
                start_x + 1);
        grid.setBounds(region);
        assertEquals(size_y -1, grid.getHeight());
        assertEquals(size_x -1, grid.getWidth());
        assertEquals(start_x + 1, grid.getX());
        assertEquals(start_y + 1, grid.getY());
    }


    @Test
    @Covers("public void setBounds(int,int,int,int)")
    public void setBounds_h_w_y_x() {
        assertEquals(size_y, grid.getHeight());
        assertEquals(size_x, grid.getWidth());
        assertEquals(start_y, grid.getY());
        assertEquals(start_x, grid.getX());
        grid.setBounds(size_y - 1, size_x - 1, start_y + 1,
                start_x + 1);
        assertEquals(size_y -1, grid.getHeight());
        assertEquals(size_x -1, grid.getWidth());
        assertEquals(start_x + 1, grid.getX());
        assertEquals(start_y + 1, grid.getY());
    }

    @Test
    @Covers("public <T> Z setCopy(T,Z)")
    public void setCopy_PositionableZ() {
        Point p = new Point(start_y, start_x);
        grid.setCopy(p, empty2);
        assertTrue(grid.get(start_y, start_x) == empty2);
        empty2 += 100;
        assertFalse(grid.get(start_y, start_x) == empty2);
    }

    @Test
    @Covers("public void setHeight(int)")
    public void setHeight_Height_Same() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        grid.setHeight(size_y);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public void setHeight(int)")
    public void setHeight_Height_Half() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        size_y /= 2;
        grid.setHeight(size_y);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public void setHeight(int)")
    public void setHeight_Height_Double() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        grid.setHeight(size_y * 2);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        this.checkSolid(grid, empty, empty, size_y, size_x, 
                        start_y + size_y, start_x);
    }

    @Test
    @Covers("public void setPosition(int,int)")
    public void setPosition() {
        int[] ideal1 = {start_y, start_x};
        int[] ideal2 = {start_y + 2, start_x + 2};
        assertEquals(ideal1[0], grid.getY());
        assertEquals(ideal1[1], grid.getX());
        grid.setPosition(ideal2[0], ideal2[1]);
        assertEquals(ideal2[0], grid.getY());
        assertEquals(ideal2[1], grid.getX());
    }

    @Test
    @Covers("public void setPosition(Positionable)")
    public void setPosition_Positionable() {
        int[] ideal1 = {start_y, start_x};
        int[] ideal2 = {start_y + 2, start_x + 2};
        assertEquals(ideal1[0], grid.getY());
        assertEquals(ideal1[1], grid.getX());
        Point p = new Point(ideal2[0], ideal2[1]);
        grid.setPosition(p);
        assertEquals(ideal2[0], grid.getY());
        assertEquals(ideal2[1], grid.getX());
    }

    @Test
    @Covers("public void setSize(int,int)")
    public void setSize_YX_Same() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        grid.setSize(size_y, size_x);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public void setSize(int,int)")
    public void setSize_YX_Half() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        size_y /= 2; size_x /= 2;
        grid.setSize(size_y, size_x);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    public void setSize_YX_Double() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        size_y /= 2; size_x /= 2;
        grid.setSize(size_y, size_x);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public void setSize(Sizable)")
    public void setSize_Size_SimpleSize() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        grid.setSize(new SimpleSize(size_y, size_x));
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public void setSize(Sizable)")
    public void setSize_Size_Half() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        size_y /= 2; size_x /= 2;
        grid.setSize(new SimpleSize(size_y, size_x));
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public void setSize(Sizable)")
    public void setSize_Size_Double() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        size_y /= 2; size_x /= 2;
        grid.setSize(new SimpleSize(size_y, size_x));
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public void setWidth(int)")
    public void setWidth_Width_Same() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        grid.setWidth(size_x);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public void setWidth(int)")
    public void setWidth_Width_Half() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        size_x /= 2;
        grid.setWidth(size_x);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public void setWidth(int)")
    public void setWidth_Width_Double() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        grid.setWidth(size_x * 2);
        this.checkPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        this.checkSolid(grid, empty, empty, size_y, size_x, 
                        start_y, start_x + size_x);
    }

    @Test
    @Covers("public void setX(int)")
    public void setX_test() {
        grid.setX(start_x);
        assertTrue(start_x == grid.getX());
        grid.setX(start_x + 100);
        assertTrue(start_x + 100 == grid.getX());
        grid.setX(start_x - 200);
        assertTrue(start_x - 200 == grid.getX());
    }

    @Test
    @Covers("public void setY(int)")
    public void setY_test() {
        grid.setY(start_y);
        assertTrue(start_y == grid.getY());
        grid.setY(start_y + 100);
        assertTrue(start_y + 100 == grid.getY());
        grid.setY(start_y - 200);
        assertTrue(start_y - 200 == grid.getY());
    }

    @Test
    @Covers("public Grid<Z> subGrid(int,int,int,int)")
    public void subGrid_RowsColsYX_Same() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        Grid<Integer> g = grid.subGrid(size_y, size_x, start_y, start_x);
        this.checkPattern(g, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public Grid<Z> subGrid(int,int,int,int)")
    public void subGrid_RowsColsYX_Half() {
        int half_x = size_x / 2;
        int half_y = size_y / 2;
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        Grid<Integer> g = grid.subGrid(half_y, half_x, 
                                       start_y + half_y / 2, 
                                       start_x + half_x / 2);
        // We want half of the visible grid, so our starting position is
        // +y,x. However the pattern itself is not based upon the position,
        // so the pattern offset doesn't need that.
        this.checkPattern(g, half_y, half_x, 
                          half_y / 2 + start_y, half_x / 2 + start_x, 
                          half_y / 2, half_x / 2);
    }


    @Test
    @Covers("public Grid<Z> cutSubGrid(int,int,int,int)")
    public void cutSubGrid_RowsColsYX_Same() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        Grid<Integer> g = grid.cutSubGrid(size_y, size_x, start_y, start_x);
        this.checkPattern(g, size_y, size_x, start_y, start_x, 0, 0);
        this.checkSolid(grid, empty, empty, size_y, size_x, start_y, start_x);
    }

    @Test
    @Covers("public Grid<Z> cutSubGrid(int,int,int,int)")
    public void cutSubGrid_RowsColsYX_Half() {
        int half_x = size_x / 2;
        int half_y = size_y / 2;
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        Grid<Integer> g = grid.cutSubGrid(half_y, half_x,
                                       start_y + half_y / 2,
                                       start_x + half_x / 2);
        // We want half of the visible grid, so our starting position is
        // +y,x. However the pattern itself is not based upon the position,
        // so the pattern offset doesn't need that.
        this.checkPattern(g, half_y, half_x,
                          half_y / 2 + start_y, half_x / 2 + start_x,
                          half_y / 2, half_x / 2);
        this.checkSolid(grid, empty, empty, half_y, half_x,
                                       start_y + half_y / 2,
                                       start_x + half_x / 2);
    }


    @Test
    @Covers("public Grid<Z> copySubGrid(int,int,int,int)")
    public void copySubGrid_RowsColsYX_Same() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        Grid<Integer> g = grid.subGrid(size_y, size_x, start_y, start_x);
        this.checkPattern(g, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public Grid<Z> copySubGrid(int,int,int,int)")
    public void copySubGrid_RowsColsYX_Half() {
        int half_x = size_x / 2;
        int half_y = size_y / 2;
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        Grid<Integer> g = grid.subGrid(half_y, half_x,
                                       start_y + half_y / 2,
                                       start_x + half_x / 2);
        // We want half of the visible grid, so our starting position is
        // +y,x. However the pattern itself is not based upon the position,
        // so the pattern offset doesn't need that.
        this.checkPattern(g, half_y, half_x,
                          half_y / 2 + start_y, half_x / 2 + start_x,
                          half_y / 2, half_x / 2);
    }

    @Test
    @Covers("public Grid<Z> subGrid(Regionlike)")
    public void subGrid_Regionlike_Same() {
        BoxRegion b = new BoxRegion(size_y, size_x, start_y, start_x);
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        Grid<Integer> g = grid.subGrid(b);
        this.checkPattern(g, size_y, size_x, start_y, start_x, 0, 0);
    }

    @Test
    @Covers("public Grid<Z> subGrid(Regionlike)")
    public void subGrid_Regionlike_Half() {
        int half_x = size_x / 2;
        int half_y = size_y / 2;
        BoxRegion b = new BoxRegion(half_y, half_x, 
                                    start_y + half_y / 2, start_x + half_x / 2);
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        Grid<Integer> g = grid.subGrid(b);
        // We want half of the visible grid, so our starting position is
        // +y,x. However the pattern itself is not based upon the position,
        // so the pattern offset doesn't need that.
        this.checkPattern(g, half_y, half_x, 
                          half_y / 2 + start_y, half_x / 2 + start_x, 
                          half_y / 2, half_x / 2);
    }

    @Test
    @Covers("public Z unset(int,int)")
    public void unset_YX() {
        grid.wipe(size_y, size_x, start_y, start_x, empty2);
        assertEquals(grid.get(start_y, start_x),empty2);
        grid.unset(start_y, start_x);
        assertEquals(grid.get(start_y, start_x),empty);
    }

    @Test
    @Covers("public Z unset(Positionable)")
    public void unset_Positionable() {
        grid.wipe(size_y, size_x, start_y, start_x, empty2);
        assertEquals(grid.get(start_y, start_x), empty2);
        Point p = new Point(start_y, start_x);
        grid.unset(p);
        assertEquals(grid.get(start_y, start_x), empty);
    }

    @Test
    @Covers("public void wipe(int,int,int,int)")
    public void wipe_YXYX() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        grid.wipe(size_y, size_x, start_y, start_x);
        this.checkSolid(empty, empty);
    }

    @Test
    @Covers("public void wipe(int,int,int,int,Z)")
    public void wipe_YXYXZ() {
        this.setPattern(grid, size_y, size_x, start_y, start_x, 0, 0);
        grid.wipe(size_y, size_x, start_y, start_x, empty2);
        this.checkSolid(empty2, empty2);
    }

    @Test
    @Covers({"public void setCellCopier(GridCellCopier<Z>)",
        "public GridCellCopier<Z> getCellCopier()",
    })
    public void cellCopier() {
        Integer v = 42;
        Integer a = this.grid.copyCell(v);
        assertSame(v, a);
        assertEquals(v, a);
        GridCellCopier<Integer> initialCopier = this.grid.getCellCopier();
        assertNotNull(initialCopier);
        a = initialCopier.copyCell(v);
        assertSame(v, a);
        assertEquals(v, a);
        GridCellCopier<Integer> flexibleCopier = new FlexibleCellCopier<>();
        this.grid.setCellCopier(flexibleCopier);
        assertNotSame(flexibleCopier, initialCopier);
        assertSame(flexibleCopier, grid.getCellCopier());
        a = flexibleCopier.copyCell(v);
        assertSame(v, a);
        assertEquals(v, a);
    }

    @Test
    @Covers("Object writeReplace() throws ObjectStreamException")
    public void writeReplace() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(grid);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Grid<Integer> aGrid = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            aGrid = (Grid<Integer>) ois.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            throw new RuntimeException(ex);
        }
        assertNotNull(aGrid);
        assertNotSame(grid, aGrid);
        assertEquals(grid, aGrid);
    }


    @Test
    @Covers("Object writeReplace() throws ObjectStreamException")
    public void writeReplace_upgrade() {
        GridData0<Integer> oldData = new GridData0<>();
        ArrayList<ArrayList<Integer>> oldg = new ArrayList<>();
        ArrayList<Integer> row = new ArrayList<>(2);
        row.add(1);
        row.add(2);
        oldg.add(row);
        row = new ArrayList<>(2);
        row.add(3);
        row.add(4);
        oldg.add(row);
        oldData.set("grid", oldg);
        oldData.set("x1", 11);
        oldData.set("y1", 7);
        oldData.set("empty", new Integer(0));

        Grid<Integer> modern = new Grid<>((Integer)0, 2, 2, 0, 0);
        modern.set(0, 0, 1);
        modern.set(0, 1, 2);
        modern.set(1, 0, 3);
        modern.set(1, 1, 4);
        modern.setPosition(7, 11);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(oldData);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Grid<Integer> oldGrid = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            oldGrid = (Grid<Integer>) ois.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            throw new RuntimeException(ex);
        }
        assertNotNull(oldGrid);
        assertNotSame(modern, oldGrid);
        assertEquals(modern, oldGrid);
    }

    @Test
    @Covers("public String toString()")
    public void testToString() {
        String expect = "Grid{start_x=10, start_y=5, size_x=80, size_y=25, "
                + "empty=0, irregular=false, grid=@-749148256, "
                + "cellCopier=com.googlecode.blacken.cell.FlexibleCellCopier}";
        assertEquals(expect, grid.toString());
    }

    @Test
    @Covers("public boolean equals(Object)")
    public void testEquals() {
        setPattern(grid, size_y, size_x, start_y, start_x, start_y, start_x);
        checkPattern(grid, size_y, size_x, start_y, start_x, start_y, start_x);
        Grid<Integer> copy = grid.copySubGrid(size_y, size_x, start_y, start_x);
        checkPattern(copy, size_y, size_x, start_y, start_x, start_y, start_x);
        copy.makeRegular(this.empty);
        assertEquals(grid, copy);
    }

    @Test
    @Covers("public int hashCode()")
    public void testHashCode() {
        int expect = 1472593576;
        assertEquals(expect, grid.hashCode());
    }
}
