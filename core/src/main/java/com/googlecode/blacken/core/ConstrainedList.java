/* blacken - a library for Roguelike games
 * Copyright © 2012 Steven Black <yam655@gmail.com>
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

package com.googlecode.blacken.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 *
 * @author Steven Black
 */
public class ConstrainedList<T> implements List<T>, RandomAccess, Serializable, Cloneable {
    private static final long serialVersionUID = -7296969321743670211L;
    private final List<T> store;
    private final Class type;
    private int maxSize = 0;
    private int start = 0;
    private int end = 0;

    private class ConstrainedListIterator implements ListIterator<T> {
        int start0 = start;
        int at = start0 - 1;

        ConstrainedListIterator(int i) {
            start0 = i;
        }

        ConstrainedListIterator() {
            // do nothing
        }

        @Override
        public boolean hasNext() {
            return at + start0 + 1 < end || (end == 0 && at + start0 + 1 < store.size());
        }

        @Override
        public T next() {
            return get(++at + start0);
        }

        @Override
        public boolean hasPrevious() {
            return (at - 1 >= start0) || at - 1 >= 0;
        }

        @Override
        public T previous() {
            return get(--at + start0);
        }

        @Override
        public int nextIndex() {
            return at + 1;
        }

        @Override
        public int previousIndex() {
            return at - 1;
        }

        @Override
        public void remove() {
            if (at > 0 && at > start0) {
                store.remove(at + start0);
            }
        }

        @Override
        public void set(T e) {
            store.set(at + start0, e);
        }

        @Override
        public void add(T e) {
            if (maxSize > 0 && store.size() + 1 >= maxSize) {
                throw new UnsupportedOperationException("Too many items");
            }
            store.add(at + start0, e);
        }
    
    }

    public ConstrainedList(Class<T> clazz, int maxSize) {
        this.maxSize = maxSize;
        List<Object> t = new ArrayList<>();
        this.store = Collections.checkedList(t, (Class)clazz);
        this.type = clazz;
    }

    public ConstrainedList(Class<T> clazz) {
        List<Object> t = new ArrayList<>();
        this.store = Collections.checkedList(t, (Class)clazz);
        this.type = clazz;
    }

    public ConstrainedList(int maxSize) {
        this.maxSize = maxSize;
        this.store = new ArrayList<>();
        this.type = Object.class;
    }

    public ConstrainedList() {
        this.store = new ArrayList<>();
        this.type = Object.class;
    }

    private ConstrainedList(ConstrainedList<T> orig, int start, int end) {
        this.store = orig.store;
        this.maxSize = orig.maxSize;
        this.start = orig.start + start;
        this.end = orig.start + end;
        this.type = orig.type;
    }

    @Override
    public int size() {
        return store.size() - start;
    }

    @Override
    public boolean isEmpty() {
        return rightList().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return rightList().contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new ConstrainedListIterator();
    }

    @Override
    public Object[] toArray() {
        return rightList().toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return rightList().toArray(ts);
    }

    @Override
    public boolean add(T e) {
        if (this.maxSize > 0 && store.size() + 1 >= maxSize) {
            throw new UnsupportedOperationException("Too many items");
        }
        return rightList().add(e);
    }

    @Override
    public boolean remove(Object o) {
        boolean ret = rightList().remove(o);
        end --;
        return ret;
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        return rightList().containsAll(clctn);
    }

    @Override
    public boolean addAll(Collection<? extends T> clctn) {
        if (this.maxSize > 0 && store.size() + clctn.size() >= maxSize) {
            throw new UnsupportedOperationException("Too many items");
        }
        return rightList().addAll(clctn);
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> clctn) {
        if (this.maxSize > 0 && store.size() + clctn.size() >= maxSize) {
            throw new UnsupportedOperationException("Too many items");
        }
        return store.addAll(i + start, clctn);
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        List<T> right = rightList();
        int s = right.size();
        boolean ret = right.removeAll(clctn);
        if (end != 0) {
            end -= right.size() - s;
        }
        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        List<T> right = rightList();
        int s = right.size();
        boolean ret = right.retainAll(clctn);
        if (end != 0) {
            end -= right.size() - s;
        }
        return ret;

    }

    private List<T> rightList() {
        if (end == 0 && start == 0) {
            return store;
        } else if (end != 0) {
            return store.subList(start, end);
        } else {
            return store.subList(start, size());
        }
    }
    @Override
    public void clear() {
        rightList().clear();
    }

    @Override
    public T get(int i) {
        checkBounds(i);
        return store.get(i + start);
    }

    private void checkBounds(int i) {
        if (i < start || (end != 0 && i >= end)) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + i + " bounds: " + start + ", " + end);
        }
    }

    @Override
    public T set(int i, T e) {
        checkBounds(i);
        return store.set(i + start, e);
    }

    @Override
    public void add(int i, T e) {
        if (this.maxSize > 0 && store.size() + 1 >= maxSize) {
            throw new UnsupportedOperationException("Too many items");
        }
        store.add(i + start, e);
    }

    @Override
    public T remove(int i) {
        return store.remove(i + start);
    }

    @Override
    public int indexOf(Object o) {
        return store.indexOf(o) - start;
    }

    @Override
    public int lastIndexOf(Object o) {
        return store.indexOf(o) - start;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ConstrainedListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return new ConstrainedListIterator(i);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new ConstrainedList(this, fromIndex, toIndex);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        if (this.store.size() > maxSize) {
            throw new UnsupportedOperationException("Already too large");
        }
        this.maxSize = maxSize;
    }

    public Class getContainedClass() {
        return type;
    }

}
