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

public interface MapLike {

    /** A simple interface to allow custom map structures to use the Blacken FOV toolkit.
     *  Any map structure implementing it must provide the listed methods for checking wall-ness, indicating a visible tile and determining map size.
     *  @author XLambda
     */

    /** When implemented by the map structure, this method should return whether a given map tile blocks field of view or not.
     *  @param x the x ordinate of the map tile
     *  @param y the y ordinate of the map tile
     *  @return does the tile (x,y) block fov? returns true if yes, false otherwise.
     */

    public boolean blocksFOV(int y, int x);

    /** When implemented by the map structure, this method will be called on all map tiles that are visible according to the FOV algorithm.
     *  @param x the x ordinate of the map tile
     *  @param y the y ordinate of the map tile
     */

    public void touch(int y, int x);

    /** When implemented by the map structure, this method should return the overall height of the map.
     *  @return the map height
     */

    public int getHeight();

    /** When implemented by the map structure, this method should return the overall width of the map.
     *  @return the map width
     */

    public int getWidth();
}