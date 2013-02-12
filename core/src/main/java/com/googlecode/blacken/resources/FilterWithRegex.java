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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Steven Black
 */
public class FilterWithRegex implements FilenameFilter {
    private final Pattern pattern;

    /**
     * Create a FilenameFilter that uses a regular expression.
     *
     * @param regex
     */
    public FilterWithRegex(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean accept(File file, String string) {
        Matcher m = pattern.matcher(string);
        if (m.matches()) {
            return true;
        }
        return false;
    }

}
