package com.googlecode.blacken.grid;

import org.junit.*;
import static org.junit.Assert.*;

public class TestRegionlike extends TestPositionable {

    private Regionlike data;
    private int size_x;
    private int size_y;
    private int x;
    private int y;

    public void setUp(Regionlike data, int height, int width, int y, int x) {
        this.data = data;
        this.size_x = width;
        this.size_y = height;
        this.x = x;
        this.y = y;
        super.setUp(data, y, x);
    }
    
    @Before
    public void setUp() {
        data = new BoxRegion(10, 20, 100, 200);
        setUp(data, 10, 20, 100, 200);
    }
    
    @Test
    public void containsYX() {
        assertTrue(data.contains(y, x));
        assertFalse(data.contains(y -1, x-1));
        assertTrue(data.contains(y + size_y -1, x + size_x -1));
        assertFalse(data.contains(y + size_y, x + size_x));
    }

    @Test
    public void containsHWYX() {
        assertTrue(data.contains(size_y, size_x, y, x));
        assertTrue(data.contains(size_y -2, size_x -2, y + 1, x +1));
        assertFalse(data.contains(size_y, size_x, y +2, x+2));
    }

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

    @Test
    public void getEdgeIterator() {
        assertNotNull(data.getEdgeIterator());
    }

    @Test
    public void getHeight() {
        assertTrue(data.getHeight() == this.size_y);
    }

    @Test
    public void getInsideIterator() {
        assertNotNull(data.getInsideIterator());
    }

    @Test
    public void getNotOutsideIterator() {
        assertNotNull(data.getNotOutsideIterator());
    }

    @Test
    public void getWidth() {
        assertTrue(data.getWidth() == this.size_x);
    }

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

    @Test
    public void setHeight() {
        assertTrue(data.getHeight() == this.size_y);
        data.setHeight(size_y + 2);
        assertTrue(data.getHeight() == this.size_y + 2);
    }

    @Test
    public void setWidth() {
        assertTrue(data.getWidth() == this.size_x);
        data.setWidth(size_x + 2);
        assertTrue(data.getWidth() == this.size_x + 2);
    }

}
