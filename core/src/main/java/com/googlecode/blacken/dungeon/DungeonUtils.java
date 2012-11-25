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

import com.googlecode.blacken.core.Random;
import com.googlecode.blacken.dungeon.TIMTypes.Itemlike;
import com.googlecode.blacken.dungeon.TIMTypes.Monsterlike;
import com.googlecode.blacken.dungeon.TIMTypes.Renderable;
import com.googlecode.blacken.dungeon.TIMTypes.Terrainlike;
import com.googlecode.blacken.grid.DirtyGridCell;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.RegionIterator;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.terminal.TerminalCellLike;
import com.googlecode.blacken.terminal.TerminalViewInterface;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collection of utility functions for dungeons.
 *
 * @author Steven Black
 */
public class DungeonUtils {
    static private final Logger LOGGER = LoggerFactory.getLogger(DungeonUtils.class);
    /**
     * Move a block of cells between grids.
     *
     * <p>There is different behavior if you're copying from the same Grid or
     * a different Grid. If it is the same Grid, we clear the source location
     * so that you can use this to move blocks around the screen. This is
     * usually what is desired and removes the need to have the caller perform
     * "smear cleanup" before the Grid is presentable to the end-user.
     *
     * <p>If you're copying from a different Grid, however, there's no issue
     * of smear. Copying from a Grid never causes visual smear, so there's no
     * need to perform smear cleanup. The source remains pristine and the
     * destination only gets modified due to the copy from the source.
     *
     * <p>Note that when dealing with the same Grid it sounds like identical
     * logic to what is performed by
     * {@link #moveBlock(int, int, int, int, int, int, DirtyGridCell)}. There's
     * a reason for that. moveBlock() is a convenience function added for
     * semantic clarity in the code.
     *
     * @param to destination terminal-like
     * @param from source grid
     * @param numRows number of rows to move
     * @param numCols number of columns to move
     * @param startY start Y coordinate
     * @param startX start X coordinate
     * @param destY destination Y coordinate
     * @param destX destination X coordinate
     */
    public static void copyFrom(TerminalViewInterface to,
            Grid<Renderable> from, int numRows, int numCols,
                         int startY, int startX, int destY, int destX) {
        for (int y = startY; y < startY + numRows ; y++) {
            if (y < to.getY() || y >= to.getY() + to.getHeight()) {
                continue;
            }
            for (int x = from.getX(); x < from.getX() + from.getWidth(); x++) {
                if (x < to.getX() || x >= to.getX() + to.getWidth()) {
                    continue;
                }
                TerminalCellLike cell = from.get(y, x).getRendering();
                if (cell != null) {
                    to.set(y, x, cell);
                }
            }
        }
    }
    public static void copyFrom(TerminalViewInterface to,
            Grid<Renderable> from) {
        copyFrom(to, from, to.getHeight(), to.getWidth(),
                from.getY(), from.getX(), to.getY(), to.getX());
    }
    public static void copyFrom(TerminalViewInterface to,
            Grid<Renderable> from, int startY, int startX) {
        copyFrom(to, from, to.getHeight(), to.getWidth(), startY, startX,
                to.getY(), to.getX());
    }
    public static void update(TerminalViewInterface view, Grid<Renderable> grid,
            int y, int x) {
        TerminalCellLike cell = grid.get(y, x).getRendering();
        if (cell != null) {
            view.set(y, x, cell);
        }
    }
    private Random rng;

    public <T> boolean assignContents(Grid<TIMCell> grid, Grid<T> flatgrid, Map<String, T> config, Room room) {
        for (Terrainlike t : room.getContainer("terrain", Terrainlike.class)) {
            Positionable pos = this.placeIt(flatgrid, config.get("room:floor"), config.get("thing:other"), room);
            grid.get(pos).setTerrain(t);
        }
        for (Itemlike i : room.getContainer("item", Itemlike.class)) {
            Positionable pos = this.placeIt(flatgrid, config.get("room:floor"), config.get("thing:other"), room);
            grid.get(pos).setItem(i);
        }
        for (Monsterlike m : room.getContainer("monster", Monsterlike.class)) {
            Positionable pos = this.placeIt(flatgrid, config.get("room:floor"), config.get("thing:other"), room);
            grid.get(pos).setMonster(m);
        }
        return false;
    }

