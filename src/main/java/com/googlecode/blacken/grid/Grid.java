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
package com.googlecode.blacken.grid;

/** Create and manage two-dimensional maps
 *
 * Copyright � 2010, Steven Black. All Rights Reserved.
 * You may distribute under the terms of the LGPL v3 or newer.
 *
 * Adapted from Python code to do the same thing, 
 * which was Copyright � 2004-2010 Steven Black.
 */

import java.io.Serializable;
import java.util.ArrayList;

import com.googlecode.blacken.exceptions.IrregularGridException;

/**
 * A two-dimensional grid. 
 * 
 * <p>Using a little work you can make a two dimensional grid with an 
 * arbitrarily-sized third dimension.</p>
 *
 * <p>Grids come in two varieties: regular and irregular. 
 * While regular grids are always rectangular, irregular grids can be both
 * arbitrary shapes, as well as contain voids.</p>
 * 
 * <p>A regular grid is
 * exactly what you think of when you think of a grid, a two-dimensional
 * array which can be arbitrarily sized along either dimension. 
 * That is, our grid forbids <code>null</code> values in favor of having 
 * a known-good <code>empty</code> cell value. This allows the grid to be
 * arbitrarily resized with cells being dropped and added and the cells always
 * have known-good values.</p>
 * 
 * <p>The irregular grids expressly allow <code>null</code> values. (Note
 * that ({@link #unset(int, int)} uses the stored <code>empty</code> value, not 
 * <code>null</code>.) Because irregular grids may have shaped edges which we
 * can not recreate, they explicitly forbid resizing.</p>
 * 
 * <p>While in the general use you could use a regular grid with a special
 * "off-limits" cell value, the voids in the irregular grids allow a shaped
 * segment of a grid to be copied in to another grid. The <code>null</code> 
 * cells are never copied when copying a grid in to another grid, so if you 
 * copy one irregular grid in to another the number of <code>null</code> 
 * cells are always reduced.</p>
 * 
 * @author yam655
 * @param <Z> the item to fill the grid with
 */
