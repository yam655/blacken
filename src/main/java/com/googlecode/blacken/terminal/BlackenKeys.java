package com.googlecode.blacken.terminal;

import java.lang.reflect.Field;

/**
 * The Blacken Key Codes map roughly to Curses key codes -- with numerous
 * additions for modifier states and numerous subtractions due to missing
 * keys on modern keyboards.
 * 
 * These fall within Unicode Plane 15 and 16 (Private Use Areas). These 
 * require storage as an int or a surrogate pair in Java -- but they are 
 * valid Unicode characters.
 * 
 * @author Steven Black
 *
 */
public class BlackenKeys {
    static final public int PLANE_KEY_CODES = 15;
    static final public int PLANE_MODIFIER_NOTICES = 16;
    static final public int MODIFIER_CLEAR = 0x100000;
    /**
     * The first of the Blacken key codes. 
     * 
     */
    static final public int KEY_FIRST = 0x0f0000;
    
    /**
     * The last possible Blacken key code.
     * 
     * See the notes about <code>KEY_START</code> as they apply. 
     * 
     * Note that 0xffffe and 0xfffff are reserved in Unicode, so they can't 
     * happen. Also note that this is the last possible key, not the last key
     * actually used.
     */
    static final public int KEY_LAST = 0x0ffffd;
    
    /**
     * A null keycode. Legal but not mapped to a physical key. Do nothing.
     * 
     * We want to drop a keycode, but -- having produced a key event -- we 
     * need to finish processing in a sane manner. This is also produced by 
     * non-blocking calls when there has been no key generated.
     */
    static final public int NO_KEY = KEY_FIRST + 0;
    /**
     * Synonym for NO_KEY.
     */
    static final public int KEY_NO_KEY = NO_KEY;
    
    /** 
     * A mouse event that we are watching for has been generated.
     */
    static final public int MOUSE_EVENT = KEY_FIRST + 1;
    static final public int KEY_MOUSE_EVENT = MOUSE_EVENT;

    /**
     * A window resize event has occurred.
     */
    static final public int RESIZE_EVENT = KEY_FIRST + 2;
    static final public int KEY_RESIZED_EVENT = RESIZE_EVENT;

    static final public int KEY_UNKNOWN = KEY_FIRST + 3;
    static final public int KEY_MODIFIER_NOTICE = KEY_FIRST + 4;
    
    static final public int WINDOW_EVENT = KEY_FIRST + 5;
    static final public int KEY_WINDOW_EVENT = WINDOW_EVENT;
    
//    static final public int KEY_ = KEY_FIRST + 6;
//    static final public int KEY_ = KEY_FIRST + 7;
//    static final public int KEY_ = KEY_FIRST + 8;
//    static final public int KEY_ = KEY_FIRST + 9;

    static final public int KEY_CAPS_LOCK = KEY_FIRST + 10;
    static final public int KEY_CAPS_LOCK_OFF = KEY_FIRST + 11;
    static final public int KEY_KANA_LOCK = KEY_FIRST + 12;
    static final public int KEY_KANA_LOCK_OFF = KEY_FIRST + 13;
    static final public int KEY_NUM_LOCK = KEY_FIRST + 14;
    static final public int KEY_NUM_LOCK_OFF = KEY_FIRST + 15;
    static final public int KEY_SCROLL_LOCK = KEY_FIRST + 16;
    static final public int KEY_SCROLL_LOCK_OFF = KEY_FIRST + 17;
//    static final public int KEY_ = KEY_START + 18;
//    static final public int KEY_ = KEY_START + 19;

