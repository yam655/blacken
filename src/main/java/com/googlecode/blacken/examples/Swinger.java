package com.googlecode.blacken.examples;

import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.colors.ColorPalette;
import com.googlecode.blacken.swing.SwingTerminal;
import com.googlecode.blacken.terminal.BlackenKeys;
import com.googlecode.blacken.terminal.BlackenModifier;
import com.googlecode.blacken.terminal.TerminalInterface;

public class Swinger {
    private TerminalInterface term = null;
    private boolean quit = false;
    private ColorPalette palette;
    
    Swinger() {
        term = new SwingTerminal();
        term.init();
        palette = ColorNames.addXtermColors(null, 256);
        ColorNames.addSvgColors(palette);
        term.setPalette(palette);
        loop();
        term.quit();
    }
    Swinger(TerminalInterface term, ColorPalette palette) {
        this.term = term;
        this.palette = palette;
    }

    public void showHelp() {
        
    }
    
    
    public boolean loop() {
        int ch = BlackenKeys.NO_KEY;
        term.enableEventNotices(null);
        term.puts("Terminal Interface\n");
        term.puts("Press F10 to quit.\n");
        term.puts(">");
        while (ch != BlackenKeys.KEY_F10) {
            ch = term.getch();
            
            if (BlackenKeys.isModifier(ch)) {
                term.puts(BlackenModifier.getModifierString(ch).toString());
            } else if (BlackenKeys.isKeyCode(ch)) {
                term.puts(BlackenKeys.getKeyName(ch));
                if (ch == BlackenKeys.RESIZE_EVENT) {
                    term.puts("\nYummy window resize!");
                }
                if (ch == BlackenKeys.MOUSE_EVENT) {
                    term.puts("\n");
                    term.puts(term.getmouse().toString());
                }
                if (ch == BlackenKeys.WINDOW_EVENT) {
                    term.puts("\n");
                    term.puts(term.getwindow().toString());
                }
                term.puts("\n>");
                
            } else {
                term.puts(BlackenKeys.toString(ch));
                term.puts("\n>");
            }
        }
        return this.quit;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        new Swinger();
    }

}
