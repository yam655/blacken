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
 * Window state
 * 
 * @author yam655
 */
public enum BlackenWindowState {
    /**
     * The window is iconified
     */
    ICONIFIED, 
    /**
     * The window is maximized horizontally
     */
    MAXIMIZED_HORIZ, 
    /**
     * The window is maximized vertically
     */
    MAXIMIZED_VERT;

    /**
     * get the state strings
     * @param set window state
     * @return state strings
     */
    public static List<String> 
    getStateStrings(EnumSet<BlackenWindowState> set) {
        List<String> ret = new ArrayList<String>();
        for (BlackenWindowState s : set) {
            ret.add(s.name());
        }
        return ret;
    }

    /**
     * get the state string
     * @param set window state
     * @return state string
     */
    public static StringBuffer getStateString(EnumSet<BlackenWindowState> set) {
        List<String> base = getStateStrings(set);
        if (base == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        for (String name : base) {
            if (buf.length() > 0) {
                buf.append('|');
            }
            buf.append(name);
        }
        if (buf.length() == 0) {
            buf.append("(none)"); //$NON-NLS-1$
        }
        return buf;
    }

}
