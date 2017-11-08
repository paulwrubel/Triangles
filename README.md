# Triangles
---
A fun processing project about triangles that shoot bullets

Supported by [processing](https://processing.org/) libraries.

@version 1.0

@author [Paul Wrubel (VoxaelFox)](https://github.com/paulwrubel)

##### Features
- Triangles aim at mouse
- Triangles can move toward, away from, and orbit the mouse
- Trianges can shoot bullets.
- Bullets can be attracted to points gravitationally
- Dynamic Gravity Modes
- Color is based on mouse position (Background) and heading (Triangles/Bullets) 
- Perspective Movement! (Alpha)

#### Controls
| Key              | Control       | Note
|:----------------:|:------------- |:----
| *Right Click*    | Create Triangle at cursor | Can be held in Dynamic mode
| *Left Click*     | Shoot Bullet from all Triangles on screen at cursor| Can be held in Dynamic mode
| *Center Click*   | Set gravity point | Valid in all gravity modes
| *Up Arrow*       | Move all Triangles towards cursor | Can be held
| *Down Arrow*     | Move all Triangles away from cursor | Can be held
| *Left Arrow*     | All Triangles orbit cursor anti-clockwise | Can be held
| *Right Arrow*    | All Triangles orbit cursor clockwise | Can be held
| *i*              | Change perspective up | Can be held in Dynamic mode / blocky in Static mode
| *k*              | Change perspective down | Can be held in Dynamic mode / blocky in Static mode
| *j*              | Change perspective left | Can be held in Dynamic mode / blocky in Static mode
| *l*              | Change perspective right | Can be held in Dynamic mode / blocky in Static mode
| *u*              | Change perspective out | Can be held in Dynamic mode / blocky in Static mode
| *o*              | Change perspective in | Can be held in Dynamic mode / blocky in Static mode
| *r*              | Resets the perspective | Any mode
| *Space*          | Clears all Triangles and Bullets from the window | Any mode
| *c*              | Clears all Bullets from the window | Any mode
| *Enter / Return* | Toggles Dynamic / Static mode | Any mode
| *b*              | Toggles bounce mode | Allows Bullets to bounce off the sides of the window
| *g*              | Toggles gravity mode | See Gravity Mode Section

#### Gravity Modes
| Gravity Mode | Description
|:------------:|:-------------
| OFF          | No acceleration / Linear motion
| SIMPLE       | Constant gravity acceleration towards mouse cursor
| TRUE         | Gravity acceleration toward cursor changes with distance (emulates real life gravity)
| POINT        | True gravity towards a specified point on screen (defaults to center)
| MULTI-POINT  | True gravity towards multiple points on screen (defaults to one point in center)                          
