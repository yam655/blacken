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

package com.googlecode.blacken.colors;

/**
 * Transform a color from one palette to another.
 *
 * @author Steven Black
 * @since 1.2
 */
public interface PaletteTransformer extends ColorTransformer {
    /**
     * Set the new and old palettes.
     *
     * @param newPalette
     * @param oldPalette
     */
    public void setPalettes(ColorPalette newPalette, ColorPalette oldPalette);
    /**
     * Get the old palette.
     *
     * @return
     */
    public ColorPalette getOldPalette();
    /**
     * Get the new palette.
     *
     * @return
     */
    public ColorPalette getNewPalette();
    /**
     * Many transforms have some sort of default foreground/background color
     * that is used when there is no mapping. This requests one of those colors
     * based on whether the <code>color</code> is closer to white or black.
     *
     * <p>If there is a requirement that cells remain visible, this may be
     * used to prevent the foreground and background from being the same color.
     *
     * @param color
     * @param invert invert the foreground/background logic
     * @return
     */
    public int makeVisible(int color, boolean invert);
}
