/**
 * 
 */
package com.googlecode.blacken.core;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A List which is also (sort of) a Map.
 * 
 * <p>The goal was to have a List that was also a Map. Unfortunately, the
 * interfaces are incompatible (<code><V> remove(<K>)</code> vs. 
 * <code> boolean remove(<E>)</code>). As such we have all of the same
 * functions as the Map interface... without actually <i>being</i> a Map.</p>
 * 
 * <p>Later, we decided we wanted the keys to more clearly be labels for the
 * index entries -- so that when indexed entries are changed the labels (and
 * aliases) are updated as well. It isn't quite a List as it doesn't support
 * arbitrary insertion locations.</p>
 * 
 * <p>Basically, the Map's key acts as a label for the entries in the List.
 * The List can have entries which are unlabeled, but a Map entry always has an
 * entry in the List. (Though it need not be 1:1.)</p>
 * 
 * @author Steven Black
 * @param <K> Key
 * @param <V> Value
 *
 */
public class ListMap<K, V> 
implements List<V>, Cloneable, Serializable {
    protected class ListMapEntry {
        private V value = null;
        private int index = -1;
        public ListMapEntry() {
        }
        public ListMapEntry(V value, int index) {
            setValue(value);
            setIndex(index);
        }
        public int getIndex() { return index; }
        public V getValue() { return value; }
        public void setIndex(int index) { this.index = index; }
        public void setValue(V value) { this.value = value; }
    }

    class ListMapIterator implements ListIterator<V> {
        private int index;
        private List<V> backing;
        ListMapIterator(List<V> backing) {
            this.backing = backing;
            this.index = 0;
        }
        ListMapIterator(List<V> backing, int index) {
            this.backing = backing;
            this.index = index;
        }
        
        @Override
        public void add(V e) {
            backing.add(index, e);
        }

        @Override
        public boolean hasNext() {
            return index < backing.size();
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public V next() {
            if (index >= backing.size()) {
                throw new NoSuchElementException();
            }
            return backing.get(index++);
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public V previous() {
            if (index <= 0) {
                throw new NoSuchElementException();
            }
            return backing.get(--index);
        }

        @Override
        public int previousIndex() {
            return index -1;
        }

        @Override
        public void remove() {
            if (index >= backing.size()) {
                return;
            }
            backing.remove(index);
        }

        @Override
        public void set(V e) {
            backing.set(index, e);
        }        
    }
    public class ListMapView implements List<V>{
        private List<V> backing;
        private int start;
        private int end;

        ListMapView(List<V> backing, int start, int end) {
            this.backing = backing;
            this.start = start;
            if (end == -1) {
                end = backing.size();
            }
            this.end = end;
        }

        @Override
        public void add(int index, V element) {
            if (index < 0 || index + start >= end) {
                throw new NoSuchElementException();
            }
            backing.add(index + start, element);
        }

        @Override
        public boolean add(V e) {
            backing.add(end++, e);
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends V> c) {
            boolean ret = backing.addAll(c);
            end += c.size();
            return ret;
        }

        @Override
        public boolean addAll(int index, Collection<? extends V> c) {
            boolean ret = backing.addAll(index, c);
            end += c.size();
            return ret;
        }

        @Override
        public void clear() {
            while (start < end) {
                backing.remove(start);
                end--;
            }
        }

        @Override
        public boolean contains(Object value) {
            for (int i = start; i < end; i++) {
                V entry = backing.get(i);
                if (entry == null) {
                    if (value == null) {
                        return true;
                    }
                } else if (entry.equals(value)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> values) {
            for (Object value : values) {
                if (!this.contains(value)) return false;
            }
            return true;
        }

        @Override
        public V get(int index) {
            if (index < 0 || index + start >= end) {
                throw new NoSuchElementException();
            }
            return backing.get(index);
        }

        @Override
        public int indexOf(Object o) {
            int i = backing.indexOf(o);
            if (i == -1) return -1;
            if (i < start || i >= end) {
                throw new NoSuchElementException();
            }
            return i - start;
        }

        @Override
        public boolean isEmpty() {
            return start == end;
        }

        @Override
        public Iterator<V> iterator() {
            return new ListMapIterator(this);
        }

        @Override
        public int lastIndexOf(Object o) {
            int i = backing.lastIndexOf(o);
            if (i == -1) return -1;
            if (i < start || i >= end) {
                throw new NoSuchElementException();
            }
            return i - start;
        }

        @Override
        public ListIterator<V> listIterator() {
            return new ListMapIterator(this);
        }

        @Override
        public ListIterator<V> listIterator(int index) {
            return new ListMapIterator(this, index);
        }

        @Override
        public V remove(int index) {
            if (index < 0 || index + start >= end) {
                throw new NoSuchElementException();
            }
            return backing.remove(index + start);
        }

        @Override
        public boolean remove(Object o) {
            int index = this.indexOf(o);
            if (index == -1) {
                return false;
            }
            backing.remove(index + start);
            return true;
        }

        @Override
        public boolean removeAll(Collection<?> values) {
            int origSize = this.size();
            for (Object value : values) {
                int index = valueList.indexOf(value);
                if (index < 0 || index + start >= end) {
                    continue;
                }
                backing.remove(index);
                end--;
            }
            return this.size() != origSize;
        }

        @Override
        public boolean retainAll(Collection<?> values) {
            if (values == null) return false;
            int oldSize = this.size();
            int index = 0;
            while (index < this.size()) {
                V value = this.get(index);
                if (values.contains(value)) {
                    index++;
                } else {
                    remove(index);
                }
            }
            return oldSize != this.size();
        }

        @Override
        public V set(int index, V element) {
            if (index < 0 || index + start >= end) {
                throw new NoSuchElementException();
            }
            return backing.set(index + start, element);
        }

        @Override
        public int size() {
            return end - start;
        }

        @Override
        public List<V> subList(int fromIndex, int toIndex) {
            if (toIndex == -1) toIndex = size();
            if (toIndex < 0 || toIndex > size()) {
                throw new NoSuchElementException();
            }
            if (fromIndex < 0 || fromIndex > size()) {
                throw new NoSuchElementException();
            }
            return new ListMapView(this, fromIndex, toIndex);
        }

        @Override
        public Object[] toArray() {
            Object[] ret = new Object[this.size()];
            for (int i = 0; i < size(); i++) {
                ret[i] = get(i);
            }
            return ret;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T[] toArray(T[] a) {
            T[] ret;
            if (a.length >= this.size()) {
                ret = a;
            } else {
                ret = (T[]) Array.newInstance(a.getClass(), this.size());
            }
            for (int i = 0; i < size(); i++) {
                ret[i] = (T) get(i);
            }
            return ret;
        }
    }
    
    /**
     * serialization ID
     */
    private static final long serialVersionUID = 4054776810296623320L;
    private List<ListMapEntry> valueList = null;
    private Map<K, ListMapEntry> keyMap = new HashMap<K, ListMapEntry>();
    public ListMap() {
        valueList = new ArrayList<ListMapEntry>();
        keyMap = new HashMap<K, ListMapEntry>();
    }
    public ListMap(int size) {
        valueList = new ArrayList<ListMapEntry>(size);
        keyMap = new HashMap<K, ListMapEntry>();
    }
    public ListMap(Collection<? extends V> c) {
        valueList = new ArrayList<ListMapEntry>(c.size());
        keyMap = new HashMap<K, ListMapEntry>();
        this.addAll(c);
    }
    public ListMap(ListMap<K, V> other) {
        valueList = new ArrayList<ListMapEntry>(other.size());
        keyMap = new HashMap<K, ListMapEntry>();
        addAll(other);
    }
    /**
     * Add a new <code>Entry</code> to the list.
     * 
     * @param entry entry to add
     * @return old index that the entry's key referred to
     */
    protected V add(Entry<K, V> entry) {
        ListMapEntry e = new ListMapEntry(entry.getValue(), this.size()-1);
        this.valueList.add(e);
        ListMapEntry old = keyMap.put(entry.getKey(), e);
        if (old == null) return null;
        return old.getValue();
    }

    @Override
    public void add(int index, V element) {
        ListMapEntry e = new ListMapEntry(element, index);
        if (index == this.valueList.size()) {
            this.valueList.add(e);
        } else {
            this.valueList.add(index, e);
            this.reindex(index+1);
        }
    }
    /**
     * Add a new <code>value</code> to the end of the list, naming it 
     * <code>key</code>.
     * 
     * <p>This always adds the entry. If the <code>key</code> already exists,
     * the value located at the old index is orphaned, the new value is added
     * to the end of the list, and the index is updated. This is by design as 
     * it provides a way to separate an aliased name from the shared index 
     * entry.</p>
     * 
     * @param key key to add
     * @param value value to add
     * @return previous value at <code>key</code> location, if any
     */
    public boolean add(K key, V value) {
        ListMapEntry e = new ListMapEntry(value, this.size()-1);
        this.valueList.add(e);
        keyMap.put(key, e);
        return true;
    }
    public boolean add(K[] keys, V value) {
        if (keys.length == 0) return false;
        add(keys[0], value);
        for (int i = 1; i < keys.length; i++) {
            putKey(keys[i], keys[0]);
        }
        return true;
    }
    @Override
    public boolean add(V value) {
        ListMapEntry e = new ListMapEntry(value, valueList.size());
        return this.valueList.add(e);
    }
    public boolean addAll(ListMap<K, V> old) {
        if (old.size() == 0) return false;
        int add = this.size();
        for (V value : old) {
            add(value);
        }
        for (Entry<K, ListMapEntry> entry : old.keyMap.entrySet()) {
            putKey(entry.getKey(), entry.getValue().getIndex() + add);
        }
        return true;
    }
    @Override
    public boolean addAll(Collection<? extends V> values) {
        boolean ret = false;
        if (values == null) return ret;
        for (V value : values) {
            if (add(value)) {
                ret = true;
            }
        }
        return ret;
    }
    @Override
    public boolean addAll(int index, Collection<? extends V> values) {
        boolean ret = false;
        if (values == null) return ret;
        if (!values.isEmpty()) ret = true;
        for (V value : values) {
            add(index++, value);
        }
        reindex(index);
        return ret;
    }

    public boolean addAll(int index, V[] items) {
        if (items.length == 0) {
            return false;
        }
        for (V item : items) {
            add(index++, item);
        }
        reindex(index);
        return true;
    }
    public void addAll(K[] keys, V[] items) {
        int commonLength = keys.length < items.length ? keys.length : items.length;
        for (int i=0; i < commonLength; i++) {
            add(keys[i], items[i]);
        }
        return;
    }
    public void addAll(List<K> keys, List<V> items) {
        int commonLength = keys.size() < items.size() ? keys.size() : items.size();
        for (int i=0; i < commonLength; i++) {
            add(keys.get(i), items.get(i));
        }
        return;
    }
    
    public void addAll(ListMap<K, V> old, K[] newOrder) {
        for (K key : newOrder) {
            this.add(key, old.get(key));
        }
    }
    public void addAll(Set<Entry<K, V>> list) {
        for (Entry<K, V> entry : list) {
            add(entry);
        }
        return;
    }

    public boolean addAll(V[] items) {
        if (items.length == 0) {
            return false;
        }
        for (V item : items) {
            add(item);
        }
        return true;
    }

    /**
     * Clear both the index as well as the name map.
     */
    @Override
    public void clear() {
        this.valueList.clear();
        clearKeys();
    }

    /**
     * Clear the key map.
     */
    public void clearKeys() {
        keyMap.clear();
    }

    /**
     * Warning: This may be slow.
     */
    @Override
    public boolean contains(Object value) {
        for (ListMapEntry entry : this.valueList) {
            if (entry == null) {
                continue;
            } else if (entry.getValue() == null) {
                if (value == null) {
                    return true;
                }
            } else if (entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Warning: This is probably super slow.
     */
    @Override
    public boolean containsAll(Collection<?> values) {
        for (Object value : values) {
            if (!this.contains(value)) return false;
        }
        return true;
    }

    /**
     * Do we have a specific key?
     * 
     * @param key key to check
     * @return true if we have the key, false if not
     */
    public boolean containsKey(K key) {
        return keyMap.containsKey(key);
    }

    /**
     * Do we have a key for a value?
     * 
     * <p>Remember that we can have elements in our index which have no key,
     * so this returns true for a subset of what will be true for 
     * {@link #contains(Object)}.</p>
     * 
     * @param value value to check.
     * @return true if there is a key for <code>value</code>
     */
    public boolean containsValue(V value) {
        Set<Entry<K, V>> set = entrySet();
        for (Entry<K, V> entry : set) {
            if (entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create an entry set for the ListMap.
     * 
     * @return a Set<Entry<K, V>>
     */
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<Entry<K, V>>();
        for (Entry<K, ListMapEntry> entry : keyMap.entrySet()) {
            SimpleEntry<K, V> e = 
                new AbstractMap.SimpleEntry<K, V>(entry.getKey(), 
                                                  entry.getValue().getValue());
            set.add(e);
        }
        return set;
    }

    /**
     * Get the value for an index
     */
    @Override
    public V get(int index) {
        return valueList.get(index).getValue();
    }
    /**
     * Get the value for a key
     * 
     * @param key
     * @return the requested value
     */
    public V get(K key) {
        return keyMap.get(key).getValue();
    }
    /**
     * Walk the entries to find the key related to a value.
     * 
     * <p>This is not considered a key function, and the speed is appropriate
     * for the priority.</p>
     * 
     * @param value
     * @return key associated with a value
     */
    public K getKey(V value) {
        Set<Map.Entry<K, V>> entries;
        entries = entrySet();
        K found = null;
        for (Map.Entry<K, V> entry : entries) {
            if (entry.getValue().equals(value)) {
                found = entry.getKey();
                break;
            }
        }
        return found;
    }

    /**
     * Warning: This may be slow.
     */
    @Override
    public int indexOf(Object value) {
        for (int index = 0; index < valueList.size(); index++) {
            ListMapEntry entry = this.valueList.get(index);
            if (entry == null) {
                continue;
            } else if (entry.getValue() == null) {
                if (value == null) {
                    return index;
                }
            } else if (entry.getValue().equals(value)) {
                return index;
            }
        }
        return -1;
    }
    /**
     * Get the index of a key.
     * 
     * <p>This is guaranteed to be the same speed as getting the value.</p>
     * 
     * @param key
     * @return the index for the key
     */
    public int indexOfKey(K key) {
        return keyMap.get(key).getIndex();
    }

    @Override
    public boolean isEmpty() {
        return valueList.isEmpty();
    }

    @Override
    public Iterator<V> iterator() {
        return new ListMapIterator(this);
    }
    public Set<? extends K> keySet() {
        return keyMap.keySet();
    }
    @Override
    public int lastIndexOf(Object value) {
        for (int index = valueList.size()-1; index > 0 ; index--) {
            ListMapEntry entry = this.valueList.get(index);
            if (entry == null) {
                continue;
            } else if (entry.getValue() == null) {
                if (value == null) {
                    return index;
                }
            } else if (entry.getValue().equals(value)) {
                return index;
            }
        }
        return -1;
    }
    @Override
    public ListIterator<V> listIterator() {
        return new ListMapIterator(this);
    }
    @Override
    public ListIterator<V> listIterator(int index) {
        return new ListMapIterator(this, index);
    }
    /**
     * Add or update entry
     * 
     * @see #put(K, V)
     * @param entry entry to add or update
     * @return the previous value
     */
    protected V put(Entry<K, V> entry) {
        ListMapEntry old = keyMap.get(entry.getKey());
        if (old == null) {
            add(entry);
            return null;
        }
        return this.set(old.getIndex(), entry.getValue());
    }
    /**
     * Add or update a key-labeled entry.
     * 
     * <p>If the key exists, the index it uses is updated with the value.
     * This update will be seen by all keys using that index. If you want
     * to always add a new index instead of updating it, use 
     * {@link #add(Object, Object)} instead.</p>
     * 
     * @param key key to use
     * @param value value to use
     * @return previous value
     */
    public V put(K key, V value) {
        ListMapEntry old = keyMap.get(key);
        if (old == null) {
            this.add(key, value);
            return null;
        }
        return this.set(old.getIndex(), value);
    }
    /**
     * Put all the values from an existing list map in a new order.
     * 
     * <p>This drops the references to the same index entries that were present
     * in the <code>old</code> ListMap. They all get promoted to their own
     * entries, all guaranteed to be in the same order described in 
     * <code>newOrder</code>.</p>
     * 
     * @param old old ListMap
     * @param newOrder the new order
     */
    public void putAll(ListMap<K, V> old, K[] newOrder) {
        for (K key : newOrder) {
            this.put(key, old.get(key));
        }
    }
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }
    /**
     * Add a (possibly additional) key for an index.
     * 
     * @param key key to use
     * @param index index to use
     * @return previous index used (or -1)
     */
    public int putKey(K key, int index) {
        ListMapEntry cur = valueList.get(index);
        ListMapEntry old = keyMap.put(key, cur);
        if (old == null) return -1;
        return old.getIndex();
    }
    /**
     * Create another association to the same index entry
     * 
     * <p>This causes another key to be associated with the same index.
     * This means:</p>
     * <pre>
     *    ListMap<String, Integer> listmap = new ListMap<String, Integer>();
     *    listmap.add("a", 1);
     *    listmap.putKey("b", "a");
     *    listmap.put("b", 2);
     *    assert(listmap.get("a") == 2);
     * </pre>
     * 
     * @param newKey
     * @param existingKey
     * @return index old index used by the alias
     */
    public int putKey(K newKey, K existingKey) {
        ListMapEntry old = keyMap.put(newKey, keyMap.get(existingKey)); 
        if (old == null) {
            return -1;
        }
        return old.getIndex();
    }
    /**
     * Update the index used by the label system to match what is in place.
     * @param start starting index position
     */
    private void reindex(int start) {
        for (int i = start; i < this.valueList.size(); i++) {
            this.valueList.get(i).setIndex(i);
        }
    }
    /**
     * Entirely remove an index entry
     * 
     * <p>This first scans the key map and removes any entries referencing the
     * index entry and then removes the index entry itself, updating any of the
     * indexes which may be impacted by the change.</p>
     */
    @Override
    public V remove(int index) {
        if (index < 0 || index >= valueList.size()) {
            return null;
        }
        Set<Entry<K, ListMapEntry>> entries = keyMap.entrySet();
        Iterator<Entry<K, ListMapEntry>> i = entries.iterator();
        while(i.hasNext()) {
            Entry<K, ListMapEntry> entry = i.next();
            if (entry == null || entry.getValue() == null) {
                i.remove();
            } else if (entry.getValue().getIndex() == index) {
                i.remove();
            }
        }
        ListMapEntry old = this.valueList.remove(index);
        if (index != valueList.size()) {
            reindex(index);
        }
        if (old == null) return null;
        return old.getValue();
    }
    @Override
    public boolean remove(Object value) {
        int origSize = this.size();
        int index = valueList.indexOf(value);
        if (index == -1) return false;
        remove(index);
        return this.size() != origSize;
    }
    
    @Override
    public boolean removeAll(Collection<?> values) {
        int origSize = this.size();
        for (Object value : values) {
            remove(value);
        }
        return this.size() != origSize;
    }

    /**
     * Remove a key.
     * 
     * <p>This does not remove the index the key references, nor does it remove
     * any other keys or aliases using the same index.</p>
     * 
     * <p>To perform a complete removal, you can call {@link #remove(int)} on
     * the results of this function safely. (It will ignore indexes of -1.)</p>
     * 
     * @param key key to remove
     * @return index value used by the key or -1 if not found
     */
    public int removeKey(K key) {
        ListMapEntry old = keyMap.remove(key);
        if (old == null) return -1;
        return old.getIndex();
    }
    @Override
    public boolean retainAll(Collection<?> values) {
        if (values == null) return false;
        int oldSize = this.size();
        int index = 0;
        while (index < this.valueList.size()) {
            V value = this.valueList.get(index).getValue();
            if (values.contains(value)) {
                index++;
            } else {
                remove(index);
            }
        }
        return oldSize != this.size();
    }
    @Override
    public V set(int index, V value) {
        ListMapEntry e = this.valueList.get(index);
        V oldValue = e.getValue();
        e.setValue(value);
        return oldValue;
    }
    @Override
    public int size() {
        return this.valueList.size();
    }
    @Override
    public List<V> subList(int start, int end) {
        if (start == -1 || end == -1) return null;
        ArrayList<V> ret = new ArrayList<V>();
        for (int i = start; i < end; i++) {
            ret.add(get(i));
        }
        return ret;
    }
    @Override
    public Object[] toArray() {
        Object[] ret = new Object[this.size()];
        for (int i = 0; i < size(); i++) {
            ret[i] = get(i);
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        T[] ret;
        if (a.length >= this.size()) {
            ret = a;
        } else {
            ret = (T[]) Array.newInstance(a.getClass(), this.size());
        }
        for (int i = 0; i < size(); i++) {
            ret[i] = (T) get(i);
        }
        return ret;
    }

    /**
     * Return a collection of just the values.
     * 
     * @return this is a collection of just the values.
     */
    public Collection<V> values() {
        return this;
    }

}
