/**
 * Copyright © 2004-2011, Steven Black. All Rights Reserved.
 */
package com.googlecode.blacken.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.*;

/**
 * @author Steven Black
 */
public class ExtRandom extends java.util.Random {
    
    /**
     * serial UID
     */
    private static final long serialVersionUID = 3049695947451276476L;
    /**
     * @param args
     * @return number of errors
     */
    public static int main(String[] args) {
        int errs = 0;
        if ("--help".equals(args[1]) || "-h".equals(args[1])) {
            System.out.printf("%s [args]\n", args[0]);
            System.out.printf("    Simple test program for ExtRandom\n");
            System.out.printf("Arguments may be:\n");
            System.out.printf("    -h | --help : this help text\n");
            System.out.printf("    {random statement} : ministatement used to generate random number\n");
            System.out.printf("Random statement may be:\n");
            System.out.printf("    (\\d+)?(([:])(\\d+))??(([db:])(\\d+))?(([+-/*])(\\d+))");
        }
        ExtRandom r = new ExtRandom();
        if (args.length == 1) {
            int got = r.guess(args[1]);
            System.out.printf("%d\n", got);
        } else {
            if (r.nextBoolean()) {
                System.out.printf("true\n");
            } else {
                System.out.printf("false\n");
            }
        }
        return errs;
    }


    
    private final Pattern guessPattern = 
        Pattern.compile("\\s*(\\d+)?\\s*(?:([:])\\s*(\\d+))??\\s*(?:([db:])\\s*(\\d+))?\\s*(?:([+-/*])\\s*(\\d+))?\\s*");
    /**
     * 
     */
    public ExtRandom() {
    }

    /**
     * @param arg0
     */
    public ExtRandom(long arg0) {
        super(arg0);
    }

    public int bell(int mean, int var) {
        // produce a bell curve with a `mean` and +/- `var`.
        int a = mean - var;
        int b = mean + var;
        int t = 0;
        final int count = 3;
        for (int d = 0; d < count; d++) {
            t += nextInt(a, b+1);
        }
        return t / count;
    }
    public List<Integer> bellList(int count, int mean, int var) {
        List<Integer> ret = new ArrayList<Integer>(count);
        for (int c = 0; c < count; c++) {
            ret.add(bell(mean, var));
        }
        return ret;
    }

    public int bestOf(int num, int outof, int sides) {
        // Find best `num` out of `outof` dice with `sides`.
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
    
    public int bestOf(int num, int outof, String g) {
        // Find best `num` out of `outof` dice with `g`.
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
    public <T> T choice(List<T> list) {
        T ret = list.get((int)(nextFloat() * list.size()));
        return ret;
    }

    public int dice(int num, int sides) {
        // """ produce a result of `num` dice, each with `sides` sides. """
        int ret = 0;
        while(num > 0){
            ret += nextInt(sides) + 1;
            num -= 1;
        }
        return ret;
    }
    public List<Integer> diceList(int num, int sides) {
        List<Integer> ret = new ArrayList<Integer>(num);
        for (int c = 0; c < num; c++) {
            ret.add(nextInt(sides) + 1);
        }
        return ret;
    }
    public int guess(String str) {
        /* Guess the way to turn the string to a randomized number

        We support the following types of strings:

        - "42": simple absolute string
        - "10:20": simple random range (inclusive between 10 and 20)
        - "d6": synonym for "1d6"
        - "3d6": sum of 3 6-sided dice
        - "3:4d6": best 3 of 4 6-sided dice
        - "42b12": possible values are on a bell curve
            with 42 being the median, and the edges being +/- 12

        We support the following suffixes for the supported types:

        <li> "+4": add 4 to the value
        <li> "-3": subtract 3 from the value
        <li> "*100": multiply value by 100
        <li> "/8": divide value by 8

        */

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
                    if (":".equals(wmode)) {
                        if ("d".equals(mode)) {
                            ret = bestOf(a, w, b);
                        } else if ("b".equals(mode)) {
                            // XXX this is bogus, need another arg
                            //ret = bestOf(a, bellList(w, w, b));
                            ret = 0;
                        }
                    }
                } else if ("d".equals(mode)) {
                    ret = dice(a, b);
                } else if ("b".equals(mode)) {
                    ret = bell(a, b);
                } else if (":".equals(mode)) {
                    ret = nextInt(a, b+1);
                }
            } else if (num1 != null) {
                if (":".equals(wmode)) {
                    ret = nextInt(a, w+1);
                } else {
                    ret = a;
                }
            } else if (num2 != null) {
                if ("d".equals(mode)) {
                    ret = dice(1, b);
                } else if (":".equals(mode)) {
                    ret = nextInt(0, b+1);
                }
            } else {
                if (":".equals(wmode)) {
                    ret = nextInt(0, w+1);
                }
            }
            if ("+".equals(pmode)) {
                ret += p;
            } else if ("-".equals(pmode)) {
                ret -= p;
            } else if ("*".equals(pmode)) {
                ret *= p;
            } else if ("/".equals(pmode)) {
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

    public <T> void shuffle(List<T> list) {
        Collections.shuffle(list, this);
    }

}
