package com.googlecode.blacken.terminal;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum BlackenWindowState {
    ICONIFIED, MAXIMIZED_HORIZ, MAXIMIZED_VERT;

    public static List<String> 
    getStateStrings(EnumSet<BlackenWindowState> set) {
        List<String> ret = new ArrayList<String>();
        for (BlackenWindowState s : set) {
            ret.add(s.name());
        }
        return ret;
    }

    public static StringBuffer getStateString(EnumSet<BlackenWindowState> set) {
        List<String> base = getStateStrings(set);
        if (base == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        for (String name : base) {
            if (buf.length() > 0) {
                buf.append('|');
            }
            buf.append(name);
        }
        if (buf.length() == 0) {
            buf.append("(none)");
        }
        return buf;
    }

}
