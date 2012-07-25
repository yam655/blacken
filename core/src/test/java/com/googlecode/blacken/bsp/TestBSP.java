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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.*;

import com.googlecode.blacken.core.Coverage;
import com.googlecode.blacken.core.Covers;
import com.googlecode.blacken.core.Random;
import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.RegionIterator;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.grid.SimpleSize;
import com.googlecode.blacken.grid.Sizable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 *  Unit test for BSPTree.
 *  Basic get methods are not tested.
 *  @author XLambda
 */

public class TestBSP {
    private static Logger LOGGER = LoggerFactory.getLogger(TestBSP.class);
    private BSPTree t;
    private static final int NUM_ROWS = 25;
    private static final int NUM_COLS = 80;
    private static final int SPLIT_POS = 10;
    private static final boolean IS_HORIZ = true;
    private static final int START_Y = 0;
    private static final int START_X = 0;

    @Before
    @Covers("public BSPTree(int,int,int,int)")
    public void setUp() {
        t = new BSPTree(NUM_ROWS, NUM_COLS, START_Y, START_X);
        t.splitOnce(IS_HORIZ,SPLIT_POS);
    }

    @Test
    @Covers("public boolean isLeaf()")
    public void testIsLeaf() {
        assertFalse(t.isLeaf());
        assertTrue(t.getLeftChild().isLeaf());
    }

    @Test
    @Covers("public boolean contains(int,int)")
    public void testContains() {
        assertTrue(t.contains(START_Y,START_X));
        assertTrue(t.contains(NUM_ROWS-1,NUM_COLS-1));
        assertTrue(t.contains(START_Y+1,START_X+1));
        assertFalse(t.contains(START_Y-1,START_X-1));
        assertFalse(t.contains(NUM_ROWS,NUM_COLS));
    }

    @Test
    @Covers({"public BSPTree findNode(int,int)",
             "public BSPTree getRightChild()",
             "public BSPTree getLeftChild()",

    })
    public void testFindNode() {
        BSPTree node = t.findNode(START_Y+1,START_X+1);
        if(t.getLeftChild().contains(START_Y+1,START_X+1)) {
            assertSame(node,t.getLeftChild());
            assertNotSame(node,t.getRightChild());
        } else if(t.getRightChild().contains(START_Y+1,START_X+1)) {
            assertSame(node,t.getRightChild());
            assertNotSame(node,t.getLeftChild());
        }
    }
    @Test
    @Covers({"public List<BSPTree> traversePreorder(List<BSPTree>)",
             "public BSPTree getRightChild()",
             "public BSPTree getLeftChild()",
    })
    public void testTraversePreorder() {
        List<BSPTree> nodelist = t.traversePreorder(null);
        assertSame(t,nodelist.get(0));
        assertSame(t.getLeftChild(),nodelist.get(1));
        assertSame(t.getRightChild(),nodelist.get(2));
    }

    @Test
    @Covers("public List<BSPTree> traverseInorder(List<BSPTree>)")
    public void testTraverseInorder() {
        List<BSPTree> nodelist = t.traverseInorder(null);
        assertSame(t.getLeftChild(),nodelist.get(0));
        assertSame(t,nodelist.get(1));
        assertSame(t.getRightChild(),nodelist.get(2));
    }

    @Test
    @Covers("public List<BSPTree> traversePostorder(List<BSPTree>)")
    public void testTraversePostorder() {
        List<BSPTree> nodelist = t.traversePostorder(null);
        assertSame(t.getLeftChild(),nodelist.get(0));
        assertSame(t.getRightChild(),nodelist.get(1));
        assertSame(t,nodelist.get(2));
    }

    @Test
    @Covers("public List<BSPTree> traverseLevelOrder(List<BSPTree>)")
    public void testTraverseLevelOrder() {
        List<BSPTree> nodelist = t.traverseLevelOrder(null);
        assertSame(t,nodelist.get(0));
        assertSame(t.getLeftChild(),nodelist.get(1));
        assertSame(t.getRightChild(),nodelist.get(2));
    }

