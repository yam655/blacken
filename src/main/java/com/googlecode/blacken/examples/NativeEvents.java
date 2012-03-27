/* blacken - a library for Roguelike games
 * Copyright © 2010, 2011 Steven Black <yam655@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.googlecode.blacken.examples;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.googlecode.blacken.colors.ColorNames;
import com.googlecode.blacken.nativeswing.AwtPalette;
import com.googlecode.blacken.nativeswing.TerminalPanel;
import com.googlecode.blacken.swing.AwtCell;

/**
 * Example application using native events.
 * 
 * <p>In part this was to answer the question "Would it be faster and more
 * responsive if it were rearchitected?" The answer to that appears to be: No.
 * If you run this and mash on the keyboard it gets just as behind as the
 * earlier version of BlackenPanel.</p>
 * 
 * <p>It has the added benefit of adding a bit of flexibility to folks who want
 * a terminal-like window, but also want do be more Java-like.</p>
 * 
 * @author yam655
 *
 */
public class NativeEvents implements KeyListener {

    private TerminalPanel gui = null;
    private AwtPalette palette = null;
    private JFrame frame = null;

    NativeEvents() {
        palette = new AwtPalette();
        palette.addMapping(ColorNames.SVG_COLORS);
        init("Native Events", 25, 80); //$NON-NLS-1$
        frame.addKeyListener(this);
        help();
    }

    /**
     * 
     * @param name name of window
     * @param rows rows
     * @param cols columns
     */
    public void init(String name, int rows, int cols) {
        frame = new JFrame(name);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);

        gui = new TerminalPanel();
        frame.setSize(480, 640); // resized later
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setBackground(palette.get("Black")); //$NON-NLS-1$
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gui);
        frame.setCursor(null);
        frame.pack();
        
        AwtCell empty = new AwtCell();
        gui.init(font, rows, cols, empty);

        gui.resizeFrame(frame, 0);
        frame.setLocationRelativeTo(null); // places window in center of screen
        frame.setResizable(false); // resizing not implemented right now
        gui.mv(0, 0);

        frame.setVisible(true);
    }

    /**
     * Show the help message.
     */
    public void help() {
        gui.clear();
        gui.setCurBackground(palette.get("Black")); //$NON-NLS-1$
        gui.setCurForeground(palette.get("White")); //$NON-NLS-1$
        gui.puts("Terminal Interface\n"); //$NON-NLS-1$
        gui.puts("Press F10 to quit.\n"); //$NON-NLS-1$
        gui.puts(">"); //$NON-NLS-1$
        gui.refresh();
    }

    private void showSomething(Object obj) {
        gui.puts(obj.toString());
        gui.puts("\n"); //$NON-NLS-1$
        gui.puts(">"); //$NON-NLS-1$
        gui.refresh();
    }

    /**
     * Start the application.
     * 
     * @param args command-line arguments
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        new NativeEvents();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // in variantKeyMode all keys are handled here.
        // In the normal key mode, only action keys are handled here.
        switch(e.getKeyCode()) {
        case KeyEvent.VK_F10:
            quit();
            break;
        default:
            this.showSomething(e);
            break;
        }
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }
    /**
     * Quit the frame/app
     */
    public void quit() {
        frame.dispose();
    }

    
}
