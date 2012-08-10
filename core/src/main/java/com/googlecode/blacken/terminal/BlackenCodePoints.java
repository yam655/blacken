/* blacken - a library for Roguelike games
 * Copyright © 2012 Steven Black <yam655@gmail.com>
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

package com.googlecode.blacken.terminal;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yam655
 */
public class BlackenCodePoints {
    static final public int CODEPOINT_NUL = 0x00;
    static final public int CODEPOINT_SOH = 0x01;
    static final public int CODEPOINT_STX = 0x02;
    static final public int CODEPOINT_ETX = 0x03;
    static final public int CODEPOINT_EOT = 0x04;
    static final public int CODEPOINT_ENQ = 0x05;
    static final public int CODEPOINT_ACK = 0x06;
    static final public int CODEPOINT_BEL = 0x07;
    static final public int CODEPOINT_BS = 0x08;
    static final public int CODEPOINT_HT = 0x09;
    static final public int CODEPOINT_LF = 0x0a;
    static final public int CODEPOINT_VT = 0x0b;
    static final public int CODEPOINT_FF = 0x0c;
    static final public int CODEPOINT_CR = 0x0d;
    static final public int CODEPOINT_SO = 0x0e;
    static final public int CODEPOINT_SI = 0x0f;
    static final public int CODEPOINT_DLE = 0x10;
    static final public int CODEPOINT_DC1 = 0x11;
    static final public int CODEPOINT_DC2 = 0x12;
    static final public int CODEPOINT_DC3 = 0x13;
    static final public int CODEPOINT_DC4 = 0x14;
    static final public int CODEPOINT_NAK = 0x15;
    static final public int CODEPOINT_SYN = 0x16;
    static final public int CODEPOINT_ETB = 0x17;
    static final public int CODEPOINT_CAN = 0x18;
    static final public int CODEPOINT_EM = 0x19;
    static final public int CODEPOINT_SUB = 0x1a;
    static final public int CODEPOINT_ESC = 0x1b;
    static final public int CODEPOINT_FS = 0x1c;
    static final public int CODEPOINT_GS = 0x1d;
    static final public int CODEPOINT_RS = 0x1e;
    static final public int CODEPOINT_US = 0x1f;
    static final public int CODEPOINT_SP = 0x20;
    static final public int CODEPOINT_DEL = 0x7f;

    static final public int CODEPOINT_PAD = 0x80;
    static final public int CODEPOINT_HOP = 0x81;
    static final public int CODEPOINT_SPH = 0x82;
    static final public int CODEPOINT_NBH = 0x83;
    static final public int CODEPOINT_IND = 0x84;
    static final public int CODEPOINT_NEL = 0x85;
    static final public int CODEPOINT_SSA = 0x86;
    static final public int CODEPOINT_ESA = 0x87;
    static final public int CODEPOINT_HTS = 0x88;
    static final public int CODEPOINT_HTJ = 0x89;
    static final public int CODEPOINT_VTS = 0x8a;
    static final public int CODEPOINT_PLD = 0x8b;
    static final public int CODEPOINT_PLU = 0x8c;
    static final public int CODEPOINT_RI = 0x8d;
    static final public int CODEPOINT_SS2 = 0x8e;
    static final public int CODEPOINT_SS3 = 0x8f;
    static final public int CODEPOINT_DCS = 0x90;
    static final public int CODEPOINT_PU1 = 0x91;
    static final public int CODEPOINT_PU2 = 0x92;
    static final public int CODEPOINT_STS = 0x93;
    static final public int CODEPOINT_CCH = 0x94;
    static final public int CODEPOINT_MW = 0x95;
    static final public int CODEPOINT_SPA = 0x96;
    static final public int CODEPOINT_EPA = 0x97;
    static final public int CODEPOINT_SOS = 0x98;
    static final public int CODEPOINT_SGCI = 0x99;
    static final public int CODEPOINT_SCI = 0x9a;
    static final public int CODEPOINT_CSI = 0x9b;
    static final public int CODEPOINT_ST = 0x9c;
    static final public int CODEPOINT_OSC = 0x9d;
    static final public int CODEPOINT_PM = 0x9e;
    static final public int CODEPOINT_APC = 0x9f;

    static final public int CODEPOINT_NBSP = 0xa0;

