/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.blacken.core;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yam655
 */
public class ObligationsTest {

    public ObligationsTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getBlackenLicense method, of class Obligations.
     */
    @Test
    public void testGetBlackenLicense() {
        String result = Obligations.getBlackenLicense();
        assertNotNull(result);
        assertTrue(result.contains("Apache License") && result.contains("Version 2.0"));
    }

    /**
     * Test of getBlackenNotice method, of class Obligations.
     */
    @Test
    public void testGetBlackenNotice() {
        String result = Obligations.getBlackenNotice();
        assertNotNull(result);
        assertTrue(result.contains("https://sites.google.com/site/blackenlib/"));
        assertTrue(result.contains("https://code.google.com/p/blacken/"));
        assertTrue(result.contains("http://www.apache.org/licenses/LICENSE-2.0.html"));
    }

    /**
     * Test of getFontLicense method, of class Obligations.
     */
    @Test
    public void testGetFontLicense() {
        String result = Obligations.getFontLicense();
        assertNotNull(result);
        assertTrue(result.contains("Bitstream Vera Fonts Copyright"));
        assertTrue(result.contains("Arev Fonts Copyright"));
        assertTrue(result.contains("DejaVu changes are in public domain."));
    }
}
