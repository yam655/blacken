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

import com.googlecode.blacken.core.ListMap;
import com.googlecode.blacken.exceptions.InvalidStringFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A named and indexed color palette.
 * 
 * <p>This leverages {@link ListMap} to create a color palette that supports 
 * both the common use-cases of index-based access as well as name-based 
 * access. See {@link ColorNames} for standard palettes to use with this.</p>
 * 
 * <p>The greatest flexibility is found in the {@link #addMapping(String[])}
 * and {@link #putMapping(String[])} functions.<p>
 *
 * <p>In the future (version 2.0) this will not be a simple
 * ListMap<String, Integer> but a ListMap<String, PaletteEntry<T>> with
 * PaletteEntry supporting both an integer as well as a configurable
 * cache type. It may well actually implement enough to still feel like
 * ListMap<String, Integer>, but there will be more there as well.</p?
 *
 * @author Steven W. Black
 * @since 1.0
 */
public class ColorPalette extends ListMap<String, Integer> {
    // XXX Version 2: Add cache value to normalize with AwtPalette

    private static final Logger LOGGER = LoggerFactory.getLogger(ColorPalette.class);
    /**
     * serial ID
     */
    private static final long serialVersionUID = 3453955962929981683L;
    /**
     * Constant string used to identify color mapping file. (Useful for syntax
     * highlighting and file-format sanity checks.)
     */
    public static final String BLACKEN_COLOR_MAPPING_TYPE="blacken-color-mapping";
    /**
     * The current Blacken color mapping version. This increments when there
     * are changes.
     *
     * <ul>
     * <li>0 - Prior to Blacken 1.2 there was no type or version information.
     * <li>1 - various changes:<ul>
     *       <li>First version number used.
     *       <li>Color formats: #RGB, #RRGGBB,
     *           0xRRGGBB. 0xAARRGGBB, "rrr,ggg,bbb", "rrr ggg bbb",
     *           "rrr,ggg,bbb,aaa", "rrr ggg bbb aaa". (Uppercase denotes hex,
     *           lowercase denotes decimal.) First version to support comments,
     *           and to have generalized configuration variable support.
     *      <li>Added @{name} references to other colors. Also
     *          added HSV-based modifications to previously referenced colors.
     *          Modifiable components include "value", "saturation", and "alpha",
     *          and they are all floats between 0 and 1.
     *          Float alpha modification done using the "ALPHA.0" label to
     *          distinguish it from (later) decimal alpha changes.
     *          (ex. "<code>@{Color of Awesome} value=-10%</code>")
     *      <li>Modifications include saturation (or "sat" or "s") and value 
     *          (or "v") in addition to the float alpha (as "alpha.0"). These
     *          are case-insensitive. Modifications include setting (no prefix),
     *          adding a float ("+"), subtracting a float ("-"), multiplying
     *          a float ("*"), dividing a float ("/"). All of these can use a
     *          percentage of the existing value instead of a static value by
     *          suffixing "%".
     *      </ul>
     * </ul>
     */
    public static final Integer BLACKEN_COLOR_MAPPING_VERSION=1;
    /**
     * For the current color mapping version, what version of Blacken was it
     * released with? (Older versions of Blacken will be incompatible.)
     */
    public static final String BLACKEN_COLOR_MAPPING_MIN_BLACKEN="1.2";
    private boolean caseInsensitive = false;
    static private Locale defaultPaletteLocale = Locale.ENGLISH;
    private Locale paletteLocale = defaultPaletteLocale;

    /**
     * Create a new empty palette.
     */
    public ColorPalette() {
        super();
    }

    /**
     * Create a new color palette based upon an collection of color values.
     * 
     * @param c simple color list
     */
    public ColorPalette(Collection<Integer> c) {
        super(c);
    }

    /**
     * Create a new empty palette.
     * 
     * @param initialCapacity initial capacity
     */
    public ColorPalette(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Create a palette based upon an existing ListMap/palette.
     * 
     * @param colors previous ColorPalette-like ListMap
     */
    public ColorPalette(ListMap<String, Integer> colors) {
        super(colors.size());
        addAll(colors);
    }

    public ColorPalette(ListMap<String, Integer> colors, boolean caseInsensative) {
        super(colors.size());
        this.caseInsensitive = caseInsensative;
        addAll(colors);
    }

    /**
     * Create a palette based upon an existing palette and an explicit order.
     * 
     * <p>This is used to change the order of a palette.</p>
     * 
     * <p>Note that due to the use of {@link #putAll(ListMap, String[])}
     * items in the <code>existingPalette</code> which do not exist in the
     * <code>order</code> will not exist in the returning palette.</p> 
     * 
     * @param existingPalette existing palette
     * @param order explicit order
     */
    public ColorPalette(ListMap<String, Integer> existingPalette, 
                        String[] order) {
        super(order.length);
        putAll(existingPalette, order);
    }

    /**
     * Create a palette based upon a simple Map.
     * 
     * <p>The index will be inconsistent if the Map does not have a known 
     * order, such as by use of {@link LinkedHashMap}.</p>
     * 
     * @param colors name to color value mapping
     */
    public ColorPalette(Map<String, Integer> colors) {
        super(colors.size());
        addAll(colors.entrySet());
    }

    /**
     * Create a palette with the given named colors.
     * 
     * @param colors name to color value mapping
     * @param order order of the palette
     */
    public ColorPalette(Map<String, Integer> colors, String[] order) {
        super(order.length);
        if (order.length > 0) {
            putAll(colors, order);
        } else {
            putAll(colors);
        }
    }

    /**
     * Add both an indexed color, as well as a name for the color. 
     * 
     * <p>This makes no attempt to prevent duplication in the index or in 
     * overwriting the existing value in the nameMap.</p>
     * 
     * @param name The name of the color
     * @param rgba The RGB/RGBA color value (never transparent)
     * @return index of color
     */
    public int add(String name, int rgba) {
        return this.add(name, rgba, false);
    }

    /**
     * Add both an indexed color, as well as a name for the color. 
     * 
     * <p>This currently makes no attempt to prevent duplication in the index 
     * or in overwriting the existing value in the nameMap.</p>
     * 
     * <p>This uses {@link ColorHelper#neverTransparent(int)} to add opaquacy
     * if <code>full_alpha</code> is false.
     *
     * @param name The name of the color
     * @param rgba The color value
     * @param full_alpha when true, neverTransparent is not in effect
     * @return index of color
     */
    public int add(String name, int rgba, boolean full_alpha) {
        if (!full_alpha) {
            rgba = ColorHelper.neverTransparent(rgba);
        }
        if (this.caseInsensitive) {
            name = name.toLowerCase(paletteLocale);
        }
        int idx = this.size();
        this.add(name, (Integer)rgba);
        put(name, rgba);
        return idx;
    }

    /**
     * Add both an indexed color, as well as a name for the color. This makes
     * no attempt to prevent duplication in the index or in overwriting the
     * existing value in the nameMap.
     * 
     * <p>This includes a call to {@link ColorHelper#neverTransparent(int)} so
     * \#112233 will show the same as 0xFF112233 -- as you would expect.</p>
     * 
     * @param name The name of the color
     * @param colordef The text color definition (#RRGGBB, 0xAARRGGBB, etc)
     * @return true on success
     * @throws InvalidStringFormatException 
     *          <code>colordef</code> provided was illegal.
     */
    public int add(String name, String colordef) 
    throws InvalidStringFormatException {
        final Integer color = ColorHelper.makeColor(colordef);
        if (this.caseInsensitive) {
            name = name.toLowerCase(paletteLocale);
        }
        this.add(name, color);
        return indexOfKey(name);
    }

    /**
     * If case-insensitive keys/names are used, this will return the canonical
     * internal representation of a key. This may be useful for debugging in
     * situations where you think a key should exist but it does not.
     *
     * <p>Case insensitivity is handled by using
     * {@link String#toLowerCase(Locale)} so if this isn't returning the
     * expected response, you should explicitly set the palette locale.
     *
     * <p>The locale can be set through {@link #setPaletteLocale(Locale)} or
     * you can set the default palette for all future instances with
     * {@link #setDefaultPaletteLocale(Locale)}.
     *
     * @param name
     * @return
     */
    public String canonicalKey(String name) {
        if (this.caseInsensitive) {
            name = name.toLowerCase(paletteLocale);
        }
        return name;
    }

    /**
     * Get this palette's locale.
     *
     * @since 1.2
     * @see #setPaletteLocale(Locale)
     * @see #setDefaultPaletteLocale(Locale)
     * @return this palette's locale
     */
    public Locale getPaletteLocale() {
        return paletteLocale;
    }

    /**
     * Set this palette's locale for case-insensitive keys. This should be
     * done either before anything has been added to the palette or before the
     * palette has been switched froma case-sensitive palette.
     *
     * @since 1.2
     * @see #setDefaultPaletteLocale(Locale)
     * @param paletteLocale
     */
    public void setPaletteLocale(Locale paletteLocale) {
        this.paletteLocale = paletteLocale;
    }

    /**
     * This is designed as a helper to load a palette based upon color
     * values. It does not wipe the existing palette.
     * 
     * @param colors an array colors stored as ints (0xRRGGBB or 0xAARRGGBB)
     * @param has_alpha indicates whether a valid alpha channel is present
     * @return true if the Collection was modified
     */
    public boolean addAll(int[] colors, boolean has_alpha) {
        if (colors.length == 0) {
            return false;
        }
        for (int clr : colors) {
            int c = clr;
            if (!has_alpha) {
                c = ColorHelper.neverTransparent(c);
                // regardless of has_alpha, c now contains RGBA data
            }
            add(c);
        }
        return true;
    }

    /**
     * This command accepts an array of strings and turns them as additional
     * names in a color palette. The format of the strings are as follows:
     * 
     * <pre>
     * color -> #RRGGBB
     * name with spaces -> #00aa22
     * name / alternate name -> #ffffff
     * Name / Case Sensitive -> #a98765
     * remember hex works, too -> 0x12ab9f
     * hex supports alpha -> 0x11335577
     * space/is->needed -> #098abd
     * web shorthand -> #39f
     * </pre>
     * 
     * <p>All the above are valid (except #RRGGBB is not -- it only indicates
     * these are standard red-green-blue hex values), but only
     * "name / alternate name" produced more than one name mapping. (It
     * produces two names for the same color.) The spaces are required
     * before/after the "->" and the "/" for them to have meaning. The
     * behavior is very much like the load(Strings[]) function, except this
     * does not clear the palette, and it does not put the colors on the list
     * of colors available by index position.</p>
     * 
     * <p>Note that hex has full support for alpha -- if you specify 8 hex 
     * digits it will allow you to set zero (0) alpha making it fully 
     * transparent. If you only specify 6 digits it performs the standard
     * {@link ColorHelper#neverTransparent(int)} logic.</p>
     * 
     * @param colors
     *            array of strings containing color mapping information
     * @return true if the colors were set, false otherwise
     */
    public boolean addMapping(String[] colors) {
        return processMapping(colors, true);
    }

    /**
     * Load a mapping by resource filename.
     *
     * @param forLoader
     * @param name
     * @return
     * @throws IOException
     */
    public boolean addMappingResource(Class forLoader, String name) throws IOException {
        InputStream stream = forLoader.getResourceAsStream(name);
        byte[] bytebuf = new byte[4096];
        int cnt;
        StringBuilder buf = new StringBuilder();
        while ((cnt = stream.read(bytebuf)) != -1) {
            buf.append(new String(bytebuf, 0, cnt, Charset.forName("UTF-8")));
        }
        return addMapping(buf.toString().split("[\n\r]+"));
    }

    /**
     * Load a mapping by resource filename.
     *
     * @param forLoader
     * @param name
     * @return
     * @throws IOException
     */
    public boolean putMappingResource(Class forLoader, String name) throws IOException {
        InputStream stream = forLoader.getResourceAsStream(name);
        byte[] bytebuf = new byte[4096];
        int cnt;
        StringBuilder buf = new StringBuilder();
        while ((cnt = stream.read(bytebuf)) != -1) {
            buf.append(new String(bytebuf, 0, cnt, Charset.forName("UTF-8")));
        }
        return putMapping(buf.toString().split("[\r\n]+"));
    }

    @Override
    public Integer get(String key) {
        String k = key;
        if (this.caseInsensitive) {
            k = key.toLowerCase(paletteLocale);
        }
        return super.get(k);
    }

    /**
     * Helper function to bypass the palette if it wasn't used.
     * 
     * <p>To support environments where setting a palette is optional,
     * we use this function to check to see if a color value is a valid
     * color index -- if it is not we consider it a 0xAARRGGBB color.<p>
     * 
     * <p>Note: Due to the implementation of this function, and the fact that
     * is performs an educated guess, if you use fully transparent colors
     * (with an Alpha of 0) this can make a wrong guess, particularly for dark
     * blue colors or if index zero is not black. The work-around is to use an 
     * Alpha of 1.</p>
     * 
     * @param indexOrColor an index in the palette or 0xAARRGGBB color
     * @return a 0xAARRGGBB color
     */
    public int getColor(int indexOrColor) {
        int ret = 0;
        if (indexOrColor < size() && indexOrColor >= 0) {
            // This is near guaranteed correct.
            ret = get(indexOrColor);
        } else {
            ret = indexOrColor;
        }
        return ret;
    }
    /**
     * This is sort of a companion to {@link #getColor(int)}. The
     * <code>keyOrColor</codde> should either be a key (or color label) in to
     * the palette or it should be a legal color definition that
     * {@link ColorHelper#makeColor(java.lang.String)} can parse.
     *
     * @param keyOrColor
     * @return
     * @since 1.1
     */
    public int getColorOrIndex(String keyOrColor) {
        if (this.containsKey(keyOrColor)) {
            return this.indexOfKey(keyOrColor);
        }
        if (this.caseInsensitive) {
            String name = keyOrColor.toLowerCase(paletteLocale);
            if (this.containsKey(name)) {
                return this.indexOfKey(name);
            }
        }

        try {
            return ColorHelper.makeColor(keyOrColor);
        } catch (InvalidStringFormatException ex) {
            throw new IllegalArgumentException("Not in palette and illegal color definition" + keyOrColor);
        }
    }

    /**
     * Create a mapping array from the palette
     * 
     * @return mapping array
     */
    public String[] getMapping() {
        List<String> ret = new ArrayList<>(size());
        Map<Integer, ConsolidatedListMapEntry<String, Integer>> ce = 
            new HashMap<>();
        this.consolidateEntries(ce );
        ret.add(String.format("# Blacken color mapping version %s appeared in Blacken %s", BLACKEN_COLOR_MAPPING_VERSION, BLACKEN_COLOR_MAPPING_MIN_BLACKEN));
        ret.add("TYPE=" + BLACKEN_COLOR_MAPPING_TYPE);
        ret.add("VERSION=" + BLACKEN_COLOR_MAPPING_VERSION.toString());
        for (Integer i = 0; i < ce.size(); i++) {
            ConsolidatedListMapEntry<String, Integer> e = ce.get(i);
            StringBuilder buf = new StringBuilder();
            List<String> sortKeys = new ArrayList<>(e.getKeys());
            Collections.sort(sortKeys);
            for (String key : sortKeys) {
                if (buf.length() > 0) {
                    buf.append(" / ");
                }
                buf.append(key);
            }
            if (buf.length() > 0) {
                buf.append(" -> ");
            }
            buf.append(String.format("%#08x", e.getValue()));
            ret.add(buf.toString());
        }
        return ret.toArray(new String[0]);
    }

    /**
     * Process both the add and put mapping commands
     * @param colors color mapping array
     * @param onlyAdd true: only do add; false: do put or add
     * @return true if change, false otherwise
     */
   protected boolean processMapping(String[] colors, boolean onlyAdd) {
        if (colors == null || colors.length == 0) {
            return false;
        }
        int firstColors = this.size();
        Pattern varPattern = Pattern.compile("^[A-Z]+[=].*$");
        for (final String color : colors) {
            if (color.matches("^\\s*(#.*)?$")) {
                continue;
            }
            Matcher varMatcher = varPattern.matcher(color);
            if (varMatcher.matches()) {
                String[] v = color.split("=", 2);
                switch (v[0]) {
                case "TYPE":
                    if (!BLACKEN_COLOR_MAPPING_TYPE.equals(v[1])) {
                        throw new RuntimeException(
                                "Failed to find expected type: " + v[1]);
                    }
                    break;
                case "VERSION":
                    int found = Integer.parseInt(v[1]);
                    if (found > BLACKEN_COLOR_MAPPING_VERSION) {
                        throw new RuntimeException(
                                "Unsupported color mapping version: " + v[1]);
                    }
                    break;
                default:
                    throw new RuntimeException(
                            "Unsupported configuration variable: " + color);
                }
                continue;
            }
            final String s[] = color.split("[ \t]+->[ \t]+", 2);
            final String names[];
            final String colorDef;
            if (s.length == 1) {
                if (ColorHelper.isColorDefinition(s[0])) {
                    colorDef = s[0];
                    names = new String[0];
                } else {
                    throw new RuntimeException(String.format
                                           ("Color format lacked '->': %s", 
                                            color));
                }
            } else {
                names = s[0].split("[ \t]+/[ \t]+");
                colorDef = s[1];
            }
            Integer colr = makeColor(colorDef);
            Integer idx = -1;
            if (!onlyAdd) {
                for (final String n1 : names) {
                    String n = n1;
                    if (this.caseInsensitive) {
                        n = n.toLowerCase(paletteLocale);
                    }
                    if (this.containsKey(n)) {
                        idx = this.indexOfKey(n);
                        // Do not stomp on a color from the new set
                        if (idx >= firstColors) {
                            idx = -1;
                        } else {
                            break;
                        }
                    }
                }
            }
            if (idx == -1) {
                if (names.length == 0) {
                    add(colr);
                } else {
                    add(names, colr);
                }
            } else {
                this.set(idx, colr);
                putKey(names, idx);
            }
        }
        return true;
    }

   private int makeColor(String colorDef) {
       Integer colr = null;
        if (colorDef.startsWith("@{")) {
            String[] s2 = colorDef.split("}\\s*", 2);
            String colorDef2 = s2[0].substring(2);
            if (this.containsKey(colorDef2)) {
                colr = this.get(colorDef2);
            } else {
                try {
                    colr = ColorHelper.makeColor(colorDef2);
                } catch (InvalidStringFormatException e) {
                    throw new RuntimeException(e);
                }
            }
            Map<String, Integer> components = ColorHelper.colorToComponents(colr, null);
            Map<String, Float> hsva = ColorHelper.colorToHSV(colr, null);
            for (String mod : s2[1].split("\\s*")) {
                String[] ma = mod.split("=", 2);
                String op = ma[0].toUpperCase(Locale.ENGLISH);
                if (op.equals("VALUE") || op.equals("V")) {
                    parseFloatMod(hsva, components, ColorHelper.VALUE, ma[1]);
                } else if (op.equals("SATURATION") || op.equals("SAT") || op.equals("S")) {
                    parseFloatMod(hsva, components, ColorHelper.SATURATION, ma[1]);
                } else if (op.equals("ALPHA.0")) {
                    parseFloatMod(hsva, components, ColorHelper.ALPHA, ma[1]);
                }
            }
        } else {
            try {
                colr = ColorHelper.makeColor(colorDef);
            } catch (InvalidStringFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return colr;
   }

    /**
     * Update both an indexed color, as well as a name for the color. This will
     * overwrite an existing value.
     * 
     * <p>This includes a call to {@link ColorHelper#neverTransparent(int)} so
     * \#112233 will show the same as 0xFF112233 -- as you would expect.</p>
     * 
     * @param name The name of the color
     * @param colordef The text color definition (#RRGGBB, 0xAARRGGBB, etc)
     * @return true on success
     * @throws InvalidStringFormatException 
     *          <code>colordef</code> provided was illegal.
     */
    public int put(String name, String colordef) 
    throws InvalidStringFormatException {
        final Integer color = ColorHelper.makeColor(colordef);
        if (this.caseInsensitive) {
            name = name.toLowerCase(paletteLocale);
        }
        this.put(name, color);
        return indexOfKey(name);
    }
    
    /**
     * This command accepts an array of strings and turns them in to a color
     * palette. The format of the strings are as follows:
     * 
     * <pre>
     * color -> #RRGGBB
     * name with spaces -> #00aa22
     * name / alternate name -> #ffffff
     * Name / Case Sensitive -> #a98765
     * remember hex works, too -> 0x12ab9f
     * hex supports alpha -> 0x11335577
     * space/is->needed -> #098abd
     * web shorthand -> #39f
     * </pre>
     * 
     * <p>All the above are valid (except #RRGGBB is not -- it only indicates
     * these are standard red-green-blue hex values), but only
     * "name / alternate name" produced more than one name mapping. (It
     * produces two names for the same color.) The spaces are required
     * before/after the "->" and the "/" for them to have meaning.</p>
     * 
     * <p>Note that hex has full support for alpha -- if you specify 8 hex 
     * digits it will allow you to set zero (0) alpha making it fully 
     * transparent. If you only specify 6 digits it performs the standard
     * {@link ColorHelper#neverTransparent(int)} logic.</p>
     * 
     * @param colors
     *            array of strings containing color mapping information
     * @return true if the colors were set, false otherwise
     */
    public boolean putMapping(String[] colors) {
        return processMapping(colors, false);
    }

    private void parseFloatMod(Map<String, Float> hsva, Map<String, Integer> components, String part, String value) {
        Float o = hsva.get(part);
        if (value.endsWith("%")) {
            char m = value.charAt(0);
            float v;
            switch(m) {
            case '+':
                v = Float.parseFloat(value.substring(1, value.length()-1));
                hsva.put(part, o + o * v);
                break;
            case '-':
                v = Float.parseFloat(value.substring(1, value.length()-1));
                hsva.put(part, o - o * v);
                break;
            case '*':
                v = Float.parseFloat(value.substring(1, value.length()-1));
                hsva.put(part, o * (o * v));
                break;
            case '/':
                v = Float.parseFloat(value.substring(1, value.length()-1));
                hsva.put(part, o / (o * v));
                break;
            default:
                v = Float.parseFloat(value.substring(0, value.length()-1));
                hsva.put(part, o * v);
                break;
            }
        } else {
            char m = value.charAt(0);
            float v;
            switch(m) {
            case '+':
                v = Float.parseFloat(value.substring(1, value.length()));
                hsva.put(part, o + o * v);
                break;
            case '-':
                v = Float.parseFloat(value.substring(1, value.length()));
                hsva.put(part, o - o * v);
                break;
            case '*':
                v = Float.parseFloat(value.substring(1, value.length()));
                hsva.put(part, o * (o * v));
                break;
            case '/':
                v = Float.parseFloat(value.substring(1, value.length()));
                hsva.put(part, o / (o * v));
                break;
            default:
                v = Float.parseFloat(value.substring(0, value.length()));
                hsva.put(part, v);
                break;
            }
        }
        ColorHelper.colorToComponents(ColorHelper.colorFromHSV(hsva), components);
    }

    /**
     * @since 1.2
     * @return
     */
    public static Locale getDefaultPaletteLocale() {
        return defaultPaletteLocale;
    }

    /**
     * This sets the default locale used for case insensitive palletes.
     *
     * <p>We do <em>not</em> default to the system locale, as that could have
     * unexpected results if the user is in a poorly tested locale. Besides,
     * the palette strings will be in the locale of the developer when they
     * are not the Blacken system default (which is English).
     *
     * @param defaultPaletteLocale
     * @since 1.2
     */
    public static void setDefaultPaletteLocale(Locale defaultPaletteLocale) {
        ColorPalette.defaultPaletteLocale = defaultPaletteLocale;
    }

    @Override
    protected Integer add(Map.Entry<String, Integer> entry) {
        Map.Entry<String, Integer> ntry = entry;
        if (this.caseInsensitive) {
            String name = entry.getKey();
            name = name.toLowerCase(paletteLocale);
            if (!name.equals(entry.getKey())) {
                ntry = new AbstractMap.SimpleEntry<>(name, entry.getValue());
            }
        }
        return super.add(ntry);
    }

    @Override
    public boolean add(String key, Integer value) {
        if (this.caseInsensitive) {
            key = key.toLowerCase(paletteLocale);
        }
        return super.add(key, value);
    }

    @Override
    public ColorPalette clone() {
        return new ColorPalette(this);
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
        if (!caseInsensitive) {
            return;
        }
        Set<String> keySet = new HashSet<>(keySet());
        for (String key : keySet) {
            String lcKey = key.toLowerCase(paletteLocale);
            if (lcKey.equals(key)) {
                continue;
            }
            putKey(lcKey, key);
            this.removeKey(key);
        }
    }

    @Override
    public int putKey(String newKey, String existingKey) {
        if (this.caseInsensitive) {
            newKey = newKey.toLowerCase(paletteLocale);
            existingKey = existingKey.toLowerCase(paletteLocale);
        }
        return super.putKey(newKey, existingKey);
    }

    @Override
    public int putKey(String key, int index) {
        if (this.caseInsensitive) {
            key = key.toLowerCase(paletteLocale);
        }
        return super.putKey(key, index);
    }

}
