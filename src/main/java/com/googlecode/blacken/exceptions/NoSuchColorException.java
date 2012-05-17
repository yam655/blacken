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
 * The requested color key does not exist.
 * 
 * @author yam655
 */
public class NoSuchColorException extends Exception {

    /**
     * The serialVersionUID is required by Serializable objects
     */
    private static final long serialVersionUID = -4372977339401691848L;

    /**
     * Thrown when a color is looked up which does not exist
     */
    public NoSuchColorException() {
        super();
    }

    /**
     * The serialVersionUID is required by Serializable objects
     * 
     * @param message
     *            Description of the problem
     * @param cause
     *            Related exception
     */
    public NoSuchColorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * The serialVersionUID is required by Serializable objects
     * 
     * @param message
     *            Description of the problem
     */
    public NoSuchColorException(String message) {
        super(message);
    }

    /**
     * The serialVersionUID is required by Serializable objects
     * 
     * @param cause
     *            Related exception
     */
    public NoSuchColorException(Throwable cause) {
        super(cause);
    }
}
