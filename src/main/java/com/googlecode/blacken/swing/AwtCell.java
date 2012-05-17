/* blacken - a library for Roguelike games
 * Copyright © 2010-2012 Steven Black <yam655@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with this program.  
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.blacken.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.GraphicAttribute;
import java.awt.font.NumericShaper;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.googlecode.blacken.grid.DirtyGridCell;
import com.googlecode.blacken.terminal.CellWalls;

/**
 * An AWT cell.
 * 
 * <p>This encapsulates the AWT properties of a cell.</p>
 * 
 * @author yam655
 */
public class AwtCell implements Cloneable {
    /**
     * Make the AWT cell dirty
     * 
     * @author yam655
     */
    public class ResetCell implements DirtyGridCell<AwtCell> {

        /*
         * (non-Javadoc)
         * @see com.googlecode.blacken.grid.ResetGridCell#reset(com.googlecode.blacken.grid.Copyable)
         */
        @Override
        public void setDirty(AwtCell cell, boolean isDirty) {
            cell.setDirty(true);
        }

    }
    private String sequence;
    private boolean dirty;
    private Map<TextAttribute, Object> attributes = 
        new HashMap<TextAttribute, Object>();
    private EnumSet<CellWalls> cellWalls = EnumSet.noneOf(CellWalls.class);
    
