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

package com.googlecode.blacken.grid;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @param <Z>
 * @author yam655
 */
final class GridData0<Z> implements Serializable {
    private static final long serialVersionUID = 1212L; // unchanging

    // They're private, really, but I didn't like the complaints that they weren't used.

    ArrayList<ArrayList<Z>> grid = null;
    int x1 = 0;
    int y1 = 0;
    Z empty = null;

    GridData0() {
        // for serialization
    }

    public void set(String name, Object value) {
        try {
            Class myClass = this.getClass();
            Field field = myClass.getDeclaredField(name);
            field.set(this, value);
        } catch (IllegalAccessException | IllegalArgumentException |
                 NoSuchFieldException | SecurityException ex) {
            throw new RuntimeException(String.format("failed to set field '%s'", name), ex);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> ret = new HashMap<>();
        Class myClass = this.getClass();
        Field[] fields = myClass.getDeclaredFields();
        for (Field f : fields) {
            try {
                ret.put(f.getName(), f.get(this));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                // should never happen.
                return null;
            }
        }
        ret.put("__target__", Grid.class.getName());
        ret.put("__version__", 1);
        return ret;
    }

    public Object readResolve() throws ObjectStreamException {
        Map<String, Object> map = toMap();
        map = GridData1.upgrade(map);
        Grid g = new Grid(map);
        return g;
    }

}
