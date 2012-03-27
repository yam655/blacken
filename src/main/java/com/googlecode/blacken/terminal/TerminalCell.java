/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.googlecode.blacken.grid.DirtyGridCell;

/**
 * A concrete terminal cell.
 * 
 * @author yam655
 *
 */
public class TerminalCell implements Cloneable, TerminalCellLike {

    /**
     * Helper class to reset the cell to known-good values.
     * 
     * @author yam655
     *
     */
    public class ResetCell implements DirtyGridCell<TerminalCellLike> {
        /*
         * (non-Javadoc)
         * @see com.googlecode.blacken.grid.ResetGridCell#reset(com.googlecode.blacken.grid.Copyable)
         */
        @Override
        public void setDirty(TerminalCellLike cell, boolean isDirty) {
            cell.setDirty(isDirty);
        }
    }
    private String sequence;
    private int fg_color;
    private int bg_color;
    private Set<TerminalStyle> style;
    private Set<CellWalls> walls;
    private boolean dirty;

    /**
     * Create a new terminal cell with default settings.
     */
    public TerminalCell() {
        super();
        clearStyle();
        fg_color = 0xFFAAAAAA;
        bg_color = 0xFF000000;
        sequence = "\u0000"; //$NON-NLS-1$
        clearCellWalls();
        dirty = true;
    }
    /**
     * Create a new TerminalCell, setting important things.
     * 
     * @param sequence visible character sequence
     * @param foreground foreground color
     * @param background background color
     * @param style terminal cell style
     * @param dirty dirty status
     */
    public TerminalCell(String sequence, int foreground, int background, 
                        Set<TerminalStyle> style, boolean dirty) {
        setStyle(style);
        setForeground(foreground);
        setBackground(background);
        setSequence(sequence);
        setDirty(dirty);
        clearCellWalls();
    }
    /**
     * Create a new cell.
     * 
     * @param cell cell to base this one off of
     */
    public TerminalCell(TerminalCellLike cell) {
        clearStyle();
        clearCellWalls();
        set(cell);
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#addCellWalls(com.googlecode.blacken.terminal.CellWalls)
     */
    @Override
    public void addCellWalls(CellWalls walls) {
        if (walls == null) return;
        dirty = true;
        this.walls.add(walls);
    }


    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#addSequence(int)
     */
    @Override
    public void addSequence(int glyph) {
        if (this.sequence.equals("\u0000")) { //$NON-NLS-1$
            this.sequence = "\u25cc"; //$NON-NLS-1$
        }
        this.sequence = this.sequence + String.copyValueOf(Character.toChars(glyph));
        dirty = true;
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#addSequence(java.lang.String)
     */
    @Override
    public void addSequence(String glyph) {
        if (this.sequence.equals("\u0000")) { //$NON-NLS-1$
            this.sequence = "\u25cc"; //$NON-NLS-1$
        }
        this.sequence = this.sequence + glyph;
        dirty = true;
    }
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#clearCellWalls()
     */
    @Override
    public void clearCellWalls() {
        this.walls = EnumSet.noneOf(CellWalls.class);
        dirty = true;
    }
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#clearStyle()
     */
    @Override
    public void clearStyle() {
        this.style = EnumSet.noneOf(TerminalStyle.class);
        dirty = true;
    }
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#clone()
     */
    @Override
    public TerminalCell clone() {
        TerminalCell ret;
        try {
            ret = (TerminalCell)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Unexpected CloneNotSupportedException", e); //$NON-NLS-1$
        }
        return ret;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#getBackground()
     */
    @Override
    public int getBackground() {
        return bg_color;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#getCellWalls()
     */
    @Override
    public Set<CellWalls> getCellWalls() {
        if (this.walls == null) {
            return null;
        }
        return Collections.unmodifiableSet(this.walls);
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#getForeground()
     */
    @Override
    public int getForeground() {
        return fg_color;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#getSequence()
     */
    @Override
    public String getSequence() {
        return sequence;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#getStyle()
     */
    @Override
    public Set<TerminalStyle> getStyle() {
        if (this.style == null) {
            return null;
        }
        return Collections.unmodifiableSet(this.style);
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#isDirty()
     */
    @Override
    public boolean isDirty() {
        return dirty;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#set(com.googlecode.blacken.terminal.TerminalCell)
     */
    @Override
    public void set(TerminalCellLike same) {
        this.sequence = same.getSequence();
        this.fg_color = same.getForeground();
        this.bg_color = same.getBackground();
        this.style = same.getStyle();
        this.walls = same.getCellWalls();
        this.dirty = true;
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#setBackground(int)
     */
    @Override
    public void setBackground(int background) {
        this.bg_color = background;
        dirty = true;
    }
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#setCellWalls(com.googlecode.blacken.terminal.CellWalls)
     */
    @Override
    public void setCellWalls(CellWalls walls) {
        if (walls == null) {
            clearCellWalls();
            return;
        }
        dirty = true;
        this.walls = EnumSet.of(walls);
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#setCellWalls(java.util.EnumSet)
     */
    @Override
    public void setCellWalls(Set<CellWalls> walls) {
        if (walls == null) {
            clearCellWalls();
            return;
        }
        dirty = true;
        this.walls = EnumSet.copyOf(walls);
    }
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#setForeground(int)
     */
    @Override
    public void setForeground(int foreground) {
        this.fg_color = foreground;
        dirty = true;
    }
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#setSequence(int)
     */
    @Override
    public void setSequence(int sequence) {
        this.sequence = String.copyValueOf(Character.toChars(sequence));
        dirty = true;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#setSequence(java.lang.String)
     */
    @Override
    public void setSequence(String sequence) {
        if (sequence == null) {
            this.sequence = "\u0000"; //$NON-NLS-1$
        } else {
            this.sequence = sequence;
        }
        dirty = true;
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#setStyle(java.util.EnumSet)
     */
    @Override
    public void setStyle(Set<TerminalStyle> style) {
        if (style == null) {
            clearStyle();
            return;
        }
        dirty = true;
        this.style = EnumSet.copyOf(style);
    }
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#setStyle(com.googlecode.blacken.terminal.TerminalStyle)
     */
    @Override
    public void setStyle(TerminalStyle style) {
        if (style == null) {
            clearStyle();
            return;
        }
        dirty = true;
        this.style = EnumSet.of(style);
    }
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#toString()
     */
    @Override 
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (sequence == null) {
            buf.append("null"); //$NON-NLS-1$
        } else {
            buf.append('"');
            for (char c : sequence.toCharArray()) {
                if (c < ' ' || c > 127) {
                    buf.append(String.format("\\u04x", c)); //$NON-NLS-1$
                } else {
                    buf.append(c);
                }
            }
            buf.append('"');
        }
        buf.append(String.format(", 0x%08x", fg_color)); //$NON-NLS-1$
        buf.append(String.format(", 0x%08x", bg_color)); //$NON-NLS-1$
        if (dirty) {
            buf.append(", DIRTY"); //$NON-NLS-1$
        } else {
            buf.append(", clean"); //$NON-NLS-1$
        }
        if (style == null || style.isEmpty()) {
            buf.append(", {}"); //$NON-NLS-1$
        } else {
            buf.append(", {"); //$NON-NLS-1$
            for (TerminalStyle sty : style) {
                buf.append(sty.name());
                buf.append(", "); //$NON-NLS-1$
            }
            buf.append("}"); //$NON-NLS-1$
        }
        if (walls == null || walls.isEmpty()) {
            buf.append(", {}"); //$NON-NLS-1$
        } else {
            buf.append(", {"); //$NON-NLS-1$
            for (CellWalls wall : walls) {
                buf.append(wall.name());
                buf.append(", "); //$NON-NLS-1$
            }
            buf.append("}"); //$NON-NLS-1$
            
        }
        return buf.toString();
    }

}
