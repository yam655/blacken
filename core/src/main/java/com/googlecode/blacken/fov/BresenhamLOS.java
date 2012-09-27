/* blacken - a library for Roguelike games
 * Copyright © 2010-2012 Steven Black <yam655@gmail.com>
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

package com.googlecode.blacken.fov;

import com.googlecode.blacken.grid.Bresenham;
import com.googlecode.blacken.grid.Positionable;

public class BresenhamLOS {

    /**
     *  A Bresenham-based line-of-sight algorithm.
     *  Requires the use of the {@link MapLike} interface.
     *  @author = xlambda
     */

    private MapLike map;

    /**
     * Create a Bresenham LOS solver for a fixed {@link MapLike} structure.
     *
     * @param map the map structure for which we want to solve LOS.
     */

    public BresenhamLOS(MapLike map) {
        this.map = map;
    }

    /**
     *  Solve LOS for some given coordinates.
     *  @param y0 starting row
     *  @param x0 starting column
     *  @param y1 ending row
     *  @param x1 ending column
     *  @return true, if (y1,x1) can be seen from (y0,x0), false otherwise
     */

    public boolean solveLOS(int y0, int x0, int y1, int x1) {
        boolean running = true;
        boolean successful = true;
        Bresenham.LineIterator lit = new Bresenham.LineIterator(x0, y0, x1, y1);
        while(running) {
            if(lit.hasNext()) {
                Positionable np = lit.next();
                if(map.blocksFOV(np.getY(), np.getX())) {
                    successful = false;
                    running = false;
                }
            } else {
                running = false;
            }

        }
        return successful;
    }

    /**
     *  Solve LOS for a given pair of {@link Positionable} objects.
     *  @param start starting position
     *  @param end ending position
     *  @return true, if the end Positionable can be seen from the start Positionable
     */

    public boolean solveLOS(Positionable start, Positionable end) {
        return solveLOS(start.getY(), start.getX(), end.getY(), end.getX());
    }


    /**
     *  Set a new {@link MapLike} structure for the LOS solver to work on.
     *  @param map the new map structure
     */

    public void setMapLike(MapLike map) {
        this.map = map;
    }

}