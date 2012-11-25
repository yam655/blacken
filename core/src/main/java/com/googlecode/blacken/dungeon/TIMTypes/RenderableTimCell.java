/* blacken - a library for Roguelike games
 * Copyright © 2012 Steven Black <yam655@gmail.com>
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

package com.googlecode.blacken.dungeon.TIMTypes;

import com.googlecode.blacken.dungeon.TIMCell;
import com.googlecode.blacken.fov.Lightable;
import com.googlecode.blacken.fov.LineOfSightable;
import com.googlecode.blacken.terminal.TerminalCell;
import com.googlecode.blacken.terminal.TerminalCellLike;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;

/**
 * A simple renderable TIM cell with line-of-sight support.
 *
 * @param <T> terrain type
 * @param <I> item type
 * @param <M> monster type
 * @author Steven Black
 */
public class RenderableTimCell<T extends LineOfSightable & Renderable,
        I extends Renderable, M extends Renderable>
extends TIMCell<T, I, M> implements Lightable, LineOfSightable, Renderable {
    private TerminalCell rendering = new TerminalCell();
    enum CellState {
        IS_VISIBLE,
        BEEN_SEEN,
        BEEN_ITEM,
    }
    private EnumSet<CellState> state = EnumSet.noneOf(CellState.class);
    /**
     * Create a new empty TIM cell.
     */
    public RenderableTimCell() {
    }

    /**
     * Create a new TIM cell, specifying the three ingredients.
     * @param terrain
     * @param item
     * @param monster
     */
    public RenderableTimCell(T terrain, I item, M monster) {
        super(terrain, item, monster);
        computeRendering();
    }

    /**
     * Create a new TIM cell, based upon an old.
     * @param old
     */
    public RenderableTimCell(RenderableTimCell<T, I, M> old) {
        super(old);
        computeRendering();
    }

    @Override
    public RenderableTimCell<T,I,M> clone() {
        return new RenderableTimCell(this);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            state.add(CellState.IS_VISIBLE);
        } else {
            state.remove(CellState.IS_VISIBLE);
        }
        this.computeRendering();
    }

    @Override
    public boolean isVisible() {
        return state.contains(CellState.IS_VISIBLE);
    }

    @Override
    public boolean blocksFOV() {
        if (getTerrain().blocksFOV()) {
            return true;
        }
        // XXX Will this be fast enough, or will we need to cache it?
        for (Method m : getMonster().getClass().getMethods()) {
            Annotation a = m.getAnnotation(ImpactsLOS.class);
            if (a == null) {
                continue;
            }
            if (m.getParameterTypes().length != 0) {
                continue;
            }
            if (m.getReturnType().equals(Boolean.TYPE)) {
                try {
                    return (boolean) m.invoke(getMonster());
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public TerminalCellLike getRendering() {
        return rendering;
    }

    private void computeRendering() {
        TerminalCellLike e;
        if (state.contains(CellState.IS_VISIBLE)) {
            state.add(CellState.BEEN_SEEN);
        }
        if (state.contains(CellState.IS_VISIBLE) && getMonster() != null) {
            e = getMonster().getRendering();
            if (e != null) {
                rendering.set(e);
                return;
            }
        }
        if (getItem() != null && state.contains(CellState.IS_VISIBLE)) {
            state.add(CellState.BEEN_ITEM);
        }
        if (state.contains(CellState.BEEN_ITEM)) {
            if (getItem() != null) {
                e = getItem().getRendering();
                if (e != null) {
                    rendering.set(e);
                    return;
                }
            } else {
                state.remove(CellState.BEEN_ITEM);
            }
        }
        if (state.contains(CellState.BEEN_SEEN)) {
            if (getTerrain() != null) {
                e = getTerrain().getRendering();
                if (e != null) {
                    rendering.set(e);
                    return;
                }
            }
        }
        rendering.clearCellWalls();
        rendering.clearStyle();
        rendering.setDirty(true);
        rendering.setSequence("");
        rendering.setBackground(0xFF000000);
        rendering.setForeground(0xFFa0a0a0);
    }

    @Override
    public void setItem(I item) {
        super.setItem(item);
        computeRendering();
    }

    @Override
    public void setMonster(M monster) {
        super.setMonster(monster);
        computeRendering();
    }

    @Override
    public void setTerrain(T terrain) {
        super.setTerrain(terrain);
        computeRendering();
    }


}
