/* blacken - a library for Roguelike games
 * Copyright © 2012 Steven Black <yam655@gmail.com>
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

package com.googlecode.blacken.dungeon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.BoxRegionIterator;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.RegionIterator;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.grid.SimpleSize;
import com.googlecode.blacken.grid.Sizable;

/**
 *
 * @param <I> item the room can contain
 * @author Steven Black
 */
public class Room<I> implements Regionlike {
    protected int height;
    protected int width;
    protected int x;
    protected int y;
    protected Map<String, Containerlike<I>> stuff;
    protected int floorSpace;

    /**
     * Normally called from a factory so the containers can be taken care of.
     * @param region 
     */
    public Room(Regionlike region) {
        this.height = region.getHeight();
        this.width = region.getWidth();
        this.x = region.getX();
        this.y = region.getY();
        floorSpace = height * width;
    }

    public int getFloorSpace() {
        return floorSpace;
    }

    /**
     * This will throw IllegalStateException if it can't be resized.
     * @param floorSpace
     */
    public void setFloorSpace(int floorSpace) {
        this.resizeContainers(floorSpace);
    }

    public Containerlike<I> getContainer(String key) {
        return stuff.get(key);
    }
    public List<I> getAllContents() {
        List<I> ret = new ArrayList<I>();
        for (Containerlike<I> box : stuff.values()) {
            ret.addAll(box);
        }
        return ret;
    }

    public void assignContainer(String key, Containerlike<I> box) {
        if (stuff == null) {
            stuff = new LinkedHashMap<>();
        }
        stuff.put(key, box);
        if (box.hasSizeLimit()) {
            box.setSizeLimit(floorSpace);
        }
    }

    public void assignToContainer(I thing ){
        boolean done = false;
        for (Containerlike<I> what : stuff.values()) {
            if (what.canFit(thing)) {
                what.add(thing);
                done = true;
                break;
            }
        }
        if (!done) {
            throw new IllegalArgumentException("Cannot fit thing in to any container.");
        }
    }

    /**
     * setSizeLimit will throw IllegalStateException if it can't be resized.
     * @param floorSpace
     */
    private void resizeContainers(int floorSpace) {
        if (floorSpace != -1) {
            this.floorSpace = floorSpace;
        }
        for (Containerlike<I> what : stuff.values()) {
            if (what.hasSizeLimit()) {
                what.setSizeLimit(floorSpace);
            }
        }
    }

    @Override
    public boolean contains(int y, int x) {
        return BoxRegion.contains(this, y, x);
    }

    @Override
    public boolean contains(Positionable p) {
        return contains(p.getY(), p.getX());
    }
    @Override
    public boolean contains(Regionlike r) {
        return BoxRegion.contains(this, r);
    }

    @Override
    public Regionlike getBounds() {
        return this;
    }

    @Override
    public RegionIterator getEdgeIterator() {
        RegionIterator ret = new BoxRegionIterator(this, true, false);
        return ret;
    }

    @Override
    public RegionIterator getInsideIterator() {
        RegionIterator ret = new BoxRegionIterator(this, false, false);
        return ret;
    }
    @Override
    public RegionIterator getNotOutsideIterator() {
        RegionIterator ret = new BoxRegionIterator(this, false, true);
        return ret;
    }

    @Override
    public boolean intersects(Regionlike room) {
        return BoxRegion.intersects(this, room);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
    @Override
    public boolean contains(int height, int width, int y1, int x1) {
        return BoxRegion.contains(this, height, width, y1, x1);
    }

    @Override
    public boolean intersects(int height, int width, int y1, int x1) {
        return BoxRegion.intersects(this, height, width, y1, x1);
    }
    @Override
    public void setHeight(int height) {
        this.height = height;
    }
    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setBounds(Regionlike r) {
        this.setPosition(r);
        this.setSize(r);
    }

    @Override
    public void setBounds(int height, int width, int y, int x) {
        this.setPosition(y, x);
        this.setSize(height, width);
    }

    @Override
    public Positionable getPosition() {
        return new Point(y, x);
    }

    @Override
    public void setPosition(int y, int x) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setPosition(Positionable point) {
        this.y = point.getY();
        this.x = point.getX();
    }

    @Override
    public Sizable getSize() {
        return new SimpleSize(height, width);
    }

    @Override
    public void setSize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public void setSize(Sizable size) {
        this.height = size.getHeight();
        this.width = size.getWidth();
    }

}
