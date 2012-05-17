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
 * An unrecoverable exception occurred.
 * 
 * @author yam655
 */
public class UnrecoverableException extends RuntimeException {
    private static final long serialVersionUID = 4239679840891572306L;

    /**
     * An unrecoverable exception occurred.
     */
    public UnrecoverableException() {
        super();
    }

    /**
     * An unrecoverable exception occurred.
     * @param message descriptive message
     */
    public UnrecoverableException(String message) {
        super(message);
    }

    /**
     * An unrecoverable exception occurred.
     * @param cause exception cause
     */
    public UnrecoverableException(Throwable cause) {
        super(cause);
    }

    /**
     * An unrecoverable exception occurred.
     * @param message descriptive message
     * @param cause exception cause
     */
    public UnrecoverableException(String message, Throwable cause) {
        super(message, cause);
    }

}
