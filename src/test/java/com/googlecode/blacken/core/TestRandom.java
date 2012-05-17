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
package com.googlecode.blacken.core;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * test
 */
public class TestRandom {
    Random r;
    
    /**
     * test
     */
    @Before
    public void setUp() {
        r = new Random(0);
    }

    /**
     * test
     */
    @Test
    public void testGuessEmpty() {
        int got = 0;
        got = r.guess(""); //$NON-NLS-1$
        assertEquals(0, got);
    }
    
    /**
     * test
     */
    @Test
    public void testPlus() {
        int got = r.guess("+2"); //$NON-NLS-1$
        assertEquals(2, got);
    }

    /**
     * test
     */
    @Test
    public void testDiv() {
        int got = r.guess("8/4"); //$NON-NLS-1$
        assertEquals(2, got);
    }

    /**
     * test
     */
    @Test
    public void testMul() {
        int got = r.guess("2*2"); //$NON-NLS-1$
        assertEquals(4, got);
    }

    /**
     * test
     */
    @Test
    public void testSub() {
        int got = r.guess("6-2"); //$NON-NLS-1$
        assertEquals(4, got);
    }

    /**
     * test
     */
    @Test
    public void testAdd() {
        int got = r.guess("2+2"); //$NON-NLS-1$
        assertEquals(4, got);
    }
    
    /**
     * test
     */
    @Test
    public void testDice() {
        int got = r.guess("5d10"); //$NON-NLS-1$
        assertEquals(34, got);
    }
    
    /**
     * test
     */
    @Test
    public void testBestOfDice() {
        int got = r.guess("3:4d6"); //$NON-NLS-1$
        assertEquals(18, got);
    }
    
    /**
     * test
     */
    @Test
    public void testBestOfDiceMul() {
        int got = r.guess("3:4d6*50"); //$NON-NLS-1$
        assertEquals(900, got);
    }

}
