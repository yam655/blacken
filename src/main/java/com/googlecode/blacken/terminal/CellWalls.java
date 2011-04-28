package com.googlecode.blacken.terminal;

import java.util.EnumSet;

public enum CellWalls {
    TOP,
    LEFT,
    BOTTOM,
    RIGHT,
    CELL_WALLS_CENTER_TOP,
    CELL_WALLS_CENTER_LEFT,
    CELL_WALLS_CENTER_BOTTOM,
    CELL_WALLS_CENTER_RIGHT;
    public static final EnumSet<CellWalls> CELL_WALLS_HORIZONTAL = 
        EnumSet.of(CELL_WALLS_CENTER_LEFT, CELL_WALLS_CENTER_RIGHT);
    public static final EnumSet<CellWalls> CELL_WALLS_VERTICAL = 
        EnumSet.of(CELL_WALLS_CENTER_TOP, CELL_WALLS_CENTER_BOTTOM);
}
