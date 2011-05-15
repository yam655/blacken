package com.googlecode.blacken.terminal;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.googlecode.blacken.terminal.TerminalCell;

public class UnmodifiableTerminalCell implements TerminalCellLike {
    TerminalCell data;
    
    /**
     * Create an immutable default TerminalCell
     */
    public UnmodifiableTerminalCell() {
        data = new TerminalCell();
    }

    public UnmodifiableTerminalCell(TerminalCellLike cell) {
        data = new TerminalCell(cell);
    }
    public UnmodifiableTerminalCell(TerminalCell cell) {
        data = cell;
    }
    
    /**
     * Create an immutable TerminalCell
     * 
     * @param sequence character sequence to use
     * @param foreground foreground color to use
     * @param background background color to use
     * @param style
     * @param dirty
     */
    public UnmodifiableTerminalCell(String sequence, 
                                 int foreground, int background,
                                 EnumSet<TerminalStyle> style, boolean dirty) {
        data = new TerminalCell(sequence, foreground, background, style, dirty);
    }

    public void addCellWalls(CellWalls walls) {
        throw new UnsupportedOperationException();
    }

    public void addSequence(int glyph) {
        throw new UnsupportedOperationException();
    }
    
    public void addSequence(String glyph) {
        throw new UnsupportedOperationException();
    }
    public void clearCellWalls() {
        throw new UnsupportedOperationException();
    }
    public void clearStyle() {
        throw new UnsupportedOperationException();
    }
    @Override
    public UnmodifiableTerminalCell clone() {
        UnmodifiableTerminalCell ret;
        try {
            ret = (UnmodifiableTerminalCell)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("unexpected clone failure");
        }
        return ret;
    }

    public void set(TerminalCell same) {
        throw new UnsupportedOperationException();
    }

    public void setBackground(int background) {
        throw new UnsupportedOperationException();
    }
    public void setCellWalls(CellWalls walls) {
        throw new UnsupportedOperationException();
    }
    
    public void setCellWalls(Set<CellWalls> walls) {
        throw new UnsupportedOperationException();
    }
    public void setDirty(boolean dirty) {
        throw new UnsupportedOperationException();
    }

    public void setForeground(int foreground) {
        throw new UnsupportedOperationException();
    }
    public void setSequence(int sequence) {
        throw new UnsupportedOperationException();
    }

    public void setSequence(String sequence) {
        throw new UnsupportedOperationException();
    }
    
    public void setStyle(Set<TerminalStyle> style) {
        throw new UnsupportedOperationException();
    }
    public void setStyle(TerminalStyle style) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void set(TerminalCellLike tcell) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TerminalCellLike copy() {
        return this.clone();
    }

    @Override
    public int getBackground() {
        return data.getBackground();
    }

    @Override
    public Set<CellWalls> getCellWalls() {
        return Collections.unmodifiableSet(data.getCellWalls());
    }

    @Override
    public int getForeground() {
        return data.getForeground();
    }

    @Override
    public String getSequence() {
        return data.getSequence();
    }

    @Override
    public Set<TerminalStyle> getStyle() {
        return Collections.unmodifiableSet(data.getStyle());
    }

    @Override
    public boolean isDirty() {
        return data.isDirty();
    }


}
