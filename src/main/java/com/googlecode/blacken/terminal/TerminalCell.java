package com.googlecode.blacken.terminal;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.googlecode.blacken.grid.Copyable;
import com.googlecode.blacken.grid.ResetGridCell;


public class TerminalCell implements Copyable, TerminalCellLike {

    public class ResetCell implements ResetGridCell<TerminalCellLike> {
        @Override
        public void reset(TerminalCellLike cell) {
            cell.setDirty(true);
        }
    }
    private String sequence;
    private int fg_color;
    private int bg_color;
    private Set<TerminalStyle> style;
    private Set<CellWalls> walls;
    private boolean dirty;

    public TerminalCell() {
        super();
        clearStyle();
        fg_color = 0xFFAAAAAA;
        bg_color = 0xFF000000;
        sequence = "\u0000";
        clearCellWalls();
        dirty = true;
    }
    
    public TerminalCell(String sequence, int foreground, int background, 
                        Set<TerminalStyle> style, boolean dirty) {
        setStyle(style);
        setForeground(foreground);
        setBackground(background);
        setSequence(sequence);
        setDirty(dirty);
    }
    public TerminalCell(TerminalCellLike cell) {
        // TODO Auto-generated constructor stub
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
        if (this.sequence.equals("\u0000")) {
            this.sequence = "\u25cc";
        }
        this.sequence = this.sequence + String.copyValueOf(Character.toChars(glyph));
        dirty = true;
    }
    
    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#addSequence(java.lang.String)
     */
    @Override
    public void addSequence(String glyph) {
        if (this.sequence.equals("\u0000")) {
            this.sequence = "\u25cc";
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
            throw new RuntimeException("Unexpected CloneNotSupportedException", e);
        }
        return ret;
    }

    /* (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#copy()
     */
    @Override
    public TerminalCell copy() {
        return this.clone();
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
            this.sequence = "\u0000";
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
            buf.append("null");
        } else {
            buf.append('"');
            for (char c : sequence.toCharArray()) {
                if (c < ' ' || c > 127) {
                    buf.append(String.format("\\u04x", c));
                } else {
                    buf.append(c);
                }
            }
            buf.append('"');
        }
        buf.append(String.format(", 0x%08x", fg_color));
        buf.append(String.format(", 0x%08x", bg_color));
        if (dirty) {
            buf.append(", DIRTY");
        } else {
            buf.append(", clean");
        }
        if (style == null || style.isEmpty()) {
            buf.append(", {}");
        } else {
            buf.append(", {");
            for (TerminalStyle sty : style) {
                buf.append(sty.name());
                buf.append(", ");
            }
            buf.append("}");
        }
        if (walls == null || walls.isEmpty()) {
            buf.append(", {}");
        } else {
            buf.append(", {");
            for (CellWalls wall : walls) {
                buf.append(wall.name());
                buf.append(", ");
            }
            buf.append("}");
            
        }
        return buf.toString();
    }

}
