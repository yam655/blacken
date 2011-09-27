package com.googlecode.blacken.terminal;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.googlecode.blacken.terminal.TerminalCell;

/**
 * An unmodifiable terminal cell
 * 
 * @author yam655
 */
public class UnmodifiableTerminalCell implements TerminalCellLike {
    TerminalCell data;
    
    /**
     * Create an immutable default TerminalCell
     */
    public UnmodifiableTerminalCell() {
        data = new TerminalCell();
    }

    /**
     * Create an immutable TerminalCell like <code>cell</code>
     * @param cell terminal cell
     */
    public UnmodifiableTerminalCell(TerminalCellLike cell) {
        data = new TerminalCell(cell);
    }
    /**
     * Create an immutable TerminalCell like <code>cell</code>
     * @param cell terminal cell
     */
    public UnmodifiableTerminalCell(TerminalCell cell) {
        data = cell;
    }
    
    /**
     * Create an immutable TerminalCell
     * 
     * @param sequence character sequence to use
     * @param foreground foreground color to use
     * @param background background color to use
     * @param style terminal style to use
     * @param dirty dirty status
     */
    public UnmodifiableTerminalCell(String sequence, 
                                 int foreground, int background,
                                 EnumSet<TerminalStyle> style, boolean dirty) {
        data = new TerminalCell(sequence, foreground, background, style, dirty);
    }

    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void addCellWalls(CellWalls walls) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void addSequence(int glyph) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void addSequence(String glyph) {
        throw new UnsupportedOperationException();
    }
    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void clearCellWalls() {
        throw new UnsupportedOperationException();
    }
    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void clearStyle() {
        throw new UnsupportedOperationException();
    }
    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public UnmodifiableTerminalCell clone() {
        UnmodifiableTerminalCell ret;
        try {
            ret = (UnmodifiableTerminalCell)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("unexpected clone failure"); //$NON-NLS-1$
        }
        return ret;
    }

    /**
     * Not supported in UnmodifiableTerminalCell.
     * @param same terminal cell to use
     */
    public void set(TerminalCell same) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void setBackground(int background) {
        throw new UnsupportedOperationException();
    }
    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void setCellWalls(CellWalls walls) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void setCellWalls(Set<CellWalls> walls) {
        throw new UnsupportedOperationException();
    }
    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void setDirty(boolean dirty) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void setForeground(int foreground) {
        throw new UnsupportedOperationException();
    }
    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void setSequence(int sequence) {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void setSequence(String sequence) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void setStyle(Set<TerminalStyle> style) {
        throw new UnsupportedOperationException();
    }
    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void setStyle(TerminalStyle style) {
        throw new UnsupportedOperationException();
    }
    /**
     * Not supported in UnmodifiableTerminalCell.
     */
    @Override
    public void set(TerminalCellLike tcell) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#getBackground()
     */
    @Override
    public int getBackground() {
        return data.getBackground();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#getCellWalls()
     */
    @Override
    public Set<CellWalls> getCellWalls() {
        return Collections.unmodifiableSet(data.getCellWalls());
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#getForeground()
     */
    @Override
    public int getForeground() {
        return data.getForeground();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#getSequence()
     */
    @Override
    public String getSequence() {
        return data.getSequence();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#getStyle()
     */
    @Override
    public Set<TerminalStyle> getStyle() {
        return Collections.unmodifiableSet(data.getStyle());
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.terminal.TerminalCellLike#isDirty()
     */
    @Override
    public boolean isDirty() {
        return data.isDirty();
    }


}
