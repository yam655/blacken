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
package com.googlecode.blacken.terminal;

import java.util.EnumSet;

/**
 * The Blacken mouse event.
 * 
 * @author yam655
 */
public class BlackenMouseEvent {
    private BlackenEventType type;
    private int y = -1;
    private int x = -1;
    private int clickCount = 0;
    private EnumSet<BlackenModifier> modifiers;
    private int button;
    
    /**
     * Check for equality
     * @param e event to check against
     * @return true on success; false on failure
     */
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
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Mouse: %s %d, %d, %s%d x%d", type.name(), y, x,   //$NON-NLS-1$
                      BlackenModifier.getModifierString(modifiers), button, 
                      clickCount);
    }
    
    /**
     * Get the type of the event
     * @return the event type
     */
    public BlackenEventType getType() {
        return type;
    }

    /**
     * Set the event type
     * @param type the event type
     */
    public void setType(BlackenEventType type) {
        this.type = type;
    }

    /**
     * Get the Y coordinate
     * @return the coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Set the Y coordinate
     * @param y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the X coordinate
     * @return the coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Set the X coordinate
     * @param x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the click count
     * @return click count
     */
    public int getClickCount() {
        return clickCount;
    }

    /**
     * Set the click count
     * @param clickCount click count
     */
    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
    
    /**
     * Create a new mouse event
     * @param type event type
     */
    public BlackenMouseEvent(BlackenEventType type) {
        this.type = type;
    }
    
    /**
     * Get the position
     * @return {y, x}
     */
    public int[] getPosition() {
        int[] ret = {y, x};
        return ret;
    }
    
    /**
     * Set the position
     * @param y coordinate
     * @param x coordinate
     */
    public void setPosition(int y, int x) {
        this.y = y;
        this.x = x;
    }

    /**
     * Set the modifiers
     * @param modifiers set of modifiers
     */
    public void setModifiers(EnumSet<BlackenModifier> modifiers) {
        if (modifiers == null) {
            this.modifiers = EnumSet.noneOf(BlackenModifier.class);
        } else {
            this.modifiers = modifiers;
        }
    }

    /**
     * Get the modifiers
     * @return the modifiers
     */
    public EnumSet<BlackenModifier> getModifiers() {
        return modifiers;
    }

    /**
     * Set the button
     * @param button the button
     */
    public void setButton(int button) {
        this.button = button;
    }

}
