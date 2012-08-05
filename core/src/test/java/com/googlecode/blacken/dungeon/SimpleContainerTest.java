package com.googlecode.blacken.dungeon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.blacken.core.Coverage;
import com.googlecode.blacken.core.Covers;

import static org.junit.Assert.*;

/**
 *
 * @author Steven Black
 */
public class SimpleContainerTest {

    public SimpleContainerTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    @Covers("public SimpleContainer(ThingTypeCheck<T>,int)")
    public void testConstructor_ThingTypeCheck_Size() {
        SimpleContainer<Integer> instance = new SimpleContainer<>(null, -2);
        assertNull(instance.getVerifier());
        assertEquals(-1, instance.getSizeLimit());
        assertTrue(instance.isEmpty());
    }

    @Test
    @Covers("public SimpleContainer(ThingTypeCheck<T>)")
    public void testConstructor_ThingTypeCheck() {
        SimpleContainer<Integer> instance = new SimpleContainer<>(null);
        assertNull(instance.getVerifier());
        assertEquals(-1, instance.getSizeLimit());
        assertTrue(instance.isEmpty());
    }

    @Test
    @Covers("public int hashCode()")
    public void testHashCode() {
        SimpleContainer<Integer> first = new SimpleContainer<>();
        assertNull(first.getVerifier());
        assertEquals(-1, first.getSizeLimit());
        assertTrue(first.isEmpty());
        SimpleContainer<Integer> second = new SimpleContainer<>(null, -2);
        assertNull(second.getVerifier());
        assertEquals(-1, second.getSizeLimit());
        assertTrue(second.isEmpty());
        assertNotSame(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        first.setSizeLimit(2);
        assertTrue("Dissimilar objects have the same hashCode",
                first.hashCode() != second.hashCode());
    }

    @Test
    @Covers("public boolean equals(Object)")
    public void testEquals() {
        SimpleContainer<Integer> first = new SimpleContainer<>();
        assertNull(first.getVerifier());
        assertEquals(-1, first.getSizeLimit());
        assertTrue(first.isEmpty());
        SimpleContainer<Integer> second = new SimpleContainer<>(null, -2);
        assertNull(second.getVerifier());
        assertEquals(-1, second.getSizeLimit());
        assertTrue(second.isEmpty());
        assertNotSame(first, second);
        assertEquals(first, second);
        assertFalse(first.equals(new Object()));
    }

    @Test
    @Covers("public SimpleContainer()")
    public void testConstructor() {
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        assertNull(instance.getVerifier());
        assertEquals(-1, instance.getSizeLimit());
        assertTrue(instance.isEmpty());
    }

    @Test
    @Covers("public boolean canFit(T)")
    public void testCanFit() {
        Integer thing = null;
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        boolean expResult = true;
        boolean result = instance.canFit(thing);
        assertEquals(expResult, result);
        thing = 42;
        result = instance.canFit(thing);
        assertEquals(expResult, result);
        SimpleIntegerCheck check = new SimpleIntegerCheck(1, 20);
        instance.setVerifier(check);
        expResult = false;
        thing = null;
        result = instance.canFit(thing);
        assertEquals(expResult, result);
        thing = 42;
        result = instance.canFit(thing);
        assertEquals(expResult, result);
        expResult = true;
        thing = 1;
        result = instance.canFit(thing);
        assertEquals(expResult, result);
        thing = 20;
        result = instance.canFit(thing);
        assertEquals(expResult, result);
    }

    /**
     * Test of findSimilar method, of class SimpleContainer.
     */
    @Test
    @Covers("public Collection<T> findSimilar(ThingTypeCheck<T>)")
    public void testFindSimilar() {
        ThingTypeCheck<Integer> judge = new SimpleIntegerCheck(1, 20);
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        instance.add(0);
        instance.add(1);
        instance.add(20);
        instance.add(21);
        instance.add(42);
        Collection<Integer> expResult = new ArrayList<>();
        expResult.add(1);
        expResult.add(20);
        Collection result = instance.findSimilar(judge);
        assertEquals(expResult, result);
    }

    @Test
    @Covers({"public void setVerifier(ThingTypeCheck<T>)",
             "public ThingTypeCheck<T> getVerifier()"})
    public void testVerifier() {
        ThingTypeCheck<Integer> verifier = new SimpleIntegerCheck();
        SimpleContainer instance = new SimpleContainer();
        assertNull(instance.getVerifier());
        instance.setVerifier(verifier);
        assertSame(verifier, instance.getVerifier());
    }

    /**
     * Test of hasSizeLimit method, of class SimpleContainer.
     */
    @Test
    @Covers("public boolean hasSizeLimit()")
    public void testHasSizeLimit() {
        SimpleContainer instance = new SimpleContainer();
        boolean expResult = false;
        boolean result = instance.hasSizeLimit();
        assertEquals(expResult, result);
        instance.setSizeLimit(1);
        expResult = true;
        result = instance.hasSizeLimit();
        assertEquals(expResult, result);
    }

    @Test
    @Covers({"public int getSizeLimit()",
        "public int setSizeLimit(int) throws IllegalStateException",
    })
    public void testGetSizeLimit() {
        SimpleContainer instance = new SimpleContainer();
        int expResult = -1;
        int result = instance.getSizeLimit();
        assertEquals(expResult, result);
        instance.add(new Object());
        instance.setSizeLimit(2);
        assertEquals(2, instance.getSizeLimit());
        instance.add(new Object());
        try {
            instance.add(new Object());
            fail("size limit not in effect");
        } catch(IllegalStateException e) {
            // correct.
        }
    }

    @Test
    @Covers("public boolean add(T)")
    public void testAdd() {
        // We also test this further elsewhere
        Object o = new Integer(10);
        SimpleContainer instance = new SimpleContainer();
        assertTrue(instance.add(o));
    }

    @Test
    @Covers("public boolean offer(T)")
    public void testOffer() {
        Integer o = 0;
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        assertTrue(instance.offer(o));
        instance.setVerifier(new SimpleIntegerCheck(1, 20));
        assertTrue(instance.offer(1));
        assertTrue(instance.offer(20));
        assertFalse(instance.offer(0));
        assertFalse(instance.offer(21));
    }

    @Test
    @Covers("public int size()")
    public void testSize() {
        SimpleContainer instance = new SimpleContainer();
        assertEquals(0, instance.size());
        instance.add(new Integer(2));
        assertEquals(1, instance.size());
    }

    @Test
    @Covers("public boolean isEmpty()")
    public void testIsEmpty() {
        SimpleContainer instance = new SimpleContainer();
        assertTrue(instance.isEmpty());
        instance.add(new Integer(2));
        assertFalse(instance.isEmpty());
    }

    @Test
    @Covers("public boolean contains(Object)")
    public void testContains() {
        Object o = new Integer(5);
        SimpleContainer instance = new SimpleContainer();
        boolean expResult = false;
        boolean result = instance.contains(o);
        assertEquals(expResult, result);
        instance.add(o);
        expResult = true;
        result = instance.contains(o);
        assertEquals(expResult, result);
    }

    @Test
    @Covers("public Iterator<T> iterator()")
    public void testIterator() {
        // XXX this is skimpy; knowing the implementation, we don't care
        SimpleContainer instance = new SimpleContainer();
        Iterator result = instance.iterator();
        assertNotNull(result);
        assertFalse(result.hasNext());
    }


    @Test
    @Covers("public T[] toArray()")
    public void testToArray_0args() {
        SimpleContainer instance = new SimpleContainer();
        Object value = new Object();
        instance.add(value);
        Object[] expResult = new Object[] {value, };
        Object[] result = instance.toArray();
        assertArrayEquals(expResult, result);
        assertEquals(1, result.length);
    }

    /**
     * Test of toArray method, of class SimpleContainer.
     */
    @Test
    @Covers("public <T> T[] toArray(T[])")
    public void testToArray_ObjectArr() {
        Object[] a = new Object[] {};
        Object value = new Object();
        SimpleContainer instance = new SimpleContainer();
        instance.add(value);
        Object[] expResult = new Object[] {value, };
        Object[] result = instance.toArray(a);
        assertArrayEquals(expResult, result);
    }

    @Test
    @Covers("public boolean remove(Object)")
    public void testRemove() {
        Object o = new Integer(5);
        SimpleContainer instance = new SimpleContainer();
        assertTrue(instance.add(o));
        assertTrue(instance.contains(o));

        boolean expResult = true;
        boolean result = instance.remove(o);
        assertEquals(expResult, result);

        expResult = false;
        result = instance.remove(o);
        assertEquals(expResult, result);
    }

    @Test
    @Covers("public boolean containsAll(Collection)")
    public void testContainsAll() {
        Collection<Integer> c = new ArrayList<>();
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        c.add(42);
        instance.add(1);
        c.add(1);
        instance.add(42);
        assertTrue(instance.containsAll(c));
    }

    @Test
    @Covers("public boolean addAll(Collection)")
    public void testAddAll() {
        Collection<Integer> c = new ArrayList<>();
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        c.add(42);
        c.add(1);
        assertFalse(instance.containsAll(c));
        assertTrue(instance.isEmpty());
        assertTrue(instance.addAll(c));
        assertTrue(instance.containsAll(c));
    }

    @Test
    @Covers("public boolean removeAll(Collection)")
    public void testRemoveAll() {
        Collection<Integer> c = new ArrayList<>();
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        c.add(42);
        c.add(1);
        assertTrue(instance.addAll(c));
        assertTrue(instance.containsAll(c));
        assertTrue(instance.removeAll(c));
        assertFalse(instance.containsAll(c));
        assertTrue(instance.isEmpty());
    }

    @Test
    @Covers("public boolean retainAll(Collection)")
    public void testRetainAll() {
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        Collection<Integer> c1 = new ArrayList<>();
        c1.add(42);
        c1.add(1);
        Collection<Integer> c2 = new ArrayList<>();
        c2.add(19);
        c2.add(0);
        assertTrue(instance.addAll(c1));
        assertTrue(instance.containsAll(c1));
        assertTrue(instance.addAll(c2));
        assertTrue(instance.containsAll(c2));
        assertTrue(instance.retainAll(c2));
        assertFalse(instance.containsAll(c1));
        assertTrue(instance.containsAll(c2));
    }

    @Test
    @Covers("public void clear()")
    public void testClear() {
        Collection<Integer> c = new ArrayList<>();
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        c.add(42);
        c.add(1);
        assertTrue(instance.addAll(c));
        assertTrue(instance.containsAll(c));
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    @Covers("public boolean hasUnfit()")
    public void testHasUnfit() {
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        instance.add(42);
        assertFalse(instance.hasUnfit());
        SimpleIntegerCheck check = new SimpleIntegerCheck(1, 20);
        instance.setVerifier(check);
        assertTrue(instance.hasUnfit());
    }

    @Test
    @Covers("public Collection<T> removeUnfit()")
    public void testRemoveUnfit() {
        SimpleContainer<Integer> instance = new SimpleContainer<>();
        instance.add(42);
        Collection<Integer> unfit = instance.removeUnfit();
        assertTrue(unfit.isEmpty());
        assertFalse(instance.isEmpty());
        SimpleIntegerCheck check = new SimpleIntegerCheck(1, 20);
        instance.setVerifier(check);
        unfit = instance.removeUnfit();
        assertFalse(unfit.isEmpty());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testCoverage() {
        Coverage.checkCoverage(SimpleContainer.class, this.getClass());
    }

}
