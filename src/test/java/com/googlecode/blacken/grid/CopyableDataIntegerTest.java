package com.googlecode.blacken.grid;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * CopyableData is designed for simple data types that already support the
 * Cloneable interface. As such, the tests included here should all pass.
 * 
 * @author Steven Black
 *
 */
public class CopyableDataIntegerTest {
    CopyableData<Integer> sample = null;
    
    @Before
    public void setUp() {
        sample = new CopyableData<Integer>(1);
    }
    
    @Test
    public void testDirectAccess() {
        assertTrue(sample.data == 1);
    }
    
    @Test
    public void testGetDataNE() {
        assertFalse(sample.getData() == 2);
    }
    
    @Test
    public void testDataEqualsT() {
        CopyableData<Integer> sample2 = new CopyableData<Integer>(1);
        assertTrue(sample.getData().equals(sample2));
    }
    
    @Test
    public void testDataEqualsI() {
        assertTrue(sample.getData().equals(1));
    }

    @Test
    public void testNotDataEqualsT() {
        CopyableData<Integer> sample2 = new CopyableData<Integer>(2);
        assertTrue(!sample.equals(sample2));
    }

    @Test
    public void testNotDataEqualsI() {
        assertTrue(!sample.equals(2));
    }

    @Test
    public void testGetDataEQ() {
        assertTrue(sample.getData() == 1);
    }
    
    @Test
    public void testSetData() {
        assertTrue(sample.getData() == 1);
        sample.setData(2);
        assertTrue(sample.getData() == 2);
    }
    
    @Test
    public void setData_null() {
        assertTrue(sample.getData() == 1);
        sample.setData(null);
        assertSame(sample.getData(), null);
    }

    @Test
    public void testStaticCopy() {
        CopyableData<Integer> newSample = null;
        // make sure it is set up as we expect
        assertTrue(sample.getData() == 1);
        // copy it
        newSample = CopyableData.copy(sample);
        // Make sure they're not secretly the same
        assertNotSame(sample, newSample);
        // make sure the copy has what we expect
        assertTrue(newSample.getData() == 1);
        // change the original
        sample.setData(2);
        // make sure it didn't change the copy
        assertTrue(newSample.getData() == 1);
        // change the copy
        newSample.setData(3);
        // make sure it didn't change the original
        assertTrue(sample.getData() == 2);
        // make sure we really changed the copy
        assertTrue(newSample.getData() == 3);
    }

    @Test
    public void testCopy() {
        CopyableData<Integer> newSample = null;
        // make sure it is set up as we expect
        assertTrue(sample.getData() == 1);
        // copy it
        newSample = sample.copy();
        // Make sure they're not secretly the same
        assertNotSame(sample, newSample);
        // make sure the copy has what we expect
        assertTrue(newSample.getData() == 1);
        // change the original
        sample.setData(2);
        // make sure it didn't change the copy
        assertTrue(newSample.getData() == 1);
        // change the copy
        newSample.setData(3);
        // make sure it didn't change the original
        assertTrue(sample.getData() == 2);
        // make sure we really changed the copy
        assertTrue(newSample.getData() == 3);
    }

    @Test
    public void testClone() {
        CopyableData<Integer> newSample = null;
        // make sure it is set up as we expect
        assertTrue(sample.getData() == 1);
        // copy it
        newSample = sample.clone();
        // Make sure they're not secretly the same
        assertNotSame(sample, newSample);
        // make sure the copy has what we expect
        assertTrue(newSample.getData() == 1);
        // change the original
        sample.setData(2);
        // make sure it didn't change the copy
        assertTrue(newSample.getData() == 1);
        // change the copy
        newSample.setData(3);
        // make sure it didn't change the original
        assertTrue(sample.getData() == 2);
        // make sure we really changed the copy
        assertTrue(newSample.getData() == 3);
    }
}
