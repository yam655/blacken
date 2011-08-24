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

import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.TerminalInterface;

/**
 * A super-simple game
 * 
 * @author yam655
 */
public class Stumble extends AbstractExample {

    TerminalInterface term = null;
    
    /**
     * Create a new instance
     */
    public Stumble() {
        // do nothing
    }

    /**
     * Make a map
     */
    protected void makeMap() {
        // XXX do this
    }
    
    /**
     * The application loop.
     */
    @Override
    public boolean loop() {
        int ch = BlackenKeys.NO_KEY;
        term.puts("Terminal Interface\n"); //$NON-NLS-1$
        term.puts("Press F10 to quit.\n"); //$NON-NLS-1$
        while (ch != BlackenKeys.KEY_F10) {
            term.puts(">"); //$NON-NLS-1$
            term.refresh();
            ch = term.getch();
            term.puts(BlackenKeys.toString(ch));
            term.puts("\n"); //$NON-NLS-1$
        }
        return this.quit;
    }
    
    /**
     * Start the example
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        AbstractExample.main(args, new Stumble());
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.examples.AbstractExample#help()
     */
    @Override
    public void help() {
        // do nothing
    }

}
