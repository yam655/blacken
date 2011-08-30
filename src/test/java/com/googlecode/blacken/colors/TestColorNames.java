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
