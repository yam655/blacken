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

import com.googlecode.blacken.terminal.editing.CodepointCallbackInterface;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yam655
 */
public interface WidgetContainer extends CodepointCallbackInterface, BlackenWidget {
    public void addWidget(BlackenWidget widget);
    public void removeWidget(BlackenWidget widget);
    public Map<String, BlackenWidget> getWidgets();
    public BlackenWidget findWidget(String name);
    public List<String> getFocusOrder();
    public void setFocusOrder(List<String> order);
    public boolean isEmpty();
}
