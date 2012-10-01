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

    /**
     *  An interface for FOV solving algorithms.
     *  Requires the use of the {@link LineOfSightable} interface.
     *  @author xlambda
     */

public interface FOVAlgorithm{

	/**
	 *	Set the {@link Grid} to be used by the FOV algorithm.
	 *	@param grid the grid to be used
	 */

	public void setOpacityMap(Grid<? extends LineOfSightable> grid);
	
	/**
	 *	Set the {@link Grid} to hold the result of the FOV algorithm.
	 *	@param grid the grid to be used
	 */	

	public void setVisibilityMap(Grid<? extends Lightable> grid);
	
	/**
	 *	Set the maximum range of sight.
	 *	@param range the maximum range of sight.
	 */
	
	public void setRange(int range);
	
	/**
	 *	Set the FOV algorithm to make walls visible or set only floor tiles as visible.
	 *	@param visible whether to include walls in the field-of-view
	 */
	
	public void setWallsVisible(boolean visible);

	/**
	 *	Solve FOV for a given pair of ordinates.
	 *	@param x the x ordinate
	 *	@param y the y ordinate
	 */
	
	public void solveFOV(int y, int x);
	
	/**
	 *	Solve FOV for a given {@link Positionable};
	 *	@param pos the Positionable
	 */
	 
	public void solveFOV(Positionable pos);

}