    @Test
    @Covers("public List<BSPTree> traverseInvertedLevelOrder(List<BSPTree>)")
    public void testTraverseInvertedLevelOrder() {
        List<BSPTree> nodelist = t.traverseInvertedLevelOrder(null);
        assertSame(t.getRightChild(),nodelist.get(0));
        assertSame(t.getLeftChild(),nodelist.get(1));
        assertSame(t,nodelist.get(2));
    }


    @Test
    @Covers({"public void splitRecursive(Random,int,int,int,double,double)",
             "public BSPTree(BSPTree,boolean)",
    })
    public void testSplitRecursive() {
        Random rng = new Random(987654321);
        t.splitRecursive(rng, 5, 3, 3, 5.0, 3.0);
        Collection<BSPTree> s = t.findLevelOrder(null);
        List<BSPTree> sample = new ArrayList<>(s);
        //     protected BSPTree(int height, int width, int y, int x, int level, int position, boolean horizontal) {

        BSPTree good = new BSPTree(25, 80, 0, 0, 0, 10, true);
        assertEquals(good, sample.get(0));
        good = new BSPTree(15,80, 10, 0, 1, 13, false);
        assertEquals(good, sample.get(2));
        good = new BSPTree(15,13, 10, 0, 2, 3, false);
        assertEquals(good, sample.get(5));
        good = new BSPTree(12,41, 13, 39, 5, -1, false);
        assertEquals(good, sample.get(sample.size()-1));
    }

    @Test
    @Covers("public void setBounds(int,int,int,int)")
    public void testSetBounds() {
        assertTrue(t.contains(START_Y, START_X));
        assertTrue(t.getLeftChild().contains(START_Y, START_X));
        assertFalse(t.getRightChild().contains(START_Y, START_X));
        t.setBounds(START_Y+1, START_X+1, NUM_ROWS-2, NUM_COLS-2);
        assertFalse(t.contains(START_Y, START_X));
        assertFalse(t.getLeftChild().contains(START_Y, START_X));
        assertFalse(t.getRightChild().contains(START_Y, START_X));
    }

    @Test
    @Covers({"public BSPTree getFather()",
             "public BSPTree getParent()",
    })
    public void testGetParent() {
        assertSame(t, t.getLeftChild().getParent());
        t.getRightChild().splitOnce(false, SPLIT_POS/2);
    }
    @Test
    @Covers("public int getLevel()")
    public void testGetLevel() {
        assertEquals(0, t.getLevel());
        assertEquals(1, t.getLeftChild().getLevel());
    }

    @Test
    @Covers("public int getWidth()")
    public void testGetWidth() {
        assertEquals(NUM_COLS, t.getWidth());
    }

    @Test
    @Covers("public int getY()")
    public void testGetY() {
        assertEquals(START_Y, t.getY());
    }
    @Test
    @Covers("public int getSplitPosition()")
    public void testSplitPosition() {
        assertEquals(SPLIT_POS, t.getSplitPosition());
    }
    @Test
    @Covers("public int getX()")
    public void testGetX() {
        assertEquals(START_X, t.getX());
    }
    @Test
    @Covers("public int getHeight()")
    public void testGetHeight() {
        assertEquals(NUM_ROWS, t.getHeight());
    }

    @Test
    @Covers({"public void splitOnce(boolean,int)",
        "public boolean isHorizontal()",
    })
    public void testSplitOnce() {
        t.getLeftChild().splitOnce(false, TestBSP.SPLIT_POS);
        assertFalse(t.getLeftChild().isHorizontal());
        assertEquals(t.getLeftChild().getSplitPosition(), TestBSP.SPLIT_POS);
    }


