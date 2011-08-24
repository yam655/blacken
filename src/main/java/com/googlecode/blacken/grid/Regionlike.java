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
 * A two-dimensional region.
 * 
 * @author yam655
 *
 */
public interface Regionlike extends Positionable {
    /**
     * Is the coordinate in the region?
     * 
     * @param y coordinate
     * @param x coordinate
     * @return true on success; false on failure
     */
    public boolean contains(int y, int x);
    /**
     * Is the box in the region?
     * 
     * @param height height of box
     * @param width width of box
     * @param y1 starting Y coordinate
     * @param x1 starting X coordinate
     * @return true on success; false on failure
     */
    public boolean contains(int height, int width, int y1, int x1);
    /**
     * Is the positionable item in the region?
     * 
     * @param p postionable item
     * @return true on success; false on failure
     */
    public boolean contains(Positionable p);
    /**
     * Is the region in the region?
     * 
     * @param r region to check
     * @return true on success; false on failure
     */
    public boolean contains(Regionlike r);
    /**
     * Get the bounds of the region
     * @return bounds, expressed in a regionlike
     */
    public Regionlike getBounds();
    /**
     * Get the edge iterator.
     * @return iterator.
     */
    public RegionIterator getEdgeIterator();
    /**
     * Get the height
     * @return height
     */
    public int getHeight();
    /**
     * Get the inside iterator
     * @return iterator
     */
    public RegionIterator getInsideIterator();
    /**
     * Get the "not outside" iterator.
     * 
     * <p>The not-outside iterator is literally everything not outside.
     * This includes both the edge and the inside.</p>
     * 
     * @return iterator
     */
    public RegionIterator getNotOutsideIterator();
    /**
     * Get the width.
     * @return width
     */
    public int getWidth();
    /**
     * Does the box intersect with the region?
     * 
     * <p>An intersection is both inside and outside a region.</p>
     * 
     * @param height height of box
     * @param width width of box
     * @param y1 coordinate
     * @param x1 coordinate
     * @return true on success; false on failure
     */
    public boolean intersects(int height, int width, int y1, int x1);
    /**
     * Does the region intersect the region?
     * 
     * @param room intersecting region
     * @return true on success; false on failure
     */
    public boolean intersects(Regionlike room);
    /**
     * Set the height.
     * @param height the height
     */
    public void setHeight(int height);
    /**
     * Set the width.
     * @param width the width
     */
    public void setWidth(int width);
}
