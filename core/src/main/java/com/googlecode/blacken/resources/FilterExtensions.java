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

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A mostly case-insensitive extension filter.
 *
 * <p>Standard extensions are in US-ASCII and are favored to be in lowercase.
 * That is, they are defined to be in lowercase, so you can technically
 * ignore them if they're upper-case. Unfortunately, there are a lot of 
 * case-insensitive file systems, so this tries to be case-insensitive.
 *
 * <p>It uses the user's normal locale -- since the filenames should be
 * specified using the user's normal locale -- which means that for game
 * developers you can get unusual results unless you're consistently specifying
 * lowercase extensions.
 *
 * <p>Also note: This uses a Unix-centric definition of "extension", which
 * means that the period -- if required -- is to be considered a part of the
 * extension.
 *
 * @author Steven Black
 */
public class FilterExtensions implements FilenameFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterExtensions.class);
    private final String[] extensions;

    FilterExtensions(String... extensions) {
        Set<String> ext = new HashSet<>();
        for (String e : extensions) {
            ext.add(e.toLowerCase());
        }
        this.extensions = ext.toArray(new String[0]);
    }

    @Override
    public boolean accept(File file, String string) {
        String check = string.toLowerCase();
        for (String e : extensions) {
            // LOGGER.debug("Checking {} for {}", string, e);
            if (check.endsWith(e)) {
                return true;
            }
        }
        return false;
    }

}
