/* blacken - a library for Roguelike games
 * Copyright © 2011,2012 Steven Black <yam655@gmail.com>
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
package com.googlecode.blacken.colors;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.googlecode.blacken.exceptions.InvalidStringFormatException;

/**
 * @author yam655
 *
 */
public class TestColorPalette {
    /**
     * Add both an indexed color, as well as a name for the color. This makes
     * no attempt to prevent duplication in the index or in overwriting the
     * existing value in the nameMap.
     */
    @Test
    public void add_Name_ColorDef() {
        ColorPalette cp1 = new ColorPalette();
        try {
            cp1.add("white", "#fff"); //$NON-NLS-1$ //$NON-NLS-2$
            cp1.add("black", "#000000"); //$NON-NLS-1$ //$NON-NLS-2$
            cp1.add("invisible", "0x001000"); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (InvalidStringFormatException e) {
            throw new RuntimeException(e);
        } 
        assertEquals(new Integer(0xFFffffff), cp1.get("white")); //$NON-NLS-1$
        assertEquals(new Integer(0xFF000000), cp1.get("black")); //$NON-NLS-1$
        assertEquals(new Integer(0xFF001000), cp1.get("invisible")); //$NON-NLS-1$
        assertEquals(3, cp1.size());
    }

    /**
     * Add both an indexed color, as well as a name for the color. 
     */
    @Test
    public void add_Name_RGBA() {
        ColorPalette cp1 = new ColorPalette();
        cp1.add("white", 0xFFffffff); //$NON-NLS-1$
        cp1.add("black", 0xFF000000); //$NON-NLS-1$
        cp1.add("invisible", 0x1000); //$NON-NLS-1$
        assertEquals(3, cp1.size());
        assertEquals(new Integer(0xFFffffff), cp1.get("white")); //$NON-NLS-1$
        assertEquals(new Integer(0xFF000000), cp1.get("black")); //$NON-NLS-1$
        assertEquals(new Integer(0xFF001000), cp1.get("invisible")); //$NON-NLS-1$
    }

    /**
     * Add both an indexed color, as well as a name for the color. 
     */
    @Test
    public void add_Name_RGBA_Boolean() {
        ColorPalette cp1 = new ColorPalette();
        cp1.add("white", 0xFFffffff, true); //$NON-NLS-1$
        cp1.add("black", 0xFF000000, true); //$NON-NLS-1$
        cp1.add("invisible", 0x1000, true); //$NON-NLS-1$
        assertEquals(3, cp1.size());
        assertEquals(new Integer(0xFFffffff), cp1.get("white")); //$NON-NLS-1$
        assertEquals(new Integer(0xFF000000), cp1.get("black")); //$NON-NLS-1$
        assertEquals(new Integer(0x1000), cp1.get("invisible")); //$NON-NLS-1$
    }

    /**
     * This is designed as a helper to load a palette based upon color
     * values. It does not wipe the existing palette.
     */
    @Test
    public void addAll_IntArray_Boolean() {
        int[] colors = {
                        0x000000,
                        0xffffff,
                        0x01aabbcc,
                        0xff998877
        };
        ColorPalette cp1 = new ColorPalette();
        cp1.addAll(colors, false);
        assertEquals(4, cp1.size());
        cp1.addAll(colors, true);
        assertEquals(8, cp1.size());
        assertEquals(new Integer(0xff000000), cp1.get(0));
        assertEquals(new Integer(0xffffffff), cp1.get(1));
        assertEquals(new Integer(0x01aabbcc), cp1.get(2));
        assertEquals(new Integer(0xff998877), cp1.get(3));
        assertEquals(new Integer(0x00000000), cp1.get(4));
        assertEquals(new Integer(0x00ffffff), cp1.get(5));
        assertEquals(new Integer(0x01aabbcc), cp1.get(6));
        assertEquals(new Integer(0xff998877), cp1.get(7));
    }

    /**
     * This command accepts an array of strings and turns them as additional
     * names in a color palette.
     */
    @Test
    public void addMapping_StringArray() {
        String[] mapping = {
                    "black / Black -> #000", //$NON-NLS-1$
                    "white / White -> #fff", //$NON-NLS-1$
                    "name with spaces -> #00aa22", //$NON-NLS-1$
                    "name / alternate name -> #ffffff", //$NON-NLS-1$
                    "Name / Case Sensitive -> #a98765", //$NON-NLS-1$
                    "remember hex works, too -> 0x12ab9f", //$NON-NLS-1$
                    "hex supports alpha -> 0x11335577", //$NON-NLS-1$
                    "space/is->needed -> #098abd", //$NON-NLS-1$
                    "web shorthand -> #39f" //$NON-NLS-1$
        };
        ColorPalette cp1 = new ColorPalette();
        cp1.add("black", 0xf0000000); //$NON-NLS-1$
        cp1.add("white", 0xf0ffffff); //$NON-NLS-1$
        cp1.addMapping(mapping);
        assertEquals(mapping.length + 2, cp1.size());
        assertEquals(new Integer(0xf0000000), cp1.get(0));
        assertEquals(new Integer(0xf0ffffff), cp1.get(1));
        assertEquals(new Integer(0xff000000), cp1.get("black")); //$NON-NLS-1$
        assertEquals(new Integer(0xffffffff), cp1.get("white")); //$NON-NLS-1$
        assertEquals(new Integer(0xff00aa22), cp1.get("name with spaces")); //$NON-NLS-1$
        assertEquals(new Integer(0xffffffff), cp1.get("name")); //$NON-NLS-1$
        assertEquals(new Integer(0xffa98765), cp1.get("Name")); //$NON-NLS-1$
        assertEquals(new Integer(0xff12ab9f), cp1.get("remember hex works, too")); //$NON-NLS-1$
        assertEquals(new Integer(0x11335577), cp1.get(8));
        assertEquals(new Integer(0xff098abd), cp1.get("space/is->needed")); //$NON-NLS-1$
        assertEquals(new Integer(0xff3399ff), cp1.get("web shorthand")); //$NON-NLS-1$
    }


    /**
     * This command accepts an array of strings and turns them as additional
     * names in a color palette.
     */
    @Test
    public void getMapping_StringArray() {
        String[] origMapping = {
                    "black / Black -> #000", //$NON-NLS-1$
                    "white / White -> #fff", //$NON-NLS-1$
                    "name with spaces -> #00aa22", //$NON-NLS-1$
                    "name / alternate name -> #ffffff", //$NON-NLS-1$
                    "Name / Case Sensitive -> #a98765", //$NON-NLS-1$
                    "remember hex works, too -> 0x12ab9f", //$NON-NLS-1$
                    "hex supports alpha -> 0x11335577", //$NON-NLS-1$
                    "space/is->needed -> #098abd", //$NON-NLS-1$
                    "web shorthand -> #39f" //$NON-NLS-1$
        };
        String[] expectedMapping = {
                                "Black / black -> 0xff000000", //$NON-NLS-1$
                                "White / white -> 0xffffffff", //$NON-NLS-1$
                                "name with spaces -> 0xff00aa22", //$NON-NLS-1$
                                "alternate name / name -> 0xffffffff", //$NON-NLS-1$
                                "Case Sensitive / Name -> 0xffa98765", //$NON-NLS-1$
                                "remember hex works, too -> 0xff12ab9f", //$NON-NLS-1$
                                "hex supports alpha -> 0x11335577", //$NON-NLS-1$
                                "space/is->needed -> 0xff098abd", //$NON-NLS-1$
                                "web shorthand -> 0xff3399ff" //$NON-NLS-1$
                    };
        ColorPalette cp2 = new ColorPalette();
        cp2.addMapping(origMapping);
        String[] gotMapping = cp2.getMapping();
        ColorPalette cp1 = new ColorPalette();
        cp1.addMapping(gotMapping);
        assertEquals(expectedMapping.length, cp1.size());
        assertEquals(new Integer(0xff000000), cp1.get("black")); //$NON-NLS-1$
        assertEquals(cp1.indexOf("Black"), cp1.indexOf("black"));  //$NON-NLS-1$//$NON-NLS-2$
        assertEquals(new Integer(0xffffffff), cp1.get("white")); //$NON-NLS-1$
        assertEquals(cp1.indexOf("White"), cp1.indexOf("white"));  //$NON-NLS-1$//$NON-NLS-2$
        assertEquals(new Integer(0xff00aa22), cp1.get("name with spaces")); //$NON-NLS-1$
        assertEquals(new Integer(0xffffffff), cp1.get("name")); //$NON-NLS-1$
        assertEquals(new Integer(0xffa98765), cp1.get("Name")); //$NON-NLS-1$
        assertEquals(new Integer(0xff12ab9f), cp1.get("remember hex works, too")); //$NON-NLS-1$
        assertEquals(new Integer(0x11335577), cp1.get(6));
        assertEquals(new Integer(0xff098abd), cp1.get("space/is->needed")); //$NON-NLS-1$
        assertEquals(new Integer(0xff3399ff), cp1.get("web shorthand")); //$NON-NLS-1$
        assertArrayEquals(expectedMapping, gotMapping);
    }

    /**
     * Create a new empty palette.
     */
    @Test
    public void ColorPalette() {
        ColorPalette cp = new ColorPalette();
        assertEquals(0, cp.size());
    }

    /**
     * Create a new color palette based upon an collection of color values.
     */
    @Test
    public void ColorPalette_Collection() {
        List<Integer> collection = new ArrayList<Integer>();
        collection.add(0xFF112233);
        collection.add(0x00214253);
        ColorPalette cp = new ColorPalette(collection);
        assertEquals(new Integer(0xFF112233), cp.get(0));
        assertEquals(new Integer(0x00214253), cp.get(1));
        assertEquals(2, cp.size());
    }

    /**
     * Create a new empty palette.
     */
    @Test
    public void ColorPalette_int() {
        ColorPalette cp1 = new ColorPalette(16);
        assertEquals(0, cp1.size());
        cp1.putMapping(ColorNames.STANDARD_16_COLORS);
        assertEquals(16, cp1.size());
    }

    /**
     * Create a palette based upon an existing ListMap/palette.
     */
    @Test
    public void ColorPalette_ListMap() {
        ColorPalette cp1 = new ColorPalette();
        cp1.putMapping(ColorNames.STANDARD_16_COLORS);
        ColorPalette cp2 = new ColorPalette(cp1);
        assertEquals(cp1.size(), cp2.size());
        for (int i = 0; i < cp1.size(); i++) {
            assertEquals(cp1.get(i), cp2.get(i));
        }
        for (String key : cp1.keySet()) {
            assertEquals(cp1.get(key), cp2.get(key));
            assertEquals(cp1.indexOfKey(key), cp2.indexOfKey(key));
        }
        assertEquals(cp1, cp2);
    }

    /**
     * Create a palette based upon an existing palette and an explicit order.
     */
    @Test
    public void ColorPalette_ListMap_StringArray() {
        ColorPalette cp1 = new ColorPalette();
        cp1.putMapping(ColorNames.STANDARD_16_COLORS);
        String[] newOrder = { "red",  //$NON-NLS-1$
                              "green",  //$NON-NLS-1$
                              "blue" }; //$NON-NLS-1$
        ColorPalette cp2 = new ColorPalette(cp1, newOrder);
        assertEquals(3, cp2.size());
        assertEquals(cp1.get("red"), cp2.get(0)); //$NON-NLS-1$
        assertEquals(cp1.get("green"), cp2.get(1)); //$NON-NLS-1$
        assertEquals(cp1.get("blue"), cp2.get(2)); //$NON-NLS-1$
    }

    /**
     * Create a palette based upon a simple Map.
     */
    @Test
    public void ColorPalette_MapColors() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("white", 0xFFffffff); //$NON-NLS-1$
        map.put("black", 0xFF000000); //$NON-NLS-1$
        map.put("invisible", 0x1000); //$NON-NLS-1$
        ColorPalette cp1 = new ColorPalette(map);
        assertEquals(3, cp1.size());
        assertEquals(new Integer(0xFFffffff), cp1.get("white")); //$NON-NLS-1$
        assertEquals(new Integer(0xFF000000), cp1.get("black")); //$NON-NLS-1$
        assertEquals(new Integer(0x1000), cp1.get("invisible")); //$NON-NLS-1$
    }

