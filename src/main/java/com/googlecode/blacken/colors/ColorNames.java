package com.googlecode.blacken.colors;


/**
 *
 * @author Steven Black
 */
public final class ColorNames {
    /**
     * HTML 3/4 official color names (from VGA color palette)
     * Described on http://en.wikipedia.org/wiki/Web_colors
     * This includes the "grey" spelling, which is not in the HTML standard.
     * This includes the 'orange' defined in CSS 2.1
     */
    static private String[] HtmlColorsDef = {
                                             "black -> #000000",
                                             "maroon -> #800000",
                                             "green -> #008000",
                                             "olive -> #808000",
                                             "navy -> #000080",
                                             "purple -> #800080",
                                             "teal -> #008080",
                                             "silver -> #c0c0c0",
                                             "gray / grey -> #808080",
                                             "red -> #ff0000",
                                             "lime -> #00ff00",
                                             "yellow -> #ffff00",
                                             "blue -> #0000ff",
                                             "fuchsia / magenta -> #ff00ff",
                                             "aqua / cyan -> #00ffff",
                                             "white -> #ffffff",
                                             "orange -> #ffa500",
    };

    /**
     * from http://www.w3schools.com/HTML/html_colornames.asp -- 
     * <p>Same values as svg_colors, except for 'MediumPurple' and 
     * 'PaleVioletRed'
     * <p>Note: These colors are in <i>no</i> standard -- unlike the SVG colors.
     */
    static private String[] WebColorsDef = {
                                            "AliceBlue -> #f0f8ff",
                                            "AntiqueWhite -> #faebd7",
                                            "Aqua / Cyan -> #00ffff",
                                            "Aquamarine -> #7fffd4",
                                            "Azure -> #f0ffff",
                                            "Beige -> #f5f5dc",
                                            "Bisque -> #ffe4c4",
                                            "Black -> #000000",
                                            "BlanchedAlmond -> #ffebcd",
                                            "Blue -> #0000ff",
                                            "BlueViolet -> #8a2be2",
                                            "Brown -> #a52a2a",
                                            "BurlyWood -> #deb887",
                                            "CadetBlue -> #5f9ea0",
                                            "Chartreuse -> #7fff00",
                                            "Chocolate -> #d2691e",
                                            "Coral -> #ff7f50",
                                            "CornflowerBlue -> #6495ed",
                                            "Cornsilk -> #fff8dc",
                                            "Crimson -> #dc143c",
                                            "DarkBlue -> #00008b",
                                            "DarkCyan -> #008b8b",
                                            "DarkGoldenrod / DarkGoldenRod -> #b8860b",
                                            "DarkGray / DarkGrey -> #a9a9a9",
                                            "DarkGreen -> #006400",
                                            "DarkKhaki -> #bdb76b",
                                            "DarkMagenta -> #8b008b",
                                            "DarkOliveGreen -> #556b2f",
                                            "DarkOrange -> #ff8c00",
                                            "DarkOrchid -> #9932cc",
                                            "DarkRed -> #8b0000",
                                            "DarkSalmon -> #e9967a",
                                            "DarkSeaGreen -> #8fbc8f",
                                            "DarkSlateBlue -> #483d8b",
                                            "DarkSlateGray / DarkSlateGrey -> #2f4f4f",
                                            "DarkTurquoise -> #00ced1",
                                            "DarkViolet -> #9400d3",
                                            "Deeppink -> #ff1493",
                                            "Deepskyblue -> #00bfff",
                                            "DimGray / DimGrey -> #696969",
                                            "DodgerBlue -> #1e90ff",
                                            "Firebrick / FireBrick -> #b22222",
                                            "FloralWhite -> #fffaf0",
                                            "ForestGreen -> #228b22",
                                            "Fuchsia -> #ff00ff",
                                            "Gainsboro -> #dcdcdc",
                                            "GhostWhite -> #f8f8ff",
                                            "Gold -> #ffd700",
                                            "Goldenrod / GoldenRod -> #daa520",
                                            "Gray / Grey -> #808080",
                                            "Green -> #008000",
                                            "GreenYellow -> #adff2f",
                                            "Honeydew / HoneyDew -> #f0fff0",
                                            "HotPink -> #ff69b4",
                                            "IndianRed -> #cd5c5c",
                                            "Indigo -> #4b0082",
                                            "Ivory -> #fffff0",
                                            "Khaki -> #f0e68c",
                                            "Lavender -> #e6e6fa",
                                            "LavenderBlush -> #fff0f5",
                                            "LawnGreen -> #7cfc00",
                                            "LemonChiffon -> #fffacd",
                                            "LightBlue -> #add8e6",
                                            "LightCoral -> #f08080",
                                            "LightCyan -> #e0ffff",
                                            "LightGoldenrodYellow / LightGoldenRodYellow -> #fafad2",
                                            "LightGray / LightGrey -> #d3d3d3",
                                            "LightGreen -> #90ee90",
                                            "LightPink -> #ffb6c1",
                                            "LightSalmon -> #ffa07a",
                                            "LightSeaGreen -> #20b2aa",
                                            "LightSkyBlue -> #87cefa",
                                            "LightSlateGray / LightSlateGrey -> #778899",
                                            "LightSteelBlue -> #b0c4de",
                                            "LightYellow -> #ffffe0",
                                            "Lime -> #00ff00",
                                            "LimeGreen -> #32cd32",
                                            "Linen -> #faf0e6",
                                            "Magenta -> #ff00ff",
                                            "Maroon -> #800000",
                                            "MediumAquaMarine -> #66cdaa",
                                            "MediumBlue -> #0000cd",
                                            "MediumOrchid -> #ba55d3",
                                            "MediumPurple -> #9370d8",
                                            "MediumSeagreen -> #3cb371",
                                            "MediumSlateBlue -> #7b68ee",
                                            "MediumSpringGreen -> #00fa9a",
                                            "MediumTurquoise -> #48d1cc",
                                            "MediumVioletRed -> #c71585",
                                            "MidnightBlue -> #191970",
                                            "MintCream -> #f5fffa",
                                            "MistyRose -> #ffe4e1",
                                            "Moccasin -> #ffe4b5",
                                            "NavajoWhite -> #ffdead",
                                            "Navy -> #000080",
                                            "OldLace -> #fdf5e6",
                                            "Olive -> #808000",
                                            "OliveDrab -> #6b8e23",
                                            "Orange -> #ffa500",
                                            "OrangeRed -> #ff4500",
                                            "Orchid -> #da70d6",
                                            "PaleGoldenrod / PaleGoldenRod -> #eee8aa",
                                            "PaleGreen -> #98fb98",
                                            "PaleTurquoise -> #afeeee",
                                            "PaleVioletRed -> #d87093",
                                            "PapayaWhip -> #ffefd5",
                                            "PeachPuff -> #ffdab9",
                                            "Peru -> #cd853f",
                                            "Pink -> #ffc0cb",
                                            "Plum -> #dda0dd",
                                            "PowderBlue -> #b0e0e6",
                                            "Purple -> #800080",
                                            "Red -> #ff0000",
                                            "RosyBrown -> #bc8f8f",
                                            "RoyalBlue -> #4169e1",
                                            "SaddleBrown -> #8b4513",
                                            "Salmon -> #fa8072",
                                            "SandyBrown -> #f4a460",
                                            "SeaGreen -> #2e8b57",
                                            "SeaShell / Seashell -> #fff5ee",
                                            "Sienna -> #a0522d",
                                            "Silver -> #c0c0c0",
                                            "SkyBlue -> #87ceeb",
                                            "SlateBlue -> #6a5acd",
                                            "SlateGray / SlateGrey -> #708090",
                                            "Snow -> #fffafa",
                                            "SpringGreen -> #00ff7f",
                                            "SteelBlue -> #4682b4",
                                            "Tan -> #d2b48c",
                                            "Teal -> #008080",
                                            "Thistle -> #d8bfd8",
                                            "Tomato -> #ff6347",
                                            "Turquoise -> #40e0d0",
                                            "Violet -> #ee82ee",
                                            "Wheat -> #f5deb3",
                                            "White -> #ffffff",
                                            "WhiteSmoke -> #f5f5f5",
                                            "Yellow -> #ffff00",
                                            "YellowGreen -> #9acd32",
    };

