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

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.core.ListMap;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenModifier;
import com.googlecode.blacken.terminal.BlackenModifier;
import com.googlecode.blacken.terminal.CursesLikeAPI;
import com.googlecode.blacken.terminal.TerminalInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Test the color system.
 * 
 * @author Steven Black
 */
public class Colors {
    /**
     * TerminalInterface used by the example
     */
    private CursesLikeAPI term;
    /**
     * ColorPalette used by the example
     */
    private ColorPalette palette;
    /**
     * Whether to quit the loop or not
     */
    private boolean quit;
    /**
     * The available palettes.
     */
    private ListMap<String, ColorPalette> palettes = new ListMap<>();
    private ListMap<String, Map<Integer, String>> nameMaps = new ListMap<>();
    /**
     * The current palette.
     */
    private int palIndex = 0;

    /**
     * Create a new, uninitialized instance
     */
    public Colors() {
        // do nothing
    }

    /**
     * Tell the loop to quit.
     *
     * @param quit new quit status
     */
    public void setQuit(boolean quit) {
        this.quit = quit;
    }
    /**
     * Get the quit status.
     *
     * @return whether we should quit
     */
    public boolean getQuit() {
        return quit;
    }

    /**
     * Initialize the example
     *
     * @param term alternate TerminalInterface to use
     */
    public void init(TerminalInterface term) {
        if (term == null) {
            term = new SwingTerminal();
            term.init("Example Program", 21, 52);
        }
        this.term = new CursesLikeAPI(term);
        this.loadPalettes();
        this.palette = palettes.get(palIndex);
        this.term.setPalette(palette);
    }
    /**
     * Quit the application.
     *
     * <p>This calls quit on the underlying TerminalInterface.</p>
     */
    public void quit() {
        term.quit();
    }

