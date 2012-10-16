/* blacken - a library for Roguelike games
 * Copyright © 2012 Steven Black <yam655@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.googlecode.blacken.terminal.widgets;

import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.terminal.BlackenCodePoints;
import com.googlecode.blacken.terminal.CellWalls;
import com.googlecode.blacken.terminal.TerminalCellTemplate;
import com.googlecode.blacken.terminal.TerminalViewInterface;
import com.googlecode.blacken.terminal.editing.CodepointCallbackInterface;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A widget creating an inset box around another widget.
 *
 * <p>Also functions as container class for static functions performing
 * generic box drawing.
 *
 * @author Steven Black
 */
public class Box extends SimpleWidgetContainer implements BlackenWidgetResizer {
    private final static Map<BoxMethod, List<TerminalCellTemplate>> methodMap;
    private BoxMethod method;
    private int rowInset = 1;
    private int colInset = 1;

    @Override
    public void resizeWidgets(Map<String, BlackenWidget> widgetMap) {
        Regionlike bounds = this.getBounds();
        BoxRegion.inset(bounds, rowInset, colInset);
        if (widgetMap != null && !widgetMap.isEmpty()) {
            BlackenWidget widget = widgetMap.values().iterator().next();
            widget.setBounds(bounds);
            CodepointCallbackInterface callback = widget.acceptsFocus();
            if (callback != null) {
                callback.handleResizeEvent();
            }
        }
    }
    public enum BoxMethod {
        INSIDE_WALL,
        OUTSIDE_WALL,
        CENTER_WALL,
        SINGLE_UNICODE,
        DOUBLE_UNICODE,
        HEAVY_UNICODE,
        LIGHT_DOUBLE_DASH_UNICODE,
        REMOVE_UNICODE,
        REMOVE_WALL,
        INSET_ONLY,
    };
    static {
        methodMap = new HashMap<>(BoxMethod.values().length);
        methodMap.put(BoxMethod.LIGHT_DOUBLE_DASH_UNICODE, Arrays.asList(
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_DOUBLE_DASH_VERTICAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_DOUBLE_DASH_VERTICAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_DOUBLE_DASH_HORIZONTAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_DOUBLE_DASH_HORIZONTAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_DOWN_AND_RIGHT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_DOWN_AND_LEFT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_UP_AND_RIGHT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_UP_AND_LEFT, null, null),
                null));
        methodMap.put(BoxMethod.HEAVY_UNICODE, Arrays.asList(
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_HEAVY_VERTICAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_HEAVY_VERTICAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_HEAVY_HORIZONTAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_HEAVY_HORIZONTAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_HEAVY_DOWN_AND_RIGHT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_HEAVY_DOWN_AND_LEFT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_HEAVY_UP_AND_RIGHT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_HEAVY_UP_AND_LEFT, null, null),
                null));
        methodMap.put(BoxMethod.SINGLE_UNICODE, Arrays.asList(
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_VERTICAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_VERTICAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_HORIZONTAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_HORIZONTAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_DOWN_AND_RIGHT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_DOWN_AND_LEFT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_UP_AND_RIGHT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_LIGHT_UP_AND_LEFT, null, null),
                null));
        methodMap.put(BoxMethod.DOUBLE_UNICODE, Arrays.asList(
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_DOUBLE_VERTICAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_DOUBLE_VERTICAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_DOUBLE_HORIZONTAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_DOUBLE_HORIZONTAL, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_DOUBLE_DOWN_AND_RIGHT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_DOUBLE_DOWN_AND_LEFT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_DOUBLE_UP_AND_RIGHT, null, null),
                new TerminalCellTemplate(BlackenCodePoints
                    .CODEPOINT_BOX_DRAWINGS_DOUBLE_UP_AND_LEFT, null, null),
                null));
        methodMap.put(BoxMethod.REMOVE_UNICODE, Arrays.asList(
                new TerminalCellTemplate("", null, null),
                new TerminalCellTemplate("", null, null),
                new TerminalCellTemplate("", null, null),
                new TerminalCellTemplate("", null, null),
                new TerminalCellTemplate("", null, null),
                new TerminalCellTemplate("", null, null),
                new TerminalCellTemplate("", null, null),
                new TerminalCellTemplate("", null, null),
                null));
        methodMap.put(BoxMethod.CENTER_WALL, Arrays.asList(
                new TerminalCellTemplate(null, null, null, null,
                            CellWalls.VERTICAL),
                new TerminalCellTemplate(null, null, null, null,
                            CellWalls.VERTICAL),
                new TerminalCellTemplate(null, null, null, null,
                            CellWalls.HORIZONTAL),
                new TerminalCellTemplate(null, null, null, null,
                            CellWalls.HORIZONTAL),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.of(CellWalls.CENTER_TO_BOTTOM,
                                       CellWalls.CENTER_TO_RIGHT)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.of(CellWalls.CENTER_TO_BOTTOM,
                                       CellWalls.CENTER_TO_LEFT)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.of(CellWalls.CENTER_TO_TOP,
                                       CellWalls.CENTER_TO_RIGHT)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.of(CellWalls.CENTER_TO_TOP,
                                       CellWalls.CENTER_TO_LEFT)),
                null));
        methodMap.put(BoxMethod.OUTSIDE_WALL, Arrays.asList(
                new TerminalCellTemplate(null, null, null, null, CellWalls.LEFT),
                new TerminalCellTemplate(null, null, null, null, CellWalls.RIGHT),
                new TerminalCellTemplate(null, null, null, null, CellWalls.TOP),
                new TerminalCellTemplate(null, null, null, null, CellWalls.BOTTOM),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.of(CellWalls.TOP, CellWalls.LEFT)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.of(CellWalls.TOP, CellWalls.RIGHT)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.of(CellWalls.BOTTOM, CellWalls.LEFT)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.of(CellWalls.BOTTOM, CellWalls.RIGHT)),
                null));
        methodMap.put(BoxMethod.INSIDE_WALL, Arrays.asList(
                new TerminalCellTemplate(null, null, null, null, CellWalls.RIGHT),
                new TerminalCellTemplate(null, null, null, null, CellWalls.LEFT),
                new TerminalCellTemplate(null, null, null, null, CellWalls.BOTTOM),
                new TerminalCellTemplate(null, null, null, null, CellWalls.TOP),
                null, null, null, null, null));
        methodMap.put(BoxMethod.REMOVE_WALL, Arrays.asList(
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.noneOf(CellWalls.class)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.noneOf(CellWalls.class)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.noneOf(CellWalls.class)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.noneOf(CellWalls.class)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.noneOf(CellWalls.class)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.noneOf(CellWalls.class)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.noneOf(CellWalls.class)),
                new TerminalCellTemplate(null, null, null, null,
                            EnumSet.noneOf(CellWalls.class)),
                null));
        methodMap.put(BoxMethod.INSET_ONLY, Arrays.asList(
                (TerminalCellTemplate)null, null, null, null, null,
                null, null, null, null));
    }
    public Box(String name, Regionlike bounds, BoxMethod method) {
        super(name, bounds);
        this.method = method;
    }
    @Override
    public void addWidget(BlackenWidget widget) {
        if (this.getResizer() == null) {
            this.setResizer(this);
        }
        if (!isEmpty()) {
            throw new UnsupportedOperationException("Remove the existing widget");
        }
        super.addWidget(widget);
    }

    public BoxMethod getMethod() {
        return method;
    }

    public void setMethod(BoxMethod method) {
        this.method = method;
    }

    public static void box(TerminalViewInterface view, BoxMethod method, Regionlike bounds) {
        List<TerminalCellTemplate> sides = methodMap.get(method);
        Box.box(view, bounds, sides.get(0), sides.get(1), sides.get(2),
                sides.get(3), sides.get(4), sides.get(5), sides.get(6),
                sides.get(7), sides.get(8));
    }

    @Override
    public void redraw(TerminalViewInterface view) {
        super.redraw(view);
        List<TerminalCellTemplate> sides = methodMap.get(method);
        Box.box(view, this, sides.get(0), sides.get(1), sides.get(2),
                sides.get(3), sides.get(4), sides.get(5), sides.get(6),
                sides.get(7), sides.get(8));
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
     * @param view view where the box is created
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
    static public void box(TerminalViewInterface view,
                    int height, int width, int y1, int x1,
                    Integer left, Integer right, Integer top, Integer bottom,
                    Integer topleft, Integer topright, Integer bottomleft,
                    Integer bottomright,
                    Integer inside) {
        int y2 = y1 + height -1;
        int x2 = x1 + width -1;
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++){
                if (y == y1) {
                    if (x == x1){
                        if (topleft != null) {
                            view.get(y, x).setSequence(topleft);
                        }
                    } else if (x == x2) {
                        if (topright != null) {
                            view.get(y, x).setSequence(topright);
                        }
                    } else if (top != null) {
                        view.get(y, x).setSequence(top);
                    }
                } else if (y == y2) {
                    if (x == x1) {
                        if (bottomleft != null) {
                            view.get(y, x).setSequence(bottomleft);
                        }
                    } else if (x == x2) {
                        if (bottomright != null) {
                            view.get(y, x).setSequence(bottomright);
                        }
                    } else if (bottom != null) {
                        view.get(y, x).setSequence(bottom);
                    }
                } else if (x == x2) {
                    if (right != null) {
                        view.get(y, x).setSequence(right);
                    }
                } else if (x == x1) {
                    if (left != null) {
                        view.get(y, x).setSequence(left);
                    }
                } else if (inside != null) {
                    view.get(y, x).setSequence(inside);
                }
            }
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
     * @param view view where the box is created
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
    static public void box(TerminalViewInterface view,
                    int height, int width, int y1, int x1,
                    TerminalCellTemplate left, TerminalCellTemplate right,
                    TerminalCellTemplate top, TerminalCellTemplate bottom,
                    TerminalCellTemplate topleft, TerminalCellTemplate topright,
                    TerminalCellTemplate bottomleft,
                    TerminalCellTemplate bottomright,
                    TerminalCellTemplate inside) {
        int y2 = y1 + height -1;
        int x2 = x1 + width -1;
        Regionlike bounds = view.getBounds();
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++){
                boolean isInside = true;
                if (y == y1) {
                    isInside = false;
                    boolean insideRow = true;
                    if (x == x1){
                        if (topleft != null) {
                            topleft.joinOn(view.get(y, x), bounds, y, x);
                        }
                        insideRow = false;
                    }
                    if (x == x2) {
                        if (topright != null) {
                            topright.joinOn(view.get(y, x), bounds, y, x);
                        }
                        insideRow = false;
                    }
                    if (insideRow && top != null) {
                        top.joinOn(view.get(y, x), bounds, y, x);
                    }
                }
                if (y == y2) {
                    isInside = false;
                    boolean insideRow = true;
                    if (x == x1) {
                        if (bottomleft != null) {
                            bottomleft.joinOn(view.get(y, x), bounds, y, x);
                        }
                        insideRow = false;
                    }
                    if (x == x2) {
                        if (bottomright != null) {
                            bottomright.joinOn(view.get(y, x), bounds, y, x);
                        }
                        insideRow = false;
                    } 
                    if (insideRow && bottom != null) {
                        bottom.joinOn(view.get(y, x), bounds, y, x);
                    }
                }
                if (isInside) {
                    if (x == x2) {
                        isInside = false;
                        if (right != null) {
                            right.joinOn(view.get(y, x), bounds, y, x);
                        }
                    }
                    if (x == x1) {
                        isInside = false;
                        if (left != null) {
                            left.joinOn(view.get(y, x), bounds, y, x);
                        }
                    }
                }
                if (isInside && inside != null) {
                    inside.joinOn(view.get(y, x), bounds, y, x);
                }
            }
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
     * @param view view where the box is created
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
    static public void box(TerminalViewInterface view,
                    int height, int width, int y1, int x1,
                    CellWalls left, CellWalls right, CellWalls top,
                    CellWalls bottom,
                    CellWalls topleft, CellWalls topright,
                    CellWalls bottomleft, CellWalls bottomright,
                    CellWalls inside) {
        int y2 = y1 + height -1;
        int x2 = x1 + width -1;
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++){
                if (y == y1) {
                    if (x == x1){
                        if (topleft != null) {
                            view.get(y, x).addCellWalls(topleft);
                        }
                    } else if (x == x2) {
                        if (topright != null) {
                            view.get(y, x).addCellWalls(topright);
                        }
                    } else if (top != null) {
                        view.get(y, x).addCellWalls(top);
                    }
                } else if (y == y2) {
                    if (x == x1) {
                        if (bottomleft != null) {
                            view.get(y, x).addCellWalls(bottomleft);
                        }
                    } else if (x == x2) {
                        if (bottomright != null) {
                            view.get(y, x).addCellWalls(bottomright);
                        }
                    } else if (bottom != null) {
                        view.get(y, x).addCellWalls(bottom);
                    }
                } else if (x == x2) {
                    if (right != null) {
                        view.get(y, x).addCellWalls(right);
                    }
                } else if (x == x1) {
                    if (left != null) {
                        view.get(y, x).addCellWalls(left);
                    }
                } else if (inside != null) {
                    view.get(y, x).addCellWalls(inside);
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
     * @param view view to create the box
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
    static public void box(TerminalViewInterface view,
                    Regionlike region,
                    Integer left, Integer right, Integer top, Integer bottom,
                    Integer topleft, Integer topright, Integer bottomleft,
                    Integer bottomright, Integer inside) {
        box(view, region.getHeight(), region.getWidth(),
                      region.getY(), region.getX(),
                      left, right, top, bottom, topleft, topright,
                      bottomleft, bottomright, inside);
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
     * @param view view to create the box
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
    static public void box(TerminalViewInterface view,
                    Regionlike region,
                    CellWalls left, CellWalls right,
                    CellWalls top, CellWalls bottom,
                    CellWalls topleft, CellWalls topright,
                    CellWalls bottomleft,
                    CellWalls bottomright, CellWalls inside) {
        box(view, region.getHeight(), region.getWidth(),
                      region.getY(), region.getX(),
                      left, right, top, bottom, topleft, topright,
                      bottomleft, bottomright, inside);
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
     * @param view view to create the box
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
    static public void box(TerminalViewInterface view,
                    Regionlike region,
                    TerminalCellTemplate left, TerminalCellTemplate right,
                    TerminalCellTemplate top, TerminalCellTemplate bottom,
                    TerminalCellTemplate topleft, TerminalCellTemplate topright,
                    TerminalCellTemplate bottomleft,
                    TerminalCellTemplate bottomright, TerminalCellTemplate inside) {
        box(view, region.getHeight(), region.getWidth(),
                      region.getY(), region.getX(),
                      left, right, top, bottom, topleft, topright,
                      bottomleft, bottomright, inside);
    }

    public int getRowInset() {
        return rowInset;
    }

    public void setRowInset(int rowInset) {
        this.rowInset = rowInset;
    }

    public int getColInset() {
        return colInset;
    }

    public void setColInset(int colInset) {
        this.colInset = colInset;
    }

    public void setInset(int rowInset, int colInset) {
        this.rowInset = rowInset;
        this.colInset = colInset;
    }
}
