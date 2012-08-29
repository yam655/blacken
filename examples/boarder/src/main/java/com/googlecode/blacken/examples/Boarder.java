/* blacken - a library for Roguelike games
 * Copyright © 2010-2012 Steven Black <yam655@gmail.com>
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

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.core.Obligations;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.*;
import java.util.EnumSet;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example program for the keyboard functions.
 *
 * @author Steven Black
 *
 */
public class Boarder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Boarder.class);

    private CursesLikeAPI term = null;
    private boolean quit = false;
    private ColorPalette palette;

    /**
     * Create a new instance, using an existing terminal and palette.
     *
     * @param term terminal interface to use
     * @param palette color palette to use
     */
    public Boarder(TerminalInterface term, ColorPalette palette) {
        this.loadEnUnitedStatesKeys();
        this.term = new CursesLikeAPI(term);
        this.palette = palette;
    }

    /**
     * Show the help text.
     */
    public void showHelp() {
        this.term.puts("F2 for Keyboard Mode\n");
        this.term.puts("F10 to quit\n");
    }

    /**
     * Change the displayed key.
     *
     * @param codepoint to press.
     */
    public void changeKey(Integer codepoint) {
        if (BlackenKeys.isModifier(codepoint)) {
            EnumSet<BlackenModifier> allMods = BlackenModifier.getAsSet(codepoint);
            if (allMods.size() > 1) {
                for (BlackenModifier mod : allMods) {
                    changeKey(mod.getAsCodepoint());
                }
                return;
            }
        }
        if (!keys.containsKey(codepoint)) {
            return;
        }
        KeyLocation loc = keys.get(codepoint);
        int y = loc.y;
        int x = loc.x;
        String what = loc.name;
        TerminalCellLike tcell = term.get(y, x);
        if (tcell.getSequence().equals("\u0000")
                || tcell.getSequence().equals(" ")) {
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
                if (i == what.codePointCount(0, what.length()) - 1) {
                    tcell.addCellWalls(CellWalls.RIGHT);
                }
                tcell.setSequence(what.codePointAt(i));
                term.set(y, x, tcell);
                tcell = term.get(y, ++x);
                i++;
            }
        } else if (!loading) {
            int back = tcell.getBackground() + 1;
            if (back >= palette.size()) {
                back = 0;
            }
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

    /**
     * Local class to make construction easier.
     */
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

    private void loadDeGermanyKeys() {
        keys = new HashMap<>();
        /*
ESC F1 F2 F3 F4 F5 F6 F7 F8 F9 F10 F11 F12       Einfg Entf   Nm // ** --    #0
^° 1! 2" 3§ 4$ 5% 6& 7/ 8( 9) 0= ß? ´` Rück       Pos1 Ende   77 88 99 ++    #2
Tab Qq Ww Ee Rr Tt Zz Uu Ii Oo Pp Üü *+          BdAuf BdAb   44 55 66       #4
Fest Aa Ss Dd Ff Gg Hh Jj Kk Ll Öö Ää #' Eingabe       ↑      11 22 33 En    #6
   <> Yy Xx Cc Vv Bb Nn Mm ,; .: -_ Umsch           ← ↓ →    00    ..       #8
Strg Logo Alt    Raum      Meta AltGr Menu                                  #10
WindowEvent MouseEvent UnknownKey ResizeEvent                           #12
             yyy  xxx   ScrollLk   yyy  xxx              ⇖ ⇑ ⇗       #14
                        NumLock                Rollen    ⇐ ∎ ⇒       #16
                        CapsLock               Druck     ⇙ ⇓ ⇘       #18
                        KanaLock                      Einfg Entf

² ³ { [ ] } \ ~ @ € | µ
*/
        keys.put(Character.codePointAt("²", 0), new KeyLocation(18, 0, "²"));
        keys.put(Character.codePointAt("³", 0), new KeyLocation(18, 2, "³"));
        keys.put(Character.codePointAt("{", 0), new KeyLocation(18, 4, "{"));
        keys.put(Character.codePointAt("[", 0), new KeyLocation(18, 6, "["));
        keys.put(Character.codePointAt("]", 0), new KeyLocation(18, 8, "]"));
        keys.put(Character.codePointAt("}", 0), new KeyLocation(18, 10, "}"));
        keys.put(Character.codePointAt("\\", 0), new KeyLocation(18, 12, "\\"));
        keys.put(Character.codePointAt("~", 0), new KeyLocation(18, 14, "~"));
        keys.put(Character.codePointAt("@", 0), new KeyLocation(18, 16, "@"));
        keys.put(Character.codePointAt("€", 0), new KeyLocation(18, 18, "€"));
        keys.put(Character.codePointAt("|", 0), new KeyLocation(18, 20, "|"));
        keys.put(Character.codePointAt("µ", 0), new KeyLocation(18, 22, "µ"));

        // XXX needs localized
        keys.put(BlackenKeys.WINDOW_EVENT, new KeyLocation(12, 0, "WindowEvent"));
        keys.put(BlackenKeys.MOUSE_EVENT, new KeyLocation(12, 12, "MouseEvent"));
        keys.put(BlackenKeys.KEY_UNKNOWN, new KeyLocation(12, 23, "UnknownKey"));
        keys.put(BlackenKeys.RESIZE_EVENT, new KeyLocation(12, 34, "ResizeEvent"));

        keys.put(BlackenModifier.MODIFIER_KEY_CTRL.getAsCodepoint(), new KeyLocation(10, 0, "Strg"));
        keys.put(BlackenKeys.KEY_LOGO, new KeyLocation(10, 5, "Logo"));
        keys.put(BlackenModifier.MODIFIER_KEY_ALT.getAsCodepoint(), new KeyLocation(10, 10, "Alt"));
        keys.put(Character.codePointAt(" ", 0), new KeyLocation(10, 18, "Raum"));
        keys.put(BlackenModifier.MODIFIER_KEY_META.getAsCodepoint(), new KeyLocation(10, 28, "Meta"));
        keys.put(BlackenModifier.MODIFIER_KEY_ALTGR.getAsCodepoint(), new KeyLocation(10, 33, "AltGr"));
        keys.put(BlackenKeys.KEY_CONTEXT_MENU, new KeyLocation(10, 39, "Menu"));
        
        keys.put(Character.codePointAt("<", 0), new KeyLocation(8, 3, "<"));
        keys.put(Character.codePointAt(">", 0), new KeyLocation(8, 4, ">"));
        keys.put(Character.codePointAt("Y", 0), new KeyLocation(8, 6, "Y"));
        keys.put(Character.codePointAt("y", 0), new KeyLocation(8, 7, "y"));
        keys.put(Character.codePointAt("X", 0), new KeyLocation(8, 9, "X"));
        keys.put(Character.codePointAt("x", 0), new KeyLocation(8, 10, "x"));
        keys.put(Character.codePointAt("C", 0), new KeyLocation(8, 12, "C"));
        keys.put(Character.codePointAt("c", 0), new KeyLocation(8, 13, "c"));
        keys.put(Character.codePointAt("V", 0), new KeyLocation(8, 15, "V"));
        keys.put(Character.codePointAt("v", 0), new KeyLocation(8, 16, "v"));
        keys.put(Character.codePointAt("B", 0), new KeyLocation(8, 18, "B"));
        keys.put(Character.codePointAt("b", 0), new KeyLocation(8, 19, "b"));
        keys.put(Character.codePointAt("N", 0), new KeyLocation(8, 21, "N"));
        keys.put(Character.codePointAt("n", 0), new KeyLocation(8, 22, "n"));
        keys.put(Character.codePointAt("M", 0), new KeyLocation(8, 24, "M"));
        keys.put(Character.codePointAt("m", 0), new KeyLocation(8, 25, "m"));
        keys.put(Character.codePointAt(",", 0), new KeyLocation(8, 27, ","));
        keys.put(Character.codePointAt(";", 0), new KeyLocation(8, 28, ";"));
        keys.put(Character.codePointAt(".", 0), new KeyLocation(8, 30, "."));
        keys.put(Character.codePointAt(":", 0), new KeyLocation(8, 31, ":"));
        keys.put(Character.codePointAt("-", 0), new KeyLocation(8, 33, "-"));
        keys.put(Character.codePointAt("_", 0), new KeyLocation(8, 34, "_"));
        keys.put(BlackenModifier.MODIFIER_KEY_SHIFT.getAsCodepoint(), new KeyLocation(8, 36, "Umsch"));

        keys.put(BlackenKeys.KEY_CAPS_LOCK, new KeyLocation(6, 0, "Fest"));
        keys.put(Character.codePointAt("A", 0), new KeyLocation(6, 5, "A"));
        keys.put(Character.codePointAt("a", 0), new KeyLocation(6, 6, "a"));
        keys.put(Character.codePointAt("S", 0), new KeyLocation(6, 8, "S"));
        keys.put(Character.codePointAt("s", 0), new KeyLocation(6, 9, "s"));
        keys.put(Character.codePointAt("D", 0), new KeyLocation(6, 11, "D"));
        keys.put(Character.codePointAt("d", 0), new KeyLocation(6, 12, "d"));
        keys.put(Character.codePointAt("F", 0), new KeyLocation(6, 14, "F"));
        keys.put(Character.codePointAt("f", 0), new KeyLocation(6, 15, "f"));
        keys.put(Character.codePointAt("G", 0), new KeyLocation(6, 17, "G"));
        keys.put(Character.codePointAt("g", 0), new KeyLocation(6, 18, "g"));
        keys.put(Character.codePointAt("H", 0), new KeyLocation(6, 20, "H"));
        keys.put(Character.codePointAt("h", 0), new KeyLocation(6, 21, "h"));
        keys.put(Character.codePointAt("J", 0), new KeyLocation(6, 23, "J"));
        keys.put(Character.codePointAt("j", 0), new KeyLocation(6, 24, "j"));
        keys.put(Character.codePointAt("K", 0), new KeyLocation(6, 26, "K"));
        keys.put(Character.codePointAt("k", 0), new KeyLocation(6, 27, "k"));
        keys.put(Character.codePointAt("L", 0), new KeyLocation(6, 29, "L"));
        keys.put(Character.codePointAt("l", 0), new KeyLocation(6, 30, "l"));
        keys.put(Character.codePointAt("Ö", 0), new KeyLocation(6, 32, "Ö"));
        keys.put(Character.codePointAt("ö", 0), new KeyLocation(6, 33, "ö"));
        keys.put(Character.codePointAt("Ä", 0), new KeyLocation(6, 35, "Ä"));
        keys.put(Character.codePointAt("ä", 0), new KeyLocation(6, 36, "ä"));
        keys.put(Character.codePointAt("#", 0), new KeyLocation(6, 38, "#"));
        keys.put(Character.codePointAt("'", 0), new KeyLocation(6, 39, "'"));
        keys.put(BlackenKeys.KEY_ENTER, new KeyLocation(6, 41, "Eingabe"));

        keys.put(BlackenKeys.KEY_TAB, new KeyLocation(4, 0, "Tab"));
        keys.put(Character.codePointAt("Q", 0), new KeyLocation(4, 4, "Q"));
        keys.put(Character.codePointAt("q", 0), new KeyLocation(4, 5, "q"));
        keys.put(Character.codePointAt("W", 0), new KeyLocation(4, 7, "W"));
        keys.put(Character.codePointAt("w", 0), new KeyLocation(4, 8, "w"));
        keys.put(Character.codePointAt("E", 0), new KeyLocation(4, 10, "E"));
        keys.put(Character.codePointAt("e", 0), new KeyLocation(4, 11, "e"));
        keys.put(Character.codePointAt("R", 0), new KeyLocation(4, 13, "R"));
        keys.put(Character.codePointAt("r", 0), new KeyLocation(4, 14, "r"));
        keys.put(Character.codePointAt("T", 0), new KeyLocation(4, 16, "T"));
        keys.put(Character.codePointAt("t", 0), new KeyLocation(4, 17, "t"));
        keys.put(Character.codePointAt("Z", 0), new KeyLocation(4, 19, "Z"));
        keys.put(Character.codePointAt("z", 0), new KeyLocation(4, 20, "z"));
        keys.put(Character.codePointAt("U", 0), new KeyLocation(4, 22, "U"));
        keys.put(Character.codePointAt("u", 0), new KeyLocation(4, 23, "u"));
        keys.put(Character.codePointAt("I", 0), new KeyLocation(4, 25, "I"));
        keys.put(Character.codePointAt("i", 0), new KeyLocation(4, 26, "i"));
        keys.put(Character.codePointAt("O", 0), new KeyLocation(4, 28, "O"));
        keys.put(Character.codePointAt("o", 0), new KeyLocation(4, 29, "o"));
        keys.put(Character.codePointAt("P", 0), new KeyLocation(4, 31, "P"));
        keys.put(Character.codePointAt("p", 0), new KeyLocation(4, 32, "p"));
        keys.put(Character.codePointAt("Ü", 0), new KeyLocation(4, 34, "Ü"));
        keys.put(Character.codePointAt("ü", 0), new KeyLocation(4, 35, "ü"));
        keys.put(Character.codePointAt("*", 0), new KeyLocation(4, 37, "*"));
        keys.put(Character.codePointAt("+", 0), new KeyLocation(4, 38, "+"));

        keys.put(Character.codePointAt("^", 0), new KeyLocation(2, 0, "^"));
        keys.put(Character.codePointAt("°", 0), new KeyLocation(2, 1, "°"));
        keys.put(Character.codePointAt("1", 0), new KeyLocation(2, 3, "1"));
        keys.put(Character.codePointAt("!", 0), new KeyLocation(2, 4, "!"));
        keys.put(Character.codePointAt("2", 0), new KeyLocation(2, 6, "2"));
        keys.put(Character.codePointAt("\"", 0), new KeyLocation(2, 7, "\""));
        keys.put(Character.codePointAt("3", 0), new KeyLocation(2, 9, "3"));
        keys.put(Character.codePointAt("§", 0), new KeyLocation(2, 10, "§"));
        keys.put(Character.codePointAt("4", 0), new KeyLocation(2, 12, "4"));
        keys.put(Character.codePointAt("$", 0), new KeyLocation(2, 13, "$"));
        keys.put(Character.codePointAt("5", 0), new KeyLocation(2, 15, "5"));
        keys.put(Character.codePointAt("%", 0), new KeyLocation(2, 16, "%"));
        keys.put(Character.codePointAt("6", 0), new KeyLocation(2, 18, "6"));
        keys.put(Character.codePointAt("&", 0), new KeyLocation(2, 19, "&"));
        keys.put(Character.codePointAt("7", 0), new KeyLocation(2, 21, "7"));
        keys.put(Character.codePointAt("/", 0), new KeyLocation(2, 22, "/"));
        keys.put(Character.codePointAt("8", 0), new KeyLocation(2, 24, "8"));
        keys.put(Character.codePointAt("(", 0), new KeyLocation(2, 25, "("));
        keys.put(Character.codePointAt("9", 0), new KeyLocation(2, 27, "9"));
        keys.put(Character.codePointAt(")", 0), new KeyLocation(2, 28, ")"));
        keys.put(Character.codePointAt("0", 0), new KeyLocation(2, 30, "0"));
        keys.put(Character.codePointAt("=", 0), new KeyLocation(2, 31, "="));
        keys.put(Character.codePointAt("ß", 0), new KeyLocation(2, 33, "ß"));
        keys.put(Character.codePointAt("?", 0), new KeyLocation(2, 34, "?"));
        keys.put(Character.codePointAt("´", 0), new KeyLocation(2, 36, "´"));
        keys.put(Character.codePointAt("`", 0), new KeyLocation(2, 37, "`"));
        keys.put(BlackenKeys.KEY_BACKSPACE, new KeyLocation(2, 39, "Rück"));

        keys.put(BlackenKeys.KEY_ESCAPE, new KeyLocation(0, 0, "ESC"));
        keys.put(BlackenKeys.KEY_F01, new KeyLocation(0, 4, "F1"));
        keys.put(BlackenKeys.KEY_F02, new KeyLocation(0, 7, "F2"));
        keys.put(BlackenKeys.KEY_F03, new KeyLocation(0, 10, "F3"));
        keys.put(BlackenKeys.KEY_F04, new KeyLocation(0, 13, "F4"));
        keys.put(BlackenKeys.KEY_F05, new KeyLocation(0, 16, "F5"));
        keys.put(BlackenKeys.KEY_F06, new KeyLocation(0, 19, "F6"));
        keys.put(BlackenKeys.KEY_F07, new KeyLocation(0, 22, "F7"));
        keys.put(BlackenKeys.KEY_F08, new KeyLocation(0, 25, "F8"));
        keys.put(BlackenKeys.KEY_F09, new KeyLocation(0, 28, "F9"));
        keys.put(BlackenKeys.KEY_F10, new KeyLocation(0, 31, "F10"));
        keys.put(BlackenKeys.KEY_F11, new KeyLocation(0, 35, "F11"));
        keys.put(BlackenKeys.KEY_F12, new KeyLocation(0, 39, "F12"));

        keys.put(BlackenKeys.KEY_KP_HOME, new KeyLocation(14, 58, "⇖"));
        keys.put(BlackenKeys.KEY_KP_UP, new KeyLocation(14, 60, "⇑"));
        keys.put(BlackenKeys.KEY_KP_PAGE_UP, new KeyLocation(14, 62, "⇗"));
        keys.put(BlackenKeys.KEY_KP_LEFT, new KeyLocation(16, 58, "⇐"));
        keys.put(BlackenKeys.KEY_KP_B2, new KeyLocation(16, 60, "∎"));
        keys.put(BlackenKeys.KEY_KP_RIGHT, new KeyLocation(16, 62, "⇒"));
        keys.put(BlackenKeys.KEY_KP_END, new KeyLocation(18, 58, "⇙"));
        keys.put(BlackenKeys.KEY_KP_DOWN, new KeyLocation(18, 60, "⇓"));
        keys.put(BlackenKeys.KEY_KP_PAGE_DOWN, new KeyLocation(18, 62, "⇘"));
        keys.put(BlackenKeys.KEY_KP_INSERT, new KeyLocation(20, 55, "Einfg"));
        keys.put(BlackenKeys.KEY_KP_DELETE, new KeyLocation(20, 61, "Entf"));

        keys.put(BlackenKeys.KEY_SCROLL_LOCK, new KeyLocation(18, 48, "Rollen"));
        keys.put(BlackenKeys.KEY_PRINT_SCREEN, new KeyLocation(19, 48, "Druck"));

        keys.put(BlackenKeys.KEY_INSERT, new KeyLocation(0, 49, "Einfg"));
        keys.put(BlackenKeys.KEY_DELETE, new KeyLocation(0, 55, "Entf"));
        keys.put(BlackenKeys.KEY_HOME, new KeyLocation(2, 50, "Pos1"));
        keys.put(BlackenKeys.KEY_END, new KeyLocation(2, 55, "Ende"));
        keys.put(BlackenKeys.KEY_PAGE_UP, new KeyLocation(4, 49, "BdAuf"));
        keys.put(BlackenKeys.KEY_PAGE_DOWN, new KeyLocation(4, 55, "BdAb"));
        keys.put(BlackenKeys.KEY_UP, new KeyLocation(6, 55, "↑"));
        keys.put(BlackenKeys.KEY_LEFT, new KeyLocation(8, 53, "←"));
        keys.put(BlackenKeys.KEY_DOWN, new KeyLocation(8, 55, "↓"));
        keys.put(BlackenKeys.KEY_RIGHT, new KeyLocation(8, 57, "→"));

        keys.put(BlackenKeys.KEY_NUM_LOCK, new KeyLocation(0, 62, "Nm"));
        keys.put(BlackenKeys.KEY_NP_DIVIDE, new KeyLocation(0, 65, "//"));
        keys.put(BlackenKeys.KEY_NP_MULTIPLY, new KeyLocation(0, 68, "**"));
        keys.put(BlackenKeys.KEY_NP_SUBTRACT, new KeyLocation(0, 71, "--"));
        keys.put(BlackenKeys.KEY_NP_7, new KeyLocation(2, 62, "77"));
        keys.put(BlackenKeys.KEY_NP_8, new KeyLocation(2, 65, "88"));
        keys.put(BlackenKeys.KEY_NP_9, new KeyLocation(2, 68, "99"));
        keys.put(BlackenKeys.KEY_NP_ADD, new KeyLocation(2, 71, "++"));
        keys.put(BlackenKeys.KEY_NP_4, new KeyLocation(4, 62, "44"));
        keys.put(BlackenKeys.KEY_NP_5, new KeyLocation(4, 65, "55"));
        keys.put(BlackenKeys.KEY_NP_6, new KeyLocation(4, 68, "66"));
        keys.put(BlackenKeys.KEY_NP_1, new KeyLocation(6, 62, "11"));
        keys.put(BlackenKeys.KEY_NP_2, new KeyLocation(6, 65, "22"));
        keys.put(BlackenKeys.KEY_NP_3, new KeyLocation(6, 68, "33"));
        keys.put(BlackenKeys.KEY_NP_ENTER, new KeyLocation(6, 71, "En"));
        keys.put(BlackenKeys.KEY_NP_0, new KeyLocation(8, 62, "00"));
        keys.put(BlackenKeys.KEY_NP_SEPARATOR, new KeyLocation(8, 68, ".."));

    }

    private void loadEnUnitedStatesKeys() {
        keys = new HashMap<>();
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
        keys.put(BlackenKeys.WINDOW_EVENT, new KeyLocation(12, 0, "WindowEvent"));
        keys.put(BlackenKeys.MOUSE_EVENT, new KeyLocation(12, 12, "MouseEvent"));
        keys.put(BlackenKeys.KEY_UNKNOWN, new KeyLocation(12, 23, "UnknownKey"));
        keys.put(BlackenKeys.RESIZE_EVENT, new KeyLocation(12, 34, "ResizeEvent"));

        keys.put(BlackenModifier.MODIFIER_KEY_CTRL.getAsCodepoint(), new KeyLocation(10, 0, "Ctrl"));
        keys.put(BlackenKeys.KEY_LOGO, new KeyLocation(10, 5, "Logo"));
        keys.put(BlackenModifier.MODIFIER_KEY_ALT.getAsCodepoint(), new KeyLocation(10, 10, "Alt"));
        keys.put(Character.codePointAt(" ", 0), new KeyLocation(10, 18, "Space"));
        keys.put(BlackenModifier.MODIFIER_KEY_META.getAsCodepoint(), new KeyLocation(10, 28, "Meta"));
        keys.put(BlackenModifier.MODIFIER_KEY_ALTGR.getAsCodepoint(), new KeyLocation(10, 33, "AltGr"));
        keys.put(BlackenKeys.KEY_CONTEXT_MENU, new KeyLocation(10, 39, "Menu"));

        keys.put(BlackenModifier.MODIFIER_KEY_SHIFT.getAsCodepoint(), new KeyLocation(8, 0, "Shift"));
        keys.put(Character.codePointAt("Z", 0), new KeyLocation(8, 6, "Z"));
        keys.put(Character.codePointAt("z", 0), new KeyLocation(8, 7, "z"));
        keys.put(Character.codePointAt("X", 0), new KeyLocation(8, 9, "X"));
        keys.put(Character.codePointAt("x", 0), new KeyLocation(8, 10, "x"));
        keys.put(Character.codePointAt("C", 0), new KeyLocation(8, 12, "C"));
        keys.put(Character.codePointAt("c", 0), new KeyLocation(8, 13, "c"));
        keys.put(Character.codePointAt("V", 0), new KeyLocation(8, 15, "V"));
        keys.put(Character.codePointAt("v", 0), new KeyLocation(8, 16, "v"));
        keys.put(Character.codePointAt("B", 0), new KeyLocation(8, 18, "B"));
        keys.put(Character.codePointAt("b", 0), new KeyLocation(8, 19, "b"));
        keys.put(Character.codePointAt("N", 0), new KeyLocation(8, 21, "N"));
        keys.put(Character.codePointAt("n", 0), new KeyLocation(8, 22, "n"));
        keys.put(Character.codePointAt("M", 0), new KeyLocation(8, 24, "M"));
        keys.put(Character.codePointAt("m", 0), new KeyLocation(8, 25, "m"));
        keys.put(Character.codePointAt(",", 0), new KeyLocation(8, 27, ","));
        keys.put(Character.codePointAt("<", 0), new KeyLocation(8, 28, "<"));
        keys.put(Character.codePointAt(".", 0), new KeyLocation(8, 30, "."));
        keys.put(Character.codePointAt(">", 0), new KeyLocation(8, 31, ">"));
        keys.put(Character.codePointAt("/", 0), new KeyLocation(8, 33, "/"));
        keys.put(Character.codePointAt("?", 0), new KeyLocation(8, 34, "?"));

        keys.put(BlackenKeys.KEY_CAPS_LOCK, new KeyLocation(6, 0, "Caps"));
        keys.put(Character.codePointAt("A", 0), new KeyLocation(6, 5, "A"));
        keys.put(Character.codePointAt("a", 0), new KeyLocation(6, 6, "a"));
        keys.put(Character.codePointAt("S", 0), new KeyLocation(6, 8, "S"));
        keys.put(Character.codePointAt("s", 0), new KeyLocation(6, 9, "s"));
        keys.put(Character.codePointAt("D", 0), new KeyLocation(6, 11, "D"));
        keys.put(Character.codePointAt("d", 0), new KeyLocation(6, 12, "d"));
        keys.put(Character.codePointAt("F", 0), new KeyLocation(6, 14, "F"));
        keys.put(Character.codePointAt("f", 0), new KeyLocation(6, 15, "f"));
        keys.put(Character.codePointAt("G", 0), new KeyLocation(6, 17, "G"));
        keys.put(Character.codePointAt("g", 0), new KeyLocation(6, 18, "g"));
        keys.put(Character.codePointAt("H", 0), new KeyLocation(6, 20, "H"));
        keys.put(Character.codePointAt("h", 0), new KeyLocation(6, 21, "h"));
        keys.put(Character.codePointAt("J", 0), new KeyLocation(6, 23, "J"));
        keys.put(Character.codePointAt("j", 0), new KeyLocation(6, 24, "j"));
        keys.put(Character.codePointAt("K", 0), new KeyLocation(6, 26, "K"));
        keys.put(Character.codePointAt("k", 0), new KeyLocation(6, 27, "k"));
        keys.put(Character.codePointAt("L", 0), new KeyLocation(6, 29, "L"));
        keys.put(Character.codePointAt("l", 0), new KeyLocation(6, 30, "l"));
        keys.put(Character.codePointAt(";", 0), new KeyLocation(6, 32, ";"));
        keys.put(Character.codePointAt(":", 0), new KeyLocation(6, 33, ":"));
        keys.put(Character.codePointAt("'", 0), new KeyLocation(6, 35, "'"));
        keys.put(Character.codePointAt("\"", 0), new KeyLocation(6, 36, "\""));
        keys.put(BlackenKeys.KEY_ENTER, new KeyLocation(6, 38, "Enter"));

        keys.put(BlackenKeys.KEY_TAB, new KeyLocation(4, 0, "Tab"));
        keys.put(Character.codePointAt("Q", 0), new KeyLocation(4, 4, "Q"));
        keys.put(Character.codePointAt("q", 0), new KeyLocation(4, 5, "q"));
        keys.put(Character.codePointAt("W", 0), new KeyLocation(4, 7, "W"));
        keys.put(Character.codePointAt("w", 0), new KeyLocation(4, 8, "w"));
        keys.put(Character.codePointAt("E", 0), new KeyLocation(4, 10, "E"));
        keys.put(Character.codePointAt("e", 0), new KeyLocation(4, 11, "e"));
        keys.put(Character.codePointAt("R", 0), new KeyLocation(4, 13, "R"));
        keys.put(Character.codePointAt("r", 0), new KeyLocation(4, 14, "r"));
        keys.put(Character.codePointAt("T", 0), new KeyLocation(4, 16, "T"));
        keys.put(Character.codePointAt("t", 0), new KeyLocation(4, 17, "t"));
        keys.put(Character.codePointAt("Y", 0), new KeyLocation(4, 19, "Y"));
        keys.put(Character.codePointAt("y", 0), new KeyLocation(4, 20, "y"));
        keys.put(Character.codePointAt("U", 0), new KeyLocation(4, 22, "U"));
        keys.put(Character.codePointAt("u", 0), new KeyLocation(4, 23, "u"));
        keys.put(Character.codePointAt("I", 0), new KeyLocation(4, 25, "I"));
        keys.put(Character.codePointAt("i", 0), new KeyLocation(4, 26, "i"));
        keys.put(Character.codePointAt("O", 0), new KeyLocation(4, 28, "O"));
        keys.put(Character.codePointAt("o", 0), new KeyLocation(4, 29, "o"));
        keys.put(Character.codePointAt("P", 0), new KeyLocation(4, 31, "P"));
        keys.put(Character.codePointAt("p", 0), new KeyLocation(4, 32, "p"));
        keys.put(Character.codePointAt("[", 0), new KeyLocation(4, 34, "["));
        keys.put(Character.codePointAt("{", 0), new KeyLocation(4, 35, "{"));
        keys.put(Character.codePointAt("]", 0), new KeyLocation(4, 37, "]"));
        keys.put(Character.codePointAt("}", 0), new KeyLocation(4, 38, "}"));
        keys.put(Character.codePointAt("\\", 0), new KeyLocation(4, 40, "\\"));
        keys.put(Character.codePointAt("|", 0), new KeyLocation(4, 41, "|"));

        keys.put(Character.codePointAt("`", 0), new KeyLocation(2, 0, "`"));
        keys.put(Character.codePointAt("~", 0), new KeyLocation(2, 1, "~"));
        keys.put(Character.codePointAt("1", 0), new KeyLocation(2, 3, "1"));
        keys.put(Character.codePointAt("!", 0), new KeyLocation(2, 4, "!"));
        keys.put(Character.codePointAt("2", 0), new KeyLocation(2, 6, "2"));
        keys.put(Character.codePointAt("@", 0), new KeyLocation(2, 7, "@"));
        keys.put(Character.codePointAt("3", 0), new KeyLocation(2, 9, "3"));
        keys.put(Character.codePointAt("#", 0), new KeyLocation(2, 10, "#"));
        keys.put(Character.codePointAt("4", 0), new KeyLocation(2, 12, "4"));
        keys.put(Character.codePointAt("$", 0), new KeyLocation(2, 13, "$"));
        keys.put(Character.codePointAt("5", 0), new KeyLocation(2, 15, "5"));
        keys.put(Character.codePointAt("%", 0), new KeyLocation(2, 16, "%"));
        keys.put(Character.codePointAt("6", 0), new KeyLocation(2, 18, "6"));
        keys.put(Character.codePointAt("^", 0), new KeyLocation(2, 19, "^"));
        keys.put(Character.codePointAt("7", 0), new KeyLocation(2, 21, "7"));
        keys.put(Character.codePointAt("&", 0), new KeyLocation(2, 22, "&"));
        keys.put(Character.codePointAt("8", 0), new KeyLocation(2, 24, "8"));
        keys.put(Character.codePointAt("*", 0), new KeyLocation(2, 25, "*"));
        keys.put(Character.codePointAt("9", 0), new KeyLocation(2, 27, "9"));
        keys.put(Character.codePointAt("(", 0), new KeyLocation(2, 28, "("));
        keys.put(Character.codePointAt("0", 0), new KeyLocation(2, 30, "0"));
        keys.put(Character.codePointAt(")", 0), new KeyLocation(2, 31, ")"));
        keys.put(Character.codePointAt("-", 0), new KeyLocation(2, 33, "-"));
        keys.put(Character.codePointAt("_", 0), new KeyLocation(2, 34, "_"));
        keys.put(Character.codePointAt("=", 0), new KeyLocation(2, 36, "="));
        keys.put(Character.codePointAt("+", 0), new KeyLocation(2, 37, "+"));
        keys.put(BlackenKeys.KEY_BACKSPACE, new KeyLocation(2, 39, "BkSp"));

        keys.put(BlackenKeys.KEY_ESCAPE, new KeyLocation(0, 0, "ESC"));
        keys.put(BlackenKeys.KEY_F01, new KeyLocation(0, 4, "F1"));
        keys.put(BlackenKeys.KEY_F02, new KeyLocation(0, 7, "F2"));
        keys.put(BlackenKeys.KEY_F03, new KeyLocation(0, 10, "F3"));
        keys.put(BlackenKeys.KEY_F04, new KeyLocation(0, 13, "F4"));
        keys.put(BlackenKeys.KEY_F05, new KeyLocation(0, 16, "F5"));
        keys.put(BlackenKeys.KEY_F06, new KeyLocation(0, 19, "F6"));
        keys.put(BlackenKeys.KEY_F07, new KeyLocation(0, 22, "F7"));
        keys.put(BlackenKeys.KEY_F08, new KeyLocation(0, 25, "F8"));
        keys.put(BlackenKeys.KEY_F09, new KeyLocation(0, 28, "F9"));
        keys.put(BlackenKeys.KEY_F10, new KeyLocation(0, 31, "F10"));
        keys.put(BlackenKeys.KEY_F11, new KeyLocation(0, 35, "F11"));
        keys.put(BlackenKeys.KEY_F12, new KeyLocation(0, 39, "F12"));

        // keys.put(BlackenKeys.KEY_NP_DIVIDE, new KeyLocation(12, 61, "//"));
        // keys.put(BlackenKeys.KEY_NP_MULTIPLY, new KeyLocation(12, 64, "**"));
        // keys.put(BlackenKeys.KEY_NP_SUBTRACT, new KeyLocation(12, 67, "--"));
        keys.put(BlackenKeys.KEY_KP_HOME, new KeyLocation(14, 58, "Hm"));
        keys.put(BlackenKeys.KEY_KP_UP, new KeyLocation(14, 61, "Up"));
        keys.put(BlackenKeys.KEY_KP_PAGE_UP, new KeyLocation(14, 64, "PU"));
        // keys.put(BlackenKeys.KEY_NP_ADD, new KeyLocation(14, 67, "++"));
        keys.put(BlackenKeys.KEY_KP_LEFT, new KeyLocation(16, 58, "Lf"));
        keys.put(BlackenKeys.KEY_KP_B2, new KeyLocation(16, 61, "B2"));
        keys.put(BlackenKeys.KEY_KP_RIGHT, new KeyLocation(16, 64, "Rt"));
        keys.put(BlackenKeys.KEY_KP_END, new KeyLocation(18, 58, "Ed"));
        keys.put(BlackenKeys.KEY_KP_DOWN, new KeyLocation(18, 61, "Dn"));
        keys.put(BlackenKeys.KEY_KP_PAGE_DOWN, new KeyLocation(18, 64, "PD"));
        // keys.put(BlackenKeys.KEY_KP_ENTER, new KeyLocation(18, 67, "En"));
        keys.put(BlackenKeys.KEY_KP_INSERT, new KeyLocation(20, 58, "Ins"));
        keys.put(BlackenKeys.KEY_KP_DELETE, new KeyLocation(20, 63, "Del"));

        keys.put(BlackenKeys.KEY_SCROLL_LOCK, new KeyLocation(18, 48, "ScrlLk"));
        keys.put(BlackenKeys.KEY_PRINT_SCREEN, new KeyLocation(19, 48, "PrtScr"));

        keys.put(BlackenKeys.KEY_INSERT, new KeyLocation(0, 46, "Ins"));
        keys.put(BlackenKeys.KEY_DELETE, new KeyLocation(0, 51, "Del"));
        keys.put(BlackenKeys.KEY_HOME, new KeyLocation(2, 46, "Home"));
        keys.put(BlackenKeys.KEY_END, new KeyLocation(2, 51, "End"));
        keys.put(BlackenKeys.KEY_PAGE_UP, new KeyLocation(4, 46, "PgUp"));
        keys.put(BlackenKeys.KEY_PAGE_DOWN, new KeyLocation(4, 51, "PgDn"));
        keys.put(BlackenKeys.KEY_UP, new KeyLocation(6, 51, "Up"));
        keys.put(BlackenKeys.KEY_LEFT, new KeyLocation(8, 47, "Lft"));
        keys.put(BlackenKeys.KEY_DOWN, new KeyLocation(8, 51, "Dn"));
        keys.put(BlackenKeys.KEY_RIGHT, new KeyLocation(8, 54, "Rt"));

        keys.put(BlackenKeys.KEY_NUM_LOCK, new KeyLocation(0, 58, "Nm"));
        keys.put(BlackenKeys.KEY_NP_DIVIDE, new KeyLocation(0, 61, "//"));
        keys.put(BlackenKeys.KEY_NP_MULTIPLY, new KeyLocation(0, 64, "**"));
        keys.put(BlackenKeys.KEY_NP_SUBTRACT, new KeyLocation(0, 67, "--"));
        keys.put(BlackenKeys.KEY_NP_7, new KeyLocation(2, 58, "77"));
        keys.put(BlackenKeys.KEY_NP_8, new KeyLocation(2, 61, "88"));
        keys.put(BlackenKeys.KEY_NP_9, new KeyLocation(2, 64, "99"));
        keys.put(BlackenKeys.KEY_NP_ADD, new KeyLocation(2, 67, "++"));
        keys.put(BlackenKeys.KEY_NP_4, new KeyLocation(4, 58, "44"));
        keys.put(BlackenKeys.KEY_NP_5, new KeyLocation(4, 61, "55"));
        keys.put(BlackenKeys.KEY_NP_6, new KeyLocation(4, 64, "66"));
        keys.put(BlackenKeys.KEY_NP_1, new KeyLocation(6, 58, "11"));
        keys.put(BlackenKeys.KEY_NP_2, new KeyLocation(6, 61, "22"));
        keys.put(BlackenKeys.KEY_NP_3, new KeyLocation(6, 64, "33"));
        keys.put(BlackenKeys.KEY_NP_ENTER, new KeyLocation(6, 67, "En"));
        keys.put(BlackenKeys.KEY_NP_0, new KeyLocation(8, 58, "00"));
        keys.put(BlackenKeys.KEY_NP_SEPARATOR, new KeyLocation(8, 64, ".."));
    }

    /**
     * Show the key screen.
     */
    public void showKeys() {
        term.disableEventNotices();
        term.setCurBackground("DimGray");
        term.clear();
        term.mvputs(22, 0, "(F3) United States - EN");
        term.mvputs(23, 0, "Show keys. (F10) Quit.");
        boolean isUS = true;
        this.loading = true;
        this.loadEnUnitedStatesKeys();
        for (Integer codepoint : keys.keySet()) {
            // System.err.printf("Found key: %d\n", codepoint);
            changeKey(codepoint);
        }
        this.loading = false;
        int ch = BlackenKeys.NO_KEY;
        term.refresh();
        while (ch != BlackenKeys.KEY_F10) {
            ch = term.getch();
            if (ch == BlackenKeys.KEY_F03) {
                term.setCurBackground("DimGray");
                term.clear();
                if (isUS) {
                    term.mvputs(23, 0, "Tasten zeigen. (F10) Schließen.");
                    term.mvputs(22, 0, "Deutschland - DE");
                    this.loadDeGermanyKeys();
                    isUS = false;
                } else {
                    term.mvputs(23, 0, "Show keys. (F10) Close.");
                    term.mvputs(22, 0, "United States - EN");
                    this.loadEnUnitedStatesKeys();
                    isUS = true;
                }
                this.loading = true;
                for (Integer codepoint : keys.keySet()) {
                    // System.err.printf("Found key: %d\n", codepoint);
                    changeKey(codepoint);
                }
                this.loading = false;
            }
            if (BlackenKeys.isModifier(ch)) {
                for (BlackenModifier m : BlackenModifier.getAsSet(ch)) {
                    changeKey(m.getAsCodepoint());
                }
            } else {
                changeKey(ch);
            }
        }
        term.clear();
    }

    /**
     * Perform the main loop.
     *
     * @return true if we should quit.
     */
    public boolean loop() {
        int ch;
        term.enableEventNotices(null);
        term.puts("Terminal Interface\n");
        this.showHelp();
        term.puts(">");
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
                    TerminalCell empty = new TerminalCell("c\u0327",
                            0xFF000000, 0xFFaaaaaa,
                            null, false);
                    term.clear(empty);
                    continue;
                }
                if (ch == BlackenKeys.KEY_F04) {
                    TerminalCell empty = new TerminalCell("o\u0327",
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
                    term.puts("\nYummy window resize!");
                }
                if (ch == BlackenKeys.MOUSE_EVENT) {
                    term.puts("\n");
                    term.puts(term.getmouse().toString());
                }
                if (ch == BlackenKeys.WINDOW_EVENT) {
                    term.puts("\n");
                    term.puts(term.getwindow().toString());
                }
                term.puts("\n>");

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
                term.puts("\n>");
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
        term.init("Boarder", 25, 80);
        ColorPalette palette = new ColorPalette();
        palette.addAll(ColorNames.XTERM_256_COLORS, false);
        palette.putMapping(ColorNames.SVG_COLORS);
        term.setPalette(palette);
        Boarder cmd = new Boarder(term, palette);
        cmd.splash();
        // cmd.loop();
        cmd.showKeys();
        term.quit();
    }

    private void centerOnLine(int y, String string) {
        int offset = term.getWidth() / 2 - string.length() / 2;
        term.mvputs(y, offset, string);
    }

    private void splash() {
        boolean ready = false;
        while (!ready) {
            term.clear();
            term.setCurBackground(0);
            term.setCurForeground(7);
            centerOnLine(0, "Boarder");
            centerOnLine(1, "An awesome demonstration of the keyboard");
            centerOnLine(3, "Copyright (C) 2010-2012 Steven Black");
            centerOnLine(5, "An example for the Blacken Roguelike Library.");
            centerOnLine(6, "Released under the Apache 2.0 License.");
            term.mvputs(8, 0, "HOW TO PLAY");
            term.mvputs(9, 0, "-----------");
            term.mvputs(10,0, "A representation of the keyboard is shown.  Press keys to highlight");
            term.mvputs(10,0, "the key.  It showcases that there are different returned values for");
            term.mvputs(11,0, "all of the important keys, and verifies that your keyboard is working");
            term.mvputs(12,0, "as expected.");
            term.mvputs(14,0, "Modifier keys by themselves are currently shown on the keyboard, but");
            term.mvputs(15,0, "not returned.  They are, however, properly returned when used to modify");
            term.mvputs(16,0, "another key.  All keycodes are valid Unicode codepoints.");
            int last = term.getHeight() - 1;
            term.mvputs(last-3, 0, "Press 'L' for the Apache 2.0 License used by Blacken.");
            term.mvputs(last-2, 0, "Press 'N' for the NOTICES file included with Blacken.");
            term.mvputs(last-1, 0, "Press 'F' for the Deja Vu Font License.");
            term.mvputs(last-0, 0, "Press any other key to continue.");
            int key = BlackenKeys.NO_KEY;
            while(key == BlackenKeys.NO_KEY) {
                // This works around an issue with the AWT putting focus someplace weird
                // if the window is not in focus when it is shown. It only happens on
                // startup, so a splash screen is the perfect place to fix it.
                // A normal game might want an animation at such a spot.
                key = term.getch(200);
            }
            // int modifier = BlackenKeys.NO_KEY;
            if (BlackenKeys.isModifier(key)) {
                // modifier = key;
                key = term.getch(); // should be immediate
            }
            switch(key) {
                case BlackenKeys.NO_KEY:
                case BlackenKeys.RESIZE_EVENT:
                    // should be safe
                    break;
                case 'l':
                case 'L':
                    // show Apache 2.0 License
                    break;
                case 'n':
                case 'N':
                    // show Notices file
                    // This is the only one that needs to be shown for normal games.
                    break;
                case 'f':
                case 'F':
                    // show the font license
                    break;
                default:
                    ready = true;
                    break;
            }
        }
    }
}
