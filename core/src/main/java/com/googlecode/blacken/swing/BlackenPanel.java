/* blacken - a library for Roguelike games
 * Copyright © 2010-2012 Steven Black <yam655@gmail.com>
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
package com.googlecode.blacken.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.grid.SimpleSize;
import com.googlecode.blacken.grid.Sizable;
import com.googlecode.blacken.terminal.CellWalls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JPanel implementation supporting Blacken.
 * 
 * @author Steven Black
 */
public class BlackenPanel extends JPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackenPanel.class);
    private static final long serialVersionUID = 1;
    private AwtCell empty = new AwtCell();
    private int minX = 80;
    private int minY = 25;
    private Grid<AwtCell> grid = new Grid<>(empty, minY, minX);

    private boolean fontHasDouble = true;
    private int fontAscent;
    private int fontSglAdvance;
    private int fontDblAdvance;
    private int fontHeight;
    private Font font;
    private FontMetrics metrics;

    /**
     * X Position where the cursor will be when image is updated
     */
    private int cursorX;
    /**
     * Y Position where the cursor will be when image is updated
     */
    private int cursorY;

    /**
     * X Position where the cursor is in the image
     */
    private int lastCursorX;
    /**
     * Y Position where the cursor is in the image
     */
    private int lastCursorY;

    /**
     * The cursor's color
     */
    private Paint cursorColor;

    /**
     * Flag to ignore cell-specific refresh and refresh everything.
     */
    private transient boolean refresh_all = true;

    /**
     * Running average of the display speed
     */
    private transient long displaySpeed = 0;
    private int refreshedCnt = 0;
    private int repaintedCnt = 0;

    /**
     * Create a new panel.
     */
    public BlackenPanel() {
        super(true);
    }
    
    /**
     * Create a new panel with a layout manager.
     * @param layout layout manager
     */
    public BlackenPanel(LayoutManager layout) {
        super(layout, true);
    }

    /**
     * Clear the screen.
     */
    public void clear() {
        this.refresh_all = true;
        grid.clear(this.empty);
        this.moveCursor(0, 0);
    }
    
    /**
     * Perform a window update.
     * @deprecated No longer needed
     */
    @Deprecated
    public void doUpdate() {
    }
    
    /**
     * Find the column number for the window coordinate
     * @param x window coordinate
     * @return column
     */
    public int findColForWindow(int x) {
        Rectangle r = this.getRootPane().getBounds();
        x -= r.x;
        int ret = x / this.fontSglAdvance;
        return ret;
    }
    /**
     * Find the grid position for a window position.
     * 
     * @param y window coordinate
     * @param x window coordinate
     * @return {row, col}
     */
    public int[] findPositionForWindow(int y, int x) {
        Rectangle r = this.getRootPane().getBounds();
        y -= r.y;
        int retY = y / this.fontHeight;
        x -= r.x;
        int retX = x / this.fontSglAdvance;
        int[] ret = {retY, retX};
        return ret;
    }
    /**
     * Find the row number for the window coordinate
     * @param y window coordinate
     * @return row
     */
    public int findRowForWindow(int y) {
        Rectangle r = this.getRootPane().getBounds();
        y -= r.y;
        int ret = y / this.fontHeight;
        return ret;
    }
    /**
     * Get an AWT cell for a position.
     * 
     * @param y coordinate
     * @param x coordinate
     * @return the AWT cell
     */
    public AwtCell get(int y, int x) {
        return grid.get(y, x);
    }
    /**
     * Get the best window size.
     * 
     * @return window size, as a SimpleSize
     */
    protected Sizable getBestWindowSize() {
        int xsize, ysize;
        Sizable gridSize = grid.getSize();
        xsize = fontSglAdvance * gridSize.getWidth();
        ysize = fontHeight * gridSize.getHeight();
        return new SimpleSize(ysize, xsize);
    }
    /**
     * Get the empty/template cell
     * @return the empty/template
     */
    public AwtCell getEmpty() {
        return empty;
    }

    @Override
    public Font getFont() {
        if (empty == null) {
            return null;
        }
        return empty.getFont();
    }
    /**
     * Get the grid size
     * @return {ySize, xSize}
     */
    public Regionlike getGridBounds() {
        return grid.getBounds();
    }
    /**
     * Hide the cursor.
     */
    public void hideCursor() {
        moveCursor(-1, -1, null);
    }
    /**
     * Initialize the terminal window.
     * 
     * @param font the font to use
     * @param rows the number of rows to use
     * @param cols the columns to use
     * @param empty the template/empty cell
     */
    public void init(Font font, int rows, int cols, AwtCell empty) {
        setCursor(null);
        this.setFocusTraversalKeysEnabled(false);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setBounds(0, 0, width, height);
        this.minY = rows;
        this.minX = cols;
        setFont(font, false);
        grid.reset(rows, cols, empty);
        repaint();
    }

    /**
     * Move a block of cells.
     * 
     * @param numRows number of rows to move
     * @param numCols number of columns to move
     * @param origY orignal Y coordinate
     * @param origX orignal X coordinate
     * @param newY new Y coordinate
     * @param newX new X coordinate
     */
    public void moveBlock(int numRows, int numCols, int origY, int origX,
                          int newY, int newX) {
        grid.moveBlock(numRows, numCols, origY, origX, newY, newX, 
                       new AwtCell().new ResetCell());
    }
    
    /**
     * Move the cursor.
     * 
     * @param y coordinate
     * @param x coordinate
     */
    public void moveCursor(int y, int x) {
        moveCursor(y, x, null);
    }
    
    /**
     * Move the cursor, and set a new cursor color.
     * 
     * @param y coordinate
     * @param x coordinate
     * @param cursorColor new cursor color
     */
    public void moveCursor(int y, int x, Paint cursorColor) {
        if (cursorColor != null) {
            this.cursorColor = cursorColor;
        }
        cursorX = x;
        cursorY = y;
    }

    @Override
    public void paintComponent(Graphics g) {
        AwtCell c;
        boolean need_cursor = false;
        Graphics2D graphics = (Graphics2D)g;

        synchronized(this) {
            if (this.refreshedCnt == this.repaintedCnt) {
                LOGGER.error("Dropped an paintComponent update -- didn't come through refresh()");
                return;
            }
            this.repaintedCnt = this.refreshedCnt;
        }

        try {
            long startTime = System.currentTimeMillis();
            if (refresh_all) {
                graphics.setPaint(Color.BLACK);
                graphics.fill(getBounds());
            } else {
                if (lastCursorX != -1 && lastCursorY != -1) {
                    if (cursorX != lastCursorX || cursorY != lastCursorY) {
                        if (grid.contains(lastCursorY, lastCursorX)) {
                            c = grid.get(lastCursorY, lastCursorX);
                            c.setDirty(true);
                        }
                    }
                }
            }
            if (cursorX != -1 && cursorY != -1) {
                if (grid.contains(cursorY, cursorX)) {
                    c = grid.get(cursorY, cursorX);
                    if (c.isDirty()) {
                        need_cursor = true;
                    } else if (cursorX != lastCursorX || cursorY != lastCursorY) {
                        need_cursor = true;
                    }
                } else {
                    need_cursor = true;
                    if (cursorY >= grid.getHeight() + grid.getY()) {
                        cursorY = grid.getHeight() + grid.getY() -1;
                    }
                    if (cursorX >= grid.getWidth() + grid.getX()) {
                        cursorX = grid.getWidth() + grid.getX() -1;
                    }
                }
            }
            lastCursorX = cursorX;
            lastCursorY = cursorY;

            for (int y = 0; y < grid.getHeight(); y++) {
                // We do the background then the foreground so that double-wide
                // characters get the background set reasonably.

                for (int x = 0; x < grid.getWidth(); x++) {
                    c = grid.get(y, x);
                    if (c.isDirty() || refresh_all) {
                        graphics.setPaint(c.getBackgroundColor());
                        graphics.fill(new Rectangle(x * fontSglAdvance,
                                                    y * fontHeight,
                                                    fontSglAdvance,
                                                    fontHeight));
                    }
                }
                final int fontHeightD2 = fontHeight / 2;
                final int fontSglAdvanceD2 = fontSglAdvance / 2;

                for (int x = 0; x < grid.getWidth(); x++) {
                    c = grid.get(y, x);

                    if (!c.isDirty() && !refresh_all) {
                        continue;
                    }
                    String cs = c.getSequence();
                    c.setFont(this.font);
                    // For double-wide characters, we can safely put a NUL
                    // byte in the second slot and it will never be displayed.
                    if (cs != null && !"\u0000".equals(cs)) {
                        int w = metrics.stringWidth(cs);
                        w = fontSglAdvance - w;
                        if (w < 0) {
                            w = 0;
                        } else {
                            w /= 2;
                        }
                        graphics.setBackground(c.getBackgroundColor());
                        graphics.setColor(c.getForegroundColor());
                        graphics.drawString(c.getSequence(),
                                        x * fontSglAdvance + w,
                                        y * fontHeight + fontAscent);
                    }

                    c.setDirty(false);
                    if (c.getCellWalls() != null && !c.getCellWalls().isEmpty()) {
                        int x1 = x * fontSglAdvance;
                        int y1 = y * fontHeight;
                        graphics.setColor(c.getForegroundColor());
                        if (c.getCellWalls().contains(CellWalls.TOP)) {
                            graphics.drawLine(x1, y1,
                                                x1 + fontSglAdvance -1, y1);
                        }
                        if (c.getCellWalls().contains(CellWalls.LEFT)) {
                            graphics.drawLine(x1, y1, x1,
                                                y1 + fontHeight -1);
                        }
                        if (c.getCellWalls().contains(CellWalls.BOTTOM)) {
                            graphics.drawLine(x1, y1 + fontHeight-1,
                                                x1 + fontSglAdvance -1,
                                                y1 + fontHeight-1);
                        }
                        if (c.getCellWalls().contains(CellWalls.RIGHT)) {
                            graphics.drawLine(x1 + fontSglAdvance-1, y1,
                                                x1 + fontSglAdvance-1,
                                                y1 + fontHeight -1);
                        }
                        if (c.getCellWalls().containsAll(CellWalls.HORIZONTAL)) {
                            graphics.drawLine(x1, y1 + fontHeightD2,
                                                x1 + fontSglAdvance -1,
                                                y1 + fontHeightD2);
                        }
                        if (c.getCellWalls().containsAll(CellWalls.VERTICAL)) {
                            graphics.drawLine(x1 + fontSglAdvanceD2, y1,
                                                x1 + fontSglAdvanceD2,
                                                y1 + fontHeight -1);
                        }
                    }
                }
            }
            if (need_cursor) {
                c = grid.get(cursorY, cursorX);
                if (cursorColor == null) {
                    graphics.setPaint(c.getForegroundColor());
                } else {
                    graphics.setPaint(cursorColor);
                }
                graphics.fill(new Rectangle(cursorX * fontSglAdvance,
                                            cursorY * fontHeight + fontAscent,
                                            fontSglAdvance -1,
                                            fontHeight - fontAscent -1));
            }
            refresh_all = false;
            long endTime = System.currentTimeMillis();
            if (this.displaySpeed == 0) {
                this.displaySpeed = endTime - startTime;
            } else {
                this.displaySpeed = (displaySpeed + endTime - startTime) / 2;
            }
            //LOGGER.info("Panel update speed: {} ms / Average: {} ms",
            //         endTime - startTime, displaySpeed);
        } finally {
            synchronized(this) {
                this.notifyAll();
            }
        }
    }
    
    /**
     * Recalculate the font bits.
     * 
     * <p>The logic this uses totally breaks down if a variable-width font is 
     * used.
     * 
     * <p>For a variable-width font, you'd need to walk every character you
     * plan to use, track the width, then use the max double-wide width or 2x 
     * the max single-wide width... That is, if you plan to do the 
     * single-width / double-width logic traditionally found on terminals.
     *  
     * <p>If you want variable width fonts, it is probably best not to treat it 
     * as a traditional double-wide character and to instead treat it as a 
     * large single-width character -- so that <code>fontDblAdvance</code> and 
     * <code>fontSglAdvance</code> are the same and <code>fontHasDouble</code>
     * is false.
     */
    protected void recalculateFontBits() {
        if (this.getGraphics() == null) {
            return;
        }
        metrics = this.getGraphics().getFontMetrics(this.font);
        fontAscent = metrics.getMaxAscent();
        fontDblAdvance = metrics.getMaxAdvance();
        fontSglAdvance = metrics.charWidth('W');
        if (fontDblAdvance == -1) {
            fontDblAdvance = fontSglAdvance;
        }
        fontHasDouble = false;
        if (fontDblAdvance >= fontSglAdvance + fontSglAdvance) {
            fontHasDouble = true;
        } 
        if (fontHasDouble) {
            int sa = fontDblAdvance / 2;
            if (sa >= fontSglAdvance) {
                fontSglAdvance = sa;
            } else {
                fontDblAdvance = fontSglAdvance * 2;
            }
        }
        /* XXX: Here's the issue:
         * <ul>
         * <li>The font metrics are based upon the base-line location.
         * <li>Any font can have multiple base-line locations.
         * <li>More-over, any reasonably complete font <i>will</i> have 
         *     multiple baseline locations.
         * <li>FontMetrics.getHeight() does not claim to use the MaxAscent and 
         *     MaxDescent.
         * <li>getAscent and getDescent explicitly state that some glyphs will
         *     fall outside the ascent and descent lines that they describe.
         * <li>The font height, as it is all based upon distance away the 
         *     baseline, will be wrong if we just use getMaxAscent and
         *     getMaxDescent. (Just imagine that ROMAN_BASELINE has glyphs
         *     spanning far above the baseline, and the same font has 
         *     HANGING_BASELINE glyphs which hang far below the baseline.
         *     They could even be the same height!)
         * <li>LineMetrics (which can provide baseline offsets) does not 
         *     appear to support any supplementary 
         *     characters.
         * <li>Our use-case doesn't require consistent baseline between glyphs
         *     which use a different baseline. We much prefer to maximize the
         *     visible cell. This is consistent with what you'd expect in a
         *     terminal application with a fixed-point font.
         * </ul>
         * 
         * The best solution, then, would be to keep separate metrics for
         * font regions which use ROMAN_BASELINE, HANGING_BASELINE, and
         * CENTER_BASELINE glyphs. Unfortunately, there doesn't seem to be a
         * way to track what sort of baseline is used by any particular glyph.
         * The system wants you to check the metrics for each glyph. (As you
         * can, in fact, have fonts which are composed of other fonts, so
         * things can have the same baseline and still have different metrics.)
         * <p>
         * I think, for us, the best approach would be to require a consistent
         * font within a Unicode range, and to allow for custom fonts for 
         * specific code ranges. We could then track the metrics for the Unicode
         * code ranges instead of for each font.
         * <p>
         * For details on the various code ranges, and the glyphs supported
         * by each: http://unicode.org/charts/
         */
        //fontHeight = metrics.getMaxAscent() + metrics.getMaxDescent() 
        //                                    + metrics.getLeading();
        fontHeight = metrics.getHeight() /* + 2 */;
    }
    /**
     * Refresh the window.
     */
    public void refresh() {
        synchronized(this) {
            this.refreshedCnt ++;
            repaint();
            try {
                this.wait();
            } catch (InterruptedException ex) {
                // do nothing
            }
        }
    }
    /**
     * Refresh a row/line.
     * @param y the line to refresh
     */
    public void refreshLine(int y) {
        if (y < 0) { y = 0; }
        if (y > grid.getHeight()) { y = grid.getHeight(); } 
        for (int x = 0; x < grid.getWidth(); x++) {
            grid.get(y, x).setDirty(true);
        }
    }

    /**
     * Refresh a box on the screen.
     * 
     * @param height height of the box
     * @param width width of the box
     * @param y1 coordinate of the box
     * @param x1 coordinate of the box
     */
    public void refreshRegion(int height, int width, int y1, int x1) {
        if (y1 < 0) y1 = 0;
        if (x1 < 0) x1 = 0;

        if (height < 0) height = grid.getHeight();
        if (height + y1 >= grid.getHeight()) {
            height = grid.getHeight() - y1;
        }

        if (width < 0) width = grid.getWidth();
        if (width + y1 >= grid.getWidth()) {
            width = grid.getWidth() - y1;
        }

        int y2 = y1 + height -1;
        int x2 = x1 + width -1;

        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                grid.get(y, x).setDirty(true);
            }
        }
    }

    protected void resizeFontToFit() {
        // Rectangle r = this.getRootPane().getContentPane().getBounds();
        Rectangle r = this.getVisibleRect();
        float fsize = 0.5f;
        int idealAdvance = r.width / this.minX;
        int idealHeight = r.height / this.minY;
        //LOGGER.debug("ideal:{}, height:{}, idealAdvance, idealHeight");
        Font f = this.font.deriveFont(fsize);
        setFontNoUpdate(f);
        if (idealAdvance <= fontSglAdvance || idealHeight <= fontHeight) {
            //LOGGER.debug("BOGUS advance:{}; height:{}",
                    //fontSglAdvance, fontHeight);
            // This is a real bogus size, but apparently we can't get 
            // anything better
            return;
        }
        Font lastFont = f;
        //LOGGER.debug("size:{}; advance:{}; height:{}",
        //        new Object[] {fsize, fontSglAdvance, fontHeight});
        while (idealAdvance >= fontSglAdvance && idealHeight >= fontHeight) {
            lastFont = f;
            f = lastFont.deriveFont(fsize += 0.5f); 
            setFontNoUpdate(f);
            //LOGGER.debug("size:{}; advance:{}; height:{}",
            //        new Object[] {fsize, fontSglAdvance, fontHeight});
        }
        setFont(lastFont, false);
        int newRows = r.height / fontHeight;
        int newCols = r.width / fontSglAdvance;
        grid.setSize(newRows, newCols);
        // LOGGER.debug("grid: ys:{}; xs:{}", newRows, newCols);
    }
    
    /**
     * Resize the frame.
     * 
     * @param frame frame to resize
     * @param fontSize font size to use
     */
    public void resizeFrame(JFrame frame, int fontSize) {
        Font f;
        if (fontSize > 0) {
            f = this.font;
            if (f == null) {
                f = new Font("Monospace", Font.PLAIN, fontSize); //$NON-NLS-1$
            } else {
                f = f.deriveFont(fontSize);
            }
            setFont(f, false);
        }
        Sizable sizes = getBestWindowSize();
        frame.setSize(sizes.getWidth(), sizes.getHeight());
    }
    /**
     * Resize the grid
     * 
     * @param rows new rows
     * @param cols new columns
     */
    public void resizeGrid(int rows, int cols) {
        grid.setSize(rows, cols);
    }
    
    /**
     * Resize the grid to the window.
     */
    protected void resizeGridToWindow() {
        int xsize, ysize;
        Dimension d = this.getSize();
        xsize = d.width / fontSglAdvance;
        ysize = d.height / fontHeight;
        this.grid.setSize(ysize, xsize);
    }
    
    /**
     * Set a cell.
     * 
     * <p>Note: This does nothing to ease the issues inherent in double-wide
     * characters. The background of a double-wide character needs to be set
     * individually, and the second half of the character needs the glyph
     * cleared or it will overwrite. -- This has particular implications
     * when changing an existing double-wide character, as the second-half
     * needs to be marked as dirty separately.
     * 
     * @param y row number
     * @param x column number
     * @param cell cell definition
     */
    public void set(int y, int x, AwtCell cell) {
        AwtCell c = grid.get(y, x);
        c.set(cell);
        c.setDirty(true);
    }
    
    /**
     * Set a cell to some common values.
     * 
     * @param y row
     * @param x column
     * @param glyph sequence
     * @param back background color
     * @param fore foreground color
     */
    public void set(int y, int x, int glyph, Color back, Color fore) {
        grid.get(y, x).setCell(glyph, back, fore);
    }

    /**
     * Set a cell to some things.
     * 
     * @param y row
     * @param x column
     * @param glyph sequence
     * @param attributes text attributes
     */
    public void set(int y, int x, int glyph, 
                    Map<TextAttribute, Object> attributes) {
        grid.get(y, x).setCell(glyph, attributes);
    }

    /**
     * Set the empty/template cell
     * @param empty new empty cell
     */
    public void setEmpty(AwtCell empty) {
        if (this.empty != empty) {
            this.empty.set(empty);
        }
    }
    /*
     * (non-Javadoc)
     * @see javax.swing.JComponent#setFont(java.awt.Font)
     */
    @Override
    public void setFont(Font font) {
        setFont(font, true);
    }

    /**
     * Set the font.
     * @param font font to use
     * @param recalc true to recalculate
     */
    private void setFont(Font font, boolean recalc) {
        setFontNoUpdate(font);
        if (recalc && this.getGraphics() != null) {
            resizeFontToFit();
        }
        if (empty != null && this.font != null) {
            if (empty.getFont() == null || 
                    !empty.getFont().equals(this.font)) {
                empty.setFont(this.font);
                for(int y = 0; y < grid.getHeight(); y++) {
                    for(int x = 0; x < grid.getWidth(); x++) {
                        grid.get(y, x).setFont(this.font);
                    }
                }
            }
        }
    }

    /**
     * Set the font and do not update
     * @param font new font
     */
    private void setFontNoUpdate(Font font) {
        if (font != null) {
            this.font = font;
        }
        super.setFont(this.font);
        if(this.getGraphics() != null) {
            this.getGraphics().setFont(this.font);
        }
        recalculateFontBits();
    }

    /**
     * Process a window resize event.
     */
    public void windowResized() {
        resizeFontToFit();
        refresh();
    }

}
