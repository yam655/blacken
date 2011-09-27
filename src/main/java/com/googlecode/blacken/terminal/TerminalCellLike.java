/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.blacken.terminal;

import java.util.Set;

/**
 * A terminal cell-like interface.
 * 
 * @author yam655
 */
public interface TerminalCellLike extends Cloneable {

    /**
     * Add cell walls
     * @param walls new wall
     */
    public void addCellWalls(CellWalls walls);

    /**
     * Add a character sequence
     * @param glyph sequence
     */
    public void addSequence(int glyph);

    /**
     * Add a character sequence
     * @param glyph sequence
     */
    public void addSequence(String glyph);

    /**
     * Clear the cell walls
     */
    public void clearCellWalls();

    /**
     * Clear the style
     */
    public void clearStyle();

    /**
     * Clone the cell
     * @return new copy
     */
    public TerminalCellLike clone();

    /**
     * Get the background
     * @return the background
     */
    public int getBackground();

    /**
     * Get the cell walls
     * @return the cell walls
     */
    public Set<CellWalls> getCellWalls();

    /**
     * Get the foreground
     * @return the foreground
     */
    public int getForeground();

    /**
     * Get the sequence
     * @return the sequence
     */
    public String getSequence();

    /**
     * Get the style
     * @return the style
     */
    public Set<TerminalStyle> getStyle();

    /**
     * Is it dirty?
     * @return dirty status
     */
    public boolean isDirty();

    /**
     * set the terminal cell
     * @param tcell cell to set from
     */
    public void set(TerminalCellLike tcell);

    /**
     * Set the background
     * @param background background
     */
    public void setBackground(int background);

    /**
     * Set the walls to one wall
     * @param walls wall to set
     */
    public void setCellWalls(CellWalls walls);

    /**
     * Set the cell walls
     * @param walls walls to set
     */
    public void setCellWalls(Set<CellWalls> walls);

    /**
     * Set the dirty status
     * @param dirty dirty status
     */
    public void setDirty(boolean dirty);

    /**
     * Set the foreground
     * @param foreground the foreground
     */
    public void setForeground(int foreground);

    /**
     * Set the sequence
     * @param sequence the sequence
     */
    public void setSequence(int sequence);

    /**
     * Set the sequence
     * @param sequence the sequence
     */
    public void setSequence(String sequence);

    /**
     * Set the terminal style
     * @param style the style
     */
    public void setStyle(Set<TerminalStyle> style);

    /**
     * Set the terminal style
     * @param style the style
     */
    public void setStyle(TerminalStyle style);
}
