package com.googlecode.blacken.grid;


public class BoxRegion implements Regionlike {

    /**
     * Helper function to perform a simple box-check to see if a position is in
     * a region.
     * 
     * @param self region to check
     * @param y coordinate
     * @param x coordinate
     * @return whether the coordinate in the region or not
     */
    public static boolean contains(Regionlike self, int y, int x) {
        if (x >= self.getX() && x < self.getX() + self.getWidth()) {
            if (y >= self.getY() && y < self.getY() + self.getHeight()) {
                return true;
            }
        }
        return false;
    }
    static public boolean contains(Regionlike self, int height, int width, int y1, int x1) {
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

    public BoxRegion(int height, int width) {
        start_x = 0;
        start_y = 0;
        this.size_y = height;
        this.size_x = width;
    }

    public BoxRegion(int height, int width, int y1, int x1) {
        this.start_x = x1;
        this.start_y = y1;
        this.size_y = height;
        this.size_x = width;
    }

    @Override
    public boolean contains(int y, int x) {
        return contains(this, y, x);
    }

    @Override
    public boolean contains(int height, int width, int y1, int x1) {
        return BoxRegion.contains(this, height, width, y1, x1);
    }

    @Override
    public boolean contains(Positionable p) {
        return contains(p.getY(), p.getX());
    }

    @Override
    public boolean contains(Regionlike r) {
        return contains(this, r);
    }

    @Override
    public Regionlike getBounds() {
        return this;
    }
    @Override
    public RegionIterator getEdgeIterator() {
        RegionIterator ret = new BoxRegionIterator(this, true, false);
        return ret;
    }

    @Override
    public int getHeight() {
        return this.size_y;
    }

    @Override
    public RegionIterator getInsideIterator() {
        RegionIterator ret = new BoxRegionIterator(this, false, false);
        return ret;
    }

    @Override
    public RegionIterator getNotOutsideIterator() {
        RegionIterator ret = new BoxRegionIterator(this, false, true);
        return ret;
    }

    @Override
    public int[] getPos() {
        int[] ret = {start_y, start_x};
        return ret;
    }
    @Override
    public int getWidth() {
        return this.size_x;
    }

    @Override
    public int getX() {
        return start_x;
    }

    @Override
    public int getY() {
        return start_y;
    }

    @Override
    public boolean intersects(int height, int width, int y1, int x1) {
        return BoxRegion.intersects(this, height, width, y1, x1);
    }

    @Override
    public boolean intersects(Regionlike room) {
        return intersects(this, room);
    }
    @Override
    public void setHeight(int height) {
        this.size_y = height;
        
    }
    @Override
    public void setPos(int y, int x) {
        setY(y);
        setX(x);
    }
    @Override
    public void setWidth(int width) {
        this.size_x = width;
    }
    @Override
    public void setX(int x) {
        start_x = x;
    }
    @Override
    public void setY(int y) {
        start_y = y;
    }

}
