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

import com.googlecode.blacken.core.ConstrainedList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.blacken.core.Random;
import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.BoxRegionIterator;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.RegionIterator;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.grid.SimpleSize;
import com.googlecode.blacken.grid.Sizable;
import java.util.Collection;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A generic room-like object.
 * 
 * <p>Rooms exist with dimensions and with stuff. Though the room has stuff,
 * the stuff does not have a position other than simply "inside the room." 
 * 
 * <p>For Roguelike games this primarily is useful when creating the dungeon.
 *
 * <p>A room's floor space is considered to consist of zero or more independent
 * layers which can all contain stuff. The implication is that each of the
 * layers can exist independently, however in practice this is rarely the case.
 * We would rarely want initial placement of a monster to be on a terrain which
 * can kill it, or initial placement of an item on terrain which will destroy
 * it.
 *
 * @author Steven Black
 */
public class Room implements Regionlike {
    static private final Logger LOGGER = LoggerFactory.getLogger(Room.class);
    protected int height;
    protected int width;
    protected int x;
    protected int y;
    protected Map<String, ConstrainedList<Object>> stuff;
    protected Map<String, RoomSize> stuffSize;
    protected int floorSpace;
    protected boolean isDug = false;
    protected Set<Positionable> doors;

    public Room() {
        this.height = 0;
        this.width = 0;
        this.x = 0;
        this.y = 0;
        floorSpace = 0;
    }
    /**
     * Normally called from a factory so the containers can be taken care of.
     * @param region 
     */
    public Room(Regionlike region) {
        this.height = region.getHeight();
        this.width = region.getWidth();
        this.x = region.getX();
        this.y = region.getY();
        floorSpace = (height - 2) * (width - 2);
    }

    /**
     * Get the current floor space
     *
     * <p>The floor space is the inside of the room. This is not the map area
     * the room fills, as the room walls are not zero-width, so a room (like all
     * {@link Regionlike} objects) consists of an inside, an outside, and an
     * edge. The room walls are the edge. The floor space is the inside.
     *
     * @return number of floor squares present
     */
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

    public <T> List<T> getContainer(String key, Class<T> expectedType) {
        ConstrainedList<Object> box = this.stuff.get(key);
        if (!expectedType.isAssignableFrom(box.getContainedClass())) {
            throw new ClassCastException("container does not have that type");
        }
        return (ConstrainedList<T>)box;
    }

    public List<Object> getContainer(String key) {
        return stuff.get(key);
    }
    public Collection<ConstrainedList<Object>> getAllContents() {
        return Collections.unmodifiableCollection(stuff.values());
    }

    public void createContainer(String key, Class acceptedType, RoomSize size) {
        if (stuff == null) {
            stuff = new LinkedHashMap<>();
        }
        ConstrainedList<Object> box = new ConstrainedList(acceptedType);
        switch (size) {
            case NO_LIMIT:
                box.setMaxSize(0);
                break;
            case ROOM_LIMIT:
                box.setMaxSize(floorSpace);
                break;
            case ONE:
                box.setMaxSize(1);
                break;
            case TWO:
                box.setMaxSize(2);
                break;
            case THREE:
                box.setMaxSize(3);
                break;
            default:
                throw new UnsupportedOperationException("Unknown value: " + size.name());
        }
        stuff.put(key, box);
        this.stuffSize.put(key, size);
    }

    public void assignToContainer(Object thing ){
        boolean done = false;
        for (ConstrainedList what : stuff.values()) {
            if (what.getContainedClass().isAssignableFrom(thing.getClass())) {
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
        for (String name : stuff.keySet()) {
            ConstrainedList box = stuff.get(name);
            if (stuffSize.get(name).equals(RoomSize.ROOM_LIMIT)) {
                box.setMaxSize(floorSpace);
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
        this.resizeContainers(height * width);
    }

    @Override
    public void setSize(Sizable size) {
        this.height = size.getHeight();
        this.width = size.getWidth();
        this.resizeContainers(height * width);
    }

    public Positionable findBestDoorPosition(Regionlike region) {
        return findBestDoorPosition(Point.centerOfRegion(region));
    }

    public Positionable findBestDoorPosition(Point point) {
        int x1 = this.getX();
        int y1 = this.getY();
        int y2 = this.getY() + this.getHeight() -1; // inclusive
        int x2 = this.getX() + this.getWidth() -1;
        int xa = point.getX();
        int ya = point.getY();
        int xd = 0;
        int yd = 0;
        if (xa < x1) {
            xd = xa - x1;
        } else if (xa > x2) {
            xd = xa - x2;
        }
        if (ya < y1) {
            yd = ya - y1;
        } else if (ya > y2) {
            yd = ya - y2;
        }
        if (yd == 0 && xd == 0) {
            // it is already inside.
            return null;
        }
        BoxRegion ret;
        if (Math.abs(yd) > Math.abs(xd)) {
            if (yd > 0) {
                ret = new BoxRegion(y2, x1+1, 1, width-2);
            } else {
                ret = new BoxRegion(y1, x1+1, 1, width-2);
            }
        } else {
            if (xd > 0) {
                ret = new BoxRegion(y1+1, x2, height-2, 1);
            } else {
                ret = new BoxRegion(y1+1, x1, height-2, 1);
            }
        }
        for (Positionable door : this.doors) {
            if (ret.contains(door)) {
                return new BoxRegion(door);
            }
        }
        return Point.centerOfRegion(ret);
    }

    public boolean hasDoor(Positionable p) {
        return this.doors.contains(p);
    }

    public boolean addDoor(Positionable p) {
        if (!this.contains(p)) {
            return false;
        }
        return this.doors.add(new Point(p));
    }

    @Override
    public boolean contains(int[] location) {
        return contains(location[0], location[1]);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.height;
        hash = 67 * hash + this.width;
        hash = 67 * hash + this.x;
        hash = 67 * hash + this.y;
        hash = 67 * hash + Objects.hashCode(this.stuff);
        hash = 67 * hash + (this.isDug ? 1 : 0);
        hash = 67 * hash + Objects.hashCode(this.doors);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        if (this.height != other.height) {
            return false;
        }
        if (this.width != other.width) {
            return false;
        }
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (!Objects.equals(this.stuff, other.stuff)) {
            return false;
        }
        if (this.isDug != other.isDug) {
            return false;
        }
        if (!Objects.equals(this.doors, other.doors)) {
            return false;
        }
        return true;
    }

    void removeDoor(Positionable door) {
        this.doors.remove(door);
    }

    public boolean isDug() {
        return isDug;
    }

    public void setDug(boolean isDug) {
        this.isDug = isDug;
    }

}
