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
package com.googlecode.blacken.grid;

/**
 * A base class which always implements {@link #clone()} support.
 * 
 * <p>Classes which implement this <b>must</b> <i>always</i> support
 * {@link #clone()}. Throwing {@link CloneNotSupportedException} is
 * expressly disallowed.</p>
 * 
 * @author Steven Black
 *
 */
public interface Copyable extends Cloneable {
    /**
     * create a new copy
     * 
     * <p>We require clones to be supported. A derived class disabling clones
     * is a violation of the API. As such, we can turn the 
     * CloneNotSupportedException in to a RuntimeException.</p>
     * 
     * @return new copy
     */
    public Copyable copy();

    /**
     * Clone the object.
     * 
     * <p>Note that it is a violation of our API to throw 
     * <code>CloneNotSupportedException</code>s. Classes that derive from 
     * this one <b>must</b> support clone.</p>
     * 
     * @return new clone of object
     */
    public Copyable clone();
}
