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
public abstract class AbstractCopyable implements Copyable {
    /**
     * create a new copy
     * 
     * <p>We require clones to be supported. A derived class disabling clones
     * is a violation of the API. As such, we can turn the 
     * CloneNotSupportedException in to a RuntimeException.</p>
     * @return new copy
     */
    @Override
    public AbstractCopyable copy() {
        return (AbstractCopyable) clone();
    }

    /**
     * @see #copy()
     * @param <T> a type of Copyable
     * @param source source object
     * @return new copy
     */
    @SuppressWarnings("unchecked")
    public static <T extends Copyable> T copy(T source) {
        if (source == null) return null;
        return (T) source.copy();
    }

    /**
     * Clone the object.
     * 
     * <p>Note that it is a violation of our API to throw 
     * <code>CloneNotSupportedException</code>s. Classes that derive from 
     * this one <b>must</b> support clone.</p>
     */
    @Override
    public AbstractCopyable clone() {
        try {
            return (AbstractCopyable)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("unexpected clone failure", e);
        }
    }
}
