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
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.RegionIterator;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.grid.Sizable;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenMouseEvent;
import com.googlecode.blacken.terminal.BlackenWindowEvent;
import com.googlecode.blacken.terminal.TerminalCellTemplate;
import com.googlecode.blacken.terminal.TerminalView;
import com.googlecode.blacken.terminal.TerminalViewInterface;
import com.googlecode.blacken.terminal.editing.CodepointCallbackInterface;
import com.googlecode.blacken.terminal.editing.SingleLine;
import com.googlecode.blacken.terminal.utils.TerminalUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yam655
 */
public class SimpleWidgetContainer implements WidgetContainer {
    static private int nextId = 0;
    private Regionlike bounds;
    private Map<String, BlackenWidget> widgets = new LinkedHashMap<>();
    private List<BlackenWidget> containerCache = new ArrayList<>();
    private TerminalCellTemplate template = null;
    private BlackenWidgetResizer resizer = null;
    private String name = String.format("%s:%d",
            SimpleWidgetContainer.class.getCanonicalName(), nextId++);
    private List<BlackenWidget> focusList = new ArrayList<>();
    private WidgetContainer parent = null;
    private BlackenWidget activeWidget = null;
    private boolean focused = false;
    private boolean hovered = false;
    private boolean enabled = true;

    public SimpleWidgetContainer(String name, Regionlike bounds) {
        this.bounds = new BoxRegion(bounds);
        this.name = name;
    }

    public BlackenWidgetResizer getResizer() {
        return resizer;
    }

    public void setResizer(BlackenWidgetResizer resizer) {
        this.resizer = resizer;
    }

    @Override
    public void addWidget(BlackenWidget widget) {
        if (contains(widget)) {
            this.widgets.put(widget.getName(), widget);
        } else {
            throw new IllegalArgumentException("widget not in container");
        }
        if (widget instanceof WidgetContainer) {
            containerCache.add(widget);
        }
        if (widget.acceptsFocus() != null) {
            this.focusList.add(widget);
        }
    }

    @Override
    public void removeWidget(BlackenWidget widget) {
        this.focusList.remove(widget);
        this.widgets.remove(widget.getName());
        containerCache.remove(widget);
    }

    @Override
    public BlackenWidget findWidget(String name) {
        BlackenWidget widget = widgets.get(name);
        if (widget != null) {
            return widget;
        }
        for (BlackenWidget wcontainer : this.containerCache) {
            WidgetContainer container = (WidgetContainer)wcontainer;
            widget = container.findWidget(name);
            if (widget != null) {
                break;
            }
        }
        return widget;
    }

    @Override
    public void setColor(TerminalCellTemplate template) {
        if (template == null) {
            this.template = null;
            return;
        }
        template = new TerminalCellTemplate(template);
        this.template = template;
        if (template.isCellWallsUnset()) {
            template.clearCellWalls();
        }
        if (template.isStyleUnset()) {
            template.clearStyle();
        }
    }
    @Override
    public void setColor(int foreground, int background) {
        template = new TerminalCellTemplate();
        template.setBackground(background);
        template.setForeground(foreground);
        template.clearCellWalls();
        template.clearStyle();
    }

    @Override
    public void redraw(TerminalViewInterface view) {
        if (template != null) {
            TerminalUtils.applyTemplate(view, template);
        }
        TerminalView aView = new TerminalView(view.getBackingTerminal());
        for (BlackenWidget widget : widgets.values()) {
            aView.setBounds(widget);
            widget.redraw(view);
        }
    }

    @Override
    public int handleCodepoint(int codepoint) {
        if (this.activeWidget != null) {
            CodepointCallbackInterface callback = activeWidget.acceptsFocus();
            if (callback != null) {
                codepoint = callback.handleCodepoint(codepoint);
            }
        }
        if (codepoint == BlackenKeys.NO_KEY) {
            return codepoint;
        }
        return codepoint;
    }

