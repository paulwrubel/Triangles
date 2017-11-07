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
    private static final boolean STROKE = true;
    private static final float STROKE_WEIGHT = 2;
    private static final float RADIUS = 8;

    private static final float MAG = 10;
    private static final float GRAVITY = 1;

    private static final float BRIGHT = 85;

    /**
     * Reference to a Triangles object to draw to
     */
    private TriangleManager parent;
    private boolean markedForDelete;
    private boolean bounce = true;

    /**
     * Location and heading of this Bullet
     */
    private float x;
    private float y;
    private float dx;
    private float dy;
    private float ddx;
    private float ddy;
    private float velocity;
    private float heading;

    /**
     * Constructor for a Bullet object
     * @param parent_ Reference to a PApplet class to draw to
     * @param x_ x coordinate location of this Bullet
     * @param y_ y coordinate location of this Bullet
     * @param heading_ heading (in radians) of this Bullet
     */

    Bullet(TriangleManager parent_, float x_, float y_, float heading_, boolean bounce_) {
        parent = parent_;
        x = x_;
        y = y_;
        heading = heading_;

        dx = MAG * (PApplet.sin(heading));
        dy = -MAG * (PApplet.cos(heading));

        ddx = 0;
        ddy = 0;

        markedForDelete = false;
        bounce = bounce_;
    }

    boolean getDeleteStatus() {
        return markedForDelete;
    }

    /**
     * Updates information and properties about this Bullet object
     * like it's location and color
     */
    void update() {

        bounce = parent.getBounceMode();

        if (parent.getGravityMode() != 0) {
            float gravX = parent.getGravityX();
            float gravY = parent.getGravityY();

            float gravAngle = getAngleFromPoint(gravX, gravY);

            ddx = GRAVITY * (PApplet.sin(gravAngle));
            ddy = -GRAVITY * (PApplet.cos(gravAngle));
        } else {

            ddx = 0;
            ddy = 0;
        }

        dx += ddx;
        dy += ddy;

        if (parent.getGravityMode() != 0) {
            dx *= parent.getDecay();
            dy *= parent.getDecay();
        }

        heading = getAngleFromVelocity(dx, dy);

        velocity = PApplet.sqrt(((dx*dx) + (dy*dy))*((dx*dx) + (dy*dy)));

        //  Update location based on heading

        x += dx;
        y += dy;

        if (bounce) {
            if (x < 0 + RADIUS + parent.getBorderWeight()) {
                dx *= -1;
                x = 0 + RADIUS + parent.getBorderWeight();
            } else if (x > parent.width - RADIUS - parent.getBorderWeight()) {
                dx *= -1;
                x = parent.width - RADIUS - parent.getBorderWeight();
            }
            if (y < 0 + RADIUS + parent.getBorderWeight()) {
                dy *= -1;
                y = 0 + RADIUS + parent.getBorderWeight();
            } else if(y > parent.height - RADIUS - parent.getBorderWeight()) {
                dy *= -1;
                y = parent.height - RADIUS - parent.getBorderWeight();
            }
        } else {
            if (x < 0 + RADIUS + parent.getBorderWeight()) {
                dx *= -1;
                x = 0 + RADIUS + parent.getBorderWeight();
                markedForDelete = true;
            } else if (x > parent.width - RADIUS - parent.getBorderWeight()) {
                dx *= -1;
                x = parent.width - RADIUS - parent.getBorderWeight();
                markedForDelete = true;
            }
            if (y < 0 + RADIUS + parent.getBorderWeight()) {
                dy *= -1;
                y = 0 + RADIUS + parent.getBorderWeight();
                markedForDelete = true;
            } else if(y > parent.height - RADIUS - parent.getBorderWeight()) {
                dy *= -1;
                y = parent.height - RADIUS - parent.getBorderWeight();
                markedForDelete = true;
            }
        }


    }

    /**
     * Draws this Bullet object to the screen
     */
    void draw() {
        // Decide of color for Bullet, or if hollow
        if (FILL) {
            float hue = (PApplet.degrees(heading) + 360) % 360;
            float sat = PApplet.map(velocity, 0, 1000, 50, 100);
            parent.fill(parent.color(hue, sat, BRIGHT));
        } else {
            parent.noFill();
        }

        if (STROKE) {
            parent.strokeWeight(STROKE_WEIGHT);
            parent.stroke(parent.color(0));
        } else {
            parent.noStroke();
        }

        //  Set basic drawing properties


        //  No need for rotation as we are simple drawing a circle
        parent.ellipse(x, y, RADIUS, RADIUS);

    }

    private float getAngleFromVelocity(float dx, float dy) {

        // Which quadrant is the cursor in relative to us?
        boolean left = dx < 0;
        boolean top = dy < 0;

        // Choose which atan formula to use based on quadrant
        float rot = PApplet.degrees(PApplet.atan((dx / dy)));
        float trueRotation;

        if (dx == 0 && dy == 0) {
            trueRotation = 0;
        } else if (top && !left) {
            //  Top Right
            trueRotation = PApplet.map(rot, 0, -90, 0, 90);
        } else if (!top && !left) {
            //  Bottom Right
            trueRotation = PApplet.map(rot, 90, 0, 90, 180);
        } else if (!top) {
            //  Bottom Left
            trueRotation = PApplet.map(rot, 0, -90, 180, 270);
        } else {
            //  Top Left
            trueRotation = PApplet.map(rot, 90, 0, 270, 360);
        }

        return PApplet.radians(trueRotation);
    }

    private float getAngleFromPoint(float xLoc, float yLoc) {
        // get distances from location to cursor
        float dx = PApplet.abs(xLoc - x);
        float dy = PApplet.abs(yLoc - y);

        // Which quadrant is the cursor in relative to us?
        boolean left = xLoc < x;
        boolean top = yLoc < y;

        // Choose which atan formula to use based on quadrant
        float rot = PApplet.degrees(PApplet.atan((dx / dy)));
        float trueRotation;

        if (dx == 0 && dy == 0) {
            trueRotation = 0;
        } else if (top && !left) {
            //  Top Right
            trueRotation = PApplet.map(rot, 0, 90, 0, 90);
        } else if (!top && !left) {
            //  Bottom Right
            trueRotation = PApplet.map(rot, 90, 0, 90, 180);
        } else if (!top) {
            //  Bottom Left
            trueRotation = PApplet.map(rot, 0, 90, 180, 270);
        } else {
            //  Top Left
            trueRotation = PApplet.map(rot, 90, 0, 270, 360);
        }

        return PApplet.radians(trueRotation);
    }

}
