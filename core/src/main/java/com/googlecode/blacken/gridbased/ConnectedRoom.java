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

package com.googlecode.blacken.gridbased;

import com.googlecode.blacken.dungeon.Room;
import com.googlecode.blacken.grid.Regionlike;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @param <C> connector type
 * @author Steven Black
 */
public class ConnectedRoom<C> extends Room {
    Set<C> connections;
    /**
     * Normally called from a factory so the containers can be taken care of.
     * @param region
     */
    public ConnectedRoom(Regionlike region) {
        super(region);
        connections = new HashSet<>();
    }

    public void clearConnections() {
        connections.clear();
    }
    public boolean isConnected(C contact) {
        return connections.contains(contact);
    }
    public boolean addConnection(C contact) {
        return connections.add(contact);
    }
    public boolean removeConnection(C contact) {
        return connections.remove(contact);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 62 * hash + super.hashCode();
        hash = 62 * hash + Objects.hashCode(this.connections);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConnectedRoom<C> other = (ConnectedRoom<C>) obj;
        if (!super.equals(obj)) {
            return false;
        }
        if (!Objects.equals(this.connections, other.connections)) {
            return false;
        }
        return true;
    }
}
