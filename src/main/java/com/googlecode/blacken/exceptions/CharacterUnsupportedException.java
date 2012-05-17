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
 * An unsupported codepoint has been requested.
 * 
 * @author yam655
 *
 */
public class CharacterUnsupportedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 3138235441574739078L;

	/**
	 * The codepoint is unsupported.
	 */
	public CharacterUnsupportedException() {
		super();
	}

	/**
	 * The codepoint is unsupported.
	 * @param message descriptive message
	 */
	public CharacterUnsupportedException(String message) {
		super(message);
	}

	/**
	 * The codepoint is unsupported.
	 * @param cause the cause
	 */
	public CharacterUnsupportedException(Throwable cause) {
		super(cause);
	}

	/**
         * The codepoint is unsupported.
         * @param message descriptive message
	 * @param cause the cause
	 */
	public CharacterUnsupportedException(String message, Throwable cause) {
		super(message, cause);
	}

}
