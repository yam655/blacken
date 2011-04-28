package com.googlecode.blacken.core;

import static org.junit.Assert.*;

import org.junit.*;

public class TestExtRandom {
    ExtRandom r;
    
    @Before
    public void setUp() {
        r = new ExtRandom(0);
    }

    @Test
    public void testGuessEmpty() {
        int got = 0;
        got = r.guess("");
        assertEquals(0, got);
    }
    
    @Test
    public void testPlus() {
        int got = r.guess("+2");
        assertEquals(2, got);
    }

    @Test
    public void testDiv() {
        int got = r.guess("8/4");
        assertEquals(2, got);
    }

    @Test
    public void testMul() {
        int got = r.guess("2*2");
        assertEquals(4, got);
    }

    @Test
    public void testSub() {
        int got = r.guess("6-2");
        assertEquals(4, got);
    }

    @Test
    public void testAdd() {
        int got = r.guess("2+2");
        assertEquals(4, got);
    }
    
    @Test
    public void testBell() {
        int got = r.guess("10b5");
        assertEquals(9, got);
    }
    
    @Test
    public void testDice() {
        int got = r.guess("5d10");
        assertEquals(34, got);
    }
    
    @Test
    public void testBestOfDice() {
        int got = r.guess("3:4d6");
        assertEquals(18, got);
    }
    
    @Test
    public void testBestOfDiceMul() {
        int got = r.guess("3:4d6*50");
        assertEquals(900, got);
    }

}
