# Introduction #

Blacken 1.1.1 has been officially released.

The only difference from 1.1 was a fix to a critical bug involving Mac OS X support.

# Details #

Blacken 1.1.1 was released to address a critical issue with Mac OS X support. Other than functional Mac OS X support there are no significant changes.

# Blacken 1.1 Release Announcement #

In time for the Annual Roguelike Release Party, we're proud to announce Blacken 1.1, the next major revision of Blacken, a Java library for Roguelike development.

Blacken now uses the Apache 2.0 license.

We now have a user-focused site which includes tutorials you can try out via Java WebStart: https://sites.google.com/site/blackenlib/try-it-out

It has zero support for raster-based graphical tiles. Games with graphical tiles need minimaps. Games with ASCII/Unicode graphics are the minimaps -- just more usable.

We're now in Maven Central, so use of Blacken is a matter of adding a few lines to your pom.xml:

```
<dependency>
  <groupId>com.googlecode.blacken</groupId>
  <artifactId>blacken-core</artifactId>
  <version>1.1</version>
</dependency>
```

The full release notes are online at: https://sites.google.com/site/blackenlib/current-version/release-notes

## Improvements over Blacken 1.0: ##

License change: We used to be GPLed, but now we use the Apache 2.0 license. It can be hard to change licenses on a project. This was done before we accepted any contributions to the codebase. It was the project of a single individual when we changed licenses.

Significant performance improvements: Our display updates were sluggish, though we did our best to work-around it so the response time was okay. With Blacken 1.1 the performance itself is good.

Simpler interface: The native programming interface is no longer similar to Curses at all. There is a "CursesLikeAPI" wrapper that adds back the functions I took out, but by default the class is smaller and simpler. In particular, the management of the cursor is much simpler.

## Some features: ##

Palette support: Indexed and named colors; mixing indexed colors from the palette with arbitrary 24-bit colors; palette rotation; load custom palettes from files using either our own format or GIMP palettes.

Arbitrarily resizing the window: maintains a "preferred size" changing the font size as needed; Alt-Enter for full screen mode; supports the visually impaired (as long as they can see something) by making it easy to make the font very large.

cell walls: Any cell can have walls on any of the four edges; use it for menus; use it for buttons; use it for zero-width walls where zombies can pull you through windows.

Unicode support: A cell contains a sequence of codepoints; codepoints may be outside of the Basic Multilingual Plane; some of the code-points may be zero-width accents.

Curses-style key input: all key input consists of valid Unicode codepoints; special keys are handled by codepoints in "private use" planes; modifier keys are handled through a prefix character (like the way Alt is handled with the Escape character as a prefix character).