/* blacken - a library for Roguelike games
 * Copyright © 2011,2012 Steven Black <yam655@gmail.com>
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
package com.googlecode.blacken.grid;

import java.util.Iterator;

/**
 * 
 * @author yam655
 */
public class Bresenham {

    /**
     * @author yam655
     *
     */
    public static class LineIterator implements Iterator<Positionable>, Iterable<Positionable> {
        private int deltaX;
        private int deltaY;
        private int stepX;
        private int stepY;
        private int err;
        private int err2;
        private boolean start;
        private int y0, x0;
        private int y1, x1;

        /**
         * Walk a Bresenham line (using Postionable objects)
         * 
         * @param start starting position
         * @param end ending position
         */
        public LineIterator(Positionable start, Positionable end) {
            this.y0 = start.getY();
            this.x0 = start.getX();
            this.y1 = end.getY();
            this.x1 = end.getX();
            finishSetup();
        }
        
        /**
         * Walk a Bresenham Line
         * 
         * @param y0 starting row
         * @param x0 starting column
         * @param y1 ending row
         * @param x1 ending column
         */
        public LineIterator(int y0, int x0, int y1, int x1) {
            this.y0 = y0;
            this.x0 = x0;
            this.y1 = y1;
            this.x1 = x1;
            finishSetup();
        }
        
        private void finishSetup() {
            deltaX = Math.abs(x1-x0);
            deltaY = Math.abs(y1-y0);
            stepX = x0<x1 ? 1 : -1;
            stepY = y0<y1 ? 1 : -1; 
            err = (deltaX>deltaY ? deltaX : -deltaY)/2;
            start = true;
        }
        
        /* (non-Javadoc)
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            if (start || x0 != x1 || y0 != y1) return true;
            return false;
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#next()
         */
        @Override
        public Positionable next() {
            Positionable ret;
            if (start) {
                start = false;
            } else {
                err2 = err;
                if (err2 >-deltaX) { 
                    err -= deltaY; 
                    x0 += stepX; 
                }
                if (err2 < deltaY) { 
                    err += deltaX; 
                    y0 += stepY; 
                }
            }
            ret = new Point(y0, x0);
            return ret;
        }

        /**
         * Remove is not supported.
         * @throws UnsupportedOperationException always thrown
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /* (non-Javadoc)
         * @see java.lang.Iterable#iterator()
         */
        @Override
        public Iterator<Positionable> iterator() {
            return this;
        }
        
    }
    
    /**
     * Create a line on a grid
     * 
     * @param <Z> cell type
     * @param grid grid of cells
     * @param y0 starting row
     * @param x0 starting column
     * @param y1 ending row
     * @param x1 ending column
     * @param cell cell contents
     */
    public static <Z> void line(Grid<Z> grid, int y0, int x0, int y1, int x1, Z cell) {
        for (Positionable pos : new LineIterator(y0, x0, y1, x1)) {
            grid.set(pos, cell);
        }
    }

    /**
     * Create a line on a grid (using Positionables)
     * 
     * @param <Z> cell type
     * @param grid grid of cells
     * @param start starting position
     * @param end ending position
     * @param cell cell contents
     */
    public static <Z> void line(Grid<Z> grid, Positionable start, Positionable end, Z cell) {
        for (Positionable pos : new LineIterator(start, end)) {
            grid.set(pos, cell);
        }
    }
    
}
