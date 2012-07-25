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
package com.googlecode.blacken.core;

import org.junit.*;

import static org.junit.Assert.assertEquals;

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
        int got;
        got = r.guess("");
        assertEquals(0, got);
    }
    
    /**
     * test
     */
    @Test
    public void testPlus() {
        int got = r.guess("+2");
        assertEquals(2, got);
    }

    /**
     * test
     */
    @Test
    public void testDiv() {
        int got = r.guess("8/4");
        assertEquals(2, got);
    }

    /**
     * test
     */
    @Test
    public void testMul() {
        int got = r.guess("2*2");
        assertEquals(4, got);
    }

    /**
     * test
     */
    @Test
    public void testSub() {
        int got = r.guess("6-2");
        assertEquals(4, got);
    }

    /**
     * test
     */
    @Test
    public void testAdd() {
        int got = r.guess("2+2");
        assertEquals(4, got);
    }
    
    /**
     * test
     */
    @Test
    public void testDice() {
        int got = r.guess("5d10");
        assertEquals(34, got);
    }
    
    /**
     * test
     */
    @Test
    public void testBestOfDice() {
        int got = r.guess("3:4d6");
        assertEquals(18, got);
    }
    
    /**
     * test
     */
    @Test
    public void testBestOfDiceMul() {
        int got = r.guess("3:4d6*50");
        assertEquals(900, got);
    }

}
