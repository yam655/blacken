/* blacken - a library for Roguelike games
 * Copyright © 2010-2012 Steven Black <yam655@gmail.com>
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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.googlecode.blacken.core.Random;
import com.googlecode.blacken.grid.BoxRegion;
import com.googlecode.blacken.grid.BoxRegionIterator;
import com.googlecode.blacken.grid.Point;
import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.RegionIterator;
import com.googlecode.blacken.grid.Regionlike;
import com.googlecode.blacken.grid.SimpleSize;
import com.googlecode.blacken.grid.Sizable;

/**
 *  An implementation of Binary Screen Partitioning Trees, useful for quick and good dungeon generation.
 *
 * @author XLambda
 *
 */

public class BSPTree implements Regionlike {

    private int x;
    private int y;
    private int width;
    private int height;
    private int level;
    private int position;
    private boolean horizontal;
    private BSPTree leftChild;
    private BSPTree rightChild;
    private BSPTree parent;

    public BSPTree(Regionlike r) {
        this.x = r.getX();
        this.y = r.getY();
        this.width = r.getWidth();
        this.height = r.getHeight();
        this.level = 0;
        this.position = -1;
        this.horizontal = false;
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
    }

    /**
     * Constructor of BSPTree.
     * Creates a new single-node tree which can be split to refine it.
     *
     * @param height the tree's height
     * @param width the tree's width
     * @param y the tree's y ordinate
     * @param x the tree's x ordinate
     */
    public BSPTree(int height, int width, int y, int x) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.level = 0;
        this.position = -1;
        this.horizontal = false;
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
    }

    /**
     * This mostly exists to help with debugging.
     *
     * @param height
     * @param width
     * @param y
     * @param x
     * @param level
     * @param position
     * @param horizontal
     */
    protected BSPTree(int height, int width, int y, int x, int level, int position, boolean horizontal) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.level = level;
        this.position = position;
        this.horizontal = horizontal;
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
    }

    /**
     *  Constructor of BSPTree.
     *  Creates a new sub-tree for an existing BSPTree.
     *  Do not use for splitting a tree.
     *  @param parent the father node
     *  @param left true to create a left child, false to create a right child
     */

    public BSPTree(BSPTree parent, boolean left) {
        if(parent.isHorizontal()) {
            this.x = parent.getX();
            this.width = parent.getWidth();
            if(left) {
                this.y = parent.getY();
                this.height = parent.getSplitPosition() - this.y;
            } else {
                this.y = parent.getSplitPosition();
                this.height = parent.getY() + parent.getHeight() - parent.getSplitPosition();
            }
        } else {
            this.y = parent.getY();
            this.height = parent.getHeight();
            if(left) {
                this.x = parent.getX();
                this.width = parent.getSplitPosition() - this.x;
            } else {
                this.x = parent.getSplitPosition();
                this.width = parent.getX() + parent.getWidth() - parent.getSplitPosition();
            }
        }
        this.level = parent.getLevel() + 1;
        this.position = -1;
        this.horizontal = false;
        this.leftChild = null;
        this.rightChild = null;
        this.parent = parent;
    }

    /**
     *  Returns the node's x ordinate.
     *  @return the node's x ordinate
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     *  Returns the node's y ordinate.
     *  @return the node's y ordinate
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     *  Returns the node's width.
     *  @return the node's width
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     *  Returns the node's height.
     *  @return the node's height
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     *  Returns the node's level. Zero is the root node.
     *  @return the node's level
     */

    public int getLevel() {
        return level;
    }

    /**
     *  Returns the place in which the node is split. See {@link #isHorizontal()} for the dimension of the split.
     *  @return the place in which the node is split.
     */

    public int getSplitPosition() {
        return position;
    }


    /**
     *  Returns the node's orientation when split.
     *  @return true if the node splits horizontally, false if it splits vertically
     */

    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     *  Returns the node's left child.
     *  @return the node's left child
     */

    public BSPTree getLeftChild() {
        return leftChild;
    }

    /**
     *  Returns the node's right child.
     *  @return the node's right child
     */

    public BSPTree getRightChild() {
        return rightChild;
    }

    /**
     *  Returns the node's parent.
     *  @return the node's parent
     */
    public BSPTree getParent() {
        return parent;
    }

    /**
     * Returns the node's father.
     * @return the node's father
     * @deprecated use {@link #getParent()} instead.
     */
    @Deprecated
    public BSPTree getFather() {
        return parent;
    }

    /**
     *  Checks whether the node is a leaf.
     *  @return true, if the node is a leaf, false otherwise
     */

    public boolean isLeaf() {
        if(leftChild == null && rightChild == null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     *  Checks whether the given coordinates lie in the node.
     *  @param px the x ordinate
     *  @param py the y ordinate
     *  @return true, if the coordinates lie in the node, false otherwise.
     */
    @Override
    public boolean contains(int py, int px) {
        return (px >= x && py >= y && px < x+width && py < y+height);
    }

    /**
     *  Finds the leaf of the tree containing the given coordinates.
     *  @param px the x ordinate
     *  @param py the y ordinate
     *  @return the leaf containing the coordinates, or null if the tree does not contain the coordinates.
     */

    public BSPTree findNode(int py, int px) {
        if(!contains(px,py)) {
            return null;
        }
        if(!isLeaf()) {
            if(leftChild.contains(px,py)) {
                return leftChild.findNode(px,py);
            } else if(rightChild.contains(px,py)) {
                return rightChild.findNode(px,py);
            }
        }
        return this;
    }

    /**
     *  Traverses the tree in preorder.
     *  @param nodelist the target list. If null is passed, a new list will be created.
     *  @return a List of the tree's nodes in preorder.
     */
    public List<BSPTree> traversePreorder(List<BSPTree> nodelist) {
        if(nodelist == null) {
            nodelist = new LinkedList<>();
        }
        nodelist.add(this);
        if(leftChild != null) {
            leftChild.traversePreorder(nodelist);
        }
        if(rightChild !=null) {
            rightChild.traversePreorder(nodelist);
        }
        return nodelist;
    }

    /**
     *  Traverses the tree in inorder.
     *  @param nodelist the target list. If null is passed, a new list will be created.
     *  @return a List of the tree's nodes in inorder
     */

    public List<BSPTree> traverseInorder(List<BSPTree> nodelist) {
        if(nodelist == null) {
            nodelist = new LinkedList<>();
        }
        if(leftChild != null) {
            leftChild.traverseInorder(nodelist);
        }
        nodelist.add(this);
        if(rightChild != null) {
            rightChild.traverseInorder(nodelist);
        }
        return nodelist;
    }

    /**
     *  Traverses the tree in postorder.
     *  @param nodelist the target list. If null is passed, a new list will be created.
     *  @return a List of the tree's nodes in postorder
     */

    public List<BSPTree> traversePostorder(List<BSPTree> nodelist) {
        if(nodelist == null) {
            nodelist = new LinkedList<>();
        }
        if(leftChild != null) {
            leftChild.traversePostorder(nodelist);
        }
        if(rightChild != null) {
            rightChild.traversePostorder(nodelist);
        }
        nodelist.add(this);
        return nodelist;
    }

    /**
     *  Traverses the tree in level order.
     *  @param nodelist the target list. If null is passed, a new list will be created.
     *  @return a List of the tree's nodes in level order
     */
    public List<BSPTree> traverseLevelOrder(List<BSPTree> nodelist) {
        if(nodelist == null) {
            nodelist = new ArrayList<>();
        }
        List<BSPTree> q = new ArrayList<>();
        q.add(this);
        while(!q.isEmpty()) {
            BSPTree currentNode = q.remove(0);
            nodelist.add(currentNode);
            if(!currentNode.isLeaf()) {
                q.add(currentNode.getLeftChild());
                q.add(currentNode.getRightChild());
            }
        }
        return nodelist;
    }

    /**
     *  Traverses the tree in inverted level order.
     *  @param nodelist the target list. If null is passed, a new list will be generated.
     *  @return a List of the tree's nodes in inverted level order
     */
    public List<BSPTree> traverseInvertedLevelOrder(List<BSPTree> nodelist) {
        List<BSPTree> ret = traverseLevelOrder(nodelist);
        Collections.reverse(ret);
        return ret;
    }

    /**
     *  Splits the tree by adding two sons to the node.
     *  @param horizontal orientation of the split
     *  @param position place in which the tree is to be split
     *
     */

    public void splitOnce(boolean horizontal, int position) {
        if (this.leftChild != null) {
            return;
        }
        this.horizontal = horizontal;
        this.position = position;
        BSPTree lc = new BSPTree(this, true);
        BSPTree rc = new BSPTree(this, false);
        this.leftChild = lc;
        this.rightChild = rc;
    }

    /**
     * Splits the tree recursively, based on size and recursion depth 
     * constraints.
     *
     * <p>A node will only be split if the resulting subnodes are at least
     * minVSize x minHSize large.</p>
     *
     * <p>If a split node does not fit the maxVRatio, the split  orientation
     * will be changed to achieve a ratio smaller than maxHRatio</p>
     *
     *  @param rng If null, a new one will be created
     *  @param recursionDepth Due to size constraints this might no be reached.
     *  @param minVSize the minumum height of a node
     *  @param minHSize the minimum width of a node
     *  @param maxVRatio the maximum height/width ratio 
     *  @param maxHRatio the maximum width/height ratio
     */
    public void splitRecursive(Random rng, int recursionDepth,
            int minVSize, int minHSize, double maxVRatio, double maxHRatio) {
        if(recursionDepth == 0 || width < 2*minHSize || height < 2*minVSize) {
            return;
        }
        boolean horiz;
        if(rng == null) {
            rng = new Random();
        }
        if(height < 2*minVSize || width > height * maxHRatio) {
            horiz = false;
        } else if (width < 2*minHSize || height > width * maxVRatio) {
            horiz = true;
        } else {
            horiz = rng.nextBoolean();
        }
        int pos;
        if(horiz) {
            pos = rng.nextInt(y+minVSize,y+height-minVSize);
        } else {
            pos = rng.nextInt(x+minHSize,x+width-minHSize);
        }
        if (this.leftChild == null && this.rightChild == null) {
            splitOnce(horiz, pos);
        }
        leftChild.splitRecursive(rng, recursionDepth-1, minVSize, minHSize, maxVRatio, maxHRatio);
        rightChild.splitRecursive(rng, recursionDepth-1, minVSize, minHSize, maxVRatio, maxHRatio);
    }


    /**
     *  Resizes the tree and all its subtrees, without changing the splitting orientation and position.
     *  This should only be called on the tree to enlarge it - shrinking may cause splits to be outside the repective node. Leafs can of course be shrunk safely, but should not be enlarged beyond their original size, otherwise a coordinate may be contained in more than one leaf.
     *  @param x the tree's new x ordinate
     *  @param y the tree's new y ordinate
     *  @param width the tree's new width
     *  @param height the tree's new height
     */
    @Override
    public void setBounds(int y, int x, int height, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if(!this.isLeaf()) {
            if(isHorizontal()) {
                leftChild.setBounds(y, x, position-y, width);
                rightChild.setBounds(position, x, y+height-position, width);
            } else {
                leftChild.setBounds(y, x, height, position-x);
                rightChild.setBounds(y, position, height, x+width-position);
            }
        }
    }
    @Override
    public void setBounds(Regionlike r) {
        this.setBounds(r.getY(), r.getX(), r.getHeight(), r.getWidth());
    }

    @Override
    public boolean contains(int height, int width, int y1, int x1) {
        return BoxRegion.contains(this, height, width, y1, x1);
    }

    @Override
    public boolean contains(Positionable p) {
        return this.contains(p.getY(), p.getX());
    }

    @Override
    public boolean contains(Regionlike r) {
        return BoxRegion.contains(this, r);
    }

    @Override
    public Regionlike getBounds() {
        return new BoxRegion(height, width, y, x);
    }

    @Override
    public RegionIterator getEdgeIterator() {
        RegionIterator ret = new BoxRegionIterator(this, true, false);
        return ret;
    }

    @Override
    public RegionIterator getInsideIterator() {
        RegionIterator ret = new BoxRegionIterator(this, false, false);
        return ret;
    }

    @Override
    public RegionIterator getNotOutsideIterator() {
        RegionIterator ret = new BoxRegionIterator(this, false, true);
        return ret;
    }

    @Override
    public boolean intersects(int height, int width, int y1, int x1) {
        return BoxRegion.intersects(this, height, width, y1, x1);
    }

    @Override
    public boolean intersects(Regionlike room) {
        return BoxRegion.intersects(this, room);
    }

    @Override
    public Positionable getPosition() {
        return new Point(y, x);
    }

    @Override
    public void setX(int x) {
        throw new UnsupportedOperationException("BSP Trees are not movable");
    }

    @Override
    public void setY(int y) {
        throw new UnsupportedOperationException("BSP Trees are not movable");
    }

    @Override
    public void setPosition(int y, int x) {
        throw new UnsupportedOperationException("BSP Trees are not movable");
    }

    @Override
    public void setPosition(Positionable point) {
        throw new UnsupportedOperationException("BSP Trees are not movable");
    }

    @Override
    public Sizable getSize() {
        return new SimpleSize(height, width);
    }

    @Override
    public void setHeight(int height) {
        throw new UnsupportedOperationException("BSP Trees are not individually resizable");
    }

    @Override
    public void setWidth(int width) {
        throw new UnsupportedOperationException("BSP Trees are not individually resizable");
    }

    @Override
    public void setSize(int height, int width) {
        throw new UnsupportedOperationException("BSP Trees are not individually resizable");
    }

    @Override
    public void setSize(Sizable size) {
        throw new UnsupportedOperationException("BSP Trees are not individually resizable");
    }

    @Override
    public String toString() {
        String right = "-";
        String left = "-";
        if (rightChild != null) {
            right = rightChild.toString();
        }
        if (leftChild != null) {
            left = leftChild.toString();
        }
        return String.format("{Level: %s; Position: %s,%s; Size: %s,%s; "
                + "Split: %s; %s; Level: %s;\n Right:%s;\n Left:%s}",
                level, getY(), getX(), getHeight(), getWidth(), position,
                (horizontal ? "Horizontal" : "Vertical"), level, right, left);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BSPTree other = (BSPTree) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.width != other.width) {
            return false;
        }
        if (this.height != other.height) {
            return false;
        }
        if (this.level != other.level) {
            return false;
        }
        if (this.position != other.position) {
            return false;
        }
        if (this.horizontal != other.horizontal) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.x;
        hash = 37 * hash + this.y;
        hash = 37 * hash + this.width;
        hash = 37 * hash + this.height;
        hash = 37 * hash + this.level;
        hash = 37 * hash + this.position;
        hash = 37 * hash + (this.horizontal ? 1 : 0);
        return hash;
    }
}
