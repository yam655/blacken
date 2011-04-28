package com.googlecode.blacken.grid;

/**
 * This primarily exists as a concrete type usable to test Positionable.
 * 
 * @author Steven Black
 *
 */
public class Point implements Positionable {

    private int x;
    private int y;

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(int y, int x) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int[] getPos() {
        int ret[] = {y, x};
        return ret;
    }

    @Override
    public void setPos(int y, int x) {
        this.setX(x);
        this.setY(y);
    }

}