    // static final public int KEY_F = KEY_START + 20;
    static final public int KEY_F01 = KEY_FIRST + 21;
    static final public int KEY_F02 = KEY_FIRST + 22;
    static final public int KEY_F03 = KEY_FIRST + 23;
    static final public int KEY_F04 = KEY_FIRST + 24;
    static final public int KEY_F05 = KEY_FIRST + 25;
    static final public int KEY_F06 = KEY_FIRST + 26;
    static final public int KEY_F07 = KEY_FIRST + 27;
    static final public int KEY_F08 = KEY_FIRST + 28;
    static final public int KEY_F09 = KEY_FIRST + 29;
    static final public int KEY_F10 = KEY_FIRST + 30;
    static final public int KEY_F11 = KEY_FIRST + 31;
    static final public int KEY_F12 = KEY_FIRST + 32;
    static final public int KEY_F13 = KEY_FIRST + 33;
    static final public int KEY_F14 = KEY_FIRST + 34;
    static final public int KEY_F15 = KEY_FIRST + 35;
    static final public int KEY_F16 = KEY_FIRST + 36;
    static final public int KEY_F17 = KEY_FIRST + 37;
    static final public int KEY_F18 = KEY_FIRST + 38;
    static final public int KEY_F19 = KEY_FIRST + 39;
    static final public int KEY_F20 = KEY_FIRST + 40;
    static final public int KEY_F21 = KEY_FIRST + 41;
    static final public int KEY_F22 = KEY_FIRST + 42;
    static final public int KEY_F23 = KEY_FIRST + 43;
    static final public int KEY_F24 = KEY_FIRST + 44;

    static final public int KEY_ACCEPT = KEY_FIRST + 45;
    static final public int KEY_BEGIN = KEY_FIRST + 46;
    static final public int KEY_CONVERT = KEY_FIRST + 48;
    static final public int KEY_CODE_INPUT = KEY_FIRST + 49;

    static final public int KEY_COMPOSE = KEY_FIRST + 50;
    static final public int KEY_FINAL = KEY_FIRST + 51;
    static final public int KEY_PROPS = KEY_FIRST + 52;
    static final public int KEY_STOP = KEY_FIRST + 53;
    static final public int KEY_CANCEL = KEY_FIRST + 54;
    static final public int KEY_AGAIN = KEY_FIRST + 55;
    static final public int KEY_COPY = KEY_FIRST + 56;
    static final public int KEY_CUT = KEY_FIRST + 57;
    static final public int KEY_PASTE = KEY_FIRST + 58;
    static final public int KEY_FIND = KEY_FIRST + 59;

    static final public int KEY_HELP = KEY_FIRST + 60;
    static final public int KEY_UNDO = KEY_FIRST + 61;
    static public int KEY_BACKSPACE = KEY_FIRST + 62;
    static final public int KEY_CONTEXT_MENU = KEY_FIRST + 63;
    static public int KEY_ESCAPE = KEY_FIRST + 64;
    static public int KEY_ENTER = KEY_FIRST + 65;
    static final public int KEY_PAUSE = KEY_FIRST + 66;
    static final public int KEY_LOGO = KEY_FIRST + 67;
    static final public int KEY_PRINT_SCREEN = KEY_FIRST + 68;
    static final public int KEY_TAB = KEY_FIRST + 69;

    static final public int KEY_NP_ADD = KEY_FIRST + 70;
    static final public int KEY_NP_DIVIDE = KEY_FIRST + 71;
    static final public int KEY_NP_SUBTRACT = KEY_FIRST + 72;
    static final public int KEY_NP_MULTIPLY = KEY_FIRST + 73;
    static final public int KEY_NP_SEPARATOR = KEY_FIRST + 74;
    static final public int KEY_NP_ENTER = KEY_FIRST + 75;
//  static final public int KEY_ = KEY_FIRST + 76;
//  static final public int KEY_ = KEY_FIRST + 77;
//  static final public int KEY_ = KEY_FIRST + 78;
//  static final public int KEY_ = KEY_FIRST + 79;