    /**
     * from http://www.w3.org/TR/SVG/types.html
     *  Same values as html_colors, except for 'mediumpurple' and 'palevioletred'
     * These color names and values are part of the SVG standard.
     */
    static private String[] SvgColorsDef = {
                                            "AliceBlue -> #f0f8ff",
                                            "AntiqueWhite -> #faebd7",
                                            "Aqua / Cyan -> #00ffff",
                                            "Aquamarine -> #7fffd4",
                                            "Azure -> #f0ffff",
                                            "Beige -> #f5f5dc",
                                            "Bisque -> #ffe4c4",
                                            "Black -> #000000",
                                            "BlanchedAlmond -> #ffebcd",
                                            "Blue -> #0000ff",
                                            "BlueViolet -> #8a2be2",
                                            "Brown -> #a52a2a",
                                            "BurlyWood -> #deb887",
                                            "CadetBlue -> #5f9ea0",
                                            "Chartreuse -> #7fff00",
                                            "Chocolate -> #d2691e",
                                            "Coral -> #ff7f50",
                                            "CornflowerBlue -> #6495ed",
                                            "Cornsilk -> #fff8dc",
                                            "Crimson -> #dc143c",
                                            "DarkBlue -> #00008b",
                                            "DarkCyan -> #008b8b",
                                            "DarkGoldenrod / DarkGoldenRod -> #b8860b",
                                            "DarkGray / DarkGrey -> #a9a9a9",
                                            "DarkGreen -> #006400",
                                            "DarkKhaki -> #bdb76b",
                                            "DarkMagenta -> #8b008b",
                                            "DarkOliveGreen -> #556b2f",
                                            "DarkOrange -> #ff8c00",
                                            "DarkOrchid -> #9932cc",
                                            "DarkRed -> #8b0000",
                                            "DarkSalmon -> #e9967a",
                                            "DarkSeaGreen -> #8fbc8f",
                                            "DarkSlateBlue -> #483d8b",
                                            "DarkSlateGray / DarkSlateGrey -> #2f4f4f",
                                            "DarkTurquoise -> #00ced1",
                                            "DarkViolet -> #9400d3",
                                            "Deeppink -> #ff1493",
                                            "Deepskyblue -> #00bfff",
                                            "DimGray / DimGrey -> #696969",
                                            "DodgerBlue -> #1e90ff",
                                            "Firebrick / FireBrick -> #b22222",
                                            "FloralWhite -> #fffaf0",
                                            "ForestGreen -> #228b22",
                                            "Fuchsia -> #ff00ff",
                                            "Gainsboro -> #dcdcdc",
                                            "GhostWhite -> #f8f8ff",
                                            "Gold -> #ffd700",
                                            "Goldenrod / GoldenRod -> #daa520",
                                            "Gray / Grey -> #808080",
                                            "Green -> #008000",
                                            "GreenYellow -> #adff2f",
                                            "Honeydew / HoneyDew -> #f0fff0",
                                            "HotPink -> #ff69b4",
                                            "IndianRed -> #cd5c5c",
                                            "Indigo -> #4b0082",
                                            "Ivory -> #fffff0",
                                            "Khaki -> #f0e68c",
                                            "Lavender -> #e6e6fa",
                                            "LavenderBlush -> #fff0f5",
                                            "LawnGreen -> #7cfc00",
                                            "LemonChiffon -> #fffacd",
                                            "LightBlue -> #add8e6",
                                            "LightCoral -> #f08080",
                                            "LightCyan -> #e0ffff",
                                            "LightGoldenrodYellow / LightGoldenRodYellow -> #fafad2",
                                            "LightGray / LightGrey -> #d3d3d3",
                                            "LightGreen -> #90ee90",
                                            "LightPink -> #ffb6c1",
                                            "LightSalmon -> #ffa07a",
                                            "LightSeaGreen -> #20b2aa",
                                            "LightSkyBlue -> #87cefa",
                                            "LightSlateGray / LightSlateGrey -> #778899",
                                            "LightSteelBlue -> #b0c4de",
                                            "LightYellow -> #ffffe0",
                                            "Lime -> #00ff00",
                                            "LimeGreen -> #32cd32",
                                            "Linen -> #faf0e6",
                                            "Magenta -> #ff00ff",
                                            "Maroon -> #800000",
                                            "MediumAquaMarine -> #66cdaa",
                                            "MediumBlue -> #0000cd",
                                            "MediumOrchid -> #ba55d3",
                                            "MediumPurple -> #9370db",
                                            "MediumSeagreen -> #3cb371",
                                            "MediumSlateBlue -> #7b68ee",
                                            "MediumSpringGreen -> #00fa9a",
                                            "MediumTurquoise -> #48d1cc",
                                            "MediumVioletRed -> #c71585",
                                            "MidnightBlue -> #191970",
                                            "MintCream -> #f5fffa",
                                            "MistyRose -> #ffe4e1",
                                            "Moccasin -> #ffe4b5",
                                            "NavajoWhite -> #ffdead",
                                            "Navy -> #000080",
                                            "OldLace -> #fdf5e6",
                                            "Olive -> #808000",
                                            "OliveDrab -> #6b8e23",
                                            "Orange -> #ffa500",
                                            "OrangeRed -> #ff4500",
                                            "Orchid -> #da70d6",
                                            "PaleGoldenrod / PaleGoldenRod -> #eee8aa",
                                            "PaleGreen -> #98fb98",
                                            "PaleTurquoise -> #afeeee",
                                            "PaleVioletRed -> #db7093",
                                            "PapayaWhip -> #ffefd5",
                                            "PeachPuff -> #ffdab9",
                                            "Peru -> #cd853f",
                                            "Pink -> #ffc0cb",
                                            "Plum -> #dda0dd",
                                            "PowderBlue -> #b0e0e6",
                                            "Purple -> #800080",
                                            "Red -> #ff0000",
                                            "RosyBrown -> #bc8f8f",
                                            "RoyalBlue -> #4169e1",
                                            "SaddleBrown -> #8b4513",
                                            "Salmon -> #fa8072",
                                            "SandyBrown -> #f4a460",
                                            "SeaGreen -> #2e8b57",
                                            "SeaShell / Seashell -> #fff5ee",
                                            "Sienna -> #a0522d",
                                            "Silver -> #c0c0c0",
                                            "SkyBlue -> #87ceeb",
                                            "SlateBlue -> #6a5acd",
                                            "SlateGray / SlateGrey -> #708090",
                                            "Snow -> #fffafa",
                                            "SpringGreen -> #00ff7f",
                                            "SteelBlue -> #4682b4",
                                            "Tan -> #d2b48c",
                                            "Teal -> #008080",
                                            "Thistle -> #d8bfd8",
                                            "Tomato -> #ff6347",
                                            "Turquoise -> #40e0d0",
                                            "Violet -> #ee82ee",
                                            "Wheat -> #f5deb3",
                                            "White -> #ffffff",
                                            "WhiteSmoke -> #f5f5f5",
                                            "Yellow -> #ffff00",
                                            "YellowGreen -> #9acd32",
    };

