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

package com.googlecode.blacken.terminal.editing;

import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.TerminalInterface;
import com.googlecode.blacken.terminal.TerminalViewInterface;

/**
 * This is a helper class for when you want to perform some sort of looping
 * operation but not lock out user-input.
 *
 * @author Steven Black
 */
public class BreakableLoop {
    /**
     * The default amount of time to wait for keystrokes before continuing.
     */
    public static final int DEFAULT_FREQUENCY_MILLIS = 200;
    private TerminalViewInterface term;
    private CodepointCallbackInterface callback = DefaultSingleLineCallback.getInstance();
    private Steppable stepper;
    private int millis = DEFAULT_FREQUENCY_MILLIS;
    /**
     * Create a new BreakableLoop with the default frequency.
     *
     * @param term terminal to check for keystrokes
     * @param stepper action to be performed
     */
    public BreakableLoop(TerminalViewInterface term, Steppable stepper) {
        this.term = term;
        this.stepper = stepper;
    }
    /**
     * Create a BreakableLoop with an explicit frequency and key handler.
     *
     * @param term
     * @param frequencyMillis
     * @param callback
     * @param stepper
     */
    public BreakableLoop(TerminalViewInterface term, int frequencyMillis,
            CodepointCallbackInterface callback, Steppable stepper) {
        this.term = term;
        this.stepper = stepper;
        if (callback != null) {
            this.callback = callback;
        }
        if (frequencyMillis > -1) {
            this.millis = frequencyMillis;
        }
    }
    /**
     * Run the stepper, checking for keystrokes.
     *
     * <p>This function has two purposes:
     * <ul>
     * <li>Call {@link Steppable#step()} to perform action.
     * <li>Watch for keycodes, calling {@link CodepointCallbackInterface} as
     *     needed
     * </ul>
     *
     * <p>This returns when either the call to step() returns false or when
     * the CodepointCallbackInterface sends a {@link BlackenKeys#CMD_END_LOOP}
     * command.
     */
    public void run() {
        int cp;
        int i = 0;
        boolean doQuit = false;
        while (!doQuit && stepper.step()) {
            cp = term.getch(millis);
            int ec = BlackenKeys.NO_KEY;
            if (cp == BlackenKeys.KEY_MOUSE_EVENT) {
                ec = callback.handleMouseEvent(term.getmouse());
            } else if (cp == BlackenKeys.KEY_WINDOW_EVENT) {
                ec = callback.handleWindowEvent(term.getwindow());
            } else if (cp == BlackenKeys.RESIZE_EVENT) {
                callback.handleResizeEvent();
            } else {
                ec = callback.handleCodepoint(cp);
            }
            switch (ec) {
                case BlackenKeys.NO_KEY:
                    continue;
                case BlackenKeys.CMD_END_LOOP:
                    doQuit = true;
                    break;
                case BlackenKeys.RESIZE_EVENT:
                    // I have no idea why this would be returned
                    callback.handleResizeEvent();
                    break;
                case BlackenKeys.MOUSE_EVENT:
                    cp = BlackenKeys.NO_KEY; // illegal, so we ignore
                    break;
                case BlackenKeys.WINDOW_EVENT:
                    cp = BlackenKeys.NO_KEY; // illegal, so we ignore
                    break;
                default:
                    cp = ec;
                    break;
            }
            if(cp == BlackenKeys.NO_KEY) {
                continue;
            }
            if (cp == BlackenKeys.CMD_END_LOOP) {
                doQuit = true;
                continue;
            }
            if (BlackenKeys.isSpecial(cp)) {
                continue;
            }
            if (doQuit) {
                continue;
            }
            i++;
        }

    }
}
