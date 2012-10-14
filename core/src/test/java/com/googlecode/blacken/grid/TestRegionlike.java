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
import com.googlecode.blacken.core.Coverage;
import com.googlecode.blacken.core.Covers;

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
    
    @Before
    public void setUp() {
        data = new BoxRegion(10, 20, 100, 200);
        setUp(data, 10, 20, 100, 200);
    }

    @Test
    public void testCoverage() {
        Coverage.checkCoverageDeep(Regionlike.class, this.getClass());
    }

    @Test
    @Covers("public abstract boolean contains(int,int)")
    public void containsYX() {
        assertTrue(data.contains(y, x));
        assertFalse(data.contains(y -1, x-1));
        assertTrue(data.contains(y + size_y -1, x + size_x -1));
        assertFalse(data.contains(y + size_y, x + size_x));
    }

    @Test
    @Covers("public abstract boolean contains(int,int,int,int)")
    public void containsHWYX() {
        assertTrue(data.contains(size_y, size_x, y, x));
        assertTrue(data.contains(size_y -2, size_x -2, y + 1, x +1));
        assertFalse(data.contains(size_y, size_x, y +2, x+2));
    }

    @Test
    @Covers("public abstract boolean contains(Positionable)")
    public void contains_Positionable() {
        Point p = new Point(y, x);
        assertTrue(data.contains(p));
        p = new Point(y - 1, x - 1);
        assertFalse(data.contains(p));
        p = new Point(y + size_y -1, x + size_x -1);
        assertTrue(data.contains(p));
        p = new Point(y + size_y, x + size_x);
        assertFalse(data.contains(p));
    }

    @Test
    @Covers("public abstract boolean contains(int[])")
    public void contains_intArr() {
        int[] p = new int[] {y, x};
        assertTrue(data.contains(p));
        p = new int[] {y + this.size_y -1, x + this.size_x -1};
        assertTrue(data.contains(p));
        p = new int[] {y - 1, x - 1};
        assertFalse(data.contains(p));
        p = new int[] {y + this.size_y, x + this.size_x};
        assertFalse(data.contains(p));
    }

    @Test
    @Covers("public abstract boolean contains(Regionlike)")
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


    @Test
    @Covers("public abstract Regionlike getBounds()")
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

    @Test
    @Covers("public abstract RegionIterator getEdgeIterator()")
    public void getEdgeIterator() {
        assertNotNull(data.getEdgeIterator());
    }

    @Test
    @Covers("public abstract int getHeight()")
    public void getHeight() {
        assertTrue(data.getHeight() == this.size_y);
    }

    @Test
    @Covers("public abstract RegionIterator getInsideIterator()")
    public void getInsideIterator() {
        assertNotNull(data.getInsideIterator());
    }

    @Test
    @Covers("public abstract RegionIterator getNotOutsideIterator()")
    public void getNotOutsideIterator() {
        assertNotNull(data.getNotOutsideIterator());
    }

    @Test
    @Covers("public abstract int getWidth()")
    public void getWidth() {
        assertTrue(data.getWidth() == this.size_x);
    }

    @Test
    @Covers("public abstract boolean intersects(int,int,int,int)")
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

    @Test
    @Covers("public abstract boolean intersects(Regionlike)")
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

    @Test
    @Covers("public abstract void setHeight(int)")
    public void setHeight() {
        assertTrue(data.getHeight() == this.size_y);
        data.setHeight(size_y + 2);
        assertTrue(data.getHeight() == this.size_y + 2);
    }

    @Test
    @Covers("public abstract void setWidth(int)")
    public void setWidth() {
        assertTrue(data.getWidth() == this.size_x);
        data.setWidth(size_x + 2);
        assertTrue(data.getWidth() == this.size_x + 2);
    }

    @Test
    @Covers("public abstract void setPosition(Positionable)")
    public void setPos() {
        parent.setPos();
    }


    @Test
    @Covers("public abstract void setBounds(Regionlike)")
    public void setBounds() {
        assertEquals(size_y, data.getHeight());
        assertEquals(size_x, data.getWidth());
        assertEquals(y, data.getY());
        assertEquals(x, data.getX());
        BoxRegion region = new BoxRegion(size_y - 1, size_x - 1, y + 1, x + 1);
        data.setBounds(region);
        assertEquals(size_y -1, data.getHeight());
        assertEquals(size_x -1, data.getWidth());
        assertEquals(x + 1, data.getX());
        assertEquals(y + 1, data.getY());
    }


    @Test
    @Covers("public abstract void setBounds(int,int,int,int)")
    public void setBounds_h_w_y_x() {
        assertEquals(size_y, data.getHeight());
        assertEquals(size_x, data.getWidth());
        assertEquals(y, data.getY());
        assertEquals(x, data.getX());
        data.setBounds(size_y - 1, size_x - 1, y + 1, x + 1);
        assertEquals(size_y -1, data.getHeight());
        assertEquals(size_x -1, data.getWidth());
        assertEquals(x + 1, data.getX());
        assertEquals(y + 1, data.getY());
    }

    @Test
    @Covers("public abstract Positionable getPosition()")
    public void getPosition() {
        Positionable e = new Point(y, x);
        assertEquals(e, data.getPosition());
    }
    @Test
    @Covers({"public abstract void setY(int)",
             "public abstract int getY()",
    })
    public void setY() {
        assertEquals(y, data.getY());
        data.setY(y+1);
        assertEquals(y+1, data.getY());
    }
    @Test
    @Covers({"public abstract int getX()",
             "public abstract void setX(int)",
    })
    public void setX() {
        assertEquals(data.getX(), x);
        data.setX(x+1);
        assertEquals(data.getX(), x+1);
    }
    @Test
    @Covers("public abstract void setPosition(int,int)")
    public void setPosition() {
        Positionable e = new Point(y, x);
        assertEquals(e, data.getPosition());
        e.setY(y+1);
        e.setX(x+1);
        data.setPosition(e);
        assertEquals(e, data.getPosition());
    }
    @Test
    @Covers("public abstract void setSize(Sizable)")
    public void setSize_sizable() {
        assertEquals(size_y, data.getHeight());
        assertEquals(size_x, data.getWidth());
        Sizable s = new SimpleSize(size_y - 1, size_x - 1);
        data.setSize(s);
        assertEquals(size_y -1, data.getHeight());
        assertEquals(size_x -1, data.getWidth());
    }
    @Test
    @Covers("public abstract void setSize(int,int)")
    public void setSize_h_w() {
        assertEquals(size_y, data.getHeight());
        assertEquals(size_x, data.getWidth());
        data.setSize(size_y - 1, size_x - 1);
        assertEquals(size_y -1, data.getHeight());
        assertEquals(size_x -1, data.getWidth());
    }
    @Test
    @Covers("public abstract Sizable getSize()")
    public void getSize() {
        assertEquals(size_y, data.getHeight());
        assertEquals(size_x, data.getWidth());
        Sizable e = new SimpleSize(size_y, size_x);
        assertEquals(e, data.getSize());
    }


}