    /**
     * From xterm, these are defined as:
     * <ol>
     *  <li>black
     *  <li>red3
     *  <li>green3
     *  <li>yellow3
     *  <li>blue2
     *  <li>magenta3
     *  <li>cyan3
     *  <li>gray90
     *  <li>gray50
     *  <li>red
     *  <li>green
     *  <li>yellow
     *  <li>rgb:0x5c5cff
     *  <li>magenta
     *  <li>cyan
     *  <li>white
     * </ol>
     */
    static private int[] Xterm16ColorDef = 
    {
     0x000000, 0xcd0000, 0x00cd00, 0xcdcd00,
     0x0000ee, 0xcd00cd, 0x00cdcd, 0xe5e5e5,
     0x7f7f7f, 0xff0000, 0x00ff00, 0xffff00,
     0x5c5cff, 0xff00ff, 0x00ffff, 0xffffff,
    };

    /**
     * From xterm, the first 16 colors are all the same.
     * However the later colors are computed by a formula.
     * (They define a color cube, then a gradiant.)
     */
    static private int[] Xterm88ColorDef = 
    {
     0x000000, 0xcd0000, 0x00cd00, 0xcdcd00, 0x0000ee, 0xcd00cd, 0x00cdcd,
     0xe5e5e5, 0x7f7f7f, 0xff0000, 0x00ff00, 0xffff00, 0x5c5cff, 0xff00ff,
     0x00ffff, 0xffffff, 0x000000, 0x00008b, 0x0000cd, 0x0000ff, 0x008b00,
     0x008b8b, 0x008bcd, 0x008bff, 0x00cd00, 0x00cd8b, 0x00cdcd, 0x00cdff,
     0x00ff00, 0x00ff8b, 0x00ffcd, 0x00ffff, 0x8b0000, 0x8b008b, 0x8b00cd,
     0x8b00ff, 0x8b8b00, 0x8b8b8b, 0x8b8bcd, 0x8b8bff, 0x8bcd00, 0x8bcd8b,
     0x8bcdcd, 0x8bcdff, 0x8bff00, 0x8bff8b, 0x8bffcd, 0x8bffff, 0xcd0000,
     0xcd008b, 0xcd00cd, 0xcd00ff, 0xcd8b00, 0xcd8b8b, 0xcd8bcd, 0xcd8bff,
     0xcdcd00, 0xcdcd8b, 0xcdcdcd, 0xcdcdff, 0xcdff00, 0xcdff8b, 0xcdffcd,
     0xcdffff, 0xff0000, 0xff008b, 0xff00cd, 0xff00ff, 0xff8b00, 0xff8b8b,
     0xff8bcd, 0xff8bff, 0xffcd00, 0xffcd8b, 0xffcdcd, 0xffcdff, 0xffff00,
     0xffff8b, 0xffffcd, 0xffffff, 0x2e2e2e, 0x5c5c5c, 0x737373, 0x8b8b8b,
     0xa2a2a2, 0xb9b9b9, 0xd0d0d0, 0xe7e7e7,
    };

