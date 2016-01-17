# Introduction #

Any time there are multiple people contributing to a project there is a need for a common coding style.

Most of Blacken is written in Java, so that is currently the only language for which we have a style. If we leveraged XML or Groovy we would want to extend the style guide to those languages as well.

At some point we may also have a documentation style guide.

# Java Style #

## English description of style ##

In general we follow Java standards for coding style.

The idea being that if you're going to write in Java, you do this professionally as well (because few people would want to write in Java for fun). As such, we need to conform to standard Java coding style so that we don't get in to any bad habits.

### TAB characters and indentation ###

Viewing code shouldn't require any special tools or special tool preparation. This means we need to accept the fact that the _TAB character has a fixed size: 8 spaces._

There are three ways to store indentation in files:

  1. "hard" tabs - these are literal `TAB` characters
  1. "soft" tabs - these are exclusively `Space` characters.
  1. "mixed" tabs - these use combination of `TAB` and `Space` characters.

With the rise of Python, mixed tabs _should be_ effectively dead. (Python explicitly does not work with mixed tabs.)

I was pretty sure they weren't even an option in modern editors. Then I found that Eclipse can't specify a TAB size of 8 with an indent size of 4 and that it performs no source cleanup on copy/paste. You need to tell Eclipse to "Insert spaces for tabs" and _never_ copy from a source that has a tab or a simple copy/paste from another source can result in mixed-tab files. (The inability to specify a different TAB size from indent size is a sign of a fundamentally broken editor.)

NetBeans, on the other hand, fully supports using a TAB size of 8 and an indent size of 4. This is the "Editor" options, and involves checking "Expand Tabs to Spaces", setting "Number of Spaces per Indent" to 4, and "Tab Size" to 8.

VI and Emacs both support a different indent size from TAB size. If we get code checked in to the tree with bad tabs you should expect the whole commit to be reverted until you fix it. If you submit a patch with bad tabs you should expect your patch to be rejected.

The tab policy is clearly documented and if you fail to read and comply with the tab policy it is expected that you will also fail to read or comply with other code style, design, and policy guidelines. If you have questions about the TAB style please ask.

For Blacken, _we want four space "soft" tabs._

### One line conditionals ###

One line conditional statements should have braces.

Like so:

```
if (var == 1) {
    var=2;
}
```

### Line length ###

There's a _strong preference for 80 column lines_. It can be broken when it makes sense to do so, (so it accounts for editor line wrapping which is moderately broken as in Eclipse), but in general 80 columns is preferred.

_When it comes to documentation 80 columns is a firm rule._ There's no reason not to wrap JavaDoc and other comments at 80 columns.

The reasoning: We're emulating a Terminal window with the Blacken user-interface. People prone to Roguelike games with ASCII/Unicode graphics are likely to be people comfortable on the command-line. While you may be able to have hundreds of columns in that fancy GUI editor, you have to accept that another developer may have his massive screen filled with a dozen 80x25 windows each dedicated to a different task.

### JavaDoc ###

All public functions and classes should have JavaDocs.

JavaDocs should wrap at 80 columns.

The first line of a JavaDoc should give a brief description of the function/class -- enough that if the rest is truncated (due to being shown in a Tooltip) it is clear why this function/class is different from another.

JavaDoc text should be meaningful. Repeating the name of the function, class, or parameter is useless in and of itself. If you have nothing to add which is not self-described in the name consider leaving it blank.

The primary documentation language of Blacken is English. Patches to Blacken must both have a JavaDoc and that JavaDoc must be in English.

## Described by way of astyle ##

Astyle <http://astyle.sourceforge.net/astyle.html> is a simple code reformatter.

Compared to GNU indent it has a lot fewer options, but it does this by generally being sane instead of needing to be configured to be sane.

This may be extended later, but right now we're going with:

```
astyle --mode=java --style=java --add-brackets --convert-tabs
```

Broken down, this means:

  * `--mode=java` : We're formatting Java code
  * `--style=java` : Start with standard Java style
  * `--add-brackets` : One-line conditional statements should always have brackets
  * `--convert-tabs`: Convert hard TAB characters in to spaces

Not mentioned is the indentation size -- the default in `astyle` is four.

This does no formatting of comments at all -- which is useful considering how frequently that goes wrong. Comment formatting can then be done by hand.

# Non-style Policies #

## Serializable objects and `serialVersionUID` ##

There are two common ways to assign a `serialVersionUID`:

  * Start at 0 and add one when there's a change.
  * Use a tool to "generate" a `serialVersionUID`.

Since these are used in the serialization process to know what fields are stored and in what format, it isn't something that can be ignored or treated lightly.

Wait, you said Java handles that for you automatically? How do you think it knows when the data has changed? It checks the `serialVersionUID`.

What this means for us is that we need to change the `serialVersionUID` whenever we make a change that would break serialization.

In a perfect world, we'd self-monitor changes in our data format and take back serialization/deserialization from the Java libraries so we could maintain some backward compatibility. This is our goal, especially considering some of our classes (like Grid) can be generally useful and are to be expected to be directly serialized by developers using Blacken.

To work toward that perfect goal, we need to monitor, track, and test serialization and deserialization of our objects. We want our tests to break when serialization changes before we get people complaining about how their saved games no longer work.

Tracking and testing the `serialVersionUID` is more complicated when it is generated by a tool. There is no implicit order in a pile of generated `serialVersionUID`s like there is in a sequential list of numbers that start at 0 and steadily increase.

For Blacken, _serialVersionUID starts at zero, increases by one, and is manually tracked with the serialization-impacting class changes._

Additionally, _whenever possible serialization should support backward compatibility_.