    @Test
    @Covers("public void setPosition(Positionable)")
    public void setPosition_pos() {
        try {
            t.setPosition(new Point(1,1));
            fail("Should have failed to move it.");
        } catch(UnsupportedOperationException e) {
            // do nothing
        }
    }
    @Test
    @Covers("public void setY(int)")
    public void setY() {
        try {
            t.setY(1);
            fail("Should have failed to move it.");
        } catch(UnsupportedOperationException e) {
            // do nothing
        }
    }
    @Test
    @Covers("public void setPosition(int,int)")
    public void setPosition_y_x() {
        try {
            t.setPosition(1, 1);
            fail("Should have failed to move it.");
        } catch(UnsupportedOperationException e) {
            // do nothing
        }
    }
    @Test
    @Covers("public void setX(int)")
    public void setX() {
        try {
            t.setX(1);
            fail("Should have failed to move it.");
        } catch(UnsupportedOperationException e) {
            // do nothing
        }
    }
    @Test
    @Covers("public void setSize(int,int)")
    public void setSize_h_w() {
        try {
            t.setSize(24, 81);
            fail("Should have failed to resize it.");
        } catch(UnsupportedOperationException e) {
            // do nothing
        }
    }
    @Test
    @Covers("public boolean contains(Positionable)")
    public void contains_p() {
        assertTrue(t.contains(new Point(START_Y, START_X)));
        assertFalse(t.contains(new Point(START_Y -1, START_X-1)));
        assertTrue(t.contains(new Point(START_Y + NUM_ROWS -1, START_X + NUM_COLS -1)));
        assertFalse(t.contains(new Point(START_Y + NUM_ROWS, START_X + NUM_COLS)));
    }

    @Test
    @Covers("public boolean contains(int,int,int,int)")
    public void containsHWYX() {
        assertTrue(t.contains(NUM_ROWS, NUM_COLS, START_Y, START_X));
        assertTrue(t.contains(NUM_ROWS -2, NUM_COLS -2, START_Y + 1, START_X +1));
        assertFalse(t.contains(NUM_ROWS, NUM_COLS, START_Y +2, START_X+2));
    }
    @Test
    @Covers("public boolean contains(Regionlike)")
    public void contains_regionlike() {
        assertTrue(t.contains(new BoxRegion(NUM_ROWS, NUM_COLS, START_Y, START_X)));
        assertTrue(t.contains(new BoxRegion(NUM_ROWS -2, NUM_COLS -2, START_Y + 1, START_X +1)));
        assertFalse(t.contains(new BoxRegion(NUM_ROWS, NUM_COLS, START_Y +2, START_X+2)));
    }
    @Test
    @Covers("public Regionlike getBounds()")
    public void getBounds() {
        Regionlike expect = new BoxRegion(NUM_ROWS, NUM_COLS, START_Y, START_X);
        Regionlike got = t.getBounds();
        assertNotSame(got, t);
        assertEquals(expect, got);
    }
    @Test
    @Covers("public Positionable getPosition()")
    public void getPosition() {
        Positionable expect = new Point(START_Y, START_X);
        Positionable got = t.getPosition();
        assertNotSame(got, t);
        assertEquals(expect, got);
    }
    @Test
    @Covers("public boolean intersects(Regionlike)")
    public void intersectsR() {
        BoxRegion b = new BoxRegion(NUM_ROWS, NUM_COLS, START_Y, START_X);
        // entirely inside
        assertFalse(t.intersects(b));
        b = new BoxRegion(NUM_ROWS -2, NUM_COLS -2, START_Y + 1, START_X +1);
        assertFalse(t.intersects(b));
        // entirely outside
        b = new BoxRegion(NUM_ROWS, NUM_COLS, START_Y + NUM_ROWS, START_X);
        assertFalse(t.intersects(b));
        // intersects
        b = new BoxRegion(NUM_ROWS, NUM_COLS, START_Y +1, START_X+1);
        assertTrue(t.intersects(b));
        b = new BoxRegion(NUM_ROWS + 2, NUM_COLS+2, START_Y -1, START_X-1);
        assertTrue(t.intersects(b));
    }
    @Test
    @Covers("public RegionIterator getEdgeIterator()")
    public void getEdgeIterator() {
        RegionIterator iterator = t.getEdgeIterator();
        assertNotNull(iterator);
    }
    @Test
    @Covers("public RegionIterator getNotOutsideIterator()")
    public void getNotOutsideIterator() {
        RegionIterator iterator = t.getNotOutsideIterator();
        assertNotNull(iterator);
    }
    @Test
    @Covers("public RegionIterator getInsideIterator()")
    public void getInsideIterator() {
        RegionIterator iterator = t.getInsideIterator();
        assertNotNull(iterator);
    }

