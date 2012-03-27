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
    public void setPos() {
        int[] ideal1 = {base_y, base_x};
        int[] ideal2 = {base_y + 2, base_x + 2};
        assertEquals(ideal1[0], data.getY());
        assertEquals(ideal1[1], data.getX());
        data.setPosition(ideal2[0], ideal2[1]);
        assertEquals(ideal2[0], data.getY());
        assertEquals(ideal2[1], data.getX());
    }

}
