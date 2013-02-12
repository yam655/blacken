/* blacken - a library for Roguelike games
 * Copyright © 2012,2013 Steven Black <yam655@gmail.com>
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

package com.googlecode.blacken.colors.transformers;

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.colors.PaletteTransformer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Palette transformer that retains all previous colors and simply tries to use
 * the new palette for colors when possible.
 *
 * @author Steven Black
 * @since 1.2
 */
public class GentlePaletteTransformer implements PaletteTransformer {
    private ColorPalette newPalette;
    private ColorPalette oldPalette;
    private Map<Integer, Integer> inversePalette;
    private Set<Integer> changedColors;

    public GentlePaletteTransformer() {
        // nothing to do
    }

    @Override
    public void setPalettes(ColorPalette newPalette, ColorPalette oldPalette) {
        if (oldPalette == null) {
            oldPalette = new ColorPalette();
            oldPalette.makeUnmodifiable();
        }
        this.newPalette = newPalette;
        this.oldPalette = oldPalette;
        preparePalettes();
    }

    private void preparePalettes() {
        if (oldPalette == null || newPalette == null) {
            throw new NullPointerException("Both palettes must be set.");
        }
        inversePalette = new HashMap<>();
        for (int i = 0; i < newPalette.size(); i++) {
            inversePalette.put(newPalette.get(i), i);
        }
        changedColors = ColorHelper.findChangedColors(newPalette, oldPalette);
    }

    @Override
    public ColorPalette getOldPalette() {
        return oldPalette;
    }

    @Override
    public ColorPalette getNewPalette() {
        return newPalette;
    }

    @Override
    public int transform(int i) {
        if (oldPalette != null) {
            /// turn an oldPalette index in to a color
            if (changedColors.contains(i)) {
                i = oldPalette.get(i);
            }
        }
        // turn a color in to a current index
        if (i > newPalette.size()) {
            if (inversePalette.containsKey(i)) {
                i = inversePalette.get(i);
            }
        }
        return i;
    }

    @Override
    public int makeVisible(int i, boolean invert) {
        return i;
    }

}
