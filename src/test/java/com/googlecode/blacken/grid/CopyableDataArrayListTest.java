package com.googlecode.blacken.grid;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

/**
 * CopyableData is designed for simple data types that already support the
 * Cloneable interface. This tests things which will not work. We make sure
 * they do not work (as expected).
 * 
 * Specifically, we perform shallow copies (like clone). If functions don't
 * explicitly do something different this is what we expect.
 * 
 * @author yam655
 *
 */
public class CopyableDataArrayListTest {
    ArrayList<Integer> data1 = null;
    ArrayList<Integer> data2 = null;
    ArrayList<Integer> dataN1 = null;
    CopyableData<ArrayList<Integer>> sample = null;
    
    /**
     * test
     */
    @Before
    public void setUp() {
        data1 = new ArrayList<Integer>();
        data2 = new ArrayList<Integer>();
        dataN1 = new ArrayList<Integer>();
        data1.add(1);
        data2.add(2);
        dataN1.add(1);
        sample = new CopyableData<ArrayList<Integer>>(data1);
    }
    
    /**
     * test
     */
    @Test
    public void data_DirectAccess() {
        assertSame(sample.data, data1);
    }
    
    /**
     * test
     */
    @Test
    public void getData_Same() {
        assertSame(sample.getData(), data1);
    }

    /**
     * test
     */
    @Test
    public void dataEquals_CopyableData_SameData() {
        CopyableData<ArrayList<Integer>> sample2 = new CopyableData<ArrayList<Integer>>(data1);
        assertEquals(sample.getData(),sample2.getData());
    }
    
    /**
     * test
     */
    @Test
    public void dataEquals_T_SameData() {
        assertTrue(sample.getData().equals(data1));
    }
    
    /**
     * test
     */
    @Test
    public void dataEquals_CopyableData_SameDataDifferentList() {
        assertTrue(sample.getData().equals(dataN1));
    }

    /**
     * test
     */
    @Test
    public void dataEquals_T_SameDataDifferentList() {
        CopyableData<ArrayList<Integer>> sample2 = new CopyableData<ArrayList<Integer>>(dataN1);
        assertTrue(sample.equals(sample2));
        assertTrue(sample.getData().equals(sample2.getData()));
    }
    
    /**
     * test
     */
    @Test
    public void dataEquals_CopyableData_DifferentData() {
        CopyableData<ArrayList<Integer>> sample2 = new CopyableData<ArrayList<Integer>>(data2);
        assertFalse(sample.getData().equals(sample2));
    }

    /**
     * test
     */
    @Test
    public void dataEquals_T_DifferentData() {
        assertFalse(sample.getData().equals(data2));
    }
    
    /**
     * test
     */
    @Test
    public void setData() {
        assertSame(sample.getData(), data1);
        sample.setData(data2);
        assertSame(sample.getData(), data2);
    }
    
    /**
     * test
     */
    @Test
    public void setData_null() {
        assertSame(sample.getData(), data1);
        sample.setData(null);
        assertSame(sample.getData(), null);
    }

    /**
     * test
     */
    @Test
    public void copy_static_CopyableData() {
        CopyableData<ArrayList<Integer>> newSample = null;
        ArrayList<Integer> data3 = new ArrayList<Integer>();
        data3.add(3);
        // make sure it is set up as we expect
        assertSame(sample.getData(), data1);
        // copy it
        newSample = CopyableData.copy(sample);
        // Make sure they're not secretly the same
        assertNotSame(sample, newSample);
        // make sure the copy has what we expect
        assertSame(newSample.getData(), data1);
        // change the original
        sample.setData(data2);
        // make sure it didn't change the copy
        assertSame(newSample.getData(), data1);
        // change the copy
        newSample.setData(data3);
        // make sure it didn't change the original
        assertSame(sample.getData(), data2);
        // make sure we really changed the copy
        assertSame(newSample.getData(), data3);
    }

    /**
     * test
     */
    @Test
    public void copy() {
        CopyableData<ArrayList<Integer>> newSample = null;
        ArrayList<Integer> data3 = new ArrayList<Integer>();
        data3.add(3);
        // make sure it is set up as we expect
        assertSame(sample.getData(), data1);
        // copy it
        newSample = sample.copy();
        // Make sure they're not secretly the same
        assertNotSame(sample, newSample);
        // make sure the copy has what we expect
        assertSame(newSample.getData(), data1);
        // change the original
        sample.setData(data2);
        // make sure it didn't change the copy
        assertSame(newSample.getData(), data1);
        // change the copy
        newSample.setData(data3);
        // make sure it didn't change the original
        assertSame(sample.getData(), data2);
        // make sure we really changed the copy
        assertSame(newSample.getData(), data3);
    }

    /**
     * test
     */
    @Test
    public void testClone() {
        CopyableData<ArrayList<Integer>> newSample = null;
        ArrayList<Integer> data3 = new ArrayList<Integer>();
        data3.add(3);
        // make sure it is set up as we expect
        assertSame(sample.getData(), data1);
        // copy it
        newSample = sample.clone();
        // Make sure they're not secretly the same
        assertNotSame(sample, newSample);
        // make sure the copy has what we expect
        assertSame(newSample.getData(), data1);
        // change the original
        sample.setData(data2);
        // make sure it didn't change the copy
        assertSame(newSample.getData(), data1);
        // change the copy
        newSample.setData(data3);
        // make sure it didn't change the original
        assertSame(sample.getData(), data2);
        // make sure we really changed the copy
        assertSame(newSample.getData(), data3);
    }
}
