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

package com.googlecode.blacken.terminal;

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.terminal.utils.CodePointUtils;
import java.util.EnumSet;
import java.util.Set;

/**
 * The template cell have all the standard terminal cell functions, plus one
 * more {@link #applyOn(TerminalCellLike)}.
 *
 * <p>The idea is pretty simple: All of the items on a template cell start out
 * as <code>null</code>, and when an item is <code>null</code> and it is
 * applied to a terminal cell, that value is left alone. This allows you to
 * create a cell template that assigns just attributes or colors which leaves
 * text alone.
 *
 * <p>If you try to use this as a normal terminal cell, you need to specify a
 * value for everything (though you can use {@link #clearCellWalls()} and
 * {@link #clearStyle()} to set those values to nothing). If you try to use
 * this as a normal terminal cell without setting every part to a value, it
 * will throw a null pointer exception.
 *
 * @author Steven Black
 * @since 1.2
 */
public class TerminalCellTemplate implements TerminalCellLike {
    TerminalCellTransformer transformer = null;
    EnumSet<CellWalls> cellWalls = null;
    EnumSet<TerminalStyle> style = null;
    String sequence = null;
    Integer foreground = null;
    Integer background = null;

    public TerminalCellTemplate() {
        // do nothing
    }
    public TerminalCellTemplate(TerminalCellTransformer transformer) {
        internalSetAll(transformer, null, null, null, null, null);
    }
    public TerminalCellTemplate(TerminalCellTransformer transformer,
            String sequence, Integer fore, Integer back) {
        internalSetAll(transformer, sequence, fore, back, null, null);
    }
    public TerminalCellTemplate(String sequence, Integer fore, Integer back) {
        internalSetAll(null, sequence, fore, back, null, null);
    }
    public TerminalCellTemplate(int sequence, Integer fore, Integer back) {
        String s = String.copyValueOf(Character.toChars(sequence));
        internalSetAll(null, s, fore, back, null, null);
    }
    public TerminalCellTemplate(TerminalCellTransformer transformer,
            String sequence, Integer fore, Integer back,
            EnumSet<TerminalStyle> style, EnumSet<CellWalls> walls) {
        internalSetAll(transformer, sequence, fore, back, style, walls);
    }
    public TerminalCellTemplate(String sequence, Integer fore, Integer back, 
            EnumSet<TerminalStyle> style, EnumSet<CellWalls> walls) {
        internalSetAll(null, sequence, fore, back, style, walls);
    }
    public TerminalCellTemplate(String sequence, Integer fore, Integer back,
            EnumSet<TerminalStyle> style, CellWalls walls) {
        internalSetAll(null, sequence, fore, back, style, EnumSet.of(walls));
    }
    public TerminalCellTemplate(TerminalCellTemplate template) {
        internalSetAll(template.transformer, template.sequence,
                template.foreground, template.background,
                template.style, template.cellWalls);
    }

    private void internalSetAll(TerminalCellTransformer transformer,
            String sequence, Integer fore, Integer back,
            EnumSet<TerminalStyle> style, EnumSet<CellWalls> walls) {
        if (transformer != null) {
            this.transformer = transformer;
        }
        if (sequence != null) {
            this.sequence = sequence;
        }
        if (fore != null) {
            this.foreground = fore;
        }
        if (back != null) {
            this.background = back;
        }
        if (style != null) {
            this.style = style.clone();
        }
        if (walls != null) {
            this.cellWalls = walls.clone();
        }
    }

    @Override
    public void addCellWalls(CellWalls walls) {
        if (cellWalls == null) {
            throw new NullPointerException("Can't add to null");
        }
        cellWalls.add(walls);
    }

    @Override
    public void addSequence(int glyph) {
        if (sequence == null) {
            throw new NullPointerException("Can't add to null");
        }
        if (this.sequence.isEmpty()) {
            this.sequence = String.copyValueOf(Character.toChars(BlackenCodePoints.CODEPOINT_DOTTED_CIRCLE));;
        }
        sequence += String.valueOf(Character.toChars(glyph));
    }

    @Override
    public void addSequence(String glyph) {
        if (sequence == null) {
            throw new NullPointerException("Can't add to null");
        }
        if (this.sequence.isEmpty()) {
            this.sequence = String.copyValueOf(Character.toChars(BlackenCodePoints.CODEPOINT_DOTTED_CIRCLE));;
        }
        sequence += glyph;
    }

    @Override
    public void clearCellWalls() {
        cellWalls = EnumSet.noneOf(CellWalls.class);
    }

    @Override
    public void clearStyle() {
        style = EnumSet.noneOf(TerminalStyle.class);
    }

    @Override
    public TerminalCellLike clone() {
        TerminalCellTemplate template = new TerminalCellTemplate();
        template.set(this);
        return template;
    }

    @Override
    public int getBackground() {
        // will throw null pointer exception automatically if needed
        return background;
    }

    @Override
    public Set<CellWalls> getCellWalls() {
        if (this.cellWalls == null) {
            throw new NullPointerException();
        }
        return cellWalls;
    }

    @Override
    public int getForeground() {
        // will throw null pointer exception automatically if needed
        return foreground;
    }

    @Override
    public String getSequence() {
        if (this.sequence == null) {
            throw new NullPointerException();
        }
        return sequence;
    }

