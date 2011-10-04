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
package com.googlecode.blacken.grid;

/**
 * Reset the dirty state of a cell.
 * 
 * @author yam655
 *
 * @param <Z> cell type
 */
public interface DirtyGridCell <Z> {
    /**
     * Dirty a cell.
     * 
     * <p>This is used when cells are copied <em>en masse</em> and they need
     * to be refreshed.</p>
     * 
     * @param cell cell to dirty
     * @param dirty true to dirty; false to clean
     */
    public void setDirty(Z cell, boolean dirty);
}