    @Test
    @Covers("public void setSize(Sizable)")
    public void setSize_s() {
        try {
            t.setSize(new SimpleSize(24, 81));
            fail("Should have failed to resize it.");
        } catch(UnsupportedOperationException e) {
            // do nothing
        }
    }
    @Test
    @Covers("public void setHeight(int)")
    public void setHeight() {
        try {
            t.setHeight(24);
            fail("Should have failed to resize it.");
        } catch(UnsupportedOperationException e) {
            // do nothing
        }
    }
    @Test
    @Covers("public void setWidth(int)")
    public void setWidth() {
        try {
            t.setWidth(81);
            fail("Should have failed to resize it.");
        } catch(UnsupportedOperationException e) {
            // do nothing
        }
    }
    @Test
    @Covers("public BSPTree(Regionlike)")
    public void constructor_Regionlike() {
        BSPTree trial = new BSPTree(new BoxRegion(NUM_ROWS, NUM_COLS, START_Y, START_X));
        trial.splitOnce(IS_HORIZ,SPLIT_POS);
        assertEquals(t, trial);
    }
    @Test
    @Covers("public boolean equals(Object)")
    public void equals_obj() {
        BSPTree trial = new BSPTree(new BoxRegion(NUM_ROWS, NUM_COLS, START_Y, START_X));
        trial.splitOnce(IS_HORIZ,SPLIT_POS);
        assertEquals(t, trial);
        Object dead = new Object();
        assertFalse(t.equals(dead));
    }
    @Test
    @Covers("public void setBounds(Regionlike)")
    public void testSetBounds_regionlike() {
        assertTrue(t.contains(START_Y, START_X));
        assertTrue(t.getLeftChild().contains(START_Y, START_X));
        assertFalse(t.getRightChild().contains(START_Y, START_X));
        t.setBounds(new BoxRegion(START_Y+1, START_X+1, NUM_ROWS-2, NUM_COLS-2));
        assertFalse(t.contains(START_Y, START_X));
        assertFalse(t.getLeftChild().contains(START_Y, START_X));
        assertFalse(t.getRightChild().contains(START_Y, START_X));
    }
    @Test
    @Covers("public String toString()")
    public void testToString() {
        String expect = "{Level: 0; Position: 0,0; Size: 25,80; Split: 10; " +
                "Horizontal; Level: 0;\n Right:{Level: 1; Position: 10,0; " +
                "Size: 15,80; Split: -1; Vertical; Level: 1;\n Right:-;\n " +
                "Left:-};\n Left:{Level: 1; Position: 0,0; Size: 10,80; " +
                "Split: -1; Vertical; Level: 1;\n Right:-;\n Left:-}}";
        assertEquals(expect, t.toString());
    }
    @Test
    @Covers("public int hashCode()")
    public void testHashCode() {
        assertEquals(-1930784615, t.hashCode());
    }

    @Test
    @Covers("public Sizable getSize()")
    public void getSize() {
        Sizable got = t.getSize();
        assertEquals(NUM_ROWS, got.getHeight());
        assertEquals(NUM_COLS, got.getWidth());
    }
    @Test
    @Covers("public boolean intersects(int,int,int,int)")
    public void intersects_HeightWidthYX() {
        assertFalse(t.intersects(NUM_ROWS, NUM_COLS, START_Y, START_X));
        assertFalse(t.intersects(NUM_ROWS -2, NUM_COLS -2, START_Y +1, START_X +1));
        assertTrue(t.intersects(NUM_ROWS +2, NUM_COLS +2, START_Y +1, START_X +1));
        assertFalse(t.intersects(NUM_ROWS, NUM_COLS, START_Y + NUM_ROWS, START_X));
        assertTrue(t.intersects(NUM_ROWS, NUM_COLS, START_Y+2, START_X));
    }

