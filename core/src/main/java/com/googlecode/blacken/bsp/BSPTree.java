package com.googlecode.blacken.bsp;

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
	 *	Returns the node's x coordinate.
	 *	@return the node's x coordinate
	 */	
	
	public int getX(){
		return x;
	}
	
	/**
	 *	Returns the node's y coordinate.
	 *	@return the node's y coordinate
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
	
	
}