    public void help() {
        term.puts("Terminal Interface\n");
        term.puts("Command keys include:\n");
        term.puts("F1  - this help text\n");
        term.puts("F2  - show the color grid\n");
        term.puts("F3  - next foreground\n");
        term.puts("F4  - prev foreground\n");
        term.puts("F5  - next background\n");
        term.puts("F6  - prev background\n");
        term.puts("F7  - print information\n");
        term.puts("F8  - show size grid\n");
        term.puts("F9  - clear screen\n");
        term.puts("F10 - quit\n");
        term.puts("\nPress F10 to quit.\n");
    }
    /**
     * Show the interactive color display.
     * 
     * @return true if the app should quit
     */
    public boolean showColors() {
        int ch = BlackenKeys.NO_KEY;
        int start_b = 0;
        int start_f = 0;
        int mode = 0;
        do {
            term.clear();
            term.move(0, 0);
            if (mode == 0 || mode == 1) {
                for (int b = start_b; b < start_b + 16; b++) {
                    int bx = b % palette.size();
                    term.setCurBackground(bx);
                    term.setCurForeground(ColorHelper.makeVisible(palette.get(bx)));
                    term.puts(String.format("%-3x", bx));
                    for (int f = start_f; f < start_f + 16; f++) {
                        int fx = f % palette.size();
                        term.setCurForeground(fx);
                        term.puts(String.format("%-3x", fx));
                    }
                    term.setCurForeground(ColorHelper.makeVisible(palette.get(bx)));
                    int avail = term.getWidth() - term.getCursorX();
                    String name = Objects.toString(
                            nameMaps.get(palIndex).get(palette.get(bx)),
                            String.format("#%06x", palette.get(bx) & 0xffffff));
                    term.puts(name.substring(0, Math.min(avail, name.length())));
                    if (term.getCursorX() > 1) {
                        term.addch('\n');
                    }
                }
            } else if (mode == 2 || mode == 4) {
                int b = start_b;
                for (int j = 0; j < 16; j++) {
                    if (mode == 4) {
                        term.setCurForeground(start_f);
                    }
                    for (int i = 0; i < 16; i++) {
                        int bx = b++ % palette.size();
                        term.setCurBackground(bx);
                        if (mode == 2) {
                            term.setCurForeground(ColorHelper.makeVisible(palette.get(bx)));
                        }
                        term.puts(String.format("%-3x", bx));
                    }
                    if (mode == 4) {
                        term.setCurBackground(ColorHelper.makeVisible(palette.get(start_f)));
                        int avail = term.getWidth() - term.getCursorX();
                        String name = Objects.toString(
                                nameMaps.get(palIndex).get(palette.get(start_f)),
                                String.format("#%06x", palette.get(start_f) & 0xffffff));
                        term.puts(name.substring(0, Math.min(avail, name.length())));
                    }
                    if (term.getCursorX() > 1) {
                        term.addch('\n');
                    }
                }
            } else if (mode == 3 || mode == 5) {
                int f = start_f;
                for (int j = 0; j < 16; j++) {
                    if (mode == 5) {
                        term.setCurBackground(start_b);
                    }
                    for (int i = 0; i < 16; i++) {
                        int fx = f++ % palette.size();
                        term.setCurForeground(palette.get(fx));
                        if (mode == 3) {
                            term.setCurBackground(ColorHelper.makeVisible(palette.get(fx), 10));
                        }
                        term.puts(String.format("%-3x", fx));
                    }
                    if (mode == 5) {
                        term.setCurForeground(ColorHelper.makeVisible(palette.get(start_b)));
                        int avail = term.getWidth() - term.getCursorX();
                        String name = Objects.toString(
                                nameMaps.get(palIndex).get(palette.get(start_b)),
                                String.format("#%06x", palette.get(start_b) & 0xffffff));
                        term.puts(name.substring(0, Math.min(avail, name.length())));
                    }
                    if (term.getCursorX() > 1) {
                        term.addch('\n');
                    }
                }
            }
            Integer normal = palette.get("Silver");
            if (normal == null) {
                normal = palette.get("White");
            }
            if (normal == null) {
                normal = palette.get("white");
            }
            if (normal == null) {
                normal = 7;
            }
            term.setCurForeground(normal);
            term.setCurBackground(0);
            term.puts("Use arrow keys to browse grid or period (.) to end.\n");
            term.puts("Use +/- to change mode.  Current mode:\n        ");
            switch(mode) {
            case 0: 
                term.puts("background: CHANGING; foreground: variable"); 
                break;
            case 1: 
                term.puts("background: variable; foreground: CHANGING"); 
                break;
            case 2: 
                term.puts("background: CHANGING; foreground: visible"); 
                break;
            case 3: 
                term.puts("background: visible; foreground: CHANGING"); 
                break;
            case 4: 
                term.puts("background: CHANGING; foreground: single"); 
                break;
            case 5:
                term.puts("background: single; foreground: CHANGING"); 
                break;
            }
            term.puts("\nUse Space to change palette. Current palette:\n        ");
            term.puts(palettes.getKey(palette));
            term.refresh();
            ch = term.getch();
            if (BlackenKeys.isModifier(ch)) {
                int modifier = ch;
                ch = term.getch();
                if (BlackenModifier.hasFlag(modifier, BlackenModifier.MODIFIER_KEY_SHIFT) && (ch == BlackenKeys.KEY_UP || ch == BlackenKeys.KEY_NP_8 || ch == BlackenKeys.KEY_KP_UP)) {
                    ch = ' ';
                }
                if (BlackenModifier.hasFlag(modifier, BlackenModifier.MODIFIER_KEY_SHIFT) && (ch == BlackenKeys.KEY_DOWN || ch == BlackenKeys.KEY_NP_2 || ch == BlackenKeys.KEY_KP_DOWN)) {
                    ch = BlackenKeys.KEY_BACKSPACE;
                }
            }
            switch(ch) {
            case ' ':
            case '*':
            case BlackenKeys.KEY_NP_MULTIPLY:
                palIndex++;
                if (palIndex == palettes.size()) {
                    palIndex = 0;
                }
                this.palette = palettes.get(palIndex);
                term.setPalette(palette);
                break;
            case BlackenKeys.KEY_BACKSPACE:
            case '/':
            case BlackenKeys.KEY_NP_DIVIDE:
                palIndex--;
                if (palIndex < 0) {
                    palIndex = palettes.size() -1;
                }
                this.palette = palettes.get(palIndex);
                term.setPalette(palette);
                break;
            case BlackenKeys.KEY_NP_ADD:
            case '+':
                mode++;
                if (mode == 6) { mode = 0; }
                break;
            case BlackenKeys.KEY_NP_SUBTRACT:
            case '-':
                mode--;
                if (mode == -1) { mode = 5; }
                break;
            case BlackenKeys.KEY_NP_4:
            case BlackenKeys.KEY_KP_LEFT:
            case BlackenKeys.KEY_LEFT:
                if (mode % 2 == 0) {
                    start_f --;
                    if (start_f < 0) {
                        start_f += this.palette.size();
                    }
                } else {
                    start_b --;
                    if (start_b < 0) {
                        start_b += this.palette.size();
                    }
                }
                break;
            case BlackenKeys.KEY_NP_6:
            case BlackenKeys.KEY_KP_RIGHT:
            case BlackenKeys.KEY_RIGHT:
                if (mode % 2 == 0) {
                    start_f ++;
                    if (start_f >= this.palette.size()) {
                        start_f -= this.palette.size();
                    }
                } else {
                    start_b ++;
                    if (start_b >= this.palette.size()) {
                        start_b -= this.palette.size();
                    }
                }
                break;
            case BlackenKeys.KEY_NP_8:
            case BlackenKeys.KEY_KP_UP:
            case BlackenKeys.KEY_UP:
                if (mode % 2 == 0) {
                    start_b --;
                    if (start_b < 0) {
                        start_b += this.palette.size();
                    }
                } else {
                    start_f --;
                    if (start_f < 0) {
                        start_f += this.palette.size();
                    }
                }
                break;
            case BlackenKeys.KEY_NP_2:
            case BlackenKeys.KEY_KP_DOWN:
            case BlackenKeys.KEY_DOWN:
                if (mode % 2 == 0) {
                    start_b ++;
                    if (start_b >= this.palette.size()) {
                        start_b -= this.palette.size();
                    }
                } else {
                    start_f ++;
                    if (start_f >= this.palette.size()) {
                        start_f -= this.palette.size();
                    }
                }
                break;
            case BlackenKeys.KEY_NP_9:
            case BlackenKeys.KEY_KP_PAGE_UP:
            case BlackenKeys.KEY_PAGE_UP:
                if (mode % 2 == 0) {
                    start_b -= 16;
                    if (start_b < 0) {
                        start_b += this.palette.size();
                    }
                } else {
                    start_f -= 16;
                    if (start_f < 0) {
                        start_f += this.palette.size();
                    }
                }
                break;
            case BlackenKeys.KEY_NP_3:
            case BlackenKeys.KEY_KP_PAGE_DOWN:
            case BlackenKeys.KEY_PAGE_DOWN:
                if (mode % 2 == 0) {
                    start_b += 16;
                    if (start_b >= this.palette.size()) {
                        start_b -= this.palette.size();
                    }
                } else {
                    start_f += 16;
                    if (start_f >= this.palette.size()) {
                        start_f -= this.palette.size();
                    }
                }
                break;
            case BlackenKeys.KEY_NP_7:
            case BlackenKeys.KEY_KP_HOME:
            case BlackenKeys.KEY_HOME:
                if (mode % 2 == 0) {
                    start_b = 0;
                } else {
                    start_f = 0;
                }
                break;
            case BlackenKeys.KEY_NP_1:
            case BlackenKeys.KEY_KP_END:
            case BlackenKeys.KEY_END:
                if (mode % 2 == 0) {
                    start_b = this.palette.size() - 16;
                } else {
                    start_f = this.palette.size() - 16;
                }
                break;
            case BlackenKeys.KEY_KP_DELETE:
            case BlackenKeys.KEY_NP_SEPARATOR:
                ch = '.';
                break;
            }
        } while(ch != BlackenKeys.KEY_F10 && ch != '.' && 
                ch != BlackenKeys.KEY_ESCAPE);
        if (ch == BlackenKeys.KEY_F10) {
            this.quit = true;
        } else {
            term.addch('\n');
        }
        return this.quit;
    }