    static final public int CODEPOINT_COMBINING_GRAVE_ACCENT = '\u0300';
    static final public int CODEPOINT_COMBINING_ACUTE_ACCENT = '\u0301';
    static final public int CODEPOINT_COMBINING_CIRCUMFLEX_ACCENT = '\u0302';
    static final public int CODEPOINT_COMBINING_TILDE = '\u0303';
    static final public int CODEPOINT_COMBINING_MACRON = '\u0304';
    static final public int CODEPOINT_COMBINING_OVERLINE = '\u0305';
    static final public int CODEPOINT_COMBINING_BREVE = '\u0306';
    static final public int CODEPOINT_COMBINING_DOT_ABOVE = '\u0307';
    static final public int CODEPOINT_COMBINING_DIAERESIS = '\u0308';
    static final public int CODEPOINT_COMBINING_HOOK_ABOVE = '\u0309';
    static final public int CODEPOINT_COMBINING_RING_ABOVE = '\u030A';
    static final public int CODEPOINT_COMBINING_DOUBLE_ACUTE_ACCENT = '\u030B';
    static final public int CODEPOINT_COMBINING_CARON = '\u030C';
    static final public int CODEPOINT_COMBINING_VERTICAL_LINE_ABOVE = '\u030D';
    static final public int CODEPOINT_COMBINING_DOUBLE_VERTICAL_LINE_ABOVE = '\u030E';
    static final public int CODEPOINT_COMBINING_DOUBLE_GRAVE_ACCENT = '\u030F';
    static final public int CODEPOINT_COMBINING_CANDRABINDU = '\u0310';
    static final public int CODEPOINT_COMBINING_INVERTED_BREVE = '\u0311';
    static final public int CODEPOINT_COMBINING_TURNED_COMMA_ABOVE = '\u0312';
    static final public int CODEPOINT_COMBINING_COMMA_ABOVE = '\u0313';
    static final public int CODEPOINT_COMBINING_REVERSED_COMMA_ABOVE = '\u0314';
    static final public int CODEPOINT_COMBINING_COMMA_ABOVE_RIGHT = '\u0315';
    static final public int CODEPOINT_COMBINING_GRAVE_ACCENT_BELOW = '\u0316';
    static final public int CODEPOINT_COMBINING_ACUTE_ACCENT_BELOW = '\u0317';
    static final public int CODEPOINT_COMBINING_LEFT_TACK_BELOW = '\u0318';
    static final public int CODEPOINT_COMBINING_RIGHT_TACK_BELOW = '\u0319';
    static final public int CODEPOINT_COMBINING_LEFT_ANGLE_ABOVE = '\u031A';
    static final public int CODEPOINT_COMBINING_HORN = '\u031B';
    static final public int CODEPOINT_COMBINING_LEFT_HALF_RING_BELOW = '\u031C';
    static final public int CODEPOINT_COMBINING_UP_TACK_BELOW = '\u031D';
    static final public int CODEPOINT_COMBINING_DOWN_TACK_BELOW = '\u031E';
    static final public int CODEPOINT_COMBINING_PLUS_SIGN_BELOW = '\u031F';
    static final public int CODEPOINT_COMBINING_MINUS_SIGN_BELOW = '\u0320';
    static final public int CODEPOINT_COMBINING_PALATALIZED_HOOK_BELOW = '\u0321';
    static final public int CODEPOINT_COMBINING_RETROFLEX_HOOK_BELOW = '\u0322';
    static final public int CODEPOINT_COMBINING_DOT_BELOW = '\u0323';
    static final public int CODEPOINT_COMBINING_DIAERESIS_BELOW = '\u0324';
    static final public int CODEPOINT_COMBINING_RING_BELOW = '\u0325';
    static final public int CODEPOINT_COMBINING_COMMA_BELOW = '\u0326';
    static final public int CODEPOINT_COMBINING_CEDILLA = '\u0327';
    static final public int CODEPOINT_COMBINING_OGONEK = '\u0328';
    static final public int CODEPOINT_COMBINING_GREEK_YPOGEGRAMMENI = '\u0345';

    static final public int CODEPOINT_LIGHT_SHADE = '\u2591';
    static final public int CODEPOINT_MEDIUM_SHADE = '\u2592';
    static final public int CODEPOINT_DARK_SHADE = '\u2593';

    public static String getName(int keycode) {
        int plane = BlackenKeys.findPlane(keycode);
        if (plane == 16) {
            return BlackenModifier.getModifierString(keycode).toString();
        } else if (plane == 15) {
            return BlackenKeys.getKeyName(keycode);
        } else {
            String name = Character.getName(keycode);
            if (name == null) {
                name = String.format("\\U%08x", keycode);
                return name;
            }
            name = "CODEPOINT_" + name.replace(' ', '_');
            Field f;
            try {
                f = BlackenCodePoints.class.getDeclaredField(name);
            } catch (NoSuchFieldException | SecurityException ex) {
                f = null;
            }
            if (f == null) {
                name = Character.getName(keycode);
            }
            return name;
        }
    }

}
