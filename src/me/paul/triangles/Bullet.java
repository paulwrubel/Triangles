package me.paul.triangles;

import processing.core.*;

/**
 *  @version 1.0
 *  @author Paul Wrubel - VoxaelFox
 *
 *  Holds a reference to a Bullet Object.
 *  This is what is drawn on the window.
 */

class Bullet {

    /**
     * Defaults for drawing properties
     * such as if the bullet is filled with color or hollow, and
     * stroke weight, radius, and magnitude of movement
     *
     * Also defaults for Saturation and Brightness values
     */
    private static final boolean FILL = true;
    private static final float STROKE_WEIGHT = 2;
    private static final float RADIUS = 8;
    private static final float MAG = 10;
    private static final float SAT = 60;
    private static final float BRIGHT = 85;

    /**
     * Reference to a Triangles object to draw to
     */
    private Triangles parent;

    /**
     * Location and heading of this Bullet
     */
    private float x;
    private float y;
    private float heading;

    /**
     * Constructor for a Bullet object
     * @param parent_ Reference to a PApplet class to draw to
     * @param x_ x coordinate location of this Bullet
     * @param y_ y coordinate location of this Bullet
     * @param heading_ heading (in radians) of this Bullet
     */

    Bullet(Triangles parent_, float x_, float y_, float heading_) {
        parent = parent_;
        x= x_;
        y = y_;
        heading = heading_;
    }

    /**
     * Getter method for x coordinate value
     * @return x coordinate of this Bullet
     */
    float getX() {
        return x;
    }

    /**
     * Getter method for y coordinate value
     * @return y coordinate of this Bullet
     */
    float getY() {
        return y;
    }

    /**
     * Updates information and properties about this Bullet object
     * like it's location and color
     */
    void update() {
        //  Update location based on heading
        x += MAG * PApplet.sin(heading);
        y += -MAG * PApplet.cos(heading);

        // Decide of color for Bullet, or if hollow
        if (FILL) {
            float hue = (PApplet.degrees(heading) + 360) % 360;
            parent.fill(parent.color(hue, SAT, BRIGHT));
        } else {
            parent.noFill();
        }
    }

    /**
     * Draws this Bullet object to the screen
     */
    void draw() {
        //  Set basic drawing propertie
        parent.strokeWeight(STROKE_WEIGHT);
        parent.stroke(parent.color(0));

        //  No need for rotation as we are simple drawing a circle
        parent.ellipse(x, y, RADIUS, RADIUS);

    }

}
