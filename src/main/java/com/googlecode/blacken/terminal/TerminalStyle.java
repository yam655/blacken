package com.googlecode.blacken.terminal;

public enum TerminalStyle {
    // Mapped to WEIGHT:
    STYLE_LIGHT, // WEIGHT_EXTRA_LIGHT
    // STYLE_LIGHT | STYLE_BOLD // WEIGHT_LIGHT
    // *NO* WEIGHT_DEMILIGHT
    // WEIGHT_REGULAR == default
    // *NO* WEIGHT_SEMIBOLD
    // STYLE_LIGHT | STYLE_HEAVY // WEIGHT_MEDIUM
    // *NO* WEIGHT_DEMIBOLD
    STYLE_BOLD, // WEIGHT_BOLD == Font.BOLD
    STYLE_HEAVY, // WEIGHT_HEAVY
    // *NO* WEIGHT_EXTRABOLD
    // STYLE_BOLD | STYLE_HEAVY // WEIGHT_ULTRABOLD
    // What is STYLE_BOLD | STYLE_HEAVY | STYLE_LIGHT ?
    
    // Mapped to WIDTH
    STYLE_NARROW, // WIDTH_CONDENSED
    // WIDTH_SEMI_CONDENSED
    // WIDTH_REGULAR == default
    // WIDTH_SEMI_EXTENDED
    STYLE_WIDE, // WIDTH_EXTENDED
    // What is STYLE_NARROW | STYLE_WIDE ?
    
    // Mapped to POSTURE
    STYLE_ITALIC, // POSTURE_OBLIQUE
    
    // Mapped to SUPERSCRIPT (possibly *unclean* mapping)
    STYLE_SUPERSCRIPT, // SUPERSCRIPT_SUPER
    STYLE_SUBSCRIPT, // SUPERSCRIPT_SUB

    // Mapped to CHAR_REPLACEMENT
    STYLE_REPLACEMENT, // glyph references Shape or Image
    
    // Mapped to UNDERLINE
    STYLE_UNDERLINE, // UNDERLINE_ON
    
    // Mapped to STRIKETHROUGH
    STYLE_STRIKETHROUGH, // STRIKETHROUGH_ON
    
    // Mapped to SWAP_COLORS
    STYLE_REVERSE, // SWAP_COLORS_ON
        
    // Handled by adding alpha to the foreground color
    STYLE_DIM,
    // Handled by directly modifying the glyph
    STYLE_INVISIBLE
}