    /**
     * From xterm, the first 16 colors are all the same.
     * However the later colors are computed by a formula.
     * (They define a color cube, then a gradiant.)
     */
    static private int[] Xterm256ColorDef = 
    {
     0x000000, 0xcd0000, 0x00cd00, 0xcdcd00, 0x0000ee, 0xcd00cd, 0x00cdcd,
     0xe5e5e5, 0x7f7f7f, 0xff0000, 0x00ff00, 0xffff00, 0x5c5cff, 0xff00ff,
     0x00ffff, 0xffffff, 0x000000, 0x00005f, 0x000087, 0x0000af, 0x0000d7,
     0x0000ff, 0x005f00, 0x005f5f, 0x005f87, 0x005faf, 0x005fd7, 0x005fff,
     0x008700, 0x00875f, 0x008787, 0x0087af, 0x0087d7, 0x0087ff, 0x00af00,
     0x00af5f, 0x00af87, 0x00afaf, 0x00afd7, 0x00afff, 0x00d700, 0x00d75f,
     0x00d787, 0x00d7af, 0x00d7d7, 0x00d7ff, 0x00ff00, 0x00ff5f, 0x00ff87,
     0x00ffaf, 0x00ffd7, 0x00ffff, 0x5f0000, 0x5f005f, 0x5f0087, 0x5f00af,
     0x5f00d7, 0x5f00ff, 0x5f5f00, 0x5f5f5f, 0x5f5f87, 0x5f5faf, 0x5f5fd7,
     0x5f5fff, 0x5f8700, 0x5f875f, 0x5f8787, 0x5f87af, 0x5f87d7, 0x5f87ff,
     0x5faf00, 0x5faf5f, 0x5faf87, 0x5fafaf, 0x5fafd7, 0x5fafff, 0x5fd700,
     0x5fd75f, 0x5fd787, 0x5fd7af, 0x5fd7d7, 0x5fd7ff, 0x5fff00, 0x5fff5f,
     0x5fff87, 0x5fffaf, 0x5fffd7, 0x5fffff, 0x870000, 0x87005f, 0x870087,
     0x8700af, 0x8700d7, 0x8700ff, 0x875f00, 0x875f5f, 0x875f87, 0x875faf,
     0x875fd7, 0x875fff, 0x878700, 0x87875f, 0x878787, 0x8787af, 0x8787d7,
     0x8787ff, 0x87af00, 0x87af5f, 0x87af87, 0x87afaf, 0x87afd7, 0x87afff,
     0x87d700, 0x87d75f, 0x87d787, 0x87d7af, 0x87d7d7, 0x87d7ff, 0x87ff00,
     0x87ff5f, 0x87ff87, 0x87ffaf, 0x87ffd7, 0x87ffff, 0xaf0000, 0xaf005f,
     0xaf0087, 0xaf00af, 0xaf00d7, 0xaf00ff, 0xaf5f00, 0xaf5f5f, 0xaf5f87,
     0xaf5faf, 0xaf5fd7, 0xaf5fff, 0xaf8700, 0xaf875f, 0xaf8787, 0xaf87af,
     0xaf87d7, 0xaf87ff, 0xafaf00, 0xafaf5f, 0xafaf87, 0xafafaf, 0xafafd7,
     0xafafff, 0xafd700, 0xafd75f, 0xafd787, 0xafd7af, 0xafd7d7, 0xafd7ff,
     0xafff00, 0xafff5f, 0xafff87, 0xafffaf, 0xafffd7, 0xafffff, 0xd70000,
     0xd7005f, 0xd70087, 0xd700af, 0xd700d7, 0xd700ff, 0xd75f00, 0xd75f5f,
     0xd75f87, 0xd75faf, 0xd75fd7, 0xd75fff, 0xd78700, 0xd7875f, 0xd78787,
     0xd787af, 0xd787d7, 0xd787ff, 0xd7af00, 0xd7af5f, 0xd7af87, 0xd7afaf,
     0xd7afd7, 0xd7afff, 0xd7d700, 0xd7d75f, 0xd7d787, 0xd7d7af, 0xd7d7d7,
     0xd7d7ff, 0xd7ff00, 0xd7ff5f, 0xd7ff87, 0xd7ffaf, 0xd7ffd7, 0xd7ffff,
     0xff0000, 0xff005f, 0xff0087, 0xff00af, 0xff00d7, 0xff00ff, 0xff5f00,
     0xff5f5f, 0xff5f87, 0xff5faf, 0xff5fd7, 0xff5fff, 0xff8700, 0xff875f,
     0xff8787, 0xff87af, 0xff87d7, 0xff87ff, 0xffaf00, 0xffaf5f, 0xffaf87,
     0xffafaf, 0xffafd7, 0xffafff, 0xffd700, 0xffd75f, 0xffd787, 0xffd7af,
     0xffd7d7, 0xffd7ff, 0xffff00, 0xffff5f, 0xffff87, 0xffffaf, 0xffffd7,
     0xffffff, 0x080808, 0x121212, 0x1c1c1c, 0x262626, 0x303030, 0x3a3a3a,
     0x444444, 0x4e4e4e, 0x585858, 0x626262, 0x6c6c6c, 0x767676, 0x808080,
     0x8a8a8a, 0x949494, 0x9e9e9e, 0xa8a8a8, 0xb2b2b2, 0xbcbcbc, 0xc6c6c6,
     0xd0d0d0, 0xdadada, 0xe4e4e4, 0xeeeeee,
    };


