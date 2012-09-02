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

package com.googlecode.blacken.colors;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Steven Black
 */
public class GimpPalette extends ColorPalette {
    private static final Logger LOGGER = LoggerFactory.getLogger(GimpPalette.class);
    private String name = null;
    /**
      * Create a new empty palette.
     */
    public GimpPalette() {
        super();
    }
    public GimpPalette(Class forLoader, String resourceName) throws IOException {
        this.addMappingResource(forLoader, resourceName);
    }

    @Override
    public String[] getMapping() {
        List<String> ret = new ArrayList<>(size());
        Map<Integer, ConsolidatedListMapEntry<String, Integer>> ce =
            new HashMap<>();
        this.consolidateEntries(ce );
        ret.add("GIMP Palette");
        if (name != null) {
            ret.add(String.format("Name: %s", name));
        }
        for (Integer i = 0; i < ce.size(); i++) {
            ConsolidatedListMapEntry<String, Integer> e = ce.get(i);
            StringBuilder buf = new StringBuilder();
            List<String> sortKeys = new ArrayList<>(e.getKeys());
            Collections.sort(sortKeys);
            int[] comps = ColorHelper.colorToComponents(e.getValue());
            for (int ci = 0; ci < comps.length; ci++) {
                if (ci == 3 && comps[ci] == 255) {
                    continue;
                } else {
                    LOGGER.warn("Creating UNSUPPORTED GIMP palette which has ALPHA component");
                }
                if (ci != 0) {
                    buf.append(" ");
                }
                buf.append(comps[ci]);
            }
            if (!sortKeys.isEmpty()) {
                buf.append("\t");
            }
            for (String key : sortKeys) {
                if (buf.length() > 0) {
                    buf.append(" / ");
                    LOGGER.warn("Creating UNSUPPORTED GIMP palette which has multiple labels for the same color");
                }
                buf.append(key);
            }
            ret.add(buf.toString());
        }
        return ret.toArray(new String[0]);
    }

    public String getGimpName() {
        return name;
    }
    public void setGimpName(String name) {
        this.name = name;
    }
    @Override
    protected boolean processMapping(String[] colors, boolean onlyAdd) {
        if (colors == null || colors.length == 0) {
            return false;
        }
        if (!colors[0].equals("GIMP Palette")) {
            throw new RuntimeException("Missing 'GIMP Palette' signature.");
        }
        int firstColors = this.size();
        for (int i = 1; i < colors.length; i++) {
            String color = colors[i];
            color = color.trim();
            if (color.startsWith("#")) {
                continue;
            }
            if (color.startsWith("Name: ")) {
                name = color.substring(5).trim();
                continue;
            }
            if (color.startsWith("Columns: ")) {
                continue;
            }
            final String s[] = color.split("[ \t]+", 4);
            final String name;
            Integer colr;
            if (s.length < 3) {
                throw new RuntimeException("unknown GIMP Palette line: " + colors[i]);
            }
            colr = ColorHelper.colorFromComponents(Integer.parseInt(s[0]),
                    Integer.parseInt(s[1]), Integer.parseInt(s[2]), null);
            if (s.length == 4) {
                name = s[3];
            } else {
                name = null;
            }
            Integer idx = -1;
            if (!onlyAdd) {
                if (this.containsKey(name)) {
                    idx = this.indexOfKey(name);
                    // Gimp palettes use a default name, be gentle
                    if (idx >= firstColors) {
                        idx = -1;
                    }
                }
            }
            if (idx == -1) {
                if (name == null) {
                    add(colr);
                } else {
                    add(name, colr);
                }
            } else {
                this.set(idx, colr);
                putKey(name, idx);
            }
        }
        return true;
    }

}
