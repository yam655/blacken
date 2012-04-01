README for Blacken
==================

What is Blacken?
----------------

Blacken is a library to assist in the creation of 100% native Java Roguelike 
games.

Examples of Roguelike games include: Angband, Moria, Larn, Nethack, Rogue, 
Omega, ADOM, Dungeon Crawl, etc.

I can't get it to work. What is Blacken supposed to do?
-------------------------------------------------------

Blacken is not a game. It is a library to assist in the development of games.

This means that Blacken does nothing on its own. If you're a player you should
never have to install Blacken manually.

If you're a developer, you should check out the "blacken-dev" Google Group and
read up on the details on the Blacken wiki located at 
<http://blacken.googlecode.com/>.

What are the requirements for Blacken?
--------------------------------------

Blacken was written with Java 7 in mind. Java 7 is the first version of Java
to support a modern version of the Unicode standard. This means that to build
Blacken or to run a game using Blacken, you need at least Java 7.

Note that when Java 8 is released, we'll be moving to that ASAP due to the 
addition of JavaFX as a standard library.

Blacken heavily leans upon standard Java components.

We rely upon Maven to build Blacken. Blacken's sole external dependency is
Junit -- but note that because of Maven this will be downloaded automatically.

How do I build Blacken?
-----------------------

Make sure you have Java 7 and Maven installed, then run:

    mvn package

Once that finishes successfully, there should be jars located in the "target"
directory.
