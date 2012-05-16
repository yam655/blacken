/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    /**
     * Produce a string representation.
     */
    @Override
    public String toString() {
        return String.format("Point:(y=%s, x=%s)",
                             this.getY(), this.getX());
    }
}
