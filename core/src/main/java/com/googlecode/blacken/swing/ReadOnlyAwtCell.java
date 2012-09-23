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

package com.googlecode.blacken.swing;

import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.terminal.CellWalls;
import com.googlecode.blacken.terminal.TerminalCellLike;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.GraphicAttribute;
import java.awt.font.TextAttribute;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yam655
 */
public class ReadOnlyAwtCell extends AwtCell {
    private boolean dirty;

    /**
     * Make an unset AWT cell.
     */
    private ReadOnlyAwtCell() {
        super();
    }

    @Override
    public void addGlyph(int glyph) {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Override
    public void addGlyph(String glyph) {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Override
    public void clearTextAttributes() {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Override
    public void clearCellWalls() {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Override
    public AwtCell clone() {
        return this;
    }

    @Override
    public boolean isDirty() {
        return true;
    }
    @Override
    public Object unsetAttribute(TextAttribute key) {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Override
    public Object setTextAttribute(TextAttribute key,
                               Object value) {
        throw new UnsupportedOperationException("Readonly cell");
    }
    @Override
    public void setTextAttributes(Map<TextAttribute, Object> attributes) {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Override
    public void setBackgroundColor(Color background) {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Deprecated
    @Override
    public void setCell(AwtCell cell) {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Override
    public void set(AwtCell cell) {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Override
    public void setCell(int glyph, Color background, Color foreground) {
        throw new UnsupportedOperationException("Readonly cell");
    }
    @Override
    public void setCell(int glyph, Map<TextAttribute, Object> attributes) {
        throw new UnsupportedOperationException("Readonly cell");
    }
    @Override
    public void setCell(String glyph, Color background, Color foreground) {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Override
    public void setCell(String glyph, Map<TextAttribute, Object> attributes) {
        throw new UnsupportedOperationException("Readonly cell");
    }

    @Override
    public void setCell(String sequence, Map<TextAttribute, Object> attributes,
                        EnumSet<CellWalls> walls) {
        throw new UnsupportedOperationException("Readonly cell");
    }
    @Override
    public void setCellWalls(CellWalls walls) {
        throw new UnsupportedOperationException("Readonly cell");
    }
    @Override
    public void setCellWalls(Set<CellWalls> walls) {
        throw new UnsupportedOperationException("Readonly cell");
    }
    /**
     * Set the dirty state of the cell
     * @param dirty dirty state
     */
    @Override
    public void setDirty(boolean dirty) {
        // do nothing
    }
    @Override
    public void setFont(Font font) {
        throw new UnsupportedOperationException("Readonly cell");
    }
    @Override
    public void setForegroundColor(Color foreground) {
        throw new UnsupportedOperationException("Readonly cell");
    }
    @Override
    public void setSequence(int sequence) {
        throw new UnsupportedOperationException("Readonly cell");
    }
    @Override
    public void setSequence(String sequence) {
        throw new UnsupportedOperationException("Readonly cell");
    }

    public static AwtCell makeAwtFromTerminal(final TerminalCellLike term) {
        return AwtCell.setAwtFromTerminal(new ReadOnlyAwtCell(), term);
    }

    public static HashMap<String, GraphicAttribute> getReplacement() {
        return AwtCell.getReplacement();
    }

    public static void setReplacement(HashMap<String, GraphicAttribute> replacement) {
        AwtCell.setReplacement(replacement);
    }

    public static Font getGlobalFont() {
        return AwtCell.getGlobalFont();
    }

    public static void setGlobalFont(Font globalFont) {
        AwtCell.setGlobalFont(globalFont);
    }

    protected static int makeDim(final int color) {
        return AwtCell.makeDim(color);
    }

    protected static Color getSwingColor(int c) {
        return AwtCell.getSwingColor(c);
    }

    public static ColorPalette getPalette() {
        return AwtCell.getPalette();
    }

    public static ColorPalette setPalette(ColorPalette palette) {
        return AwtCell.setPalette(palette);
    }

}
