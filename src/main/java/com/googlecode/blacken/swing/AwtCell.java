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

import com.googlecode.blacken.grid.Copyable;
import com.googlecode.blacken.grid.ResetGridCell;
import com.googlecode.blacken.terminal.CellWalls;

public class AwtCell implements Copyable {
    public class ResetCell implements ResetGridCell<AwtCell> {

        @Override
        public void reset(AwtCell cell) {
            cell.setDirty(true);
        }

    }
    private String sequence;
    private boolean dirty;
    private Map<TextAttribute, Object> attributes = 
        new HashMap<TextAttribute, Object>();
    private EnumSet<CellWalls> cellWalls = EnumSet.noneOf(CellWalls.class);
    
    public AwtCell() {
        super();
        setCell("\u0000", Color.BLACK, Color.WHITE);
    }
    
    @Override 
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (sequence == null) {
            buf.append("null");
        } else {
            buf.append('"');
            for (char c : sequence.toCharArray()) {
                if (c < ' ' || c > 127) {
                    buf.append(String.format("\\u04x", c));
                } else {
                    buf.append(c);
                }
            }
            buf.append('"');
        }
        Color clr = getForegroundColor();
        if (clr == null) {
            buf.append(", null");
        } else {
            buf.append(String.format(", 0x%08x", clr.getRGB()));
        }
        clr = getBackgroundColor();
        if (clr == null) {
            buf.append(", null");
        } else {
            buf.append(String.format(", 0x%08x", clr.getRGB()));
        }
        if (dirty) {
            buf.append(", DIRTY");
        } else {
            buf.append(", clean");
        }
        if (attributes == null || attributes.isEmpty()) {
            buf.append(", {}");
        } else {
            buf.append(", {");
            for (TextAttribute att : attributes.keySet()) {
                if (att == TextAttribute.WEIGHT){
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.WEIGHT_BOLD)) {
                        buf.append("WEIGHT_BOLD");
                    } else if (value.equals(TextAttribute.WEIGHT_DEMIBOLD)) {
                        buf.append("WEIGHT_DEMIBOLD");
                    } else if (value.equals(TextAttribute.WEIGHT_DEMILIGHT)) {
                        buf.append("WEIGHT_DEMILIGHT");
                    } else if (value.equals(TextAttribute.WEIGHT_EXTRA_LIGHT)) {
                        buf.append("WEIGHT_EXTRA_LIGHT");
                    } else if (value.equals(TextAttribute.WEIGHT_EXTRABOLD)) {
                        buf.append("WEIGHT_EXTRABOLD");
                    } else if (value.equals(TextAttribute.WEIGHT_HEAVY)) {
                        buf.append("WEIGHT_HEAVY");
                    } else if (value.equals(TextAttribute.WEIGHT_LIGHT)) {
                        buf.append("WEIGHT_LIGHT");
                    } else if (value.equals(TextAttribute.WEIGHT_MEDIUM)) {
                        buf.append("WEIGHT_MEDIUM");
                    } else if (value.equals(TextAttribute.WEIGHT_REGULAR)) {
                        buf.append("WEIGHT_REGULAR");
                    } else if (value.equals(TextAttribute.WEIGHT_SEMIBOLD)) {
                        buf.append("WEIGHT_SEMIBOLD");
                    } else if (value.equals(TextAttribute.WEIGHT_ULTRABOLD)) {
                        buf.append("WEIGHT_ULTRABOLD");
                    } else {
                        float f = (Float)value;
                        buf.append(String.format("WEIGHT:%f", f));
                    }
                } else if (att == TextAttribute.WIDTH) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.WIDTH_CONDENSED)) {
                        buf.append("WIDTH_CONDENSED");
                    } else if (value.equals(TextAttribute.WIDTH_EXTENDED)) {
                        buf.append("WIDTH_EXTENDED");
                    } else if (value.equals(TextAttribute.WIDTH_REGULAR)) {
                        buf.append("WIDTH_REGULAR");
                    } else if (value.equals(TextAttribute.WIDTH_SEMI_CONDENSED)) {
                        buf.append("WIDTH_SEMI_CONDENSED");
                    } else if (value.equals(TextAttribute.WIDTH_SEMI_EXTENDED)) {
                        buf.append("WIDTH_SEMI_EXTENDED");
                    }
                } else if (att == TextAttribute.KERNING) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.KERNING_ON)) {
                        buf.append("KERNING_ON");
                    } else {
                        Integer v = (Integer)value;
                        buf.append(String.format("KERNING:%d", v));
                    }
                } else if (att == TextAttribute.LIGATURES) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.LIGATURES_ON)) {
                        buf.append("LIGATURES_ON");
                    } else {
                        Integer v = (Integer)value;
                        buf.append(String.format("LIGATURES:%d", v));
                    }
                } else if (att == TextAttribute.POSTURE) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.POSTURE_OBLIQUE)) {
                        buf.append("POSTURE_OBLIQUE");
                    } else if (value.equals(TextAttribute.POSTURE_REGULAR)) {
                        buf.append("POSTURE_REGULAR");
                    } else {
                        Float f = (Float)value;
                        buf.append(String.format("POSTURE:%f", f));
                    }
                } else if (att == TextAttribute.STRIKETHROUGH) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.STRIKETHROUGH_ON)) {
                        buf.append("STRIKETHROUGH_ON");
                    } else {
                        Integer v = (Integer)value;
                        buf.append(String.format("STRIKETHROUGH:%d", v));
                    }
                } else if (att == TextAttribute.SUPERSCRIPT) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.SUPERSCRIPT_SUB)) {
                        buf.append("SUPERSCRIPT_SUB");
                    } else if (value.equals(TextAttribute.SUPERSCRIPT_SUPER)) {
                        buf.append("SUPERSCRIPT_SUPER");
                    } else {
                        Integer v = (Integer)value;
                        buf.append(String.format("SUPERSCRIPT:%d", v));
                    }
                } else if (att == TextAttribute.SWAP_COLORS) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.SWAP_COLORS_ON)) {
                        buf.append("SWAP_COLORS_ON");
                    } else {
                        Boolean v = (Boolean)value;
                        buf.append(String.format("SWAP_COLORS:%s", v.toString()));
                    }
                } else if (att == TextAttribute.TRACKING) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.TRACKING_LOOSE)) {
                        buf.append("TRACKING_LOOSE");
                    } else if (value.equals(TextAttribute.TRACKING_TIGHT)) {
                        buf.append("TRACKING_TIGHT");
                    } else {
                        Float f = (Float)value;
                        buf.append(String.format("TRACKING:%f", f));
                    }
                } else if (att == TextAttribute.UNDERLINE) {
                    Object value = attributes.get(att);
                    if (value == null) {
                        continue;
                    } else if (value.equals(TextAttribute.UNDERLINE_LOW_DASHED)) {
                        buf.append("UNDERLINE_LOW_DASHED");
                    } else if (value.equals(TextAttribute.UNDERLINE_LOW_DOTTED)) {
                        buf.append("UNDERLINE_LOW_DOTTED");
                    } else if (value.equals(TextAttribute.UNDERLINE_LOW_GRAY)) {
                        buf.append("UNDERLINE_LOW_GRAY");
                    } else if (value.equals(TextAttribute.UNDERLINE_LOW_ONE_PIXEL)) {
                        buf.append("UNDERLINE_LOW_ONE_PIXEL");
                    } else if (value.equals(TextAttribute.UNDERLINE_LOW_TWO_PIXEL)) {
                        buf.append("UNDERLINE_LOW_TWO_PIXEL");
                    } else if (value.equals(TextAttribute.UNDERLINE_ON)) {
                        buf.append("UNDERLINE_ON");
                    } else {
                        Integer i = (Integer)value;
                        buf.append(String.format("UNDERLING:%d", i));
                    }
                } else if (att == TextAttribute.NUMERIC_SHAPING) {
                    NumericShaper ns = (NumericShaper)attributes.get(att);
                    if (ns == null) {
                        continue;
                    } else {
                        buf.append(ns.toString());
                    }
                } else if (att == TextAttribute.CHAR_REPLACEMENT) {
                    GraphicAttribute ga = (GraphicAttribute)attributes.get(att);
                    if (ga == null) {
                        continue;
                    } else {
                        buf.append(ga.toString());
                    }
                } else if (att == TextAttribute.FONT) {
                    Font f = (Font)attributes.get(att);
                    if (f == null) {
                        continue;
                    } else {
                        buf.append(f.toString());
                    }
                }
                buf.append(", ");
            }
            buf.append("}");
        }
        if (cellWalls == null || cellWalls.isEmpty()) {
            buf.append(", {}");
        } else {
            buf.append(", {");
            for (CellWalls wall : cellWalls) {
                buf.append(wall.name());
                buf.append(", ");
            }
            buf.append("}");
            
        }
        return buf.toString();
    }
    
    public AwtCell(String glyph, Color background, Color foreground, 
                   boolean dirty) {
        super();
        setCell(glyph, background, foreground);
        this.dirty = dirty;
    }
    public AwtCell(AwtCell source) {
        super();
        setCell(source);
    }
    public void addGlyph(int glyph) {
        this.sequence += String.copyValueOf(Character.toChars(glyph));
        dirty = true;
    }
    public void addGlyph(String glyph) {
        this.sequence += glyph;
        dirty = true;
    }

    /**
     * While generally we treat the foreground and background color as
     * simply attributes, this function avoids clearing them. This allows
     * us to hope that the character has a better chance of remaining visible.
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
    public void clearCellWalls() {
        this.cellWalls = EnumSet.noneOf(CellWalls.class);
    }

    @Override
    public AwtCell clone() {
        AwtCell ret;
        try {
            ret = (AwtCell)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("unexpected clone failure", e);
        }
        ret.cellWalls = EnumSet.copyOf(this.cellWalls);
        ret.attributes = new HashMap<TextAttribute, Object>(this.attributes);
        return ret;
    }
    public AttributedString getAttributedString() {
        AttributedString ret = new AttributedString(sequence, attributes);
        return ret;
    }
    public Map<TextAttribute, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Color getBackgroundColor() {
        return (Color)attributes.get(TextAttribute.BACKGROUND);
    }


    public AwtCell getCell() {
        return this;
    }
    public Set<CellWalls> getCellWalls() {
        return Collections.unmodifiableSet(cellWalls);
    }
    public Font getFont() {
        return (Font)attributes.get(TextAttribute.FONT);
    }
    public Color getForegroundColor() {
        return (Color)attributes.get(TextAttribute.FOREGROUND);
    }
 
    public String getGlyph() {
        return sequence;
    }
    public boolean isDirty() {
        return dirty;
    }
    public Object unsetAttribute(TextAttribute key) {
        if (attributes.containsKey(key)) {
            dirty = true;
            return attributes.remove(key);
        }
        return null;
    }
    public Object setTextAttribute(TextAttribute key, 
                               Object value) {
        dirty = true;
        if (value == null) {
            return attributes.remove(key);
        } else {
            return attributes.put(key, value);
        }
    }
    public void setTextAttributes(Map<TextAttribute, Object> attributes) {
        if (attributes != null && !attributes.equals(this.attributes)) {
            clearTextAttributes();
            dirty = true;
            attributes.putAll(attributes);
        }
    }
    public void setBackgroundColor(Color background) {
        if (background != null) {
            if (!background.equals(attributes.get(TextAttribute.BACKGROUND))) {
                attributes.put(TextAttribute.BACKGROUND, background);
                dirty = true;
            }
        }
    }
    public void setCell(AwtCell cell) {
        if (cell == null) {
            setCell("\u0000", Color.BLACK, Color.WHITE);
            clearTextAttributes();
            clearCellWalls();
        } else {
            this.sequence = cell.sequence;
            setTextAttributes(cell.attributes);
            setCellWalls(cell.cellWalls);
        }
        dirty = true;
    }
    
    public void setCell(int glyph, Color background, Color foreground) {
        setSequence(glyph);
        setForegroundColor(foreground);
        setBackgroundColor(background);
    }
    public void setCell(int glyph, Map<TextAttribute, Object> attributes) {
        setSequence(glyph);
        if (attributes != null) {
            setTextAttributes(attributes);
        }
    }
    public void setCell(String glyph, Color background, Color foreground) {
        setSequence(glyph);
        setForegroundColor(foreground);
        setBackgroundColor(background);
    }
    
    public void setCell(String glyph, Map<TextAttribute, Object> attributes) {
        this.sequence = glyph;
        setTextAttributes(attributes);
    }
    
    public void setCell(String glyph, Map<TextAttribute, Object> attributes, 
                        EnumSet<CellWalls> walls) {
        setSequence(glyph);
        setTextAttributes(attributes);
        setCellWalls(walls);
    }
    public void setCellWalls(CellWalls walls) {
        if (walls != null && !walls.equals(this.cellWalls)) {
            this.cellWalls = EnumSet.of(walls);
            dirty = true;
        }
    }
    public void setCellWalls(Set<CellWalls> walls) {
        if (walls != null && !walls.equals(this.cellWalls)) {
            cellWalls = EnumSet.copyOf(walls);
            dirty = true;
        }
    }
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
    public void setFont(Font font) {
        if (font != null) {
            attributes.put(TextAttribute.FONT, font);
            dirty = true;
        }
    }
    public void setForegroundColor(Color foreground) {
        if (foreground != null) {
            attributes.put(TextAttribute.FOREGROUND, foreground);
            dirty = true;
        }
    }
    public void setSequence(int sequence) {
        this.sequence = String.copyValueOf(Character.toChars(sequence));
        dirty = true;
    }
    public void setSequence(String sequence) {
        if (sequence == null || sequence.length() == 0) {
            sequence = "\u0000";
        }
        if (this.sequence == null) {
            this.sequence = sequence;
        } else if (!this.sequence.equals(sequence)) {
            this.sequence = sequence;
            dirty = true;
        }
    }

    @Override
    public AwtCell copy() {
        return this.clone();
    }

}
