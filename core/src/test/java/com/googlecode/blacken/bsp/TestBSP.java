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

package com.googlecode.blacken.bsp;

import static org.junit.Assert.*;

import org.junit.*;
import java.util.List;

/**
 * 	Unit test for BSPTree.
 *	Basic get methods are not tested.
 *	@author XLambda
 */
 
public class TestBSP {
    BSPTree t;
    
    /**
     * test
     */
    @Before
    public void setUp() {
        t = new BSPTree(0,0,80,25);
		t.splitOnce(true,10);
    }

    /**
     * test
     */
    @Test
    public void testIsLeaf() {
		assertFalse(t.isLeaf());
		assertTrue(t.getLeftChild().isLeaf());
    }
    
    /**
     * test
     */
    @Test
    public void testContains() {
			assertTrue(t.contains(0,0));
			assertTrue(t.contains(24,79));
			assertTrue(t.contains(1,1));
			assertFalse(t.contains(-1,-1));
			assertFalse(t.contains(25,80));
	}

    /**
     * test
     */
    @Test
    public void testFindNode() {
		BSPTree node = t.findNode(1,1);
		if(t.getLeftChild().contains(1,1)){
			assertSame(node,t.getLeftChild());
			assertNotSame(node,t.getRightChild());
		}else if(t.getRightChild().contains(1,1)){
			assertSame(node,t.getRightChild());
			assertNotSame(node,t.getLeftChild());
		}
	}

    /**
     * test
     */
    @Test
    public void testTraversePreorder() {
		List<BSPTree> nodelist = t.traversePreorder(null);
		assertSame(t,nodelist.get(0));
		assertSame(t.getLeftChild(),nodelist.get(1));
		assertSame(t.getRightChild(),nodelist.get(2));
    }

    /**
     * test
     */
    @Test
    public void testTraverseInorder() {
		List<BSPTree> nodelist = t.traverseInorder(null);
		assertSame(t.getLeftChild(),nodelist.get(0));
		assertSame(t,nodelist.get(1));
		assertSame(t.getRightChild(),nodelist.get(2));
    }

    /**
     * test
     */
    @Test
    public void testTraversePostorder() {
		List<BSPTree> nodelist = t.traversePostorder(null);
		assertSame(t.getLeftChild(),nodelist.get(0));
		assertSame(t.getRightChild(),nodelist.get(1));
		assertSame(t,nodelist.get(2));
    }
    
    /**
     * test
     */
    @Test
    public void testTraverseLevelOrder() {
		List<BSPTree> nodelist = t.traverseLevelOrder(null);
		assertSame(t,nodelist.get(0));
		assertSame(t.getLeftChild(),nodelist.get(1));
		assertSame(t.getRightChild(),nodelist.get(2));
    }
    
    /**
     * test
     */
    @Test
    public void testTraverseInvertedLevelOrder() {
		List<BSPTree> nodelist = t.traverseInvertedLevelOrder(null);
		assertSame(t.getRightChild(),nodelist.get(0));		
		assertSame(t.getLeftChild(),nodelist.get(1));
		assertSame(t,nodelist.get(2));
    }
    
    /**
     * test
     */
    @Test
    public void testSplitOnce() {
		t.splitOnce(false, 10);
		assertFalse(t.isHorizontal());
		assertEquals(t.getPosition(),10);
    }

}
