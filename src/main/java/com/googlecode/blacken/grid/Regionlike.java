package com.googlecode.blacken.grid;

/**
 * A two-dimensional region.
 * 
 * @author Steven Black
 *
 */
public interface Regionlike extends Positionable {
    public abstract boolean contains(int y, int x);
    public abstract boolean contains(int height, int width, int y1, int x1);
    public abstract boolean contains(Positionable p);
    public abstract boolean contains(Regionlike r);
    public abstract Regionlike getBounds();
    public abstract RegionIterator getEdgeIterator();
    public abstract int getHeight();
    public abstract RegionIterator getInsideIterator();
    public abstract RegionIterator getNotOutsideIterator();
    public abstract int getWidth();
    public abstract boolean intersects(int height, int width, int y1, int x1);
    public abstract boolean intersects(Regionlike room);
    public abstract void setHeight(int height);
    public abstract void setWidth(int width);
}
