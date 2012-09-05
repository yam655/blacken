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

import com.googlecode.blacken.bsp.BSPTree;
import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.core.Obligations;
import com.googlecode.blacken.core.Random;
import com.googlecode.blacken.dungeon.Room;
import com.googlecode.blacken.dungeon.SimpleDigger;
import com.googlecode.blacken.extras.PerlinNoise;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    private class Representation {
        public List<Integer> codepoints = new ArrayList<>();
        public List<List<Integer>> colors = new ArrayList<>();
        /**
         * Add a codepoint and a color.
         * @param codepoint
         * @param color
         */
        public void add(Integer codepoint, Integer color) {
            this.codepoints.add(codepoint);
            List<Integer> t = new ArrayList<>(1);
            t.add(color);
            this.colors.add(t);
        }
        public void add(Integer codepoint, Integer... colorset) {
            this.codepoints.add(codepoint);
            List<Integer> t = new ArrayList<>(1);
            t.addAll(Arrays.asList(colorset));
            this.colors.add(t);
        }
        /**
         * Add a codepoint and a palette range.
         * @param codepoint
         * @param startColor
         * @param count
         */
        public void add(Integer codepoint, int startColor, int count) {
            this.codepoints.add(codepoint);
            List<Integer> t = new ArrayList<>(count);
            while(count-- > 0) {
                t.add(startColor++);
            }
            this.colors.add(t);
        }
        /**
         * Add a codepoint and a color series.
         * @param codepoint
         * @param colorItr
         */
        public void add(Integer codepoint, Iterator<Integer> colorItr) {
            this.codepoints.add(codepoint);
            List<Integer> t = new ArrayList<>();
            while(colorItr.hasNext()) {
                t.add(colorItr.next());
            }
            this.colors.add(t);
        }
        /**
         * Get the codepoint/color for a float (likely Perlin) value.
         * @param value 0.0 to 1.0
         * @return {codepoint, color}
         */
        public Integer[] get(float value) {
            if (value < 0) { value *= -1; }
            int index = (int)Math.floor(value * this.codepoints.size());
            int clrIdx = (int)Math.floor(value * this.colors.get(index).size());
            Integer codepoint = this.codepoints.get(index);
            Integer color = this.colors.get(index).get(clrIdx);
            return new Integer[] {codepoint, color};
        }
        public List<Integer> getColors(int index) {
            return this.colors.get(index);
        }
        public List<Integer> getColors(double value) {
            if (value < 0) { value *= -1; }
            int index = (int)Math.floor(value * this.codepoints.size());
            return this.colors.get(index);
        }
        public Integer getColor(double value) {
            if (value < 0) { value *= -1; }
            int index = (int)Math.floor(value * this.codepoints.size());
            int clrIdx = (int)Math.floor(value * this.colors.get(index).size());
            return this.colors.get(index).get(clrIdx);
        }
        public Integer getCodePoint(double value) {
            if (value < 0) { value *= -1; }
            int index = (int)Math.floor(value * this.codepoints.size());
            return this.codepoints.get(index);
        }
        public Integer getCodePoint(int index) {
            return this.codepoints.get(index);
        }
        public int size() {
            return this.codepoints.size();
        }
        public boolean isEmpty() {
            return this.codepoints.isEmpty();
        }
    }

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
    private Set<Integer> roomWalls;
    private List<Map<Integer, Representation>> representations = new ArrayList<>();
    private int represent = 0;
    private String helpMessage =