    @Test
    public void testCoverage() {
        // Parent is abstract, so we need to test it!
        Coverage.checkCoverageDeep(BSPTree.class, this.getClass());
    }

    @Test
    public void testSimpleBounds() {
        Sizable whole = t.getBounds();
        Sizable partA = t.getLeftChild().getBounds();
        Sizable partB = t.getRightChild().getBounds();
        if (t.isHorizontal()) {
            assertEquals(whole.getHeight(), partA.getHeight() + partB.getHeight());
            assertEquals(whole.getWidth(), partA.getWidth());
            assertEquals(whole.getWidth(), partB.getWidth());
        } else {
            assertEquals(whole.getWidth(), partA.getWidth() + partB.getWidth());
            assertEquals(whole.getHeight(), partA.getHeight());
            assertEquals(whole.getHeight(), partB.getHeight());
        }
        assertTrue(t.contains(START_Y + NUM_ROWS -1, START_X + NUM_ROWS -1));
        assertFalse(t.contains(START_Y + NUM_ROWS, START_X + NUM_ROWS));
    }

    @Test
    @Covers({"public void setContained(R)", "public R getContained()"})
    public void setContained() {
        Object sassy = new Object();
        try {
            t.setContained(sassy);
            if (!t.isLeaf()) {
                fail("failed to throw exception");
            }
        } catch(IllegalStateException e) {
            // do nothing
        }
        BSPTree r = t.getRightChild();
        r.setContained(sassy);
        assertNotNull(r.getContained());
        assertSame(sassy, r.getContained());
    }
    @Test
    @Covers("public Collection<BSPTree> findTopOrder(Collection<BSPTree>)")
    public void findTopOrder() {
        t.splitRecursive(new Random(1234556), 2, 4, 4, 5.0, 3.0);
        Collection<BSPTree> o = t.findTopOrder(null);
        String expect = "[{Level: 0; Position: 0,0; Size: 25,80; Split: 10; Horizontal; Level: 0;\n"
            + " Right:{Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}};\n"
            + " Left:{Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}}, {Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}]";
        assertEquals(expect, o.toString());
        // fail(o.toString().replaceAll("\n", "\\\\n\"\n    + \""));
    }
    @Test
    @Covers("public Collection<BSPTree> findLevelOrder(Collection<BSPTree>)")
    public void findLevelOrder() {
        t.splitRecursive(new Random(1234556), 2, 4, 4, 5.0, 3.0);
        Collection<BSPTree> o = t.findLevelOrder(null);
        String expect = "[{Level: 0; Position: 0,0; Size: 25,80; Split: 10; Horizontal; Level: 0;\n"
            + " Right:{Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}};\n"
            + " Left:{Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}}, {Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}]";
        assertEquals(expect, o.toString());
        // fail(o.toString().replaceAll("\n", "\\\\n\"\n    + \""));
    }
    @Test
    @Covers("public Collection<BSPTree> findLeaves(Collection<BSPTree>)")
    public void findLeaves() {
        t.splitRecursive(new Random(1234556), 2, 4, 4, 5.0, 3.0);
        Collection<BSPTree> o = t.findLeaves(null);
        String expect = "[{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}]";
        assertEquals(expect, o.toString());
        // fail(o.toString().replaceAll("\n", "\\\\n\"\n    + \""));
    }
    @Test
    @Covers("public Collection<R> findContained(Collection<R>)")
    public void findContained() {
        t.splitRecursive(new Random(1234556), 2, 4, 4, 5.0, 3.0);
        Collection<BSPTree> trees = t.findLeaves(null);
        Integer idx = 0;
        for (BSPTree bsp : trees) {
            bsp.setContained(idx++);
        }
        Collection<Integer> ints = t.findContained(null);
        idx = 0;
        for (Integer i : ints) {
            assertEquals(idx++, i);
        }
    }
    @Test
    @Covers("public Collection<BSPTree> findInOrder(Collection<BSPTree>)")
    public void findInOrder() {
        t.splitRecursive(new Random(1234556), 2, 4, 4, 5.0, 3.0);
        Collection<BSPTree> o = t.findInOrder(null);
        String expect = "[{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 0; Position: 0,0; Size: 25,80; Split: 10; Horizontal; Level: 0;\n"
            + " Right:{Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}};\n"
            + " Left:{Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}}, {Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}]";
        assertEquals(expect, o.toString());
        // fail(o.toString().replaceAll("\n", "\\\\n\"\n    + \""));
    }
    @Test
    @Covers("public void splitRecursive(Random,int,int,int)")
    public void splitRecursiveSimple() {
        t.splitRecursive(new Random(1234556), 2, 4, 4, 5.0, 3.0);
        Collection<BSPTree> o = t.findInOrder(null);
        String expect = "[{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 0; Position: 0,0; Size: 25,80; Split: 10; Horizontal; Level: 0;\n"
            + " Right:{Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}};\n"
            + " Left:{Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}}, {Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}]";
        assertEquals(expect, o.toString());
        //fail(o.toString().replaceAll("\n", "\\\\n\"\n    + \""));
    }
    @Test
    @Covers("public Collection<BSPTree> findInvertedLevelOrder(Collection<BSPTree>)")
    public void findInvertedLevelOrder() {
        t.splitRecursive(new Random(1234556), 2, 4, 4, 5.0, 3.0);
        Collection<BSPTree> o = t.findInvertedLevelOrder(null);
        String expect = "[{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 0; Position: 0,0; Size: 25,80; Split: 10; Horizontal; Level: 0;\n"
            + " Right:{Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}};\n"
            + " Left:{Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}}]";
        assertEquals(expect, o.toString());
        // fail(o.toString().replaceAll("\n", "\\\\n\"\n    + \""));
    }

