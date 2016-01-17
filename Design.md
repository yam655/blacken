# Introduction #

The key design philosophy is to be a Curses-like library while providing features not possible with Curses. There are specific features I want to be available for a Roguelike game.


# Details #

The event model is like Curses:
  * The AWT event loop happens in another thread, so as far as the application is concerned, when it waits for a `getch()` the application is sleeping -- just like in Curses.
  * As in ncurses, mouse events are flagged with a special "MOUSE\_EVENT" keycode. Once this keycode is found, the mouse data is read by calling `getmouse()`.
  * As in ncurses, a window resize event triggers a WINDOW\_RESIZE keycode.
  * **Unlike** curses, window events are available in addition to mouse events.

Locations on screen are like Curses:
  * `y, x` or `y, x, numRows, numCols`

Keycodes are somewhat like Curses:
  * Like curses: getch() returns an int
  * Like curses: For normal typed keys, getch() returns the key
  * Like curses: For special/function keys, getch() returns specific codes.
  * **Unlike** curses: There is no attempt to maintain the same list of keys as Curses. Many of the Curses keys are no longer seen on keyboards. Our key list is based upon the key codes provided by Java.
  * **Unlike** curses: In Curses the special keys are not valid characters. In Blacken the special keys are valid Unicode codepoints (located in Private Use Areas).
  * Like curses: We use a prefix character to indicate when keys are pressed with modifiers like Alt.
  * **Unlike** curses: Our modifier prefix is not an escape character. It's a codepoint on a particular Unicode plan, the bits of which indicate the modifier states in play.