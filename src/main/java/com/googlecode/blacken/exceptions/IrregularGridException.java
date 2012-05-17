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
 * An operation was performed that is disallowed due to the regularity of 
 * the grid.
 * 
 * <p>This can either be an operation on an irregular grid which is disallowed
 * because it is an irregular grid, or an operation on a regular grid which is
 * only allowed on irregular grids.</p>
 * 
 * @author yam655
 */
public class IrregularGridException extends IllegalArgumentException {
    private static final long serialVersionUID = 2473129069282045073L;

    /**
     * Grid regularity exception.
     */
    public IrregularGridException() {
        // do nothing
    }

    /**
     * Grid regularity exception.
     * @param arg0 descriptive message
     */
    public IrregularGridException(String arg0) {
        super(arg0);
    }

    /**
     * Grid regularity exception.
     * @param arg0 cause
     */
    public IrregularGridException(Throwable arg0) {
        super(arg0);
    }

    /**
     * Grid regularity exception.
     * @param arg0 descriptive message
     * @param arg1 cause
     */
    public IrregularGridException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
