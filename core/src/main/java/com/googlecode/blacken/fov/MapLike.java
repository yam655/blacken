package com.googlecode.blacken.fov;

public interface MapLike {

    /** A simple interface to allow custom map structures to use the Blacken FOV toolkit.
     *  Any map structure implementing it must provide the listed methods for checking wall-ness, indicating a visible tile and determining map size.
     *  @author XLambda
     */

    /** When implemented by the map structure, this method should return whether a given map tile blocks field of view or not.
     *  @param x the x ordinate of the map tile
     *  @param y the y ordinate of the map tile
     *  @return does the tile (x,y) block fov? returns true if yes, false otherwise.
     */

    public boolean blocksFOV(int x, int y);

    /** When implemented by the map structure, this method will be called on all map tiles that are visible according to the FOV algorithm.
     *  @param x the x ordinate of the map tile
     *  @param y the y ordinate of the map tile
     */

    public void touch(int x, int y);

    /** When implemented by the map structure, this method should return the overall height of the map.
     *  @return the map height
     */

    public int getHeight();

    /** When implemented by the map structure, this method should return the overall width of the map.
     *  @return the map width
     */

    public int getWidth();
}