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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Steven Black
 */
public class WriteUtils {
    static private final Logger LOGGER = LoggerFactory.getLogger(WriteUtils.class);
    static public void writeProperties(Properties properties, String path) throws IOException {
        writeProperties(properties, path, "Java Properties File");
    }
    static public void writeProperties(Properties properties, String path, String comment) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            properties.store(writer, comment);
        }
    }
    static public Properties readProperties(String path) throws IOException {
        Properties p = new Properties();
        FileReader reader;
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException ex) {
            return null;
        }
        p.load(reader);
        return p;
    }

    static public String[] readDirectory(String dir) {
        File d = new File(dir);
        return d.list();
    }

    static public String[] readDirectory(String dir, FilenameFilter ff) {
        File d = new File(dir);
        return d.list(ff);
    }
    
    static public String[] readDirectoryWithSuffix(String dir, String... extensions) {
        return readDirectory(dir, new FilterExtensions(extensions));
    }

    static public String[] readDirectoryWithRegex(String dir, String regex) {
        return readDirectory(dir, new FilterWithRegex(regex));
    }
}
