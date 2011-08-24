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
package com.googlecode.blacken.exceptions;

/**
 * This indicates that an invalid string format was found.
 * 
 * @author yam655
 */
public class InvalidStringFormatException extends Exception {
    private static final long serialVersionUID = 1395889187899154754L;

    /**
     * An illegal string format was found.
     */
    public InvalidStringFormatException() {
        super();
    }

    /**
     * An illegal string format was found.
     * @param arg0 descriptive message
     */
    public InvalidStringFormatException(String arg0) {
        super(arg0);
    }

    /**
     * An illegal string format was found.
     * @param arg0 cause
     */
    public InvalidStringFormatException(Throwable arg0) {
        super(arg0);
    }

    /**
     * An illegal string format was found.
     * @param arg0 descriptive message
     * @param arg1 cause
     */
    public InvalidStringFormatException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
