/* blacken - a library for Roguelike games
 * Copyright © 2013 Steven Black <yam655@gmail.com>
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
import com.googlecode.blacken.terminal.TerminalCellLike;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yam655
 */
public class ForcedPaletteTransformer implements PaletteTransformer {
    private ColorPalette newPalette;
    private ColorPalette oldPalette;
    private Map<Integer, Integer> inversePalette;
    private Set<Integer> changedColors;
    private String whiteName = null;
    private String blackName = null;
    private Integer whiteIdx = null;
    private Integer blackIdx = null;

    public ForcedPaletteTransformer() {
        // do nothing
    }

    public ForcedPaletteTransformer(String white, String black) {
        whiteName = white;
        blackName = black;
    }

    public ForcedPaletteTransformer(Integer white, Integer black) {
        whiteIdx = white;
        blackIdx = black;
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

        if (whiteName != null) {
            whiteIdx = newPalette.getColorOrIndex(whiteName);
        }
        if (blackName != null) {
            blackIdx = newPalette.getColorOrIndex(blackName);
        }
        if (blackIdx == null) {
            blackIdx = ColorHelper.guessBlack(newPalette);
        }
        if (whiteIdx == null) {
            whiteIdx = ColorHelper.guessWhite(newPalette);
        }
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

        if (i > newPalette.size()) {
            Integer t = ColorHelper.makeVisible(oldPalette.getColor(i),
                    66, whiteIdx, blackIdx);
            if (t != null) {
                i = t;
            }
        }
        return i;
    }

    @Override
    public int makeVisible(int i, boolean invert) {
        if (invert) {
            return ColorHelper.makeVisible(i, 66, blackIdx, whiteIdx);
        } else {
            return ColorHelper.makeVisible(i, 66, whiteIdx, blackIdx);
        }
    }
}
