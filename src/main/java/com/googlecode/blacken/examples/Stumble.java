/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.blacken.examples;

import java.util.EnumSet;

import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.core.Random;
import com.googlecode.blacken.grid.CopyableData;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenModifier;
import com.googlecode.blacken.terminal.CellWalls;
import com.googlecode.blacken.terminal.TerminalInterface;
import com.googlecode.blacken.terminal.TerminalStyle;

/**
 * A super-simple game
 * 
 * @author yam655
 */
public class Stumble {
    /**
     * TerminalInterface used by the example
     */
    protected TerminalInterface term;
    /**
     * ColorPalette used by the example
     */
    protected ColorPalette palette;
    /**
     * Whether to quit the loop or not
     */
    protected boolean quit;
    private Grid<CopyableData<Integer>> grid;
    private Random rand;
    private int nextLocation;
    private final static int EMPTY_FLOOR = 0x2e; 
    private final static Point MAP_START = new Point(1, 0);
    private final static Point MAP_END = new Point(-1, 0);
    private Point upperLeft = new Point(0, 0);
    private Point player = new Point(-1, -1);
    private boolean dirtyMsg = false;
    private boolean dirtyStatus = false;
    private String message;
    
    /**
     * Create a new instance
     */
    public Stumble() {
        grid = new Grid<CopyableData<Integer>>(new CopyableData<Integer>(EMPTY_FLOOR),
                100, 100);
        rand = new Random();
    }

    int[] placeIt(int what) {
        int[] placement = {-1, -1};
        for (int t=0; t < 10000; t++) {
            int x = rand.nextInt(grid.getWidth());
            int y = rand.nextInt(grid.getHeight());
            if (grid.get(y, x).getData() == EMPTY_FLOOR) {
                grid.set(y, x, new CopyableData<Integer>(what));
                placement[0] = y;
                placement[1] = x;
                break;
            }
        }
        if (placement[0] == -1) {
            throw new RuntimeException("It took too long to place."); //$NON-NLS-1$
        }
        return placement;
    }
    
    /**
     * Make a map
     */
    private void makeMap() {
        grid.clear();
        for (int c = 0; c < 100; c++) {
            placeIt(0x23);
        }
        nextLocation = 0x31;
        for (int c = 0x31; c < 0x3a; c++) {
            placeIt(c);
        }
        
    }
    
    private void showMap() {
        int ey = MAP_END.getY();
        int ex = MAP_END.getX();
        if (ey <= 0) {
            ey += term.gridHeight();
        }
        if (ey > grid.getHeight()) {
            ey = grid.getHeight();
        }
        if (ex <= 0) {
            ex += term.gridWidth();
        }
        if (ex > grid.getWidth()) {
            ex = grid.getWidth();
        }
        for (int y = MAP_START.getY(); y < ey; y++) {
            for (int x = MAP_START.getX(); x < ex; x++) {
                int y1 = y + upperLeft.getY() - MAP_START.getY();
                int x1 = x + upperLeft.getX() - MAP_START.getX();
                int what = ' ';
                if (y1 >= 0 && x1 >= 0 && y1 < grid.getHeight() && x1 < grid.getWidth()) {
                    what = grid.get(y1, x1).getData();
                }
                int fclr = 7;
                int bclr = 0;
                EnumSet<CellWalls> walls = EnumSet.noneOf(CellWalls.class);
                if (what == '@'){
                    bclr = 0xe4;
                    fclr = 0;
                } else if (what == '.') {
                    fclr = (y1 * x1) % 10 + 0xee;
                } else if (what == '#') {
                    fclr = (y1 * x1) % 14 + 0x58;
                } else if (what == ' ') {
                    if (y1 < 0) {
                        y1 *= -1;
                    }
                    if (x1 < 0) {
                        x1 *= -1;
                    }
                    fclr = (y1 * x1) % 38;
                    if (fclr < 28) {
                        if (fclr > 14) {
                            fclr -= 14;
                        }
                        fclr += 0x58;
                        what = '#';
                    } else {
                        fclr -= 28;
                        fclr += 0xee;
                        what = ':';
                    }
                } else if (what >= '0' || what <= '9') {
                    if (what > nextLocation) {
                        walls = CellWalls.BOX;
                    }
                    bclr = 0x11;
                    fclr = (what - '0') + 0x4;
                }
                term.set(y, x, new String(Character.toChars(what)), 
                         fclr, bclr, EnumSet.noneOf(TerminalStyle.class), walls);
            }
        }
    }
    
    /**
     * The application loop.
     * @return the quit status
     */
    public boolean loop() {
        makeMap();
        int ch = BlackenKeys.NO_KEY;
        int mod = BlackenKeys.NO_KEY;
        updateStatus();
        movePlayerBy(0,0);
        this.message = "Welcome to Stumble!";
        term.move(-1, -1);
        term.setSeparateCursor(true);
        while (ch != BlackenKeys.KEY_F10) {
            if (dirtyStatus) {
                updateStatus();
            }
            updateMessage(false);
            showMap();
            term.setCursorLocation(player.getY() - upperLeft.getY() + MAP_START.getY(), 
                                   player.getX() - upperLeft.getX() + MAP_START.getX());
            term.refresh();
            mod = BlackenKeys.NO_KEY;
            ch = term.getch();
            if (ch == BlackenKeys.RESIZE_EVENT) {
                this.refreshScreen();
                continue;
            } else if (BlackenKeys.isModifier(ch)) {
                mod = ch;
                ch = term.getch();
            }
            if (ch != BlackenKeys.NO_KEY) {
                this.message = null;
                doAction(mod, ch);
            }
        }
        return this.quit;
    }

