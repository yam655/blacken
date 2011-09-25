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
     * The top cell wall
     */
    TOP,
    /**
     * the left cell wall
     */
    LEFT,
    /**
     * The bottom cell wall
     */
    BOTTOM,
    /**
     * The right cell wall
     */
    RIGHT,
    /**
     * Cell wall
     */
    CENTER_TO_TOP,
    /**
     * Cell wall
     */
    CENTER_TO_LEFT,
    /**
     * Cell wall
     */
    CENTER_TO_BOTTOM,
    /**
     * Cell wall
     */
    CENTER_TO_RIGHT;
    /**
     * Cell wall
     */
    public static final EnumSet<CellWalls> HORIZONTAL = 
        EnumSet.of(CENTER_TO_LEFT, CENTER_TO_RIGHT);
    /**
     * Cell wall
     */
    public static final EnumSet<CellWalls> VERTICAL = 
        EnumSet.of(CENTER_TO_TOP, CENTER_TO_BOTTOM);
    /**
     * Box a cell completely
     */
    public static final EnumSet<CellWalls> BOX =
        EnumSet.of(TOP, LEFT, BOTTOM, RIGHT);
}
