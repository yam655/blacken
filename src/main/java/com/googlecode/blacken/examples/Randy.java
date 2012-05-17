/* blacken - a library for Roguelike games
 * Copyright © 2011,2012 Steven Black <yam655@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with this program.  
 * If not, see <http://www.gnu.org/licenses/>.
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
