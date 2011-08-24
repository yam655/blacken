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
 * A region iterator.
 * 
 * @author yam655
 */
public abstract class RegionIterator {
    /**
     * This indicates you have seen all the segments.
     * 
     * The p1,p2 locations will be undefined.
     */
    public static final int SEG_COMPLETE = -1;
    /**
     * A solid line segment in cells that are border cells.
     * 
     * When a region is bordered every cell in the region can belong to one of
     * three states: outside, inside, or border. A border cell is neither inside
     * the region nor is it outside the region.
     */
    public static final int SEG_BORDER_SOLID = 0;
    /**
     * A patterned line segment in cells that are border cells.
     * 
     * Patterned cells have an repeating array of booleans indicating whether 
     * the cells in the line segment belong or not. To get this pattern you 
     * must call {@link #currentPattern()}.
     * 
     * @see #currentPattern()
     */
    public static final int SEG_BORDER_PATTERNED = 1;
    /**
     * An solid line segment in cells that is inside a region.
     * 
     * Unbordered regions have every cell belonging to one of two states: 
     * inside and outside. This value indicates that the edge cells are also
     * interior cells. Any border that might be visible is zero cells wide.
     * 
     * Again, this means that these cells will also be returned if you ask for
     * inside of a region.
     */
    public static final int SEG_INSIDE_SOLID = 0;
    /**
     * A patterned line segment in cells that that are border cells.
     * 
     * @see #SEG_INSIDE_SOLID
     * @see #SEG_BORDER_PATTERNED
     */
    public static final int SEG_INSIDE_PATTERNED = 1;
    protected RegionIterator() {
        // do nothing
    }
    /**
     * Get the current pattern.
     * 
     * @return the current pattern.
     */
    public abstract boolean[] currentPattern();
    /**
     * Return the current segment.
     * 
     * The return value is the type of segment. All segments are composed of
     * a pair of (y, x) coordinates. All segments are straight lines, so
     * either x1 will equal x2 or y1 will equal y2.
     * 
     * @see #SEG_BORDER_PATTERNED
     * @see #SEG_BORDER_SOLID
     * @see #SEG_INSIDE_PATTERNED
     * @see #SEG_INSIDE_SOLID
     * @param coords int[4] containing y1, x1, y2, x2
     * @return segment type
     */
    public abstract int currentSegment(int[] coords);
    /**
     * Are we done?
     * @return done status
     */
    public abstract boolean isDone();
    /**
     * Move to next segment.
     */
    public abstract void next();
}
