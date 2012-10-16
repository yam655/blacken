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

import com.googlecode.blacken.resources.ResourceIdentifier;
import com.googlecode.blacken.resources.ResourceMissingException;
import com.googlecode.blacken.resources.ResourceUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Steven Black
 */
public class Obligations {
    private static final Logger LOGGER = LoggerFactory.getLogger(Obligations.class);
    private static String getResourceContents(String name) {
        String ret;
        try {
            ret = ResourceUtils.getResourceAsString(Obligations.class, name);
        } catch (ResourceMissingException ex) {
            ret =  String.format("Failed to load 'Obligations' resource: %s", name);
        }
        return ret;
    }
    public static String getBlackenLicense() {
        return getResourceContents("LICENSE.txt");
    }
    public static ResourceIdentifier getBlackenLicense(ResourceIdentifier resid) {
        if (resid == null) {
            resid = new ResourceIdentifier();
        }
        resid.setResourceLoader(Obligations.class);
        resid.setResourcePath("LICENSE.txt");
        return resid;
    }
    public static String getBlackenNotice() {
        return getResourceContents("NOTICE.txt");
    }
    public static ResourceIdentifier getBlackenNotice(ResourceIdentifier resid) {
        if (resid == null) {
            resid = new ResourceIdentifier();
        }
        resid.setResourceLoader(Obligations.class);
        resid.setResourcePath("NOTICE.txt");
        return resid;
    }

    public static String getFontName() {
        return "DejaVu";
    }
    public static String getFontLicense() {
        return getResourceContents("/fonts/LICENSE-dejavu.txt");
    }
    public static ResourceIdentifier getFontLicense(ResourceIdentifier resid) {
        if (resid == null) {
            resid = new ResourceIdentifier();
        }
        resid.setResourceLoader(null);
        resid.setResourcePath("/fonts/LICENSE-dejavu.txt");
        return resid;
    }
}
