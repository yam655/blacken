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
package com.googlecode.blacken.terminal;

/**
 * The available event types.
 * 
 * @author yam655
 */
public enum BlackenEventType {
    /**
     * The mouse was clicked.
     */
    MOUSE_CLICKED, 
    /**
     * The mouse entered the window.
     */
    MOUSE_ENTERED, 
    /**
     * The mouse exited the window.
     */
    MOUSE_EXITED, 
    /**
     * The mouse was pressed.
     */
    MOUSE_PRESSED, 
    /**
     * The mouse was dragged
     */
    MOUSE_DRAGGED, 
    /**
     * The mouse was moved.
     */
    MOUSE_MOVED, 
    /**
     * The window was activated
     */
    WINDOW_ACTIVATED, 
    /**
     * The window was closed.
     */
    WINDOW_CLOSED, 
    /**
     * The window is closing.
     */
    WINDOW_CLOSING, 
    /**
     * The window is deactivated
     */
    WINDOW_DEACTIVATED, 
    /**
     * The window is deiconified.
     */
    WINDOW_DEICONIFIED, 
    /**
     * The window is iconified.
     */
    WINDOW_ICONIFIED, 
    /**
     * The window is opened.
     */
    WINDOW_OPENED, 
    /**
     * The window gained focus.
     */
    WINDOW_GAINED_FOCUS,
    /**
     * The window lost focus
     */
    WINDOW_LOST_FOCUS, 
    /**
     * Mouse wheel events
     */
    MOUSE_WHEEL, 
    /**
     * The mouse was released.
     */
    MOUSE_RELEASED,
}