    public <T> boolean digRoom(Grid<T> grid, Map<String, T> config, Regionlike region) {
        boolean intr = false;
        int[] coords = {0,0,0,0};
        RegionIterator itr = region.getInsideIterator();
        int patIdx = 0;
        while(!itr.isDone()) {
            int segment = itr.currentSegment(coords);
            boolean[] ptrn = itr.currentPattern();
            if (segment == RegionIterator.SEG_INSIDE_SOLID || segment == RegionIterator.SEG_INSIDE_PATTERNED) {
                boolean state = true;
                if (segment == RegionIterator.SEG_INSIDE_PATTERNED) {
                    state = ptrn[patIdx++];
                    if (patIdx >= ptrn.length) {
                        patIdx = 0;
                    }
                }
                for(int y1 = coords[0]; y1 <= coords[2]; y1++) {
                    for (int x1 = coords[1]; x1 <= coords[3]; x1++) {
                        T cell = grid.get(y1, x1);
                        if ((cell == config.get("diggable") || (cell != null && cell.equals(config.get("diggable")))) ||
                                (cell == config.get("hall:wall") || (cell != null && cell.equals(config.get("hall:wall"))))) {
                            if (state) {
                                grid.setCopy(y1, x1, config.get("room:floor"));
                            } else {
                                grid.setCopy(y1, x1, config.get("room:wall"));
                            }
                        } else if ((cell == config.get("room:wall") || (cell != null && cell.equals(config.get("room:wall")))) ||
                                (cell == config.get("hall:floor") || (cell != null && cell.equals(config.get("hall:floor"))))) {
                            grid.setCopy(y1, x1, config.get("room:floor"));
                            intr = true;
                        }
                    }
                }
            } else if (segment == RegionIterator.SEG_COMPLETE) {
                break;
            } else {
                throw new UnsupportedOperationException("Please implement");
            }
        }
        itr = region.getEdgeIterator();
        int side;
        while(!itr.isDone()) {
            itr.currentSegment(coords);
            itr.next();
            //boolean[] ptrn = itr.currentPattern();
            boolean isHorizontal = coords[2] == coords[0] ? true : false;
            int direction = isHorizontal ? (coords[3] > coords[1] ? +1 : -1) : (coords[2] > coords[0] ? +1 : -1);
            int count = isHorizontal ? coords[3] - coords[1] : coords[2] - coords[0];
            if (count < 0) {
                count *= -1;
            }
            count++;
            int x0 = coords[1];
            int y0 = coords[0];
            boolean lastDoor = false;
            boolean lastFloor = false;
            T roomWall;
            //LOGGER.debug("Coords: {}, isHoriz?: {}", coords, isHorizontal);
            if (coords[2] == coords[0] && coords[3] == coords[1]) {
                side = -1;
                roomWall = config.get("room:wall");
            } else if (coords[2] == coords[0]) {
                if (coords[0] == region.getY()) {
                    side = 0;
                    roomWall = config.get("room:wall:top");
                } else {
                    roomWall = config.get("room:wall:bottom");
                    side = 2;
                }
            } else {
                if (coords[1] == region.getX()) {
                    side = 1;
                    roomWall = config.get("room:wall:left");
                } else {
                    side = 3;
                    roomWall = config.get("room:wall:right");
                }
            }
            boolean firstOrLast = true;
            while(count-- > 0) {
                if (count == 0) {
                    firstOrLast = true;
                }
                //if (!isHorizontal) {
                //    LOGGER.debug("Position: {},{}", y0, x0);
                //}
                T cell = grid.get(y0, x0);
                if ((cell == config.get("diggable") || (cell != null && cell.equals(config.get("diggable")))) ||
                        (cell == config.get("hall:wall") || (cell != null && cell.equals(config.get("hall:wall"))))) {
                    /*
                    if (count == 0) {
                        // cell = config.get("diggable"); // still set
                    } else if (isHorizontal) {
                        cell = grid.get(y0, x0 + direction);
                    } else {
                        cell = grid.get(y0 + direction, x0);
                    }
                    if ((cell == config.get("hall:floor") || (cell != null && cell.equals(config.get("hall:floor")))) ||
                            (cell == config.get("room:floor") || (cell != null && cell.equals(config.get("room:floor")))) ||
                            (cell == config.get("hall:door") || (cell != null && cell.equals(config.get("hall:door")))) ||
                            (cell == config.get("room:door") || (cell != null && cell.equals(config.get("room:door"))))) {
                        if (lastFloor || lastDoor) {
                            grid.setCopy(y0, x0, config.get("room:floor"));
                            if (lastDoor) {
                                Positionable lastPos;
                                if (isHorizontal) {
                                    lastPos = new Point(y0, x0 + (direction*-1));
                                } else {
                                    lastPos = new Point(y0 + (direction*-1), x0);
                                }
                                grid.setCopy(lastPos, config.get("room:floor"));
                                lastFloor = true;
                                lastDoor = false;
                            }
                        } else {
                            grid.setCopy(y0, x0, config.get("room:door"));
                            lastDoor = true;
                        }
                        grid.setCopy(y0, x0, config.get("room:floor"));
                        intr = true;
                    } else {*/
                        // LOGGER.debug("Position: {},{}", y0, x0);
                        if (side == 0 && firstOrLast) {
                            if (count == 0) {
                                grid.setCopy(y0, x0, config.get("room:wall:top-right"));
                            } else {
                                grid.setCopy(y0, x0, config.get("room:wall:top-left"));
                            }
                        } else if (side == 2 && firstOrLast) {
                            if (count == 0) {
                                grid.setCopy(y0, x0, config.get("room:wall:bottom-right"));
                            } else {
                                grid.setCopy(y0, x0, config.get("room:wall:bottom-left"));
                            }
                        } else {
                            grid.setCopy(y0, x0, roomWall);
                        }
                    //}
                } else if ((cell == config.get("hall:floor") || (cell != null && cell.equals(config.get("hall:floor")))) ||
                        (cell == config.get("room:floor") || (cell != null && cell.equals(config.get("room:floor"))))) {
                    if (lastFloor || lastDoor) {
                        grid.setCopy(y0, x0, config.get("room:floor"));
                        if (lastDoor) {
                            Positionable lastPos;
                            if (isHorizontal) {
                                lastPos = new Point(y0, x0 + (direction*-1));
                            } else {
                                lastPos = new Point(y0 + (direction*-1), x0);
                            }
                            grid.setCopy(lastPos, config.get("room:floor"));
                            lastFloor = true;
                            lastDoor = false;
                        }
                    } else {
                        grid.setCopy(y0, x0, config.get("room:door"));
                        lastDoor = true;
                    }
                    intr = true;
                }
                if (isHorizontal) {
                    x0+=direction;
                } else {
                    y0+=direction;
                }
                firstOrLast = false;
            }
        }
        return intr;
    }

