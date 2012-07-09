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

import java.util.Map;

import com.googlecode.blacken.bsp.BSPTree;
import com.googlecode.blacken.core.Random;
import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.RegionIterator;
import com.googlecode.blacken.grid.Regionlike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple dungeon digger supporting a generic Z axis containing
 * a single item.
 *
 * @author Steven Black
 */
public class SimpleDigger<T> {
    private static Logger LOGGER = LoggerFactory.getLogger(SimpleDigger.class);
    private Random rand = Random.getInstance();

    public BSPTree<Room> setup(Regionlike bounds) {
        RoomFactory<Integer> rf = new RoomFactory(null);
        BSPTree<Room> bsp = new BSPTree<>(bounds);
        bsp.splitRecursive(null, 50, 4, 4);
        for (BSPTree node : bsp.findLeaves(null)) {
            node.setContained(rf.createApproximateRoom(node, 200));
        }
        return bsp;
    }
    /**
     * Dig a prepared dungeon, starting with the halls.
     *
     * <p>The fall-back method precludes a lot of selective nulls. Either one of
     * the floors needs to be defined as non-null, or the diggable cell needs
     * to be defined. If there are both no floors and no diggable surface, a
     * NullPointerException is thrown as there is no way to dig anything.</p>
     *
     * <p>Note that the order does matter. We don't go recursive.</p>
     *
     * <dl>
     * <dt>floor</dt>
     * <dd>Default floor. Fallback: "room:floor", "hall:floor", <code>null</code></dd>
     * <dt>room:floor</dt>
     * <dd>Room internal floor. Fallback: "floor"</dd>
     * <dt>hall:floor</dt>
     * <dd>Hall/external floor. Fallback: "floor"</dd>
     * <dt>room:door</dt>
     * <dd>Dividers between room and hall floors. Fallback: "room:floor"</dd>
     * <dt>room:door</dt>
     * <dd>Dividers between hall (and hall) floors. Fallback: "hall:floor"</dd>
     * <dt>diggable<dt>
     * <dd>Diggable identifier. Fallback: <code>null</code></dd>
     * <dt>wall</dt>
     * <dd>Default wall. Fallback: "room:wall", "hall:wall", "diggable"</dd>
     * <dt>room:wall</dt>
     * <dd>Default room wall. Fallback: "wall"</dd>
     * <dt>hall:wall</dt>
     * <dd>Default hall wall. Fallback: "wall"</dd>
     * <dt>room:wall:horizontal</dt>
     * <dd>Default horizontal room wall. Fallback: "top", "bottom", "wall"</dd>
     * <dt>room:wall:top</dt>
     * <dd>Top wall. Fallback: "horizontal"</dd>
     * <dt>room:wall:bottom</dt>
     * <dd>Bottom wall. Fallback: "horizontal"</dd>
     * <dt>room:wall:vertical</dt>
     * <dd>Default vertical room wall. Fallback: "right", "left", "wall"</dd>
     * <dt>room:wall:right</dt>
     * <dd>Top wall. Fallback: "vertical"</dd>
     * <dt>room:wall:left</dt>
     * <dd>Bottom wall. Fallback: "vertical"</dd>
     * <dt>room:wall:top-right<dt>
     * <dd>Top-right corner. Fallback: "room:wall:corner", "room:wall:top"</dd>
     * <dt>room:wall:top-left<dt>
     * <dd>Top-left corner. Fallback: "room:wall:corner", "room:wall:top"</dd>
     * <dt>room:wall:bottom-right<dt>
     * <dd>Top-right corner. Fallback: "room:wall:corner", "room:wall:bottom"</dd>
     * <dt>room:wall:bottom-left<dt>
     * <dd>Top-left corner. Fallback: "room:wall:corner", "room:wall:bottom"</dd>
     * <dt>room:wall:corner</dt>
     * <dd>Generic corner. Fallback: "room:wall"</dd>
     * @param bsp bsp tree of rooms
     * @param grid map to dig
     * @param config mapping configuration
     * @param interruptable
     */
    public void digHallFirst(BSPTree<Room> bsp, Grid<T> grid, Map<String, T> config, boolean interruptable) {
        cleanConfig(config);
        // grid.clear(config.get("diggable"));
        Regionlike last = null;
        for (BSPTree node : bsp.findInOrder(null)) {
            // Why do I need to cast this?
            Regionlike room = (Room)node.getContained();
            if (room == null) {
                LOGGER.info("Node: {}", new BoxRegion(node));
                room = node;
            } else {
                LOGGER.info("Room: {}; Node: {}", new BoxRegion(room), new BoxRegion(node));
            }
            if (last != null) {
                tunnel(grid, Point.centerOfRegion(last), Point.centerOfRegion(room), config, null, interruptable);
            }
            last = room;
        }
        tunnel(grid, Point.centerOfRegion(last), Point.centerOfRegion(bsp), config, null, interruptable);
        for (Room room : bsp.findContained(null)) {
            digRoom(grid, room, config);
        }
    }