    static public int KEY_NP_0 = KEY_FIRST + 80;
    static public int KEY_NP_1 = KEY_FIRST + 81;
    static public int KEY_NP_2 = KEY_FIRST + 82;
    static public int KEY_NP_3 = KEY_FIRST + 83;
    static public int KEY_NP_4 = KEY_FIRST + 84;
    static public int KEY_NP_5 = KEY_FIRST + 85;
    static public int KEY_NP_6 = KEY_FIRST + 86;
    static public int KEY_NP_7 = KEY_FIRST + 87;
    static public int KEY_NP_8 = KEY_FIRST + 88;
    static public int KEY_NP_9 = KEY_FIRST + 89;

    static final public int KEY_UP = KEY_FIRST + 90;
    static final public int KEY_DOWN = KEY_FIRST + 91;
    static final public int KEY_LEFT = KEY_FIRST + 92;
    static final public int KEY_RIGHT = KEY_FIRST + 93;
    static final public int KEY_PAGE_DOWN = KEY_FIRST + 94;
    static final public int KEY_PAGE_UP = KEY_FIRST + 95;
    static final public int KEY_HOME = KEY_FIRST + 96;
    static final public int KEY_END = KEY_FIRST + 97;
    static final public int KEY_INSERT = KEY_FIRST + 98;
    static final public int KEY_DELETE = KEY_FIRST + 99;

    static final public int KEY_ALL_CANDIDATES = KEY_FIRST + 101;
    static final public int KEY_PREVIOUS_CANDIDATE = KEY_FIRST + 102;
    static final public int KEY_FULL_WIDTH = KEY_FIRST + 103;
    static final public int KEY_HALF_WIDTH = KEY_FIRST + 104;
    static final public int KEY_ALPHANUMERIC = KEY_FIRST + 105;
    static final public int KEY_HIRAGANA = KEY_FIRST + 106;
    static final public int KEY_KATAKANA = KEY_FIRST + 107;
    static final public int KEY_KANJI = KEY_FIRST + 108;
    static final public int KEY_NONCONVERT = KEY_FIRST + 109;
    static final public int KEY_ROMAN_CHARACTERS = KEY_FIRST + 110;

    static final public int KEY_INPUT_METHOD_TOGGLE = KEY_FIRST + 111;
    static final public int KEY_MODECHANGE = KEY_FIRST + 112;
    static final public int KEY_JAPANESE_HIRAGANA = KEY_FIRST + 113;
    static final public int KEY_JAPANESE_KATAKANA = KEY_FIRST + 114;
    static final public int KEY_JAPANESE_ROMAN = KEY_FIRST + 115;
//  static final public int KEY_ = KEY_FIRST + 116;
//  static final public int KEY_ = KEY_FIRST + 117;
//  static final public int KEY_ = KEY_FIRST + 118;
//  static final public int KEY_ = KEY_FIRST + 119;

    static final public int KEY_KP_UP = KEY_FIRST + 120;
    static final public int KEY_KP_DOWN = KEY_FIRST + 121;
    static final public int KEY_KP_LEFT = KEY_FIRST + 122;
    static final public int KEY_KP_RIGHT = KEY_FIRST + 123;
    static final public int KEY_KP_PAGE_DOWN = KEY_FIRST + 124;
    static final public int KEY_KP_PAGE_UP = KEY_FIRST + 125;
    static final public int KEY_KP_HOME = KEY_FIRST + 126;
    static final public int KEY_KP_END = KEY_FIRST + 127;
    static final public int KEY_KP_INSERT = KEY_FIRST + 128;
    static final public int KEY_KP_DELETE = KEY_FIRST + 129;
    /**
     * Java calls it CLEAR, Curses calls it B2.
     * It is the center key or the number pad 5 key when numberlock is disabled. 
     */
    static final public int KEY_KP_CLEAR = KEY_FIRST + 130;
    static final public int KEY_KP_B2 = KEY_KP_CLEAR;

//  static final public int KEY_ = KEY_FIRST + 10;
//  static final public int KEY_ = KEY_FIRST + 11;
//  static final public int KEY_ = KEY_FIRST + 12;
//  static final public int KEY_ = KEY_FIRST + 13;
//  static final public int KEY_ = KEY_FIRST + 14;
//  static final public int KEY_ = KEY_FIRST + 15;
//  static final public int KEY_ = KEY_FIRST + 16;
//  static final public int KEY_ = KEY_FIRST + 17;
//  static final public int KEY_ = KEY_FIRST + 18;
//  static final public int KEY_ = KEY_FIRST + 19;

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

