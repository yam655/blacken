/* blacken - a library for Roguelike games
 * Copyright © 2010-2012 Steven Black <yam655@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with this program.  
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.blacken.grid;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * Test
 * 
 * @author yam655
 */
public class GridTest extends TestRegionlike {

    private Integer empty = null;
    private Grid<Integer> grid = null;
    private int size_x;
    private int size_y;
    private int x;
    private int y;
    Grid<Integer> emptygrid = null;
    private Integer empty2;
    /**
     * test
     */
    public GridTest() {
        // do nothing
    }
    /**
     * test
     * @param grid grid
     * @param empty cell template
     * @param height grid height
     * @param width grid width
     * @param y starting Y coord
     * @param x starting X coord
     */
    public void setUp(Grid<Integer> grid, Object empty, 
                      int height, int width, int y, int x) {
        this.grid = grid;
        this.size_x = width;
        this.size_y = height;
        this.x = x;
        this.y = y;
        // empty2 = new CopyableData<Integer>(100);
        empty2 = new Integer(100);
        emptygrid = new Grid<Integer>();
        super.setUp(grid, height, width, y, x);
    }

    /**
     * test
     */
    @Override
    @Before
    public void setUp() {
        empty = 0;
        grid = new Grid<Integer>(empty, 25, 80, 5, 10);
        setUp(grid, empty, 25, 80, 5, 10);
    }

    /**
     * test
     */
    @Test
    public void Grid_test() {
        assertNotNull(emptygrid);
        assertTrue(emptygrid.getHeight() == 0);
        assertTrue(emptygrid.getWidth() == 0);
        assertTrue(emptygrid.getX() == 0);
        assertTrue(emptygrid.getY() == 0);
        try {
            emptygrid.get(0, 0);
            fail("Should have through IndexOutOfBoundsException"); //$NON-NLS-1$
        } catch (IndexOutOfBoundsException e) {
            // do nothing
        }
    }

