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
import com.googlecode.blacken.terminal.BlackenKeys;

/**
 * Test the color system.
 * 
 * @author Steven Black
 */
public class Colors extends AbstractExample {
    
    /**
     * Create a new, uninitialized instance
     */
    public Colors() {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.examples.AbstractExample#help()
     */
    @Override
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
                    term.setCurBackground(palette.get(bx));
                    term.setCurForeground(ColorHelper.makeVisible(palette.get(bx)));
                    term.puts(String.format("%-3x", bx));
                    for (int f = start_f; f < start_f + 16; f++) {
                        int fx = f % palette.size();
                        term.setCurForeground(palette.get(fx));
                        term.puts(String.format("%-3x", fx));
                    }
                    term.addch('\n');
                }
            } else if (mode == 2 || mode == 4) {
                int b = start_b;
                for (int j = 0; j < 16; j++) {
                    for (int i = 0; i < 16; i++) {
                        int bx = b++ % palette.size();
                        term.setCurBackground(palette.get(bx));
                        if (mode == 2) {
                            term.setCurForeground(ColorHelper.makeVisible(palette.get(bx)));
                        } else {
                            term.setCurForeground(palette.get(start_f));
                        }
                        term.puts(String.format("%-3x", bx));
                    }
                    term.addch('\n');
                }
            } else if (mode == 3 || mode == 5) {
                int f = start_f;
                for (int j = 0; j < 16; j++) {
                    for (int i = 0; i < 16; i++) {
                        int fx = f++ % palette.size();
                        term.setCurForeground(palette.get(fx));
                        if (mode == 3) {
                            term.setCurBackground(ColorHelper.makeVisible(palette.get(fx), 10));
                        } else {
                            term.setCurBackground(palette.get(start_b));
                        }
                        term.puts(String.format("%-3x", fx));
                    }
                    term.addch('\n');
                }
            }
            term.setCurForeground(palette.get(7));
            term.setCurBackground(palette.get(0));
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
            term.refresh();
            ch = term.getch();
            switch(ch) {
            case '+':
                mode++;
                if (mode == 6) { mode = 0; }
                break;
            case '-':
                mode--;
                if (mode == -1) { mode = 5; }
                break;
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
            case BlackenKeys.KEY_HOME:
                if (mode % 2 == 0) {
                    start_b = 0;
                } else {
                    start_f = 0;
                }
                break;
            case BlackenKeys.KEY_END:
                if (mode % 2 == 0) {
                    start_b = this.palette.size() - 16;
                } else {
                    start_f = this.palette.size() - 16;
                }
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
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.examples.AbstractExample#loop()
     */
    @Override
    public boolean loop() {
        int ch = BlackenKeys.NO_KEY;
        int fore = 7;
        int back = 0;
        term.setCurForeground(palette.get(fore));
        term.setCurBackground(palette.get(back));
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
                term.setCurForeground(palette.get(fore));
                term.setCurBackground(palette.get(back));
                break;

            case BlackenKeys.KEY_F03:
                term.puts("<F3 / NEXT FORE>\n");
                fore ++;
                if (fore == palette.size()) fore = 0;
                term.setCurForeground(palette.get(fore));
                break;
            case BlackenKeys.KEY_F04:
                term.puts("<F4 / PREV FORE>\n");
                fore --;
                if (fore < 0) fore = palette.size() - 1;
                term.setCurForeground(palette.get(fore));
                break;
            case BlackenKeys.KEY_F05:
                term.puts("<F5 / NEXT BACK>\n");
                back ++;
                if (back == palette.size()) back = 0;
                term.setCurBackground(palette.get(back));
                break;
            case BlackenKeys.KEY_F06:
                term.puts("<F6 / PREV BACK>\n");
                back --;
                if (back < 0) back = palette.size() - 1;
                term.setCurBackground(palette.get(back));
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
        term.setCurBackground(palette.get(0));
        term.setCurForeground(palette.get(7));
        term.move(0, 0);
        for (int x = 0; x < term.gridWidth(); x++) {
            term.mvputs(0, x, String.format("%1d", x % 10));
        }
        for (int x = 10; x < term.gridWidth(); x+=5) {
            term.mvputs(1, x, String.format("%1d", x / 10 % 10));
        }
        if (term.gridWidth() >= 100) {
            for (int x = 100; x < term.gridWidth(); x+=5) {
                term.mvputs(1, x, String.format("%1d", x / 100));
            }
        }
        for (int y = 2; y < term.gridHeight(); y++) {
            term.mvputs(y, 10, String.format("%d", y));
        }
        term.move(term.gridHeight()-2, 0);
    }

    /**
     * Show the screen dimensions (and maybe other info)
     */
    public void show_info() {
        term.clear();
        // term.setCurBackground(palette.getIndex(0));
        // term.setCurForeground(palette.getIndex(7));
        term.move(0, 0);
        term.puts(String.format("Screen Dimensions: %d x %d\n", 
                                term.gridHeight(), term.gridWidth()));
    }

    /**
     * Start the application.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        AbstractExample.main(args, new Colors());
    }

}
