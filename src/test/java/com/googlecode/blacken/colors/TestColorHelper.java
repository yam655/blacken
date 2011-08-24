/* blacken - a library for Roguelike games
 * Copyright © 2011 Steven Black <yam655@gmail.com>
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

package com.googlecode.blacken.colors;

import static org.junit.Assert.*;

import org.junit.Test;

import com.googlecode.blacken.exceptions.InvalidStringFormatException;

/**
 * @author yam655
 *
 */
public class TestColorHelper {

    int WHITE = 0xFFFFFFFF;
    int BLACK = 0xFF000000;
    int TRANSPARENT = 0x00000000;
    int NEAR_BLACK = 0x01000000;
    int GREY_HALF = 0xFF7f7f7f;
    int RED = 0xFFFF0000;
    int GREEN = 0xFF00FF00;
    int BLUE = 0xFF0000FF;
    
    /**
     * Test the color helper functions
     */
    public TestColorHelper() {
        // do nothing
        
    }

    /**
     * Is the color fully transparent?
     */
    @Test
    public void isTransparent() {
        assertTrue(ColorHelper.isTransparent(TRANSPARENT));
        assertFalse(ColorHelper.isTransparent(NEAR_BLACK));
        assertFalse(ColorHelper.isTransparent(BLACK));
        assertFalse(ColorHelper.isTransparent(WHITE));
    }

    /**
     * Is the color fully opaque?
     */
    @Test
    public void isOpaque() {
        assertFalse(ColorHelper.isOpaque(TRANSPARENT));
        assertFalse(ColorHelper.isOpaque(NEAR_BLACK));
        assertTrue(ColorHelper.isOpaque(BLACK));
        assertTrue(ColorHelper.isOpaque(WHITE));
    }

    /**
     * For a given color value, decide whether black or white is visible
     */
    @Test
    public void makeVisible_Value() {
        assertEquals(WHITE, ColorHelper.makeVisible(BLACK));
        assertEquals(BLACK, ColorHelper.makeVisible(WHITE));
        assertEquals(WHITE, ColorHelper.makeVisible(NEAR_BLACK));
        assertEquals(WHITE, ColorHelper.makeVisible(TRANSPARENT));
    }

    /**
     * For a given color value, decide whether black or white is visible
     */
    @Test
    public void makeVisible_Value_Threshold() {
        assertEquals(WHITE, ColorHelper.makeVisible(GREY_HALF, 75));
        assertEquals(BLACK, ColorHelper.makeVisible(GREY_HALF, 25));
    }
    /**
     * For a given color value, decide whether "black" or "white" is visible
     */
    @Test
    public void makeVisible_Value_Threshold_White_Black() {
        assertEquals(RED, ColorHelper.makeVisible(GREY_HALF, 75, RED, BLUE));
        assertEquals(BLUE, ColorHelper.makeVisible(GREY_HALF, 25, RED, BLUE));
    }
    
    /**
     * Make a color opaque. (Need not have valid alpha data.)
     */
    @Test
    public void makeOpaque() {
        assertEquals(BLACK, ColorHelper.makeOpaque(TRANSPARENT));
        assertEquals(BLACK, ColorHelper.makeOpaque(NEAR_BLACK));
        assertEquals(BLACK, ColorHelper.makeOpaque(BLACK));
        assertEquals(WHITE, ColorHelper.makeOpaque(WHITE));
    }

    /**
     * Force fully transparent colors to become opaque.
     */
    @Test
    public void neverTransparent() {
        assertEquals(BLACK, ColorHelper.neverTransparent(TRANSPARENT));
        assertEquals(NEAR_BLACK, ColorHelper.neverTransparent(NEAR_BLACK));
        assertEquals(BLACK, ColorHelper.neverTransparent(BLACK));
        assertEquals(WHITE, ColorHelper.neverTransparent(WHITE));
    }