    private void updateMessage(boolean press) {
        if (this.message != null && !dirtyMsg) {
            dirtyMsg = true;
        }
        if (dirtyMsg) {
            for (int x = 0; x < term.gridWidth(); x++) {
                term.mvaddch(0, x, ' ');
            }
            if (message == null) {
                dirtyMsg = false;
            } else {
                term.mvputs(0, 0, message);
            }
            if (press) {
                message = null;
            }
        }
    }

    /**
     * Update the status.
     */
    private void updateStatus() {
        term.setCurForeground(7);
        dirtyStatus = false;
        for (int x = 0; x < term.gridWidth()-1; x++) {
            term.mvaddch(0, x, ' ');
        }
        if (nextLocation < '9') {
            term.mvputs(term.gridHeight(), 0, "Get the ");
            term.setCurForeground((nextLocation - '0') + 0x4);
            term.addch(nextLocation);
            term.setCurForeground(7);
            if (nextLocation == '9') {
                term.puts(" to win.");
            }
        } else {
            term.mvputs(term.gridHeight(), 0, "You won!");
        }
        String msg = "F10 to quit.";
        term.mvputs(term.gridHeight(), term.gridWidth()-msg.length()-1, msg);
    }

    private void refreshScreen() {
        term.clear();
        updateStatus();
        updateMessage(false);
        this.showMap();
    }
    
    /**
     * @param ch
     */
    private void doAction(int modifier, int ch) {
        if (BlackenModifier.MODIFIER_KEY_CTRL.hasFlag(modifier)) {
            switch (ch) {
            case 'l':
            case 'L':
                this.recenterMap();
                refreshScreen();
                break;
            }
            return;
        }
        switch (ch) {
        case 'j':
        case BlackenKeys.KEY_DOWN:
            movePlayerBy(+1,  0);
            break;
        case 'k':
        case BlackenKeys.KEY_UP:
            movePlayerBy(-1,  0);
            break;
        case 'l':
        case BlackenKeys.KEY_LEFT:
            movePlayerBy(0,  -1);
            break;
        case 'h':
        case BlackenKeys.KEY_RIGHT:
            movePlayerBy(0,  +1);
            break;
        case BlackenKeys.KEY_F10:
        case BlackenKeys.KEY_ESCAPE:
            this.quit = true;
        }
        
    }

    /**
     * Move the player by an offset
     * 
     * @param y row offset (0 stationary)
     * @param x column offset (0 stationary)
     */
    private void movePlayerBy(int y, int x) {
        Integer there;
        if (player.getY() == -1) {
            int[] pos = placeIt('@');
            player.setPos(pos[0], pos[1]);
            recenterMap();
            return;
        }
        int[] oldPos = player.getPos();
        try {
            there = grid.get(player.getY() + y, player.getX() + x).getData();
        } catch(IndexOutOfBoundsException e) {
            return;
        }
        if (there == EMPTY_FLOOR || there == nextLocation) {
            grid.get(oldPos[0], oldPos[1]).setData(EMPTY_FLOOR);
            player.setPos(player.getY() + y, player.getX() + x);
            grid.get(player.getY(), player.getX()).setData(0x40);
            int playerScreenY = player.getY() - upperLeft.getY() + MAP_START.getY();  
            int playerScreenX = player.getX() - upperLeft.getX() + MAP_START.getX();
            int ScreenY2 = (MAP_END.getY() <= 0 
                    ? term.gridHeight() -1 + MAP_END.getY() : MAP_END.getY());
            int ScreenX2 = (MAP_END.getX() <= 0 
                    ? term.gridWidth() -1 + MAP_END.getX() : MAP_END.getX());
            if (playerScreenY >= ScreenY2 || playerScreenX >= ScreenX2 ||
                    playerScreenY <= MAP_START.getY() || 
                    playerScreenX <= MAP_START.getX()) {
                recenterMap();
            }
            if (there == nextLocation) {
                StringBuffer buf = new StringBuffer();
                buf.append("Got it.");
                buf.append(' ');
                if (there == '9') {
                    buf.append("All done!");
                } else {
                    buf.append("Next is unlocked.");
                }
                nextLocation ++;
                this.message = buf.toString();
                dirtyStatus = true;
                this.updateMessage(false);
            }
        } else if (there >= '0' && there <= '9') {
            this.message = "That position is still locked.";
            this.updateMessage(false);
        }
    }

    private void recenterMap() {
        upperLeft.setY(player.getY() - (term.gridHeight()-2)/2);
        upperLeft.setX(player.getX() - (term.gridWidth()-2)/2);
    }
    
    
    /**
     * Initialize the example
     * 
     * @param term alternate TerminalInterface to use
     * @param palette alternate ColorPalette to use
     */
    public void init(TerminalInterface term, ColorPalette palette) {
        if (term == null) {
            this.term = new SwingTerminal();
            this.term.init("Blacken Example: Stumble", 25, 80);
        } else {
            this.term = term;
        }
        if (palette == null) {
            palette = new ColorPalette();
            palette.addAll(ColorNames.XTERM_256_COLORS, false);
            palette.putMapping(ColorNames.SVG_COLORS);
        } 
        this.palette = palette;
        this.term.setPalette(palette);
    }
    
    /**
     * Start the example
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Stumble that = new Stumble();
        that.init(null, null);
        that.loop();
        that.quit();
    }
    
    /**
     * Quit the application.
     * 
     * <p>This calls quit on the underlying TerminalInterface.</p>
     */
    public void quit() {
        term.quit();
    }

}
