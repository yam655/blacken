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
package com.googlecode.blacken.terminal;


import java.util.EnumSet;

import com.googlecode.blacken.grid.Grid;


/**
 * An unbound terminal -- no user-interface attached
 * 
 * @author yam655
 */
public class UnboundTerminal extends AbstractTerminal {

    /**
     * Create a new terminal
     */
    public UnboundTerminal() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.AbstractTerminal#disableEventNotice(com.googlecode.blacken.terminal.BlackenEventType)
     */
    @Override
    public void disableEventNotice(BlackenEventType event) {
        // do nothing
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.AbstractTerminal#disableEventNotices()
     */
    @Override
    public void disableEventNotices() {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.AbstractTerminal#enableEventNotice(com.googlecode.blacken.terminal.BlackenEventType)
     */
    @Override
    public void enableEventNotice(BlackenEventType event) {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.AbstractTerminal#enableEventNotices(java.util.EnumSet)
     */
    @Override
    public void enableEventNotices(EnumSet<BlackenEventType> events) {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.AbstractTerminal#getch()
     */
    @Override
    public int getch() {
        this.refresh();
        return BlackenKeys.NO_KEY;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.AbstractTerminal#getLockingStates()
     */
    @Override
    public EnumSet<BlackenModifier> getLockingStates() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.AbstractTerminal#getmouse()
     */
    @Override
    public BlackenMouseEvent getmouse() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.AbstractTerminal#getwindow()
     */
    @Override
    public BlackenWindowEvent getwindow() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalInterface#set(int, int, com.googlecode.blacken.terminal.TerminalCellLike)
     */
    @Override
    public void set(int y, int x, TerminalCellLike tcell) {
        Grid<TerminalCellLike> grid = getGrid();
        grid.get(y, x).set(tcell);
        grid.get(y, x).setDirty(false);
        
    }

}
