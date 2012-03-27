/* blacken - a library for Roguelike games
 * Copyright © 2011 Steven Black <yam655@gmail.com>
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
