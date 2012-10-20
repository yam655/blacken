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

package com.googlecode.blacken.dungeon.TIMTypes;

/**
 *
 * @author yam655
 */
public @interface Equipment {
    public static final String MELEE="melee";
    public static final String PROJECTILE="projectile";

    public static final String HEAD="head";
    public static final String ARMS="arms";
    public static final String TORSO="torso";
    public static final String LEGS="legs";
    public static final String FEET="feet";
    public static final String FINGERS="fingers";
    public static final String NECK="neck";
    public static final String BODY="body";
    String value();
}
