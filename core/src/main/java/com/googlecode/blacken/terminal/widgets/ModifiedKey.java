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

package com.googlecode.blacken.terminal.widgets;

import com.googlecode.blacken.terminal.BlackenCodePoints;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenModifier;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Steven Black
 */
public class ModifiedKey implements Serializable {
    private static final long serialVersionUID = -3432157937995639882L;
    private EnumSet<BlackenModifier> modifier = EnumSet.noneOf(BlackenModifier.class);
    private int codepoint = BlackenKeys.NO_KEY;

    public ModifiedKey() {
        // do nothing
    }
    public ModifiedKey(Set<BlackenModifier> mods, int codepoint) {
        if (mods == null || mods.isEmpty()) {
            modifier = EnumSet.noneOf(BlackenModifier.class);
        } else {
            modifier = EnumSet.copyOf(mods);
        }
        this.codepoint = codepoint;
    }

    public EnumSet<BlackenModifier> getModifier() {
        return modifier;
    }
    public int getModifierAsCodepoint() {
        if (modifier == null || modifier.isEmpty()) {
            return BlackenKeys.NO_KEY;
        }
        return BlackenModifier.getAsCodepoint(modifier);
    }

    public void setModifier(EnumSet<BlackenModifier> modifier) {
        if (modifier == null) {
            this.modifier = EnumSet.noneOf(BlackenModifier.class);
        } else {
            this.modifier = modifier;
        }
    }
    public void setModifier(BlackenModifier modifier) {
        this.modifier = EnumSet.of(modifier);
    }
    public void setModifier(int modifier) {
        this.modifier = BlackenModifier.getAsSet(modifier);
    }

    public int getCodepoint() {
        return codepoint;
    }
    public String getCodepointAsString() {
        return String.copyValueOf(Character.toChars(codepoint));
    }

    public void setCodepoint(int codepoint) {
        this.codepoint = codepoint;
    }
    public void setCodepoint(String codepoint) {
        if (codepoint == null || codepoint.isEmpty()) {
            this.codepoint = BlackenKeys.NO_KEY;
        }
        if (codepoint.codePointCount(0, codepoint.length()) > 1) {
            throw new RuntimeException("Need a single codepoint only");
        }
        this.codepoint = codepoint.codePointAt(0);
    }

    public static ModifiedKey parse(String keyname) {
        String[] keyArr = keyname.split("[+]");
        List<String> keys = new ArrayList<>(Arrays.asList(keyArr));
        EnumSet<BlackenModifier> mods = BlackenModifier.extractModifiers(keys);
        if (keys.isEmpty()) {
            return new ModifiedKey(mods, BlackenKeys.NO_KEY);
        } else if (keys.size() > 1) {
            throw new IllegalArgumentException("Too many unknown things:" + keys.toString());
        }
        String k = keys.get(0);
        if (k.isEmpty()) {
            return new ModifiedKey(mods, BlackenKeys.NO_KEY);
        }
        if (k.codePointCount(0, k.length()) == 1) {
            return new ModifiedKey(mods, k.codePointAt(0));
        }
        if (k.startsWith("\\u") || k.startsWith("\\U")) {
            return new ModifiedKey(mods, Integer.parseInt(k.substring(2), 16));
        }
        k = k.toUpperCase().replaceAll("[\\s-]", "_");
        String check = "KEY_" + k;
        Field field = null;
        try {
            field = BlackenKeys.class.getDeclaredField(check);
        } catch (NoSuchFieldException | SecurityException ex) {
            // do nothing
        }
        if (field == null) {
            check = "CODEPOINT_" + k;
            try {
                field = BlackenCodePoints.class.getDeclaredField(check);
            } catch (NoSuchFieldException | SecurityException ex) {
                // do nothing
            }
        }
        if (field != null) {
            try {
                int codepoint = field.getInt(null);
                return new ModifiedKey(mods, codepoint);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        throw new RuntimeException("Failed to find codepoint: " + k);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.modifier);
        hash = 29 * hash + this.codepoint;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModifiedKey other = (ModifiedKey) obj;
        if (!Objects.equals(this.modifier, other.modifier)) {
            return false;
        }
        if (this.codepoint != other.codepoint) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer ret = BlackenModifier.getModifierString(modifier);
        String c;
        if (BlackenKeys.isKeyCode(codepoint)) {
            c = BlackenKeys.getKeyName(codepoint);
            if (c != null && c.startsWith("KEY_")) {
                c = c.substring("KEY_".length());
            }
        } else {
            c = BlackenCodePoints.getName(codepoint);
            if (c != null) {
                c = c.substring("CODEPOINT_".length());
            }
        }
        if (c == null) {
            c = BlackenKeys.toString(codepoint);
        }
        ret.append(c);
        return ret.toString();
    }

}
