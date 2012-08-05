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

import java.util.HashMap;
import java.util.Map;

import com.googlecode.blacken.exceptions.InvalidStringFormatException;

/**
 * A container class for a number of color-related convenience functions.
 * 
 * @author Steven Black
 */
public class ColorHelper {
    /**
     * This exists to help prevent spelling errors in Strings where the
     * compiler cannot catch them.
     *
     * @see {@link #colorToHSV(int, java.util.Map)}
     */
    public static final String HUE = "hue";
    /**
     * This exists to help prevent spelling errors in Strings where the
     * compiler cannot catch them.
     *
     * @see {@link #colorToHSV(int, java.util.Map)}
     */
    public static final String SATURATION = "saturation";
    /**
     * This exists to help prevent spelling errors in Strings where the
     * compiler cannot catch them.
     *
     * @see {@link #colorToHSV(int, java.util.Map)}
     */
    public static final String VALUE = "value";
    /**
     * This exists to help prevent spelling errors in Strings where the
     * compiler cannot catch them.
     *
     * @see {@link #colorToHSV(int, java.util.Map)}
     * @see {@link #colorToComponents(int, java.util.Map)}
     */
    public static final String ALPHA = "alpha";
    /**
     * This exists to help prevent spelling errors in Strings where the
     * compiler cannot catch them.
     *
     * @see {@link #colorToComponents(int, java.util.Map)}
     */
    public static final String RED = "red";
    /**
     * This exists to help prevent spelling errors in Strings where the
     * compiler cannot catch them.
     *
     * @see {@link #colorToComponents(int, java.util.Map)}
     */
    public static final String GREEN = "green";
    /**
     * This exists to help prevent spelling errors in Strings where the
     * compiler cannot catch them.
     *
     * @see {@link #colorToComponents(int, java.util.Map)}
     */
    public static final String BLUE = "blue";
    private ColorHelper() {
        // do nothing
    }
    /**
     * Get the alpha channel for a color.
     * 
     * @param color RGBA color value
     * @see #setAlpha(int, int)
     * @return the alpha channel value
     */
    public static int getAlpha(final int color) {
        return (color >>> 24);
    }

    /**
     * Modify the current alpha value of a color.
     * 
     * <p>Increase the alpha in a particular color by a percentage of the 
     * current alpha. Note that full 100% alpha is opaque, so to add some 
     * transparency you would use a negative percentage. Please note that the 
     * percentage is expressed in a number from 0 to 1, so to increase the 
     * transparency by 20% you would use an <i>amount</i> of 
     * <code>-0.20</code>.
     * 
     * <p>Note also that as this operates on the existing alpha channel value if
     * the color is completely transparent this function does nothing. If this
     * isn't what you want, you could pass the <code>color</code> through 
     * <code>neverTransparent</code> first.
     * 
     * @param color the RGBA color value (a valid alpha channel must be present)
     * @param amount the percentage from 0.0 to 1.0 to increase the alpha
     * @return new color value 
     */
    public static int increaseAlpha(final int color, final double amount) {
        int alpha = getAlpha(color);
        int c = (int) Math.floor(alpha * amount / 100) + alpha;
        if (c > 255) {
            c = 255;
        } else if (c < 0) {
            c = 0;
        }
        return setAlpha(color, c);
    }

