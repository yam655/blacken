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