    @Override
    public boolean handleMouseEvent(BlackenMouseEvent mouse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean handleWindowEvent(BlackenWindowEvent window) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleResizeEvent() {
        int size = widgets.size();
        if (this.resizer != null) {
            this.resizer.resizeWidgets(widgets);
        }
        if (widgets.size() != size) {
            throw new UnsupportedOperationException("widgets modified during resize operation");
        }
    }

    @Override
    public boolean contains(int y, int x) {
        return bounds.contains(y, x);
    }

    @Override
    public boolean contains(int[] location) {
        return bounds.contains(location);
    }

    @Override
    public boolean contains(int height, int width, int y1, int x1) {
        return bounds.contains(height, width, y1, x1);
    }

    @Override
    public boolean contains(Positionable p) {
        return bounds.contains(p);
    }

    @Override
    public boolean contains(Regionlike r) {
        return bounds.contains(r);
    }

    @Override
    public Regionlike getBounds() {
        return bounds.getBounds();
    }

    @Override
    public RegionIterator getEdgeIterator() {
        return bounds.getEdgeIterator();
    }

    @Override
    public RegionIterator getInsideIterator() {
        return bounds.getInsideIterator();
    }

    @Override
    public RegionIterator getNotOutsideIterator() {
        return bounds.getNotOutsideIterator();
    }

    @Override
    public boolean intersects(int height, int width, int y1, int x1) {
        return bounds.intersects(height, width, y1, x1);
    }

    @Override
    public boolean intersects(Regionlike region) {
        return bounds.intersects(region);
    }

    @Override
    public void setBounds(Regionlike r) {
        bounds.setBounds(r);
    }

    @Override
    public void setBounds(int height, int width, int y, int x) {
        bounds.setBounds(height, width, y, x);
    }

    @Override
    public int getX() {
        return bounds.getX();
    }

    @Override
    public int getY() {
        return bounds.getY();
    }

    @Override
    public Positionable getPosition() {
        return bounds.getPosition();
    }

    @Override
    public void setX(int x) {
        bounds.setX(x);
    }

    @Override
    public void setY(int y) {
        bounds.setY(y);
    }

    @Override
    public void setPosition(int y, int x) {
        bounds.setPosition(y,x);
    }

    @Override
    public void setPosition(Positionable point) {
        bounds.setPosition(point);
    }

    @Override
    public int getHeight() {
        return bounds.getHeight();
    }

    @Override
    public int getWidth() {
        return bounds.getWidth();
    }

    @Override
    public Sizable getSize() {
        return bounds.getSize();
    }

    @Override
    public void setHeight(int height) {
        bounds.setHeight(height);
    }

    @Override
    public void setWidth(int width) {
        bounds.setWidth(width);
    }

    @Override
    public void setSize(int height, int width) {
        bounds.setSize(height, width);
    }

    @Override
    public void setSize(Sizable size) {
        bounds.setSize(size);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isHovered() {
        return this.hovered;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    @Override
    public void setEnabled(boolean state) {
        this.enabled = state;
    }

    @Override
    public void setHovered(boolean state) {
        this.hovered = state;
    }

    @Override
    public void setFocused(boolean state) {
        this.focused = state;
    }

    @Override
    public CodepointCallbackInterface acceptsFocus() {
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Map<String, BlackenWidget> getWidgets() {
        return Collections.unmodifiableMap(this.widgets);
    }

    @Override
    public WidgetContainer getWidgetParent() {
        return parent;
    }

    @Override
    public void setWidgetParent(WidgetContainer container) {
        if (container == this) {
            throw new IllegalArgumentException("Cannot parent self");
        }
        this.parent = container;
    }

    @Override
    public List<String> getFocusOrder() {
        List<String> ret = new ArrayList<>();
        for (BlackenWidget widget : this.focusList) {
            ret.add(widget.getName());
        }
        return ret;
    }

    @Override
    public void setFocusOrder(List<String> order) {
        focusList.clear();
        for (String name : order) {
            BlackenWidget widget = widgets.get(name);
            if (widget == null) {
                throw new NullPointerException("No component named " + name);
            }
            focusList.add(widget);
        }
    }

    @Override
    public boolean isEmpty() {
        return widgets.isEmpty();
    }

    @Override
    public WidgetCodepointHandler acceptsKeys() {
        return null;
    }

}
