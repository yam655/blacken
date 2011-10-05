/* blacken - a library for Roguelike games
 * Copyright © 2011 Steven Black <yam655@gmail.com>
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
 * This is an implementation of Sizable so it can be returned by things.
 * 
 * @author yam655
 */
public class SimpleSize implements Sizable {
    private int height;
    private int width;
    /**
     * Crate a new simple size
     * 
     * @param size_y the height
     * @param size_x the width
     */
    public SimpleSize(int size_y, int size_x) {
        this.height = size_y;
        this.width = size_x;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.grid.Sizable#getHeight()
     */
    @Override
    public int getHeight() {
        return height;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.grid.Sizable#getWidth()
     */
    @Override
    public int getWidth() {
        return width;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.grid.Sizable#setHeight(int)
     */
    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.grid.Sizable#setWidth(int)
     */
    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.grid.Sizable#setSize(int, int)
     */
    @Override
    public void setSize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.grid.Sizable#setSize(com.googlecode.blacken.grid.Sizable)
     */
    @Override
    public void setSize(Sizable size) {
        this.height = size.getHeight();
        this.width = size.getWidth();
    }

    /**
     * Produce a string representation
     */
    @Override
    public String toString() {
        return String.format("Size:(h: %s, w: %s)",  //$NON-NLS-1$
                             height, width);
    }
}
