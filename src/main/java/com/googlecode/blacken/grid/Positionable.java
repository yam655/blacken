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
 * A positional interface.
 * 
 * @author yam655
 */
public interface Positionable {
    /**
     * Get the X coordinate
     * @return the coordinate
     */
    public int getX();
    /**
     * Get the Y coordinate
     * @return the coordinate
     */
    public int getY();
    /**
     * Set the X coordinate
     * @param x the coordinate
     */
    public void setX(int x);
    /**
     * Set the Y coordinate
     * @param y the coordinate
     */
    public void setY(int y);
    /**
     * Returns an array containing the X and Y positions in that order.
     * 
     * @return [x, y]
     */
    public int[] getPos();
    /**
     * Set the position.
     * 
     * @param y new coordinate
     * @param x new coordinate
     */
    public void setPos(int y, int x);
}
