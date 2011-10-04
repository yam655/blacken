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
 * This primarily exists as a concrete type usable to test Positionable.
 * 
 * @author Steven Black
 *
 */
public class Point implements Positionable {

    private int x;
    private int y;

    /**
     * A point
     */
    public Point() {
        x = 0;
        y = 0;
    }

    /**
     * A point with a value.
     * @param y coordinate
     * @param x coordinate
     */
    public Point(int y, int x) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * @param point a point to base this one off of
     */
    public Point(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#getX()
     */
    @Override
    public int getX() {
        return this.x;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#getY()
     */
    @Override
    public int getY() {
        return this.y;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#setX(int)
     */
    @Override
    public void setX(int x) {
        this.x = x;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#setY(int)
     */
    @Override
    public void setY(int y) {
        this.y = y;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#setPos(int, int)
     */
    @Override
    public void setPosition(int y, int x) {
        this.setX(x);
        this.setY(y);
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#setPos(com.googlecode.blacken.grid.Positionable)
     */
    @Override
    public void setPosition(Positionable point) {
        this.setX(point.getX());
        this.setY(point.getY());
    }

}
