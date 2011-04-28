package com.googlecode.blacken.examples;

import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.TerminalInterface;

public class Stumble {

    TerminalInterface term = null;
    
    Stumble() {
        init();
        loop();
        quit();
    }

    protected void makeMap() {
        
    }
    
    protected void quit() {
        term.quit();
    }
    protected void init() {
        term = new SwingTerminal();
    }
    protected void loop() {
        int ch = BlackenKeys.NO_KEY;
        term.puts("Terminal Interface\n");
        term.puts("Press F10 to quit.\n");
        while (ch != BlackenKeys.KEY_F10) {
            term.puts(">");
            term.refresh();
            ch = term.getch();
            term.puts(BlackenKeys.toString(ch));
            term.puts("\n");
        }
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        new Stumble();
    }

}
