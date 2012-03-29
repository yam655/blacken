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

import com.googlecode.blacken.core.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;

/**
 *	An implementation of Binary Screen Partitioning Trees, useful for quick and good dungeon generation.
 *
 * @author XLambda
 *
 */

public class BSPTree{
	
	private int x; 
	private int y;
	private int w;
	private int h;
	private int level;
	private int position;
	private boolean horizontal;
	private BSPTree leftChild;
	private BSPTree rightChild;
	private BSPTree father;
	
	/**
	 *	Constructor of BSPTree. 
	 *	Creates a new single-node tree which can be split to refine it.
	 *	@param x the tree's x ordinate
	 *	@param y the tree's y ordinate
	 *	@param w the tree's width
	 *	@param h the tree's height
	 */
	 
	public BSPTree(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.level = 0;
		this.position = 0;
		this.horizontal = false;
		this.leftChild = null;
		this.rightChild = null;
		this.father = null;
	}
	
	/**
	 *	Constructor of BSPTree. 
	 *	Creates a new sub-tree for an existing BSPTree. 
	 * 	Do not use for splitting a tree.
	 *	@param father the father node
	 *	@param left true to create a left child, false to create a right child
	 */	
	 
	public BSPTree(BSPTree father, boolean left){
		if(father.isHorizontal()){
			this.x = father.getX();
			this.w = father.getWidth();
			if(left){
				this.y = father.getY();
				this.h = father.getPosition() - this.y;
			}else{
				this.y = father.getPosition();
				this.h = father.getY() + father.getHeight() - father.getPosition();
			}
		}else{
			this.y = father.getY();
			this.h = father.getHeight();
			if(left){
				this.x = father.getX();
				this.w = father.getPosition() - this.x;
			}else{
				this.x = father.getPosition();
				this.w = father.getX() + father.getWidth() - father.getPosition();
			}
		}
		this.level = father.getLevel() + 1;
		this.position = 0;
		this.horizontal = false;
		this.leftChild = null;
		this.rightChild = null;
		this.father = father;
	}
	
	/**
	 *	Returns the node's x ordinate.
	 *	@return the node's x ordinate
	 */	
	
	public int getX(){
		return x;
	}
	
	/**
	 *	Returns the node's y ordinate.
	 *	@return the node's y ordinate
	 */	
	
	public int getY(){
		return y;
	}
	
	/**
	 *	Returns the node's width.
	 *	@return the node's width
	 */
	
	
	public int getWidth(){
		return w;
	}
	
	/**
	 *	Returns the node's height.
	 *	@return the node's height
	 */
	
	
	public int getHeight(){
		return h;
	}
		
	/**
	 *	Returns the node's level. Zero is the root node.
	 *	@return the node's level
	 */
	
	public int getLevel(){
		return level;
	}
	
	/**
	 *	Returns the place in which the node is split. See {@link #isHorizontal()} for the dimension of the split. 
	 *	@return the place in which the node is split.
	 */
	 
	public int getPosition(){
		return position;
	}
	
	
	/**
	 *	Returns the node's orientation when split. 
	 *	@return true if the node splits horizontally, false if it splits vertically
	 */
	
	public boolean isHorizontal(){
		return horizontal;
	}
	
	/**
	 *	Returns the node's left child. 
	 *	@return the node's left child
	 */
	 
	public BSPTree getLeftChild(){
		return leftChild;
	}
	
	/**
	 *	Returns the node's right child. 
	 *	@return the node's right child
	 */
	 
	public BSPTree getRightChild(){
		return rightChild;
	}
	
	/**
	 *	Returns the node's father. 
	 *	@return the node's father
	 */
	 
	public BSPTree getFather(){
		return father;
	}
	
	/**
	 *	Checks whether the node is a leaf. 
	 *	@return true, if the node is a leaf, false otherwise
	 */
	 
