/**
 * 
 */
package com.googlecode.blacken.grid;

/**
 * @author Steven Black
 *
 */
public interface Positionable {
    public int getX();
    public int getY();
    // public int getStackingOrder();
    public void setX(int x);
    public void setY(int y);
    // public void setStackingOrder(int stacking_order);
    /**
     * Returns an array containing the X and Y positions in that order.
     * 
     * @return [x, y]
     */
    public int[] getPos();
    public void setPos(int y, int x);
}