    static private String[] standard16def = 
    {
     "COLOR_BLACK / black -> #000000",
     "COLOR_RED / dark red / maroon -> #800000",
     "COLOR_GREEN / dark green / green -> #008000",     	
     "COLOR_YELLOW / brown / olive -> #808000",     	
     "COLOR_BLUE / dark blue / navy -> #000080",     	
     "COLOR_MAGENTA / dark magenta / purple -> #800080",     	
     "COLOR_CYAN / dark cyan / teal -> #008080",     	
     "COLOR_WHITE / light grey / light gray / silver -> #C0C0C0",
     "COLOR_BLACK|A_BOLD / dark grey / dark gray / grey / gray -> #808080",
     "COLOR_RED|A_BOLD / light red / red -> #FF0000",
     "COLOR_GREEN|A_BOLD / light green / lime -> #00FF00",
     "COLOR_YELLOW|A_BOLD / yellow -> #FFFF00", 
     "COLOR_BLUE|A_BOLD / light blue / blue -> #0000FF",
     "COLOR_MAGENTA|A_BOLD / light magenta / fuchsia / magenta -> #FF00FF",
     "COLOR_CYAN|A_BOLD / light cyan / aqua / cyan -> #00FFFF",
     "COLOR_WHITE|A_BOLD / white -> #FFFFFF",
    };

