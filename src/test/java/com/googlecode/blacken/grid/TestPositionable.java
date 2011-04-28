package com.googlecode.blacken.grid;

import static org.junit.Assert.*;

import org.junit.*;

public class TestPositionable {
    private Positionable data;
    private int base_y;
    private int base_x;
    
    public void setUp(Positionable p, int y, int x) {
        data = p;
        this.base_y = y;
        this.base_x = x;
    }

    @Before
    public void setUp() {
        data = new Point(1, 2);
        this.base_y = 1;
        this.base_x = 2;
    }
    
    @Test
    public void getX() {
        assertTrue(data.getX() == base_x);
    }

    @Test
    public void getY() {
        assertTrue(data.getY() == base_y);
    }

    @Test
    public void setX() {
        assertTrue(data.getX() == base_x);
        data.setX(base_x + 2);
        assertTrue(data.getX() == base_x + 2);
    }

    @Test
    public void setY() {
        assertTrue(data.getY() == base_y);
        data.setY(base_y + 2);
        assertTrue(data.getY() == base_y + 2);
    }

    @Test
    public void getPos() {
        int[] got = data.getPos();
        int[] ideal = {base_y, base_x};
        assertArrayEquals(ideal, got);
    }

    @Test
    public void setPos() {
        int[] ideal1 = {base_y, base_x};
        int[] ideal2 = {base_y + 2, base_x + 2};
        int[] got = data.getPos();
        assertArrayEquals(ideal1, got);
        data.setPos(ideal2[0], ideal2[1]);
        got = data.getPos();
        assertArrayEquals(ideal2, got);
    }

}
