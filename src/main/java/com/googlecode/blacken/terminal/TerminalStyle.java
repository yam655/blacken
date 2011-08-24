/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
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
package com.googlecode.blacken.terminal;

/**
 * The basic terminal styles
 * 
 * @author yam655
 */
public enum TerminalStyle {
    // Mapped to WEIGHT:
    /**
     * Maps to WEIGHT_EXTRA_LIGHT
     */
    STYLE_LIGHT,
    // STYLE_LIGHT | STYLE_BOLD // WEIGHT_LIGHT
    // *NO* WEIGHT_DEMILIGHT
    // WEIGHT_REGULAR == default
    // *NO* WEIGHT_SEMIBOLD
    // STYLE_LIGHT | STYLE_HEAVY // WEIGHT_MEDIUM
    // *NO* WEIGHT_DEMIBOLD
    /**
     * WEIGHT_BOLD == Font.BOLD
     */
    STYLE_BOLD,
    /**
     * WEIGHT_HEAVY
     */
    STYLE_HEAVY, 
    // *NO* WEIGHT_EXTRABOLD
    // STYLE_BOLD | STYLE_HEAVY // WEIGHT_ULTRABOLD
    // What is STYLE_BOLD | STYLE_HEAVY | STYLE_LIGHT ?
    
    // Mapped to WIDTH
    /**
     * WIDTH_CONDENSED
     */
    STYLE_NARROW,
    // WIDTH_SEMI_CONDENSED
    // WIDTH_REGULAR == default
    // WIDTH_SEMI_EXTENDED
    /**
     * WIDTH_EXTENDED
     */
    STYLE_WIDE,
    // What is STYLE_NARROW | STYLE_WIDE ?
    
    /**
     * Mapped to POSTURE: POSTURE_OBLIQUE
     */
    STYLE_ITALIC,
    
    // Mapped to SUPERSCRIPT (possibly *unclean* mapping)
    /**
     * SUPERSCRIPT_SUPER
     */
    STYLE_SUPERSCRIPT,
    /**
     * SUPERSCRIPT_SUB
     */
    STYLE_SUBSCRIPT,

    /**
     * Mapped to CHAR_REPLACEMENT: glyph references Shape or Image 
     */
    STYLE_REPLACEMENT,
    
    /**
     * Mapped to UNDERLINE: UNDERLINE_ON
     */
    STYLE_UNDERLINE,
    
    /**
     * Mapped to STRIKETHROUGH: STRIKETHROUGH_ON
     */
    STYLE_STRIKETHROUGH,
    
    /**
     * Mapped to SWAP_COLORS: SWAP_COLORS_ON
     */
    STYLE_REVERSE,
        
    /**
     * Handled by adding alpha to the foreground color
     */
    STYLE_DIM,
    /**
     * Handled by directly modifying the glyph
     */
    STYLE_INVISIBLE
}
