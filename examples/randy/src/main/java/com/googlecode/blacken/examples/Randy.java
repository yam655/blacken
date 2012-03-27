/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
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
package com.googlecode.blacken.examples;

import com.googlecode.blacken.core.Random;

/**
 * @author yam655
 *
 */
public class Randy {

    /**
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        int errs = 0;
        if (args.length == 0 || "--help".equals(args[1]) 
                || "-h".equals(args[1])) {
            System.out.println("Randy [args]");
            System.out.println("     Simple test program for ExtRandom");
            System.out.println("Arguments may be:");
            System.out.println("    -h | --help : this help text\n");
            System.out.println("    {random statement} : ministatement used to "
                    + "generate random number");
            System.out.println("Random statement may be:");
            System.out.println("    (\\d+)?(([:])(\\d+))??(([d:])(\\d+))?(([+-/"
                    + "*])(\\d+))");
            System.exit(args.length == 0 ? 1 : 0);
        }
        Random r = new Random();
        if (args.length == 1) {
            int got = r.guess(args[1]);
            System.out.printf("%d\n", got);
        }
        System.exit(errs);
    }

    /**
     * 
     */
    public Randy() {
        // do nothing
    }

}
