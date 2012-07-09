/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.blacken.dungeon;

import java.util.Collection;
import java.util.Iterator;

/**
 * A generic container-like interface supporting fuzzy typing.
 *
 * @param <I> the type of item contained
 * @author Steven Black
 */
public interface Containerlike<I> extends Collection<I> {
    public boolean canFit(I thing);
    public Collection<I> getSimilar(ThingTypeCheck<I> judge);
    public void setVerifier(ThingTypeCheck<I> judge);
    public ThingTypeCheck<I> getVerifier();
    /**
     * Is a size limit in effect?
     * @return
     */
    public boolean hasSizeLimit();
    public int getSizeLimit();
    public int setSizeLimit(int limit) throws IllegalStateException;

    @Override
    public boolean add(I e);
    /**
     * Potentially add to the container.
     * @param e
     * @return true if added; false if not added
     */
    public boolean offer(I e);

    @Override
    public int size();
    @Override
    public boolean isEmpty();
    @Override
    public boolean contains(Object o);
    @Override
    public Iterator<I> iterator();
    @Override
    public Object[] toArray();
    @Override
    public <T> T[] toArray(T[] a);
    @Override
    public boolean remove(Object o);
    @Override
    public boolean containsAll(Collection<?> c);
    @Override
    public boolean addAll(Collection<? extends I> c);
    @Override
    public boolean removeAll(Collection<?> c);
    @Override
    public boolean retainAll(Collection<?> c);
    @Override
    public void clear();

}