    private Map<Integer, String> inverseMap(ColorPalette p) {
        ListMap<Integer, List<String>> tmp = new ListMap<>();
        Map<Integer, String> ret = new HashMap<>();
        for (String name : p.keySet()) {
            Integer value = p.get(name);
            if (value == null) {
                continue; // should never happen
            }
            List<String> current = tmp.get(value);
            if (current == null) {
                current = new ArrayList<>();
                tmp.put(value, current);
            }
            current.add(name);
        }
        for (Integer value : tmp.keySet()) {
            List<String> current = tmp.get(value);
            Collections.sort(current);
            StringBuilder buf = new StringBuilder();
            for (String name : current) {
                if (buf.length() != 0) {
                    buf.append(" / ");
                }
                buf.append(name);
            }
            ret.put(value, buf.toString());
        }
        return ret;
    }

    private void loadPalettes() {
        ColorPalette p = new ColorPalette();
        p.putMapping(ColorNames.CGA_16_COLORS);
        this.palettes.put("CGA 16", p);
        this.nameMaps.put("CGA 16", inverseMap(p));
        p = new ColorPalette();
        p.putMapping(ColorNames.HTML_COLORS);
        this.palettes.put("HTML", p);
        this.nameMaps.put("HTML", inverseMap(p));
        p = new ColorPalette();
        p.putMapping(ColorNames.STANDARD_16_COLORS);
        this.palettes.put("STANDARD 16", p);
        this.nameMaps.put("STANDARD 16", inverseMap(p));
        p = new ColorPalette();
        p.putMapping(ColorNames.SVG_COLORS);
        this.palettes.put("SVG", p);
        this.nameMaps.put("SVG", inverseMap(p));
        p = new ColorPalette();
        p.addAll(ColorNames.XTERM_16_COLORS, false);
        this.palettes.put("XTERM 16", p);
        this.nameMaps.put("XTERM 16", new HashMap<Integer, String>());
        p = new ColorPalette();
        p.addAll(ColorNames.XTERM_88_COLORS, false);
        this.palettes.put("XTERM 88", p);
        this.nameMaps.put("XTERM 88", new HashMap<Integer, String>());
        p = new ColorPalette();
        p.addAll(ColorNames.XTERM_256_COLORS, false);
        this.palettes.put("XTERM 256", p);
        this.nameMaps.put("XTERM 256", new HashMap<Integer, String>());
        p = new ColorPalette();
        p.putMapping(ColorNames.LIBTCOD_COLORS);
        this.palettes.put("LIBTCOD", p);
        this.nameMaps.put("LIBTCOD", inverseMap(p));
    }

