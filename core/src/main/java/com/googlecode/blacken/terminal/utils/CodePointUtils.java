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

package com.googlecode.blacken.terminal.utils;

import com.googlecode.blacken.terminal.BlackenKeys;

/**
 * A utility class for functions dealing with code points.
 *
 * @author Steven Black
 */
public class CodePointUtils {

    /**
     * Find the number of advancing codepoints, and the line terminator.
     *
     * @param str string to check
     * @param start starting point in string
     * @return {advancingCodepoints, lengthToTerminator}
     */
    public static int[] findAdvancingCodepoints(String str, int start) {
        int[] ret = {0, 0};
        out:
        for (int i = start; i < str.length(); i++) {
            int cp = str.codePointAt(i);
            if (cp > 65535) {
                if (Character.isLowSurrogate(str.charAt(i))) {
                    continue;
                }
            }
            switch (Character.getType(cp)) {
                case Character.COMBINING_SPACING_MARK:
                case Character.ENCLOSING_MARK:
                case Character.NON_SPACING_MARK:
                    // do nothing
                    break;
                default:
                    if (cp == '\n' || cp == BlackenKeys.KEY_ENTER || cp == BlackenKeys.KEY_NP_ENTER || cp == '\r') {
                        ret[1] = i;
                        break out;
                    } else if (cp == '\b' || cp == BlackenKeys.KEY_BACKSPACE) {
                        ret[0]--;
                    } else if (cp == '\t' || cp == BlackenKeys.KEY_TAB) {
                        ret[1] = i;
                        break out;
                    } else {
                        ret[0]++;
                    }
                    break;
            }
        }
        return ret;
    }

}
