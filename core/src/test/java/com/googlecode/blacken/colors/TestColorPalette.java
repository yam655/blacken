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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.googlecode.blacken.exceptions.InvalidStringFormatException;
import java.util.Arrays;

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
            cp1.add("white", "#fff");
            cp1.add("black", "#000000");
            cp1.add("invisible", "0x001000");
        } catch (InvalidStringFormatException e) {
            throw new RuntimeException(e);
        } 
        assertEquals(new Integer(0xFFffffff), cp1.get("white"));
        assertEquals(new Integer(0xFF000000), cp1.get("black"));
        assertEquals(new Integer(0xFF001000), cp1.get("invisible"));
        assertEquals(3, cp1.size());
    }

    /**
     * Add both an indexed color, as well as a name for the color. 
     */
    @Test
    public void add_Name_RGBA() {
        ColorPalette cp1 = new ColorPalette();
        cp1.add("white", 0xFFffffff);
        cp1.add("black", 0xFF000000);
        cp1.add("invisible", 0x1000);
        assertEquals(3, cp1.size());
        assertEquals(new Integer(0xFFffffff), cp1.get("white"));
        assertEquals(new Integer(0xFF000000), cp1.get("black"));
        assertEquals(new Integer(0xFF001000), cp1.get("invisible"));
    }

    /**
     * Add both an indexed color, as well as a name for the color. 
     */
    @Test
    public void add_Name_RGBA_Boolean() {
        ColorPalette cp1 = new ColorPalette();
        cp1.add("white", 0xFFffffff, true);
        cp1.add("black", 0xFF000000, true);
        cp1.add("invisible", 0x1000, true);
        assertEquals(3, cp1.size());
        assertEquals(new Integer(0xFFffffff), cp1.get("white"));
        assertEquals(new Integer(0xFF000000), cp1.get("black"));
        assertEquals(new Integer(0x1000), cp1.get("invisible"));
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
                    "black / Black -> #000",
                    "white / White -> #fff",
                    "name with spaces -> #00aa22",
                    "name / alternate name -> #ffffff",
                    "Name / Case Sensitive -> #a98765",
                    "remember hex works, too -> 0x12ab9f",
                    "hex supports alpha -> 0x11335577",
                    "space/is->needed -> #098abd",
                    "web shorthand -> #39f"
        };
        ColorPalette cp1 = new ColorPalette();
        cp1.add("black", 0xf0000000);
        cp1.add("white", 0xf0ffffff);
        cp1.addMapping(mapping);
        assertEquals(mapping.length + 2, cp1.size());
        assertEquals(new Integer(0xf0000000), cp1.get(0));
        assertEquals(new Integer(0xf0ffffff), cp1.get(1));
        assertEquals(new Integer(0xff000000), cp1.get("black"));
        assertEquals(new Integer(0xffffffff), cp1.get("white"));
        assertEquals(new Integer(0xff00aa22), cp1.get("name with spaces"));
        assertEquals(new Integer(0xffffffff), cp1.get("name"));
        assertEquals(new Integer(0xffa98765), cp1.get("Name"));
        assertEquals(new Integer(0xff12ab9f), cp1.get("remember hex works, too"));
        assertEquals(new Integer(0x11335577), cp1.get(8));
        assertEquals(new Integer(0xff098abd), cp1.get("space/is->needed"));
        assertEquals(new Integer(0xff3399ff), cp1.get("web shorthand"));
    }


    /**
     * This command accepts an array of strings and turns them as additional
     * names in a color palette.
     */
    @Test
    public void getMapping_StringArray() {
        String[] origMapping = {
                    "black / Black -> #000",
                    "white / White -> #fff",
                    "name with spaces -> #00aa22",
                    "name / alternate name -> #ffffff",
                    "Name / Case Sensitive -> #a98765",
                    "remember hex works, too -> 0x12ab9f",
                    "hex supports alpha -> 0x11335577",
                    "space/is->needed -> #098abd",
                    "web shorthand -> #39f"
        };
        String[] expectedMapping = {
                    "Black / black -> 0xff000000",
                    "White / white -> 0xffffffff",
                    "name with spaces -> 0xff00aa22",
                    "alternate name / name -> 0xffffffff",
                    "Case Sensitive / Name -> 0xffa98765",
                    "remember hex works, too -> 0xff12ab9f",
                    "hex supports alpha -> 0x11335577",
                    "space/is->needed -> 0xff098abd",
                    "web shorthand -> 0xff3399ff"
        };
        ColorPalette cp2 = new ColorPalette();
        cp2.addMapping(origMapping);
        List<String> gotList = new ArrayList<>(Arrays.asList(cp2.getMapping()));
        assertTrue(gotList.get(0).startsWith("#"));
        gotList.remove(0);
        assertEquals(String.format("TYPE=%s",
                ColorPalette.BLACKEN_COLOR_MAPPING_TYPE), gotList.get(0));
        gotList.remove(0);
        assertEquals(String.format("VERSION=%s",
                ColorPalette.BLACKEN_COLOR_MAPPING_VERSION), gotList.get(0));
        gotList.remove(0);
        String[] gotMapping = gotList.toArray(new String[0]);
        ColorPalette cp1 = new ColorPalette();
        cp1.addMapping(gotMapping);
        assertEquals(expectedMapping.length, cp1.size());
        assertEquals(new Integer(0xff000000), cp1.get("black"));
        assertEquals(cp1.indexOf("Black"), cp1.indexOf("black"));
        assertEquals(new Integer(0xffffffff), cp1.get("white"));
        assertEquals(cp1.indexOf("White"), cp1.indexOf("white"));
        assertEquals(new Integer(0xff00aa22), cp1.get("name with spaces"));
        assertEquals(new Integer(0xffffffff), cp1.get("name"));
        assertEquals(new Integer(0xffa98765), cp1.get("Name"));
        assertEquals(new Integer(0xff12ab9f), cp1.get("remember hex works, too"));
        assertEquals(new Integer(0x11335577), cp1.get(6));
        assertEquals(new Integer(0xff098abd), cp1.get("space/is->needed"));
        assertEquals(new Integer(0xff3399ff), cp1.get("web shorthand"));
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
        String[] newOrder = { "red", 
                              "green", 
                              "blue" };
        ColorPalette cp2 = new ColorPalette(cp1, newOrder);
        assertEquals(3, cp2.size());
        assertEquals(cp1.get("red"), cp2.get(0));
        assertEquals(cp1.get("green"), cp2.get(1));
        assertEquals(cp1.get("blue"), cp2.get(2));
    }

    /**
     * Create a palette based upon a simple Map.
     */
    @Test
    public void ColorPalette_MapColors() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("white", 0xFFffffff);
        map.put("black", 0xFF000000);
        map.put("invisible", 0x1000);
        ColorPalette cp1 = new ColorPalette(map);
        assertEquals(3, cp1.size());
        assertEquals(new Integer(0xFFffffff), cp1.get("white"));
        assertEquals(new Integer(0xFF000000), cp1.get("black"));
        assertEquals(new Integer(0x1000), cp1.get("invisible"));
    }

    /**
     * Create a palette with the given named colors.
     */
    @Test
    public void ColorPalette_MapColors_StringArray() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] a = {"white", "black"};
        map.put("white", 0xFFffffff);
        map.put("black", 0xFF000000);
        map.put("invisible", 0x1000);
        ColorPalette cp1 = new ColorPalette(map, a);
        assertEquals(2, cp1.size());
        assertEquals(new Integer(0xFFffffff), cp1.get("white"));
        assertEquals(new Integer(0xFF000000), cp1.get("black"));
        assertNull(cp1.get("invisible"));
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
        cp1.add("white", 0xff888888);
        cp1.add("black", 0xff888888);
        try {
            cp1.put("white", "#fff");
            cp1.put("black", "#000000");
            cp1.put("invisible", "0x001000");
        } catch (InvalidStringFormatException e) {
            throw new RuntimeException(e);
        } 
        assertEquals(new Integer(0xFFffffff), cp1.get("white"));
        assertEquals(new Integer(0xFF000000), cp1.get("black"));
        assertEquals(new Integer(0xFF001000), cp1.get("invisible"));
        assertEquals(3, cp1.size());
    }

    /**
     * This command accepts an array of strings and turns them in to a color
     * palette.
     */
    @Test
    public void putMapping_StringArray() {
        String[] mapping = {
            "", "",
            "black / Black -> #000",
            "white / White -> #fff",
            "name with spaces -> #00aa22",
            "name / alternate name -> #ffffff",
            "Name / Case Sensitive -> #a98765",
            "remember hex works, too -> 0x12ab9f",
            "hex supports alpha -> 0x11335577",
            "space/is->needed -> #098abd",
            "web shorthand -> #39f"
        };
        mapping[0] = String.format("TYPE=%s",
            ColorPalette.BLACKEN_COLOR_MAPPING_TYPE);
        mapping[1] = String.format("VERSION=%s",
            ColorPalette.BLACKEN_COLOR_MAPPING_VERSION);
        ColorPalette cp1 = new ColorPalette();
        cp1.add("black", 0xf0000000);
        cp1.putKey("even blacker", "black");
        cp1.add("white", 0xf0ffffff);
        cp1.putMapping(mapping);
        assertEquals(mapping.length - 2, cp1.size());
        assertEquals(new Integer(0xff000000), cp1.get(0));
        assertEquals(new Integer(0xffffffff), cp1.get(1));
        assertEquals(new Integer(0xff000000), cp1.get("black"));
        assertEquals(cp1.get("Black"), cp1.get("even blacker"));
        assertEquals(new Integer(0xffffffff), cp1.get("white"));
        assertEquals(new Integer(0xff00aa22), cp1.get("name with spaces"));
        assertEquals(new Integer(0xffffffff), cp1.get("name"));
        assertEquals(new Integer(0xffa98765), cp1.get("Name"));
        assertEquals(new Integer(0xff12ab9f), cp1.get("remember hex works, too"));
        assertEquals(new Integer(0x11335577), cp1.get(6));
        assertEquals(new Integer(0xff098abd), cp1.get("space/is->needed"));
        assertEquals(new Integer(0xff3399ff), cp1.get("web shorthand"));
    }

        /**
     * This command accepts an array of strings and turns them in to a color
     * palette.
     */
    @Test
    public void putMapping_insensitive_StringArray() {
        String[] mapping = {
            "", "",
            "Black -> #000",
            "White -> #fff",
            "name WITH spaces -> #00aa22",
            "Alternate name -> #ffffff",
            "Case Sensitive -> #a98765",
            "Remember hex works, too -> 0x12ab9f",
            "hex supports ALPHA -> 0x11335577",
            "space/IS->needed -> #098abd",
            "web shorTHand -> #39f"
        };
        mapping[0] = String.format("TYPE=%s",
            ColorPalette.BLACKEN_COLOR_MAPPING_TYPE);
        mapping[1] = String.format("VERSION=%s",
            ColorPalette.BLACKEN_COLOR_MAPPING_VERSION);
        ColorPalette cp1 = new ColorPalette();
        cp1.setCaseInsensitive(true);
        cp1.add("bLack", 0xf0000000);
        cp1.putKey("even BLacker", "BlacK");
        cp1.add("white", 0xf0ffffff);
        assertEquals(new Integer(0xf0ffffff), cp1.get("white"));
        cp1.putMapping(mapping);
        assertEquals(mapping.length - 2, cp1.size());
        assertEquals(new Integer(0xff000000), cp1.get(0));
        assertEquals(new Integer(0xffffffff), cp1.get(1));
        assertEquals(cp1.get("Black"), cp1.get("blACK"));
        assertEquals(cp1.get("Black"), cp1.get("even blACKer"));
        assertEquals((Integer)0xffffffff, cp1.get("white"));
        assertEquals((Integer)0xff00aa22, cp1.get("name with SPACES"));
        assertEquals((Integer)0xff12ab9f, cp1.get("remember HEX works, too"));
        assertEquals((Integer)0x11335577, cp1.get("hex SUPPORTS alpha"));
        assertEquals((Integer)0xff098abd, cp1.get("space/is->needed"));
        assertEquals((Integer)0xff3399ff, cp1.get("WEB SHORTHAND"));
    }

}
