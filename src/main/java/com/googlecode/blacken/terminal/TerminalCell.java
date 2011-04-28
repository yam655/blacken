package com.googlecode.blacken.terminal;

import java.util.EnumSet;

import com.googlecode.blacken.grid.Copyable;
import com.googlecode.blacken.grid.ResetGridCell;


public class TerminalCell extends Copyable {

    public class ResetCell implements ResetGridCell<TerminalCell> {
        @Override
        public void reset(TerminalCell cell) {
            cell.setDirty(true);
        }
    }
    private String glyph;
    private int fg_color;
    private int bg_color;
    private EnumSet<TerminalStyle> style;
    private EnumSet<CellWalls> walls;
    private boolean dirty;

    public TerminalCell() {
        super();
        clearStyle();
        fg_color = 0xFFAAAAAA;
        bg_color = 0xFF000000;
        glyph = " ";
        clearCellWalls();
        dirty = true;
    }
    
    public TerminalCell(String glyph, int foreground, int background, 
                        EnumSet<TerminalStyle> style, boolean dirty) {
        setStyle(style);
        setForeground(foreground);
        setBackground(background);
        setGlyph(glyph);
        setDirty(dirty);
    }
    public void addCellWalls(CellWalls walls) {
        if (walls == null) return;
        dirty = true;
        this.walls.add(walls);
    }


    public void addGlyph(int glyph) {
        this.glyph += String.copyValueOf(Character.toChars(glyph));
        dirty = true;
    }
    
    public void addGlyph(String glyph) {
        this.glyph += glyph;
        dirty = true;
    }
    public void clearCellWalls() {
        this.walls = EnumSet.noneOf(CellWalls.class);
        dirty = true;
    }
    public void clearStyle() {
        this.style = EnumSet.noneOf(TerminalStyle.class);
        dirty = true;
    }
    @Override
    public TerminalCell clone() {
        TerminalCell ret = (TerminalCell)super.clone();
        return ret;
    }

    @Override
    public TerminalCell copy() {
        return (TerminalCell)super.copy();
    }
    public int getBackground() {
        return bg_color;
    }

    public EnumSet<CellWalls> getCellWalls() {
        return this.walls.clone();
    }

    public int getForeground() {
        return fg_color;
    }

    public String getGlyph() {
        return glyph;
    }

    public EnumSet<TerminalStyle> getStyle() {
        return style.clone();
    }

    public boolean isDirty() {
        return dirty;
    }

    public void set(TerminalCell same) {
        this.glyph = same.glyph;
        this.fg_color = same.fg_color;
        this.bg_color = same.bg_color;
        this.style = same.style;
        this.walls = same.walls;
        this.dirty = true;
    }

    public void setBackground(int background) {
        this.bg_color = background;
        dirty = true;
    }
    public void setCellWalls(CellWalls walls) {
        if (walls == null) {
            clearCellWalls();
            return;
        }
        dirty = true;
        this.walls = EnumSet.of(walls);
    }
    
    public void setCellWalls(EnumSet<CellWalls> walls) {
        if (walls == null) {
            clearCellWalls();
            return;
        }
        dirty = true;
        this.walls = walls.clone();
    }
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setForeground(int foreground) {
        this.fg_color = foreground;
        dirty = true;
    }
    public void setGlyph(int glyph) {
        this.glyph = String.copyValueOf(Character.toChars(glyph));
        dirty = true;
    }

    public void setGlyph(String glyph) {
        if (glyph == null) {
            this.glyph = "\u0000";
        } else {
            this.glyph = glyph;
        }
        dirty = true;
    }
    
    public void setStyle(EnumSet<TerminalStyle> style) {
        if (style == null) {
            clearStyle();
            return;
        }
        dirty = true;
        this.style = style.clone();
    }
    public void setStyle(TerminalStyle style) {
        if (style == null) {
            clearStyle();
            return;
        }
        dirty = true;
        this.style = EnumSet.of(style);
    }
    @Override 
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (glyph == null) {
            buf.append("null");
        } else {
            buf.append('"');
            for (char c : glyph.toCharArray()) {
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
