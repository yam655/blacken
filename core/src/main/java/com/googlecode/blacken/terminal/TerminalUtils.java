/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.blacken.terminal;

import com.googlecode.blacken.grid.Grid;

/**
 *
 * @author Steven Black
 */
public class TerminalUtils {
    /**
     * Handle generic case of monitoring codepoints entered in another function
     */
    public interface CodepointDispatcherInterface {
        /**
         * While processing keys in another function, this function is called
         * first with codepoints so that they can be processed.
         * 
         * @param codepoint Unicode codepoint to process
         * @return true to ignore the codepoint; false to process as usual
         */
        public boolean dispatchCodePoint(int codepoint);
    }
    
    /**
     * Get a string from the window.
     *
     * This caches the previous cursor position, then moves the cursor to the
     * requested input location. It restores the cursor position after the
     * string has been entered.
     * 
     * @param terminal
     * @param y
     * @param x
     * @param length
     * @return
     */
    static public String getString(TerminalInterface terminal, int y, int x,
            int length, CodepointDispatcherInterface cd) {
        int firstX = x;
        // int firstY = y;
        if (terminal.getWidth() - x - length <= 0) {
            length = terminal.getWidth() - x;
        }
        int[] lastCursor = terminal.getCursorLocation();
        int cp = -1;
        StringBuffer out;
        if (length < 0) {
            out = new StringBuffer();
        } else {
            out = new StringBuffer(length);
        }
        int i = 0;
        int lastUpX = -1, lastUpY = -1;
        terminal.setCursorLocation(y, x);
        while (cp != '\r' && cp != '\t' && cp != BlackenKeys.KEY_ENTER && 
                 cp != BlackenKeys.KEY_NP_ENTER && cp != BlackenKeys.KEY_TAB) {
            cp = terminal.getch();
            if (cd != null) {
                if (cd.dispatchCodePoint(cp)) {
                    continue;
                }
            }
            if(cp == BlackenKeys.NO_KEY) {
                continue;
            }
            if (cp == '\r' || cp == BlackenKeys.KEY_ENTER || 
                    cp == BlackenKeys.KEY_NP_ENTER) {
                continue;
            }
            if (cp == '\t' || cp == BlackenKeys.KEY_TAB) {
                continue;
            }
            TerminalCellLike c;
            // XXX add line-editing capabilities
            if (cp == '\b' || cp == BlackenKeys.KEY_BACKSPACE) {
                if (x > firstX) {
                    x--;
                }
                c = terminal.get(y, x);
                c.setSequence("\u0000");
                terminal.set(y, x, c);
                terminal.setCursorLocation(y, x);
            }
            // When the limit is reached, we don't force a return.
            // We should be able to incorporate some line-editing
            if (length != -1 && i >= length) {
                continue;
            }
            if (BlackenKeys.isSpecial(cp)) {
                continue;
            }
            if (Character.isValidCodePoint(cp)) {
                out.append(Character.toChars(cp));
            }
            switch (Character.getType(cp)) {
                case Character.COMBINING_SPACING_MARK:
                case Character.ENCLOSING_MARK:
                case Character.NON_SPACING_MARK:
                    c = terminal.get(lastUpY, lastUpX);
                    c.addSequence(cp);
                    break;
                default:
                    c = terminal.get(y, x);
                    c.setSequence(cp);
                    lastUpX = x++;
                    lastUpY = y;
                    terminal.setCursorLocation(y, x);
            }
            i++;
        }
        terminal.setCursorLocation(lastCursor);
        return out.toString();
    }

    /**
     * A simple method to write a processed string to a terminal.
     * 
     * <p>The sequence is processed for common codepoints that have meaning
     * such as TAB, NL, CR.
     * 
     * @param terminal
     * @param y
     * @param x
     * @param string
     * @param fore
     * @param back
     * @return 
     */
    static public int[] putString(TerminalInterface terminal, int y, int x, String string, int fore, int back) {
        Grid<TerminalCellLike> grid = terminal.getGrid();
        if (grid == null) {
            throw new NullPointerException("TerminalInterface wasn't initialized.");
        }
        int cp;
        if (x >= grid.getWidth()) {
            x = grid.getWidth() - 1;
        }
        if (y >= grid.getHeight()) {
            y = grid.getHeight() - 1;
        }
        int lastUpX = x - 1;
        int lastUpY = y - 1;
        for (int i = 0; i < string.codePointCount(0, string.length()); i++) {
            cp = string.codePointAt(i);
            TerminalCellLike c;
            switch (Character.getType(cp)) {
                case Character.COMBINING_SPACING_MARK:
                case Character.ENCLOSING_MARK:
                case Character.NON_SPACING_MARK:
                    if (lastUpX >= 0 && lastUpY >= 0) {
                        c = terminal.get(lastUpY, lastUpX);
                        c.addSequence(cp);
                    }
                    break;
                default:
                    lastUpX = x;
                    lastUpY = y;
                    TerminalCellLike cell;
                    if (cp == '\n' || cp == BlackenKeys.KEY_ENTER || 
                            cp == BlackenKeys.KEY_NP_ENTER) {
                        y++;
                        x = 0;
                    } else if (cp == '\r') {
                        x = 0;
                    } else if (cp == '\b' || cp == BlackenKeys.KEY_BACKSPACE) {
                        if (x > 0) {
                            x--;
                        }
                        cell = terminal.get(y, x);
                        cell.setSequence("\u0000");
                        terminal.set(y, x, cell);
                    } else if (cp == '\t' || cp == BlackenKeys.KEY_TAB) {
                        x = x + 8;
                        x -= x % 8;
                    } else {
                        cell = terminal.get(y, x);
                        cell.setSequence(cp);
                        cell.setForeground(fore);
                        cell.setBackground(back);
                        terminal.set(y, x, cell);
                        x++;
                    }
                    if (x >= grid.getWidth()) {
                        x = 0;
                        y++;
                    }
                    if (y >= grid.getHeight()) {
                        terminal.moveBlock(grid.getHeight() - 1, grid.getWidth(), 1, 0, 0, 0);
                        y = grid.getHeight() - 1;
                    }
            }
        }
        int[] ret = {y, x};
        return ret;
    }
}
