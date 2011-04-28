package com.googlecode.blacken.terminal;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public class BlackenWindowEvent {
    private BlackenEventType type;
    private String name;
    private String oppositeName;
    private EnumSet<BlackenWindowState> newState;
    private EnumSet<BlackenWindowState> oldState;
    
    public static List<String> 
    getWindowStateStrings(EnumSet<BlackenWindowState> set) {
        List<String> ret = new ArrayList<String>();
        for (BlackenWindowState s : set) {
            ret.add(s.name());
        }
        return ret;
    }

    public static StringBuffer 
    getWindowStateString(EnumSet<BlackenWindowState> set) {
        List<String> base = getWindowStateStrings(set);
        if (base == null) {
            return null;
        }
        StringBuffer keybuf = new StringBuffer();
        for (String name : base) {
            if (keybuf.length() != 0) {
                keybuf.append('+');
            }
            keybuf.append(name);
        }
        if (keybuf.length() == 0) {
            keybuf.append("(none)");
        }
        return keybuf;
    }
    
    public String toString() {
        return String.format("Window: %s now: %s (was: %s) now: %s (was: %s)", 
                             type.name(), name, oppositeName,
                             getWindowStateString(newState),
                             getWindowStateString(oldState));
    }

    public BlackenWindowEvent(BlackenEventType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOppositeName(String oppositeName) {
        this.oppositeName = oppositeName;
    }

    public void setNewState(EnumSet<BlackenWindowState> set) {
        this.newState = set;
    }

    public void setOldState(EnumSet<BlackenWindowState> set) {
        this.oldState = set;
    }

    public BlackenEventType getType() {
        return type;
    }

    public void setType(BlackenEventType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getOppositeName() {
        return oppositeName;
    }

    public EnumSet<BlackenWindowState> getNewState() {
        return newState;
    }

    public EnumSet<BlackenWindowState> getOldState() {
        return oldState;
    }

}
