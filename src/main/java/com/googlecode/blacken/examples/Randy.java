/* blacken - a library for Roguelike games
 * Copyright © 2011 Steven Black <yam655@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.blacken.examples;

import com.googlecode.blacken.core.ExtRandom;
import com.googlecode.blacken.examples.Messages;

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
        if (args.length == 0 || 
                Messages.getString("Randy.help_cmd").equals(args[1]) ||  //$NON-NLS-1$
                "-h".equals(args[1])) { //$NON-NLS-1$
            System.out.printf("Randy [%s]\n", //$NON-NLS-1$
                              Messages.getString("Randy.args")); //$NON-NLS-1$
            System.out.printf("     %s\n",  //$NON-NLS-1$
                              Messages.getString("Randy.desc")); //$NON-NLS-1$
            System.out.println(Messages.getString("Randy.args_may_be")); //$NON-NLS-1$
            System.out.printf("    -h | %s : %s\n",  //$NON-NLS-1$
                              Messages.getString("Randy.help_cmd"),  //$NON-NLS-1$
                              Messages.getString("Randy.help_desc")); //$NON-NLS-1$
            System.out.printf("    %s\n",  //$NON-NLS-1$
                              Messages.getString("Randy.statement_desc")); //$NON-NLS-1$
            System.out.println(Messages.getString("Randy.regex_desc")); //$NON-NLS-1$
            System.out.println("    (\\d+)?(([:])(\\d+))??(([d:])(\\d+))?(([+-/*])(\\d+))"); //$NON-NLS-1$
            System.exit(args.length == 0 ? 1 : 0);
        }
        ExtRandom r = new ExtRandom();
        if (args.length == 1) {
            int got = r.guess(args[1]);
            System.out.printf("%d\n", got); //$NON-NLS-1$
        } else {
            if (r.nextBoolean()) {
                System.out.println(Messages.getString("Randy.true")); //$NON-NLS-1$
            } else {
                System.out.println(Messages.getString("Randy.false")); //$NON-NLS-1$
            }
        }
        System.exit(errs);
    }

    /**
     * 
     */
    public Randy() {
        // TODO Auto-generated constructor stub
    }

}
