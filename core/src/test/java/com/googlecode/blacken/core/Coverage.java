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

package com.googlecode.blacken.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * Used with @Covers to verify a test class tests all public functions in
 * an implementation class.
 * 
 * @author Steven Black
 */
public class Coverage {

    /**
     * Check that <code>implClass</code> has <code>Covers</code> for each public
     * function in <code>testClass</code> -- even functions in super classes.
     * 
     * <p>This makes sure the test class tests for functions defined in the
     * implClass as well as all parents. This avoids running tests on 
     * <code>Object</code> itself, but will run tests on any function overridden
     * elsewhere.</p>
     * 
     * @param implClass
     * @param testClass 
     */
    public static void checkCoverageDeep(Class<?> implClass, 
            Class<?> testClass) {
        _checkCoverage(implClass, testClass, true);
    }

    /**
     * Check that <code>implClass</code> has <code>Covers</code> for each public
     * function in <code>testClass</code> -- but not those in super classes.
     * 
     * @param implClass
     * @param testClass 
     */
    public static void checkCoverage(Class<?> implClass, Class<?> testClass) {
        _checkCoverage(implClass, testClass, false);
    }

    /**
     * Underlying logic to check coverage.
     * 
     * @param implClass
     * @param testClass
     * @param isDeep 
     */
    protected static void _checkCoverage(Class<?> implClass, 
            Class<?> testClass, boolean isDeep) {
        Set<String> implNames = new HashSet<>();
        Set<String> accept = null;
        if (isDeep) {
            Method[] implMethods = implClass.getMethods();
            for (Method m : implMethods) {
                String impl = m.toGenericString().replaceAll(
                        "[a-zA-Z0-9.]+[.]([a-zA-Z0-9_]+)", "$1");
                implNames.add(impl);
            }
            Set<String> myNames = new HashSet<>();
            implMethods = implClass.getDeclaredMethods();
            for (Method m : implMethods) {
                String impl = m.toGenericString().replaceAll(
                        "[a-zA-Z0-9.]+[.]([a-zA-Z0-9_]+)", "$1");
                myNames.add(impl);
            }
            Method[] objMethods = Object.class.getMethods();
            accept = new HashSet<>();
            for (Method m : objMethods) {
                String impl = m.toGenericString().replaceAll(
                        "[a-zA-Z0-9.]+[.]([a-zA-Z0-9_]+)", "$1");
                if (!myNames.contains(impl)) {
                    implNames.remove(impl);
                    accept.add(impl);
                }
            }
        } else {
            Method[] implMethods = implClass.getDeclaredMethods();
            for (Method m : implMethods) {
                String impl = m.toGenericString().replaceAll(
                        "[a-zA-Z0-9.]+[.]([a-zA-Z0-9_]+)", "$1");
                implNames.add(impl);
            }
        }
        Constructor<?>[] implConstructors = implClass.getConstructors();
        for (Constructor<?> c : implConstructors) {
            String impl = c.toGenericString().replaceAll(
                    "[a-zA-Z0-9.]+[.]([a-zA-Z0-9_]+)", "$1");
            implNames.add(impl);
        }
        Method[] testMethods = testClass.getMethods();
        Map<String, String> testNames = new HashMap<>();
        for (Method m : testMethods) {
            Covers covers = m.getAnnotation(Covers.class);
            if (covers == null) {
                continue;
            }
            for (String test : covers.value()) {
                if (test.isEmpty()) {
                    continue;
                }
                testNames.put(test, m.toString());
            }
        }
        for (String key : new ArrayList<>(implNames)) {
            if (testNames.containsKey(key)) {
                implNames.remove(key);
                testNames.remove(key);
            }
        }
        if (isDeep) {
            for (String key : accept) {
                testNames.remove(key);
            }
        }
        StringBuilder buf = new StringBuilder();
        if (!implNames.isEmpty()) {
            buf.append("Missing tests for:\n");
            for (String s : implNames) {
                buf.append(s);
                buf.append("\n");
            }
        }
        if (!testNames.isEmpty()) {
            if (!implNames.isEmpty()) {
                buf.append("\n");
            }
            buf.append("Missing implementation for:\n");
            for (String s : testNames.keySet()) {
                buf.append(s);
                buf.append(" used by ");
                buf.append(testNames.get(s));
                buf.append("\n");
            }
        }
        if (buf.length() > 0) {
            fail(buf.toString());
        }
    }
}