    @Test
    @Covers("public void splitRecursive(Random,int,int,int,int,int)")
    public void splitRecursiveBig() {
        t.splitRecursive(new Random(1234556), 2, 4, 4, 5000, 3000);
        Collection<BSPTree> o = t.findInOrder(null);
        String expect = "[{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 0; Position: 0,0; Size: 25,80; Split: 10; Horizontal; Level: 0;\n"
            + " Right:{Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}};\n"
            + " Left:{Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}}, {Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}]";
        assertEquals(expect, o.toString());
        // fail(o.toString().replaceAll("\n", "\\\\n\"\n    + \""));
    }

    @Test
    @Covers("public Collection<BSPTree> findBottomOrder(Collection<BSPTree>)")
    public void findBottomOrder() {
        t.splitRecursive(new Random(1234556), 2, 4, 4, 5.0, 3.0);
        Collection<BSPTree> o = t.findBottomOrder(null);
        String expect = "[{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}, {Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}, {Level: 0; Position: 0,0; Size: 25,80; Split: 10; Horizontal; Level: 0;\n"
            + " Right:{Level: 1; Position: 10,0; Size: 15,80; Split: 18; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 10,18; Size: 15,62; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 10,0; Size: 15,18; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}};\n"
            + " Left:{Level: 1; Position: 0,0; Size: 10,80; Split: 12; Vertical; Level: 1;\n"
            + " Right:{Level: 2; Position: 0,12; Size: 10,68; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-};\n"
            + " Left:{Level: 2; Position: 0,0; Size: 10,12; Split: -1; Vertical; Level: 2;\n"
            + " Right:-;\n"
            + " Left:-}}}]";
        assertEquals(expect, o.toString());
        // fail(o.toString().replaceAll("\n", "\\\\n\"\n    + \""));
    }

}
