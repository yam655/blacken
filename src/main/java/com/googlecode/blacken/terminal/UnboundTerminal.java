package com.googlecode.blacken.terminal;


import java.util.EnumSet;


public class UnboundTerminal extends AbstractTerminal {

    public UnboundTerminal() {
        super();
    }

    @Override
    public void disableEventNotice(BlackenEventType event) {
        
    }
    
    @Override
    public void disableEventNotices() {
        
    }

    @Override
    public void enableEventNotice(BlackenEventType event) {
        
    }

    @Override
    public void enableEventNotices(EnumSet<BlackenEventType> events) {
        
    }

    @Override
    public int getch() {
        this.refresh();
        return BlackenKeys.NO_KEY;
    }

    @Override
    public EnumSet<BlackenModifier> getLockingStates() {
        return null;
    }

    @Override
    public BlackenMouseEvent getmouse() {
        return null;
    }

    @Override
    public BlackenWindowEvent getwindow() {
        return null;
    }

    @Override
    public void set(int y, int x, TerminalCell tcell) {
        grid.get(y, x).set(tcell);
        grid.get(y, x).setDirty(false);
        
    }

}
