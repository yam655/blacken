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
package com.googlecode.blacken.grid;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * test
 * @author yam655
 */
public class TestRegionlike extends TestPositionable {

    private Regionlike data;
    private int size_x;
    private int size_y;
    private int x;
    private int y;

    /**
     * test
     * @param data regionlike
     * @param height height
     * @param width width
     * @param y coordinate
     * @param x coordinate
     */
    public void setUp(Regionlike data, int height, int width, int y, int x) {
        this.data = data;
        this.size_x = width;
        this.size_y = height;
        this.x = x;
        this.y = y;
        super.setUp(data, y, x);
    }
    
    /**
     * test
     */
    @Override
    @Before
    public void setUp() {
        data = new BoxRegion(10, 20, 100, 200);
        setUp(data, 10, 20, 100, 200);
    }
    
    /**
     * test
     */
    @Test
    public void containsYX() {
        assertTrue(data.contains(y, x));
        assertFalse(data.contains(y -1, x-1));
        assertTrue(data.contains(y + size_y -1, x + size_x -1));
        assertFalse(data.contains(y + size_y, x + size_x));
    }

    /**
     * test
     */
    @Test
    public void containsHWYX() {
        assertTrue(data.contains(size_y, size_x, y, x));
        assertTrue(data.contains(size_y -2, size_x -2, y + 1, x +1));
        assertFalse(data.contains(size_y, size_x, y +2, x+2));
    }

    /**
     * test
     */
    @Test
    public void containsP() {
        Point p = new Point(y, x);
        assertTrue(data.contains(p));
        p = new Point(y - 1, x - 1);
        assertFalse(data.contains(p));
        p = new Point(y + size_y -1, x + size_x -1);
        assertTrue(data.contains(p));
        p = new Point(y + size_y, x + size_x);
        assertFalse(data.contains(p));
    }

    /**
     * test
     */
    @Test
    public void containsR() {
        BoxRegion b = new BoxRegion(size_y, size_x, y, x);
        assertTrue(data.contains(b));
        b = new BoxRegion(size_y -2, size_x -2, y + 1, x +1);
        assertTrue(data.contains(b));
        b = new BoxRegion(size_y, size_x, y +2, x+2);
        assertFalse(data.contains(b));
        b = new BoxRegion(size_y+2, size_x+2, y -1, x-1);
        assertFalse(data.contains(b));
    }

    /**
     * test
     */
    @Test
    public void getBounds() {
        assertTrue(data.getX() == x);
        assertTrue(data.getY() == y);
        assertTrue(data.getWidth() == this.size_x);
        assertTrue(data.getHeight() == this.size_y);
        Regionlike b = data.getBounds();
        assertTrue(b.getX() == x);
        assertTrue(b.getY() == y);
        assertTrue(b.getWidth() == this.size_x);
        assertTrue(b.getHeight() == this.size_y);
    }

    /**
     * test
     */
    @Test
    public void getEdgeIterator() {
        assertNotNull(data.getEdgeIterator());
    }

    /**
     * test
     */
    @Test
    public void getHeight() {
        assertTrue(data.getHeight() == this.size_y);
    }

    /**
     * test
     */
    @Test
    public void getInsideIterator() {
        assertNotNull(data.getInsideIterator());
    }

    /**
     * test
     */
    @Test
    public void getNotOutsideIterator() {
        assertNotNull(data.getNotOutsideIterator());
    }

    /**
     * test
     */
    @Test
    public void getWidth() {
        assertTrue(data.getWidth() == this.size_x);
    }

    /**
     * test
     */
    @Test
    public void intersectsHWYX() {
        // entirely inside
        assertFalse(data.intersects(size_y, size_x, y, x));
        assertFalse(data.intersects(size_y -2, size_x -2, y + 1, x +1));
        // entirely outside
        assertFalse(data.intersects(size_y, size_x, y + size_y, x));
        // intersects
        assertTrue(data.intersects(size_y, size_x, y +1, x+1));
        assertTrue(data.intersects(size_y + 2, size_x+2, y -1, x-1));
    }

    /**
     * test
     */
    @Test
    public void intersectsR() {
        BoxRegion b = new BoxRegion(size_y, size_x, y, x);
        // entirely inside
        assertFalse(data.intersects(b));
        b = new BoxRegion(size_y -2, size_x -2, y + 1, x +1);
        assertFalse(data.intersects(b));
        // entirely outside
        b = new BoxRegion(size_y, size_x, y + size_y, x);
        assertFalse(data.intersects(b));
        // intersects
        b = new BoxRegion(size_y, size_x, y +1, x+1);
        assertTrue(data.intersects(b));
        b = new BoxRegion(size_y + 2, size_x+2, y -1, x-1);
        assertTrue(data.intersects(b));
    }

    /**
     * test
     */
    @Test
    public void setHeight() {
        assertTrue(data.getHeight() == this.size_y);
        data.setHeight(size_y + 2);
        assertTrue(data.getHeight() == this.size_y + 2);
    }

    /**
     * test
     */
    @Test
    public void setWidth() {
        assertTrue(data.getWidth() == this.size_x);
        data.setWidth(size_x + 2);
        assertTrue(data.getWidth() == this.size_x + 2);
    }

}
