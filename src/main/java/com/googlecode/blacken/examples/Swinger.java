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
import com.googlecode.blacken.terminal.BlackenModifier;

/**
 * Generic SwingTerminal test.
 * 
 * @author yam655
 */
public class Swinger extends AbstractExample {
    
    Swinger() {
        // do nothing
    }

    /**
     * Show the help message
     */
    @Override
    public void help() {
        // XXX do this
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.examples.AbstractExample#loop()
     */
    @Override
    public boolean loop() {
        int ch = BlackenKeys.NO_KEY;
        term.enableEventNotices(null);
        term.puts("Terminal Interface\n"); //$NON-NLS-1$
        term.puts("Press F10 to quit.\n"); //$NON-NLS-1$
        term.puts(">"); //$NON-NLS-1$
        while (ch != BlackenKeys.KEY_F10) {
            ch = term.getch();
            
            if (BlackenKeys.isModifier(ch)) {
                term.puts(BlackenModifier.getModifierString(ch).toString());
            } else if (BlackenKeys.isKeyCode(ch)) {
                term.puts(BlackenKeys.getKeyName(ch));
                if (ch == BlackenKeys.RESIZE_EVENT) {
                    term.puts("\nYummy window resize!"); //$NON-NLS-1$
                }
                if (ch == BlackenKeys.MOUSE_EVENT) {
                    term.puts("\n"); //$NON-NLS-1$
                    term.puts(term.getmouse().toString());
                }
                if (ch == BlackenKeys.WINDOW_EVENT) {
                    term.puts("\n"); //$NON-NLS-1$
                    term.puts(term.getwindow().toString());
                }
                term.puts("\n>"); //$NON-NLS-1$
                
            } else {
                term.puts(BlackenKeys.toString(ch));
                term.puts("\n>"); //$NON-NLS-1$
            }
        }
        return this.quit;
    }
    
    /**
     * Start the example.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        AbstractExample.main(args, new Swinger());
    }

}
