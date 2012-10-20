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

package com.googlecode.blacken.resources;

/**
 * Upgrade a SecurityException from a RuntimeException to one that requires
 * catching it.
 *
 * @author Steven Black
 */
public class PlannedSecurityException extends Exception {
    private static final long serialVersionUID = 4580830052853229692L;
    private Exception cause;
    PlannedSecurityException(SecurityException ex) {
        cause = ex;
    }
    @Override
    public String toString() {
        if (cause == null) {
            return "PlannedSecurityException without cause";
        }
        return cause.toString();
    }
}
