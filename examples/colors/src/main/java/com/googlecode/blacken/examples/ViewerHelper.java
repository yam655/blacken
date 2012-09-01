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

package com.googlecode.blacken.examples;

import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenMouseEvent;
import com.googlecode.blacken.terminal.BlackenWindowEvent;
import com.googlecode.blacken.terminal.CellWalls;
import com.googlecode.blacken.terminal.TerminalCellLike;
import com.googlecode.blacken.terminal.TerminalInterface;
import com.googlecode.blacken.terminal.TerminalView;
import com.googlecode.blacken.terminal.TerminalViewInterface;
import com.googlecode.blacken.terminal.editing.CodepointCallbackInterface;
import com.googlecode.blacken.terminal.editing.SingleLine;
import com.googlecode.blacken.terminal.editing.StringViewer;
import java.util.EnumSet;

/**
 *
 * @author Steven Black
 */
public class ViewerHelper implements CodepointCallbackInterface {
    private final TerminalInterface term;
    private int foreground = 0xFFa0a0a0;
    private int background = 0xFF000000;
    private TerminalViewInterface view;
    private StringViewer viewer;
    private TerminalViewInterface helpView;
    private StringViewer helpViewer;
    private String title;
    String helpMessage =
            "Q / q : quit this viewer; " +
            "PageUp / PageDown : Next or previous page";

    public ViewerHelper(TerminalInterface term, String title, String message) {
        this.title = title;
        this.term = term;
        view = new TerminalView(term);
        viewer = new StringViewer(view, message, this);
        helpView = new TerminalView(term);
        helpViewer = new StringViewer(helpView, helpMessage, null);

    }
    public void setColor(int foreground, int background) {
        this.foreground = foreground;
        this.background = background;
        TerminalCellLike cell = term.getEmpty().clone();
        cell.setBackground(background);
        cell.setForeground(foreground);
        term.setEmpty(cell);
        viewer.setColor(foreground, background);
    }
    public void setColor(String foreground, String background) {
        ColorPalette palette = term.getBackingTerminal().getPalette();
        int fg = palette.getColorOrIndex(foreground);
        int bg = palette.getColorOrIndex(background);
        setColor(fg, bg);
    }

    public void run() {
        term.clear();
        displayFrame();
        viewer.run();
    }

    @Override
    public int handleCodepoint(int codepoint) {
        return codepoint;
    }

    @Override
    public int handleMouseEvent(BlackenMouseEvent mouse) {
        return BlackenKeys.NO_KEY;
    }

    @Override
    public int handleWindowEvent(BlackenWindowEvent window) {
        return BlackenKeys.NO_KEY;
    }

    @Override
    public void handleResizeEvent() {
        term.clear();
        displayFrame();
    }

    public void centerOnLine(int y, String string) {
        int offset = term.getWidth() / 2 - string.length() / 2;
        SingleLine.putString(term, y, offset, string, foreground, background);
    }

    private void displayFrame() {
        centerOnLine(0, title);
        view.setBounds(term.getHeight()-1-helpViewer.getLines(), term.getWidth()-2, 1, 1);
        helpView.setBounds(helpViewer.getLines(), term.getWidth(), term.getHeight()-helpViewer.getLines(), 0);

        helpViewer.step();

        for (int x = 1; x < term.getWidth()-1; x++) {
            term.set(0, x, null, foreground, background, null, EnumSet.of(CellWalls.BOTTOM));
            term.set(term.getHeight()-helpViewer.getLines(), x, null, foreground, background, null, EnumSet.of(CellWalls.TOP));
        }
        for (int y = 1; y < term.getHeight()-helpViewer.getLines(); y++) {
            term.set(y, 0, null, foreground, background, null, EnumSet.of(CellWalls.RIGHT));
            term.set(y, term.getWidth()-1, null, foreground, background, null, EnumSet.of(CellWalls.LEFT));
        }
    }

}
