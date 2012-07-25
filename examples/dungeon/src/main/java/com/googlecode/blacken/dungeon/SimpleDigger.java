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
import com.googlecode.blacken.grid.Regionlike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple dungeon digger supporting a generic Z axis containing
 * a single item.
 *
 * @param <T> type of flooring square
 * @author Steven Black
 */
public class SimpleDigger<T> {
    private static Logger LOGGER = LoggerFactory.getLogger(SimpleDigger.class);
    private Random rand = Random.getInstance();

    public BSPTree<Room> setup(Regionlike bounds, Map<String, T> config) {
        RoomFactory<T> rf = new RoomFactory<>(null);
        rf.setConfig(config);
        BSPTree<Room> bsp = new BSPTree<>(bounds);
        bsp.splitRecursive(null, Math.max(2, Math.min(bounds.getHeight(), bounds.getWidth()) / 24), 6, 6);
        for (BSPTree node : bsp.findLeaves(null)) {
            node.setContained(rf.createApproximateRoom(node, 800));
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
        SimpleDungeonConfig.cleanConfig(config);
        // grid.clear(config.get("diggable"));
        Regionlike last = null;
        for (BSPTree<Room> node : bsp.findLeaves(null)) {
            Regionlike room = node.getContained();
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
            LOGGER.info("Digging room {}", new BoxRegion(room));
            room.dig(grid);
        }
    }
    public void digRoomAvoidanceHalls(BSPTree<Room> bsp, Grid<T> grid, Map<String, T> config) {
        SimpleDungeonConfig.cleanConfig(config);
        for (Room room : bsp.findContained(null)) {
            room.dig(grid);
        }
        Room first = null;
        Room last = null;
        for (Room room : bsp.findContained(null)) {
            if (first == null) {
                first = room;
            }
            if (last != null) {
                LOGGER.info("Room: {}; Last: {}", new BoxRegion(room), new BoxRegion(last));
                avoidanceHall(grid, last, room, config, null);
            }
            last = room;
        }
        LOGGER.info("Room: {}; Last: {}", new BoxRegion(last), new BoxRegion(first));
        avoidanceHall(grid, last, first, config, null);
    }

    private boolean avoidanceDig(Grid<T> grid, int ya, int xa, int yb, int xb, Map<String, T> config, Positionable cursor) {
        return digLine(grid, ya, xa, yb, xb, config, cursor, false);
    }

    private boolean avoidanceHall(Grid<T> grid, Room rooma, Room roomb,
            Map<String, T> config, Positionable cursor) {
        Positionable a = rooma.findBestDoorPosition(rooma);
        Positionable b = roomb.findBestDoorPosition(roomb);
        LOGGER.info("tunnel: {} -> {}", a, b);
        int off_y = b.getY() - a.getY();
        int off_x = b.getX() - a.getX();
        if (off_y == 0) {
            return avoidanceDig(grid, a.getY(), a.getX(), b.getY(), b.getX(),
                    config, cursor);
        } else if (off_x == 0) {
            return avoidanceDig(grid, a.getY(), a.getX(), b.getY(), b.getX(),
                    config, cursor);
        } else if (rand.nextBoolean()) {
            int half_y = off_y / 2;
            boolean intr = false;
            LOGGER.debug("To dig line? off_y: {}; half_y: {}", off_y, half_y);
            if (avoidanceDig(grid, a.getY(), a.getX(), a.getY() + half_y, a.getX(),
                    config, cursor)) {
                intr = true;
            }
            if (avoidanceDig(grid, a.getY() + half_y, a.getX(),
                    a.getY() + half_y, b.getX(),
                    config, cursor)) {
                intr = true;
            }
            if (avoidanceDig(grid, b.getY(), b.getX(),
                    a.getY() + half_y, b.getX(),
                    config, cursor)) {
                intr = true;
            }
            return intr;
        } else {
            int half_x = off_x / 2;
            boolean intr = false;
            LOGGER.debug("To dig line? off_x: {}; half_x: {}", off_x, half_x);
            if (avoidanceDig(grid, a.getY(), a.getX(), a.getY(), a.getX() + half_x,
                    config, cursor)) {
                intr = true;
            }
            if (avoidanceDig(grid, a.getY(), a.getX() + half_x,
                    b.getY(), a.getX() + half_x,
                    config, cursor)) {
                intr = true;
            }
            if (avoidanceDig(grid, b.getY(), b.getX(),
                    b.getY(), a.getX() + half_x,
                    config, cursor)) {
                intr = true;
            }
            return intr;
        }
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
