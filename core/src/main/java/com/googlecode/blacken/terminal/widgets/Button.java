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
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.terminal.BlackenCodePoints;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenModifier;
import com.googlecode.blacken.terminal.TerminalCellTemplate;
import com.googlecode.blacken.terminal.TerminalStyle;
import com.googlecode.blacken.terminal.TerminalViewInterface;
import com.googlecode.blacken.terminal.editing.CodepointCallbackInterface;
import com.googlecode.blacken.terminal.editing.SingleLine;
import com.googlecode.blacken.terminal.utils.TerminalUtils;

/**
 *
 * @author Steven Black
 */
public class Button extends BoxRegion implements BlackenWidget, WidgetCodepointHandler {
    static int nextId = 0;
    private String text;
    private boolean enabled = true;
    private boolean hovered = true;
    private boolean focused = false;
    private TerminalCellTemplate template;
    private String name = String.format("%s:%u", Button.class.getCanonicalName(), nextId++);
    private WidgetContainer parent;

    public Button(Regionlike region, String text) {
        super(region);
        this.text = text;
    }

    public Button(String name, Regionlike region, String text) {
        super(region);
        this.text = text;
        this.name = name;
    }

    /**
     * Create a button.
     *
     * @param height height of box
     * @param width width of box
     * @param y1 starting Y coordinate
     * @param x1 starting X coordinate
     * @param text button text
     */
    public Button(int height, int width, int y1, int x1, String text) {
        super(height, width, y1, x1);
        this.text = text;
    }

    /**
     * Create a button.
     *
     * @param height height of box
     * @param width width of box
     * @param y1 starting Y coordinate
     * @param x1 starting X coordinate
     * @param text button text
     */
    public Button(String name, int height, int width, int y1, int x1, String text) {
        super(height, width, y1, x1);
        this.text = text;
        this.name = name;
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
    public WidgetCodepointHandler acceptsKeys() {
        return this;
    }

    @Override
    public void redraw(TerminalViewInterface view) {
        int[] buttonborder = {'[', ']'};
        TerminalCellTemplate t = template;
        if (!this.enabled) {
            t = new TerminalCellTemplate(template);
            t.addStyle(TerminalStyle.STYLE_DIM);
            buttonborder = new int[] {'(', ')'};
        } else {
            if (this.hovered) {
                t = new TerminalCellTemplate(template);
                t.addStyle(TerminalStyle.STYLE_REVERSE);
            }
            if (this.focused) {
                buttonborder = new int[] {'<', '>'};
            }
        }
        TerminalUtils.applyTemplate(view, t);
        for (int y0 = 0; y0 < this.getHeight(); y0++) {
            view.get(y0 + this.getY(), this.getX()).setSequence(buttonborder[0]);
            view.get(y0 + this.getY(), this.getX() + this.getWidth() - 1).setSequence(buttonborder[1]);
        }
        String displayText = this.text;
        int textX = this.getX() + 1;
        int idealWidth = this.getWidth() - 2;
        if (idealWidth < this.text.length()) {
            displayText = this.text.substring(0, idealWidth-1);
            view.get(this.getY(), this.getX() + this.getWidth() - 2).setSequence(BlackenCodePoints.CODEPOINT_HORIZONTAL_ELLIPSIS);
        } else {
            textX += (idealWidth - displayText.length()) / 2;
        }
        SingleLine.putString(view, new Point(this.getY(), textX), null, displayText, t);
    }

    @Override
    public void setColor(TerminalCellTemplate template) {
        if (template == null) {
            throw new NullPointerException("template cannot be null");
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
    public String getName() {
        return this.name;
    }

    @Override
    public WidgetContainer getWidgetParent() {
        return this.parent;
    }

    @Override
    public void setWidgetParent(WidgetContainer container) {
        this.parent = container;
    }

    @Override
    public int handleCodepoint(int modifiers, int codepoint) {
        switch(codepoint) {
            case BlackenKeys.KEY_TAB:
              if (BlackenModifier.hasFlag(modifiers, BlackenModifier.MODIFIER_KEY_SHIFT)) {
                  return BlackenKeys.CMD_FOCUS_PREVIOUS;
              } else {
                  return BlackenKeys.CMD_FOCUS_NEXT;
              }
            case BlackenKeys.KEY_DOWN:
            case BlackenKeys.KEY_KP_DOWN:
            case BlackenKeys.KEY_RIGHT:
            case BlackenKeys.KEY_KP_RIGHT:
                return BlackenKeys.CMD_FOCUS_NEXT;
            case BlackenKeys.KEY_UP:
            case BlackenKeys.KEY_KP_UP:
            case BlackenKeys.KEY_LEFT:
            case BlackenKeys.KEY_KP_LEFT:
                return BlackenKeys.CMD_FOCUS_PREVIOUS;
            case ' ':
                return BlackenKeys.CMD_EXECUTE;
        }
        return codepoint;
    }

    @Override
    public CodepointCallbackInterface acceptsFocus() {
        return null;
    }

}
