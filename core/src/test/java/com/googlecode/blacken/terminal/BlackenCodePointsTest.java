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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author yam655
 */
public class BlackenCodePointsTest {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BlackenCodePointsTest.class);

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetName() {
        StringBuilder buf = new StringBuilder();
        for (Field f : BlackenCodePoints.class.getFields()) {
            String fieldname = f.getName();
            if (fieldname.startsWith("CODEPOINT_")) {
                // NO_KEY isn't in BlackenCodePoints; perfect for this
                int codepoint = BlackenKeys.NO_KEY;
                try {
                    codepoint = f.getInt(null);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
                BlackenCodePoints.LongName longname = f.getAnnotation(BlackenCodePoints.LongName.class);
                String codepointname = Character.getName(codepoint);
                if (codepointname == null) {
                    buf.append(String.format("Field %s has codepoint %s "
                            + "which is UNDEFINED\n", fieldname, codepoint));
                    continue;
                }
                String found = BlackenCodePoints.getName(codepoint);
                if (fieldname.equals("CODEPOINT_SP") && found.equals("CODEPOINT_SPACE")) {
                    continue;
                }
                if (longname != null) {
                    if (!codepointname.equals(longname.value())) {
                        buf.append(String.format("Field %s has LongName %s " +
                                "which is different than expected (%s)\n",
                                fieldname, longname.value(), codepointname));
                    }
                    if (found == null || !found.equals(fieldname)) {
                        buf.append(String.format("Field %s didn't return field"
                                + " name; returned %s (codepoint: %s)\n",
                                fieldname, found, codepointname));
                    }
                    continue;
                }
                String expected = "CODEPOINT_" + codepointname.replaceAll("[ ()-]+", "_");
                if (found == null || !found.equals(expected) || !found.equals(fieldname)) {
                    buf.append(String.format(
                            "Field %s; Unicode name: %s; Expected Field Name: %s; Returned Name: %s\n",
                            fieldname, codepointname, expected, found));
                }
            }
        }
        String msg = buf.toString();
        if (!msg.isEmpty()) {
            msg = "Constants differ from expected value:\n" + msg;
            LOGGER.equals(msg);
            fail(msg);
        }

    }
}
