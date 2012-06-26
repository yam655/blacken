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

import com.googlecode.blacken.core.Coverage;
import com.googlecode.blacken.core.Covers;

import static org.junit.Assert.*;

/**
 * Test
 * @author Steven Black
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

    @Before
    public void setUp() {
        data = new Point(1, 2);
        this.base_y = 1;
        this.base_x = 2;
    }
    
    /*
    @Test
    public void testCoverage() {
        Coverage.checkCoverage(Point.class, this.getClass());
    }
    */

    @Test
    @Covers("public Point(int,int)")
    public void point_xy() {
        assertEquals(base_y, data.getY());
        assertEquals(base_x, data.getX());
    }

    @Test
    @Covers("public Point()")
    public void point_void() {
        data = new Point();
        assertEquals(0, data.getY());
        assertEquals(0, data.getX());
    }

    @Test
    @Covers("public Point(Positionable)")
    public void point_Positionable() {
        Point data2 = new Point(12,34);
        data = new Point(data2);
        assertEquals(12, data.getY());
        assertEquals(34, data.getX());
    }
    
    @Test
    @Covers("public int getX()")
    public void getX() {
        assertTrue(data.getX() == base_x);
    }

    @Test
    @Covers("public int getY()")
    public void getY() {
        assertTrue(data.getY() == base_y);
    }

    @Test
    @Covers("public void setX(int)")
    public void setX() {
        assertTrue(data.getX() == base_x);
        data.setX(base_x + 2);
        assertTrue(data.getX() == base_x + 2);
    }

    @Test
    @Covers("public void setY(int)")
    public void setY() {
        assertTrue(data.getY() == base_y);
        data.setY(base_y + 2);
        assertTrue(data.getY() == base_y + 2);
    }

    @Test
    @Covers("public void setPosition(int,int)")
    public void setPos() {
        int[] ideal1 = {base_y, base_x};
        int[] ideal2 = {base_y + 2, base_x + 2};
        assertEquals(ideal1[0], data.getY());
        assertEquals(ideal1[1], data.getX());
        data.setPosition(ideal2[0], ideal2[1]);
        assertEquals(ideal2[0], data.getY());
        assertEquals(ideal2[1], data.getX());
    }

    @Test
    @Covers("public void setPosition(Positionable)")
    public void setPosition_Positionable() {
        int[] ideal1 = {base_y, base_x};
        int[] ideal2 = {base_y + 2, base_x + 2};
        assertEquals(ideal1[0], data.getY());
        assertEquals(ideal1[1], data.getX());
        Point p = new Point(ideal2[0], ideal2[1]);
        data.setPosition(p);
        assertEquals(ideal2[0], data.getY());
        assertEquals(ideal2[1], data.getX());
    }

    @Test
    @Covers("public String toString()")
    public void test_toString() {
        data.setPosition(12, 34);
        assertEquals("Point:(y=12, x=34)", data.toString());
    }
}
