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
package com.googlecode.blacken.exceptions;

/**
 * A complex character showed up where only simple characters were expected.
 * 
 * @author yam655
 */
public class ComplexCharacterException extends Exception {

    private static final long serialVersionUID = 4389421563337664791L;

    /**
     * Illegal use of complex character.
     */
    public ComplexCharacterException() {
        super();
    }

    /**
     * Illegal use of complex character.
     * 
     * @param arg0 descriptive message
     * @param arg1 cause
     */
    public ComplexCharacterException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Illegal use of complex character.
     * 
     * @param arg0 descriptive message
     */
    public ComplexCharacterException(String arg0) {
        super(arg0);
    }

    /**
     * Illegal use of complex character.
     * 
     * @param arg0 cause
     */
    public ComplexCharacterException(Throwable arg0) {
        super(arg0);
    }

}
