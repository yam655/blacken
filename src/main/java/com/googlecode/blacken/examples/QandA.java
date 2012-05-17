/* blacken - a library for Roguelike games
 * Copyright © 2012 Steven Black <yam655@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with this program.  
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.blacken.examples;

import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.TerminalInterface;

/**
 * QandA example application.
 * 
 * @author Steven Black
 *
 */
public class QandA {

    private boolean quit;
    private ColorPalette palette;
    private TerminalInterface term = null;
    
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
        term.puts(">");
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
                term.puts(String.format("\nI saw: %s\n", got));
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
            this.term = new SwingTerminal();
            this.term.init("QandA", 25, 80);
        } else {
            this.term = term;
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