	public boolean isLeaf(){
		if(leftChild == null && rightChild == null){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 *	Checks whether the given coordinates lie in the node.
	 *	@param px the x ordinate
	 *	@param py the y ordinate
	 *	@return true, if the coordinates lie in the node, false otherwise.
	 */
	
	public boolean contains(int px, int py){
		return (px >= x && py >= y && px < x+w && py < y+h);
	}
	
	/**
	 *	Finds the leaf of the tree containing the given coordinates. 
	 *	@param px the x ordinate
	 *	@param py the y ordinate
	 *	@return the leaf containing the coordinates, or null if the tree does not contain the coordinates.
	 */
	
	public BSPTree findNode(int px, int py){
		if(!contains(px,py)){
			return null;
		}
		if(!isLeaf()){
			if(leftChild.contains(px,py)){
				return leftChild.findNode(px,py);
			}else if(rightChild.contains(px,py)){
				return rightChild.findNode(px,py);
			}
		}
		return this;
	}
	
	/**
	 *	Traverses the tree in preorder.
	 *	@param nodelist the target list. If null is passed, a new list will be created.
	 *	@return a List of the tree's nodes in preorder.
	 */
	
	public List<BSPTree> traversePreorder(List<BSPTree> nodelist){
		if(nodelist == null){
			nodelist = new LinkedList<BSPTree>();
		}
		nodelist.add(this);
		leftChild.traversePreorder(nodelist);
		rightChild.traversePreorder(nodelist);
		return nodelist;
	}
	
	/**
	 *	Traverses the tree in inorder.
	 *	@param nodelist the target list. If null is passed, a new list will be created.
	 *	@return a List of the tree's nodes in inorder
	 */
	
	public List<BSPTree> traverseInorder(List<BSPTree> nodelist){
		if(nodelist == null){
			nodelist = new LinkedList<BSPTree>();
		}
		leftChild.traverseInorder(nodelist);
		nodelist.add(this);
		rightChild.traverseInorder(nodelist);
		return nodelist;
	}
	
	/**
	 *	Traverses the tree in postorder.
	 *	@param nodelist the target list. If null is passed, a new list will be created.
	 *	@return a List of the tree's nodes in postorder
	 */
	
	public List<BSPTree> traversePostorder(List<BSPTree> nodelist){
		if(nodelist == null){
			nodelist = new LinkedList<BSPTree>();
		}
		leftChild.traversePostorder(nodelist);
		rightChild.traversePostorder(nodelist);
		nodelist.add(this);
		return nodelist;
	}
	
	/** 
	 *	Traverses the tree in level order.
	 *	@param nodelist the target list. If null is passed, a new list will be created.
	 *	@return a List of the tree's nodes in level order
	 */
	 
	public List<BSPTree> traverseLevelOrder(List<BSPTree> nodelist){
		if(nodelist == null){
			nodelist = new LinkedList<BSPTree>();
		}
		Stack<BSPTree> nodestack = new Stack<BSPTree>();
		nodestack.push(this);
		while(!nodestack.empty()){
			BSPTree currentNode = nodestack.pop();
			nodelist.add(currentNode);
			if(!currentNode.isLeaf()){
				nodestack.push(currentNode.getLeftChild());
				nodestack.push(currentNode.getRightChild());
			}
		}
		return nodelist;
	}
	
	/**
	 *	Traverses the tree in inverted level order.
	 *	@param nodelist the target list. If null is passed, a new list will be generated.
	 *	@return a List of the tree's nodes in inverted level order
	 */
	 
	public List<BSPTree> traverseInvertedLevelOrder(List<BSPTree> nodelist){
		if(nodelist == null){
			nodelist = new LinkedList<BSPTree>();
		}
		Stack<BSPTree> stack1 = new Stack<BSPTree>();
		Stack<BSPTree> stack2 = new Stack<BSPTree>();
		stack1.push(this);
		while(!stack1.empty()){
			BSPTree currentNode = stack1.pop();
			stack2.push(currentNode);
			if(!currentNode.isLeaf()){
				stack1.push(currentNode.getLeftChild());
				stack1.push(currentNode.getRightChild());
			}
		}
		while(!stack2.empty()){
			BSPTree currentNode = stack2.pop();
			nodelist.add(currentNode);
		}
		return nodelist;
	}
	
	/**
	 *	Splits the tree by adding two sons to the node.
	 *	@param horizontal orientation of the split
	 *	@param position place in which the tree is to be split
	 *
	 */
	
	public void splitOnce(boolean horizontal, int position){
		this.horizontal = horizontal;
		this.position = position;
		BSPTree lc = new BSPTree(this, true);
		BSPTree rc = new BSPTree(this, false);
		this.leftChild = lc;
		this.rightChild = rc;
	}
	
	/**
	 *	Splits the tree recursively, based on size and recursion depth constraints.
	 *	@param gen a blacken random number generator. If no RNG is passed, a new one will be created
	 *	@param nb the desired recursion depth. Note that due to size constraints this depth might no be reached. 
	 *	@param minVSize the minumum height of a node. A node will only be split if the resulting subnodes are at least minVSize x minHSize large
	 *	@param minHSize the minimum width of a node. A node will only be split if the resulting subnodes are at least minVSize x minHSize large
	 *	@param maxVRatio the maximum height/width ratio. If a split node does not fit this, the split orientation will be changed to achieve a ratio smaller than maxVRatio
	 *	@param maxHRatio the maximum width/height ratio. If a split node does not fit this, the split orientation will be changed to achieve a ratio smaller than maxHRatio
	 */
	
	
	public void splitRecursive(Random gen, int nb, int minVSize, int minHSize, int maxVRatio, int maxHRatio){
		if(nb == 0 || w < 2*minHSize || h < 2*minVSize){
			return;
		}
		boolean horiz;
		if(gen == null){
			gen = new Random();
		}
		if(h < 2*minVSize || w > h * maxHRatio){
			horiz = false;
		}else if (w < 2*minHSize || h > w * maxVRatio){
			horiz = true;
		}else{
			int orientation = gen.nextInt(0,2);
			if(orientation == 0) {	
				horiz = false;
			}else{
				horiz = true;
			}
		}
		int position;
		if(horiz){
			position = gen.nextInt(y+minVSize,y+h-minVSize);
		}else{
			position = gen.nextInt(x+minHSize,x+w-minHSize);
		}
		splitOnce(horiz, position);
		leftChild.splitRecursive(gen, nb-1, minVSize, minHSize, maxVRatio, maxHRatio);
		rightChild.splitRecursive(gen, nb-1, minVSize, minHSize, maxVRatio, maxHRatio);
		rightChild.splitRecursive(gen, nb-1, minVSize, minHSize, maxVRatio, maxHRatio);
	}
	
	
	/**
	 *	Resizes the tree and all its subtrees, without changing the splitting orientation and position.
	 *	This should only be called on the tree to enlarge it - shrinking may cause splits to be outside the repective node. Leafs can of course be shrunk safely, but should not be enlarged beyond their original size, otherwise a coordinate may be contained in more than one leaf.
	 *	@param x the tree's new x ordinate
	 *	@param y the tree's new y ordinate
	 *	@param w the tree's new width
	 *	@param h the tree's new height
	 */
	
	public void resize(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		if(!this.isLeaf()){
			if(isHorizontal()){
				leftChild.resize(x,y,w,position-y);
				rightChild.resize(x,position,w,y+h-position);
			}
		}
	}
	
}