    public boolean loop() {
        int ch;
        int fore = 7;
        int back = 0;
        term.setCurForeground(fore);
        term.setCurBackground(back);
        help();
        quit = false;
        while (!quit) {
            term.puts(">");
            term.refresh();
            ch = term.getch();
            switch(ch) {
            case BlackenKeys.KEY_F01:
                term.puts("<F1 / HELP>\n");
                help();
                break;
            case BlackenKeys.KEY_F02:
                term.puts("<F2 / SHOW COLORS>\n");
                showColors();
                term.setCurForeground(fore);
                term.setCurBackground(back);
                break;

            case BlackenKeys.KEY_F03:
                term.puts("<F3 / NEXT FORE>\n");
                fore ++;
                if (fore == palette.size()) {
                    fore = 0;
                }
                term.setCurForeground(fore);
                break;
            case BlackenKeys.KEY_F04:
                term.puts("<F4 / PREV FORE>\n");
                fore --;
                if (fore < 0) {
                    fore = palette.size() - 1;
                }
                term.setCurForeground(fore);
                break;
            case BlackenKeys.KEY_F05:
                term.puts("<F5 / NEXT BACK>\n");
                back ++;
                if (back == palette.size()) {
                    back = 0;
                }
                term.setCurBackground(back);
                break;
            case BlackenKeys.KEY_F06:
                term.puts("<F6 / PREV BACK>\n");
                back --;
                if (back < 0) {
                    back = palette.size() - 1;
                }
                term.setCurBackground(back);
                break;
            case BlackenKeys.KEY_F07:
                term.puts("<F7 / SHOW INFO>\n");
                show_info();
                break;
            case BlackenKeys.KEY_F08:
                term.puts("<F8 / SHOW GRID>\n");
                show_grid();
                break;
            case BlackenKeys.KEY_F09:
                term.puts("<F9 / CLEAR>\n");
                term.clear();
                term.move(0, 0);
                break;
            case BlackenKeys.KEY_F10:
                term.puts("<F10 / QUIT>\n");
                quit = true;
                continue;
            default:
                term.puts(BlackenKeys.toString(ch));
                term.puts("\n");
            }
        }
        return quit;
    }
    
    /**
     * Show the screen-size grid
     */
    public void show_grid() {
        term.clear();
        term.setCurBackground(0);
        term.setCurForeground(7);
        term.move(0, 0);
        for (int x = 0; x < term.getWidth(); x++) {
            term.mvputs(0, x, String.format("%1d", x % 10));
        }
        for (int x = 10; x < term.getWidth(); x+=5) {
            term.mvputs(1, x, String.format("%1d", x / 10 % 10));
        }
        if (term.getWidth() >= 100) {
            for (int x = 100; x < term.getWidth(); x+=5) {
                term.mvputs(1, x, String.format("%1d", x / 100));
            }
        }
        for (int y = 2; y < term.getHeight(); y++) {
            term.mvputs(y, 10, String.format("%d", y));
        }
        term.move(term.getHeight()-2, 0);
    }

    /**
     * Show the screen dimensions (and maybe other info)
     */
    public void show_info() {
        term.clear();
        // term.setCurBackground(0);
        // term.setCurForeground(7);
        term.move(0, 0);
        term.puts(String.format("Screen Dimensions: %d x %d\n", 
                                term.getHeight(), term.getWidth()));
    }

    /**
     * Start the application.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Colors that = new Colors();
        that.init(null);
        // that.loop();
        that.showColors();
        that.quit();
    }

}