    /**
     * Unnamed in unicode.
     */
    static final public int CODEPOINT_PAD = 0x80;
    /**
     * Unnamed in unicode.
     */
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
    /**
     * Unnamed in unicode.
     */
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
    /**
     * Find the plane that a codepoint is on.
     * 
     * @param codepoint The codepoint to check.
     * @return -1 if codepoint isn't a legal codepoint (as of Unicode 5.2)
     */
    public final static int findPlane(int codepoint) {
        if (codepoint < 0) return -1;
        codepoint >>= 16;
        if (codepoint > 16) codepoint = -1;
        return codepoint;
    }

    /**
     * Is the codepoint a keycode or a modifier notice?
     * 
     * @param codepoint
     * @return true if it is a keycode or modifer notice, false otherwise
     */
    public final static boolean isSpecial(int codepoint) {
        int plane = findPlane(codepoint);
        if (plane == PLANE_KEY_CODES || plane == PLANE_MODIFIER_NOTICES) {
            return true;
        }
        return false;
    }
    
    /**
     * Is the codepoint a keycode?
     * 
     * Keycodes exist on Unicode plane PLANE_KEY_CODES. They are guaranteed
     * to exist on a private plane. They are not modifier notices.
     * 
     * @param codepoint
     * @return true if a keycode, false otherwise
     */
    public final static boolean isKeyCode(int codepoint) {
        if (findPlane(codepoint) == BlackenKeys.PLANE_KEY_CODES) {
            return true;
        }
        return false;
    }
    
    /**
     * Is the keycode a modifier key?
     * 
     * @param codepoint keycode to check
     * @return true if modifier, false otherwise
     */
    public final static boolean isModifier(int codepoint) {
        if (findPlane(codepoint) == BlackenKeys.PLANE_MODIFIER_NOTICES) {
            return true;
        }
        return false;
    }

    /**
     * Squash any modifier keycode.
     * 
     * @param codepoint modifier key
     * @return new keycode
     */
    public static int removeModifier(int codepoint) {
        if (findPlane(codepoint) == BlackenKeys.PLANE_MODIFIER_NOTICES) {
            return BlackenKeys.NO_KEY;
        }
        return codepoint;
    }

    public static String toString(int keycode) {
        StringBuffer keybuf = new StringBuffer();
        if (findPlane(keycode) != 0) {
            keybuf.append(String.format("\\U%08x", keycode));
        } else if (keycode < 0x20 || (keycode >= 0x7f && keycode < 0xa0)) {
            keybuf.append(String.format("\\u%04x", keycode));
        } else {
            keybuf.appendCodePoint(keycode);
        }
        return keybuf.toString();
    }
    
    public static String getKeyName(int keycode) {
        StringBuffer keybuf = new StringBuffer();
        keycode = removeModifier(keycode);
        int plane = findPlane(keycode);
        if (plane == 16) {
            keybuf.append(BlackenModifier.getModifierString(keycode));
        } else if (plane == 15) {
            String name = String.format("\\U%08x", keycode);
            for (Field f : BlackenKeys.class.getDeclaredFields()) {
                String fieldName = f.getName();
                if (fieldName.startsWith("KEY_") || 
                    fieldName.startsWith("CODEPOINT_")) {
                    try {
                        if (f.getInt(null) == keycode) {
                            name = f.getName();
                            break;
                        }
                    } catch (IllegalArgumentException e) {
                        continue;
                    } catch (IllegalAccessException e) {
                        continue;
                    }
                }
            }
            keybuf.append(name);
        } else {
            keybuf.appendCodePoint(keycode);
        }
        return keybuf.toString();
    }
}
