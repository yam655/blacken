package com.googlecode.blacken.examples;

import java.util.EnumSet;
import java.util.HashMap;

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenModifier;
import com.googlecode.blacken.terminal.CellWalls;
import com.googlecode.blacken.terminal.TerminalCell;
import com.googlecode.blacken.terminal.TerminalInterface;

public class Boarder {
    private TerminalInterface term = null;
    private boolean quit = false;
    private ColorPalette palette;
    
    Boarder() {
        loadKeys();
        term = new SwingTerminal();
        term.init("Boarder", 25, 80);
        palette = ColorNames.addXtermColors(null, 256);
        ColorNames.addSvgColors(palette);
        loop();
        term.quit();
    }
    Boarder(TerminalInterface term, ColorPalette palette) {
        loadKeys();
        this.term = term;
        this.palette = palette;
    }

    public void showHelp() {
        
    }
    
    public void changeKey(Integer codepoint) {
        if (!keys.containsKey(codepoint)) {
            return;
        }
        KeyLocation loc = keys.get(codepoint); 
        int y = loc.y;
        int x = loc.x;
        String what = loc.name;
        TerminalCell tcell = term.get(y, x);
        if (tcell.getGlyph().equals("\u0000") || tcell.getGlyph().equals(" ")) {
            int back = this.palette.get(1);
            int fore = ColorHelper.makeVisible(back);
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
                tcell.setGlyph(what.codePointAt(i));
                term.set(y, x, tcell);
                tcell = term.get(y, ++x);
                i++;
            }
        } else if (!loading){
            int c = palette.indexOf(tcell.getBackground()) + 1;
            if (c == palette.size()) c = 0;
            int back = this.palette.get(c);
            int fore = ColorHelper.makeVisible(back);
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
ESC F1 F2 F3 F4 F5 F6 F7 F8 F9 F10 F11 F12   Ins  Del    N+ // ** --    #0
`~ 1! 2@ 3# 4$ 5% 6^ 7& 8* 9( 0) -_ =+ BkSp  Home End    77 88 99 ++    #2
Tab Qq Ww Ee Rr Tt Yy Uu Ii Oo Pp [{ ]} \|   PgUp PgDn   44 55 66       #4
Cap+ Aa Ss Dd Ff Gg Hh Jj Kk Ll ;: '" Enter       Up     11 22 33 En    #6
Shift Zz Xx Cc Vv Bb Nn Mm ,< .> /?           Lft Dn Rt  00    ..       #8
Ctrl Logo Alt     Space     Meta AltGr Menu                             #10
WindowEvent MouseEvent UnknownKey ResizeEvent  ScrlLk+   N- // ** --    #12
             yyy  xxx   ScrollLk   yyy  xxx    ScrlLk-   Hm Up PU ++    #14
                        NumLock                Caps-     Lf Ct Rt       #16
                        CapsLock               PrtScr    Ed Dn PD En    #18
                        KanaLock                         Ins  Del
         */

        keys.put(BlackenKeys.KEY_PRINT_SCREEN, new KeyLocation(18, 48, "PrtScr"));

        keys.put(BlackenKeys.KEY_INSERT, new KeyLocation(1, 46, "Ins"));
        keys.put(BlackenKeys.KEY_DELETE, new KeyLocation(1, 51, "Del"));
        keys.put(BlackenKeys.KEY_HOME, new KeyLocation(3, 46, "Home"));
        keys.put(BlackenKeys.KEY_END, new KeyLocation(3, 51, "End"));
        keys.put(BlackenKeys.KEY_PAGE_UP, new KeyLocation(5, 46, "PgUp"));
        keys.put(BlackenKeys.KEY_PAGE_DOWN, new KeyLocation(5, 51, "PgDn"));
        keys.put(BlackenKeys.KEY_UP, new KeyLocation(7, 51, "Up"));
        keys.put(BlackenKeys.KEY_LEFT, new KeyLocation(9, 47, "Lft"));
        keys.put(BlackenKeys.KEY_DOWN, new KeyLocation(9, 51, "Dn"));
        keys.put(BlackenKeys.KEY_RIGHT, new KeyLocation(9, 54, "Rt"));

        keys.put(BlackenKeys.KEY_NUM_LOCK, new KeyLocation(0, 58, "Nm"));
        keys.put(BlackenKeys.KEY_NUM_LOCK_OFF, new KeyLocation(0, 58, "Nm"));
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
    
    public void showKeys() {
        term.disableEventNotices();
        int c = this.palette.get("DimGray");
        term.setCurBackground(c);
        term.clear();
        term.mvputs(23, 0, "Show keys.");
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
    
    public boolean loop() {
        int ch = BlackenKeys.NO_KEY;
        term.enableEventNotices(null);
        term.puts("Terminal Interface\n");
        term.puts("Press F10 to quit.\n");
        term.puts(">");
        while (!quit) {
            ch = term.getch();
            
            if (BlackenKeys.isModifier(ch)) {
                term.puts(BlackenModifier.getModifierString(ch).toString());
            } else if (BlackenKeys.isKeyCode(ch)) {
                if (ch == BlackenKeys.KEY_F02) {
                    showKeys();
                    continue;
                }
                if (ch == BlackenKeys.KEY_F10) {
                    this.quit = true;
                    continue;
                }
                term.puts(BlackenKeys.getKeyName(ch));
                if (ch == BlackenKeys.RESIZED_EVENT) {
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
                term.puts(BlackenKeys.toString(ch));
                term.puts("\n>");
            }
        }
        return this.quit;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        new Boarder();
    }

}
