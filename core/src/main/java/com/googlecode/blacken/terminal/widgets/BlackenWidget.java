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

import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.terminal.TerminalCellTemplate;
import com.googlecode.blacken.terminal.TerminalViewInterface;
import com.googlecode.blacken.terminal.editing.CodepointCallbackInterface;

/**
 *
 * @author yam655
 */
public interface BlackenWidget extends Regionlike {
    public String getName();
    public boolean isEnabled();
    public boolean isHovered();
    public boolean isFocused();
    public void setEnabled(boolean state);
    public void setHovered(boolean state);
    public void setFocused(boolean state);
    /**
     * This widget gets the full assortment of callbacks.
     *
     * <p>Most widgets will return <code>null</code> here. Most widgets will
     * only need to accept keys.
     *
     * <p>The only real exception to this is new Widget containers. They need
     * to handle a more complex set of callbacks.
     * @return
     */
    public CodepointCallbackInterface acceptsFocus();
    public WidgetCodepointHandler acceptsKeys();
    public void redraw(TerminalViewInterface view);
    public void setColor(TerminalCellTemplate template);
    public void setColor(int foreground, int background);
    public WidgetContainer getWidgetParent();
    public void setWidgetParent(WidgetContainer container);
}