    /**
     * Create a palette with the given named colors.
     */
    @Test
    public void ColorPalette_MapColors_StringArray() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] a = {"white", "black"}; //$NON-NLS-1$ //$NON-NLS-2$
        map.put("white", 0xFFffffff); //$NON-NLS-1$
        map.put("black", 0xFF000000); //$NON-NLS-1$
        map.put("invisible", 0x1000); //$NON-NLS-1$
        ColorPalette cp1 = new ColorPalette(map, a);
        assertEquals(2, cp1.size());
        assertEquals(new Integer(0xFFffffff), cp1.get("white")); //$NON-NLS-1$
        assertEquals(new Integer(0xFF000000), cp1.get("black")); //$NON-NLS-1$
        assertNull(cp1.get("invisible")); //$NON-NLS-1$
    }

    /**
     * Helper function to bypass the palette if it wasn't used.
     */
    @Test
    public void getColor_IndexOrColor() {
        int[] colors = {
                        0x000000,
                        0xffffff,
                        0x01aabbcc,
                        0xff998877
        };
        ColorPalette cp1 = new ColorPalette();
        cp1.addAll(colors, false);
        assertEquals(0xff000000, cp1.getColor(0));
        assertEquals(0xffffffff, cp1.getColor(1));
        assertEquals(0x01aabbcc, cp1.getColor(2));
        assertEquals(0xff998877, cp1.getColor(3));
        assertEquals(0x00000008, cp1.getColor(8));
        assertEquals(0xff000000, cp1.getColor(0xff000000));
        assertEquals(0xffffffff, cp1.getColor(0xffffffff));
        assertEquals(0x01aabbcc, cp1.getColor(0x01aabbcc));
        assertEquals(0xff998877, cp1.getColor(0xff998877));
    }

    /**
     * Add both an indexed color, as well as a name for the color. This makes
     * no attempt to prevent duplication in the index or in overwriting the
     * existing value in the nameMap.
     */
    @Test
    public void put_Name_ColorDef() {
        ColorPalette cp1 = new ColorPalette();
        cp1.add("white", 0xff888888); //$NON-NLS-1$
        cp1.add("black", 0xff888888); //$NON-NLS-1$
        try {
            cp1.put("white", "#fff"); //$NON-NLS-1$ //$NON-NLS-2$
            cp1.put("black", "#000000"); //$NON-NLS-1$ //$NON-NLS-2$
            cp1.put("invisible", "0x001000"); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (InvalidStringFormatException e) {
            throw new RuntimeException(e);
        } 
        assertEquals(new Integer(0xFFffffff), cp1.get("white")); //$NON-NLS-1$
        assertEquals(new Integer(0xFF000000), cp1.get("black")); //$NON-NLS-1$
        assertEquals(new Integer(0xFF001000), cp1.get("invisible")); //$NON-NLS-1$
        assertEquals(3, cp1.size());
    }

    /**
     * This command accepts an array of strings and turns them in to a color
     * palette.
     */
    @Test
    public void putMapping_StringArray() {
        String[] mapping = {
                            "black / Black -> #000", //$NON-NLS-1$
                            "white / White -> #fff", //$NON-NLS-1$
                            "name with spaces -> #00aa22", //$NON-NLS-1$
                            "name / alternate name -> #ffffff", //$NON-NLS-1$
                            "Name / Case Sensitive -> #a98765", //$NON-NLS-1$
                            "remember hex works, too -> 0x12ab9f", //$NON-NLS-1$
                            "hex supports alpha -> 0x11335577", //$NON-NLS-1$
                            "space/is->needed -> #098abd", //$NON-NLS-1$
                            "web shorthand -> #39f" //$NON-NLS-1$
                };
                ColorPalette cp1 = new ColorPalette();
                cp1.add("black", 0xf0000000); //$NON-NLS-1$
                cp1.putKey("even blacker", "black"); //$NON-NLS-1$ //$NON-NLS-2$
                cp1.add("white", 0xf0ffffff); //$NON-NLS-1$
                cp1.putMapping(mapping);
                assertEquals(mapping.length, cp1.size());
                assertEquals(new Integer(0xff000000), cp1.get(0));
                assertEquals(new Integer(0xffffffff), cp1.get(1));
                assertEquals(new Integer(0xff000000), cp1.get("black")); //$NON-NLS-1$
                assertEquals(cp1.get("Black"), cp1.get("even blacker"));  //$NON-NLS-1$//$NON-NLS-2$
                assertEquals(new Integer(0xffffffff), cp1.get("white")); //$NON-NLS-1$
                assertEquals(new Integer(0xff00aa22), cp1.get("name with spaces")); //$NON-NLS-1$
                assertEquals(new Integer(0xffffffff), cp1.get("name")); //$NON-NLS-1$
                assertEquals(new Integer(0xffa98765), cp1.get("Name")); //$NON-NLS-1$
                assertEquals(new Integer(0xff12ab9f), cp1.get("remember hex works, too")); //$NON-NLS-1$
                assertEquals(new Integer(0x11335577), cp1.get(6));
                assertEquals(new Integer(0xff098abd), cp1.get("space/is->needed")); //$NON-NLS-1$
                assertEquals(new Integer(0xff3399ff), cp1.get("web shorthand")); //$NON-NLS-1$
    }
}
