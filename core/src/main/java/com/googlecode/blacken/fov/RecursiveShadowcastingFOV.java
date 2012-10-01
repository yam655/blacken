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

package com.googlecode.blacken.fov;

import com.googlecode.blacken.grid.Positionable;
import com.googlecode.blacken.grid.Grid;

public class RecursiveShadowcastingFOV implements FOVAlgorithm{
	
	private static int[][] mult = {
		{1,0,0,-1,-1,0,0,1},
		{0,1,-1,0,0,-1,1,0},
		{0,1,1,0,0,-1,-1,0},
		{1,0,0,1,-1,0,0,-1},
	};
	
	private int range;
	private Grid<? extends LineOfSightable> grid;
	private boolean wallsVisible = true;
	
	/**
	 *	Create a new Recursive Shadowcasting FOV Solver.
	 *	@param grid the grid you want to solve FOV for
	 *	@param range the maximum range of sight
	 *	@param wallsVisible Wall visibility
	 */
	
	public RecursiveShadowcastingFOV(Grid<? extends LineOfSightable> grid, int range, boolean wallsVisible){
		this.grid = grid;
		this.range = range;
		this.wallsVisible = wallsVisible;
	}
	
	/**
	 *	Create a new Recursive Shadowcasting FOV Solver without setting any variables.
	 */
	
	public RecursiveShadowcastingFOV(){
		// do nothing
	}
	
	/**
	 *	Set a new {@link Grid} of {@link LineOfSightable} cells for the algorithm to work on.
	 *	@param grid the new Grid object
	 */
	
	@Override
	public void setGrid(Grid<? extends LineOfSightable> grid){
		this.grid = grid;
	}
	
	/** 
	 *	Set a new maximum range for the field-of-view.
	 *	@param range the new maximum range
	 */

	@Override
	public void setRange(int range){
		this.range = range;
	}
	
	/**
	 *	Set Wall visibility.
	 *	@param wallsVisible wall visibility.
	 */
	
	@Override
	public void setWallsVisible(boolean wallsVisible){
		this.wallsVisible = wallsVisible;
	}
	
	/**
	 *	Solve FOV for a given pair of ordinates.
	 *	@param y the y ordinate
	 *	@param x the x ordinate
	 */
	
	@Override
	public void solveFOV(int y, int x){
		int oct;
		int c;
		int r2;
		purgeFOV();
		if(range == 0){
			int max_radius_x = grid.getWidth()-x;
			int max_radius_y = grid.getHeight()-y;
			max_radius_x = Math.max(max_radius_x,x);
			max_radius_y = Math.max(max_radius_y,y);
			range = (int) (Math.sqrt(max_radius_x*max_radius_x+max_radius_y*max_radius_y))+1;
		}
		r2 = range*range;
		/* recursive shadow casting */
		for (oct=0; oct < 8; oct++){
			castLight(x,y,1,1.0f,0.0f,range,r2,mult[0][oct],mult[1][oct],mult[2][oct],mult[3][oct],0);
		}
		grid.get(y,x).setVisible(true);
	}
	
	/**
	 *	Solve FOV for a given {@link Positionable}
	 *	@param pos the Positionable
	 */
	
	@Override
	public void solveFOV(Positionable pos){
		solveFOV(pos.getY(), pos.getX());
	}
	
	private void castLight(int cx, int cy, int row, float start, float end, int radius, int r2, int xx, int xy, int yx, int yy, int id){
		int j;
		float new_start = 0.0f;
		if(start < end) return;
		for(j=row; j<radius+1; j++){
			int dx=-j-1;
			int dy=-j;
			boolean blocked=false;
			while(dx <= 0){
				dx++;
				int X = cx+dx*xx+dy*xy;
				int Y = cy+dx*yx+dy*yy;
				if ((long) X < (long) grid.getWidth() && (long) Y < (long) grid.getHeight()) {
					float l_slope;
					float r_slope;
					l_slope = (dx-0.5f)/(dy+0.5f);
					r_slope = (dx+0.5f)/(dy-0.5f);
					if(start < r_slope) continue;
					else if(end > l_slope) break;
					if (dx*dx+dy*dy <= r2 && (wallsVisible || !grid.get(Y,X).blocksFOV())) grid.get(Y,X).setVisible(true);
					if (blocked){
						if(grid.get(Y,X).blocksFOV()){
							new_start = r_slope;
							continue;
						}else{
							blocked = false;
							start = new_start;
						}
					}else{
						if(grid.get(Y,X).blocksFOV() && j<radius){
							blocked = true;
							castLight(cx,cy,j+1,start,l_slope,radius,r2,xx,xy,yx,yy,id+1);
							new_start=r_slope;
						}
					}
				}
			}
		}
	}
	
	private void purgeFOV(){
		for(int x = 0; x < grid.getWidth(); x++){
			for(int y = 0; y < grid.getHeight(); y++){
				grid.get(y,x).setVisible(false);
			}
		}
	}
	
}