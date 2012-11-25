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

package com.googlecode.blacken.gridbased;

import com.googlecode.blacken.dungeon.Room;
import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.grid.SimpleSize;
import com.googlecode.blacken.grid.Sizable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Steven Black
 */
public class GridDungeon {
    private List<Room> store;
    private final SimpleSize mapSize;
    private final SimpleSize blockSize;
    private int blockRows;
    private int blockCols;

    public Regionlike getBounds() {
        Regionlike ret = new BoxRegion(0,0,0,0);
        ret.setSize(mapSize);
        return ret;
    }
    public Regionlike getBounds(int i) {
        BoxRegion box = new BoxRegion(blockSize.getHeight(), blockSize.getWidth());
        box.setPosition(i / blockCols * blockSize.getHeight(), i % blockCols * blockSize.getWidth());
        return box;
    }

    public int getIndex(int y, int x) {
        y /= blockSize.getHeight();
        x /= blockSize.getWidth();
        int r = y * blockCols + x;
        return r;
    }

    public int getBlockIndex(int y, int x) {
        int r = y * blockCols + x;
        return r;
    }

    public Room get(int y, int x) {
        int i = getIndex(y, x);
        Room r = get(i);
        if (r == null) {
            return null;
        }
        if (r.contains(y, x)) {
            return r;
        }
        return null;
    }

    /**
     * @param blockSize
     * @param mapSize
     * @param factory
     */
    public GridDungeon(Sizable blockSize, Sizable mapSize) {
        this.mapSize = new SimpleSize(mapSize);
        this.blockSize = new SimpleSize(blockSize);
        finishInit();
    }
    private void finishInit() {
        blockRows = mapSize.getHeight() / blockSize.getHeight();
        blockCols = mapSize.getWidth() / blockSize.getWidth();
        store = new ArrayList<>(blockRows * blockCols);
        for (int i = 0; i < blockRows * blockCols; i++) {
            store.add(null);
        }
    }

    public GridDungeon(int blockYSize, int blockXSize, Sizable mapSize) {
        this.mapSize = new SimpleSize(mapSize);
        this.blockSize = new SimpleSize(blockYSize, blockXSize);
        finishInit();
    }

    public int size() {
        return store.size();
    }

    public boolean isEmpty() {
        return store.isEmpty();
    }

    public boolean contains(Room o) {
        return store.contains(o);
    }

    public Room[] toArray() {
        return (Room[]) store.toArray();
    }

    public <T> T[] toArray(T[] ts) {
        return store.toArray(ts);
    }

    public Room set(int idx, Room e) {
        return store.set(idx, e);
    }

    public boolean remove(Room o) {
        int i = store.indexOf(o);
        if (i < 0) {
            return false;
        }
        return store.set(i, null) != null;
    }

    public boolean remove(int idx) {
        return store.set(idx, null) != null;
    }

    public boolean containsAll(Collection<Room> clctn) {
        return store.containsAll(clctn);
    }

    public boolean removeAll(Collection<Room> clctn) {
        boolean ret = false;
        for (Room r : clctn) {
            if (remove(r)) {
                ret = true;
            }
        }
        return ret;
    }

    public boolean retainAll(Collection<?> clctn) {
        boolean ret = false;
        for (Room r : store) {
            if (!clctn.contains(r)) {
                if (remove(r)) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    public void clear() {
        for (int i = 0; i < store.size(); i++) {
            store.set(i, null);
        }
    }

    public Room get(int i) {
        return store.get(i);
    }

    public int indexOf(Object o) {
        return store.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return store.lastIndexOf(o);
    }

    public boolean withinRoom(int y, int x) {
        return get(y, x) != null;
    }

    public Collection<Room> uniqueRooms() {
        Set<Room> s = new HashSet<>(store);
        return s;
    }

    public Collection<Integer> occupiedIndexes() {
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < store.size(); i++) {
            if (store.get(i) != null) {
                ret.add(i);
            }
        }
        return ret;
    }

    public List<Integer> neighborIndexes(int idx) {
        List<Integer> ret = new ArrayList<>();
        Positionable loc = getBlockPosition(idx);
        if (loc.getY() > 0) {
            ret.add(getBlockIndex(loc.getY() - 1, loc.getX()));
        }
        if (loc.getX() > 0) {
            ret.add(getBlockIndex(loc.getY(), loc.getX() - 1));
        }
        if (loc.getY() < blockRows -1) {
            ret.add(getBlockIndex(loc.getY() + 1, loc.getX()));
        }
        if (loc.getX() < blockCols -1) {
            ret.add(getBlockIndex(loc.getY(), loc.getX() + 1));
        }
        return ret;
    }

    private Positionable getBlockPosition(int idx) {
        Positionable p = new Point();
        p.setPosition(idx / blockCols, idx % blockCols);
        return p;
    }
}
