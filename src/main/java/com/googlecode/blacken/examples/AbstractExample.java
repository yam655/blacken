/* blacken - a library for Roguelike games
 * Copyright © 2011,2012 Steven Black <yam655@gmail.com>
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
import com.googlecode.blacken.terminal.TerminalInterface;

/**
 * An abstract example class.
 * 
 * @author yam655
 */
public abstract class AbstractExample {
    /**
     * TerminalInterface used by the example
     */
    protected TerminalInterface term;
    /**
     * ColorPalette used by the example
     */
    protected ColorPalette palette;
    /**
     * Whether to quit the loop or not
     */
    protected boolean quit;

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
     * @param palette alternate ColorPalette to use
     */
    public void init(TerminalInterface term, ColorPalette palette) {
        if (term == null) {
            this.term = new SwingTerminal();
            this.term.init("Example Program", 25, 80); //$NON-NLS-1$
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
     * Quit the application.
     * 
     * <p>This calls quit on the underlying TerminalInterface.</p>
     */
    public void quit() {
        term.quit();
    }

    /**
     * The main loop for this example.
     * 
     * @return whether to quit or not
     */
    public abstract boolean loop();
    /**
     * Display the help text within the window.
     */
    public abstract void help();
    /**
     * @param args command-line arguments
     * @param that example instance
     */
    public static void main(String[] args, AbstractExample that) {
        that.init(null, null);
        that.loop();
        that.quit();
    }

}
