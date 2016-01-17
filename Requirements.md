# Introduction #

We have very specific requirements for Blacken. I want to empower a wide variety of novel user interfaces for Roguelike games.

This is very incomplete.

# Details #

The following are required features:
  * color palettes
    * arbitrary size
    * easy color and palette changes
  * zero thickness walls
    * all four cell walls can become a visible border
    * default to foreround color
    * independant color to foreground
  * full Unicode support
    * (limited by poor AWT Unicode support)
    * all planes, not just BMP
    * usable for RTL languages, too!
    * embedded RTL text!
    * full double-wide support