    public <T> Positionable findLocation(Grid<T> grid, Set<T> empties, Regionlike r) {
        Positionable placement = null;
        for (int t=0; t < 10 && placement == null; t++) {
            int x1 = rng.nextInt(r.getWidth()) + r.getX();
            int y1 = rng.nextInt(r.getHeight()) + r.getY();
            T cell = grid.get(y1, x1);
            if (empties.contains(cell)) {
                placement = new Point(y1, x1);
            }
        }
        if (placement == null) {
            Point rowadd;
            Point coladd;
            int x0 = rng.nextInt(r.getWidth()) + r.getX();
            int y0 = rng.nextInt(r.getHeight()) + r.getY();
            if (rng.nextBoolean()) {
                if (rng.nextBoolean()) {
                    rowadd = new Point(1, 0);
                    coladd = new Point(0, 1);
                } else {
                    rowadd = new Point(-1, 0);
                    coladd = new Point(0, -1);
                }
            } else {
                if (rng.nextBoolean()) {
                    rowadd = new Point(0, 1);
                    coladd = new Point(1, 0);
                } else {
                    rowadd = new Point(0, -1);
                    coladd = new Point(-1, 0);
                }
            }
            Point start = new Point(y0, x0);
            Point cur = new Point(start);
            int y2 = r.getY() + r.getHeight()-1;
            int x2 = r.getX() + r.getWidth()-1;
            do {
                cur.add(coladd);
                // column wrap
                if (cur.getY() > y2) {
                    cur.add(rowadd);
                    cur.setY(r.getY());
                } else if (cur.getX() > x2) {
                    cur.add(rowadd);
                    cur.setX(r.getX());
                } else if (cur.getY() < r.getY()) {
                    cur.add(rowadd);
                    cur.setY(y2);
                } else if (cur.getX() < r.getX()) {
                    cur.add(rowadd);
                    cur.setX(x2);
                }
                // row wrap
                if (cur.getY() > y2) {
                    cur.setY(r.getY());
                } else if (cur.getX() > x2) {
                    cur.setX(r.getX());
                } else if (cur.getY() < r.getY()) {
                    cur.setY(y2);
                } else if (cur.getX() < r.getX()) {
                    cur.setX(x2);
                }
                T cell = grid.get(cur);
                LOGGER.debug("Checking {} in {}", cell, empties);
                if (empties.contains(cell)) {
                    placement = new Point(cur);
                }
            } while(placement == null && !cur.equals(start));
        }
        return placement;
    }

    /**
     * Place a thing and throw an exception if there's not space for it.
     * @param grid
     * @param empty
     * @param what
     * @return location used
     */
    public <T> Positionable placeThing(Grid<T> grid, Room room, T empty, T what) {
        if (!room.isDug) {
            throw new RuntimeException("room must be dug first");
        }
        room.assignToContainer(what);
        return this.placeIt(grid, empty, what, room);
    }

    public Random getRandom() {
        return rng;
    }

    public void setRandom(Random rng) {
        this.rng = rng;
    }

    /**
     * Place a thing -- slowly if need be -- and return null if impossible.
     * @param grid
     * @param empty
     * @param what
     * @return location used
     */
    private <T> Positionable placeIt(Grid<T> grid, T empty, T what, Regionlike room) {
        Set<T> empties = new HashSet<>(1);
        empties.add(empty);

        Positionable placement = findLocation(grid, empties, room);
        if (placement == null) {
            throw new RuntimeException(
                    String.format(
                    "It took too long to place. (Found %s; looking for %s)",
                    grid.get(grid.getY()+1, grid.getX()+1), empty));
        } else {
            grid.setCopy(placement, what);
        }
        return placement;
    }


    public <T> boolean addDoor(Room room, Map<String, T> config,
            Positionable door, Grid<T> grid) {
        if (!room.addDoor(door)) {
            return false;
        }
        Set<T> walls = SimpleDungeonConfig.findRoomWalls(config);
        if (room.isDug()) {
            T spot = grid.get(door);
            if (walls.contains(spot)) {
                grid.set(door, spot);
            } else if (!spot.equals(config.get("room:door"))) {
                room.removeDoor(door);
                return false;
            }
        }
        return true;
    }

}
