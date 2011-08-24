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