    /**
     * Check to see if there is a reasonable expectation of a valid color.
     * 
     * <p>This checks to see if the string is a color definition. That is, the
     * string is not a color name, but is instead an RGB or RGBA color 
     * definition.</p>
     * 
     * @param color string to check
     * @return true when it may be a valid color description, false otherwise
     */
    public static boolean isColorDefinition(String color) {
        boolean ret = false;
        if (color == null) {
            return false;
        }
        if (color.startsWith("#")) {  //$NON-NLS-1$
            color = color.substring(1);
            try {
                Integer.parseInt(color, 16);
                ret = true;
            } catch (NumberFormatException e) {
                // do nothing
            }
        } else if (color.startsWith("0x") ||  //$NON-NLS-1$
                color.startsWith("0X")) { //$NON-NLS-1$
            color = color.substring(2);
            try {
                Integer.parseInt(color, 16);
                ret = true;
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        return ret;
    }

    /**
     * Is the color fully opaque?
     * 
     * @param color the color to check
     * @see isTransparent
     * @see makeOpaque
     * @return true if the alpha channel is fully opaque, false otherwise
     */
    public static boolean isOpaque(int color) {
        return (color & 0xFF000000) == 0xFF000000 ? true : false;
    }
    /**
     * Is the color fully transparent?
     * 
     * <p>Please note, a partially transparent color would only be translucent.
     * If this is what you want, you may want to use !isOpaque.</p>
     * 
     * @param color the color to check
     * @see #isOpaque
     * @return true if the alpha channel is fully transparent, false otherwise
     */
    public static boolean isTransparent(int color) {
        return (color & 0xFF000000) == 0 ? true : false;
    }
    
    /**
     * Make an opaque RGBA color -- color components have upper value of 1.0.
     *
     * <p>Having a separate function for opaque color is silly.</p>
     * 
     * @param red red value
     * @param green green value
     * @param blue blue value
     * @return valid RGBA color
     * @deprecated use {@link #colorFromFloatComponents(float, float, float, float)} instead
     */
    @Deprecated
    public static int makeColor(float red, float green, float blue) {
        return colorFromFloatComponents(red, green, blue, 1.0F);
    }
    /**
     * Make an RGBA color -- color components have upper value of 1.0.
     *
     * @param red red value
     * @param green green value
     * @param blue blue value
     * @param alpha alpha value
     * @return valid RGBA color
     * @deprecated use {@link #colorFromFloatComponents(float[])}
     */
    @Deprecated
    public static int makeColor(float red, float green, float blue,
                                float alpha) {
        return colorFromFloatComponents(red, green, blue, alpha);
    }

    /**
     * Make an RGBA color -- color components have upper value of 1.0.
     * 
     * @param red red value
     * @param green green value
     * @param blue blue value
     * @param alpha alpha value
     * @return valid RGBA color
     */
    public static int colorFromFloatComponents(float red, float green, float blue,
                                Float alpha) {
        if (alpha == null) {
            alpha = 1.0F;
        }
        if (red > 1.0) {
            red = 1.0F;
        }
        if (green > 1.0) {
            green = 1.0F;
        }
        if (blue > 1.0) {
            blue = 1.0F;
        }
        if (alpha > 1.0) {
            alpha = 1.0F;
        }
        red *= 255.0F;
        blue *= 255.0F;
        green *= 255.0F;
        alpha *= 255.0F;
        return colorFromComponents((int)Math.floor(red), (int)Math.floor(green),
                         (int)Math.floor(blue), (int)Math.floor(alpha));
    }

    /**
     * Make an opaque RGB color -- color components have upper value of 255.
     *
     * <p>Having a separate function for opaque color is silly.</p>
     *
     * @param red red value
     * @param green green value
     * @param blue blue value
     * @return valid RGBA color (opaque)
     * @deprecated use {@link #colorFromComponents(int, int, int, int)}
     */
    public static int makeColor(int red, int green, int blue) {
        return makeColor(red, green, blue, 255, 255);
    }

    /**
     * Make an RGBA color -- color components have upper value of 255.
     * 
     * @param red red value
     * @param green green value
     * @param blue blue value
     * @param alpha alpha value
     * @return valid RGBA color
     * @deprecated use {@link #colorFromComponents(int, int, int, int)}
     */
    public static int makeColor(int red, int green, int blue, int alpha) {
        return makeColor(red, green, blue, alpha, 255);
    }

    /**
     * Make an RGBA color -- color components have upper value of 255.
     *
     * @param red red value
     * @param green green value
     * @param blue blue value
     * @param alpha alpha value (completely opaque when null)
     * @return valid RGBA color
     */
    public static int colorFromComponents(int red, int green, int blue, Integer alpha) {
        if (alpha == null) {
            alpha = 255;
        }
        return makeColor(red, green, blue, alpha, 255);
    }


    /**
     * Make an RGBA color using a variable upper value for color components
     * 
     * @param red red value
     * @param green green value
     * @param blue blue value
     * @param alpha alpha value
     * @param top upper value for all color components
     * @return valid RGBA color
     */
    public static int makeColor(final int red, final int green, final int blue, 
                                final int alpha, final int top) {
        int r, g, b, a;
        if (top == 0) {
            return 0;
        } else if (top == 255) {
            r = red;
            g = green;
            b = blue;
            a = alpha;
        } else if (top == 15) {
            r = (red << 4) | red;
            g = (green << 4) | green;
            b = (blue << 4) | blue;
            a = (alpha << 4) | alpha;
        } else if (top <= 0xffffff) {
            r = red * 255 / top;
            b = blue * 255 / top;
            g = green * 255 / top;
            a = alpha * 255 / top;
        } else {
            int t = top >>> 8;
            r = (red >>> 8) * 255 / t;
            g = (green >>> 8) * 255 / t;
            b = (blue >>> 8) * 255 / t;
            a = (alpha >>> 8) * 255 / t;
        }
        a &= 0xff; r &= 0xff; g &= 0xff; b &= 0xff;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Set the color based upon a string. 
     * 
     * <p>The color can be defined in a number of ways:
     * <ul>
     * <li>0xAARRGGBB</li>
     * <li>0xRRGGBB (opaque)</li>
     * <li>#RGB (web short-cut for #RRGGBB)</li>
     * <li>#RRGGBB (web standard)</li>
     * </ul>
     * </p>
     * 
     * <p>Note that this does not handle named colors. If you want that, check
     * out the ColorNames class. If you need to check that a string could
     * be a color, try the canStringColor command.</p>
     * 
     * @param color the color we are referencing
     * @return new color
     * @throws InvalidStringFormatException 
     *            specified <code>color</code> definition was invalid
     */
    public static int makeColor(String color)
            throws InvalidStringFormatException {
        if (color.startsWith("#")) {
            color = color.substring(1);
            int c;
            try {
                c = Integer.parseInt(color, 16);
            } catch(NumberFormatException e) {
                throw new InvalidStringFormatException(e);
            }
            if (color.length() == 3) {
                // web shorthand #RGB == #RRGGBB
                return makeColor((c & 0xf00) >> 8, (c & 0x0f0) >> 4,
                                 (c & 0x00f), 0xf, 0xf);
            } else if (color.length() == 6) {
                return makeOpaque(c);
            } else if (color.length() == 8) {
                // This may be disappearing or changing
                return c;
            }
        } else if (color.startsWith("0x") ||
                color.startsWith("0X")) {
            color = color.substring(2);
            int c;
            long trialC;
            try {
                trialC = Long.parseLong(color, 16);
            } catch(NumberFormatException e) {
                throw new InvalidStringFormatException(e);
            }
            c = (int)(trialC & 0xffffffffL);
            if (color.length() == 6) {
                return makeOpaque(c);
            } else if (color.length() == 8) {
                return c;
            }
        } else if (color.contains(",")) {
            String[] parts = color.split(",");
            int[] components = new int[4];
            try {
                components[0] = Integer.parseInt(parts[0], 10);
                components[1] = Integer.parseInt(parts[1], 10);
                components[2] = Integer.parseInt(parts[2], 10);
                if (parts.length > 3) {
                    components[3] = Integer.parseInt(parts[3], 10);
                } else {
                    components[3] = 255;
                }
            } catch(NumberFormatException e) {
                throw new InvalidStringFormatException(e);
            }
            return colorFromComponents(components);
        }
        throw new InvalidStringFormatException();
    }

    /**
     * Split a color out in to the component R,G,B,A values.
     *
     * @param color RGBA value
     * @deprecated use {@link #colorToComponents(int)} instead.
     * @return int[] {red, green, blue, alpha}
     */
    @Deprecated
    public static int[] makeComponents(final int color) {
        return colorToComponents(color);
    }

    /**
     * Split a color out in to the component R,G,B,A values.
     *
     * @param color color value
     * @see #colorFromComponents(int[])
     * @return int[] {red, green, blue, alpha}
     */
    public static int[] colorToComponents(final int color) {
        int r, g, b, a;
        a = (color & 0xff000000) >>> 24;
        r = (color & 0x00ff0000) >>> 16;
        g = (color & 0x0000ff00) >>> 8;
        b = color & 0x000000ff;
        int[] ret = {r, g, b, a};
        return ret;
    }

    /**
     * Convert a color in to 0.0-1.0 component colors.
     * 
     * @param rgba integer color encoded 0xAARRGGBB (AWT standard)
     * @return float[] {red, green, blue, alpha}
     */
    public static float[] colorToFloatComponents(int rgba) {
        int[] rgb = colorToComponents(rgba);
        float[] ret = {rgb[0] / 255.0F, rgb[1] / 255.0F,
                       rgb[2] / 255.0F, rgb[3] / 255.0F};
        return ret;
    }

    /**
     * Convert a set of 0.0-1.0 component colors in to a color value.
     *
     * <p>The <code>alpha</code> index can be missing. If it is not provided
     * a completely opaque color is created.
     *
     * @param rgba float[] {red, green, blue, alpha}
     * @return
     */
    public static int colorFromFloatComponents(float[] rgba) {
        if (rgba.length == 3) {
            return colorFromFloatComponents(rgba[0], rgba[1], rgba[2], 1.0F);
        }
        return colorFromFloatComponents(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    /**
     * Split a color out in to the component R,G,B,A values.
     *
     * <p>If the <code>out</code> Map is provided, it will be used to store
     * the component settings, otherwise a new map will be created and
     * returned. The components have keys of "red", "green", "blue", and
     * "alpha".</p>
     *
     * @param color an RGBA value
     * @param out null to create a new Map; otherwise output variable
     * @return components in a Map
     * @since Blacken 1.1
     */
    public static Map<String, Integer> colorToComponents(final int color,
            Map<String, Integer> out) {
        Map<String, Integer> ret = out;
        if (ret == null) {
            ret = new HashMap<>(4);
        }
        int[] comps = colorToComponents(color);
        ret.put(RED, comps[0]);
        ret.put(GREEN, comps[1]);
        ret.put(BLUE, comps[2]);
        ret.put(ALPHA, comps[3]);
        return ret;
    }

    /**
     * Make a color opaque. (Need not have valid alpha data.)
     * 
     * @param value an RGB or RGBA color (0xAARRGGBB)
     * @return RGBA color, with alpha channel set to fully opaque.
     */
    public static int makeOpaque(int value) {
        return (value & 0x00ffffff) | 0xff000000;
    }

    /**
     * For a given color value, decide whether black or white is visible
     * 
     * Same as calling {@link #makeVisible(int, int)} with a 
     * <code>threshold</code> of 66.
     * 
     * @param value color value (alpha dropped, only 0xRRGGBB used)
     * @see ColorHelper#makeVisible(int, int)
     * @return black or white, expressed as 0xAARRGGBB
     */
    public static int makeVisible(int value) {
        return makeVisible(value, 66, 0xFFffffff, 0xFF000000);
    }

    /**
     * For a given color value, decide whether black or white is visible
     * 
     * <p>The provided threshold is used to determine whether to go black or
     * white.</p>
     * 
     * @param value color value (alpha dropped, only 0xRRGGBB used)
     * @param threshold number between 0 and 100 (smaller results in more black)
     * @return black or white, expressed as 0xAARRGGBB
     */
    public static int makeVisible(int value, int threshold) {
        return makeVisible(value, threshold, 0xFFffffff, 0xFF000000);
    }
    
    /**
     * For a given color value, decide whether "black" or "white" is visible
     * 
     * @param value color value as 0xAARRRGGBB (or 0xRRGGBB)
     * @param threshold number between 0 and 100 (smaller results in more black)
     * @param white index to use for white
     * @param black index to use for black
     * @return the <code>white</code> or <code>black> value
     */
    public static Integer makeVisible(int value, int threshold, int white, 
            int black) {
        int[] parts = makeComponents(value);
        int g = (parts[0] * 20 + parts[1] * 69 + parts[2] * 11) / 255;
        if (g < threshold) {
            return white;
        }
        return black;
    }

    /**
     * Force fully transparent colors to become opaque.
     * 
     * <p>We support both RGB and RGBA colors. The problem is that when RGB
     * color information is used somewhere that expects RGBA color, the color
     * winds up being fully transparent. This solves the problem by taking
     * any fully transparent color and turning it fully opaque.</p>
     * 
     * <p>This means that the closest you can then come to fully transparent 
     * is 0.4% opaque (1/255). This approximation should be good enough for 
     * most roguelike games. Note the name of this function. The isTransparent
     * function will never return true for data returned by this function.
     * Anyplace you would use isTransparent, you'll need to use !isOpaque.)</p>
     * 
     * @param color either RGB or RGBA data
     * @return valid RGBA data, with alpha channel never zero
     */
    static public int neverTransparent(int color) {
        if (isTransparent(color)) {
            color = makeOpaque(color);
        }
        return color;
    }

    /**
     * Set the alpha channel for a color.
     * 
     * @param color RGB or RGBA color value
     * @param alpha new alpha value (upper-limit of 255)
     * @see #getAlpha(int)
     * @return valid RGBA value
     */
    public static int setAlpha(final int color, final int alpha) {
        int ret = alpha & 0xff;
        ret <<= 24;
        ret |= (color & 0x00ffffff);
        return ret;
    }

    /**
     * Generate HSVA components for a color.
     *
     * <p>The map contains float values with names and ranges as follows:
     * <ul>
     * <li> <code>hue</code> : 0.0 <= v < 360.0
     * <li> <code>saturation</code> : 0.0 <= v <= 1.0
     * <li> <code>value</code> : 0.0 <= v <= 1.0
     * <li> <code>alpha</code> : 0.0 <= v <= 1.0
     * </ul>
     *
     * <p>The alpha component is always specified in the output map.
     *
     * @param rgba
     * @param out
     * @return
     */
    public static Map<String, Float> colorToHSV(int rgba, Map<String, Float> out) {
        Map<String, Float> ret = out;
        if (ret == null) {
            ret = new HashMap<>(4);
        }
        float[] hsva = colorToHSV(rgba);
        ret.put(HUE, hsva[0]);
        ret.put(SATURATION, hsva[1]);
        ret.put(VALUE, hsva[2]);
        ret.put(ALPHA, hsva[3]);
        return ret;
    }

    /**
     *
     * @param rgba color value
     * @return array of {hue, saturation, value, alpha}
     * @deprecated use {@link #colorToHSV(int)} instead.
     */
    @Deprecated
    public static float[] ColorToHSV(int rgba) {
        return colorToHSV(rgba);
    }

    /**
     * @param rgba color value
     * @return array of {hue, saturation, value, alpha}
     */
    public static float[] colorToHSV(int rgba) {
        int min, max;
        float d;
        int[] comps = colorToComponents(rgba);
        int r = comps[0]; int g = comps[1]; int b = comps[2];
        int alpha = comps[3];
        
        if (r > g) { 
            min = g; max = r; 
        } else { 
            min = r; max = g; 
        }
        if (b > max) {
            max = b;
        }
        if (b < min) {
            min = b;
        }
        d = max - min;

        float h = 0;
        if (d != 0F) {
            if (r == max) {
                h = ((g - b) / d);
            } else if (g == max) { 
                h = (2 + (b - r) / d);
            } else if (b == max) { 
                h = (4 + (r - g) / d);
            }
        }
        if (h < 0) {
            // convert from -180:+180 to 0:360
            h += 6F;
        }
        return new float[]{ h * 60F, d / 255F, max / 255F, alpha / 255F};
    }

    /**
     * Shift the hue while retaining saturation and value.
     *
     * @param rgba original color code
     * @param shift amount to shift
     * @return  new color code
     */
    public static int shiftHue(int rgba, float shift) {
        if (shift >= 360.0 || shift <= -360.0) {
            throw new IllegalArgumentException("shift should be within +/- 360.0");
        }
        float[] hsv = colorToHSV(rgba);
        hsv[0] += shift;
        if (hsv[0] < 0) {
            hsv[0] += 360.0;
        } else if (hsv[0] >= 360.0) {
            hsv[0] -= 360.0;
        }
        return colorFromHSV(hsv);
    }

    /**
     * Scale the saturation and value without touching the hue or alpha.
     *
     * <p>This multiplies the scale values by the current saturation and
     * value. The result is then cropped to the standard  0.0-1.0 (inclusive)
     * limits.
     *
     * @param rgba standard color value
     * @param scaleS
     * @param scaleV
     * @return
     */
    public static int scaleSV(int rgba, float scaleS, float scaleV) {
        float[] hsv = colorToHSV(rgba);
        hsv[1] *= scaleS;
        hsv[2] *= scaleV;
        return colorFromHSV(hsv);
    }

    public static int colorFromHSV(float hue, float saturation, float value, Float alpha) {
        if (alpha == null) {
            alpha = 1.0F;
        }
        return colorFromHSV(new float[] {hue, saturation, value, alpha});
    }

    public static int colorFromHSV(Map<String, Float> hsva) {
        Float hue = hsva.get(HUE);
        Float saturation = hsva.get(SATURATION);
        Float value = hsva.get(VALUE);
        Float alpha = hsva.get(ALPHA);
        if (alpha == null) {
            alpha = 1.0F;
        }
        if (hue == null) {
            hue = 0.0F;
        }
        if (saturation == null) {
            saturation = 0.0F;
        }
        if (value == null) {
            value = 0.0F;
        }
        return ColorFromHSV(new float[] {hue, saturation, value, alpha});
    }

    /**
     * Convert color from HSV to RGBA
     *
     * @param hsva {hue, saturation, value, (optional) alpha}
     * @return color value
     * @deprecated use {@link #colorFromHSV(float[])} instead.
     */
    @Deprecated
    public static int ColorFromHSV(float[] hsva) {
        return colorFromHSV(hsva);
    }

    /**
     * Convert color from HSV to RGBA
     * 
     * <p>If the alpha component is missing it defaults to 1.0F,
     * which is fully opaque.</p>
     * 
     * @param hsva {hue, saturation, value, (optional) alpha}
     * @return color value
     */
    public static int colorFromHSV(float[] hsva) {
        float h = hsva[0]; float s = hsva[1]; float v = hsva[2];
        float alpha = 1F;
        if (hsva.length > 3) {
            alpha = hsva[3];
        }
        float r, g, b;
        
        int segment;
        float f, p, q, t;

        if (s == 0.0F) {
            r = g = b = v;
            return colorFromFloatComponents(r, g, b, alpha);
        }
        
        h /= 60.0F;
        segment = (int)Math.floor(h);
        f = h - segment;
        p = v * (1 - s);
        q = v * (1 - s * f);
        t = v * (1 - s * (1.0F - f));

        switch(segment) {
        case 0: r = v; g = t; b = p; break;
        case 1: r = q; g = v; b = p; break;
        case 2: r = p; g = v; b = t; break;
        case 3: r = p; g = q; b = v; break;
        case 4: r = t; g = p; b = v; break;
        default: r = v; g = p; b = q; break;
        }
        return makeColor(r, g, b, alpha);
    }

    /**
     * Saturate/Desaturate a color
     *
     * <p>The satStep is added to the current saturation and forced to remain
     * in the 0.0:1.0 (inclusive) range.</p>
     *
     * @param rgba the color to saturate/desaturate
     * @param satStep offset to modify the saturation (-1.0 to +1.0)
     * @return the modified color
     */
    static public int saturate(int rgba, float satStep) {
        float[] hsv = colorToHSV(rgba);
        hsv[1] += satStep;
        if (hsv[1] < 0F) {
            hsv[1] = 0F;
        } else if (hsv[1] > 1.0F) {
            hsv[1] = 1F;
        }
        return colorFromHSV(hsv);
    }
    /**
     * Saturate/Desaturate a range of a palette.
     * 
     * <p>The satStep is added to the current saturation and forced to remain
     * in the 0.0:1.0 (inclusive) range.</p>
     * 
     * @param pal the palette to saturate/desaturate
     * @param startIdx starting index (inclusive)
     * @param endIdx ending index (exclusive)
     * @param satStep offset to modify the saturation (-1.0 to +1.0)
     * @deprecated use {@link #repeatForPalette(ColorPalette, int, int, ColorTransformer)}
     */
    static public void saturatePalette(ColorPalette pal, int startIdx, int endIdx, float satStep) {
        class transformer implements ColorTransformer {
            float value = 0;
            transformer(float v) {
                this.value = v;
            }
            @Override
            public int transform(int rgba) {
                return saturate(rgba, value);
            }
        }
        repeatForPalette(pal, startIdx, endIdx, new transformer(satStep));

    }

    /**
     * Repeat a color transformation for a section of a palette.
     *
     * <p>If <code>length</code> is less than zero it indicates how many indexes
     * to ignore at the end of the palette. If length is zero it will go to the
     * end of the palette. Note that this is different than the behavior of
     * negative index values for
     * {@link #repeatForPalette(ColorPalette, int, int, ColorTransformer)}.
     *
     * <p>Starting indexes less than zero are not supported.
     * {@link IllegalArgumentException} will be thrown.
     *
     * @param pal
     * @param startIdx inclusive starting index
     * @param transformer
     * @param length number of colors to transform
     */
    public static void repeatForPalette(ColorPalette pal, int startIdx, ColorTransformer transformer, Integer length) {
        if (transformer == null) {
            return;
        }
        if (startIdx < 0) {
            throw new IllegalArgumentException("starting index must be non-negative");
        }
        int endIdx;
        if (length == null) {
            endIdx = pal.size();
        } else {
            endIdx = startIdx + length;
            if (length <= 0) {
                endIdx = pal.size() + length;
            }
        }
        for (int idx = startIdx; idx < endIdx; idx++) {
            int newColor = transformer.transform(pal.get(idx));
            pal.set(idx, newColor);
        }
    }

    /**
     * Repeat a color transformation for a section of a palette.
     *
     * <p>If either index is less than zero, it is added to the palette size
     * plus one. This provides an index from the end of the palette, with -1
     * specifying processing to the end of the palette, and -2 specifying
     * stopping one shy of the end of the palette.
     *
     * <p>If the starting index is greater than the ending index, 
     * the {@link IllegalArgumentException} is thrown.
     *
     * @param pal
     * @param startIdx inclusive starting index
     * @param endIdx exclusive ending index
     * @param transformer
     */
    public static void repeatForPalette(ColorPalette pal, int startIdx, int endIdx, ColorTransformer transformer) {
        if (transformer == null) {
            return;
        }
        if (startIdx < 0) {
            startIdx += pal.size() +1;
        }
        if (endIdx < 0) {
            endIdx += pal.size() +1;
        }
        if (startIdx > endIdx) {
            throw new IllegalArgumentException("start cannot be greater than the end");
        }
        for (int idx = startIdx; idx < endIdx; idx++) {
            int newColor = transformer.transform(pal.get(idx));
            pal.set(idx, newColor);
        }
    }

    /**
     * Multiply two colors together to get a third color.
     *
     * <p>Actually splits the colors in to component values, then does a
     * <code>component1 * component2 / 255</code> -- so it isn't <em>strictly</em>
     * just multiplication.</p>
     *
     * <p>The overall effect is that if you take a saturated color, and a grey
     * scale setting and combine them with this function you get the color with
     * a value similar to that of the grey value. The HSV functions may product
     * more accurate results, but this will be much faster.</p>
     *
     * @param rgba1
     * @param rgba2
     * @return
     */
    public static int multiply(int rgba1, int rgba2) {
        int[] comps1 = colorToComponents(rgba1);
        int[] comps2 = colorToComponents(rgba2);
        int[] result = {comps1[0] * comps2[0] / 255,
                        comps1[1] * comps2[1] / 255,
                        comps1[2] * comps2[2] / 255,
                        comps1[3] * comps2[3] / 255};
        return colorFromComponents(result);
    }

    /**
     * Multiply RGB components of a color by a single float value leaving
     * alpha alone.
     *
     * @param rgba1 RGBA value
     * @param value 0.0 - 1.0
     * @return new RGBA value
     */
    public static int multiply(int rgba1, float value) {
        int[] comps1 = colorToComponents(rgba1);
        int component = (int)Math.floor(value * 255.0);
        int[] result = {comps1[0] * component / 255,
                        comps1[1] * component / 255,
                        comps1[2] * component / 255,
                        comps1[3]};
        return colorFromComponents(result);
    }

    public static int add(int rgba1, int rgba2) {
        int[] comps1 = colorToComponents(rgba1);
        int[] comps2 = colorToComponents(rgba2);
        int[] result = {Math.min(255, comps1[0] + comps2[0]),
                        Math.min(255, comps1[1] + comps2[1]),
                        Math.min(255, comps1[2] + comps2[2]),
                        Math.min(255, comps1[3] + comps2[3])};
        return colorFromComponents(result);
    }

    public static int subtract(int rgba1, int rgba2) {
        int[] comps1 = colorToComponents(rgba1);
        int[] comps2 = colorToComponents(rgba2);
        int[] result = {Math.max(0, comps1[0] - comps2[0]),
                        Math.max(0, comps1[1] - comps2[1]),
                        Math.max(0, comps1[2] - comps2[2]),
                        Math.max(0, comps1[3] - comps2[3])};
        return colorFromComponents(result);
    }

    /**
     * Perform linear interpolation on a color with a weight.
     *
     * @param rgba1
     * @param rgba2
     * @param weight common weight (0.0-1.0) of how much rgba2 goes in to rgba1
     * @return
     */
    public static int lerp(int rgba1, int rgba2, float weight) {
        int[] comps1 = colorToComponents(rgba1);
        int[] comps2 = colorToComponents(rgba2);
        int[] result = {(int)Math.floor(comps1[0] + (comps2[0] - comps1[0]) * weight),
                        (int)Math.floor(comps1[1] + (comps2[1] - comps1[1]) * weight),
                        (int)Math.floor(comps1[2] + (comps2[2] - comps1[2]) * weight),
                        (int)Math.floor(comps1[3] + (comps2[3] - comps1[3]) * weight)};
        return colorFromComponents(result);
    }

    /**
     * Perform linear interpolation on a color with a weight.
     *
     * <p>The "weight" argument has a number of options for the number of
     * arguments:
     * <ul>
     * <li>0 floats : returns rgba1 untouched
     * <li>1 float : uses {@link #lerp(int, int, float)} instead
     * <li>3 floats : ignore's rgba2's alpha channel
     * <li>4 floats : full channel control
     * </ul>
     *
     * @param rgba1
     * @param rgba2
     * @param weight set of weights (0.0-1.0) of how much rgba2 goes in to rgba1
     * @return
     */
    public static int lerp(int rgba1, int rgba2, float[] weight) {
        if (weight.length == 0) {
            return rgba1;
        }
        if (weight.length == 1) {
            return lerp(rgba1, rgba2, weight[0]);
        }
        if (weight.length == 3) {
            weight = new float[] {weight[0], weight[1], weight[2], 0.0F};
        }
        if (weight.length < 4) {
            throw new IllegalArgumentException("Need four weights.");
        }
        int[] comps1 = colorToComponents(rgba1);
        int[] comps2 = colorToComponents(rgba2);
        int[] result = {(int)Math.floor(comps1[0] + (comps2[0] - comps1[0]) * weight[0]),
                        (int)Math.floor(comps1[1] + (comps2[1] - comps1[1]) * weight[1]),
                        (int)Math.floor(comps1[2] + (comps2[2] - comps1[2]) * weight[2]),
                        (int)Math.floor(comps1[3] + (comps2[3] - comps1[3]) * weight[3])};
        return colorFromComponents(result);
    }

    /**
     * Perform linear interpolation on a color with a component-varied weight.
     *
     * <p>This exists to help migration from LibTCOD. In general, it will result
     * in more straight-forward code to use
     * <code>lerp(rgba1, rgba2, colorToFloatComponents(weight))</code> as a
     * direct replacement.
     *
     * @param rgba1
     * @param rgba2
     * @param weight set of weights of how much rgba2 goes in to rgba1
     * @see #lerp(int, int, float[])
     * @see #colorToFloatComponents(int)
     * @return
     * @deprecated Direct replacement: <code>lerp(rgba1, rgba2, colorToFloatComponents(weight))</code>
     */
    @Deprecated
    public static int lerp(int rgba1, int rgba2, int weight) {
        return lerp(rgba1, rgba2, colorToFloatComponents(weight));
    }

    public static int colorFromComponents(Map<String, Integer> components) {
        Integer alpha = components.get(ALPHA);
        if (alpha == null) {
            alpha = 255;
        }
        return colorFromComponents(components.get(RED),
                components.get(GREEN), components.get(BLUE), alpha);
    }

    public static int colorFromComponents(int[] components) {
        int alpha = 255;
        if (components.length >= 3) {
            alpha = components[3];
        }
        return colorFromComponents(components[0], components[1], components[2], alpha);
    }
}
