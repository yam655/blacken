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
package com.googlecode.blacken.examples;

import java.util.EnumSet;
import java.util.HashMap;

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.*;

/**
 * Example program for the keyboard functions.
 * 
 * @author yam655
 *
 */
public class Boarder {
    private TerminalInterface term = null;
    private boolean quit = false;
    private ColorPalette palette;
        
    /**
     * Create a new instance, using an existing terminal and palette.
     * 
     * @param term terminal interface to use
     * @param palette color palette to use
     */
    public Boarder(TerminalInterface term, ColorPalette palette) {
        loadKeys();
        this.term = term;
        this.palette = palette;
    }

    /**
     * Show the help text.
     */
    public void showHelp() {
        /* XXX add this */
    }
    
    /**
     * Change the displayed key.
     * 
     * @param codepoint to press.
     */
    public void changeKey(Integer codepoint) {
        if (!keys.containsKey(codepoint)) {
            return;
        }
        KeyLocation loc = keys.get(codepoint); 
        int y = loc.y;
        int x = loc.x;
        String what = loc.name;
        TerminalCellLike tcell = term.get(y, x);
        if (tcell.getSequence().equals("\u0000") ||  //$NON-NLS-1$
                tcell.getSequence().equals(" ")) { //$NON-NLS-1$
            int back = 1;
            int fore = ColorHelper.makeVisible(this.palette.get(back));
            int i = 0;
            term.setCurBackground(back);
            term.setCurForeground(fore);
            term.mvputs(y, x, what);
            while (i < what.codePointCount(0, what.length())) {
                tcell.clearCellWalls();
                tcell.setBackground(back);
                tcell.setForeground(fore);
                tcell.setCellWalls(EnumSet.of(CellWalls.BOTTOM, CellWalls.TOP));
                if (i == 0) {
                    tcell.addCellWalls(CellWalls.LEFT);
                }
                if (i == what.codePointCount(0, what.length()) -1) {
                    tcell.addCellWalls(CellWalls.RIGHT);
                }
                tcell.setSequence(what.codePointAt(i));
                term.set(y, x, tcell);
                tcell = term.get(y, ++x);
                i++;
            }
        } else if (!loading){
            int back = tcell.getBackground() + 1;
            if (back >= palette.size()) back = 0;
            int fore = ColorHelper.makeVisible(this.palette.get(back));
            int i = 0;
            while (i < what.codePointCount(0, what.length())) {
                tcell.setBackground(back);
                tcell.setForeground(fore);
                term.set(y, x, tcell);
                tcell = term.get(y, ++x);
                i++;
            }
        }
    }
    class KeyLocation {
        public int y;
        public int x;
        public String name;
        KeyLocation(int y, int x, String name) {
            this.y = y;
            this.x = x;
            this.name = name;
        }
    }
    HashMap<Integer, KeyLocation> keys = null;
    private boolean loading;
    private void loadKeys() {
        keys = new HashMap<Integer, KeyLocation>();
        /*
ESC F1 F2 F3 F4 F5 F6 F7 F8 F9 F10 F11 F12   Ins  Del    Nm // ** --    #0
`~ 1! 2@ 3# 4$ 5% 6^ 7& 8* 9( 0) -_ =+ BkSp  Home End    77 88 99 ++    #2
Tab Qq Ww Ee Rr Tt Yy Uu Ii Oo Pp [{ ]} \|   PgUp PgDn   44 55 66       #4
Caps Aa Ss Dd Ff Gg Hh Jj Kk Ll ;: '" Enter       Up     11 22 33 En    #6
Shift Zz Xx Cc Vv Bb Nn Mm ,< .> /?           Lft Dn Rt  00    ..       #8
Ctrl Logo Alt     Space     Meta AltGr Menu                             #10
WindowEvent MouseEvent UnknownKey ResizeEvent                           #12
             yyy  xxx   ScrollLk   yyy  xxx              Hm Up PU       #14
                        NumLock                ScrlLk    Lf Ct Rt       #16
                        CapsLock               PrtScr    Ed Dn PD       #18
                        KanaLock                         Ins  Del
         */
        keys.put(BlackenKeys.WINDOW_EVENT, new KeyLocation(12, 0, "WindowEvent")); //$NON-NLS-1$
        keys.put(BlackenKeys.MOUSE_EVENT, new KeyLocation(12, 12, "MouseEvent")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_UNKNOWN, new KeyLocation(12, 23, "UnknownKey")); //$NON-NLS-1$
        keys.put(BlackenKeys.RESIZE_EVENT, new KeyLocation(12, 34, "ResizeEvent")); //$NON-NLS-1$

        keys.put(BlackenModifier.MODIFIER_KEY_CTRL.getAsCodepoint(), new KeyLocation(10, 0, "Ctrl")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_LOGO, new KeyLocation(10, 5, "Logo")); //$NON-NLS-1$
        keys.put(BlackenModifier.MODIFIER_KEY_ALT.getAsCodepoint(), new KeyLocation(10, 10, "Alt")); //$NON-NLS-1$
        keys.put(Character.codePointAt(" ", 0), new KeyLocation(10, 18, "Space")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(BlackenModifier.MODIFIER_KEY_META.getAsCodepoint(), new KeyLocation(10, 28, "Meta")); //$NON-NLS-1$
        keys.put(BlackenModifier.MODIFIER_KEY_ALTGR.getAsCodepoint(), new KeyLocation(10, 33, "AltGr")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_CONTEXT_MENU, new KeyLocation(10, 39, "Menu")); //$NON-NLS-1$

        keys.put(BlackenModifier.MODIFIER_KEY_SHIFT.getAsCodepoint(), new KeyLocation(8, 0, "Shift")); //$NON-NLS-1$
        keys.put(Character.codePointAt("Z", 0), new KeyLocation(8, 6, "Z")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("z", 0), new KeyLocation(8, 7, "z")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("X", 0), new KeyLocation(8, 9, "X")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("x", 0), new KeyLocation(8, 10, "x")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("C", 0), new KeyLocation(8, 12, "C")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("c", 0), new KeyLocation(8, 13, "c")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("V", 0), new KeyLocation(8, 15, "V")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("v", 0), new KeyLocation(8, 16, "v")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("B", 0), new KeyLocation(8, 18, "B")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("b", 0), new KeyLocation(8, 19, "b")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("N", 0), new KeyLocation(8, 21, "N")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("n", 0), new KeyLocation(8, 22, "n")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("M", 0), new KeyLocation(8, 24, "M")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("m", 0), new KeyLocation(8, 25, "m")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt(",", 0), new KeyLocation(8, 27, ",")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("<", 0), new KeyLocation(8, 28, "<")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt(".", 0), new KeyLocation(8, 30, ".")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt(">", 0), new KeyLocation(8, 31, ">")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("/", 0), new KeyLocation(8, 33, "/")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("?", 0), new KeyLocation(8, 34, "?")); //$NON-NLS-1$ //$NON-NLS-2$

        keys.put(BlackenKeys.KEY_CAPS_LOCK, new KeyLocation(6, 0, "Caps")); //$NON-NLS-1$
        keys.put(Character.codePointAt("A", 0), new KeyLocation(6, 5, "A")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("a", 0), new KeyLocation(6, 6, "a")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("S", 0), new KeyLocation(6, 8, "S")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("s", 0), new KeyLocation(6, 9, "s")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("D", 0), new KeyLocation(6, 11, "D")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("d", 0), new KeyLocation(6, 12, "d")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("F", 0), new KeyLocation(6, 14, "F")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("f", 0), new KeyLocation(6, 15, "f")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("G", 0), new KeyLocation(6, 17, "G")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("g", 0), new KeyLocation(6, 18, "g")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("H", 0), new KeyLocation(6, 20, "H")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("h", 0), new KeyLocation(6, 21, "h")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("J", 0), new KeyLocation(6, 23, "J")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("j", 0), new KeyLocation(6, 24, "j")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("K", 0), new KeyLocation(6, 26, "K")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("k", 0), new KeyLocation(6, 27, "k")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("L", 0), new KeyLocation(6, 29, "L")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("l", 0), new KeyLocation(6, 30, "l")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt(";", 0), new KeyLocation(6, 32, ";")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt(":", 0), new KeyLocation(6, 33, ":")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("'", 0), new KeyLocation(6, 35, "'")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("\"", 0), new KeyLocation(6, 36, "\"")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(BlackenKeys.KEY_ENTER, new KeyLocation(6, 38, "Enter")); //$NON-NLS-1$

        keys.put(BlackenKeys.KEY_TAB, new KeyLocation(4, 0, "Tab")); //$NON-NLS-1$
        keys.put(Character.codePointAt("Q", 0), new KeyLocation(4, 4, "Q")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("q", 0), new KeyLocation(4, 5, "q")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("W", 0), new KeyLocation(4, 7, "W")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("w", 0), new KeyLocation(4, 8, "w")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("E", 0), new KeyLocation(4, 10, "E")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("e", 0), new KeyLocation(4, 11, "e")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("R", 0), new KeyLocation(4, 13, "R")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("r", 0), new KeyLocation(4, 14, "r")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("T", 0), new KeyLocation(4, 16, "T")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("t", 0), new KeyLocation(4, 17, "t")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("Y", 0), new KeyLocation(4, 19, "Y")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("y", 0), new KeyLocation(4, 20, "y")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("U", 0), new KeyLocation(4, 22, "U")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("u", 0), new KeyLocation(4, 23, "u")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("I", 0), new KeyLocation(4, 25, "I")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("i", 0), new KeyLocation(4, 26, "i")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("O", 0), new KeyLocation(4, 28, "O")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("o", 0), new KeyLocation(4, 29, "o")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("P", 0), new KeyLocation(4, 31, "P")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("p", 0), new KeyLocation(4, 32, "p")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("[", 0), new KeyLocation(4, 34, "[")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("{", 0), new KeyLocation(4, 35, "}")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("]", 0), new KeyLocation(4, 37, "]")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("}", 0), new KeyLocation(4, 38, "}")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("\\", 0), new KeyLocation(4, 40, "\\")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("|", 0), new KeyLocation(4, 41, "|")); //$NON-NLS-1$ //$NON-NLS-2$
        
        keys.put(Character.codePointAt("`", 0), new KeyLocation(2, 0, "`")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("~", 0), new KeyLocation(2, 1, "~")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("1", 0), new KeyLocation(2, 3, "1")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("!", 0), new KeyLocation(2, 4, "!")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("2", 0), new KeyLocation(2, 6, "2")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("@", 0), new KeyLocation(2, 7, "@")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("3", 0), new KeyLocation(2, 9, "3")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("#", 0), new KeyLocation(2, 10, "#")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("4", 0), new KeyLocation(2, 12, "4")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("$", 0), new KeyLocation(2, 13, "$")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("5", 0), new KeyLocation(2, 15, "5")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("%", 0), new KeyLocation(2, 16, "%")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("6", 0), new KeyLocation(2, 18, "6")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("^", 0), new KeyLocation(2, 19, "^")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("7", 0), new KeyLocation(2, 21, "7")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("&", 0), new KeyLocation(2, 22, "&")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("8", 0), new KeyLocation(2, 24, "8")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("*", 0), new KeyLocation(2, 25, "*")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("9", 0), new KeyLocation(2, 27, "9")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("(", 0), new KeyLocation(2, 28, "(")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("0", 0), new KeyLocation(2, 30, "0")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt(")", 0), new KeyLocation(2, 31, ")")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("-", 0), new KeyLocation(2, 33, "-")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("_", 0), new KeyLocation(2, 34, "_")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("=", 0), new KeyLocation(2, 36, "=")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(Character.codePointAt("+", 0), new KeyLocation(2, 37, "+")); //$NON-NLS-1$ //$NON-NLS-2$
        keys.put(BlackenKeys.KEY_BACKSPACE, new KeyLocation(2, 39, "BkSp")); //$NON-NLS-1$

        keys.put(BlackenKeys.KEY_ESCAPE, new KeyLocation(0, 0, "ESC")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F01, new KeyLocation(0, 4, "F1")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F02, new KeyLocation(0, 7, "F2")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F03, new KeyLocation(0, 10, "F3")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F04, new KeyLocation(0, 13, "F4")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F05, new KeyLocation(0, 16, "F5")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F06, new KeyLocation(0, 19, "F6")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F07, new KeyLocation(0, 22, "F7")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F08, new KeyLocation(0, 25, "F8")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F09, new KeyLocation(0, 28, "F9")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F10, new KeyLocation(0, 31, "F10")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F11, new KeyLocation(0, 35, "F11")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_F12, new KeyLocation(0, 39, "F12")); //$NON-NLS-1$

        // keys.put(BlackenKeys.KEY_NP_DIVIDE, new KeyLocation(12, 61, "//"));
        // keys.put(BlackenKeys.KEY_NP_MULTIPLY, new KeyLocation(12, 64, "**"));
        // keys.put(BlackenKeys.KEY_NP_SUBTRACT, new KeyLocation(12, 67, "--"));
        keys.put(BlackenKeys.KEY_KP_HOME, new KeyLocation(14, 58, "Hm")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_KP_UP, new KeyLocation(14, 61, "Up")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_KP_PAGE_UP, new KeyLocation(14, 64, "PU")); //$NON-NLS-1$
        // keys.put(BlackenKeys.KEY_NP_ADD, new KeyLocation(14, 67, "++"));
        keys.put(BlackenKeys.KEY_KP_LEFT, new KeyLocation(16, 58, "Lf")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_KP_B2, new KeyLocation(16, 61, "B2")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_KP_RIGHT, new KeyLocation(16, 64, "Rt")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_KP_END, new KeyLocation(18, 58, "Ed")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_KP_DOWN, new KeyLocation(18, 61, "Dn")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_KP_PAGE_DOWN, new KeyLocation(18, 64, "PD")); //$NON-NLS-1$
        // keys.put(BlackenKeys.KEY_KP_ENTER, new KeyLocation(18, 67, "En"));
        keys.put(BlackenKeys.KEY_KP_INSERT, new KeyLocation(20, 58, "Ins")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_KP_DELETE, new KeyLocation(20, 63, "Del")); //$NON-NLS-1$
        
        keys.put(BlackenKeys.KEY_SCROLL_LOCK, new KeyLocation(18, 48, "ScrlLk")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_PRINT_SCREEN, new KeyLocation(19, 48, "PrtScr")); //$NON-NLS-1$

        keys.put(BlackenKeys.KEY_INSERT, new KeyLocation(0, 46, "Ins")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_DELETE, new KeyLocation(0, 51, "Del")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_HOME, new KeyLocation(2, 46, "Home")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_END, new KeyLocation(2, 51, "End")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_PAGE_UP, new KeyLocation(4, 46, "PgUp")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_PAGE_DOWN, new KeyLocation(4, 51, "PgDn")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_UP, new KeyLocation(6, 51, "Up")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_LEFT, new KeyLocation(8, 47, "Lft")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_DOWN, new KeyLocation(8, 51, "Dn")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_RIGHT, new KeyLocation(8, 54, "Rt")); //$NON-NLS-1$

        keys.put(BlackenKeys.KEY_NUM_LOCK, new KeyLocation(0, 58, "Nm")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_DIVIDE, new KeyLocation(0, 61, "//")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_MULTIPLY, new KeyLocation(0, 64, "**")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_SUBTRACT, new KeyLocation(0, 67, "--")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_7, new KeyLocation(2, 58, "77")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_8, new KeyLocation(2, 61, "88")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_9, new KeyLocation(2, 64, "99")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_ADD, new KeyLocation(2, 67, "++")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_4, new KeyLocation(4, 58, "44")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_5, new KeyLocation(4, 61, "55")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_6, new KeyLocation(4, 64, "66")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_1, new KeyLocation(6, 58, "11")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_2, new KeyLocation(6, 61, "22")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_3, new KeyLocation(6, 64, "33")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_ENTER, new KeyLocation(6, 67, "En")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_0, new KeyLocation(8, 58, "00")); //$NON-NLS-1$
        keys.put(BlackenKeys.KEY_NP_SEPARATOR, new KeyLocation(8, 64, "..")); //$NON-NLS-1$
    }
    
    /**
     * Show the key screen.
     */
    public void showKeys() {
        term.disableEventNotices();
        int c = this.palette.get("DimGray"); //$NON-NLS-1$
        term.setCurBackground(c);
        term.clear();
        term.mvputs(23, 0, "Show keys."); //$NON-NLS-1$
        this.loading = true;
        for (Integer codepoint : keys.keySet()) {
            // System.err.printf("Found key: %d\n", codepoint);
            changeKey(codepoint);
        }
        this.loading = false;
        int ch = BlackenKeys.NO_KEY;
        term.refresh();
        while (ch != BlackenKeys.KEY_F10) {
            ch = term.getch();
            if (BlackenKeys.isModifier(ch)) {
                for (BlackenModifier m : BlackenModifier.getAsSet(ch)) {
                    changeKey(m.getAsCodepoint());
                }
            } else {
                changeKey(ch);
            }
        }
    }
    
    /**
     * Perform the main loop.
     * 
     * @return true if we should quit.
     */
    public boolean loop() {
        int ch;
        term.enableEventNotices(null);
        term.puts("Terminal Interface\n"); //$NON-NLS-1$
        term.puts("Press F10 to quit.\n"); //$NON-NLS-1$
        term.puts(">"); //$NON-NLS-1$
        while (!quit) {
            ch = term.getch();
            
            if (BlackenKeys.isModifier(ch)) {
                term.puts(BlackenModifier.getModifierString(ch).toString());
            } else if (BlackenKeys.isKeyCode(ch)) {
                if (ch == BlackenKeys.KEY_F02) {
                    TerminalCell empty = new TerminalCell(null, 
                                                          0xFF000000, 0xFFaaaaaa,
                                                          null, false);
                    term.clear(empty);
                    showKeys();
                    continue;
                }
                if (ch == BlackenKeys.KEY_F03) {
                    TerminalCell empty = new TerminalCell("c\u0327",  //$NON-NLS-1$
                                                         0xFF000000, 0xFFaaaaaa,
                                                         null, false);
                    term.clear(empty);
                    continue;
                }
                if (ch == BlackenKeys.KEY_F04) {
                    TerminalCell empty = new TerminalCell("o\u0327",  //$NON-NLS-1$
                                                         0xFF000000, 0xFFaaaaaa,
                                                         null, false);
                    term.clear(empty);
                    continue;
                }
                if (ch == BlackenKeys.KEY_F09) {
                    TerminalCell empty = new TerminalCell(null, 
                                                          0xFF000000, 0xFFaaaaaa,
                                                          null, false);
                    term.clear(empty);
                }
                if (ch == BlackenKeys.KEY_F10) {
                    this.quit = true;
                    continue;
                }
                term.puts(BlackenKeys.getKeyName(ch));
                if (ch == BlackenKeys.RESIZE_EVENT) {
                    term.puts("\nYummy window resize!"); //$NON-NLS-1$
                }
                if (ch == BlackenKeys.MOUSE_EVENT) {
                    term.puts("\n"); //$NON-NLS-1$
                    term.puts(term.getmouse().toString());
                }
                if (ch == BlackenKeys.WINDOW_EVENT) {
                    term.puts("\n"); //$NON-NLS-1$
                    term.puts(term.getwindow().toString());
                }
                term.puts("\n>"); //$NON-NLS-1$
                
            } else {
                switch (Character.getType(ch)) {
                case Character.COMBINING_SPACING_MARK:
                case Character.ENCLOSING_MARK:
                case Character.NON_SPACING_MARK:
                    term.addch('\u25cc');
                    term.overlaych(ch);
                    break;
                default:
                    term.puts(BlackenKeys.toString(ch));
                }
                term.puts("\n>"); //$NON-NLS-1$
            }
        }
        return this.quit;
    }
    
    /**
     * Command-line starting point.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingTerminal term = new SwingTerminal();
        term.init("Boarder", 25, 80); //$NON-NLS-1$
        ColorPalette palette = new ColorPalette();
        palette.addAll(ColorNames.XTERM_256_COLORS, false);
        palette.putMapping(ColorNames.SVG_COLORS);
        term.setPalette(palette);
        Boarder cmd = new Boarder(term, palette);
        cmd.loop();
        term.quit();
    }

}
