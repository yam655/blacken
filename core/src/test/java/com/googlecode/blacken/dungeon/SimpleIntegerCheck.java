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

/**
 *
 * @author Steven Black
 */
public class SimpleIntegerCheck implements ThingTypeCheck<Integer> {
    Integer min = null;
    Integer max = null;

    SimpleIntegerCheck() {
        // do nothing
    }

    SimpleIntegerCheck(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
        if (max != null && min != null) {
            if (max < min) {
                min = max;
            }
        }
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
        if (max != null && min != null) {
            if (max < min) {
                max = min;
            }
        }
    }
    
    @Override
    public boolean isSufficient(Integer obj) {
        if (obj == null) {
            if (min != null) {
                return false;
            }
            return true;
        }
        int eMax = Integer.MAX_VALUE;
        if (max != null) {
            eMax = max;
        }
        int eMin = Integer.MIN_VALUE;
        if (min != null) {
            eMin = min;
        }
        return eMin <= obj && obj <= eMax;
    }

}
