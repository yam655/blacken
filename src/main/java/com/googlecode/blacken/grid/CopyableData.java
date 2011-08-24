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
 * This is a Copyable-wrapper around <i>simple</i> data items.
 * 
 * <p>The target for the items wrapped are Java simple types. These are
 * explicitly copy-as-value types and not copy-as-reference types. (This
 * is because Integer and friends don't support {@link #clone()}.)</p>
 * 
 * @author yam655
 * @param <T> type of data
 */
public class CopyableData <T> extends AbstractCopyable {
    /**
     * Create a copy.
     * 
     * @param <U> type of data
     * @param source source data
     * @return new instance of data
     */
    @SuppressWarnings("unchecked")
    public static <U extends CopyableData<?>> U copy(U source) {
        return (U) source.clone();
    }
    /**
     * Direct access to data
     */
    protected T data;
    
    /**
     * Create a new CopyableData
     * 
     * @param data data to wrap
     */
    public CopyableData(T data) {
        this.data = data;
    }
    
    /**
     * Create a new CopyableData based existing instance
     * 
     * @param other data to copy
     */
    public CopyableData(CopyableData<T> other) {
        this.data = other.data;
    }
    
    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.AbstractCopyable#clone()
     */
    @SuppressWarnings("unchecked")
    @Override
    public CopyableData<T> clone() {
        return (CopyableData<T>)super.clone();
    }

    /*
     * (non-Javadoc)
     * @see com.googlecode.blacken.grid.AbstractCopyable#copy()
     */
    @Override
    @SuppressWarnings("cast")
    public CopyableData<T> copy() {
        return (CopyableData<T>) clone();
    }

    /**
     * Compare equality of ComparableData items
     * 
     * @param other other item to compare
     * @return true if equal, false if unequal
     */
    public boolean equals(CopyableData<T> other) {
        if (data == null && other.data == null) {
            return true;
        }
        return data.equals(other.data);
    }
    
    /**
     * Get the datum
     * @return datum
     */
    public T getData() {
        return data;
    }

    /**
     * Set the datum
     * 
     * @param data new datum
     */
    public void setData(T data) {
        this.data = data;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(CopyableData.class.getName());
        buf.append(":"); //$NON-NLS-1$
        if (data == null) {
            buf.append("null"); //$NON-NLS-1$
        } else {
            buf.append(data.toString());
        }
        return buf.toString();
    }
}
