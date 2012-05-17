/* blacken - a library for Roguelike games
 * Copyright © 2010-2012 Steven Black <yam655@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with this program.  
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.blacken.grid;

/**
 * This is a box-like region.
 * 
 * @author yam655
 */
public class BoxRegion implements Regionlike {

    /**
     * Helper function to perform a simple box-check to see if a position is in
     * a region.
     * 
     * @param self region to check
     * @param y coordinate
     * @param x coordinate
     * @return whether the coordinate is in the region or not
     */
    public static boolean contains(Regionlike self, int y, int x) {
        if (x >= self.getX() && x < self.getX() + self.getWidth()) {
            if (y >= self.getY() && y < self.getY() + self.getHeight()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Helper function to perform a simple box-check to see if a box is in
     * a region.
     * 
     * @param self region to check
     * @param height height
     * @param width width
     * @param y1 upper right Y position
     * @param x1 upper right X position
     * @return whether the box is in the region or not
     */
    static public boolean contains(Regionlike self, 
                                   int height, int width, int y1, int x1) {
        boolean ret = true;
        int y2 = y1 + height -1;
        int x2 = x1 + width -1;
        out:
            for (int y = y1; y <= y2; y++) {
                for (int x = x1; x <= x2; x++) {
                    if (!self.contains(y, x)) {
                        ret = false;
                        break out;
                    }
                }
            }
            return ret;
    }
    
    /**
     * Helper function to perform a simple box-check to see if a region is in
     * a region.
     * 
     * @param self container region to check
     * @param r contained region to check
     * @return whether the box is in the region or not
     */
    public static boolean contains(Regionlike self, Regionlike r) {
        boolean ret = true;
        RegionIterator edge = r.getEdgeIterator();
        int[] p = new int[4];
        boolean[] pattern = null;
        int segtype;
        
    all_out:
        while (!edge.isDone()) {
            segtype = edge.currentSegment(p);
            if (segtype == RegionIterator.SEG_BORDER_SOLID || 
                    segtype == RegionIterator.SEG_INSIDE_SOLID) {
                for (int y = p[0]; y <= p[2]; y++) {
                    for (int x = p[1]; x <= p[3]; x++) {
                        if (!self.contains(y, x)) {
                            ret = false;
                            break all_out;
                        }
                    }
                }
            } else if (segtype == RegionIterator.SEG_BORDER_PATTERNED || 
                    segtype == RegionIterator.SEG_INSIDE_PATTERNED) {
                int pidx = -1;
                pattern = edge.currentPattern();
                for (int y = p[0]; y <= p[2]; y++) {
                    for (int x = p[1]; x <= p[3]; x++) {
                        pidx++;
                        if (pidx >= pattern.length) pidx = 0;
                        if (!pattern[pidx]) continue;
                        if (!self.contains(y, x)) {
                            ret = false;
                            break all_out;
                        }
                    }
                }
            } else if (segtype == RegionIterator.SEG_COMPLETE) {
                // should never happen, but just in case...
                break;
            }
            edge.next();
        }
        return ret;
    }

    /**
     * Check to see if a box intersects a region.
     * 
     * <p>To intersect, there must be parts both inside the region as well as 
     * outside.</p>
     * 
     * @param self region to check
     * @param height height
     * @param width width
     * @param y1 upper right Y position
     * @param x1 upper right X position
     * @return whether the box intersects the region or not
     */
    static public boolean intersects(Regionlike self, 
                                     int height, int width, int y1, int x1) {
        boolean does_contain = false;
        boolean does_not_contain = false;
        int y2 = y1 + height -1;
        int x2 = x1 + width -1;
    out:
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                if (self.contains(y, x)) {
                    does_contain = true;
                    if (does_not_contain) break out;
                } else {
                    does_not_contain = true;
                    if (does_contain) break out;
                }
            }
        }
        return does_contain && does_not_contain;
    }

    /**
     * Helper function to perform a simple box-check to see if a region 
     * intersects a region.
     * 
     * @param self container region to check
     * @param room contained region to check
     * @return whether the box is in the region or not
     */
    public static boolean intersects(Regionlike self, Regionlike room) {
        boolean does_contain = false;
        boolean does_not_contain = false;
        RegionIterator edge = room.getNotOutsideIterator();
        int[] p = new int[4];
        boolean[] pattern = null;
        int segtype;
        
    all_out:
        while (!edge.isDone()) {
            segtype = edge.currentSegment(p);
            if (segtype == RegionIterator.SEG_BORDER_SOLID || 
                    segtype == RegionIterator.SEG_INSIDE_SOLID) {
                for (int y = p[0]; y <= p[2]; y++) {
                    for (int x = p[1]; x <= p[3]; x++) {
                        if (self.contains(y, x)) {
                            does_contain = true;
                            if (does_not_contain) break all_out;
                        } else {
                            does_not_contain = true;
                            if (does_contain) break all_out;
                        }
                    }
                }
            } else if (segtype == RegionIterator.SEG_BORDER_PATTERNED || 
                    segtype == RegionIterator.SEG_INSIDE_PATTERNED) {
                int pidx = -1;
                pattern = edge.currentPattern();
                for (int y = p[0]; y <= p[2]; y++) {
                    for (int x = p[1]; x <= p[3]; x++) {
                        pidx++;
                        if (pidx >= pattern.length) pidx = 0;
                        if (!pattern[pidx]) continue;
                        if (self.contains(y, x)) {
                            does_contain = true;
                            if (does_not_contain) break all_out;
                        } else {
                            does_not_contain = true;
                            if (does_contain) break all_out;
                        }
                    }
                }
            } else if (segtype == RegionIterator.SEG_COMPLETE) {
                // should never happen, but just in case...
                break;
            }
            edge.next();
        }
        return does_contain && does_not_contain;
    }
    
    private int size_y;
    private int size_x;
    private int start_x;
    private int start_y;

    /**
     * Create a simple box region.
     * 
     * <p>The starting Y, X coords are 0, 0.</p>
     * 
     * @param height height of box
     * @param width width of box
     */
    public BoxRegion(int height, int width) {
        start_x = 0;
        start_y = 0;
        this.size_y = height;
        this.size_x = width;
    }

    /**
     * Create a box region.
     * 
     * @param height height of box
     * @param width width of box
     * @param y1 starting Y coordinate
     * @param x1 starting X coordinate
     */
    public BoxRegion(int height, int width, int y1, int x1) {
        this.start_x = x1;
        this.start_y = y1;
        this.size_y = height;
        this.size_x = width;
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#contains(int, int)
     */
    @Override
    public boolean contains(int y, int x) {
        return contains(this, y, x);
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
        return contains(this, r);
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
        return intersects(this, room);
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#setHeight(int)
     */
    @Override
    public void setHeight(int height) {
        this.size_y = height;
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#setPos(int, int)
     */
    @Override
    public void setPosition(int y, int x) {
        setY(y);
        setX(x);
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.Regionlike#setWidth(int)
     */
    @Override
    public void setWidth(int width) {
        this.size_x = width;
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
    /* (non-Javadoc)
     * @see com.googlecode.blacken.grid.Positionable#setPosition(com.googlecode.blacken.grid.Positionable)
     */
    @Override
    public void setPosition(Positionable point) {
        this.setX(point.getX());
        this.setY(point.getY());
    }
    /* (non-Javadoc)
     * @see com.googlecode.blacken.grid.Sizable#setSize(int, int)
     */
    @Override
    public void setSize(int height, int width) {
        this.setHeight(height);
        this.setWidth(width);        
    }
    /* (non-Javadoc)
     * @see com.googlecode.blacken.grid.Sizable#setSize(com.googlecode.blacken.grid.Sizable)
     */
    @Override
    public void setSize(Sizable size) {
        this.setHeight(size.getHeight());
        this.setWidth(size.getWidth());
    }

}
