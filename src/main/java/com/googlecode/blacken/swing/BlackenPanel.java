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
package com.googlecode.blacken.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.grid.SimpleSize;
import com.googlecode.blacken.grid.Sizable;
import com.googlecode.blacken.terminal.CellWalls;

/**
 * A JPanel implementation supporting Blacken.
 * 
 * @author yam655
 */
public class BlackenPanel extends JPanel {
    private static final long serialVersionUID = 4608415642377103276L;
    private AwtCell empty = new AwtCell();
    private int minX = 80;
    private int minY = 25;
    private Grid<AwtCell> grid = new Grid<AwtCell>(empty, minY, minX);
    private final ReentrantLock imageChangeLock = new ReentrantLock();
    
    private transient Image image = null;
    private transient Graphics2D graphics = null;

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
    private boolean refresh_all;

    /**
     * The AWT-side image.
     */
    private Image imageDisplay = null;

    /**
     * Marker for aborted Terminal-side updates.
     */
    private int updates;

    /**
     * Indicates the AWT-side image needs to be updated.
     */
    private boolean changed;
    
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
        this.maybeRepaint();
        grid.clear(this.empty);
        this.moveCursor(0, 0);
    }
    
    /**
     * Perform a window update.
     */
    public void doUpdate() {
        paintImage();
    }
    
    /**
     * Find the column number for the window coordinate
     * @param x window coordinate
     * @return column
     */
    public int findColForWindow(int x) {
        this.maybeRepaint();
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
        this.maybeRepaint();
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
        this.maybeRepaint();
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
        this.maybeRepaint();
        return grid.get(y, x);
    }
    /**
     * Get the best window size.
     * 
     * @return window size, as a Positionable
     */
    protected Sizable getBestWindowSize() {
        this.maybeRepaint();
        int xsize, ysize;
        Sizable gridSize = grid.getSize();
        xsize = fontSglAdvance * gridSize.getHeight();
        ysize = fontHeight * gridSize.getWidth();
        return new SimpleSize(ysize, xsize);
    }
    /**
     * Get the empty/template cell
     * @return the empty/template
     */
    public AwtCell getEmpty() {
        this.maybeRepaint();
        return empty;
    }
    /*
     * (non-Javadoc)
     * @see java.awt.Component#getFont()
     */
    @Override
    public Font getFont() {
        this.maybeRepaint();
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
        this.maybeRepaint();
        return grid.getBounds();
    }
    /**
     * Hide the cursor.
     */
    public void hideCursor() {
        this.maybeRepaint();
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
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setBounds(0, 0, width, height);
        image = createImage(width, height);
        graphics = (Graphics2D) image.getGraphics();
        imageDisplay = createImage(width, height);
        this.minY = rows;
        this.minX = cols;
        setFont(font, false);
        grid.reset(rows, cols, empty);
        repaint();
    }

    /**
     * Maybe repaint the screen.
     * 
     * <p>We can skip the updates if we need to. As such, we try to find places
     * to perform the repaint if needed.</p>
     */
    private void maybeRepaint() {
        if (this.updates != 0) {
            paintImage();
        }
        if (this.changed) {
            repaint();
        }
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
        this.maybeRepaint();
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
        this.maybeRepaint();
        if (cursorColor != null) {
            this.cursorColor = cursorColor;
        }
        cursorX = x;
        cursorY = y;
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        try {
            if (!imageChangeLock.tryLock(0, TimeUnit.SECONDS)) {
                return;
            }
        } catch (InterruptedException e) {
            return;
        }
        try {
            if (this.changed) {
                this.changed = false;
                imageDisplay.getGraphics().drawImage(image, 0, 0, null);
            }
            g.drawImage(imageDisplay, 0, 0, null);
        } finally {
            imageChangeLock.unlock();
        }
    }

    /**
     * Paint the bitmap the terminal-side sees.
     * 
     * <p>The Terminal and AWT sides exist in separate threads. This function
     * updates the Terminal-visible bitmap. {@link #paintComponent(Graphics)}
     * copies the Terminal-visible bitmap to the AWT-visible bitmap.</p>
     */
    public void paintImage() {
        AwtCell c = null;
        boolean need_cursor = false;
        if (imageChangeLock == null) {
            return;
        }
        try {
            if (!imageChangeLock.tryLock(0, TimeUnit.SECONDS)) {
                this.updates ++;
                return;
            }
        } catch (InterruptedException e) {
            this.updates ++;
            return;
        }

        try {
            this.updates = 0;
            this.changed = true;
            if (refresh_all) {
                graphics.setPaint(Color.BLACK);
                graphics.fill(getBounds());
            }
    
            if (lastCursorX != -1 && lastCursorY != -1) {
                if (cursorX != lastCursorX || cursorY != lastCursorY) {
                    if (grid.contains(lastCursorY, lastCursorX)) {
                        c = grid.get(lastCursorY, lastCursorX);
                        c.setDirty(true);
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
                // I'm not sure whether this will be enough to get the "complex"
                // characters to work. -- We may need to do it by code-point.
                int fontHeightD2 = fontHeight / 2;
                int fontSglAdvanceD2 = fontSglAdvance / 2;
                for (int x = 0; x < grid.getWidth(); x++) {
                    c = grid.get(y, x);
                    if (c.isDirty() || refresh_all) {
                        String cs = c.getSequence();
                        c.setFont(this.font);
                        // For double-wide characters, we can safely put a NUL
                        // byte in the second slot and it will never be displayed.
                        if (cs != null && !"\u0000".equals(cs)) { //$NON-NLS-1$
                            int w = metrics.stringWidth(cs);
                            w = fontSglAdvance - w;
                            if (w < 0) w = 0;
                            else w /= 2;
                            graphics.drawString(c.getAttributedString().getIterator(), 
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
                            if (c.getCellWalls().contains(CellWalls.HORIZONTAL)) {
                                graphics.drawLine(x1, y1 + fontHeightD2, 
                                                  x1 + fontSglAdvance -1, 
                                                  y1 + fontHeightD2);
                            }
                            if (c.getCellWalls().contains(CellWalls.VERTICAL)) {
                                graphics.drawLine(x1 + fontSglAdvanceD2, y1, 
                                                  x1 + fontSglAdvanceD2, 
                                                  y1 + fontHeight -1);
                            }
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
        } finally {
            imageChangeLock.unlock();
        }
    }
    
    /**
     * Recalculate the font bits.
     * 
     * <p>The logic this uses totally breaks down if a variable-width font is 
     * used.</p>
     * 
     * <p>For a variable-width font, you'd need to walk every character you
     * plan to use, track the width, then use the max double-wide width or 2x 
     * the max single-wide width... That is, if you plan to do the 
     * single-width / double-width logic traditionally found on terminals.</p>
     *  
     * <p>If you want variable width fonts, it is probably best not to treat it 
     * as a traditional double-wide character and to instead treat it as a 
     * large single-width character -- so that <code>fontDblAdvance</code> and 
     * <code>fontSglAdvance</code> are the same and <code>fontHasDouble</code>
     * is false.</p> 
     */
    protected void recalculateFontBits() {
        if (this.getGraphics() == null) {
            return;
        }
        this.maybeRepaint();
        metrics = this.getGraphics().getFontMetrics(this.font);
        fontAscent = metrics.getMaxAscent()+1;
        fontDblAdvance = metrics.getMaxAdvance();
        fontSglAdvance = metrics.charWidth('W');
        if (fontDblAdvance == -1) fontDblAdvance = fontSglAdvance;
        if (fontDblAdvance >= fontSglAdvance + fontSglAdvance) {
            fontHasDouble = true;
        } else fontHasDouble = false;
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
        fontHeight = metrics.getHeight() +2;
    }
    /**
     * Refresh the window.
     */
    public void refresh() {
        refresh_all = true;
        paintImage();
    }
    /**
     * Refresh a row/line.
     * @param y the line to refresh
     */
    public void refreshLine(int y) {
        this.maybeRepaint();
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
        this.maybeRepaint();
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
        this.maybeRepaint();
        // Rectangle r = this.getRootPane().getContentPane().getBounds();
        Rectangle r = this.getVisibleRect();
        float fsize = 0.5f;
        int idealAdvance = r.width / this.minX;
        int idealHeight = r.height / this.minY;
        //System.out.printf("DEBUG(font2fit): ideal:%d height:%d\n", 
        //                  idealAdvance, idealHeight);
        Font f = this.font.deriveFont(fsize);
        setFontNoUpdate(f);
        if (idealAdvance <= fontSglAdvance || idealHeight <= fontHeight) {
            //System.out.printf("DEBUG(font2fit): BOGUS advance:%d height:%d\n", 
            //                  fontSglAdvance, fontHeight);
            // This is a real bogus size, but apparently we can't get 
            // anything better
            return;
        }
        Font lastFont = f;
        // System.out.printf("DEBUG(font2fit): size:%f advance:%d height:%d\n", 
        //                  fsize, fontSglAdvance, fontHeight);
        while (idealAdvance >= fontSglAdvance && idealHeight >= fontHeight) {
            lastFont = f;
            f = lastFont.deriveFont(fsize += 0.5f); 
            setFontNoUpdate(f);
            // System.out.printf("DEBUG(font2fit): size:%f advance:%d height:%d\n", fsize, fontSglAdvance, fontHeight);
        }
        setFont(lastFont, false);
        int newRows = r.height / fontHeight;
        int newCols = r.width / fontSglAdvance;
        grid.setSize(newRows, newCols);
        //System.out.printf("DEBUG(font2fit): grid: ys:%d; xs:%d\n", newRows, newCols);
    }
    
    /**
     * Resize the frame.
     * 
     * @param frame frame to resize
     * @param fontSize font size to use
     */
    public void resizeFrame(JFrame frame, int fontSize) {
        this.maybeRepaint();
        Font f = null;
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
        this.maybeRepaint();
        grid.setSize(rows, cols);
    }
    
    /**
     * Resize the grid to the window.
     */
    protected void resizeGridToWindow() {
        this.maybeRepaint();
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
        this.maybeRepaint();
        AwtCell c = grid.get(y, x);
        c.setCell(cell);
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
        this.maybeRepaint();
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
        this.maybeRepaint();
        grid.get(y, x).setCell(glyph, attributes);
    }

    /**
     * Set the empty/template cell
     * @param empty new empty cell
     */
    public void setEmpty(AwtCell empty) {
        this.maybeRepaint();
        if (this.empty != empty) {
            this.empty.setCell(empty);
        }
    }
    /*
     * (non-Javadoc)
     * @see javax.swing.JComponent#setFont(java.awt.Font)
     */
    @Override
    public void setFont(Font font) {
        this.maybeRepaint();
        setFont(font, true);
    }

    /**
     * Set the font.
     * @param font font to use
     * @param recalc true to recalculate
     */
    private void setFont(Font font, boolean recalc) {
        this.maybeRepaint();
        setFontNoUpdate(font);
        if (recalc && graphics != null) {
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
        this.maybeRepaint();
        if (font != null) {
            this.font = font;
        }
        super.setFont(this.font);
        if(graphics != null) {
            graphics.setFont(this.font);
            this.getGraphics().setFont(this.font);
        }
        recalculateFontBits();
    }

    /**
     * Process a window resize event.
     */
    public void windowResized() {
        this.maybeRepaint();
        resizeFontToFit();
        refresh();
    }


}
