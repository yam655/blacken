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
package com.googlecode.blacken.examples;

import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.CursesLikeAPI;
import com.googlecode.blacken.terminal.TerminalInterface;
import com.googlecode.blacken.terminal.TerminalUtils;

/**
 * QandA example application.
 * 
 * @author Steven Black
 *
 */
public class QandA {

    private boolean quit;
    private ColorPalette palette;
    private CursesLikeAPI term = null;
    
    QandA() {
        // do nothing
    }

    protected boolean loop() {
        int ch = BlackenKeys.NO_KEY;
        if (palette.containsKey("Black")) {
            term.setCurBackground("Black");
        }
        if (palette.containsKey("White")) {
            term.setCurForeground("White");
        }
        term.puts("What is your name?\n");
        term.puts(">");
        String name = term.gets(-1);
        term.puts(String.format("\nHello, %s.\n", name));
        term.puts("What is your occupation?\n");
        String job = term.gets(-1);
        term.puts(String.format("\nExcellent, every %s I have met has been very wise.\n", job));
        term.puts("Type any string you like. When you are done type 'END' in uppercase.\n");
        while (!this.quit) {
            term.puts(">");
            String got = term.gets(-1);
            if ("END".equals(got)) {
                term.puts("\nEnding...\n");
                this.quit = true;
            } else if ("END".equals(got.toUpperCase())) {
                term.puts("\nI'm not ending yet. Case matters. Type 'END' to quit.\n");
            } else {
                term.puts(String.format("\nI saw: %s\n", name));
            }
            
            term.puts("\n");
        }
        term.refresh();
        return this.quit;
    }

    /**
     * Initialize the example
     * 
     * @param term alternate TerminalInterface to use
     * @param palette alternate ColorPalette to use
     */
    public void init(TerminalInterface term, ColorPalette palette) {
        if (term == null) {
            this.term = new CursesLikeAPI(new SwingTerminal());
            this.term.init("QandA", 25, 80);
        } else {
            this.term = new CursesLikeAPI(term);
        }
        if (palette == null) {
            palette = new ColorPalette();
            palette.addAll(ColorNames.XTERM_256_COLORS, false);
            palette.putMapping(ColorNames.SVG_COLORS);
        } 
        this.palette = palette;
        this.term.setPalette(palette);
    }

    /**
     * Start the application.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        QandA cmd = new QandA();
        cmd.init(null, null);
        cmd.loop();
        cmd.quit();
    }
    
    /**
     * Quit this application
     */
    public void quit() {
        this.quit = true;
        term.quit();
    }
}
