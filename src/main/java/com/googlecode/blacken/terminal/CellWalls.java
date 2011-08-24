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

/**
 * The cell walls.
 * 
 * @author yam655
 */
public enum CellWalls {
    /**
     * Cell wall
     */
    TOP,
    /**
     * Cell wall
     */
    LEFT,
    /**
     * Cell wall
     */
    BOTTOM,
    /**
     * Cell wall
     */
    RIGHT,
    /**
     * Cell wall
     */
    CELL_WALLS_CENTER_TOP,
    /**
     * Cell wall
     */
    CELL_WALLS_CENTER_LEFT,
    /**
     * Cell wall
     */
    CELL_WALLS_CENTER_BOTTOM,
    /**
     * Cell wall
     */
    CELL_WALLS_CENTER_RIGHT;
    /**
     * Cell wall
     */
    public static final EnumSet<CellWalls> CELL_WALLS_HORIZONTAL = 
        EnumSet.of(CELL_WALLS_CENTER_LEFT, CELL_WALLS_CENTER_RIGHT);
    /**
     * Cell wall
     */
    public static final EnumSet<CellWalls> CELL_WALLS_VERTICAL = 
        EnumSet.of(CELL_WALLS_CENTER_TOP, CELL_WALLS_CENTER_BOTTOM);
}
