/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.blacken.examples;

import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.TerminalInterface;

/**
 * Simple example application.
 * 
 * <p>Most applications make use of {@link AbstractExample} but this one does
 * not to make it clearer to new developers.</p>
 * 
 * @author yam655
 *
 */
public class Simple {

    private boolean quit;
    private ColorPalette palette;
    private TerminalInterface term = null;
    
    Simple() {
        // do nothing
    }

    protected boolean loop() {
        int ch = BlackenKeys.NO_KEY;
        if (palette.containsKey("Black")) { //$NON-NLS-1$
            term.setCurBackground("Black"); //$NON-NLS-1$
        }
        if (palette.containsKey("White")) { //$NON-NLS-1$
            term.setCurForeground("White"); //$NON-NLS-1$
        }
        term.puts("Terminal Interface\n"); //$NON-NLS-1$
        term.puts("Press F10 to quit.\n"); //$NON-NLS-1$
        while (!this.quit) {
            term.puts(">"); //$NON-NLS-1$
            // getch automatically does a refresh
            ch = term.getch();
            term.puts(BlackenKeys.toString(ch));
            if (ch == BlackenKeys.KEY_F10) {
                this.quit = true;
            }
            term.puts("\n"); //$NON-NLS-1$
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
            this.term.init("Simple", 25, 80); //$NON-NLS-1$
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
        Simple cmd = new Simple();
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
