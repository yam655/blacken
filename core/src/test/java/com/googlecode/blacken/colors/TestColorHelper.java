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
package com.googlecode.blacken.colors;

import com.googlecode.blacken.core.Coverage;
import com.googlecode.blacken.core.Covers;
import com.googlecode.blacken.exceptions.InvalidStringFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Steven Black
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
    @Covers("public static boolean isTransparent(int)")
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
    @Covers("public static boolean isOpaque(int)")
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
    @Covers("public static int makeVisible(int)")
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
    @Covers("public static int makeVisible(int,int)")
    public void makeVisible_Value_Threshold() {
        assertEquals(WHITE, ColorHelper.makeVisible(GREY_HALF, 75));
        assertEquals(BLACK, ColorHelper.makeVisible(GREY_HALF, 25));
    }
    /**
     * For a given color value, decide whether "black" or "white" is visible
     */
    @Test
    @Covers("public static Integer makeVisible(int,int,int,int)")
    public void makeVisible_Value_Threshold_White_Black() {
        assertEquals(RED, 
                (int)ColorHelper.makeVisible(GREY_HALF, 75, RED, BLUE));
        assertEquals(BLUE, 
                (int)ColorHelper.makeVisible(GREY_HALF, 25, RED, BLUE));
    }
    
    /**
     * Make a color opaque. (Need not have valid alpha data.)
     */
    @Test
    @Covers("public static int makeOpaque(int)")
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
    @Covers("public static int neverTransparent(int)")
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
    @Covers("public static boolean isColorDefinition(String)")
    public void isColorDefinition() {
        assertFalse(ColorHelper.isColorDefinition("#pickle"));
        assertTrue(ColorHelper.isColorDefinition("#00FF00"));
        assertTrue(ColorHelper.isColorDefinition("#0f0"));
        assertTrue(ColorHelper.isColorDefinition("0xFF007f"));
        assertTrue(ColorHelper.isColorDefinition("0XFF007f"));
        assertFalse(ColorHelper.isColorDefinition("0xOXENFART"));
        assertFalse(ColorHelper.isColorDefinition("White"));
        assertFalse(ColorHelper.isColorDefinition("0"));
    }

    /**
     * Set the color based upon a string. 
     */
    @Test
    @Covers("public static int makeColor(String) throws InvalidStringFormatException")
    public void makeColor_string() {
        try {
            assertEquals(WHITE, ColorHelper.makeColor("0xFFFFFF"));
        } catch (InvalidStringFormatException e1) {
            fail();
        }
        try {
            assertEquals(BLACK, ColorHelper.makeColor("#000"));
        } catch (InvalidStringFormatException e1) {
            fail();
        }
        try {
            assertEquals(RED, ColorHelper.makeColor("#F00"));
        } catch (InvalidStringFormatException e) {
            fail();
        }
        try {
            assertEquals(GREEN, ColorHelper.makeColor("#00FF00"));
        } catch (InvalidStringFormatException e) {
            fail();
        }
        try {
            assertEquals(NEAR_BLACK, ColorHelper.makeColor("#01000000"));
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
            assertEquals(WHITE, ColorHelper.makeColor("0xFFFFFG"));
            fail();
        } catch (InvalidStringFormatException e1) {
            // do nothing
        }
        try {
            assertEquals(BLACK, ColorHelper.makeColor("$000"));
            fail();
        } catch (InvalidStringFormatException e1) {
            // do nothing
        }
        try {
            assertEquals(TRANSPARENT, ColorHelper.makeColor("#000080000"));
            fail();
        } catch (InvalidStringFormatException e1) {
            // do nothing
        }
        try {
            assertEquals(RED, ColorHelper.makeColor("#F800"));
            fail();
        } catch (InvalidStringFormatException e) {
            // do nothing
        }
        try {
            assertEquals(GREEN, ColorHelper.makeColor("#00ZF00"));
            fail();
        } catch (InvalidStringFormatException e) {
            // do nothing
        }
    }
    
    /**
     * Make an opaque RGB color -- color components have upper value of 255.
     * @deprecated testing deprecated function
     */
    @Test
    @Covers("public static int makeColor(int,int,int)")
    @Deprecated
    public void makeColor_red_green_blue_i() {
        assertEquals(WHITE, ColorHelper.makeColor(255, 255, 255));
        assertEquals(BLACK, ColorHelper.makeColor(0, 0, 0));
        assertEquals(RED, ColorHelper.makeColor(255, 0, 0));        
    }

    /**
     * Make an RGBA color -- color components have upper value of 255.
     * @deprecated testing deprecated function
     */
    @Test
    @Covers("public static int makeColor(int,int,int,int)")
    @Deprecated
    public void makeColor_red_green_blue_alpha_i() {
        assertEquals(WHITE, ColorHelper.makeColor(255, 255, 255, 255));
        assertEquals(BLACK, ColorHelper.makeColor(0, 0, 0, 255));
        assertEquals(RED, ColorHelper.makeColor(255, 0, 0, 255));        
        assertEquals(NEAR_BLACK, ColorHelper.makeColor(0, 0, 0, 1));
        assertEquals(TRANSPARENT, ColorHelper.makeColor(0, 0, 0, 0));
    }

    @Test
    @Covers("public static int colorFromComponents(int[])")
    public void colorFromComponents_intarr() {
        assertEquals(WHITE, ColorHelper.colorFromComponents(new int[] {255, 255, 255, 255}));
        assertEquals(BLACK, ColorHelper.colorFromComponents(new int[] {0, 0, 0, 255}));
        assertEquals(RED, ColorHelper.colorFromComponents(new int[] {255, 0, 0, 255}));
        assertEquals(NEAR_BLACK, ColorHelper.colorFromComponents(new int[] {0, 0, 0, 1}));
        assertEquals(TRANSPARENT, ColorHelper.colorFromComponents(new int[] {0, 0, 0, 0}));
    }

    @Test
    @Covers("public static float[] colorToFloatComponents(int)")
    public void colorToFloatComponents() {
        float[] expect = {1.0F, 1.0F, 1.0F, 1.0F};
        assertArrayEquals(expect, ColorHelper.colorToFloatComponents(WHITE), 0F);
        expect = new float[] {0F, 0F, 0F, 1.0F};
        assertArrayEquals(expect, ColorHelper.colorToFloatComponents(BLACK), 0F);
        expect = new float[] {1.0F, 0F, 0F, 1.0F};
        assertArrayEquals(expect, ColorHelper.colorToFloatComponents(RED), 0F);
        expect = new float[] {0F, 0F, 0F, 1F/255F};
        assertArrayEquals(expect, ColorHelper.colorToFloatComponents(NEAR_BLACK), 0F);
        expect = new float[] {0F, 0F, 0F, 0F};
        assertArrayEquals(expect, ColorHelper.colorToFloatComponents(TRANSPARENT), 0F);
    }

    @Test
    @Covers("public static int colorFromFloatComponents(float[])")
    public void colorFromFloatComponents_floatarr() {
        float[] value = {1.0F, 1.0F, 1.0F, 1.0F};
        assertEquals(WHITE, ColorHelper.colorFromFloatComponents(value));
        value = new float[] {0F, 0F, 0F, 1.0F};
        assertEquals(BLACK, ColorHelper.colorFromFloatComponents(value));
        value = new float[] {1.0F, 0F, 0F, 1.0F};
        assertEquals(RED, ColorHelper.colorFromFloatComponents(value));
        value = new float[] {0F, 0F, 0F, 1F/255F};
        assertEquals(NEAR_BLACK, ColorHelper.colorFromFloatComponents(value));
        value = new float[] {0F, 0F, 0F, 0F};
        assertEquals(TRANSPARENT, ColorHelper.colorFromFloatComponents(value));
    }

    @Test
    @Covers("public static int colorFromFloatComponents(float,float,float,Float)")
    public void colorFromFloatComponents_floats() {
        int got = ColorHelper.colorFromFloatComponents(1.0F, 1.0F, 1.0F, 1.0F);
        assertEquals(WHITE, got);
        got = ColorHelper.colorFromFloatComponents(0F, 0F, 0F, 1.0F);
        assertEquals(BLACK, got);
        got = ColorHelper.colorFromFloatComponents(1.0F, 0F, 0F, 1.0F);
        assertEquals(RED, got);
        got = ColorHelper.colorFromFloatComponents(0F, 0F, 0F, 1F/255F);
        assertEquals(NEAR_BLACK, got);
        got = ColorHelper.colorFromFloatComponents(0F, 0F, 0F, 0F);
        assertEquals(TRANSPARENT, got);
    }

    @Test
    @Covers("public static void repeatForPalette(ColorPalette,int,int,ColorTransformer)")
    public void repeatForPalette_endidx() {
        class transformer implements ColorTransformer {
            float value = 0;
            transformer(float v) {
                this.value = v;
            }
            @Override
            public int transform(int rgba) {
                return ColorHelper.saturate(rgba, value);
            }
        }
        ColorPalette pal = new ColorPalette();
        pal.add(BLACK);
        pal.add(WHITE);
        pal.add(RED);
        pal.add(GREEN);
        pal.add(BLUE);
        ColorHelper.repeatForPalette(pal, 0, -1, new transformer(-0.5F));
        assertEquals(BLACK, (int)pal.get(0));
        assertEquals(WHITE, (int)pal.get(1));
        assertEquals(0xFFff7f7f, (int)pal.get(2));
        assertEquals(0xFF7fff7f, (int)pal.get(3));
        assertEquals(0xFF7f7fff, (int)pal.get(4));
    }
    @Test
    @Covers("public static void repeatForPalette(ColorPalette,int,ColorTransformer,Integer)")
    public void repeatForPalette_length() {
        class transformer implements ColorTransformer {
            float value = 0;
            transformer(float v) {
                this.value = v;
            }
            @Override
            public int transform(int rgba) {
                return ColorHelper.saturate(rgba, value);
            }
        }
        ColorPalette pal = new ColorPalette();
        pal.add(BLACK);
        pal.add(WHITE);
        pal.add(RED);
        pal.add(GREEN);
        pal.add(BLUE);
        ColorHelper.repeatForPalette(pal, 0, new transformer(-0.5F), 5);
        assertEquals(BLACK, (int)pal.get(0));
        assertEquals(WHITE, (int)pal.get(1));
        assertEquals(0xFFff7f7f, (int)pal.get(2));
        assertEquals(0xFF7fff7f, (int)pal.get(3));
        assertEquals(0xFF7f7fff, (int)pal.get(4));
    }

    @Test
    @Covers("public static int colorFromComponents(int,int,int,Integer)")
    public void colorFromComponents_r_g_b_a() {
        assertEquals(WHITE, ColorHelper.colorFromComponents(255, 255, 255, 255));
        assertEquals(BLACK, ColorHelper.colorFromComponents(0, 0, 0, 255));
        assertEquals(RED, ColorHelper.colorFromComponents(255, 0, 0, 255));
        assertEquals(NEAR_BLACK, ColorHelper.colorFromComponents(0, 0, 0, 1));
        assertEquals(TRANSPARENT, ColorHelper.colorFromComponents(0, 0, 0, 0));
    }

    /**
     * Make an RGBA color -- color components have upper value of 1.0.
     * @deprecated testing deprecated function
     */
    @Test
    @Covers("public static int makeColor(float,float,float,float)")
    @Deprecated
    public void makeColor_red_green_blue_alpha_d() {
        assertEquals(WHITE, ColorHelper.makeColor(1.F, 1.F, 1.F, 1.F));
        assertEquals(BLACK, ColorHelper.makeColor(0.F, 0.F, 0.F, 1.F));
        assertEquals(RED, ColorHelper.makeColor(1.F, 0.F, 0.F, 1.F));
        assertEquals(NEAR_BLACK, ColorHelper.makeColor(0.F, 0.F, 0.F, 0.003922F));
        assertEquals(TRANSPARENT, ColorHelper.makeColor(0.F, 0.F, 0.F, 0.F));
    }

    /**
     * Make an opaque RGBA color -- color components have upper value of 1.0.
     * @deprecated testing deprecated function
     */
    @Test
    @Deprecated
    @Covers("public static int makeColor(float,float,float)")
    public void makeColor_red_green_blue_d() {
        assertEquals(WHITE, ColorHelper.makeColor(1.F, 1.F, 1.F));
        assertEquals(BLACK, ColorHelper.makeColor(0.F, 0.F, 0.F));
        assertEquals(RED, ColorHelper.makeColor(1.F, 0.F, 0.F));
    }

    /**
     * Make an RGBA color using a variable upper value for color components
     */
    @Test
    @Covers("public static int makeColor(int,int,int,int,int)")
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
     * @deprecated test for deprecated calls
     */
    @Test
    @Covers("public static int[] makeComponents(int)")
    @Deprecated
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

    @Test
    @Covers("public static int[] colorToComponents(int)")
    public void colorToComponents() {
        assertArrayEquals(new int[] {255, 0, 0, 255},
                          ColorHelper.colorToComponents(RED));
        assertArrayEquals(new int[] {0, 0, 0, 1},
                          ColorHelper.colorToComponents(NEAR_BLACK));
        assertArrayEquals(new int[] {255, 255, 255, 255},
                          ColorHelper.colorToComponents(WHITE));
        assertArrayEquals(new int[] {0, 0, 0, 255},
                          ColorHelper.colorToComponents(BLACK));
        assertArrayEquals(new int[] {0, 0, 0, 0},
                          ColorHelper.colorToComponents(TRANSPARENT));
    }

    /**
     * Set the alpha channel for a color.
     */
    @Test
    @Covers("public static int setAlpha(int,int)")
    public void setAlpha() {
        assertEquals(NEAR_BLACK, ColorHelper.setAlpha(BLACK, 1));
        assertEquals(BLACK, ColorHelper.setAlpha(TRANSPARENT, 255));
    }

    /**
     * Get the alpha channel for a color.
     */
    @Test
    @Covers("public static int getAlpha(int)")
    public void getAlpha() {
        assertEquals(1, ColorHelper.getAlpha(NEAR_BLACK));
        assertEquals(255, ColorHelper.getAlpha(BLACK));
        assertEquals(0, ColorHelper.getAlpha(TRANSPARENT));
    }

    /**
     * Modify the current alpha value of a color.
     */
    @Test
    @Covers("public static int increaseAlpha(int,double)")
    public void increaseAlpha() {
        assertEquals(0, ColorHelper.getAlpha(
                ColorHelper.increaseAlpha(TRANSPARENT, 50.0)));
        assertEquals(0xff, ColorHelper.getAlpha(
                ColorHelper.increaseAlpha(NEAR_BLACK, 50000.0)));
        assertEquals(0x7f, ColorHelper.getAlpha(
                ColorHelper.increaseAlpha(BLACK, -50.0)));
    }

    @Test
    public void testCoverage() {
        Coverage.checkCoverage(ColorHelper.class, this.getClass());
    }

    @Test
    @Covers("public static int subtract(int,int)")
    public void subtract() {
        int expect = 0xee080020;
        int found = ColorHelper.subtract(0xfe103322, 0x10084002);
        assertEquals(expect, found);
    }

    @Test
    @Covers("public static int add(int,int)")
    public void add() {
        int result = ColorHelper.add(0xff001fee, 0x0022e111);
        assertEquals(0xff22ffff, result);
    }

    @Test
    @Covers("public static Map<String, Integer> colorToComponents(int,Map<String, Integer>)")
    public void colorToComponents_map() {
        Map<String, Integer> value = ColorHelper.colorToComponents(0x11223344, null);
        assertEquals(4, value.size());
        assertEquals(0x11, (int)value.get("alpha"));
        assertEquals(0x22, (int)value.get("red"));
        assertEquals(0x33, (int)value.get("green"));
        assertEquals(0x44, (int)value.get("blue"));
    }
    @Test
    @Covers("public static int colorFromComponents(Map<String, Integer>)")
    public void colorFromComponents_map() {
        Map<String, Integer> value = ColorHelper.colorToComponents(0x11223344, null);
        assertEquals(4, value.size());
        assertEquals(0x11, (int)value.get("alpha"));
        assertEquals(0x22, (int)value.get("red"));
        assertEquals(0x33, (int)value.get("green"));
        assertEquals(0x44, (int)value.get("blue"));
        assertEquals(0x11223344, ColorHelper.colorFromComponents(value));
    }

    @Test
    @Covers("public static int multiply(int,int)")
    public void multiple_int_int() {
        assertEquals(0xff7f0000, ColorHelper.multiply(0xffff0000, 0xff7f7f7f));
        assertEquals(0xff007f00, ColorHelper.multiply(0xff00ff00, 0xff7f7f7f));
        assertEquals(0xff00007f, ColorHelper.multiply(0xff0000ff, 0xff7f7f7f));
    }
    @Test
    @Covers("public static int multiply(int,float)")
    public void multiply_int_float() {
        assertEquals("0xff7f0000", String.format("0x%08x", ColorHelper.multiply(0xffff0000, 0.5F)));
        assertEquals("0xff007f00", String.format("0x%08x", ColorHelper.multiply(0xff00ff00, 0.5F)));
        assertEquals("0xff00007f", String.format("0x%08x", ColorHelper.multiply(0xff0000ff, 0.5F)));
    }

    @Test
    @Covers("public static int lerp(int,int,float)")
    public void lerp_a_b_float() {
        assertEquals(BLACK, ColorHelper.lerp(BLACK, WHITE, 0.0F));
        assertEquals(GREY_HALF, ColorHelper.lerp(BLACK, WHITE, 127.0F/255.0F));
        assertEquals(WHITE, ColorHelper.lerp(BLACK, WHITE, 1.0F));
    }

    @Test
    @Covers("public static int lerp(int,int)")
    public void lerp_a_b() {
        assertEquals(BLACK, ColorHelper.lerp(BLACK, 0x00ffffff));
        assertEquals(GREY_HALF, ColorHelper.lerp(BLACK, 0x7fffffff));
        assertEquals(WHITE, ColorHelper.lerp(BLACK, 0xffffffff));
    }
    @Test
    @Covers("public static int lerp(int,int,float[])")
    public void lerp_a_b_floatarr() {
        assertEquals(BLACK, ColorHelper.lerp(BLACK, WHITE,
                new float[] {0F, 0F, 0F, 0F}));
        assertEquals(GREY_HALF, ColorHelper.lerp(BLACK, WHITE,
                new float[] {127.0F/255.0F, 127.0F/255.0F, 127.0F/255.0F}));
        assertEquals(WHITE, ColorHelper.lerp(BLACK, WHITE,
                new float[] {1.0F, 1.0F, 1.0F, 1.0F}));
        assertEquals(RED, ColorHelper.lerp(BLACK, WHITE,
                new float[] {1.0F, 0F, 0F, 0F}));
    }
    @Test
    @Covers("public static int lerp(int,int,int)")
    @Deprecated
    public void lerp_a_b_int() {
        assertEquals(BLACK, ColorHelper.lerp(BLACK, WHITE,
                ColorHelper.colorFromFloatComponents(0F, 0F, 0F, 0F)));
        assertEquals(GREY_HALF, ColorHelper.lerp(BLACK, WHITE,
                ColorHelper.colorFromFloatComponents(127.0F/255.0F,
                127.0F/255.0F, 127.0F/255.0F, 0F)));
        assertEquals(WHITE, ColorHelper.lerp(BLACK, WHITE,
                ColorHelper.colorFromFloatComponents(1.0F, 1.0F, 1.0F, 1.0F)));
        assertEquals(RED, ColorHelper.lerp(BLACK, WHITE,
                ColorHelper.colorFromFloatComponents(1.0F, 0F, 0F, 0F)));
    }

    @Test
    @Covers("public static int saturate(int,float)")
    public void saturate() {
        assertEquals("0xffff7f7f", String.format("0x%08x", ColorHelper.saturate(RED, -0.5F)));
        assertEquals("0xff7fff7f", String.format("0x%08x", ColorHelper.saturate(GREEN, -0.5F)));
        assertEquals("0xff7f7fff", String.format("0x%08x", ColorHelper.saturate(BLUE, -0.5F)));
        assertEquals("0xffffffff", String.format("0x%08x", ColorHelper.saturate(RED, -1.0F)));
        assertEquals("0xff00ff00", String.format("0x%08x", ColorHelper.saturate(0xff00ff00, 1.0F)));
        assertEquals("0xff000000", String.format("0x%08x", ColorHelper.saturate(BLACK, -0.5F)));
        assertEquals("0xffffffff", String.format("0x%08x", ColorHelper.saturate(WHITE, -0.5F)));
    }
    @Test
    @Covers("public static void saturatePalette(ColorPalette,int,int,float)")
    @Deprecated
    public void saturatePalette() {
        ColorPalette pal = new ColorPalette();
        pal.add(BLACK);
        pal.add(WHITE);
        pal.add(RED);
        pal.add(GREEN);
        pal.add(BLUE);
        ColorHelper.saturatePalette(pal, 0, -1, -0.5F);
        assertEquals(BLACK, (int)pal.get(0));
        assertEquals(WHITE, (int)pal.get(1));
        assertEquals(0xFFff7f7f, (int)pal.get(2));
        assertEquals(0xFF7fff7f, (int)pal.get(3));
        assertEquals(0xFF7f7fff, (int)pal.get(4));

    }
    @Test
    @Covers("public static int scaleSV(int,float,float)")
    public void scaleSV() {
        assertEquals("0xff000000", String.format("0x%08x", ColorHelper.scaleSV(RED, 1F, 0.0F)));
        assertEquals("0xffffffff", String.format("0x%08x", ColorHelper.scaleSV(GREEN, 0.5F, 20.0F)));
        assertEquals("0xffffffff", String.format("0x%08x", ColorHelper.scaleSV(RED, 0F, 1F)));
        assertEquals("0xff7fff7f", String.format("0x%08x", ColorHelper.scaleSV(GREEN, 0.5F, 1F)));
    }
    @Test
    @Covers("public static int shiftHue(int,float)")
    public void shiftHue() {
        assertEquals(BLACK, ColorHelper.shiftHue(BLACK, -90.0F));
        assertEquals(WHITE, ColorHelper.shiftHue(WHITE, 90.0F));
    }

    @Test
    @Covers("public static int colorFromHSV(float,float,float,Float)")
    public void colorFromHSV_4args() {
        // XXX should actually validate that the values are _correct_

        float[] hsv = ColorHelper.colorToHSV(WHITE);
        assertEquals(4, hsv.length);
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(WHITE, ColorHelper.colorFromHSV(hsv[0], hsv[1], hsv[2], hsv[3]));

        hsv = ColorHelper.colorToHSV(RED);
        assertEquals(4, hsv.length);
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(RED, ColorHelper.colorFromHSV(hsv[0], hsv[1], hsv[2], hsv[3]));

        hsv = ColorHelper.colorToHSV(BLUE);
        assertEquals(4, hsv.length);
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(BLUE, ColorHelper.colorFromHSV(hsv[0], hsv[1], hsv[2], hsv[3]));

        hsv = ColorHelper.colorToHSV(GREEN);
        assertEquals(4, hsv.length);
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(GREEN, ColorHelper.colorFromHSV(hsv[0], hsv[1], hsv[2], hsv[3]));

        hsv = ColorHelper.colorToHSV(NEAR_BLACK);
        assertEquals(4, hsv.length);
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(NEAR_BLACK, ColorHelper.colorFromHSV(hsv[0], hsv[1], hsv[2], hsv[3]));
    }

    @Test
    @Covers({"public static float[] colorToHSV(int)",
             "public static float[] ColorToHSV(int)",
             "public static int colorFromHSV(float[])",
             "public static int ColorFromHSV(float[])"})
    public void colorToHSV_floatarr_symetrical() {
        // XXX should actually validate that the values are _correct_

        float[] hsv = ColorHelper.colorToHSV(WHITE);
        assertEquals(4, hsv.length);
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(WHITE, ColorHelper.colorFromHSV(hsv));

        hsv = ColorHelper.colorToHSV(RED);
        assertEquals(4, hsv.length);
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(RED, ColorHelper.colorFromHSV(hsv));

        hsv = ColorHelper.colorToHSV(BLUE);
        assertEquals(4, hsv.length);
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(BLUE, ColorHelper.colorFromHSV(hsv));

        hsv = ColorHelper.colorToHSV(GREEN);
        assertEquals(4, hsv.length);
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(GREEN, ColorHelper.colorFromHSV(hsv));

        hsv = ColorHelper.colorToHSV(NEAR_BLACK);
        assertEquals(4, hsv.length);
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(NEAR_BLACK, ColorHelper.colorFromHSV(hsv));
    }
    @Test
    @Covers({"public static Map<String, Float> colorToHSV(int,Map<String, Float>)",
             "public static int colorFromHSV(Map<String, Float>)"})
    public void colorToHSV_map_symetrical() {
        // XXX should actually validate that the values are _correct_
        Map<String, Float> result = ColorHelper.colorToHSV(this.WHITE, null);
        assertEquals(4, result.size());
        float[] hsv = new float[] {result.get("hue"), result.get("saturation"), result.get("value"), result.get("alpha")};
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(String.format("%s", hsv[3]), hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(String.format("0x%08x", WHITE), String.format("0x%08x", ColorHelper.colorFromHSV(hsv)));
        assertEquals(String.format("0x%08x", WHITE), String.format("0x%08x", ColorHelper.colorFromHSV(result)));

        result = ColorHelper.colorToHSV(RED, null);
        assertEquals(4, result.size());
        hsv = new float[] {result.get("hue"), result.get("saturation"), result.get("value"), result.get("alpha")};
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(RED, ColorHelper.colorFromHSV(result));

        result = ColorHelper.colorToHSV(GREEN, null);
        assertEquals(4, result.size());
        hsv = new float[] {result.get("hue"), result.get("saturation"), result.get("value"), result.get("alpha")};
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(GREEN, ColorHelper.colorFromHSV(result));

        result = ColorHelper.colorToHSV(BLUE, null);
        assertEquals(4, result.size());
        hsv = new float[] {result.get("hue"), result.get("saturation"), result.get("value"), result.get("alpha")};
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(BLUE, ColorHelper.colorFromHSV(result));

        result = ColorHelper.colorToHSV(this.NEAR_BLACK, null);
        assertEquals(4, result.size());
        hsv = new float[] {result.get("hue"), result.get("saturation"), result.get("value"), result.get("alpha")};
        assertTrue(hsv[0] < 360.0F && hsv[0] >= 0.0F);
        assertTrue(hsv[1] <= 1.0F && hsv[1] >= 0.0F);
        assertTrue(hsv[2] <= 1.0F && hsv[2] >= 0.0F);
        assertTrue(hsv[3] <= 1.0F && hsv[3] >= 0.0F);
        assertEquals(NEAR_BLACK, ColorHelper.colorFromHSV(result));
    }

    @Test
    @Covers("public static List<Integer> createGradient(List<Integer>,int,List<Integer>)")
    public void createGradient_out_n_List() {
        List<Integer> grads = new ArrayList<>();
        grads.add(BLACK);
        grads.add(WHITE);
        List<Integer> gradient = ColorHelper.createGradient(null, 5, grads);
        assertEquals(5, gradient.size());
        assertEquals(BLACK, (int)gradient.get(0));
        assertEquals(WHITE, (int)gradient.get(4));
        assertEquals("0xff7f7f7f", String.format("0x%8x", gradient.get(2)));
        assertEquals("0xff3f3f3f", String.format("0x%8x", gradient.get(1)));
        assertEquals("0xffbfbfbf", String.format("0x%8x", gradient.get(3)));

        grads.clear();
        grads.add(BLACK);
        grads.add(BLUE);
        grads.add(RED);
        grads.add(GREEN);
        grads.add(WHITE);
        gradient = ColorHelper.createGradient(null, 3, grads);
        assertEquals(3, gradient.size());
        assertEquals(BLACK, (int)gradient.get(0));
        assertEquals(RED, (int)gradient.get(1));
        assertEquals(WHITE, (int)gradient.get(2));

        grads.clear();
        grads.add(BLACK);
        grads.add(RED);
        grads.add(WHITE);
        gradient = ColorHelper.createGradient(null, 4, grads);
        assertEquals(4, gradient.size());
        assertEquals(BLACK, (int)gradient.get(0));
        assertEquals("0xffaa0000", String.format("0x%8x", gradient.get(1)));
        assertEquals("0xffff5555", String.format("0x%8x", gradient.get(2)));
        assertEquals(WHITE, (int)gradient.get(3));

        List<Integer> out = new ArrayList<>();
        out.add(GREEN);
        grads.clear();
        grads.add(BLACK);
        grads.add(RED);
        grads.add(WHITE);
        gradient = ColorHelper.createGradient(out, 4, grads);
        assertSame(out, gradient);
        assertEquals(5, gradient.size());
        assertEquals(GREEN, (int)gradient.get(0));
        assertEquals(BLACK, (int)gradient.get(1));
        assertEquals("0xffaa0000", String.format("0x%8x", gradient.get(2)));
        assertEquals("0xffff5555", String.format("0x%8x", gradient.get(3)));
        assertEquals(WHITE, (int)gradient.get(4));
    }
    @Test
    @Covers("public static List<Integer> createGradient(int,Integer...)")
    public void createGradient_int_Integers() {
        List<Integer> gradient = ColorHelper.createGradient(5, BLACK, WHITE);
        assertEquals(5, gradient.size());
        assertEquals(BLACK, (int)gradient.get(0));
        assertEquals(WHITE, (int)gradient.get(4));
        assertEquals("0xff7f7f7f", String.format("0x%8x", gradient.get(2)));
        assertEquals("0xff3f3f3f", String.format("0x%8x", gradient.get(1)));
        assertEquals("0xffbfbfbf", String.format("0x%8x", gradient.get(3)));

        gradient = ColorHelper.createGradient(3, BLACK, BLUE, RED, GREEN, WHITE);
        assertEquals(3, gradient.size());
        assertEquals(BLACK, (int)gradient.get(0));
        assertEquals(RED, (int)gradient.get(1));
        assertEquals(WHITE, (int)gradient.get(2));

        gradient = ColorHelper.createGradient(4, BLACK, RED, WHITE);
        assertEquals(4, gradient.size());
        assertEquals(BLACK, (int)gradient.get(0));
        assertEquals("0xffaa0000", String.format("0x%8x", gradient.get(1)));
        assertEquals("0xffff5555", String.format("0x%8x", gradient.get(2)));
        assertEquals(WHITE, (int)gradient.get(3));
    }

    @Test
    @Covers("public static List<Integer> lookup(ColorPalette,Integer...)")
    public void lookup_in_Integers() {
        ColorPalette in = new ColorPalette();
        in.add("Black", BLACK);
        in.add("White", WHITE);
        in.add("Red", RED);
        in.add("Green", GREEN);
        in.add("Blue", BLUE);

        List<Integer> got = ColorHelper.lookup(in, 0, 1);
        assertNotSame(in, got);
        assertEquals(2, got.size());
        assertEquals(BLACK, (int)got.get(0));
        assertEquals(WHITE, (int)got.get(1));

        got = ColorHelper.lookup(in, 0, 4, 2, 3, 1);
        assertNotSame(in, got);
        assertEquals(5, got.size());
        assertEquals(BLACK, (int)got.get(0));
        assertEquals(BLUE, (int)got.get(1));
        assertEquals(RED, (int)got.get(2));
        assertEquals(GREEN, (int)got.get(3));
        assertEquals(WHITE, (int)got.get(4));

        got = ColorHelper.lookup(null, BLACK);
        assertNotNull(got);
        assertEquals(1, got.size());
        assertEquals(BLACK, (int)got.get(0));
    }

    @Test
    @Covers("public static List<Integer> lookup(ColorPalette,String...) throws InvalidStringFormatException")
    public void lookup_in_Strings() {
        ColorPalette in = new ColorPalette();
        in.add("Black", BLACK);
        in.add("White", WHITE);
        in.add("Red", RED);
        in.add("Green", GREEN);
        in.add("Blue", BLUE);

        List<Integer> got;
        try {
            got = ColorHelper.lookup(in, "Black", "White");
        } catch (InvalidStringFormatException ex) {
            throw new RuntimeException(ex);
        }
        assertNotSame(in, got);
        assertEquals(2, got.size());
        assertEquals(BLACK, (int)got.get(0));
        assertEquals(WHITE, (int)got.get(1));

        try {
            got = ColorHelper.lookup(in, "Black", "Blue", "Red", "Green", "White");
        } catch (InvalidStringFormatException ex) {
            throw new RuntimeException(ex);
        }
        assertNotSame(in, got);
        assertEquals(5, got.size());
        assertEquals(BLACK, (int)got.get(0));
        assertEquals(BLUE, (int)got.get(1));
        assertEquals(RED, (int)got.get(2));
        assertEquals(GREEN, (int)got.get(3));
        assertEquals(WHITE, (int)got.get(4));

        try {
            got = ColorHelper.lookup(null, "#fff");
        } catch (InvalidStringFormatException ex) {
            throw new RuntimeException(ex);
        }
        assertNotNull(got);
        assertEquals(1, got.size());
        assertEquals(WHITE, (int)got.get(0));
    }

    @Test
    @Covers("public static List<Integer> extendGradient(List<Integer>,int,List<Integer>)")
    public void extendGradient_out_n_List() {
        List<Integer> out = new ArrayList<>();
        out.add(BLACK);
        List<Integer> grads = new ArrayList<>();
        grads.add(WHITE);
        List<Integer> gradient = ColorHelper.extendGradient(out, 5, grads);
        assertSame(gradient, out);
        assertEquals(5, gradient.size());
        assertEquals(BLACK, (int)gradient.get(0));
        assertEquals(WHITE, (int)gradient.get(4));
        assertEquals("0xff7f7f7f", String.format("0x%8x", gradient.get(2)));
        assertEquals("0xff3f3f3f", String.format("0x%8x", gradient.get(1)));
        assertEquals("0xffbfbfbf", String.format("0x%8x", gradient.get(3)));

        out.clear();
        out.add(BLACK);
        grads.clear();
        grads.add(BLUE);
        grads.add(RED);
        grads.add(GREEN);
        grads.add(WHITE);
        gradient = ColorHelper.extendGradient(out, 3, grads);
        assertSame(gradient, out);
        assertEquals(3, gradient.size());
        assertEquals(BLACK, (int)gradient.get(0));
        assertEquals(RED, (int)gradient.get(1));
        assertEquals(WHITE, (int)gradient.get(2));

        out.clear();
        out.add(BLACK);
        grads.clear();
        grads.add(RED);
        grads.add(WHITE);
        gradient = ColorHelper.extendGradient(out, 4, grads);
        assertSame(gradient, out);
        assertEquals(4, gradient.size());
        assertEquals(BLACK, (int)gradient.get(0));
        assertEquals("0xffaa0000", String.format("0x%8x", gradient.get(1)));
        assertEquals("0xffff5555", String.format("0x%8x", gradient.get(2)));
        assertEquals(WHITE, (int)gradient.get(3));

        out.clear();
        out.add(GREEN);
        out.add(BLACK);
        grads.clear();
        grads.add(RED);
        grads.add(WHITE);
        gradient = ColorHelper.extendGradient(out, 4, grads);
        assertSame(out, gradient);
        assertEquals(5, gradient.size());
        assertEquals(GREEN, (int)gradient.get(0));
        assertEquals(BLACK, (int)gradient.get(1));
        assertEquals("0xffaa0000", String.format("0x%8x", gradient.get(2)));
        assertEquals("0xffff5555", String.format("0x%8x", gradient.get(3)));
        assertEquals(WHITE, (int)gradient.get(4));
    }

    @Covers("public static List<Integer> extendGradient(List<Integer>,int,int)")
    public void extendGradient_out_n_value() {
        List<Integer> out = new ArrayList<>();
        out.add(BLACK);
        List<Integer> gradient = ColorHelper.extendGradient(out, 5, WHITE);
        assertSame(gradient, out);
        assertEquals(5, gradient.size());
        assertEquals(BLACK, (int)gradient.get(0));
        assertEquals(WHITE, (int)gradient.get(4));
        assertEquals("0xff7f7f7f", String.format("0x%8x", gradient.get(2)));
        assertEquals("0xff3f3f3f", String.format("0x%8x", gradient.get(1)));
        assertEquals("0xffbfbfbf", String.format("0x%8x", gradient.get(3)));
    }

}
