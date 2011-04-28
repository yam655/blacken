package com.googlecode.blacken.examples;

import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.TerminalInterface;

public class Simple {

    protected TerminalInterface term = null;
    private boolean quit;
    
    Simple() {
        term = new SwingTerminal();
        loop();
        term.quit();
    }
    Simple(TerminalInterface term) {
        this.term = term;
    }

    protected boolean loop() {
        int ch = BlackenKeys.NO_KEY;
        term.puts("Terminal Interface\n");
        term.puts("Press F10 to quit.\n");
        while (!this.quit) {
            term.puts(">");
            // getch automatically does a refresh
            ch = term.getch();
            term.puts(BlackenKeys.toString(ch));
            if (ch == BlackenKeys.KEY_F10) {
                this.quit = true;
            }
            term.puts("\n");
        }
        term.refresh();
        return this.quit;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        new Simple();
    }

}
