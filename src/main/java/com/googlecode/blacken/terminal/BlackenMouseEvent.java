package com.googlecode.blacken.terminal;

import java.util.EnumSet;

public class BlackenMouseEvent {
    private BlackenEventType type;
    private int y = -1;
    private int x = -1;
    private int clickCount = 0;
    private EnumSet<BlackenModifier> modifiers;
    private int button;
    
    public boolean equals(BlackenMouseEvent e) {
        if (e == null) return false;
        if (!this.type.equals(e.type)) return false;
        if (this.y != e.y) return false;
        if (this.x != e.x) return false;
        if (this.clickCount != e.clickCount) return false;
        if (!this.modifiers.equals(e.modifiers)) return false;
        if (this.button != e.button) return false;
        return true;
        
    }
    
    @Override
    public String toString() {
        return String.format("Mouse: %s %d, %d, %s%d x%d", type.name(), y, x,  
                      BlackenModifier.getModifierString(modifiers), button, 
                      clickCount);
    }
    
    public BlackenEventType getType() {
        return type;
    }

    public void setType(BlackenEventType type) {
        this.type = type;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
    
    public BlackenMouseEvent(BlackenEventType type) {
        this.type = type;
    }
    
    public int[] getPosition() {
        int[] ret = {y, x};
        return ret;
    }
    
    public void setPosition(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public void setModifiers(EnumSet<BlackenModifier> modifiers) {
        if (modifiers == null) {
            this.modifiers = EnumSet.noneOf(BlackenModifier.class);
        } else {
            this.modifiers = modifiers;
        }
    }

    public EnumSet<BlackenModifier> getModifiers() {
        return modifiers;
    }

    public void setButton(int button) {
        this.button = button;
    }

}
