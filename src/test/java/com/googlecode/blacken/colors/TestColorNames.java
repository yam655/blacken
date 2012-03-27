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

import org.junit.Test;

/**
 * Test the color names.
 * 
 * @author yam655
 */
public class TestColorNames {

    /**
     * Add the xterm colors to a palette.
     */
    @Test
    public void addXtermColors_16() {
        ColorPalette p = new ColorPalette();
        p.addAll(ColorNames.XTERM_16_COLORS, false);
        assertEquals(p.size(), 16);
    }

    /**
     * Add the xterm colors to a palette.
     */
    @Test
    public void addXtermColors_88() {
        ColorPalette p = new ColorPalette();
        p.addAll(ColorNames.XTERM_88_COLORS, false);
        assertEquals(p.size(), 88);
    }

    /**
     * Add the xterm colors to a palette.
     */
    @Test
    public void addXtermColors_256() {
        ColorPalette p = new ColorPalette();
        p.addAll(ColorNames.XTERM_256_COLORS, false);
        assertEquals(p.size(), 256);
    }

    /**
     * Add the SVG color names to a palette.
     */
    @Test
    public void addSvgColors() {
        ColorPalette palette;
        palette = new ColorPalette(ColorNames.SVG_COLORS.length);
        palette.addMapping(ColorNames.SVG_COLORS);
        assertEquals(palette.get("Grey"),  //$NON-NLS-1$
                     palette.get("Gray")); //$NON-NLS-1$
    }
    
    /**
     * Verify appending works
     */
    @Test
    public void svgColorsAppend() {
        ColorPalette svg;
        svg = new ColorPalette(ColorNames.SVG_COLORS.length);
        svg.addMapping(ColorNames.SVG_COLORS);
        int origSize = svg.size();
        for (int i = 0; i < origSize; i ++) {
            svg.set(i, ~svg.get(i));
        }
        assertEquals("overwriting index entries changed size",  //$NON-NLS-1$
                     origSize, svg.size());
        svg.addMapping(ColorNames.SVG_COLORS);
        assertEquals("overwriten palette unexpected size",  //$NON-NLS-1$
                     origSize * 2, svg.size());
    }
    
    /**
     * Verify overwriting works
     */
    @Test
    public void svgColorsOverwrite() {
        ColorPalette svg;
        svg = new ColorPalette(ColorNames.SVG_COLORS.length);
        svg.addMapping(ColorNames.SVG_COLORS);
        int origSize = svg.size();
        for (int i = 0; i < origSize; i ++) {
            svg.set(i, ~svg.get(i));
        }
        assertEquals("overwriting index entries changed size",  //$NON-NLS-1$
                     origSize, svg.size());
        svg.putMapping(ColorNames.SVG_COLORS);
        assertEquals("overwriten palette changed size",  //$NON-NLS-1$
                     origSize, svg.size());
    }

    /**
     * Load the 17 color HTML4 named palette. 
     */
    @Test
    public void addHtmlPalette() {
        ColorPalette palette = new ColorPalette();
        palette.addMapping(ColorNames.HTML_COLORS);
        assertEquals(17, palette.size());
        assertEquals(palette.get("Grey"),  //$NON-NLS-1$
                     palette.get("Gray")); //$NON-NLS-1$
    }

    /**
     * The standard 16 colors.
     */
    @Test
    public void addStandard16Palette() {
        ColorPalette palette = new ColorPalette();
        palette.addMapping(ColorNames.STANDARD_16_COLORS);
        assertEquals(16, palette.size());
        assertEquals(palette.get("grey"),  //$NON-NLS-1$
                     palette.get("gray")); //$NON-NLS-1$
        assertEquals(1, palette.indexOfKey("dark red")); //$NON-NLS-1$
    }

    /**
     * The CGA 16 color palette
     */
    @Test
    public void addCga16Palette() {
        ColorPalette palette = new ColorPalette();
        palette.addMapping(ColorNames.CGA_16_COLORS);
        assertEquals(16, palette.size());
        assertEquals(palette.get("grey"),  //$NON-NLS-1$
                     palette.get("gray")); //$NON-NLS-1$
        assertEquals(1, palette.indexOfKey("dark blue")); //$NON-NLS-1$
    }

}