    /**
     * Add the xterm colors to a palette.
     * 
     * @param palette Palette to modify (null to create new palette)
     * @param count either 16, 88, or 256 indicating group of colors
     * @return true on success, false on failure
     * @throws NullPointerException
     */
    public static ColorPalette addXtermColors(ColorPalette palette, int count) {
        if (palette == null) {
            palette = new ColorPalette();
        }
        if (count <= 16) {
            palette.addAll(Xterm16ColorDef, false);
        } else if (count <= 88) {
            palette.addAll(Xterm88ColorDef, false);
        } else {
            palette.addAll(Xterm256ColorDef, false);
        }
        return palette;
    }

    /**
     * Add the SVG color names to a palette.
     * 
     * <p>The names overlap the names of the common cross-platform web color 
     * names, however two of the colors have slightly different values.
     * As this set follows a standard, it is preferred.</p>
     * 
     * @param palette Palette to modify (null to create new palette)
     * @see #addWebColors(ColorPalette)
     * @return Returns the palette argument (or the new palette)
     */
    public static ColorPalette addSvgColors(ColorPalette palette) {
        if(palette == null) palette = new ColorPalette(SvgColorsDef.length);
        palette.addMapping(SvgColorsDef);
        return palette;
    }

    /**
     * Add names for the common web color names to the palette.
     * 
     * <p>Currently the names overlap the names of SVG color names,
     * however two of the colors have slightly different values.</p>
     * 
     * <p>Since these colors do not conform to a standard, this list of
     * colors could be expanded to include non-standard color names.</p>
     * 
     * @param palette Palette to modify (null to create new palette)
     * @return the modified ColorPalette object
     */
    public static ColorPalette addWebColors(ColorPalette palette) {
        if(palette == null) palette = new ColorPalette(WebColorsDef.length);
        palette.addMapping(WebColorsDef);
        return palette;
    }
    /**
     * Load the 17 color HTML4 named palette. 
     * 
     * <p>The first 16 are the standard HTML names with their normal alternate 
     * names. The 17th is the orange included with CSS2.1. These are the only 
     * standard HTML4 color names.</p>
     * 
     * @param palette Palette to modify (null to create new palette)
     * @return the new Color Palette
     */
    public static ColorPalette addHtmlPalette(ColorPalette palette) {
        if (palette == null) palette = new ColorPalette(HtmlColorsDef.length);
        palette.addMapping(HtmlColorsDef);
        return palette;
    }

    /**
     * The standard 16 colors. We combine a number of name options:
     * <ul>
     * 	<li>HTML 3/4 colors - including alternate names
     * 	<li>old fashioned VGA names - 8 color names with light/dark variations
     * 	<li>CURSES-throwback - COLOR_-prefixed; bright with |A_BOLD suffixed  
     * </ul>
     * 
     * @param palette Palette to modify (null to create new palette)
     * @return the new Color Palette
     */
    public static ColorPalette addStandard16Palette(ColorPalette palette) {
        if (palette == null) palette = new ColorPalette(standard16def.length);
        palette.addMapping(standard16def);
        return palette;
    }
}
