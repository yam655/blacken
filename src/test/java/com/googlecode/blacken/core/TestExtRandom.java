package com.googlecode.blacken.core;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * test
 */
public class TestExtRandom {
    ExtRandom r;
    
    /**
     * test
     */
    @Before
    public void setUp() {
        r = new ExtRandom(0);
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