    /**
     * Check to see if there is a reasonable expectation of a valid color.
     */
    @Test
    public void isColorDefinition() {
        assertFalse(ColorHelper.isColorDefinition("#pickle")); //$NON-NLS-1$
        assertTrue(ColorHelper.isColorDefinition("#00FF00")); //$NON-NLS-1$
        assertTrue(ColorHelper.isColorDefinition("#0f0")); //$NON-NLS-1$
        assertTrue(ColorHelper.isColorDefinition("0xFF007f")); //$NON-NLS-1$
        assertTrue(ColorHelper.isColorDefinition("0XFF007f")); //$NON-NLS-1$
        assertFalse(ColorHelper.isColorDefinition("0xOXENFART")); //$NON-NLS-1$
        assertFalse(ColorHelper.isColorDefinition("White")); //$NON-NLS-1$
        assertFalse(ColorHelper.isColorDefinition("0")); //$NON-NLS-1$
    }

    /**
     * Set the color based upon a string. 
     */
    @Test
    public void makeColor_string() {
        try {
            assertEquals(WHITE, ColorHelper.makeColor("0xFFFFFF")); //$NON-NLS-1$
        } catch (InvalidStringFormatException e1) {
            fail();
        }
        try {
            assertEquals(BLACK, ColorHelper.makeColor("#000")); //$NON-NLS-1$
        } catch (InvalidStringFormatException e1) {
            fail();
        }
        try {
            assertEquals(RED, ColorHelper.makeColor("#F00")); //$NON-NLS-1$
        } catch (InvalidStringFormatException e) {
            fail();
        }
        try {
            assertEquals(GREEN, ColorHelper.makeColor("#00FF00")); //$NON-NLS-1$
        } catch (InvalidStringFormatException e) {
            fail();
        }
        try {
            assertEquals(NEAR_BLACK, ColorHelper.makeColor("#01000000")); //$NON-NLS-1$
        } catch (InvalidStringFormatException e1) {
            fail();
        }
    }


    /**
     * Set the color based upon a string. 
     */
    @Test
    public void makeColor_string_fail() {
        try {
            assertEquals(WHITE, ColorHelper.makeColor("0xFFFFFG")); //$NON-NLS-1$
            fail();
        } catch (InvalidStringFormatException e1) {
            // do nothing
        }
        try {
            assertEquals(BLACK, ColorHelper.makeColor("$000")); //$NON-NLS-1$
            fail();
        } catch (InvalidStringFormatException e1) {
            // do nothing
        }
        try {
            assertEquals(TRANSPARENT, ColorHelper.makeColor("#000080000")); //$NON-NLS-1$
            fail();
        } catch (InvalidStringFormatException e1) {
            // do nothing
        }
        try {
            assertEquals(RED, ColorHelper.makeColor("#F800")); //$NON-NLS-1$
            fail();
        } catch (InvalidStringFormatException e) {
            // do nothing
        }
        try {
            assertEquals(GREEN, ColorHelper.makeColor("#00ZF00")); //$NON-NLS-1$
            fail();
        } catch (InvalidStringFormatException e) {
            // do nothing
        }
    }
    
    /**
     * Make an opaque RGB color -- color components have upper value of 255.
     */
    @Test
    public void makeColor_red_green_blue_i() {
        assertEquals(WHITE, ColorHelper.makeColor(255, 255, 255));
        assertEquals(BLACK, ColorHelper.makeColor(0, 0, 0));
        assertEquals(RED, ColorHelper.makeColor(255, 0, 0));        
    }

    /**
     * Make an RGBA color -- color components have upper value of 255.
     */
    @Test
    public void makeColor_red_green_blue_alpha_i() {
        assertEquals(WHITE, ColorHelper.makeColor(255, 255, 255, 255));
        assertEquals(BLACK, ColorHelper.makeColor(0, 0, 0, 255));
        assertEquals(RED, ColorHelper.makeColor(255, 0, 0, 255));        
        assertEquals(NEAR_BLACK, ColorHelper.makeColor(0, 0, 0, 1));
        assertEquals(TRANSPARENT, ColorHelper.makeColor(0, 0, 0, 0));
    }

