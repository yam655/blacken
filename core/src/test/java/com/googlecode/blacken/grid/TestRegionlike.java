/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
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

import org.junit.*;
import static org.junit.Assert.*;

/**
 * test
 * @author Steven Black
 */
public class TestRegionlike {

    private Regionlike data;
    private int size_x;
    private int size_y;
    private int x;
    private int y;
    TestPositionable parent = new TestPositionable();
    

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
        parent.setUp(data, y, x);
    }
    
    /**
     * test
     */
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

    @Test
    public void setPos() {
        parent.setPos();
    }

}