    public Positionable placeIt(Grid<T> grid, T empty, Regionlike bounds, T what) {
        Positionable placement = null;
        for (int t=0; t < 10000; t++) {
            int x = rand.nextInt(bounds.getWidth()) + bounds.getX();
            int y = rand.nextInt(bounds.getHeight()) + bounds.getY();
            T cell = grid.get(y, x);
            if (cell == empty || (cell != null && cell.equals(empty))) {
                grid.setCopy(y, x, what);
                placement = new Point(y, x);
                break;
            }
        }
        if (placement == null) {
            throw new RuntimeException("It took too long to place.");
        }
        return placement;
    }

    private boolean digRoom(Grid<T> grid, Room<T> room, Map<String, T> config) {
        boolean intr = false;
        int[] coords = {0,0,0,0};
        RegionIterator itr = room.getInsideIterator();
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
                for(int y = coords[0]; y < coords[2]; y++) {
                    for (int x = coords[1]; x < coords[3]; x++) {
                        T cell = grid.get(y, x);
                        if ((cell == config.get("diggable") || (cell != null && cell.equals(config.get("diggable")))) ||
                                (cell == config.get("hall:wall") || (cell != null && cell.equals(config.get("hall:wall"))))) {
                            if (state) {
                                grid.setCopy(y, x, config.get("room:floor"));
                            } else {
                                grid.setCopy(y, x, config.get("room:wall"));
                            }
                        } else if ((cell == config.get("room:wall") || (cell != null && cell.equals(config.get("room:wall")))) ||
                                (cell == config.get("hall:floor") || (cell != null && cell.equals(config.get("hall:floor"))))) {
                            grid.setCopy(y, x, config.get("room:floor"));
                            intr = true;
                        }
                    }
                }
            } else {
                throw new UnsupportedOperationException("Please implement");
            }
        }
        for (T something : room.getAllContents()) {
            this.placeIt(grid, config.get("room:floor"), room, something);
        }
        itr = room.getEdgeIterator();
        int side = -1;
        while(!itr.isDone()) {
            side++; // 0: top; 1: left; 1: bottom; 2: right
            boolean isHorizontal = coords[2] == coords[0] ? true : false;
            int direction = isHorizontal ? (coords[3] > coords[1] ? +1 : -1) : (coords[2] > coords[0] ? +1 : -1);
            int count = isHorizontal ? coords[3] - coords[1] : coords[2] - coords[0];
            if (count < 0) {
                count *= -1;
            }
            count++;
            int x = coords[1];
            int y = coords[0];
            boolean lastDoor = false;
            boolean lastFloor = false;
            T roomWall = config.get("room:wall");
            if (side == 0) {
                roomWall = config.get("room:wall:top");
            } else if (side == 1) {
                roomWall = config.get("room:wall:left");
            } else if (side == 2) {
                roomWall = config.get("room:wall:bottom");
            } else if (side == 3) {
                roomWall = config.get("room:wall:right");
            } else {
                LOGGER.error("Edge iterator should have ended.");
                break;
            }
            boolean firstOrLast = true;
            while(count-- > 0) {
                if (count == 0) {
                    firstOrLast = true;
                }
                LOGGER.debug("Position: {},{}", y, x);
                T cell = grid.get(y, x);
                if (cell == config.get("diggable") || (cell != null && cell.equals(config.get("diggable")))) {
                    cell = grid.get(y, x + direction);
                    if ((cell == config.get("hall:floor") || (cell != null && cell.equals(config.get("hall:floor")))) ||
                            (cell == config.get("room:floor") || (cell != null && cell.equals(config.get("room:floor")))) ||
                            (cell == config.get("hall:door") || (cell != null && cell.equals(config.get("hall:door")))) ||
                            (cell == config.get("room:door") || (cell != null && cell.equals(config.get("room:door"))))) {
                        if (lastFloor || lastDoor) {
                            grid.setCopy(y, x, config.get("room:floor"));
                            if (lastDoor) {
                                Positionable lastPos;
                                if (isHorizontal) {
                                    lastPos = new Point(y, x + (direction*-1));
                                } else {
                                    lastPos = new Point(y + (direction*-1), x);
                                }
                                grid.setCopy(lastPos, config.get("room:floor"));
                                lastFloor = true;
                                lastDoor = false;
                            }
                        } else {
                            grid.setCopy(y, x, config.get("room:door"));
                            lastDoor = true;
                        }
                        intr = true;
                    } else {
                        if (side == 0 && firstOrLast) {
                            if (count != 0) {
                                grid.setCopy(y, x, config.get("room:wall:top-right"));
                            } else {
                                grid.setCopy(y, x, config.get("room:wall:top-left"));
                            }
                        } else if (side == 2 && firstOrLast) {
                            if (count != 0) {
                                grid.setCopy(y, x, config.get("room:wall:bottom-right"));
                            } else {
                                grid.setCopy(y, x, config.get("room:wall:bottom-left"));
                            }
                        } else {
                            grid.setCopy(y, x, roomWall);
                        }
                    }
                }
                if (isHorizontal) {
                    x+=direction;
                } else {
                    y+=direction;
                }
                firstOrLast = false;
            }
        }
        return intr;
    }

    private boolean tunnel(Grid<T> grid, Point a, Point b,
            Map<String, T> config, Positionable cursor, boolean interruptable) {
        LOGGER.info("tunnel: {} -> {}", a, b);
        int off_y = b.getY() - a.getY();
        int off_x = b.getX() - a.getX();
        if (off_y == 0) {
            return digLine(grid, a.getY(), a.getX(), b.getY(), b.getX(),
                    config, cursor, interruptable);
        } else if (off_x == 0) {
            return digLine(grid, a.getY(), a.getX(), b.getY(), b.getX(),
                    config, cursor, interruptable);
        } else if (rand.nextBoolean()) {
            int half_y = off_y / 2;
            boolean intr = false;
            LOGGER.debug("To dig line? off_y: {}; half_y: {}", off_y, half_y);
            if (digLine(grid, a.getY(), a.getX(), a.getY() + half_y, a.getX(),
                    config, cursor, interruptable)) {
                intr = true;
            }
            if (digLine(grid, a.getY() + half_y, a.getX(),
                    a.getY() + half_y, b.getX(),
                    config, cursor, interruptable)) {
                if (interruptable) {
                    digLine(grid, a.getY() + half_y, b.getX(),
                            a.getY() + half_y, a.getX(), config,
                            cursor, interruptable);
                }
                intr = true;
            }
            if (digLine(grid, b.getY(), b.getX(),
                    a.getY() + half_y, b.getX(),
                    config, cursor, interruptable)) {
                intr = true;
            }
            return intr;
        } else {
            int half_x = off_x / 2;
            boolean intr = false;
            LOGGER.debug("To dig line? off_x: {}; half_x: {}", off_x, half_x);
            if (digLine(grid, a.getY(), a.getX(), a.getY(), a.getX() + half_x,
                    config, cursor, interruptable)) {
                intr = true;
            }
            if (digLine(grid, a.getY(), a.getX() + half_x,
                    b.getY(), a.getX() + half_x,
                    config, cursor, interruptable)) {
                if (interruptable) {
                    digLine(grid, b.getY(), a.getX() + half_x,
                            a.getY(), a.getX() + half_x,
                            config, cursor, interruptable);
                }
                intr = true;
            }
            if (digLine(grid, b.getY(), b.getX(),
                    b.getY(), a.getX() + half_x,
                    config, cursor, interruptable)) {
                intr = true;
            }
            return intr;
        }
    }

    /**
     * <p>The fall-back method precludes a lot of selective nulls. Either one of
     * the floors needs to be defined as non-null, or the diggable cell needs
     * to be defined. If there are both no floors and no diggable surface, a
     * NullPointerException is thrown as there is no way to dig anything.</p>
     *
     * <p>Note that the order does matter. We don't go recursive.</p>
     *
     * <dl>
     * <dt>floor</dt>
     * <dd>Default floor. Fallback: "room:floor", "hall:floor", <code>null</code></dd>
     * <dt>room:floor</dt>
     * <dd>Room internal floor. Fallback: "floor"</dd>
     * <dt>hall:floor</dt>
     * <dd>Hall/external floor. Fallback: "floor"</dd>
     * <dt>room:door</dt>
     * <dd>Dividers between room and hall floors. Fallback: "room:floor"</dd>
     * <dt>room:door</dt>
     * <dd>Dividers between hall (and hall) floors. Fallback: "hall:floor"</dd>
     * <dt>diggable<dt>
     * <dd>Diggable identifier. Fallback: <code>null</code></dd>
     * <dt>wall</dt>
     * <dd>Default wall. Fallback: "room:wall", "hall:wall", "diggable"</dd>
     * <dt>room:wall</dt>
     * <dd>Default room wall. Fallback: "wall"</dd>
     * <dt>hall:wall</dt>
     * <dd>Default hall wall. Fallback: "wall"</dd>
     * <dt>room:wall:horizontal</dt>
     * <dd>Default horizontal room wall. Fallback: "top", "bottom", "wall"</dd>
     * <dt>room:wall:top</dt>
     * <dd>Top wall. Fallback: "horizontal"</dd>
     * <dt>room:wall:bottom</dt>
     * <dd>Bottom wall. Fallback: "horizontal"</dd>
     * <dt>room:wall:vertical</dt>
     * <dd>Default vertical room wall. Fallback: "right", "left", "wall"</dd>
     * <dt>room:wall:right</dt>
     * <dd>Top wall. Fallback: "vertical"</dd>
     * <dt>room:wall:left</dt>
     * <dd>Bottom wall. Fallback: "vertical"</dd>
     * <dt>room:wall:top-right<dt>
     * <dd>Top-right corner. Fallback: "room:wall:corner", "room:wall:top"</dd>
     * <dt>room:wall:top-left<dt>
     * <dd>Top-left corner. Fallback: "room:wall:corner", "room:wall:top"</dd>
     * <dt>room:wall:bottom-right<dt>
     * <dd>Top-right corner. Fallback: "room:wall:corner", "room:wall:bottom"</dd>
     * <dt>room:wall:bottom-left<dt>
     * <dd>Top-left corner. Fallback: "room:wall:corner", "room:wall:bottom"</dd>
     * <dt>room:wall:corner</dt>
     * <dd>Generic corner. Fallback: "room:wall"</dd>
     * @param config Mapping obj
     */
    private void cleanConfig(Map<String, T> config) {
        T a = config.get("floor");
        T b = config.get("room:floor");
        T c = config.get("hall:floor");
        if (a == null) {
            if (b != null) {
                config.put("floor", b);
                a = b;
            } else if (c != null) {
                config.put("floor", c);
                a = c;
            }
        }
        if (b == null) {
            config.put("room:floor", a);
        }
        if (c == null) {
            config.put("hall:floor", a);
        }

        a = config.get("room:door");
        if (a == null) {
            config.put("room:door", config.get("room:floor"));
        }
        a = config.get("hall:door");
        if (a == null) {
            config.put("hall:door", config.get("hall:floor"));
        }

        if (config.get("floor") == null && config.get("diggable") == null) {
            throw new NullPointerException("Cannot perform with all nulls.");
        }

        a = config.get("wall");
        b = config.get("room:wall");
        c = config.get("hall:wall");
        if (a == null) {
            if (b != null) {
                config.put("wall", b);
                a = b;
            } else if (c != null) {
                config.put("wall", c);
                a = c;
            } else {
                a = config.get("diggable");
                config.put("wall", a);
                config.put("room:wall", a);
                config.put("hall:wall", a);
                b = c = a;
            }
        }
        if (b == null) {
            config.put("room:wall", a);
        }
        if (c == null) {
            config.put("hall:wall", a);
        }

        a = config.get("room:wall:horizontal");
        b = config.get("room:wall:top");
        c = config.get("room:wall:bottom");
        if (a == null) {
            if (b != null) {
                config.put("room:wall:horizontal", b);
                a = b;
            } else if (c != null) {
                config.put("room:wall:horizontal", c);
                a = c;
            } else {
                a = config.get("room:wall");
                config.put("room:wall:horizontal", a);
                config.put("room:wall:top", a);
                config.put("room:wall:bottom", a);
                b = c = a;
            }
        }
        if (b == null) {
            config.put("room:wall:top", a);
        }
        if (c == null) {
            config.put("room:wall:bottom", a);
        }

        a = config.get("room:wall:vertical");
        b = config.get("room:wall:right");
        c = config.get("room:wall:left");
        if (a == null) {
            if (b != null) {
                config.put("room:wall:vertical", b);
                a = b;
            } else if (c != null) {
                config.put("room:wall:vertical", c);
                a = c;
            } else {
                a = config.get("room:wall");
                config.put("room:wall:vertical", a);
                config.put("room:wall:right", a);
                config.put("room:wall:left", a);
                b = c = a;
            }
        }
        if (b == null) {
            config.put("room:wall:right", a);
        }
        if (c == null) {
            config.put("room:wall:left", a);
        }

        a = config.get("room:wall:corner");
        if (a == null) {
            a = config.get("room:wall:top");
        }
        b = config.get("room:wall:top-right");
        c = config.get("room:wall:top-left");
        if (b == null) {
            config.put("room:wall:top-right", a);
        }
        if (c == null) {
            config.put("room:wall:top-left", a);
        }
        a = config.get("room:wall:corner");
        if (a == null) {
            a = config.get("room:wall:bottom");
        }
        b = config.get("room:wall:bottom-right");
        c = config.get("room:wall:bottom-left");
        if (b == null) {
            config.put("room:wall:bottom-right", a);
        }
        if (c == null) {
            config.put("room:wall:bottom-left", a);
        }
        a = config.get("room:wall:corner");
        if (a == null) {
            config.put("room:wall:corner", config.get("room:wall"));
        }
    }

    private boolean digLine(Grid<T> grid, int ya, int xa, int yb, int xb,
            Map<String, T> config,
            Positionable cursor, boolean interruptable) {
        LOGGER.info("digging line: {},{}, -> {},{}", new Object[] {ya,xa, yb,xb});
        boolean intr = false;
        boolean isHorizontal = ya == yb ? true : false;
        int direction = isHorizontal ? (xa < xb ? +1 : -1) : (ya < yb ? +1 : -1);
        int count = isHorizontal ? xb - xa : yb - ya;
        if (count < 0) {
            count *= -1;
        }
        count++;
        int x = xa;
        int y = ya;
        while (count-- > 0 && (!interruptable || !intr)) {
            T cell = grid.get(y, x);
            boolean door_mode = false;
            if (cell == config.get("diggable") || (cell != null && cell.equals(config.get("diggable")))) {
                cell = isHorizontal ? grid.get(y, x + direction) : grid.get(y + direction, x);
                if (cell == config.get("room:floor") || (cell != null && cell.equals(config.get("room:floor")))) {
                    grid.setCopy(y, x, config.get("room:door"));
                    door_mode = true;
                    intr = true;
                } else if (cell == config.get("hall:floor") || (cell != null && cell.equals(config.get("hall:floor")))) {
                    grid.setCopy(y, x, config.get("hall:door"));
                    intr = true;
                } else {
                    grid.setCopy(y, x, config.get("hall:floor"));
                }
            } else if (cell == config.get("hall:wall") || (cell != null && cell.equals(config.get("hall:wall")))) {
                cell = isHorizontal ? grid.get(y, x + direction) : grid.get(y + direction, x);
                if (cell == config.get("room:floor") || (cell != null && cell.equals(config.get("room:floor")))) {
                    grid.setCopy(y, x, config.get("room:door"));
                    door_mode = true;
                    intr = true;
                } else if (cell == config.get("hall:floor") || (cell != null && cell.equals(config.get("hall:floor")))) {
                    if (x != xa && x != xb) {
                        intr = true;
                        grid.setCopy(y, x, config.get("hall:door"));
                    } else {
                        grid.setCopy(y, x, config.get("room:floor"));
                    }
                } else {
                    grid.setCopy(y, x, config.get("room:floor"));
                }
            } else {
                intr = true;
            }
            if (config.get("hall:wall") != config.get("diggable") && (config.get("hall:wall") == null || !config.get("hall:wall").equals(config.get("diggable")))) {
                for (int side = -1; side <= 1; side += 2) {
                    if (isHorizontal) {
                        int t = y+side;
                        if (t < grid.getY() || t >= grid.getY() + grid.getHeight()) {
                            continue;
                        }
                    } else {
                        int t = x+side;
                        if (t < grid.getX() || t >= grid.getX() + grid.getWidth()) {
                            continue;
                        }
                    }
                    cell = isHorizontal ? grid.get(y+side, x) : grid.get(y, x+side);
                    if (cell == config.get("diggable") || (cell != null && cell.equals(config.get("diggable")))) {
                        cell = grid.get(y+side, x + direction);
                        if (cell == config.get("room:floor") || (cell != null && cell.equals(config.get("room:floor")))) {
                            if (door_mode) {
                                grid.setCopy(y, x, config.get("room:floor"));
                            }
                            if (isHorizontal) {
                                grid.setCopy(y+side, x, door_mode ? config.get("room:floor") : config.get("room:door"));
                            } else {
                                grid.setCopy(y, x+side, door_mode ? config.get("room:floor") : config.get("room:door"));
                            }
                            intr = true;
                        } else {
                            if (isHorizontal) {
                                grid.setCopy(y+side, x, config.get("hall:wall"));
                            } else {
                                grid.setCopy(y, x+side, config.get("hall:wall"));
                            }
                        }
                    } else if (cell == config.get("hall:wall") || (cell != null && cell.equals(config.get("hall:wall")))) {
                        // do nothing
                    } else {
                        intr = true;
                    }
                }
            }
            if (isHorizontal) {
                x+=direction;
            } else {
                y+=direction;
            }
        }
        if (cursor != null) {
            cursor.setPosition(y, x);
        }
        return intr;
    }

    public Random getRandom() {
        return rand;
    }
    public void setRandom(Random random) {
        rand = random;
    }

}