    /**
     * test
     */
    @Test
    public void Grid_EmptyRowsCols() {
        grid = new Grid<Integer>(empty, size_y, size_x);
        assertNotNull(grid);
        assertTrue(grid.getHeight() == size_y);
        assertTrue(grid.getWidth() == size_x);
        assertTrue(grid.getX() == 0);
        assertTrue(grid.getY() == 0);
        for (int y = 0; y < grid.getHeight(); y ++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                grid.get(y, x).equals(empty);
            }
        }
    }

    /**
     * test
     */
    @Test
    public void Grid_EmptyRowsColsYX() {
        assertNotNull(grid);
        assertTrue(grid.getHeight() == size_y);
        assertTrue(grid.getWidth() == size_x);
        assertTrue(grid.getX() == this.x);
        assertTrue(grid.getY() == this.y);
        for (int y = this.y; y < this.y + grid.getHeight(); y ++) {
            for (int x = this.x; x < this.x + grid.getWidth(); x++) {
                grid.get(y, x).equals(empty);
            }
        }
    }

    /**
     * test
     */
    @Test
    public void box_HeightWidthXYLeftRightTopTlTrBlBrI() {
        grid.box(size_y - 2, size_x - 2, y+1, x+1, 
                 4, 6, 8, 2, 7, 9, 1, 3, 5);
        checkBox();
    }
    
    /**
     * test
     */
    protected void setPattern(Grid<Integer> grid, 
                              int height, int width, int y1, int x1, 
                              int oy, int ox) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid.set(y + y1, x + x1, 
                         new Integer(x + ox + y+ oy));
            }
        }
    }
    /**
     * test
     */
    protected void checkPattern(Grid<Integer> grid, 
                                int height, int width, int y1, int x1, 
                                int oy, int ox) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Integer d = grid.get(y + y1, x + x1);
                assertNotNull(d);
                assertTrue(String.format("(%d,%d) Offset:(%d,%d) Expected: %d Found %d", //$NON-NLS-1$
                                         y, x, oy, ox, y + oy + x + ox, d), 
                                         d.equals(x + y + ox + oy));
            }
        }
    }

    /**
     * test
     */
    protected void checkSolid(Integer inside, Integer border) {
        checkSolid(grid, inside, border, size_y, size_x, y, x);
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
        assertTrue(grid.get(y, x).equals(border));
        assertTrue(grid.get(y2, x2).equals(border));
        Integer i = grid.get(y, x2);
        assertTrue(String.format("(%d,%d) wanted %d; got %d", y, x2, border, i),  //$NON-NLS-1$
                   i.equals(border));
        assertTrue(grid.get(y2, x).equals(border));
        assertTrue(grid.get(y5, x).equals(border));
        assertTrue(grid.get(y, x5).equals(border));
        assertTrue(grid.get(y5, x2).equals(border));
        assertTrue(grid.get(y2, x5).equals(border));
        
        // compare a sample that should be the box detail
        assertTrue(grid.get(y + 1, x + 1).equals(inside));
        assertTrue(grid.get(y2 - 1, x2 - 1).equals(inside));
        assertTrue(grid.get(y + 1, x2 - 1).equals(inside));
        assertTrue(grid.get(y2 - 1, x + 1).equals(inside));
        assertTrue(grid.get(y5, x + 1).equals(inside));
        assertTrue(grid.get(y + 1, x5).equals(inside));
        assertTrue(grid.get(y5, x2 - 1).equals(inside));
        assertTrue(grid.get(y2 - 1, x5).equals(inside));

        // compare a sample that should be the box inside
        assertTrue(grid.get(y5, x5).equals(inside));
        assertTrue(grid.get(y + 2, x + 2).equals(inside));
        assertTrue(grid.get(y2 - 2, x2 - 2).equals(inside));
        assertTrue(grid.get(y + 2, x2 - 2).equals(inside));
        assertTrue(grid.get(y2 - 2, x + 2).equals(inside));
        assertTrue(grid.get(y5, x + 2).equals(inside));
        assertTrue(grid.get(y + 2, x5).equals(inside));
        assertTrue(grid.get(y5, x2 - 2).equals(inside));
        assertTrue(grid.get(y2 - 2, x5).equals(inside));
    }
    
    /**
     * test
     */
    protected void checkBox() {
        int x2 = x + size_x -1;
        int y2 = y + size_y -1;
        int x5 = x + (size_x / 2);
        int y5 = y + (size_y / 2);
        
        // compare a sample that should be outside
        assertTrue(grid.get(y, x) == 0);
        assertTrue(grid.get(y2, x2) == 0);
        assertTrue(grid.get(y, x2) == 0);
        assertTrue(grid.get(y2, x) == 0);
        assertTrue(grid.get(y5, x) == 0);
        assertTrue(grid.get(y, x5) == 0);
        assertTrue(grid.get(y5, x2) == 0);
        assertTrue(grid.get(y2, x5) == 0);
        
        // compare a sample that should be the box detail
        assertTrue(grid.get(y + 1, x + 1) == 7);
        assertTrue(grid.get(y2 - 1, x2 - 1) == 3);
        assertTrue(grid.get(y + 1, x2 - 1) == 9);
        assertTrue(grid.get(y2 - 1, x + 1) == 1);
        assertTrue(grid.get(y5, x + 1) == 4);
        assertTrue(grid.get(y + 1, x5) == 8);
        assertTrue(grid.get(y5, x2 - 1) == 6);
        assertTrue(grid.get(y2 - 1, x5) == 2);

        // compare a sample that should be the box inside
        assertTrue(grid.get(y5, x5) == 5);
        assertTrue(grid.get(y + 2, x + 2) == 5);
        assertTrue(grid.get(y2 - 2, x2 - 2) == 5);
        assertTrue(grid.get(y + 2, x2 - 2) == 5);
        assertTrue(grid.get(y2 - 2, x + 2) == 5);
        assertTrue(grid.get(y5, x + 2) == 5);
        assertTrue(grid.get(y + 2, x5) == 5);
        assertTrue(grid.get(y5, x2 - 2) == 5);
        assertTrue(grid.get(y2 - 2, x5) == 5);
    }
    
    /**
     * test
     */
    @Test
    public void box_RegionlikeLeftRightTopBottomTlTrBrBl() {
        BoxRegion b = new BoxRegion(size_y -2, size_x -2, y+1, x+1);
        grid.box(b, 
                 4, 6, 8, 2, 7, 9, 1, 3, 5);
        checkBox();
    }

    /**
     * test
     */
    @Test
    public void clear_test() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
        grid.clear();
        checkSolid(empty, empty);
    }

    /**
     * test
     */
    @Test
    public void clear_Empty() {
        assertTrue(grid.getEmpty() == empty);
        assertTrue(empty != empty2);
        grid.clear(empty2);
        checkSolid(empty2, empty2);
    }
    /**
     * test
     */
    @Test
    public void contains_YX() {
        assertTrue(grid.contains(y, x));
        assertTrue(grid.contains(y + this.size_y -1, x + this.size_x -1));
        assertFalse(grid.contains(y - 1, x - 1));
        assertFalse(grid.contains(y + this.size_y, x + this.size_x));
    }

    /**
     * test
     */
    @Test
    public void contains_HeightWidthYX() {
        assertTrue(grid.contains(size_y, size_x, y, x));
        assertTrue(grid.contains(size_y -2, size_x -2, y +1, x +1));
        assertFalse(grid.contains(size_y +2, size_x +2, y +1, x +1));
        assertFalse(grid.contains(size_y, size_x, y + size_y, x));
        assertFalse(grid.contains(size_y, size_x, y+2, x));
    }
    /**
     * test
     */
    @Test
    public void contains_Positionable() {
        Point p = new Point(y, x);
        assertTrue(grid.contains(p));
        p = new Point(y + this.size_y -1, x + this.size_x -1);
        assertTrue(grid.contains(p));
        p = new Point(y - 1, x - 1);
        assertFalse(grid.contains(p));
        p = new Point(y + this.size_y, x + this.size_x);
        assertFalse(grid.contains(p));
    }
    
    /**
     * test
     */
    @Test
    public void contains_Regionlike() {
        BoxRegion b = new BoxRegion(size_y, size_x, y, x);
        assertTrue(grid.contains(b));
        assertTrue(grid.contains(size_y -2, size_x -2, y +1, x +1));
        assertFalse(grid.contains(size_y +2, size_x +2, y +1, x +1));
        assertFalse(grid.contains(size_y, size_x, y + size_y, x));
        assertFalse(grid.contains(size_y, size_x, y+2, x));
    }

    /**
     * test
     */
    @Test
    public void get_YX() {
        assertNotNull(grid.get(y, x));
        assertTrue(grid.get(y, x) == empty);
        assertNotNull(grid.get(y + size_y - 1, x + size_x - 1));
        assertTrue(grid.get(y + size_y - 1, x + size_x - 1) == empty);
    }
    /**
     * test
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void get_YX_Exception1() {
        assertNotNull(grid.get(y-1, x-1));
    }

    /**
     * test
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void get_YX_Exception2() {
        assertNotNull(grid.get(y + size_y, x + size_x));
    }

    /**
     * test
     */
    @Test
    public void get_Positionable() {
        Point p = new Point(y, x);
        assertNotNull(grid.get(p));
        assertTrue(grid.get(p) == empty);
        p = new Point(y + size_y - 1, x + size_x - 1);
        assertNotNull(grid.get(p));
        assertTrue(grid.get(p) == empty);
    }
    
    /**
     * test
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void get_Positionable_Exception1() {
        Point p = new Point(y - 1, x - 1);
        assertNotNull(grid.get(p));
    }
    
    /**
     * test
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void get_Positionable_Exception2() {
        Point p = new Point(y + size_y, x + size_x);
        assertNotNull(grid.get(p));
    }
    /**
     * test
     */
    @Test
    public void getBounds_test() {
        assertNotNull(grid.getBounds());
    }
    /**
     * test
     */
    @Test
    public void getEdgeIterator_test() {
        RegionIterator edge = grid.getEdgeIterator();
        int[] p = new int[4];
        boolean[] pattern = null;
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

    /**
     * test
     */
    @Test
    public void getHeight_test() {
        assertTrue(grid.getHeight() == size_y);
    }
    /**
     * test
     */
    @Test
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
                fail("Grid shouldn't be patterned."); //$NON-NLS-1$
            }
            edge.next();
        }
        checkSolid(empty2, empty);
    }
    /**
     * test
     */
    @Test
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
                fail("Grid shouldn't be patterned."); //$NON-NLS-1$
            }
            edge.next();
        }
        checkSolid(empty2, empty2);
    }

    /**
     * test
     */
    @Test
    public void getSize_test() {
        Sizable size = grid.getSize();
        assertEquals(this.size_y, size.getHeight());
        assertEquals(this.size_x, size.getWidth());
    }

    /**
     * test
     */
    @Test
    public void getWidth_test() {
        assertTrue(this.size_x == grid.getWidth());
    }

    /**
     * test
     */
    @Test
    public void getX_test() {
        assertTrue(this.x == grid.getX());
    }

    /**
     * test
     */
    @Test
    public void getY_test() {
        assertTrue(this.y == grid.getY());
    }

    /**
     * test
     */
    @Test
    public void intersects_HeightWidthYX() {
        assertFalse(grid.intersects(size_y, size_x, y, x));
        assertFalse(grid.intersects(size_y -2, size_x -2, y +1, x +1));
        assertTrue(grid.intersects(size_y +2, size_x +2, y +1, x +1));
        assertFalse(grid.intersects(size_y, size_x, y + size_y, x));
        assertTrue(grid.intersects(size_y, size_x, y+2, x));
    }

    /**
     * test
     */
    @Test
    public void intersects_Regionlike() {
        BoxRegion b = new BoxRegion(size_y, size_x, y, x);
        assertFalse(grid.intersects(b));
        b = new BoxRegion(size_y -2, size_x -2, y +1, x +1);
        assertFalse(grid.intersects(b));
        b = new BoxRegion(size_y +2, size_x +2, y +1, x +1);
        assertTrue(grid.intersects(b));
        b = new BoxRegion(size_y, size_x, y + size_y, x);
        assertFalse(grid.intersects(b));
        b = new BoxRegion(size_y, size_x, y+2, x);
        assertTrue(grid.intersects(b));
    }

    /**
     * test
     */
    @Test
    public void like_test() {
        grid.wipe(size_y, size_x, y, x, empty2);
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

    /**
     * test
     */
    @Test
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

    /**
     * test
     */
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
    /**
     * test
     */
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
    /**
     * test
     */
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

    /**
     * test
     */
    @Test
    public void set_YXZ() {
        grid.set(y, x, empty2);
        assertTrue(grid.get(y, x) == empty2);
        empty2 += 100;
        assertFalse(grid.get(y, x) == empty2);
    }

    /**
     * test
     */
    @Test
    public void set_PositionableZ() {
        Point p = new Point(y, x);
        grid.set(p, empty2);
        assertTrue(grid.get(p) == empty2);
        empty2 += 100;
        assertFalse(grid.get(p) == empty2);
    }

    /**
     * test
     */
    @Test
    public void setCopy_YXZ() {
        grid.setCopy(y, x, empty2);
        assertTrue(grid.get(y, x) == empty2);
        empty2 += 100;
        assertFalse(grid.get(y, x) == empty2);
    }

    /**
     * test
     */
    @Test
    public void setCopy_PositionableZ() {
        Point p = new Point(y, x);
        grid.setCopy(p, empty2);
        assertTrue(grid.get(y, x) == empty2);
        empty2 += 100;
        assertFalse(grid.get(y, x) == empty2);
    }

    /**
     * test
     */
    @Test
    public void setHeight_Height_Same() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        grid.setHeight(size_y);
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void setHeight_Height_Half() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        size_y /= 2;
        grid.setHeight(size_y);
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void setHeight_Height_Double() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        grid.setHeight(size_y * 2);
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
        this.checkSolid(grid, empty, empty, size_y, size_x, 
                        y + size_y, x);
    }

    /**
     * test
     */
    @Override
    @Test
    public void setPos() {
        super.setPos();
    }

    /**
     * test
     */
    @Test
    public void setSize_YX_Same() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        grid.setSize(size_y, size_x);
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void setSize_YX_Half() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        size_y /= 2; size_x /= 2;
        grid.setSize(size_y, size_x);
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void setSize_YX_Double() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        size_y /= 2; size_x /= 2;
        grid.setSize(size_y, size_x);
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void setSize_Size_SimpleSize() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        grid.setSize(new SimpleSize(size_y, size_x));
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void setSize_Size_Half() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        size_y /= 2; size_x /= 2;
        grid.setSize(new SimpleSize(size_y, size_x));
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void setSize_Size_Double() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        size_y /= 2; size_x /= 2;
        grid.setSize(new SimpleSize(size_y, size_x));
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void setWidth_Width_Same() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        grid.setWidth(size_x);
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void setWidth_Width_Half() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        size_x /= 2;
        grid.setWidth(size_x);
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void setWidth_Width_Double() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        grid.setWidth(size_x * 2);
        this.checkPattern(grid, size_y, size_x, y, x, 0, 0);
        this.checkSolid(grid, empty, empty, size_y, size_x, 
                        y, x + size_x);
    }

    /**
     * test
     */
    @Test
    public void setX_test() {
        grid.setX(x);
        assertTrue(x == grid.getX());
        grid.setX(x + 100);
        assertTrue(x + 100 == grid.getX());
        grid.setX(x - 200);
        assertTrue(x - 200 == grid.getX());
    }

    /**
     * test
     */
    @Test
    public void setY_test() {
        grid.setY(y);
        assertTrue(y == grid.getY());
        grid.setY(y + 100);
        assertTrue(y + 100 == grid.getY());
        grid.setY(y - 200);
        assertTrue(y - 200 == grid.getY());
    }

    /**
     * test
     */
    @Test
    public void subGrid_RowsColsYX_Same() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        Grid<Integer> g = grid.subGrid(size_y, size_x, y, x);
        this.checkPattern(g, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void subGrid_RowsColsYX_Half() {
        int half_x = size_x / 2;
        int half_y = size_y / 2;
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        Grid<Integer> g = grid.subGrid(half_y, half_x, 
                                       y + half_y / 2, 
                                       x + half_x / 2);
        // We want half of the visible grid, so our starting position is
        // +y,x. However the pattern itself is not based upon the position,
        // so the pattern offset doesn't need that.
        this.checkPattern(g, half_y, half_x, 
                          half_y / 2 + y, half_x / 2 + x, 
                          half_y / 2, half_x / 2);
    }

    /**
     * test
     */
    @Test
    public void subGrid_Regionlike_Same() {
        BoxRegion b = new BoxRegion(size_y, size_x, y, x);
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        Grid<Integer> g = grid.subGrid(b);
        this.checkPattern(g, size_y, size_x, y, x, 0, 0);
    }

    /**
     * test
     */
    @Test
    public void subGrid_Regionlike_Half() {
        int half_x = size_x / 2;
        int half_y = size_y / 2;
        BoxRegion b = new BoxRegion(half_y, half_x, 
                                    y + half_y / 2, x + half_x / 2);
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        Grid<Integer> g = grid.subGrid(b);
        // We want half of the visible grid, so our starting position is
        // +y,x. However the pattern itself is not based upon the position,
        // so the pattern offset doesn't need that.
        this.checkPattern(g, half_y, half_x, 
                          half_y / 2 + y, half_x / 2 + x, 
                          half_y / 2, half_x / 2);
    }

    /**
     * test
     */
    @Test
    public void unset_YX() {
        grid.wipe(size_y, size_x, y, x, empty2);
        assertEquals(grid.get(y, x),empty2);
        grid.unset(y, x);
        assertEquals(grid.get(y, x),empty);
    }

    /**
     * test
     */
    @Test
    public void unset_Positionable() {
        grid.wipe(size_y, size_x, y, x, empty2);
        assertEquals(grid.get(y, x), empty2);
        Point p = new Point(y, x);
        grid.unset(p);
        assertEquals(grid.get(y, x), empty);
    }

    /**
     * test
     */
    @Test
    public void wipe_YXYX() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        grid.wipe(size_y, size_x, y, x);
        this.checkSolid(empty, empty);
    }

    /**
     * test
     */
    @Test
    public void wipe_YXYXZ() {
        this.setPattern(grid, size_y, size_x, y, x, 0, 0);
        grid.wipe(size_y, size_x, y, x, empty2);
        this.checkSolid(empty2, empty2);
    }

}