public class Grid <Z extends Copyable> 
implements Regionlike, Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<ArrayList<Z>> grid = null;
    private int start_x = 0;
    private int start_y = 0;
    private int size_x = 0;
    private int size_y = 0;
    protected Z empty = null;

    private boolean irregular = false;

    /**
     * Create a new, zero height, zero width grid
     * 
     * <p>Because the <code>empty</code> value is set to <code>null</code>
     * it creates an irregular grid. You can safely resize the grid 
     * by calling {@link #setSize(int, int)} -- without
     * calling {@link #reset(int, int, Copyable)}.</p>
     */
    public Grid() {
        grid = new ArrayList<ArrayList<Z>>();
        this.empty = null;
        this.size_x = 0;
        this.size_y = 0;
        this.start_x = 0;
        this.start_y = 0;
        this.irregular = true;
    }
    
    /**
     * Create a new grid.
     * 
     * <p>The starting coordinate for the grid is 0,0.</p>
     * 
     * @param empty the prototype empty cell
     * @param numRows number of rows
     * @param numCols number of columns
     */
    public Grid(Z empty, int numRows, int numCols) {
        grid = new ArrayList<ArrayList<Z>>();
        this.empty = empty;
        this.size_y = 0;
        this.size_x = 0;
        if (numRows > 0) {
            setSize(numRows, numCols);
        }
    }

    /**
     * Create a new grid with a position.
     * 
     * @param empty the prototype empty cell
     * @param numRows number of rows
     * @param numCols number of columns
     * @param y starting Y coordinate
     * @param x starting X coordinate
     */
    public Grid(Z empty, int numRows, int numCols, int y, int x) {
        grid = new ArrayList<ArrayList<Z>>();
        this.empty = empty;
        this.size_x = 0;
        this.size_y = 0;
        this.start_y = y;
        this.start_x = x;
        if (numRows > 0) {
            setSize(numRows, numCols);
        }
    }
    
    /**
     * Create an irregularly shaped grid.
     * 
     * An irregular grid does not have cells within the square that is the
     * normal bounds. This is handled by inserting nulls in the missing cells.
     * When we create an irregularly shaped grid, the entire grid is full of
     * nulls. To create the pattern, you need to explicitly set some of them
     * to non-null.
     * 
     * Irregularly shaped grids can not be resized after they're created. They
     * will throw an IrregularGridException if you try. 
     * 
     * @param empty the normal empty cell (though not the initial cell) 
     * @param numRows number of rows
     * @param numCols number of cells
     * @param y starting Y coord
     * @param x starting X coord
     * @param irregular true if irregular, false if regular
     * 
     */
    public Grid(Z empty, int numRows, int numCols, int y, int x, 
                boolean irregular) {
        this.irregular = irregular;
        grid = new ArrayList<ArrayList<Z>>();
        if (irregular) {
            this.empty = null;
        } else {
            this.empty = empty;
        }
        this.size_x = 0;
        this.size_y = 0;
        this.start_y = y;
        this.start_x = x;
        if (numRows > 0) {
            setSize(numRows, numCols);
        }
        if (irregular) {
            this.empty = empty;
        }
    }

    /**
     * Create a box with custom corners.
     * 
     * The order of the arguments is based upon Curses.
     * 
     * Any of the Z arguments can be <code>null</code> which will cause that
     * section to not be written within the grid. This allows layering of boxes
     * to produce reasonable results, including the option for use of 'T'
     * line cells where they will intersect.
     * 
     * This doesn't support irregularly shaped grids. No exceptions raised,
     * instead of will cause the grid to conform to the box shape.
     * 
     * @param height height of the box
     * @param width width of the box
     * @param x1 starting X position
     * @param y1 starting Y position
     * @param left left-most cell (4)
     * @param right right-most cell (6)
     * @param top top-most cell (8)
     * @param bottom bottom-most cell (2)
     * @param topleft top-left corner cell (7)
     * @param topright top-right corner cell (9)
     * @param bottomleft bottom-left corner cell (1)
     * @param bottomright bottom-right corner cell (3)
     * @param inside interior cell (5)
     */
    public void box(int height, int width, int y1, int x1, 
                    Z left, Z right, Z top, Z bottom, 
                    Z topleft, Z topright, Z bottomleft, Z bottomright, 
                    Z inside) {
        int y2 = y1 + height -1;
        int x2 = x1 + width -1;
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++){
                if (y == y1) {
                    if (x == x1){
                        if (topleft != null) setCopy(y, x, topleft);
                    } else if (x == x2) {
                        if (topright != null) setCopy(y, x, topright);
                    } else if (top != null) setCopy(y, x, top);
                } else if (y == y2) {
                    if (x == x1) {
                        if (bottomleft != null) setCopy(y, x, bottomleft);
                    } else if (x == x2) {
                        if (bottomright != null) setCopy(y, x, bottomright);
                    } else if (bottom != null) setCopy(y, x, bottom);
                } else if (x == x2) {
                    if (right != null) setCopy(y, x, right);
                } else if (x == x1) {
                    if (left != null) setCopy(y, x, left);
                } else if (inside != null) {
                    setCopy(y, x, inside);
                }
            }
        }
    }
    
    /**
     * Draw a box around the entirety of a region, ignoring region edge.
     * 
     * <p>Note that this function is called 'box' and not 'outline' or 'trace'.
     * A Regionlike item is capable of being much more than a box, but this
     * ignores all of that.</p>
     * 
     * <p>This is designed so that if the Z arguments were ('4', '6', '8', '2', 
     * '7', '9', '1', '3', '5'), it would yield:</p>
     * 
     * <pre>
     * 789
     * 456
     * 123
     * </pre>
     * 
     * This doesn't support irregularly shaped grids. No exceptions raised,
     * instead of will cause the grid to conform to the box shape.
     * 
     * @param region region which we plan to box
     * @param left left-most cell (4)
     * @param right right-most cell (6)
     * @param top top-most cell (8)
     * @param bottom bottom-most cell (2)
     * @param topleft top-left corner cell (7)
     * @param topright top-right corner cell (9)
     * @param bottomleft bottom-left corner cell (1)
     * @param bottomright bottom-right corner cell (3)
     * @param inside interior cell (5)
     */
    public void box(Regionlike region, 
                    Z left, Z right, Z top, Z bottom, 
                    Z topleft, Z topright, Z bottomleft, Z bottomright, 
                    Z inside) {
        box(region.getHeight(), region.getWidth(), 
                      region.getY(), region.getX(), 
                      left, right, top, bottom, topleft, topright, 
                      bottomleft, bottomright, inside);
    }

    /**
     * Call clear with the stored <code>empty</code> value.
     */
    public void clear() {
        clear(null);
    }

    /**
     * Clear the entire map, optionally using a new/different empty value.
     * 
     * If <code>empty</code> is null this will use the empty value from at 
     * map creation (or last clearing).
     * 
     * @param empty The exact contents of each cell on the map. 
     */
    public void clear(Z empty) {
        if (empty != null) this.empty = empty;
        resize(this.size_y, this.size_x, true);
    }
    /**
     * Is position (y, x) contained within the grid?
     * 
     * <p>This supports irregular grids. If a cell is <code>null</code> it is
     * not in the in the grid.</p>
     */
    @Override
    public boolean contains(int y, int x) {
        if (x >= getX() && x < getX() + getWidth()) {
            if (y >= getY() && y < getY() + getHeight()) {
                if (this.irregular) {
                    return get(y, x) != null;
                }
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#contains(int, int, int, int)
     */
    @Override
    public boolean contains(int height, int width, int y1, int x1) {
        return BoxRegion.contains(this, height, width, y1, x1);
    }
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#contains(com.googlecode.blacken.grid.Positionable)
     */
    @Override
    public boolean contains(Positionable p) {
        return contains(p.getY(), p.getX());
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#contains(com.googlecode.blacken.grid.Regionlike)
     */
    @Override
    public boolean contains(Regionlike r) {
        return BoxRegion.contains(this, r);
    }

    /**
     * get takes relative y, x offsets from start of grid
     * 
     * @param y row offset
     * @param x column offset
     * @return the Z at the given grid location
     */
    public Z get(int y, int x) {
        return grid.get(y - start_y).get(x - start_x);
    }
    /**
     * 
     * @param <T> a positionable type
     * @param pos a positionable item in the grid
     * @return the Z at the given grid location
     */
    public <T extends Positionable> Z get (T pos) {
        return grid.get(pos.getY() - start_y).get(pos.getX() - start_x); 
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#getBounds()
     */
    @Override
    public Regionlike getBounds() {
        return this;
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#getEdgeIterator()
     */
    @Override
    public RegionIterator getEdgeIterator() {
        RegionIterator ret = new BoxRegionIterator(this, true, false);
        return ret;
    }

    /**
     * Get the current empty cell
     * 
     * @return the current empty cell
     */
    public Z getEmpty() {
        return AbstractCopyable.copy(empty);
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#getHeight()
     */
    @Override
    public int getHeight() {
        return this.size_y;
    }
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#getInsideIterator()
     */
    @Override
    public RegionIterator getInsideIterator() {
        RegionIterator ret = new BoxRegionIterator(this, false, false);
        return ret;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#getNotOutsideIterator()
     */
    @Override
    public RegionIterator getNotOutsideIterator() {
        RegionIterator ret = new BoxRegionIterator(this, false, true);
        return ret;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#getPos()
     */
    @Override
    public int[] getPos() {
        int[] ret = {start_y, start_x};
        return ret;
    }

    /**
     * Get the size of the grid.
     * 
     * @return int[] {size_y, size_x}
     */
    public int[] getSize() {
        int[] ret = {size_y, size_x};
        return ret;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#getWidth()
     */
    @Override
    public int getWidth() {
        return this.size_x;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#getX()
     */
    @Override
    public int getX() {
        return start_x;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#getY()
     */
    @Override
    public int getY() {
        return start_y;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#intersects(int, int, int, int)
     */
    @Override
    public boolean intersects(int height, int width, int y1, int x1) {
        return BoxRegion.intersects(this, height, width, y1, x1);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#intersects(com.googlecode.blacken.grid.Regionlike)
     */
    @Override
    public boolean intersects(Regionlike room) {
        return BoxRegion.intersects(this, room);
    }

    /**
     * Make a new grid just like this one (but empty).
     * 
     * @return the new grid
     */
    public Grid<Z> like() {
        // not quite a copy, instead we want a similar map
        return new Grid<Z>(empty, size_y, size_x, start_y, start_x, irregular);
    }
    
    /**
     * <p>Turn this to an irregular grid.</p>
     * 
     * <p>This will cause the grid to raise an IrregularGridException if you
     * try to resize it. However, you will be able to set cells to 
     * <code>null</code> without raising a NullPointerException.</p>
     */
    public void makeIrregular() {
        if (this.irregular) return;
        this.irregular = true;
    }

    /**
     * Turn this to a regular grid.
     * 
     * This turns an irregular grid in to a regular grid by replacing the
     * <code>null</code> cells with empty cells.
     * 
     * @param filler used to fill <code>null</code> grid gaps
     */
    public void makeRegular(Z filler) {
        if (!this.irregular) return;
        if (filler == null) {
            if (this.empty == null) {
                throw new NullPointerException();
            }
            filler = this.empty;
        }
        for (int y = start_y; y < start_y + size_y; y++) {
            for (int x = start_x; x < start_x + size_x; x++) {
                Z c = get(y, x);
                if (c == null) {
                    set(y, x, AbstractCopyable.copy(filler));
                }
            }
        }
        this.irregular = false;
    }

    /**
     * Reset the grid.
     * 
     * <p>While this does reset the grid using the new empty value (if not
     * null -- if null it leaves the empty value untouched). It does not
     * alter the regularity of the grid. An irregular grid remains irregular,
     * even though this function may be setting every cell to a non-null 
     * value.</p>
     * 
     * @param ysize new row count
     * @param xsize new column count
     * @param empty new cell template
     */
    public void reset(int ysize, int xsize, Z empty) {
        if (empty != null) {
            this.empty = AbstractCopyable.copy(empty);
        }
        resize(ysize, xsize, true);
    }

    /**
     * Resize the grid, optionally wiping it
     * 
     * @param ysize new Y size
     * @param xsize new X size
     * @param wipe true to wipe; false to leave existing cells
     */
    protected void resize(int ysize, int xsize, boolean wipe) {
        if (ysize == -1) ysize = this.size_y;
        if (xsize == -1) xsize = this.size_x;
        if (this.irregular) {
            if (!wipe) {
                if (size_x == 0 && size_y == 0) {
                    // legal -- we're doing the initial sizing
                } else if (ysize != size_y || xsize != size_x) {
                    throw new IrregularGridException("Can not change the size of an irregular map."); //$NON-NLS-1$
                }
            }
        } else if ((size_x != xsize || size_y != ysize) && this.empty == null) {
            throw new NullPointerException();
        }
        int x2 = Math.min(size_x, xsize);
        int y2 = Math.min(size_y, ysize);
        ArrayList<ArrayList<Z>> old_grid = grid;
        if (wipe) old_grid = null;
        ArrayList<ArrayList<Z>> new_grid = new ArrayList<ArrayList<Z>>();
        if(ysize != 0) new_grid.ensureCapacity(ysize);
        for (int y = 0; y < ysize; y++) {
            ArrayList<Z> grid_line = new ArrayList<Z>();
            if (xsize != 0) grid_line.ensureCapacity(xsize);
            if (y < y2 && x2 > 0) {
                if (wipe && this.irregular) {
                    for (int x = 0; x < x2; x++) {
                        if (old_grid != null && old_grid.get(y).get(x) == null) {
                            grid_line.add(null);
                        } else {
                            grid_line.add(AbstractCopyable.copy(empty));
                        }
                    }
                } else if (!wipe && old_grid != null) {
                    grid_line.addAll(old_grid.get(y).subList(0, x2));
                }
            }
            int x1 = grid_line.size();
            for (int x = x1; x < xsize; x++) {
                grid_line.add(AbstractCopyable.copy(empty));
            }
            new_grid.add(grid_line);
        }
        grid = new_grid;
        size_x = xsize;
        size_y = ysize;
    }

    /**
     * Set a cell to a value.
     * 
     * <p?This supports irregular grid shapes. Irregularly shaped grids can 
     * use this function to set a cell to <code>null</code>. Normal grids 
     * will have a <code>NullPointerException</code> thrown if they try.
     * 
     * @param y coordinate
     * @param x coordinate
     * @param value new value for cell
     * @return previous cell value
     */
    public Z set(int y, int x, Z value) {
        if (value == null) {
            if (!this.irregular) {
                throw new NullPointerException("Use 'unset' to clear a cell."); //$NON-NLS-1$
            }
        }
        return grid.get(y - start_y).set(x - start_x, value); 
    }

    /**
     * Set a cell to a value.
     * 
     * This supports irregular grid shapes. Irregularly shaped grids can use
     * this function to set a cell to <code>null</code>. Normal grids will
     * have a NullPointerException thrown if they try.
     * 
     * @param <T> A Positionable type
     * @param pos the position of a cell
     * @param value a new value for cell
     * @return previous cell value
     */
    public <T extends Positionable> Z set (T pos, Z value) {
        if (value == null) {
            if (!this.irregular) {
                throw new NullPointerException("Use 'unset' to clear a cell."); //$NON-NLS-1$
            }
        }
        return grid.get(pos.getY() - start_y).set(pos.getX() - start_x, value); 
    }

    /**
     * Set the cell to a copy of the <code>value</code>.
     * 
     * @param y coordinate
     * @param x coordinate
     * @param value new cell value
     * @return old cell value.
     */
    public Z setCopy(int y, int x, Z value) {
        int y1 = y - start_y;
        int x1 = x - start_x;
        if (value == null) {
            if (!this.irregular) {
                throw new NullPointerException("Use 'unset' to clear a cell."); //$NON-NLS-1$
            }
            return grid.get(y1).set(x1, null);
        }
        return grid.get(y1).set(x1, AbstractCopyable.copy(value));
    }

    /**
     * Set a cell to a copy of a <code>value</code>.
     * 
     * @param <T> positionable item
     * @param pos position
     * @param value new cell value
     * @return old cell value
     */
    public <T extends Positionable> Z setCopy (T pos, Z value) {
        int y1 = pos.getY() - start_y;
        int x1 = pos.getX() - start_x;
        if (value == null) {
            if (!this.irregular) {
                throw new NullPointerException("Use 'unset' to clear a cell."); //$NON-NLS-1$
            }
            return grid.get(y1).set(x1, null); 
        }
        return grid.get(y1).set(x1, AbstractCopyable.copy(value));
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#setHeight(int)
     */
    @Override
    public void setHeight(int height) {
        resize(height, this.size_x, false);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#setPos(int, int)
     */
    @Override
    public void setPos(int y, int x) {
        this.setX(x);
        this.setY(y);
    }
    
    /**
     * Set the size.
     * 
     * @param ysize new Y size
     * @param xsize new X size
     */
    public void setSize(int ysize, int xsize) {
        resize(ysize, xsize, false);
    }

    /**
     * Set the size.
     * 
     * @param size new size
     */
    public void setSize(int[] size) {
        if (size.length == 2) {
            setSize(size[0], size[1]);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#setWidth(int)
     */
    @Override
    public void setWidth(int width) {
        resize(this.size_y, width, false);
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#setX(int)
     */
    @Override
    public void setX(int x) {
        start_x = x;
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#setY(int)
     */
    @Override
    public void setY(int y) {
        start_y = y;
    }

    /**
     * <p>Pull a copy of a grid in to a subgrid (by quad ints).</p>
     * 
     * <p>While the returned grid doesn't contain any <code>null</code>'s it is
     * still returned as an IrregularGrid. This allows the subgrid to have
     * segments which have been removed (useful if the subgrid will be 
     * reapplied to the grid).</p>
     * 
     * <p>The returned grid has the starting upper-left coordinate equal to
     * the base coordinate (y1, x1). That is, if a cell is at 123,345 in
     * the original grid, it will still be in that cell in the subgrid.</p>
     * 
     * @param numRows number of rows
     * @param numCols number of columns
     * @param y1 start Y coordinate
     * @param x1 start X coordinate
     * @return returns the new subgrid
     */
    public Grid<Z> subGrid(int numRows, int numCols, int y1, int x1) {
        Grid<Z> ret = new Grid<Z>(this.empty, numRows, numCols, y1, x1, true);
        for (int y = y1; y < y1 + numRows; y++) {
            for (int x = x1; x < x1 + numCols; x++) {
                if (!contains(y, x)) {
                    continue;
                }
                ret.setCopy(y, x, get(y, x));
            }
        }
        return ret;
    }

    /**
     * Pull a copy of a grid in to a subgrid (by region).
     * 
     * <p>The returned grid is an irregular grid. That is, the cells outside of
     * the region are <code>null</code>.</p>
     * 
     * <p>The returned grid has the starting upper-left coordinate equal to
     * the base coordinate of the region. That is, if a cell is at 123,345 in
     * the original grid, it will still be in that cell in the subgrid.</p>
     * 
     * @param r region used for location of subgrid
     * @return new grid
     */
    public Grid<Z> subGrid(Regionlike r) {
        Grid<Z> ret = new Grid<Z>(this.empty, r.getHeight(), r.getWidth(), 
                                  r.getY(), r.getX());

        RegionIterator inside = r.getNotOutsideIterator();
        int[] p = new int[4];
        boolean[] pattern = null;
        int segtype;
        
        while (!inside.isDone()) {
            segtype = inside.currentSegment(p);
            if (segtype == RegionIterator.SEG_BORDER_SOLID || 
                    segtype == RegionIterator.SEG_INSIDE_SOLID) {
                for (int y = p[0]; y <= p[2]; y++) {
                    for (int x = p[1]; x <= p[3]; x++) {
                        if (!contains(y, x)) {
                            continue;
                        }
                        ret.setCopy(y, x, get(y, x));
                    }
                }
            } else if (segtype == RegionIterator.SEG_BORDER_PATTERNED || 
                    segtype == RegionIterator.SEG_INSIDE_PATTERNED) {
                int pidx = -1;
                pattern = inside.currentPattern();
                for (int y = p[0]; y <= p[2]; y++) {
                    for (int x = p[1]; x <= p[3]; x++) {
                        pidx++;
                        if (pidx >= pattern.length) pidx = 0;
                        if (!pattern[pidx]) continue;
                        if (!contains(y, x)) {
                            continue;
                        }
                        ret.setCopy(y, x, get(y, x));
                    }
                }
            } else if (segtype == RegionIterator.SEG_COMPLETE) {
                // should never happen, but just in case...
                break;
            }
            inside.next();
        }
        
        return ret;
    }

    /**
     * <p>Set the (y, x) coordinate to the stored empty cell.</p>
     * 
     * @param y coordinate
     * @param x coordinate
     * @return previous value for cell
     */
    public Z unset(int y, int x) {
        return setCopy(y, x, empty);
    }

    /**
     * Set the (Positionable) coordinate to the stored empty cell.
     * 
     * @param pos position of the cell
     * @return previous value for cell
     */
    public Z unset(Positionable pos) {
        return setCopy(pos.getY(), pos.getX(), empty);
    }

    /**
     * Clear a specific region using the default empty value.
     * 
     * @param height height to wipe
     * @param width width to wipe
     * @param y1 coordinate to start
     * @param x1 coordinate to start
     */
    public void wipe(int height, int width, int y1, int x1) {
        wipe(height, width, y1, x1, empty);
    }

    /**
     * This is like clear(), but for a specific region.
     * 
     * This works with irregular regions. For null cells it doesn't overwrite
     * anything.
     * 
     * @param height height of the region
     * @param width width of the region
     * @param y1 anchor point (upper-left corner)
     * @param x1 anchor point (upper-left corner)
     * @param empty new empty value
     */
    public void wipe(int height, int width, int y1, int x1, Z empty) {
        for (int y = y1; y < y1 + height; y++) {
            for (int x = x1; x < x1 + width; x++) {
                if (grid.get(y - this.start_y).get(x - this.start_x) != null) {
                    grid.get(y - this.start_y).set(x - this.start_x, 
                                                   AbstractCopyable.copy(empty));
                }
            }
        }
    }

    /**
     * Add a grid on top of the existing grid.
     * 
     * <p>This works best when the grid to copy is an irregular grid which
     * contains transparent values.</p>
     * 
     * @param tgrid grid
     */
    public void addGrid(Grid<Z> tgrid) {
        for (int y = 0; y < tgrid.size_y ; y++) {
            int offsety = y + tgrid.start_y - this.start_y;
            if (offsety < 0 || offsety >= this.size_y) {
                continue;
            }
            for (int x = 0; x < tgrid.size_x; x++) {
                int offsetx = x + tgrid.start_x - this.start_x;
                if (offsetx < 0 || offsetx >= this.size_x) {
                    continue;
                }
                this.setCopy(y + tgrid.start_x, x + tgrid.start_x, 
                             tgrid.get(y + tgrid.start_y, x + tgrid.start_x));
            }
        }
    }

    /**
     * Move a block of cells.
     * 
     * @param numRows number of rows to move
     * @param numCols number of columns to move
     * @param origY original Y coordinate
     * @param origX original X coordinate
     * @param newY new Y coordinate
     * @param newX new X coordinate
     * @param resetCell class/function to refresh a cell
     */
    public void moveBlock(int numRows, int numCols, int origY, int origX,
                          int newY, int newX, ResetGridCell<Z> resetCell) {
        this.copyFrom(this, numRows, numCols, origY, origX, newY, newX, 
                      resetCell);
    }

    /**
     * Move a block of cells.
     * 
     * @param source source grid
     * @param numRows number of rows to move
     * @param numCols number of columns to move
     * @param startY start Y coordinate
     * @param startX start X coordinate
     * @param destY destination Y coordinate
     * @param destX destination X coordinate
     * @param resetCell class/function to refresh a cell
     */
    public void copyFrom(Grid<Z> source, int numRows, int numCols, 
                         int startY, int startX, int destY, int destX, 
                         ResetGridCell<Z> resetCell) {
        Grid<Z> tgrid = source.subGrid(numRows, numCols, startY, startX);
        if (source == this) {
            this.wipe(numRows, numCols, startY, startX);
        }
        if (resetCell != null) {
            for (int y = 0; y < numRows; y++) {
                for (int x = 0; x < numCols; x++) {
                    Z cell = tgrid.grid.get(y).get(x); 
                    if (cell == null) {
                        continue;
                    }
                    resetCell.reset(cell);
                }
            }
        }
        tgrid.setPos(destY, destX);
        this.addGrid(tgrid);
    }
    
}
