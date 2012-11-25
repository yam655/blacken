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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.googlecode.blacken.core.Random;
import com.googlecode.blacken.dungeon.TIMTypes.Itemlike;
import com.googlecode.blacken.dungeon.TIMTypes.Monsterlike;
import com.googlecode.blacken.dungeon.TIMTypes.Terrainlike;
import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.Regionlike;

/**
 *
 * @author Steven Black
 */
public class RoomFactory {
    protected class CheckData {
        CheckData(Class<?> type, RoomSize sizeLimited) {
            this.type = type;
            this.sizeLimited = sizeLimited;
        }
        public Class<?> type;
        public RoomSize sizeLimited;
    }
    protected Map<String, CheckData> checks;
    protected Random rng = Random.getInstance();
    protected Class<? extends Room> roomType = Room.class;

    /**
     * A room factory creating simple single-item rooms.
     * @param simple null to disable type checking
     */
    public RoomFactory(Class<?> simple) {
        checks = new LinkedHashMap<>(1);
        checks.put("simple", new CheckData(simple, RoomSize.NO_LIMIT));
    }

    /**
     * A room factory creating rooms with separate monsters and objects.
     *
     * <p>The implementation is generic, but 'large' items can never coexist
     * on a same square, but 'small' items can (potentially) coexist. In this
     * case 'large' would include terrain features precluding other 'large'
     * things from co-habitating the square.</p>
     *
     * @param large
     * @param small
     * @param havePiles
     */
    public RoomFactory(Class<?> large, Class<?> small, boolean havePiles) {
        checks = new LinkedHashMap<>(2);
        checks.put("large", new CheckData(large, RoomSize.ROOM_LIMIT));
        checks.put("small", new CheckData(small, 
                havePiles ? RoomSize.NO_LIMIT : RoomSize.ROOM_LIMIT));
    }

    /**
     * A room factory creating rooms with separate terrain, monsters and objects.
     *
     * <p>Remember that rooms have no concept of placement within the room.
     * Making sense of what is requested in a room should happen elsewhere.</p>
     *
     * <p>An important note: This may be totally useless due to having no
     * relationship between 'terrain' and 'large' items. Some 'terrain' is
     * incompatible with some 'large' items co-habitating the same square.</p>
     *
     * @param terrain
     * @param large
     * @param small
     * @param havePiles
     */
    public RoomFactory(Class<?> terrain, Class<?> large,
            Class<?> small, boolean havePiles) {
        checks = new LinkedHashMap<>(2);
        checks.put("terrain", new CheckData(terrain, RoomSize.ROOM_LIMIT));
        checks.put("large", new CheckData(large, RoomSize.ROOM_LIMIT));
        checks.put("small", new CheckData(small, 
                havePiles ? RoomSize.NO_LIMIT : RoomSize.ROOM_LIMIT));
    }

    /**
     *
     * @param terrain
     * @param item
     * @param monster
     * @param itemLimit
     */
    public RoomFactory(Class<? extends Terrainlike> terrain, Class<? extends Itemlike> item,
            Class<? extends Monsterlike> monster, RoomSize itemLimit) {
        checks = new LinkedHashMap<>(2);
        checks.put("terrain", new CheckData(terrain, RoomSize.ROOM_LIMIT));
        checks.put("item", new CheckData(item, itemLimit));
        checks.put("monster", new CheckData(monster, RoomSize.ROOM_LIMIT));
    }

    public RoomFactory(Class<?> terrain, Class<?> large,
            Class<?> small, Class<?> flag, boolean havePiles) {
        checks = new LinkedHashMap<>(2);
        checks.put("terrain", new CheckData(terrain, RoomSize.ROOM_LIMIT));
        checks.put("large", new CheckData(large, RoomSize.ROOM_LIMIT));
        checks.put("small", new CheckData(small,
                havePiles ? RoomSize.NO_LIMIT : RoomSize.ROOM_LIMIT));
        checks.put("flag", new CheckData(flag, RoomSize.ONE));
    }

    /**
     * Create a room filling a region.
     * @param region
     * @return
     */
    public Room createRoom(Regionlike region) {
        Room ret = null;
        try {
            ret = roomType.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        ret.setBounds(region);
        if (checks != null) {
            for (Entry<String, CheckData> check : checks.entrySet()) {
                RoomSize sizeLimit = check.getValue().sizeLimited;
                ret.createContainer(check.getKey(), check.getValue().type, sizeLimit);
            }
        }
        return ret;
    }

    /**
     * Create a room mostly filling a region
     *
     * <p>This has a lower limit of 3x3 for the room size. It won't shrink
     * rooms by more than this.</p>
     *
     * @param region general region to create the room
     * @param per percentage of original room to change multiplied to 1000 (0-1000)
     * @return
     */
    public Room createApproximateRoom(Regionlike region, int per) {
        Regionlike r = new BoxRegion(region);
        int meddleX = r.getWidth() * 1000 / per;
        int meddleY = r.getHeight() * 1000 / per;
        if (r.getWidth() < 5) {
            meddleX = 0;
        } else if (r.getWidth() - meddleX < 5) {
            meddleX = r.getWidth() - 5;
        }
        if (r.getHeight() < 5) {
            meddleY = 0;
        } else if (r.getHeight() - meddleY < 5) {
            meddleY = r.getHeight() - 5;
        }
        if (meddleY != 0 || meddleX != 0) {
            int shrinkY = meddleY == 0 ? 0 : rng.nextInt(meddleY) + 1;
            int shrinkX = meddleX == 0 ? 0 : rng.nextInt(meddleX) + 1;
            r.setSize(r.getHeight()-shrinkY, r.getWidth()-shrinkX);
            int moveY = shrinkY == 0 ? 0 : rng.nextInt(shrinkY) + 1;
            int moveX = shrinkX == 0 ? 0 : rng.nextInt(shrinkX) + 1;
            if (moveY != 0 || moveX != 0) {
                r.setPosition(r.getY() + moveY, r.getX() + moveX);
            }
        }
        return createRoom(r);
    }

    public void setRandomNumberGenerator(Random randomNumberGenerator) {
        this.rng = randomNumberGenerator;
    }

    public Random getRandomNumberGenerator() {
        return rng;
    }

    public Class<? extends Room> getRoomType() {
        return roomType;
    }

    public void setRoomType(Class<? extends Room> roomType) {
        this.roomType = roomType;
    }

}