    /**
     * Make an RGBA color -- color components have upper value of 1.0.
     */
    @Test
    public void makeColor_red_green_blue_alpha_d() {
        assertEquals(WHITE, ColorHelper.makeColor(1., 1., 1., 1.));
        assertEquals(BLACK, ColorHelper.makeColor(0., 0., 0., 1.));
        assertEquals(RED, ColorHelper.makeColor(1., 0., 0., 1.));
        assertEquals(NEAR_BLACK, ColorHelper.makeColor(0., 0., 0., 0.003922));
        assertEquals(TRANSPARENT, ColorHelper.makeColor(0., 0., 0., 0.));
    }

    /**
     * Make an opaque RGBA color -- color components have upper value of 1.0.
     */
    @Test
    public void makeColor_red_green_blue_d() {
        assertEquals(WHITE, ColorHelper.makeColor(1., 1., 1.));
        assertEquals(BLACK, ColorHelper.makeColor(0., 0., 0.));
        assertEquals(RED, ColorHelper.makeColor(1., 0., 0.));
    }

    /**
     * Make an RGBA color using a variable upper value for color components
     */
    @Test
    public void makeColor_red_green_blue_alpha_top() {
        assertEquals(WHITE, ColorHelper.makeColor(15, 15, 15, 15, 15));
        assertEquals(0x77777777, ColorHelper.makeColor(7, 7, 7, 7, 15));
        assertEquals(0x1f1f1f1f, ColorHelper.makeColor(1, 1, 1, 1, 8));
        assertEquals(BLACK, ColorHelper.makeColor(0, 0, 0, 0xffff, 0xffff));
        assertEquals(RED, ColorHelper.makeColor(0x1f000000, 0, 0, 0x1f000000, 0x1f000000));
        assertEquals(NEAR_BLACK, ColorHelper.makeColor(0, 0, 0, 1, 255));
        assertEquals(TRANSPARENT, ColorHelper.makeColor(0, 0, 0, 0, 0));
    }

    /**
     * Split a color out in to the component R,G,B,A values.
     */
    @Test
    public void makeComponents() {
        assertArrayEquals(new int[] {255, 0, 0, 255}, 
                          ColorHelper.makeComponents(RED));
        assertArrayEquals(new int[] {0, 0, 0, 1}, 
                          ColorHelper.makeComponents(NEAR_BLACK));
        assertArrayEquals(new int[] {255, 255, 255, 255}, 
                          ColorHelper.makeComponents(WHITE));
        assertArrayEquals(new int[] {0, 0, 0, 255},
                          ColorHelper.makeComponents(BLACK));
        assertArrayEquals(new int[] {0, 0, 0, 0},
                          ColorHelper.makeComponents(TRANSPARENT));
    }
    
    /**
     * Set the alpha channel for a color.
     */
    @Test
    public void setAlpha() {
        assertEquals(NEAR_BLACK, ColorHelper.setAlpha(BLACK, 1));
        assertEquals(BLACK, ColorHelper.setAlpha(TRANSPARENT, 255));
    }

    /**
     * Get the alpha channel for a color.
     */
    @Test
    public void getAlpha() {
        assertEquals(1, ColorHelper.getAlpha(NEAR_BLACK));
        assertEquals(255, ColorHelper.getAlpha(BLACK));
        assertEquals(0, ColorHelper.getAlpha(TRANSPARENT));
    }

    /**
     * Modify the current alpha value of a color.
     */
    @Test
    public void increaseAlpha() {
        assertEquals(0, ColorHelper.getAlpha(
                ColorHelper.increaseAlpha(TRANSPARENT, 50.0)));
        assertEquals(0xff, ColorHelper.getAlpha(
                ColorHelper.increaseAlpha(NEAR_BLACK, 50000.0)));
        assertEquals(0x7f, ColorHelper.getAlpha(
                ColorHelper.increaseAlpha(BLACK, -50.0)));
    }

    
}
