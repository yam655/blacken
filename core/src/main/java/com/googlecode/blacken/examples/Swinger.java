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
