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

package com.googlecode.blacken.terminal.utils;

import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.terminal.TerminalCellLike;
import com.googlecode.blacken.terminal.TerminalCellTemplate;
import com.googlecode.blacken.terminal.TerminalViewInterface;

/**
 *
 * @author yam655
 */
public class TerminalUtils {

    /**
     *
     * @param terminal
     * @param rows
     * @param cols
     * @param y
     * @param x
     * @param template
     */
    public static void applyTemplate(TerminalViewInterface terminal, int rows, int cols, int y, int x, TerminalCellTemplate template) {
        if (template == null) {
            return;
        }
        if (rows < 0) {
            rows += terminal.getHeight() + 1;
            rows -= y;
        }
        if (cols < 0) {
            cols += terminal.getWidth() + 1;
            cols -= x;
        }
        Regionlike bounds = terminal.getBackingTerminal().getBounds();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                TerminalCellLike cell = terminal.get(y + r, x + c);
                if (cell == null) {
                    continue;
                }
                template.applyOn(cell, bounds, y + r, x + c);
                terminal.refresh(y + r, x + c);
            }
        }
    }

    /**
     *
     * @param terminal
     * @param template
     * @since 1.2
     */
    public static void applyTemplate(TerminalViewInterface terminal, TerminalCellTemplate template) {
        Regionlike bounds = terminal.getBounds();
        applyTemplate(terminal, bounds.getHeight(), bounds.getWidth(), bounds.getY(), bounds.getX(), template);
    }

    public static void applyTemplate(TerminalViewInterface terminal, int y, int x, TerminalCellTemplate template, int length) {
        if (template == null) {
            return;
        }
        if (length < 0) {
            length += terminal.getWidth() + 1;
            length -= x;
        }
        for (int o = 0; o < length; o++) {
            TerminalCellLike c = terminal.get(y, x + o);
            template.applyOn(c, terminal.getBounds(), y, x + o);
            terminal.refresh(y, x + o);
        }
    }

}