    @Override
    public Set<TerminalStyle> getStyle() {
        if (this.style == null) {
            throw new NullPointerException();
        }
        return style;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public void joinOn(TerminalCellLike tcell, Regionlike bounds, int y, int x) {
        boolean changed = false;
        if (background != null) {
            tcell.setBackground(ColorHelper.lerp(tcell.getBackground(), background));
            changed = true;
        }
        if (foreground != null) {
            tcell.setForeground(ColorHelper.lerp(tcell.getForeground(), foreground));
            changed = true;
        }
        if (sequence != null && !sequence.isEmpty()) {
            int[] adv = CodePointUtils.findAdvancingCodepoints(sequence, 0);
            if (adv[0] == 0 && adv[1] == sequence.length()) {
                String old = tcell.getSequence();
                if (old.isEmpty()) {
                    old = String.copyValueOf(Character.toChars(BlackenCodePoints.CODEPOINT_DOTTED_CIRCLE));
                }
                tcell.setSequence(old + sequence);
            } else {
                tcell.setSequence(sequence);
            }
            changed = true;
        }
        if (cellWalls != null && !cellWalls.isEmpty()) {
            if (tcell.getCellWalls().isEmpty()) {
                tcell.setCellWalls(cellWalls);
            } else {
                EnumSet<CellWalls> walls = EnumSet.copyOf(tcell.getCellWalls());
                walls.addAll(cellWalls);
                tcell.setCellWalls(walls);
            }
            changed = true;
        }
        if (style != null && !style.isEmpty()) {
            if (tcell.getStyle().isEmpty()) {
                tcell.setStyle(style);
            } else {
                EnumSet<TerminalStyle> nStyle = EnumSet.copyOf(tcell.getStyle());
                nStyle.addAll(style);
            }
            changed = true;
        }
        if (this.transformer != null) {
            if (this.transformer.transform(tcell, bounds, y, x)) {
                changed = true;
            }
        }
        if (changed) {
            tcell.setDirty(true);
        }
    }

    public void applyOn(TerminalCellLike tcell, Regionlike bounds, int y, int x) {
        boolean changed = false;
        if (background != null) {
            tcell.setBackground(background);
            changed = true;
        }
        if (foreground != null) {
            tcell.setForeground(foreground);
            changed = true;
        }
        if (sequence != null) {
            tcell.setSequence(sequence);
            changed = true;
        }
        if (cellWalls != null) {
            tcell.setCellWalls(cellWalls);
            changed = true;
        }
        if (style != null) {
            tcell.setStyle(style);
            changed = true;
        }
        if (this.transformer != null) {
            if (this.transformer.transform(tcell, bounds, y, x)) {
                changed = true;
            }
        }
        if (changed) {
            tcell.setDirty(true);
        }
    }

    @Override
    public void set(TerminalCellLike tcell) {
        this.setBackground(tcell.getBackground());
        this.setForeground(tcell.getForeground());
        this.setCellWalls(tcell.getCellWalls());
        this.setSequence(tcell.getSequence());
        this.setStyle(tcell.getStyle());
    }

    @Override
    public void setBackground(int background) {
        this.background = background;
    }

    @Override
    public void setCellWalls(CellWalls walls) {
        this.cellWalls = EnumSet.of(walls);
    }

    @Override
    public void setCellWalls(Set<CellWalls> walls) {
        if (walls == null || walls.isEmpty()) {
            this.cellWalls = EnumSet.noneOf(CellWalls.class);
        } else {
            this.cellWalls = EnumSet.copyOf(walls);
        }
    }

    @Override
    public void setDirty(boolean dirty) {
        // do nothing (always dirty)
    }

    @Override
    public void setForeground(int foreground) {
        this.foreground = foreground;
    }

    @Override
    public void setSequence(int sequence) {
        this.sequence = String.copyValueOf(Character.toChars(sequence));
    }

    @Override
    public void setSequence(String sequence) {
        if (sequence == null) {
            sequence = "";
        }
        this.sequence = sequence;
    }

    @Override
    public void setStyle(Set<TerminalStyle> style) {
        if (style == null || style.isEmpty()) {
            this.style = EnumSet.noneOf(TerminalStyle.class);
        } else {
            this.style = EnumSet.copyOf(style);
        }
    }

    @Override
    public void setStyle(TerminalStyle style) {
        this.style = EnumSet.of(style);
    }

    public boolean isStyleUnset() {
        return this.style == null;
    }

    public boolean isCellWallsUnset() {
        return this.cellWalls == null;
    }

    public boolean isForegroundUnset() {
        return this.foreground == null;
    }

    public boolean isBackgroundUnset() {
        return this.background == null;
    }

    public boolean isSequenceUnset() {
        return this.sequence == null;
    }

    public boolean isTransformerUnset() {
        return this.transformer == null;
    }

    public TerminalCellLike makeSafe(TerminalCellLike target) {
        TerminalCellLike ret = target;
        if (ret == null) {
            ret = new TerminalCell();
        }
        if (background == null && foreground == null) {
            ret.setBackground(0xFF000000);
            ret.setForeground(0xFFFFFFFF);
        } else if (background == null) {
            ret.setBackground(ColorHelper.makeVisible(foreground));
            if (ret != this) {
                ret.setForeground(foreground);
            }
        } else if (foreground == null) {
            ret.setForeground(ColorHelper.makeVisible(background));
            if (ret != this) {
                ret.setBackground(background);
            }
        } else if (ret != this) {
            ret.setForeground(foreground);
            ret.setBackground(background);
        }
        if (sequence == null) {
            ret.setSequence("");
        } else if (ret != this) {
            ret.setSequence(sequence);
        }
        if (cellWalls == null) {
            ret.clearCellWalls();
        } else if (ret != this) {
            ret.setCellWalls(cellWalls);
        }
        if (style == null) {
            this.clearStyle();
        } else if (ret != this) {
            ret.setStyle(style);
        }
        ret.setDirty(true);
        return ret;
    }

    public void makeSafe() {
        makeSafe(this);
    }

    public void addStyle(TerminalStyle terminalStyle) {
        if (this.style == null) {
            setStyle(terminalStyle);
        } else {
            style.add(terminalStyle);
        }
    }
}
