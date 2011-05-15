package com.googlecode.blacken.terminal;

import java.util.Set;

import com.googlecode.blacken.grid.Copyable;

public interface TerminalCellLike extends Copyable {

    public void addCellWalls(CellWalls walls);

    public void addSequence(int glyph);

    public void addSequence(String glyph);

    public void clearCellWalls();

    public void clearStyle();

    public TerminalCellLike clone();

    public TerminalCellLike copy();

    public int getBackground();

    public Set<CellWalls> getCellWalls();

    public int getForeground();

    public String getSequence();

    public Set<TerminalStyle> getStyle();

    public boolean isDirty();

    public void set(TerminalCellLike tcell);

    public void setBackground(int background);

    public void setCellWalls(CellWalls walls);

    public void setCellWalls(Set<CellWalls> walls);

    public void setDirty(boolean dirty);

    public void setForeground(int foreground);

    public void setSequence(int sequence);

    public void setSequence(String sequence);

    public void setStyle(Set<TerminalStyle> style);

    public void setStyle(TerminalStyle style);

}
