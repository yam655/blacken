/* blacken - a library for Roguelike games
 * Copyright © 2010-2012 Steven Black <yam655@gmail.com>
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
package com.googlecode.blacken.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
import java.util.regex.*;

/**
 * An extended random class
 * 
 * @author yam655
 */
public class Random extends java.util.Random {
    
    private static final long serialVersionUID = 3049695947451276476L;
        
    private final Pattern guessPattern = 
        Pattern.compile("\\s*(\\d+)?\\s*(?:([:])\\s*(\\d+))??\\s*(?:([d:])\\s*(\\d+))?\\s*(?:([+-/*])\\s*(\\d+))?\\s*"); //$NON-NLS-1$
    /**
     * Create a new random number generator
     */
    public Random() {
        // empty
    }

    /**
     * @param arg0 random number seed
     */
    public Random(long arg0) {
        super(arg0);
    }

    /**
     * Find best <code>num</code> out of <code>outof</code> dice with 
     * <code>sides</code>.
     * 
     * @param num best number of dice to use
     * @param outof total number of dice to use
     * @param sides number of sides on the dice
     * @return sum of best <code>num</code> out of <em>outof</em><b>d</b><em>sides</em>
     */
    public int bestOf(int num, int outof, int sides) {
        // 
        if (num > outof) {
            int t = num;
            num = outof;
            outof = t;
        }
        ArrayList<Integer> arr = new ArrayList<Integer>(outof);
        int ret = 0;
        while(outof > 0) {
            arr.add(dice(1, sides));
            outof -= 1;
        }
        Collections.sort(arr);
        while(num > 0) {
            ret += arr.get(arr.size()-1);
            num -= 1;
        }
        return ret;
    }
    /**
     * Find the best <code>num</code> numbers in the <code>group</code>
     * 
     * @param num number of integers to use
     * @param group group of integers to use
     * @return sum of best <code>num</code> integers in <code>group</code>
     */
    public int bestOf(int num, List<Integer> group) {
        int outof = group.size();
        if (num > outof) {
            throw new IllegalArgumentException();
        }
        ArrayList<Integer> arr = new ArrayList<Integer>(group);
        int ret = 0;
        Collections.sort(arr);
        while(num > 0) {
            ret += arr.get(arr.size()-1);
            num -= 1;
        }
        return ret;
    }
    
    /**
     * Find the best <code>num</code> out of <code>outof</code> using the
     * guessed string <code>g</code>.
     * 
     * @param num number of integers to use
     * @param outof number of integers to check
     * @param g random string parsed using {@link #guess(String)}
     * @return sum of best <code>num</code> out of <code>outof</code> runs of <code>g</code>
     */
    public int bestOf(int num, int outof, String g) {
        if (num > outof) {
            int t = num;
            num = outof;
            outof = t;
        }
        ArrayList<Integer> arr = new ArrayList<Integer>(outof);
        int ret = 0;
        while(outof > 0) {
            arr.add(guess(g));
            outof -= 1;
        }
        Collections.sort(arr);
        while(num > 0) {
            ret += arr.get(arr.size()-1);
            num -= 1;
        }
        return ret;
    }
    
    /**
     * Get a random choice from a List
     * 
     * <p>If the List does not support RandomAccess, it ends up calling
     * {@link #choice(Collection)}.</p>
     * 
     * @param <T> type contained in the List
     * @param c List from which to pull a random item
     * @return random item from List
     */
    public <T> T choice(List<T> c) {
        for (Class<?> cls : c.getClass().getInterfaces()) {
            if (cls.equals(RandomAccess.class)) {
                T ret = c.get((int)(nextFloat() * c.size()));
                return ret;
            }
        }
        return choice((Collection<T>)c);
    }
    
    /**
     * Get a random choice from a Collection
     * 
     * <p>This calls {@link Collection#toArray()} so if that is slow, this will
     * be slow.</p>
     * 
     * @param <T> type contained in the Collection
     * @param c Set from which to pull a random item
     * @return random item from Collection
     */
    public <T> T choice(Collection<T> c) {
        Object[] a = c.toArray();
        @SuppressWarnings("unchecked")
        T ret = (T)a[(int)(nextFloat() * a.length)];
        return ret;
    }

