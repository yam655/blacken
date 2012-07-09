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

package com.googlecode.blacken.dungeon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @param <T>
 * @author Steven Black
 */
public class SimpleContainer<T> implements Containerlike<T> {
    private ThingTypeCheck verifier;
    private int sizeLimit = -1;
    private List<T> storage;

    public SimpleContainer() {
        this.verifier = null;
        this.storage = new ArrayList<>();
    }
    public SimpleContainer(ThingTypeCheck<T> verifier) {
        this.verifier = verifier;
        this.storage = new ArrayList<>();
    }
    public SimpleContainer(ThingTypeCheck<T> verifier, int limit) {
        this.verifier = verifier;
        this.storage = new ArrayList<>();
        if (limit >= 0) {
            this.sizeLimit = limit;
        }
    }

    @Override
    public boolean canFit(T thing) {
        if (this.verifier != null) {
            if (!this.verifier.isSufficient(thing)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Collection<T> getSimilar(ThingTypeCheck<T>judge) {
        List<T> ret = new ArrayList<>();
        if (judge == null) {
            throw new NullPointerException("Need a judge.");
        }
        for (T thing : this) {
            if (judge.isSufficient(thing)) {
                ret.add(thing);
            }
        }
        return ret;
    }

    @Override
    public void setVerifier(ThingTypeCheck<T> verifier) {
        this.verifier = verifier;
    }

    @Override
    public ThingTypeCheck<T> getVerifier() {
        return verifier;
    }

    @Override
    public boolean hasSizeLimit() {
        return sizeLimit >= 0;
    }

    @Override
    public int getSizeLimit() {
        if (sizeLimit < 0) {
            return -1;
        }
        return sizeLimit;
    }

    @Override
    public int setSizeLimit(int limit) throws IllegalStateException {
        if (sizeLimit == limit) {
            return sizeLimit;
        }
        int oldLimit = sizeLimit;
        if (limit < 0) {
            sizeLimit = -1;
        } else if (limit < this.size()) {
            throw new IllegalStateException("Too late. It is already too big.");
        } else {
            sizeLimit = limit;
        }
        return oldLimit;
    }

    @Override
    public boolean add(Object o) {
        if (o == null) {
            throw new NullPointerException("parameter cannot be null");
        }
        // throw a class cast exception sooner rather than later.
        T thing = (T)o;
        if (this.canFit(thing)) {
            return this.storage.add(thing);
        } else {
            throw new IllegalStateException("Can't fit that there.");
        }
    }

    @Override
    public boolean offer(Object o) {
        if (o == null) {
            throw new NullPointerException("parameter cannot be null");
        }
        // throw a class cast exception sooner rather than later.
        T thing = (T)o;
        if (this.canFit(thing)) {
            this.storage.add(thing);
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return this.storage.size();
    }

    @Override
    public boolean isEmpty() {
        return this.storage.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.storage.contains(o);
    }

    @Override
    public Iterator iterator() {
        return storage.iterator();
    }

    @Override
    public Object[] toArray() {
        return storage.toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return storage.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return storage.remove(o);
    }

    @Override
    public boolean containsAll(Collection c) {
        return storage.containsAll(c);
    }

    @Override
    public boolean addAll(Collection c) {
        return storage.addAll(c);
    }

    @Override
    public boolean removeAll(Collection c) {
        return storage.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection c) {
        return storage.retainAll(c);
    }

    @Override
    public void clear() {
        storage.clear();
    }

}
