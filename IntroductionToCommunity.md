# Introduction #

I publicized Blacken at Temple of the Roguelike today.

http://roguetemple.com/forums/index.php?topic=1829.0

# Details #

I'm a long-time Roguelike fan, first time poster.

I missed the annual Roguelike Release day, but I don't know when my schedule will change, so I'm going to talk about this now.

I've been working on a library to assist with UI for Roguelike games in Java. (Why Java? I need to know Java for work.) I call it Blacken: http://code.google.com/p/blacken/

It is in development, but has reached a point where further development on my part will likely be tied to my work on a Roguelike of my own.

It has zero support for graphical tiles. Games with graphical tiles need minimaps. Games with ASCII/Unicode graphics **are** the minimaps -- just more usable.

The library includes some sample code, but only one of the samples resembles a roguelike game and it was done in less than a week of work-time and looks like it.

Some features it has that I wish more Roguelikes had:

1. It has support for arbitrary color palettes. Xterm-256 color palettes are supported out-of-the-box, but you can create your own and there is no length limit.

It should also support changing a palette without needing to explicitly redraw the entire screen (as you see in real palette-based environments) but that has not yet been tested.

It also supports named colors and also (or additionally) has support for the SVG/HTML color names.

2. It supports arbitrarily resizing the window.

It has a concept of a "preferred size" for a Roguelike window (in terms of rows and columns) which it will try to maintain that size by resizing the font as appropriate.

As a developer if you're writing a game for a 80x25 display, you know you always have at least 80x25 available. It may actually be 80x100, or it may be 256x25, but it is always at least 80x25.

This means that maximizing the window generally zooms in, making the font bigger, but stretching a single dimension, expands the number of rows or columns.

I may add something like the Linux/X Control-Alt-Plus/Control-Alt-Minus feature where you can switch between some established min. sizes for windows without requiring explicit code for this in-game. (They will probably be 80x25, 80x50, 132x25, 132x60.)

3. It supports zero-width walls.

So, the X/Open Curses standard appears to have an entirely unsupported feature allowing a cell to have all four walls to have a line on them.

I call it "cell walls", and currently it is always just the foreground color. I may allow it to be something different as well at some point, but this works for now.

This was an early requirement for me, and one of the reasons Blacken doesn't have a Curses output mode (it only supports Swing right now).

I want a survival-horror Roguelike where you can be pulled through a window. Sure, you can do this without zero-width walls, but I think the impact will be heightened if, for instance, the only way to prevent the boarded-up window from being torn apart is if you stand at it and attack the creatures on the other side and if you don't have a projectile weapon you can only attack the creature directly next to you that just also happens to be on the other side of the window.

4. It supports Unicode.

A single cell in Blacken is defined as taking a String to describe the image -- not a 'char'. This is because there are numerous zero-width codepoints that you should be able to stack.

When a single codepoint is required, the standard requires an 'int' not a 'char'. Java 'char's are too small to hold a whole Unicode codepoint outside of the Basic Multilingual Plane. To pass a whole codepoint, you need to accept a pair of 'char's -- so you can get both halves of a surrogate pair -- or you need to just accept an 'int'. -- No one wants to deal with surrogate pairs, so Blacken just uses 'int's.

getch() returns an 'int' which is a valid Unicode codepoint. Keys which do not map to Unicode characters are assigned to values from one of the Private Use Planes. We handle modifier keys similar to what Linux/X does with the 'ESC' prefix, except we use a prefix pulled from another Private Use Plane which maps to all of the modifier keys.

5. It has support for font attributes other than foreground color.

It uses the same palette for foreground and background of text. -- I've noticed a lot of Roguelikes avoid background color, but then most Roguelikes also seem to stick to the old 16 color palette.

It also It has support for monochrome font attributes, but I've not tested that yet.

6. It uses GPL v3.

It does not use the LGPL, which means if you use Blacken, you must use the GPL.

This is more a feature for the players and for developers of mods/variants of your game.

This was done because as a player, I hate finding a Roguelike game I'd like to play, only to find the developer has stopped maintaining it and there isn't a version for my platform. I hate this the most when it is clear the Roguelike wants to use an older version of a library I have -- in these cases a simply recompile may be all I need, but I can't do it because I don't have the source.

If you want any more clear example of just how severe of an issue this can be consider all of the Angband variants, then consider that Robert Alan Koeneke was surprised when he got on Usenet and found a group dedicated to Moria as he didn't know that people were still playing it. Had he not open-sourced Moria, Angband would have never existed, and -- as an aside -- I would have never gotten serious in to programming. (I started programming to cheat at the PC port of Jim Wilson's Unix port of Moria. -- Remember Moria started on VAX.)

Comparisons to the competition:

The nearest competing library is libjcsi. Both are Java libraries with Swing interfaces available.

Now, I've not looked at libjcsi since before I started working on Blacken, so some of this may no longer be accurate.

libjcsi supports other UI backends including Curses. Blacken only supports Swing at this point, and will never support Curses.

libjcsi doesn't support background colors. Blacken does.

libjcsi doesn't support resizing the window. Blacken does.

libjcsi follows the GUI standard of Column then Row. Blacken follows the Curses standard of Row then Column.

Both use threading to mask the Swing event-driven mechanism and make it appear to be more like Curses.

The big one is Blacken is GPL and libjcsi is not.

Blacken should currently have a super-set of the functionality of libjcsi. However they are definitely not drop-in replacements.

Long-term direction:

Aside from some bug fixes and testing of some features that may or may not currently work like wide-character support and additional font styles and officially supporting Java 7, there are a few cases where I have some big ideas.

A. Support for a Blacken "virtual font" allowing different regions to be mapped to different physical fonts.

This should allow for real support for nearly all of the Unicode codepoints -- including Egyptian Heiroglyphs.

B. Support for line-drawing (maybe an SVG subset) graphical substitutions.

The ultimate goal here is to turn the traditional ASCII/Unicode map in to something resembling a real map. Buildings should look like blueprints using the same visual shortcuts. This would mean that we know which direction doors open.


---


Update 2012-03-26: Blacken has moved to the Apache 2.0 license.