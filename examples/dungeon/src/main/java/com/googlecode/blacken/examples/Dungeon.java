/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
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

import com.googlecode.blacken.bsp.BSPTree;
import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.core.Random;
import com.googlecode.blacken.dungeon.SimpleDigger;
import com.googlecode.blacken.dungeon.Room;
import com.googlecode.blacken.extras.PerlinNoise;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A super-simple game
 * 
 * @author Steven Black
 */
public class Dungeon {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dungeon.class);
    /**
     * TerminalInterface used by the example
     */
    protected CursesLikeAPI term;
    /**
     * Whether to quit the loop or not
     */
    protected boolean quit;
    private Grid<Integer> grid;
    private Random rand;
    private int nextLocation;
    private final static Positionable MAP_START = new Point(1, 0);
    private final static Positionable MAP_END = new Point(-1, 0);
    private Positionable upperLeft = new Point(0, 0);
    private Positionable player = new Point(-1, -1);
    private Integer underPlayer = -1;
    private boolean dirtyMsg = false;
    private boolean dirtyStatus = false;
    private String message;
    private float noisePlane;
    private Map<String, Integer> config;
    private Set<Integer> passable;

    /**
     * Create a new instance
     */
    public Dungeon() {
        rand = new Random();
        noisePlane = rand.nextFloat();
        config = new HashMap<>();
        config.put("diggable", " ".codePointAt(0));
        config.put("floor", ".".codePointAt(0));
        config.put("hall:floor", ",".codePointAt(0));
        config.put("wall", "#".codePointAt(0));
        config.put("hall:wall", "%".codePointAt(0));
        config.put("room:door", "+".codePointAt(0));
        grid = new Grid<>(config.get("diggable"), 100, 100);
        passable = new HashSet<>();
        passable.add(config.get("floor"));
        passable.add(config.get("hall:floor"));
        passable.add(config.get("room:door"));
    }


    /**
     * Make a map
     */
    private void makeMap() {
        grid.clear();
        SimpleDigger simpleDigger = new SimpleDigger();
        BSPTree<Room> bsp = simpleDigger.setup(grid);
        List<Room> rooms = new ArrayList(bsp.findContained(null));
        Collections.shuffle(rooms, rand);
        nextLocation = 0x31;
        int idx = 0;
        for (Integer c = 0x31; c < 0x3a; c++) {
            rooms.get(idx).assignToContainer(c);
            idx++;
            if (idx >= rooms.size()) {
                idx = 0;
                Collections.shuffle(rooms, rand);
            }
        }
        simpleDigger.digHallFirst(bsp, grid, config, false);
        underPlayer = config.get("room:floor");
        Positionable pos = simpleDigger.placeIt(grid, underPlayer,
                rooms.get(idx), "@".codePointAt(0));
        this.player.setPosition(pos);
        recenterMap();
    }

    private void showMap() {
        int ey = MAP_END.getY();
        int ex = MAP_END.getX();
        if (ey <= 0) {
            ey += term.getHeight();
        }
        if (ey > grid.getHeight()) {
            ey = grid.getHeight();
        }
        if (ex <= 0) {
            ex += term.getWidth();
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
                    what = grid.get(y1, x1);
                }
                int fclr = 7;
                int bclr = 0;
                EnumSet<CellWalls> walls = EnumSet.noneOf(CellWalls.class);
                if (what == '@'){
                    bclr = 0xe4;
                    fclr = 0;
                } else if (what == '.') {
                    fclr = (int)(Math.floor(PerlinNoise.noise(x1, y1, noisePlane) * 10.0F)) + 0xee;
                } else if (what == '#') {
                    fclr = (int)(Math.floor(PerlinNoise.noise(x1, y1, noisePlane) * 14.0F)) + 0x58;
                } else if (what == ' ') {
                    fclr = (int)(Math.floor(PerlinNoise.noise(x1, y1, noisePlane) * 38.0F));
                    if (fclr < 0) {
                        fclr *= -1;
                    }
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
        int mod;
        updateStatus();
        movePlayerBy(0,0);
        this.message = "Welcome to Dungeon!";
        term.move(-1, -1);
        while (ch != BlackenKeys.KEY_F10) {
            if (dirtyStatus) {
                updateStatus();
            }
            updateMessage(false);
            showMap();
            term.setCursorLocation(player.getY() - upperLeft.getY() + MAP_START.getY(), 
                                   player.getX() - upperLeft.getX() + MAP_START.getX());
            this.term.getPalette().rotate(0xee, 10, +1);
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
            // LOGGER.debug("Processing key: {}", ch);
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
            for (int x = 0; x < term.getWidth(); x++) {
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
        for (int x = 0; x < term.getWidth()-1; x++) {
            term.mvaddch(term.getHeight(), x, ' ');
        }
        if (nextLocation <= '9') {
            term.mvputs(term.getHeight(), 0, "Get the ");
            term.setCurForeground((nextLocation - '0') + 0x4);
            term.addch(nextLocation);
            term.setCurForeground(7);
            if (nextLocation == '9') {
                term.puts(" to win.");
            }
        } else {
            term.mvputs(term.getHeight(), 0, "You won!");
        }
        String msg = "F10 to quit.";
        term.mvputs(term.getHeight(), term.getWidth()-msg.length()-1, msg);
    }

    private void refreshScreen() {
        term.clear();
        updateStatus();
        updateMessage(false);
        this.showMap();
    }
    
    private boolean doAction(int modifier, int ch) {
        if (BlackenModifier.MODIFIER_KEY_CTRL.hasFlag(modifier)) {
            switch (ch) {
            case 'l':
            case 'L':
                this.recenterMap();
                refreshScreen();
                break;
            }
            return false;
        }
        switch (ch) {
        case 'j':
        case BlackenKeys.KEY_DOWN:
        case BlackenKeys.KEY_NP_2:
        case BlackenKeys.KEY_KP_DOWN:
            movePlayerBy(+1,  0);
            break;
        case 'k':
        case BlackenKeys.KEY_UP:
        case BlackenKeys.KEY_NP_8:
        case BlackenKeys.KEY_KP_UP:
            movePlayerBy(-1,  0);
            break;
        case 'h':
        case BlackenKeys.KEY_LEFT:
        case BlackenKeys.KEY_NP_4:
        case BlackenKeys.KEY_KP_LEFT:
            movePlayerBy(0,  -1);
            break;
        case 'l':
        case BlackenKeys.KEY_RIGHT:
        case BlackenKeys.KEY_NP_6:
        case BlackenKeys.KEY_KP_RIGHT:
            movePlayerBy(0,  +1);
            break;
        case BlackenKeys.KEY_F10:
        case BlackenKeys.KEY_ESCAPE:
            this.quit = true;
            // fall-through to 'default'
        default:
            return false;
        }
        return true;
    }

    /**
     * Move the player by an offset
     * 
     * @param y row offset (0 stationary)
     * @param x column offset (0 stationary)
     */
    private void movePlayerBy(int y, int x) {
        Integer there;
        Positionable oldPos = player.getPosition();
        try {
            there = grid.get(player.getY() + y, player.getX() + x);
        } catch(IndexOutOfBoundsException e) {
            return;
        }
        if (passable.contains(there) || there == nextLocation) {
            grid.set(oldPos.getY(), oldPos.getX(), underPlayer);
            player.setPosition(player.getY() + y, player.getX() + x);
            underPlayer = grid.get(player);
            grid.set(player.getY(), player.getX(), 0x40);
            int playerScreenY = player.getY() - upperLeft.getY() + MAP_START.getY();  
            int playerScreenX = player.getX() - upperLeft.getX() + MAP_START.getX();
            int ScreenY2 = (MAP_END.getY() <= 0 
                    ? term.getHeight() -1 + MAP_END.getY() : MAP_END.getY());
            int ScreenX2 = (MAP_END.getX() <= 0 
                    ? term.getWidth() -1 + MAP_END.getX() : MAP_END.getX());
            if (playerScreenY >= ScreenY2 || playerScreenX >= ScreenX2 ||
                    playerScreenY <= MAP_START.getY() || 
                    playerScreenX <= MAP_START.getX()) {
                recenterMap();
            }
            if (there == nextLocation) {
                StringBuilder buf = new StringBuilder();
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
            term = new SwingTerminal();
            term.init("Blacken Example: Dungeon", 25, 80);
        }
        this.term = new CursesLikeAPI(term);
        if (palette == null) {
            palette = new ColorPalette();
            palette.addAll(ColorNames.XTERM_256_COLORS, false);
        } 
        this.term.setPalette(palette);
    }
    
    /**
     * Start the example
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Dungeon that = new Dungeon();
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