    /**
     * Make an unset AWT cell.
     */
    public AwtCell() {
        super();
        setCell("\u0000", Color.BLACK, Color.WHITE); //$NON-NLS-1$
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override 
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (sequence == null) {
            buf.append("null"); //$NON-NLS-1$
        } else {
            buf.append('"');
            for (char c : sequence.toCharArray()) {
                if (c < ' ' || c > 127) {
                    buf.append(String.format("\\u04x", c)); //$NON-NLS-1$
                } else {
                    buf.append(c);
                }
            }
            buf.append('"');
        }
        Color clr = getForegroundColor();
        if (clr == null) {
            buf.append(", null"); //$NON-NLS-1$
        } else {
            buf.append(String.format(", 0x%08x", clr.getRGB())); //$NON-NLS-1$
        }
        clr = getBackgroundColor();
        if (clr == null) {
            buf.append(", null"); //$NON-NLS-1$
        } else {
            buf.append(String.format(", 0x%08x", clr.getRGB())); //$NON-NLS-1$
        }
        if (dirty) {
            buf.append(", DIRTY"); //$NON-NLS-1$
        } else {
            buf.append(", clean"); //$NON-NLS-1$
        }
        if (attributes == null || attributes.isEmpty()) {
            buf.append(", {}"); //$NON-NLS-1$
        } else {
            buf.append(", {"); //$NON-NLS-1$
            for (TextAttribute att : attributes.keySet()) {
                if (att == TextAttribute.WEIGHT){
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.WEIGHT_BOLD)) {
                        buf.append("WEIGHT_BOLD"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WEIGHT_DEMIBOLD)) {
                        buf.append("WEIGHT_DEMIBOLD"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WEIGHT_DEMILIGHT)) {
                        buf.append("WEIGHT_DEMILIGHT"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WEIGHT_EXTRA_LIGHT)) {
                        buf.append("WEIGHT_EXTRA_LIGHT"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WEIGHT_EXTRABOLD)) {
                        buf.append("WEIGHT_EXTRABOLD"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WEIGHT_HEAVY)) {
                        buf.append("WEIGHT_HEAVY"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WEIGHT_LIGHT)) {
                        buf.append("WEIGHT_LIGHT"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WEIGHT_MEDIUM)) {
                        buf.append("WEIGHT_MEDIUM"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WEIGHT_REGULAR)) {
                        buf.append("WEIGHT_REGULAR"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WEIGHT_SEMIBOLD)) {
                        buf.append("WEIGHT_SEMIBOLD"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WEIGHT_ULTRABOLD)) {
                        buf.append("WEIGHT_ULTRABOLD"); //$NON-NLS-1$
                    } else {
                        float f = (Float)value;
                        buf.append(String.format("WEIGHT:%f", f)); //$NON-NLS-1$
                    }
                } else if (att == TextAttribute.WIDTH) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.WIDTH_CONDENSED)) {
                        buf.append("WIDTH_CONDENSED"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WIDTH_EXTENDED)) {
                        buf.append("WIDTH_EXTENDED"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WIDTH_REGULAR)) {
                        buf.append("WIDTH_REGULAR"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WIDTH_SEMI_CONDENSED)) {
                        buf.append("WIDTH_SEMI_CONDENSED"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.WIDTH_SEMI_EXTENDED)) {
                        buf.append("WIDTH_SEMI_EXTENDED"); //$NON-NLS-1$
                    }
                } else if (att == TextAttribute.KERNING) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.KERNING_ON)) {
                        buf.append("KERNING_ON"); //$NON-NLS-1$
                    } else {
                        Integer v = (Integer)value;
                        buf.append(String.format("KERNING:%d", v)); //$NON-NLS-1$
                    }
                } else if (att == TextAttribute.LIGATURES) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.LIGATURES_ON)) {
                        buf.append("LIGATURES_ON"); //$NON-NLS-1$
                    } else {
                        Integer v = (Integer)value;
                        buf.append(String.format("LIGATURES:%d", v)); //$NON-NLS-1$
                    }
                } else if (att == TextAttribute.POSTURE) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.POSTURE_OBLIQUE)) {
                        buf.append("POSTURE_OBLIQUE"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.POSTURE_REGULAR)) {
                        buf.append("POSTURE_REGULAR"); //$NON-NLS-1$
                    } else {
                        Float f = (Float)value;
                        buf.append(String.format("POSTURE:%f", f)); //$NON-NLS-1$
                    }
                } else if (att == TextAttribute.STRIKETHROUGH) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.STRIKETHROUGH_ON)) {
                        buf.append("STRIKETHROUGH_ON"); //$NON-NLS-1$
                    } else {
                        Integer v = (Integer)value;
                        buf.append(String.format("STRIKETHROUGH:%d", v)); //$NON-NLS-1$
                    }
                } else if (att == TextAttribute.SUPERSCRIPT) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.SUPERSCRIPT_SUB)) {
                        buf.append("SUPERSCRIPT_SUB"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.SUPERSCRIPT_SUPER)) {
                        buf.append("SUPERSCRIPT_SUPER"); //$NON-NLS-1$
                    } else {
                        Integer v = (Integer)value;
                        buf.append(String.format("SUPERSCRIPT:%d", v)); //$NON-NLS-1$
                    }
                } else if (att == TextAttribute.SWAP_COLORS) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.SWAP_COLORS_ON)) {
                        buf.append("SWAP_COLORS_ON"); //$NON-NLS-1$
                    } else {
                        Boolean v = (Boolean)value;
                        buf.append(String.format("SWAP_COLORS:%s", v.toString())); //$NON-NLS-1$
                    }
                } else if (att == TextAttribute.TRACKING) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.TRACKING_LOOSE)) {
                        buf.append("TRACKING_LOOSE"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.TRACKING_TIGHT)) {
                        buf.append("TRACKING_TIGHT"); //$NON-NLS-1$
                    } else {
                        Float f = (Float)value;
                        buf.append(String.format("TRACKING:%f", f)); //$NON-NLS-1$
                    }
                } else if (att == TextAttribute.UNDERLINE) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.UNDERLINE_LOW_DASHED)) {
                        buf.append("UNDERLINE_LOW_DASHED"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.UNDERLINE_LOW_DOTTED)) {
                        buf.append("UNDERLINE_LOW_DOTTED"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.UNDERLINE_LOW_GRAY)) {
                        buf.append("UNDERLINE_LOW_GRAY"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.UNDERLINE_LOW_ONE_PIXEL)) {
                        buf.append("UNDERLINE_LOW_ONE_PIXEL"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.UNDERLINE_LOW_TWO_PIXEL)) {
                        buf.append("UNDERLINE_LOW_TWO_PIXEL"); //$NON-NLS-1$
                    } else if (value.equals(TextAttribute.UNDERLINE_ON)) {
                        buf.append("UNDERLINE_ON"); //$NON-NLS-1$
                    } else {
                        Integer i = (Integer)value;
                        buf.append(String.format("UNDERLING:%d", i)); //$NON-NLS-1$
                    }
                } else if (att == TextAttribute.NUMERIC_SHAPING) {
                    NumericShaper ns = (NumericShaper)attributes.get(att);
                    if (ns == null) {
                        continue;
                    }
                    buf.append(ns.toString());
                } else if (att == TextAttribute.CHAR_REPLACEMENT) {
                    GraphicAttribute ga = (GraphicAttribute)attributes.get(att);
                    if (ga == null) {
                        continue;
                    }
                    buf.append(ga.toString());
                } else if (att == TextAttribute.FONT) {
                    Font f = (Font)attributes.get(att);
                    if (f == null) {
                        continue;
                    }
                    buf.append(f.toString());
                }
                buf.append(", "); //$NON-NLS-1$
            }
            buf.append("}"); //$NON-NLS-1$
        }
        if (cellWalls == null || cellWalls.isEmpty()) {
            buf.append(", {}"); //$NON-NLS-1$
        } else {
            buf.append(", {"); //$NON-NLS-1$
            for (CellWalls wall : cellWalls) {
                buf.append(wall.name());
                buf.append(", "); //$NON-NLS-1$
            }
            buf.append("}"); //$NON-NLS-1$
            
        }
        return buf.toString();
    }
    
    /**
     * Create a new simple AWT cell
     * 
     * @param glyph the character sequence
     * @param background the background color
     * @param foreground the foreground color
     * @param dirty the dirty status
     */
    public AwtCell(String glyph, Color background, Color foreground, 
                   boolean dirty) {
        super();
        setCell(glyph, background, foreground);
        this.dirty = dirty;
    }
    
    /**
     * Create a new AWT cell based upon an existing cell.
     * 
     * @param source source cell
     */
    public AwtCell(AwtCell source) {
        super();
        setCell(source);
    }
    /**
     * Add a character sequence to a cell
     * @param glyph a character sequence
     */
    public void addGlyph(int glyph) {
        this.sequence += String.copyValueOf(Character.toChars(glyph));
        dirty = true;
    }
    /**
     * Add a character sequence to a cell
     * @param glyph a character sequence
     */
    public void addGlyph(String glyph) {
        this.sequence += glyph;
        dirty = true;
    }

    /**
     * Clear the text attributes, but not the colors.
     * 
     * <p>While generally we treat the foreground and background color as
     * simply attributes, this function avoids clearing them. This allows
     * us to hope that the character has a better chance of remaining 
     * visible.</p>
     */
    public void clearTextAttributes() {
        Color background = (Color)attributes.get(TextAttribute.BACKGROUND);
        Color foreground = (Color)attributes.get(TextAttribute.FOREGROUND);
        Font f = (Font)attributes.get(TextAttribute.FONT);
        attributes.clear();
        attributes.put(TextAttribute.BACKGROUND, background);
        attributes.put(TextAttribute.FOREGROUND, foreground);
        attributes.put(TextAttribute.FONT, f);
        dirty = true;
    }
    /**
     * Clear the cell walls.
     */
    public void clearCellWalls() {
        this.cellWalls = EnumSet.noneOf(CellWalls.class);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public AwtCell clone() {
        AwtCell ret;
        try {
            ret = (AwtCell)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("unexpected clone failure", e); //$NON-NLS-1$
        }
        ret.cellWalls = EnumSet.copyOf(this.cellWalls);
        ret.attributes = new HashMap<TextAttribute, Object>(this.attributes);
        return ret;
    }
    /**
     * Get an attributed string of the character sequence.
     * @return attributed string
     */
    public AttributedString getAttributedString() {
        AttributedString ret = new AttributedString(sequence, attributes);
        return ret;
    }
    /**
     * Get the attributes
     * @return attributes
     */
    public Map<TextAttribute, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    /**
     * Get the background color
     * @return background color
     */
    public Color getBackgroundColor() {
        return (Color)attributes.get(TextAttribute.BACKGROUND);
    }

    /**
     * Get the AWT cell.
     * @return the AWT cell.
     */
    public AwtCell getCell() {
        return this;
    }
    /**
     * Get the cell walls
     * @return the cell walls
     */
    public Set<CellWalls> getCellWalls() {
        return Collections.unmodifiableSet(cellWalls);
    }
    /**
     * Get the font.
     * @return the font
     */
    public Font getFont() {
        return (Font)attributes.get(TextAttribute.FONT);
    }
    /**
     * Get the foreground color
     * @return foreground color
     */
    public Color getForegroundColor() {
        return (Color)attributes.get(TextAttribute.FOREGROUND);
    }
 
    /**
     * Get the character sequence.
     * @return the character sequence
     */
    public String getSequence() {
        return sequence;
    }
    /**
     * Is the cell dirty?
     * @return dirty status
     */
    public boolean isDirty() {
        return dirty;
    }
    /**
     * Unset a text attribute
     * @param key text attribute key
     * @return previous value
     */
    public Object unsetAttribute(TextAttribute key) {
        if (attributes.containsKey(key)) {
            dirty = true;
            return attributes.remove(key);
        }
        return null;
    }
    /**
     * Set a text attribute
     * 
     * @param key the key
     * @param value the value
     * @return the previous value
     */
    public Object setTextAttribute(TextAttribute key, 
                               Object value) {
        dirty = true;
        if (value == null) {
            return attributes.remove(key);
        }
        return attributes.put(key, value);
    }
    /**
     * Set the text attributes to an existing map
     * @param attributes new text attributes
     */
    public void setTextAttributes(Map<TextAttribute, Object> attributes) {
        if (attributes != null && !attributes.equals(this.attributes)) {
            clearTextAttributes();
            dirty = true;
            this.attributes.putAll(attributes);
        }
    }
    /**
     * Set the background color
     * @param background new background color
     */
    public void setBackgroundColor(Color background) {
        if (background != null) {
            if (!background.equals(attributes.get(TextAttribute.BACKGROUND))) {
                attributes.put(TextAttribute.BACKGROUND, background);
                dirty = true;
            }
        }
    }
    /**
     * Set the cell to an existing cell
     * @param cell new cell
     */
    public void setCell(AwtCell cell) {
        if (cell == null) {
            setCell("\u0000", Color.BLACK, Color.WHITE); //$NON-NLS-1$
            clearTextAttributes();
            clearCellWalls();
        } else {
            this.sequence = cell.sequence;
            setTextAttributes(cell.attributes);
            setCellWalls(cell.cellWalls);
        }
        dirty = true;
    }
    
    /**
     * Set some common parts of a cell.
     * 
     * @param glyph the character sequence
     * @param background the background
     * @param foreground the foreground
     */
    public void setCell(int glyph, Color background, Color foreground) {
        setSequence(glyph);
        setForegroundColor(foreground);
        setBackgroundColor(background);
    }
    /**
     * Set the core components of a cell.
     * @param glyph the character sequence
     * @param attributes the text attributes
     */
    public void setCell(int glyph, Map<TextAttribute, Object> attributes) {
        setSequence(glyph);
        if (attributes != null) {
            setTextAttributes(attributes);
        }
    }
    /**
     * Set the common parts of a cell
     * @param glyph a character sequence
     * @param background the background
     * @param foreground the foreground
     */
    public void setCell(String glyph, Color background, Color foreground) {
        setSequence(glyph);
        setForegroundColor(foreground);
        setBackgroundColor(background);
    }
    
    /**
     * Set the cell using the better common form
     * 
     * @param glyph the character sequence
     * @param attributes the text attributes
     */
    public void setCell(String glyph, Map<TextAttribute, Object> attributes) {
        this.sequence = glyph;
        setTextAttributes(attributes);
    }
    
    /**
     * Set all of a cell.
     * 
     * @param sequence the character sequence
     * @param attributes the text attributes
     * @param walls the cell walls
     */
    public void setCell(String sequence, Map<TextAttribute, Object> attributes, 
                        EnumSet<CellWalls> walls) {
        setSequence(sequence);
        setTextAttributes(attributes);
        setCellWalls(walls);
    }
    /**
     * Set a single cell wall
     * @param walls wall to set
     */
    public void setCellWalls(CellWalls walls) {
        if (walls != null && !walls.equals(this.cellWalls)) {
            this.cellWalls = EnumSet.of(walls);
            dirty = true;
        }
    }
    /**
     * Set the cell walls
     * @param walls complete set cell walls
     */
    public void setCellWalls(Set<CellWalls> walls) {
        if (walls != null && !walls.equals(this.cellWalls)) {
            cellWalls = EnumSet.copyOf(walls);
            dirty = true;
        }
    }
    /**
     * Set the dirty state of the cell
     * @param dirty dirty state
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
    /**
     * Set the font to use.
     * @param font the font to use
     */
    public void setFont(Font font) {
        if (font != null) {
            attributes.put(TextAttribute.FONT, font);
            dirty = true;
        }
    }
    /**
     * Set the foreground color
     * @param foreground color
     */
    public void setForegroundColor(Color foreground) {
        if (foreground != null) {
            attributes.put(TextAttribute.FOREGROUND, foreground);
            dirty = true;
        }
    }
    /**
     * Set the sequence
     * @param sequence new sequence
     */
    public void setSequence(int sequence) {
        this.sequence = String.copyValueOf(Character.toChars(sequence));
        dirty = true;
    }
    /**
     * Set the sequence
     * @param sequence new sequence
     */
    public void setSequence(String sequence) {
        if (sequence == null || sequence.length() == 0) {
            sequence = "\u0000"; //$NON-NLS-1$
        }
        if (this.sequence == null) {
            this.sequence = sequence;
        } else if (!this.sequence.equals(sequence)) {
            this.sequence = sequence;
            dirty = true;
        }
    }

}
