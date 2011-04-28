package com.googlecode.blacken.colors;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.googlecode.blacken.core.ListMap;
import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.exceptions.InvalidStringFormatException;

/**
 * Track a list of Integer items with some extra features. Added features:
 * <ul>
 * <li>capacity to keep track of named colors as well
 * <li>support for loading colors from a number of sources.
 * </ul>
 * 
 * @author Steven Black
 */
public class ColorPalette extends ListMap<String, Integer> {

    /**
     * serial ID
     */
    private static final long serialVersionUID = -8266572442676381801L;

    /**
     * Create a new empty palette.
     */
    public ColorPalette() {
        super();
    }

    /**
     * Create a new color palette based upon an existing color list.
     * 
     * @param c simple color list
     */
    public ColorPalette(Collection<Integer> c) {
        super(c);
    }

    /**
     * Create a palette based upon an existing palette and an explicit order.
     * 
     * @param existingPalette existing palette
     * @param order explicit order
     */
    public ColorPalette(ColorPalette existingPalette, String[] order) {
        super(order.length);
        if (order != null && order.length > 0) {
            putAll(existingPalette, order);
        } else {
            addAll(existingPalette);
        }
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
     * @param colors
     */
    public ColorPalette(ListMap<String, Integer> colors) {
        super(colors.size());
        addAll(colors.entrySet());
    }

    /**
     * Create a palette based upon a LinkedHashMap.
     * 
     * @param colors
     */
    public ColorPalette(LinkedHashMap<String, Integer> colors) {
        super(colors.size());
        addAll(colors.entrySet());
    }

    /**
     * Create a palette with the given named colors. (No index created.)
     * 
     * @param colors
     */
    public ColorPalette(Map<String, Integer> colors) {
        this.putAll(colors);
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
     * @param name The name of the color
     * @param rgba The color value
     * @param full_alpha when true, neverTransparent is not in effect
     * @return index of color
     */
    public int add(String name, int rgba, boolean full_alpha) {
        if (!full_alpha) {
            rgba = ColorHelper.neverTransparent(rgba);
        }
        int idx = this.size();
        this.add(rgba);
        put(name, rgba);
        return idx;
    }

    /**
     * Add both an indexed color, as well as a name for the color. This makes
     * no attempt to prevent duplication in the index or in overwriting the
     * existing value in the nameMap.
     * 
     * @param name The name of the color
     * @param colordef The text color definition (#RRGGBB, 0xAARRGGBB, etc)
     * @return true on success
     * @throws InvalidStringFormatException 
     */
    public int add(String name, String colordef) 
    throws InvalidStringFormatException {
        final Integer color = ColorHelper.makeColor(colordef);
        int idx = this.size();
        this.add(color);
        put(name, color);
        return idx;
    }

    /**
     * This is designed as a helper to load a palette based upon color
     * values. It does not wipe the existing palette, if you want the new
     * indexes to start at 0 you should call <code>unindex()</code> first.
     * <p>
     * It is optimized for reuse of the Integer objects, if available. This
     * means it will walk your indexed palette as well as your nameMap, as
     * there is never any guarantee that they will have the same colors.
     * (However it walks these only once.)
     * 
     * @param colors an array colors stored as ints (0xRRGGBB or 0xAARRGGBB)
     * @param has_alpha indicates whether a valid alpha channel is present
     * @return true if the Collection was modified
     * @throws NullPointerException
     */
    public boolean addAll(int[] colors, boolean has_alpha) {
        if (colors.length == 0) {
            return false;
        }
        for (int c : colors) {
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
     * remember hex works, too -> 0x12ab9f
     * space/is->needed -> #098abd
     * </pre>
     * 
     * All the above are valid (except #RRGGBB is not -- it only indicates
     * these are standard red-green-blue hex values), but only
     * "name / alternate name" produced more than one name mapping. (It
     * produces two names for the same color.) The spaces are required
     * before/after the "->" and the "/" for them to have meaning. The
     * behavior is very much like the load(Strings[]) function, except this
     * does not clear the palette, and it does not put the colors on the list
     * of colors available by index position.
     * 
     * @param colors
     *            array of strings containing color mapping information
     * @return true if the colors were set, false otherwise
     */
    public boolean addMapping(String[] colors) {
        if (colors == null || colors.length == 0) {
            return false;
        }
        for (final String color : colors) {
            final String s[] = color.split("[ \t]+->[ \t]+#", 2);
            final String names[] = s[0].split("[ \t]+/[ \t]+");
            if (s.length < 2) continue;
            final Integer colr = Integer.valueOf(s[1], 16);
            add(colr);
            for (final String n : names) {
                put(n, colr);
            }
        }
        return true;
    }

    /**
     * This command accepts an array of strings and turns them in to a color
     * palette. The format of the strings are as follows:
     * 
     * <pre>
     * color -> #RRGGBB
     * name with spaces -> #00aa22
     * name / alternate name -> #ffffff
     * remember hex works, too -> 0x12ab9f
     * space/is->needed -> #098abd
     * </pre>
     * 
     * All the above are valid (except #RRGGBB is not -- it only indicates
     * these are standard red-green-blue hex values), but only
     * "name / alternate name" produced more than one name mapping. (It
     * produces two names for the same color.) The spaces are required
     * before/after the "->" and the "/" for them to have meaning.
     * 
     * @param colors
     *            array of strings containing color mapping information
     * @return true if the colors were set, false otherwise
     */
    public boolean putMapping(String[] colors) {
        clear();
        if (colors == null || colors.length == 0) {
            return false;
        }
        for (final String color : colors) {
            final String s[] = color.split("[ \t]+->[ \t]+", 1);
            final String names[] = s[0].split("[ \t]+/[ \t]+", 1);
            final Integer colr = new Integer(s[1]);
            this.add(colr);
            for (final String n : names) {
                put(n, colr);
            }
        }
        return true;
    }

}
