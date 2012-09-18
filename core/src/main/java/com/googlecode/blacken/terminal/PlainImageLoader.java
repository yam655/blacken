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

package com.googlecode.blacken.terminal;

import com.googlecode.blacken.colors.ColorHelper;
import com.googlecode.blacken.core.ListMap;
import com.googlecode.blacken.exceptions.InvalidStringFormatException;
import com.googlecode.blacken.grid.Grid;
import com.googlecode.blacken.resources.ResourceMissingException;
import com.googlecode.blacken.resources.ResourceUtils;
import java.util.Map.Entry;
import java.util.Properties;

/**
 *
 * @author Steven Black
 */
public class PlainImageLoader implements BlackenImageLoader {
    public static Grid<Integer> loadPlainImage(Class resourceLoader, String resourceName) throws ResourceMissingException {
        String propname = resourceName.replaceFirst("[.][^.]*$", ".properties");
        ListMap<String,Integer> mapping = null;
        if (ResourceUtils.hasResource(resourceLoader, propname)) {
            mapping = new ListMap<>();
            Properties prop = ResourceUtils.getResourceAsProperties(resourceLoader, propname);
            for (String key : prop.stringPropertyNames()) {
                String rKey = key;
                if (key.startsWith("0x") || key.startsWith("0X")) {
                    rKey = String.copyValueOf(Character.toChars(
                            Integer.parseInt(key.substring(2), 16)));
                }
                String what = prop.getProperty(key);
                Integer ready;
                try {
                    ready = ColorHelper.makeColor(what);
                } catch (InvalidStringFormatException ex) {
                    throw new RuntimeException(ex);
                }
                mapping.put(rKey, ready);
            }
        }
        return ResourceUtils.getResourceAsGrid(resourceLoader, resourceName, mapping, false);
    }

    @Override
    public Grid<Integer> loadImage(Class resourceLoader, String resourceName)
            throws ResourceMissingException {
        if (resourceName.endsWith(".txt")) {
            return loadPlainImage(resourceLoader, resourceName);
        }
        throw new UnsupportedOperationException("Unsupported image type.");
    }

    @Override
    public Grid<Integer> loadImage(Class resourceLoader, String resourceName, 
            int height, int width) throws ResourceMissingException {
        throw new UnsupportedOperationException("Plain images do not support resizing.");
    }

}
