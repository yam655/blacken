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
    public enum EditorCommand {
        /**
         * Ignore the key that was pressed.
         */
        IGNORE_KEY,
        /**
         * Process the key that was just pressed.
         */
        PROCESS_KEY,
        /**
         * We're done entering the string. If the key is valid, it gets added. 
         * If it was a keycode or modifier state it is ignored.
         */
        RETURN_STRING,
        /**
         * Treat the key pressed as if it were a destructive backspace.
         */
        PERFORM_BACKSPACE
    }
    /**
     * Handle generic case of monitoring codepoints entered in another function
     */
    public interface EditorCodepointDispatcherInterface {
        /**
         * While processing keys in another function, this function is called
         * first with codepoints so that they can be processed.
         * 
         * @param codepoint Unicode codepoint to process
         * @return true to ignore the codepoint; false to process as usual
         */
        public EditorCommand dispatchEditorCodepoint(int codepoint);
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
            int length, EditorCodepointDispatcherInterface cd) {
        int firstX = x;
        // int firstY = y;
        if (length < 0 || terminal.getWidth() - x - length <= 0) {
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
        boolean doQuit = false;
        while (!doQuit) {
            cp = terminal.getch();
            if (cd != null) {
                switch (cd.dispatchEditorCodepoint(cp)) {
                    case IGNORE_KEY:
                        continue;
                    case PROCESS_KEY:
                        // do nothing
                        break;
                    case RETURN_STRING:
                        doQuit = true;
                        break;
                    case PERFORM_BACKSPACE:
                        cp = BlackenKeys.KEY_BACKSPACE;
                        break;
                }
            }
            if(cp == BlackenKeys.NO_KEY) {
                continue;
            }
            if (cp == '\r' || cp == BlackenKeys.KEY_ENTER || 
                    cp == BlackenKeys.KEY_NP_ENTER) {
                doQuit = true;
                continue;
            }
            if (cp == '\t' || cp == BlackenKeys.KEY_TAB) {
                doQuit = true;
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
            if (doQuit) {
                continue;
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
