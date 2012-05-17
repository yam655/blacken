/* blacken - a library for Roguelike games
 * Copyright © 2010-2012 Steven Black <yam655@gmail.com>
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
package com.googlecode.blacken.terminal;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * The window event
 * 
 * @author yam655
 */
public class BlackenWindowEvent {
    private BlackenEventType type;
    private String name;
    private String oppositeName;
    private EnumSet<BlackenWindowState> newState;
    private EnumSet<BlackenWindowState> oldState;
    
    /**
     * Get the window state strings
     * @param set window state
     * @return list of window states
     */
    public static List<String> 
    getWindowStateStrings(EnumSet<BlackenWindowState> set) {
        List<String> ret = new ArrayList<String>();
        for (BlackenWindowState s : set) {
            ret.add(s.name());
        }
        return ret;
    }

    /**
     * Get the window state strings
     * @param set window state
     * @return buffer containing window states
     */
    public static StringBuffer 
    getWindowStateString(EnumSet<BlackenWindowState> set) {
        List<String> base = getWindowStateStrings(set);
        if (base == null) {
            return null;
        }
        StringBuffer keybuf = new StringBuffer();
        for (String name : base) {
            if (keybuf.length() != 0) {
                keybuf.append('+');
            }
            keybuf.append(name);
        }
        if (keybuf.length() == 0) {
            keybuf.append("(none)"); //$NON-NLS-1$
        }
        return keybuf;
    }
    
    /**
     * Describe the window event
     */
    @Override
    public String toString() {
        return String.format("Window: %s now: %s (was: %s) now: %s (was: %s)",  //$NON-NLS-1$
                             type.name(), name, oppositeName,
                             getWindowStateString(newState),
                             getWindowStateString(oldState));
    }

    /**
     * Create a new window event
     * @param type event type
     */
    public BlackenWindowEvent(BlackenEventType type) {
        this.type = type;
    }

    /**
     * Set the name
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the opposite name
     * @param oppositeName the opposite name
     */
    public void setOppositeName(String oppositeName) {
        this.oppositeName = oppositeName;
    }

    /**
     * Set the new state
     * @param set the new state
     */
    public void setNewState(EnumSet<BlackenWindowState> set) {
        this.newState = set;
    }

    /**
     * Set the old state
     * @param set the old state
     */
    public void setOldState(EnumSet<BlackenWindowState> set) {
        this.oldState = set;
    }

    /**
     * Get the event type
     * @return the event type
     */
    public BlackenEventType getType() {
        return type;
    }

    /**
     * Set the event type
     * @param type the event type
     */
    public void setType(BlackenEventType type) {
        this.type = type;
    }

    /**
     * Get the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the opposite name
     * @return the opposite name
     */
    public String getOppositeName() {
        return oppositeName;
    }

    /**
     * Get the new state
     * @return the new state
     */
    public EnumSet<BlackenWindowState> getNewState() {
        return newState;
    }

    /**
     * Get the old state
     * @return the old state
     */
    public EnumSet<BlackenWindowState> getOldState() {
        return oldState;
    }

}