    /**
     * Emulate a dice roll and return the sum.
     * 
     * @param num number of dice to sum
     * @param sides number of sides on the dice
     * @return sum of dice
     */
    public int dice(int num, int sides) {
        int ret = 0;
        while(num > 0){
            ret += nextInt(sides) + 1;
            num -= 1;
        }
        return ret;
    }
    
    /**
     * Get all possible results from a set of dice rolls.
     * 
     * @param num number of dice used
     * @param sides number of sides on each die
     * @return list of results
     */
    public List<Integer> diceList(int num, int sides) {
        List<Integer> ret = new ArrayList<Integer>(num);
        for (int c = 0; c < num; c++) {
            ret.add(nextInt(sides) + 1);
        }
        return ret;
    }
    /**
     * Guess the way to turn the string to a randomized number.
     * 
     * <p>We support the following types of strings:
     * <ul>
     *   <li>"42": simple absolute string</li>
     *   <li>"10:20": simple random range (inclusive between 10 and 20)</li>
     *   <li>"d6": synonym for "1d6"</li>
     *   <li>"3d6": sum of 3 6-sided dice</li>
     *   <li>"3:4d6": best 3 of 4 6-sided dice</li>
     * </ul></p>
     * 
     * <p>We support the following suffixes for the supported types:
     * <ul>
     *   <li> "+4": add 4 to the value
     *   <li> "-3": subtract 3 from the value
     *   <li> "*100": multiply value by 100
     *   <li> "/8": divide value by 8
     * </ul></p>
     * 
     * @param str string to guess
     * @return random number
     */
    public int guess(String str) {

        Matcher mat = guessPattern.matcher(str);
        int ret = 0;
        if (mat.matches()) {
            String num1 = mat.group(1); // 12
            String wmode = mat.group(2); // :
            String wnum = mat.group(3); // 23
            String mode = mat.group(4); // db
            String num2 = mat.group(5); // 34
            String pmode = mat.group(6); // +-
            String pnum = mat.group(7); // 45
            int a, b, w, p;
            a = num1 == null ? 0 : Integer.parseInt(num1);
            b = num2 == null ? 0 : Integer.parseInt(num2);
            w = wnum == null ? 0 : Integer.parseInt(wnum);
            p = pnum == null ? 0 : Integer.parseInt(pnum);
            if (num1 != null && num2 != null) {
                if (wnum != null) {
                    if (":".equals(wmode)) { //$NON-NLS-1$
                        if ("d".equals(mode)) { //$NON-NLS-1$
                            ret = bestOf(a, w, b);
                        }
                    }
                } else if ("d".equals(mode)) { //$NON-NLS-1$
                    ret = dice(a, b);
                } else if (":".equals(mode)) { //$NON-NLS-1$
                    ret = nextInt(a, b+1);
                }
            } else if (num1 != null) {
                if (":".equals(wmode)) { //$NON-NLS-1$
                    ret = nextInt(a, w+1);
                } else {
                    ret = a;
                }
            } else if (num2 != null) {
                if ("d".equals(mode)) { //$NON-NLS-1$
                    ret = dice(1, b);
                } else if (":".equals(mode)) { //$NON-NLS-1$
                    ret = nextInt(0, b+1);
                }
            } else {
                if (":".equals(wmode)) { //$NON-NLS-1$
                    ret = nextInt(0, w+1);
                }
            }
            if ("+".equals(pmode)) { //$NON-NLS-1$
                ret += p;
            } else if ("-".equals(pmode)) { //$NON-NLS-1$
                ret -= p;
            } else if ("*".equals(pmode)) { //$NON-NLS-1$
                ret *= p;
            } else if ("/".equals(pmode)) { //$NON-NLS-1$
                ret /= p;
            }
        }
        return ret;
    }
    /**
     * Find the next integer within a range.
     * 
     * @param bottom inclusive bottom
     * @param top exclusive top
     * @return n where bottom <= n < top
     */
    public int nextInt(int bottom, int top) {
        int diff = top - bottom;
        return bottom + nextInt(diff);
    }

    /**
     * Shuffle a List in-place.
     * 
     * <p>This just calls {@link Collections#shuffle(List, java.util.Random)}.</p>
     * 
     * @param <T> type contained in the List
     * @param list list to shuffle
     */
    public <T> void shuffle(List<T> list) {
        Collections.shuffle(list, this);
    }

}
