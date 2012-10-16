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
package com.googlecode.blacken.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * A List which is also (sort of) a Map.
 */
public class TestListMap {
    /**
     * Test the list map
     */
    @Test
    public void ListMap() {
        ListMap<Integer, Boolean> lm = new ListMap<Integer, Boolean>();
        assertNotNull(lm);
    }
    /**
     * Create a ListMap expecting a particular size
     */
    @Test
    public void ListMap_size() {
        ListMap<Integer, Boolean> lm = new ListMap<Integer, Boolean>(12);
        assertEquals(lm.size(), 0);
        lm.add(true);
        assertEquals(lm.size(), 1);
        lm.add(false);
        assertEquals(lm.size(), 2);
    }
    /**
     * Create a ListMap based upon an ordered Collection
     */
    @Test
    public void ListMap_Collection() {
        Collection<Boolean> col = new ArrayList<Boolean>();
        col.add(true);
        col.add(false);
        ListMap<Integer, Boolean> lm = new ListMap<Integer, Boolean>(col);
        assertEquals(lm.size(), 2);
        assertTrue(lm.get(0));
        assertFalse(lm.get(1));
    }
    /**
     * Create a ListMap based upon an existing ListMap
     */
    @Test
    public void ListMap_ListMap() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        lm.addAndLabel(false, "false", "no"); //$NON-NLS-2$
        assertEquals(lm.size(), 2);
        assertTrue(lm.get(0));
        assertFalse(lm.get(1));
        ListMap<String, Boolean> lm1 = new ListMap<String, Boolean>(lm);
        assertEquals(lm1.size(), 2);
        assertTrue(lm1.get(0));
        assertFalse(lm1.get(1));
    }
    /**
     * Create a ListMap based upon an existing ListMap
     */
    @Test(expected=NullPointerException.class)
    public void ListMap_ListMap2() {
        ListMap<Integer, Boolean> lm = null;
        ListMap<Integer, Boolean> lm2 = new ListMap<Integer, Boolean>(lm);
        // never gets here
        assertEquals(lm2.size(), 0);
    }

    /**
     * @see java.util.List#add(int, java.lang.Object)
     */
    @Test
    public void add_index_element() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.add(0, true);
        assertTrue(lm.get(0));
        lm.add(0, false);
        assertFalse(lm.get(0));
    }
    
    /**
     * Add a new <code>value</code> to the end of the list, naming it 
     * <code>key</code>.
     */
    @Test
    public void add_key_value() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.add("true", true);
        assertTrue(lm.get(0));
        assertTrue(lm.get("true"));
        lm.add("false", false);
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
    }
    /**
     * Add a new <code>value</code> to the end of the list, naming it 
     * <code>key</code>.
     */
    @Test
    public void add_key_value2() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.add("name", true);
        assertTrue(lm.get(0));
        assertTrue(lm.get("name"));
        lm.add("name", false);
        assertFalse(lm.get("name"));
        assertTrue(lm.get(0));
        assertFalse(lm.get(1));
    }
    /**
     * Map a set of keys to a particular value
     */
    @Test
    public void add_keys_value() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        String[] t = {"true","yes"}; //$NON-NLS-2$
        lm.add(t, true);
        assertTrue(lm.get(0));
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        String[] f = {"false", "no"}; //$NON-NLS-2$
        lm.add(f, false);
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        assertFalse(lm.get("no"));
    }
    /**
     * A version of add(Object[], Object) that will work with varargs.
     */
    @Test
    public void addAndLabel_value_keys() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        assertTrue(lm.get(0));
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        lm.addAndLabel(false, "false", "no"); //$NON-NLS-2$
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        assertFalse(lm.get("no"));
    }
    /**
     * Add a value
     */
    @Test
    public void add_value() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.add(true);
        assertTrue(lm.get(0));
        lm.add(false);
        assertFalse(lm.get(1));
    }
    /**
     * Add an existing ListMap to the end of this one.
     */
    @Test
    public void addAll_ListMap() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        assertTrue(lm.get(0));
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        lm.addAndLabel(false, "false", "no"); //$NON-NLS-2$
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        assertFalse(lm.get("no"));
        assertEquals(2, lm.size());
        ListMap<String, Boolean> lm1 = new ListMap<String, Boolean>();
        lm1.addAll(lm);
        // Check that they're the same
        assertTrue(lm1.get(0));
        assertTrue(lm1.get("true"));
        assertTrue(lm1.get("yes"));
        assertFalse(lm1.get(1));
        assertFalse(lm1.get("false"));
        assertFalse(lm1.get("no"));
        assertEquals(2, lm1.size());
    }

    /**
     * Add an existing ListMap to the end of this one.
     */
    @Test
    public void addAll_ListMap2() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        lm.addAndLabel(false, "false", "no"); //$NON-NLS-2$
        assertEquals(2, lm.size());
        ListMap<String, Boolean> lm1 = new ListMap<String, Boolean>();
        lm1.addAll(lm);
        assertEquals(2, lm1.size());
        
        // Now do something different
        
        // setting lm1 does not set lm
        lm1.set(0, false);
        assertFalse(lm1.get(0));
        assertTrue(lm.get(0));
        // adding to lm1 does not add to lm
        lm1.add(true);
        assertEquals(2, lm.size());
        assertEquals(3, lm1.size());
        assertTrue(lm1.get(2));
    }

    /**
     * Add the indexed <code>values</code> to the end of this list.
     */
    @Test
    public void addAll_Collection() {
        List<Boolean> collection = new ArrayList<Boolean>();
        collection.add(true);
        collection.add(false);
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAll(collection);
        assertTrue(lm.get(0));
        assertFalse(lm.get(1));
    }
    /**
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    @Test
    public void addAll_index_valuesCollection() {
        List<Integer> collection = new ArrayList<Integer>();
        collection.add(0);
        collection.add(4);
        collection.add(5);
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(0, collection);
        collection.clear();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        lm.addAll(1, collection);
        collection.clear();
        collection.add(lm.size());
        lm.addAll(lm.size(), collection);
        for (Integer i = 0; i < lm.size(); i++) {
            assertEquals(i, lm.get(i));
        }
    }

    /**
     * Add a set of values at a particular index position.
     */
    @Test
    public void addAll_index_itemsArray() {
        Integer[] collection = new Integer[3];
        collection[0] = 0;
        collection[1] = 4;
        collection[2] = 5;
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(0, collection);
        collection[0] = 1;
        collection[1] = 2;
        collection[2] = 3;
        lm.addAll(1, collection);
        collection[0] = lm.size();
        collection[1] = lm.size()+1;
        collection[2] = lm.size()+2;
        lm.addAll(lm.size(), collection);
        for (Integer i = 0; i < lm.size(); i++) {
            assertEquals(i, lm.get(i));
        }
    }
    /**
     * Add a synchronized pair of key and item arrays.
     */
    @Test
    public void addAll_KeyArray_ValueArray() {
        Integer[] values = new Integer[3];
        values[0] = 0;
        values[1] = 1;
        values[2] = 2;
        String[] keys = new String[3];
        keys[0] = "0";
        keys[1] = "1";
        keys[2] = "2";
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(keys, values);
        for (Integer i = 0; i < lm.size(); i++) {
            assertEquals(i, lm.get(i));
            assertEquals(i.toString(), lm.getKey(i));
            assertEquals(i, lm.get(i.toString()));
        }
    }
    /**
     * Add a synchronized pair of key and item Lists.
     */
    @Test
    public void addAll_KeyList_ValueList() {
        List<Integer> values = new ArrayList<Integer>();
        values.add(0);
        values.add(1);
        values.add(2);
        List<String> keys = new ArrayList<String>();
        keys.add("0");
        keys.add("1");
        keys.add("2");
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(keys, values);
        for (Integer i = 0; i < lm.size(); i++) {
            assertEquals(i, lm.get(i));
            assertEquals(i.toString(), lm.getKey(i));
            assertEquals(i, lm.get(i.toString()));
        }
    }
    
    /**
     * Add a set of keys from an existing ListMap in a particular order.
     */
    @Test
    public void addAll_ListMap_KeyOrder() {
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAndLabel(3, "3");
        lm.addAndLabel(2, "2");
        lm.addAndLabel(1, "1");
        lm.addAndLabel(0, "0");
        assertEquals(new Integer(3), lm.get(0));
        assertEquals(new Integer(2), lm.get(1));
        assertEquals(new Integer(1), lm.get(2));
        assertEquals(new Integer(0), lm.get(3));
        ListMap<String, Integer> lm1 = new ListMap<String, Integer>();
        String[] order = {"0",
                          "1",
                          "2",
                          "3"};
        lm1.addAll(lm, order);
        assertEquals(new Integer(0), lm1.get(0));
        assertEquals(new Integer(1), lm1.get(1));
        assertEquals(new Integer(2), lm1.get(2));
        assertEquals(new Integer(3), lm1.get(3));
    }

    /**
     * Add an existing map set
     */
    @Test
    public void addAll_EntrySet() {
        Map<String, Boolean> m = new HashMap<String, Boolean>();
        m.put("true", true); 
        m.put("false", false); 
        assertEquals(2, m.size());
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAll(m.entrySet());
        assertEquals(2, lm.size());
        assertTrue(lm.get("true"));
        assertFalse(lm.get("false"));
        ListMap<String, Boolean> lm1 = new ListMap<String, Boolean>();
        lm1.addAll(lm.entrySet());
        assertEquals(2, lm1.size());
        assertTrue(lm1.get("true"));
        assertFalse(lm1.get("false"));
        lm1.putKey("yes", "true"); //$NON-NLS-2$
        assertEquals(2, lm1.size());
        ListMap<String, Boolean> lm2 = new ListMap<String, Boolean>();
        lm2.addAll(lm1.entrySet());
        assertEquals(3, lm2.size());
    }

    /**
     * Add an array of values.
     */
    @Test
    public void addAll_Values() {
        List<Integer> values = new ArrayList<Integer>();
        values.add(0);
        values.add(1);
        values.add(2);
        assertEquals(3, values.size());
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(values);
        assertEquals(3, lm.size());
        for (Integer i = 0; i < lm.size(); i++) {
            assertEquals(i, lm.get(i));
        }
    }

    /**
     * Clear both the index as well as the name map.
     */
    @Test
    public void clear() {
        List<Integer> values = new ArrayList<Integer>();
        values.add(0);
        values.add(1);
        values.add(2);
        assertEquals(3, values.size());
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(values);
        assertEquals(3, lm.size());
        assertFalse(lm.isEmpty());
        lm.clear();
        assertEquals(0, lm.size());
        assertTrue(lm.isEmpty());
        ListMap<String, Integer> lm1 = new ListMap<String, Integer>();
        assertEquals(0, lm1.size());
        assertTrue(lm1.isEmpty());
        lm1.clear();
        assertEquals(0, lm1.size());
        assertTrue(lm1.isEmpty());
    }

    /**
     * Clear the key map.
     */
    @Test
    public void clearKeys() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        lm.add("false", false);
        assertEquals(2, lm.size());
        assertTrue(lm.get(0));
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        assertTrue(lm.containsKey("true"));
        assertTrue(lm.containsKey("yes"));
        assertTrue(lm.containsKey("false"));
        lm.clearKeys();
        assertEquals(2, lm.size());
        assertTrue(lm.get(0));
        assertNull(lm.get("true"));
        assertNull(lm.get("yes"));
        assertFalse(lm.get(1));
        assertNull(lm.get("false"));
        assertFalse(lm.containsKey("true"));
        assertFalse(lm.containsKey("yes"));
        assertFalse(lm.containsKey("false"));
    }

    /**
     * Is the value present?
     */
    @Test
    public void contains_Value() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        lm.add("false", false);
        assertEquals(2, lm.size());
        assertTrue(lm.get(0));
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        assertTrue(lm.containsKey("true"));
        assertTrue(lm.containsKey("yes"));
        assertTrue(lm.containsKey("false"));
        assertTrue(lm.contains(true));
        assertTrue(lm.contains(false));
        lm.clearKeys();
        assertEquals(2, lm.size());
        assertTrue(lm.get(0));
        assertNull(lm.get("true"));
        assertNull(lm.get("yes"));
        assertFalse(lm.get(1));
        assertNull(lm.get("false"));
        assertFalse(lm.containsKey("true"));
        assertFalse(lm.containsKey("yes"));
        assertFalse(lm.containsKey("false"));
        assertTrue(lm.contains(true));
        assertTrue(lm.contains(false));
    }

    /**
     * Are all the values present?
     */
    @Test
    public void containsAll() {
        List<Boolean> c = new ArrayList<Boolean>();
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        assertTrue(lm.containsAll(c));
        c.add(true);
        assertFalse(lm.containsAll(c));
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        assertTrue(lm.containsAll(c));
        c.add(false);
        assertFalse(lm.containsAll(c));
        lm.add("false", false);
        assertTrue(lm.containsAll(c));
        c.clear();
        assertTrue(lm.containsAll(c));
        lm.clear();
        assertTrue(lm.containsAll(c));
    }

    /**
     * Do we have a specific key?
     */
    @Test
    public void containsKey() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        lm.add("false", false);
        assertEquals(2, lm.size());
        assertTrue(lm.containsKey("true"));
        assertTrue(lm.containsKey("yes"));
        assertTrue(lm.containsKey("false"));
        lm.clearKeys();
        assertEquals(2, lm.size());
        assertNull(lm.get("true"));
        assertNull(lm.get("yes"));
        assertNull(lm.get("false"));
        assertFalse(lm.containsKey("true"));
        assertFalse(lm.containsKey("yes"));
        assertFalse(lm.containsKey("false"));
    }

    /**
     * Do we have a key for a value?
     */
    @Test
    public void containsValue() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        assertTrue(lm.contains(true));
        assertTrue(lm.containsKey("true"));
        assertTrue(lm.containsKey("yes"));
        assertTrue(lm.containsValue(true));
        assertFalse(lm.containsValue(false));
        assertFalse(lm.contains(false));
        lm.add(false);
        assertTrue(lm.contains(false));
        assertFalse(lm.containsValue(false));
        lm.putKey("false", 1);
        assertTrue(lm.containsKey("false"));
        assertFalse(lm.get("false"));
        assertTrue(lm.containsValue(false));
    }

    /**
     * Create an entry set for the ListMap.
     */
    @Test
    public void entrySet() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        assertEquals(1, lm.size());
        lm.add(false);
        assertEquals(2, lm.size());
        Set<Map.Entry<String, Boolean>> s;
        s = lm.entrySet();
        assertEquals(2, lm.size());
        for (Map.Entry<String, Boolean> e : s) {
            assertTrue(e.getValue());
            assertNotNull(e.getKey());
            if (!e.getKey().equals("true") && !e.getKey().equals("yes")) { //$NON-NLS-2$
                fail(String.format("Wrong entry in set: %s", e));
            }
        }
    }

    /**
     * Get the value for an index
     */
    @Test
    public void get_index() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        String[] t = {"true","yes"}; //$NON-NLS-2$
        lm.add(t, true);
        assertTrue(lm.get(0));
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        String[] f = {"false", "no"}; //$NON-NLS-2$
        lm.add(f, false);
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        assertFalse(lm.get("no"));
    }
    /**
     * Get the value for a key
     */
    @Test
    public void get_key() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        String[] t = {"true","yes"}; //$NON-NLS-2$
        lm.add(t, true);
        assertTrue(lm.get(0));
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        String[] f = {"false", "no"}; //$NON-NLS-2$
        lm.add(f, false);
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        assertFalse(lm.get("no"));
    }
    
    /**
     * Walk the entries to find the key related to a value.
     */
    @Test
    public void getKey() {
        Integer[] values = new Integer[3];
        values[0] = 0;
        values[1] = 1;
        values[2] = 2;
        String[] keys = new String[3];
        keys[0] = "0";
        keys[1] = "1";
        keys[2] = "2";
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(keys, values);
        for (Integer i = 0; i < lm.size(); i++) {
            assertEquals(i, lm.get(i));
            assertEquals(i.toString(), lm.getKey(i));
            assertEquals(i, lm.get(i.toString()));
        }
    }

    /**
     * Get the index for a value.
     */
    @Test
    public void indexOf_value() {
        Integer[] values = new Integer[3];
        values[0] = 0;
        values[1] = 1;
        values[2] = 2;
        String[] keys = new String[3];
        keys[0] = "0";
        keys[1] = "1";
        keys[2] = "2";
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(keys, values);
        for (Integer i = 0; i < lm.size(); i++) {
            assertEquals(i, lm.get(i));
            assertEquals(i.toString(), lm.getKey(i));
            assertEquals(i, lm.get(i.toString()));
            assertEquals((int)i, lm.indexOf(i));
            assertEquals((int)i, lm.indexOfKey(i.toString()));
        }
    }
    /**
     * Get the index of a key.
     */
    @Test
    public void indexOfKey() {
        Integer[] values = new Integer[3];
        values[0] = 0;
        values[1] = 1;
        values[2] = 2;
        String[] keys = new String[3];
        keys[0] = "0";
        keys[1] = "1";
        keys[2] = "2";
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(keys, values);
        for (Integer i = 0; i < lm.size(); i++) {
            assertEquals(i, lm.get(i));
            assertEquals(i.toString(), lm.getKey(i));
            assertEquals(i, lm.get(i.toString()));
            assertEquals((int)i, lm.indexOf(i));
            assertEquals((int)i, lm.indexOfKey(i.toString()));
        }
    }

    /**
     * @see java.util.List#isEmpty()
     */
    @Test
    public void isEmpty() {
        List<Integer> values = new ArrayList<Integer>();
        values.add(0);
        values.add(1);
        values.add(2);
        assertEquals(3, values.size());
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        assertTrue(lm.isEmpty());
        lm.addAll(values);
        assertEquals(3, lm.size());
        assertFalse(lm.isEmpty());
        lm.clear();
        assertEquals(0, lm.size());
        assertTrue(lm.isEmpty());
        ListMap<String, Integer> lm1 = new ListMap<String, Integer>();
        assertEquals(0, lm1.size());
        assertTrue(lm1.isEmpty());
        lm1.clear();
        assertEquals(0, lm1.size());
        assertTrue(lm1.isEmpty());
    }

    /**
     * @see java.util.List#iterator()
     */
    @Test
    public void iterator() {
        Integer[] values = new Integer[3];
        values[0] = 0;
        values[1] = 1;
        values[2] = 2;
        String[] keys = new String[3];
        keys[0] = "0";
        keys[1] = "1";
        keys[2] = "2";
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(keys, values);
        Integer i = 0;
        for (Integer v : lm) {
            assertEquals(i, v);
            assertEquals((int)i, lm.indexOf(v));
            i++;
        }
    }
    /**
     * Similar to {@link Map#keySet()}.
     */
    @Test
    public void keySet() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        assertTrue(lm.get(0));
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        lm.addAndLabel(false, "false", "no"); //$NON-NLS-2$
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        assertFalse(lm.get("no"));
        assertEquals(2, lm.size());
        Set<? extends String> s = lm.keySet();
        assertEquals(4, s.size());
        for (String z : s) {
            assertNotNull(z);
        }
    }
    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    @Test
    public void lastIndexOf_value() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        lm.addAndLabel(false, "false", "no"); //$NON-NLS-2$
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        lm.add(false);
        assertEquals(4, lm.size());
        assertEquals(0, lm.indexOf(true));
        assertEquals(2, lm.lastIndexOf(true));
        assertEquals(1, lm.indexOf(false));
        assertEquals(3, lm.lastIndexOf(false));
    }
    /**
     * @see java.util.List#listIterator()
     */
    @Test
    public void listIterator() {
        Integer[] values = new Integer[3];
        values[0] = 0;
        values[1] = 1;
        values[2] = 2;
        String[] keys = new String[3];
        keys[0] = "0";
        keys[1] = "1";
        keys[2] = "2";
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(keys, values);
        Integer i = 0;
        ListIterator<Integer> li = lm.listIterator();
        while(li.hasNext()) {
            Integer v = li.next();
            assertEquals(i, v);
            assertEquals((int)i, lm.indexOf(v));
            i++;
            assertEquals((int)i, li.nextIndex());
        }
        while(li.hasPrevious()) {
            i--;
            assertEquals((int)i, li.previousIndex());
            Integer v = li.previous();
            assertEquals((int)i, lm.indexOf(v));
        }
    }
    /**
     * @see java.util.List#listIterator(int)
     */
    @Test
    public void listIterator_index() {
        Integer[] values = new Integer[3];
        values[0] = 0;
        values[1] = 1;
        values[2] = 2;
        String[] keys = new String[3];
        keys[0] = "0";
        keys[1] = "1";
        keys[2] = "2";
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAll(keys, values);
        Integer i = lm.size();
        ListIterator<Integer> li = lm.listIterator(lm.size());
        while(li.hasPrevious()) {
            i--;
            assertEquals((int)i, li.previousIndex());
            Integer v = li.previous();
            assertEquals((int)i, lm.indexOf(v));
        }
        while(li.hasNext()) {
            Integer v = li.next();
            assertEquals(i, v);
            assertEquals((int)i, lm.indexOf(v));
            i++;
            assertEquals((int)i, li.nextIndex());
        }
    }
    /**
     * Add or update a key-labeled entry.
     */
    @Test
    public void put_Key_Value() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.put("true", true); 
        lm.put("false", false); 
        assertEquals(2, lm.size());
        assertTrue(lm.get("true"));
        assertFalse(lm.get("false"));
        lm.put("true", false);
        lm.put("false", true);
        assertEquals(2, lm.size());
        assertFalse(lm.get("true"));
        assertTrue(lm.get("false"));
    }
    /**
     * Put all the values from an existing list map in a new order.
     */
    @Test
    public void putAll_ListMap_KeyOrder() {
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAndLabel(3, "3");
        lm.addAndLabel(2, "2");
        lm.addAndLabel(1, "1");
        lm.addAndLabel(0, "0");
        assertEquals(new Integer(3), lm.get(0));
        assertEquals(new Integer(2), lm.get(1));
        assertEquals(new Integer(1), lm.get(2));
        assertEquals(new Integer(0), lm.get(3));
        ListMap<String, Integer> lm1 = new ListMap<String, Integer>();
        String[] order = {"0",
                          "1",
                          "2",
                          "3"};
        lm1.put("-1", -1);
        lm1.put("0", 0);
        lm1.putAll(lm, order);
        assertEquals(new Integer(-1), lm1.get(0));
        assertEquals(new Integer(0), lm1.get(1));
        assertEquals(new Integer(1), lm1.get(2));
        assertEquals(new Integer(2), lm1.get(3));
        assertEquals(new Integer(3), lm1.get(4));
        assertEquals(5, lm1.size());
    }
    /**
     * Put all entries from an existing map into this one.
     */
    @Test
    public void putAll_Map() {
        Map<String, Boolean> m = new HashMap<String, Boolean>();
        m.put("true", true); 
        m.put("false", false); 
        assertEquals(2, m.size());
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.putAll(m);
        assertEquals(2, lm.size());
        assertTrue(lm.get("true"));
        assertFalse(lm.get("false"));
    }
    /**
     * Put all entries from an existing map, using an explicit order.
     */
    @Test
    public void putAll_Map_KeyOrder() {
        // linked hash map so order walked is not the order we want
        Map<String, Boolean> m = new LinkedHashMap<String, Boolean>();
        m.put("true", true); 
        m.put("false", false); 
        assertEquals(2, m.size());
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        String[] order = {"false", "true"}; //$NON-NLS-2$
        lm.putAll(m, order);
        assertEquals(2, lm.size());
        assertFalse(lm.get(0));
        assertTrue(lm.get(1));
        assertTrue(lm.get("true"));
        assertFalse(lm.get("false"));
    }
    
    /**
     * Copy entries from an existing ListMap in a particular order.
     */
    @Test
    public void putAll_ListMap_IntOrder() {
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.addAndLabel(3, "3", "three"); //$NON-NLS-2$
        lm.addAndLabel(2, "2", "two"); //$NON-NLS-2$
        lm.addAndLabel(1, "1", "one"); //$NON-NLS-2$
        lm.addAndLabel(0, "0", "zero"); //$NON-NLS-2$
        assertEquals(new Integer(3), lm.get(0));
        assertEquals(new Integer(2), lm.get(1));
        assertEquals(new Integer(1), lm.get(2));
        assertEquals(new Integer(0), lm.get(3));
        ListMap<String, Integer> lm1 = new ListMap<String, Integer>();
        int[] order = {3, 2, 1, 0};
        lm1.put("-1", -1);
        lm1.put("0", 0);
        lm1.putAll(lm, order);
        assertEquals(new Integer(-1), lm1.get(0));
        assertEquals(new Integer(0), lm1.get(1));
        assertEquals(1, lm1.indexOfKey("zero"));
        assertEquals(1, lm1.indexOfKey("0"));
        assertEquals(new Integer(1), lm1.get(2));
        assertEquals(2, lm1.indexOfKey("one"));
        assertEquals(2, lm1.indexOfKey("1"));
        assertEquals(new Integer(2), lm1.get(3));
        assertEquals(3, lm1.indexOfKey("two"));
        assertEquals(3, lm1.indexOfKey("2"));
        assertEquals(new Integer(3), lm1.get(4));
        assertEquals(4, lm1.indexOfKey("three"));
        assertEquals(4, lm1.indexOfKey("3"));
        assertEquals(5, lm1.size());
    }
    
    /**
     * Add a (possibly additional) key for an index.
     */
    @Test
    public void putKey_Key_Index() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        assertTrue(lm.contains(true));
        assertTrue(lm.containsKey("true"));
        assertTrue(lm.containsKey("yes"));
        assertTrue(lm.containsValue(true));
        assertFalse(lm.containsValue(false));
        assertFalse(lm.contains(false));
        lm.add(false);
        assertEquals(2, lm.size());
        assertTrue(lm.contains(false));
        assertFalse(lm.containsValue(false));
        lm.putKey("false", 1);
        assertEquals(2, lm.size());
        assertTrue(lm.containsKey("false"));
        assertFalse(lm.get("false"));
        assertTrue(lm.containsValue(false));
    }
    
    /**
     * Add a (possibly additional) key for an index.
     */
    @Test
    public void putKey_KeyArray_Index() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$
        assertTrue(lm.contains(true));
        assertTrue(lm.containsKey("true"));
        assertTrue(lm.containsKey("yes"));
        assertTrue(lm.containsValue(true));
        assertFalse(lm.containsValue(false));
        assertFalse(lm.contains(false));
        lm.add(false);
        assertTrue(lm.contains(false));
        assertFalse(lm.containsValue(false));
        String[] k = {"false", "no"}; //$NON-NLS-2$
        lm.putKey(k, 1);
        assertEquals(2, lm.size());
        assertTrue(lm.containsKey("false"));
        assertTrue(lm.containsKey("no"));
        assertFalse(lm.get("false"));
        assertFalse(lm.get("no"));
        assertTrue(lm.containsValue(false));
    }
    
    /**
     * Create another association to the same index entry
     */
    @Test
    public void putKey_Key_Key() {
        Map<String, Boolean> m = new HashMap<String, Boolean>();
        m.put("true", true); 
        m.put("false", false); 
        assertEquals(2, m.size());
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAll(m.entrySet());
        assertEquals(2, lm.size());
        assertTrue(lm.get("true"));
        assertFalse(lm.get("false"));
        ListMap<String, Boolean> lm1 = new ListMap<String, Boolean>();
        lm1.addAll(lm.entrySet());
        assertEquals(2, lm1.size());
        assertTrue(lm1.get("true"));
        assertFalse(lm1.get("false"));
        lm1.putKey("yes", "true"); //$NON-NLS-2$
        assertEquals(2, lm1.size());
        assertTrue(lm1.get("yes"));
        assertEquals(0, lm1.indexOfKey("yes"));
        assertEquals(0, lm1.indexOfKey("true"));
    }
    
    /**
     * Entirely remove an index entry
     */
    @Test
    public void remove_Index() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$ 
        lm.add("false", false); 
        assertEquals(2, lm.size());
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        assertTrue(lm.get(0));
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        lm.remove(0);
        assertFalse(lm.get(0));
        assertEquals(1, lm.size());
        assertFalse(lm.get("false"));
        assertNull(lm.get("true"));
        assertNull(lm.get("yes"));
        lm.remove(0);
        assertEquals(0, lm.size());
        assertNull(lm.get("false"));
        lm.add(true);
        lm.add(false);
        assertEquals(2, lm.size());
        lm.remove(1);
        assertEquals(1, lm.size());
        assertTrue(lm.get(0));
    }
    /**
     * Remove a value
     */
    @Test
    public void remove_Value() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$ 
        lm.add("false", false); 
        assertEquals(2, lm.size());
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        assertTrue(lm.get(0));
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        lm.remove(true);
        assertFalse(lm.get(0));
        assertEquals(1, lm.size());
        assertFalse(lm.get("false"));
        assertNull(lm.get("true"));
        assertNull(lm.get("yes"));
        lm.remove(false);
        assertEquals(0, lm.size());
        assertNull(lm.get("false"));
        lm.add(true);
        lm.add(true);
        assertEquals(2, lm.size());
        lm.remove(true);
        assertEquals(1, lm.size());
        assertTrue(lm.get(0));
    }
    
    /**
     * Remove all from a collection
     */
    @Test
    public void removeAll() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$ 
        lm.add("false", false); 
        assertEquals(2, lm.size());
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        assertTrue(lm.get(0));
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        List<Boolean> c = new ArrayList<Boolean>();
        c.add(true);
        lm.removeAll(c);
        assertFalse(lm.get(0));
        assertEquals(1, lm.size());
        assertFalse(lm.get("false"));
        assertNull(lm.get("true"));
        assertNull(lm.get("yes"));
        c.add(false);
        lm.removeAll(c);
        assertEquals(0, lm.size());
        assertNull(lm.get("false"));
        c.remove(1);
        lm.add(true);
        lm.add(true);
        assertEquals(2, lm.size());
        lm.removeAll(c);
        assertEquals(1, lm.size());
        assertTrue(lm.get(0));
    }

    /**
     * Remove a key.
     */
    @Test
    public void removeKey() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$ 
        lm.add("false", false); 
        assertEquals(2, lm.size());
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        assertTrue(lm.get(0));
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        lm.remove(true);
        assertFalse(lm.get(0));
        assertEquals(1, lm.size());
        assertFalse(lm.get("false"));
        assertNull(lm.get("true"));
        assertNull(lm.get("yes"));
        lm.remove(false);
        assertEquals(0, lm.size());
        assertNull(lm.get("false"));
        lm.add(true);
        lm.add(true);
        assertEquals(2, lm.size());
        lm.remove(true);
        assertEquals(1, lm.size());
        assertTrue(lm.get(0));
    }
    /**
     * @see java.util.List#retainAll(java.util.Collection)
     */
    @Test
    public void retainAll() {
        ListMap<String, Boolean> lm = new ListMap<String, Boolean>();
        lm.addAndLabel(true, "true", "yes"); //$NON-NLS-2$ 
        lm.add("false", false); 
        assertEquals(2, lm.size());
        assertTrue(lm.get("true"));
        assertTrue(lm.get("yes"));
        assertTrue(lm.get(0));
        assertFalse(lm.get(1));
        assertFalse(lm.get("false"));
        List<Boolean> c = new ArrayList<Boolean>();
        c.add(false);
        lm.retainAll(c);
        assertFalse(lm.get(0));
        assertEquals(1, lm.size());
        assertFalse(lm.get("false"));
        assertNull(lm.get("true"));
        assertNull(lm.get("yes"));
        c.remove(0);
        lm.retainAll(c);
        assertEquals(0, lm.size());
        assertNull(lm.get("false"));
        c.add(true);
        lm.add(true);
        lm.add(true);
        assertEquals(2, lm.size());
        lm.retainAll(c);
        assertEquals(2, lm.size());
        assertTrue(lm.get(0));
        assertTrue(lm.get(1));
    }
    
    /**
     * Set the value of an index to a new value.
     */
    @Test
    public void set_index_Value() {
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.add("0", 100);
        lm.add("1", 101);
        lm.add("2", 102);
        for (Integer i = 0; i < lm.size(); i++) {
            assertEquals(new Integer(i + 100), lm.get(i.toString()));
            assertEquals(new Integer(i + 100), lm.get(i));
            lm.set(i, i);
            assertEquals(i, lm.get(i));
            assertEquals(i, lm.get(i.toString()));
        }
    }
    /**
     * Returns the number of values in our list
     */
    @Test
    public void size() {
        ListMap<Integer, Boolean> lm = new ListMap<Integer, Boolean>();
        assertEquals(lm.size(), 0);
        lm.add(true);
        assertEquals(lm.size(), 1);
        lm.add(false);
        assertEquals(lm.size(), 2);
    }
    /**
     * @see java.util.List#subList(int, int)
     */
    @Test
    public void subList() {
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.add("0", 0);
        lm.add("1", 1);
        lm.add("2", 2);
        lm.add("3", 3);
        lm.add("4", 4);
        int start = 1;
        int end = 4;
        List<Integer> sl = lm.subList(start, end);
        assertEquals(end - start, sl.size());
        for (int i = 0; i < sl.size(); i++) {
            assertEquals(new Integer(i + start), sl.get(i)); 
        }
    }
    /**
     * @see java.util.List#toArray()
     */
    @Test
    public void toArray() {
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.add("0", 0);
        lm.add("1", 1);
        lm.add("2", 2);
        lm.add("3", 3);
        lm.add("4", 4);
        Integer[] correct = {0, 1, 2, 3, 4};
        assertArrayEquals(correct, lm.toArray());
    }

    /**
     * @see java.util.List#toArray(Object[])
     */
    @Test
    public void toArray_TypeArray() {
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.add("0", 0);
        lm.add("1", 1);
        lm.add("2", 2);
        lm.add("3", 3);
        lm.add("4", 4);
        Integer[] correct = {0, 1, 2, 3, 4};
        assertArrayEquals(correct, lm.toArray(new Integer[0]));
    }

    /**
     * @see java.util.List#toArray(Object[])
     */
    @Test(expected = ArrayStoreException.class)
    public void toArray_TypeArray2() {
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.add("0", 0);
        lm.add("1", 1);
        lm.add("2", 2);
        lm.add("3", 3);
        lm.add("4", 4);
        Integer[] correct = {0, 1, 2, 3, 4};
        Byte[] invalid = new Byte[5];
        assertArrayEquals(correct, lm.toArray(invalid));
    }

    /**
     * Return a collection of just the values.
     */
    @Test
    public void values() {
        ListMap<String, Integer> lm = new ListMap<String, Integer>();
        lm.add("0", 0);
        lm.add("1", 1);
        lm.add("2", 2);
        lm.add("3", 3);
        lm.add("4", 4);
        Integer[] correct = {0, 1, 2, 3, 4};
        Collection<Integer> got = lm.values();
        assertEquals(lm.size(), got.size());
        Integer[] gotarr = new Integer[0];
        gotarr = got.toArray(gotarr);
        assertArrayEquals(correct, gotarr);
    }

}
