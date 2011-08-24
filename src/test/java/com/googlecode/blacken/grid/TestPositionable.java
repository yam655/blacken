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

import static org.junit.Assert.*;

import org.junit.*;

/**
 * Test
 * @author yam655
 *
 */
public class TestPositionable {
    private Positionable data;
    private int base_y;
    private int base_x;
    
    /**
     * test
     * @param p positionable
     * @param y coordinate
     * @param x coordinate
     */
    public void setUp(Positionable p, int y, int x) {
        data = p;
        this.base_y = y;
        this.base_x = x;
    }

    /**
     * test
     */
    @Before
    public void setUp() {
        data = new Point(1, 2);
        this.base_y = 1;
        this.base_x = 2;
    }
    
    /**
     * test
     */
    @Test
    public void getX() {
        assertTrue(data.getX() == base_x);
    }

    /**
     * test
     */
    @Test
    public void getY() {
        assertTrue(data.getY() == base_y);
    }

    /**
     * test
     */
    @Test
    public void setX() {
        assertTrue(data.getX() == base_x);
        data.setX(base_x + 2);
        assertTrue(data.getX() == base_x + 2);
    }

    /**
     * test
     */
    @Test
    public void setY() {
        assertTrue(data.getY() == base_y);
        data.setY(base_y + 2);
        assertTrue(data.getY() == base_y + 2);
    }

    /**
     * test
     */
    @Test
    public void getPos() {
        int[] got = data.getPos();
        int[] ideal = {base_y, base_x};
        assertArrayEquals(ideal, got);
    }

    /**
     * test
     */
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
