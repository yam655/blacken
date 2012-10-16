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

import com.googlecode.blacken.grid.Grid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class for dealing with resources.
 *
 * @author Steven Black
 */
public class ResourceUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtils.class);

    public static String getResourceAsString(ResourceIdentifier resid) throws ResourceMissingException {
        return getResourceAsString(resid.getResourceLoader(), resid.getResourcePath());
    }

    public static String getResourceAsString(Class resourceLoader, String path) throws ResourceMissingException {
        if (resourceLoader == null) {
            // legal for absolute paths
            resourceLoader = Object.class;
        }
        InputStream stream = resourceLoader.getResourceAsStream(path);
        byte[] bytebuf = new byte[4096];
        int cnt;
        StringBuilder buf = new StringBuilder();
        try {
            while ((cnt = stream.read(bytebuf)) != -1) {
                buf.append(new String(bytebuf, 0, cnt, Charset.forName("UTF-8")));
            }
        } catch (NullPointerException | IOException ex) {
            throw new ResourceMissingException("Failed to load :" + path, ex);
        }
        return buf.toString();
    }

    public static boolean hasResource(Class resourceLoader, String name) {
        if (resourceLoader == null) {
            // legal for absolute paths
            resourceLoader = Object.class;
        }
        return resourceLoader.getResource(name) != null;
    }

    public static boolean hasResource(ResourceIdentifier resid) {
        return resid.getResourceLoader().getResource(resid.getResourcePath()) != null;
    }

    public static Properties getResourceAsProperties(Class resourceLoader, String name) throws ResourceMissingException {
        if (resourceLoader == null) {
            // legal for absolute paths
            resourceLoader = Object.class;
        }
        Properties p = new Properties();
        try {
            p.load(resourceLoader.getResourceAsStream(name));
        } catch (NullPointerException | IOException ex) {
            throw new ResourceMissingException("Failed to load :" + name, ex);
        }
        return p;
    }

    public static Properties getResourceAsProperties(ResourceIdentifier resid) throws ResourceMissingException {
        return getResourceAsProperties(resid.getResourceLoader(), resid.getResourcePath());
    }

    public static Grid<Integer> getResourceAsGrid(ResourceIdentifier resid,
            Map<String,Integer> transform, boolean doTrim)
            throws ResourceMissingException {
        return getResourceAsGrid(resid.getResourceLoader(), resid.getResourcePath(), transform, doTrim);
    }

    public static Grid<Integer> getResourceAsGrid(Class resourceLoader,
            String name, Map<String,Integer> transform, boolean doTrim)
            throws ResourceMissingException {
        String s = getResourceAsString(resourceLoader, name);
        Grid<Integer> grid = new Grid<>();
        List<String> source = new ArrayList<>(Arrays.asList(s.split("[\r\n]+")));
        ListIterator<String> iterator = source.listIterator();
        int maxCol = 0;
        while(iterator.hasNext()) {
            String line = iterator.next();
            if (doTrim) {
                line = line.trim();
            }
            if (line.isEmpty()) {
                if (doTrim) {
                    iterator.remove();
                }
            } else {
                if (maxCol < line.length()) {
                    maxCol = line.length();
                }
                iterator.set(line);
            }
        }
        grid.reset(source.size(), maxCol, null);
        for (int y = 0; y < source.size(); y++) {
            String line = source.get(y);
            int ox = 0;
            for (int x = 0; x < line.length(); x++) {
                if (Character.isLowSurrogate(line.charAt(x))) {
                    // codePointAt processes the high+low surrogates together
                    continue;
                }
                Integer val = line.codePointAt(x);
                if (transform != null) {
                    String key = String.copyValueOf(Character.toChars(val));
                    val = transform.get(key);
                }
                if (val != null) {
                    grid.set(y, ox, val);
                }
                ox++;
            }
        }
        return grid;
    }
}