"Dungeon Example Commands\n" +
"============================================================================\n" +
"Ctrl+L : recenter and redisplay the screen\n" +
"j, Down : move down                  | k, Up : move up\n" +
"h, Left : move left                  | l (ell), Right: move right\n" +
"\n" +
"Space : next representation set      | Backspace : previous representations\n" +
"\n" +
"Q, q, Escape : quit\n" +
"\n" +
"L : show my license                  | N : show legal notices\n" +
"\n" +
"? : this help screen\n";

    public void addRepresentations() {
        // default
        Representation e;
        Map<Integer, Representation> r;

        r = new HashMap<>();
        representations.add(r);

        e = new Representation();
        e.add(config.get("player"), 0xe4);
        r.put(config.get("player"), e);

        e = new Representation();
        e.add(config.get("room:door"), 58, 130, 94, 94, 94, 94, 94, 94, 94, 94);
        r.put(config.get("room:door"), e);

        e = new Representation();
        e.add(config.get("floor"), 0xee, 10);
        r.put(config.get("floor"), e);

        e = new Representation();
        e.add(config.get("hall:floor"), 0xee, 10);
        r.put(config.get("hall:floor"), e);

        e = new Representation();
        e.add(config.get("diggable"), 0x58, 14);
        r.put(config.get("diggable"), e);

        e = new Representation();
        e.add(config.get("hall:wall"), 0x58, 14);
        r.put(config.get("hall:wall"), e);

        for (Integer roomWall : roomWalls) {
            e = new Representation();
            e.add(roomWall, 0x58, 14);
            r.put(roomWall, e);
        }

        e = new Representation();
        e.add(config.get("water"), 17, 11);
        e.add(config.get("mountains"), 236, 20);
        e.add(config.get("water"), 17, 11);
        e.add(config.get("water"), 17, 11);
        e.add(config.get("water"), 17, 11);
        e.add(config.get("mountains"), 236, 20);
        r.put(config.get("void"), e);

        for (char goal='0'; goal <= '9'; goal++) {
            Integer g = new Integer(goal);
            e = new Representation();
            e.add(g, 0x4 + g - '0');
            r.put(g, e);
        }
        
        // nethack

        r = new HashMap<>();
        representations.add(r);
        e = new Representation();
        e.add("@".codePointAt(0), 7);
        r.put(config.get("player"), e);

        e = new Representation();
        e.add("+".codePointAt(0), 7);
        r.put(config.get("room:door"), e);

        e = new Representation();
        e.add(".".codePointAt(0), 7);
        r.put(config.get("floor"), e);

        e = new Representation();
        e.add("#".codePointAt(0), 7);
        r.put(config.get("hall:floor"), e);

        e = new Representation();
        e.add(" ".codePointAt(0), 0);
        r.put(config.get("diggable"), e);

        e = new Representation();
        e.add(" ".codePointAt(0), 0);
        r.put(config.get("hall:wall"), e);

        e = new Representation();
        e.add("-".codePointAt(0), 7);
        r.put(config.get("room:wall:top"), e);
        r.put(config.get("room:wall:bottom"), e);
        r.put(config.get("room:wall:top-left"), e);
        r.put(config.get("room:wall:top-right"), e);
        r.put(config.get("room:wall:bottom-left"), e);
        r.put(config.get("room:wall:bottom-right"), e);

        e = new Representation();
        e.add("|".codePointAt(0), 7);
        r.put(config.get("room:wall:left"), e);
        r.put(config.get("room:wall:right"), e);

        for (Integer roomWall : roomWalls) {
            if (!r.containsKey(roomWall)) {
                e = new Representation();
                e.add(roomWall, 0x58, 14);
                r.put(roomWall, e);
            }
        }

        e = new Representation();
        e.add(" ".codePointAt(0), 0);
        r.put(config.get("void"), e);

        for (char goal='0'; goal <= '9'; goal++) {
            Integer g = new Integer(goal);
            e = new Representation();
            e.add(g, 0x4 + g - '0');
            r.put(g, e);
        }

        // moria

        r = new HashMap<>();
        representations.add(r);

        e = new Representation();
        e.add((int)'@', 0xe4);
        r.put(config.get("player"), e);

        e = new Representation();
        e.add((int)'+', 58, 130, 94, 94, 94, 94, 94, 94, 94, 94);
        e.add((int)'+', 58, 130, 94, 94, 94, 94, 94, 94, 94, 94);
        e.add((int)'\'', 58, 130, 94, 94, 94, 94, 94, 94, 94, 94);
        r.put(config.get("room:door"), e);

        e = new Representation();
        e.add((int)'.', 0xee, 10);
        r.put(config.get("floor"), e);

        r.put(config.get("hall:floor"), e);

        e = new Representation();
        e.add(BlackenCodePoints.CODEPOINT_MEDIUM_SHADE, 0x58, 14);
        e.add(BlackenCodePoints.CODEPOINT_LIGHT_SHADE, 0x58, 14);
        e.add(BlackenCodePoints.CODEPOINT_MEDIUM_SHADE, 0x58, 14);
        e.add(BlackenCodePoints.CODEPOINT_MEDIUM_SHADE, 0x58, 14);
        r.put(config.get("diggable"), e);

        e = r.get(config.get("diggable"));
        r.put(config.get("hall:wall"), e);

        for (Integer roomWall : roomWalls) {
            e = new Representation();
            e.add(BlackenCodePoints.CODEPOINT_MEDIUM_SHADE, 0x58, 14);
            r.put(roomWall, e);
        }

        e = new Representation();
        e.add(" ".codePointAt(0), 0);
        r.put(config.get("void"), e);

        for (char goal='0'; goal <= '9'; goal++) {
            Integer g = new Integer(goal);
            e = new Representation();
            e.add(g, 0x4 + g - '0');
            r.put(g, e);
        }

    }

    /**
     * Create a new instance
     */
    public Dungeon() {
        rand = new Random();
        noisePlane = rand.nextFloat();
        config = new HashMap<>();
        // Used by Simple Digger
        // Courier New doesn't have Heavy, but does have Double.
        config.put("diggable", "\u2592".codePointAt(0)); // 50% shade
        config.put("floor", "\u25AA".codePointAt(0)); // small black square
        config.put("hall:floor", "\u25AB".codePointAt(0)); // sm. white square
        config.put("hall:wall", "\u2591".codePointAt(0)); // 25% shade
        config.put("room:door", "+".codePointAt(0));
        config.put("room:wall:top", "\u2500".codePointAt(0)); // light horiz
        config.put("room:wall:left", "\u2502".codePointAt(0)); // light vert
        config.put("room:wall:bottom", "\u2550".codePointAt(0)); // heavy horiz
        config.put("room:wall:right", "\u2551".codePointAt(0)); // heavy horiz
        config.put("room:wall:top-left", "\u250C".codePointAt(0)); // Lh/Lv
        config.put("room:wall:top-right", "\u2556".codePointAt(0)); // Lh/Hv
        config.put("room:wall:bottom-left", "\u2558".codePointAt(0)); // Hh/Lv
        config.put("room:wall:bottom-right", "\u255D".codePointAt(0)); // Hv/Hh
        
        // game specific
        config.put("void", " ".codePointAt(0));
        config.put("player", "@".codePointAt(0));
        config.put("water", "~".codePointAt(0));
        config.put("mountains", "^".codePointAt(0));

        grid = new Grid<>(config.get("diggable"), 100, 100);
        passable = new HashSet<>();
        passable.add(config.get("floor"));
        passable.add(config.get("hall:floor"));
        passable.add(config.get("room:door"));

        roomWalls = new HashSet<>();
        // roomWalls.add(config.get("room:wall"));
        roomWalls.add(config.get("room:wall:top"));
        roomWalls.add(config.get("room:wall:left"));
        roomWalls.add(config.get("room:wall:bottom"));
        roomWalls.add(config.get("room:wall:right"));
        roomWalls.add(config.get("room:wall:top-left"));
        roomWalls.add(config.get("room:wall:top-right"));
        roomWalls.add(config.get("room:wall:bottom-left"));
        roomWalls.add(config.get("room:wall:bottom-right"));

    }


    /**
     * Make a map
     */
    private void makeMap() {
        grid.clear();
        SimpleDigger simpleDigger = new SimpleDigger();
        BSPTree<Room> bsp = simpleDigger.setup(grid, config);
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
        // simpleDigger.digRoomAvoidanceHalls(bsp, grid, config);
        simpleDigger.digHallFirst(bsp, grid, config, false);
        underPlayer = config.get("room:floor");
        Positionable pos = rooms.get(idx).placeThing(grid, underPlayer, config.get("player"));
        this.player.setPosition(pos);
        recenterMap();
    }

    private void showMap() {
        int ey = MAP_END.getY();
        int ex = MAP_END.getX();
        if (ey <= 0) {
            ey += term.getHeight();
        }
        if (ex <= 0) {
            ex += term.getWidth();
        }
        Map<Integer, Representation> currentRep = this.representations.get(this.represent);
        for (int y = MAP_START.getY(); y < ey; y++) {
            for (int x = MAP_START.getX(); x < ex; x++) {
                int y1 = y + upperLeft.getY() - MAP_START.getY();
                int x1 = x + upperLeft.getX() - MAP_START.getX();
                int what = config.get("void");
                if (y1 >= 0 && x1 >= 0 && y1 < grid.getHeight() && x1 < grid.getWidth()) {
                    what = grid.get(y1, x1);
                }
                Representation how = currentRep.get(what);
                if (how == null) {
                    LOGGER.error("Failed to find entry for {}", BlackenKeys.toString(what));
                }
                double noise = PerlinNoise.noise(x1, y1, noisePlane);
                int as = how.getCodePoint(noise);
                int fclr = how.getColor(noise);
                int bclr = 0;
                EnumSet<CellWalls> walls = EnumSet.noneOf(CellWalls.class);
                if (what >= '0' && what <= '9') {
                    if (what > nextLocation) {
                        walls = CellWalls.BOX;
                    }
                }
                term.set(y, x, BlackenCodePoints.asString(as),
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
        term.disableEventNotices();
        int ch = BlackenKeys.NO_KEY;
        int mod;
        updateStatus();
        movePlayerBy(0,0);
        this.message = "Welcome to Dungeon!";
        term.move(-1, -1);
        while (!quit) {
            if (dirtyStatus) {
                updateStatus();
            }
            updateMessage(false);
            showMap();
            term.setCursorLocation(player.getY() - upperLeft.getY() + MAP_START.getY(), 
                                   player.getX() - upperLeft.getX() + MAP_START.getX());
            this.term.getPalette().rotate(0xee, 10, +1);
            // term.refresh();
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
        String msg = "Q to quit.";
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
        } else {
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
            case ' ':
                this.represent ++;
                if (this.represent >= this.representations.size()) {
                    this.represent = 0;
                }
                break;
            case BlackenKeys.KEY_BACKSPACE:
                this.represent --;
                if (this.represent < 0) {
                    this.represent = this.representations.size() -1;
                }
                break;
            case 'q':
            case 'Q':
            case BlackenKeys.KEY_ESCAPE:
                this.quit = true;
                return false;
            case 'L':
                showMyLicense();
                refreshScreen();
                break;
            case 'N':
                showLegalNotices();
                refreshScreen();
                break;
            case 'F':
                showFontLicense();
                refreshScreen();
                break;
            case '?':
                showHelp();
                refreshScreen();
                break;
            default:
                return false;
            }
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
                this.underPlayer = config.get("room:floor");
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
        upperLeft.setY(player.getY() - (term.getHeight()-2)/2);
        upperLeft.setX(player.getX() - (term.getWidth()-2)/2);
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
        addRepresentations();
    }
    
    /**
     * Start the example
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Dungeon that = new Dungeon();
        that.init(null, null);
        that.splash();
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

    private void centerOnLine(int y, String string) {
        int offset = term.getWidth() / 2 - string.length() / 2;
        term.mvputs(y, offset, string);
    }

    private void alignRight(int y, String string) {
        int offset = term.getWidth() - string.length();
        if (term.getHeight() -1 == y) {
            offset--;
        }
        term.mvputs(y, offset, string);
    }

    private void splash() {
        boolean ready = false;
        term.disableEventNotices();
        while (!ready) {
            term.clear();
            term.setCurBackground(0);
            term.setCurForeground(7);
            centerOnLine(0, "Dungeon");
            centerOnLine(1, "A simple demonstration of a dungeon");
            centerOnLine(3, "Copyright (C) 2010-2012 Steven Black");
            centerOnLine(5, "An example for the Blacken Roguelike Library.");
            centerOnLine(6, "Released under the Apache 2.0 License.");
            term.mvputs(8, 0, "HOW TO PLAY");
            term.mvputs(9, 0, "-----------");
            term.mvputs(10,0, "A representation of a map is shown.  You (the player) are the");
            term.mvputs(11,0, "at sign (@).  The object is to run around collecting the numbers");
            term.mvputs(12,0, "in order.  The numbers have walls around them that only open up");
            term.mvputs(13,0, "if you've collected the previous number.");
            term.mvputs(15,0, "Use the arrow keys to move around.  There are no opponents.");
            int last = term.getHeight() - 1;
            term.mvputs(last-1, 0, "Press '?' for Help.");
            alignRight(last-0, "Press any other key to continue.");
            int key = BlackenKeys.NO_KEY;
            while(key == BlackenKeys.NO_KEY) {
                // This works around an issue with the AWT putting focus someplace weird
                // if the window is not in focus when it is shown. It only happens on
                // startup, so a splash screen is the perfect place to fix it.
                // A normal game might want an animation at such a spot.
                key = term.getch(200);
            }
            // int modifier = BlackenKeys.NO_KEY;
            if (BlackenKeys.isModifier(key)) {
                // modifier = key;
                key = term.getch(); // should be immediate
            }
            switch(key) {
                case BlackenKeys.NO_KEY:
                case BlackenKeys.RESIZE_EVENT:
                    // should be safe
                    break;
                case 'l':
                case 'L':
                    showMyLicense();
                    break;
                case 'n':
                case 'N':
                    showLegalNotices();
                    break;
                case 'f':
                case 'F':
                    showFontLicense();
                    break;
                case '?':
                    showHelp();
                    break;
                default:
                    ready = true;
                    break;
            }
        }
    }

    private void showLegalNotices() {
        // show Notices file
        // This is the only one that needs to be shown for normal games.
        ViewerHelper vh;
        vh = new ViewerHelper(term, "Legal Notices", Obligations.getBlackenNotice());
        vh.setColor(7, 0);
        vh.run();
    }

    private void showFontLicense() {
        // show the font license
        ViewerHelper vh;
        new ViewerHelper(term,
                Obligations.getFontName() + " Font License",
                Obligations.getFontLicense()).run();
    }

    private void showHelp() {
        ViewerHelper vh;
        vh = new ViewerHelper(term, "Help", helpMessage);
        vh.setColor(7, 0);
        vh.run();
    }

    private void showMyLicense() {
        // show Apache 2.0 License
        ViewerHelper vh;
        vh = new ViewerHelper(term, "License", Obligations.getBlackenLicense());
        vh.setColor(7, 0);
        vh.run();
    }
}
