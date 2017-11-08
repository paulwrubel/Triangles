package me.paul.triangles;

import processing.core.*;

import java.util.ArrayList;

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

    private static final float BRIGHT = 85;

    /**
     * Reference to a Triangles object to draw to
     */
    private TriangleManager manager;
    private boolean markedForDelete;
    private boolean bounce;
    private float gravity;

    /**
     * Location and heading of this Bullet
     */
    private PVector pos;
    private float dx;
    private float dy;
    private float ddx;
    private float ddy;
    private float velocity;
    private float heading;

    /**
     * Constructor for a Bullet object
     * @param manager_ Reference to a PApplet class to draw to
     * @param pos_ PVector describing position of this Bullet
     * @param heading_ heading (in radians) of this Bullet
     */

    Bullet(TriangleManager manager_, PVector pos_, float heading_, boolean bounce_) {
        manager = manager_;
        pos = pos_;
        heading = heading_;

        dx = MAG * (PApplet.sin(heading));
        dy = -MAG * (PApplet.cos(heading));

        ddx = 0;
        ddy = 0;

        markedForDelete = false;
        bounce = bounce_;

        gravity = getDistFromPoint(pos);
    }

    boolean getDeleteStatus() {
        return markedForDelete;
    }

    /**
     * Updates information and properties about this Bullet object
     * like it's location and color
     */
    void update() {

        bounce = manager.getBounceMode();
        Gravity gm = manager.getGravityMode();

        if (gm == Gravity.OFF) {

            ddx = 0;
            ddy = 0;

        } else if (gm == Gravity.SIMPLE) {
            PVector gravityPoint = manager.getGravityPoint();

            gravity = 1;
            float gravAngle = getAngleFromPoint(gravityPoint);

            ddx = gravity * (PApplet.sin(gravAngle));
            ddy = -gravity * (PApplet.cos(gravAngle));
        } else if (gm == Gravity.MULTI_POINT){
            ArrayList<PVector> gravForces = new ArrayList<>();

            for (PVector v : manager.getGravityList()) {
                float gravAngle = getAngleFromPoint(v);
                float gravDist = getDistFromPoint(v);
                if (getDistFromPoint(v) > 100) {
                    gravity = 10000 / PApplet.sq(gravDist);
                } else {
                    gravity = 1;
                }
                gravForces.add(new PVector(gravity * (PApplet.sin(gravAngle)), -gravity * (PApplet.cos(gravAngle))));
            }
            PVector finalForce = new PVector(0, 0);
            for (PVector v : gravForces) {
                finalForce = finalForce.add(v);
            }

            ddx = finalForce.x;
            ddy = finalForce.y;
        } else {
            PVector gravityPoint = manager.getGravityPoint();

            if (getDistFromPoint(gravityPoint) > 100) {
                gravity = 10000 / PApplet.sq(getDistFromPoint(gravityPoint));
            } else {
                gravity = 1;
            }
            float gravAngle = getAngleFromPoint(gravityPoint);

            ddx = gravity * (PApplet.sin(gravAngle));
            ddy = -gravity * (PApplet.cos(gravAngle));
        }

        dx += ddx;
        dy += ddy;

        if (manager.getGravityMode() != Gravity.OFF) {
            dx *= manager.getDecay();
            dy *= manager.getDecay();
        }

        heading = getAngleFromVelocity(dx, dy);

        velocity = PApplet.sqrt(((dx*dx) + (dy*dy))*((dx*dx) + (dy*dy)));

        //  Update location based on heading

        pos.x += dx;
        pos.y += dy;

        if (bounce) {
            if (pos.x < 0 + RADIUS + manager.getBorderWeight()) {
                dx *= -1;
                pos.x = 0 + RADIUS + manager.getBorderWeight();
            } else if (pos.x > manager.width - RADIUS - manager.getBorderWeight()) {
                dx *= -1;
                pos.x = manager.width - RADIUS - manager.getBorderWeight();
            }
            if (pos.y < 0 + RADIUS + manager.getBorderWeight()) {
                dy *= -1;
                pos.y = 0 + RADIUS + manager.getBorderWeight();
            } else if(pos.y > manager.height - RADIUS - manager.getBorderWeight()) {
                dy *= -1;
                pos.y = manager.height - RADIUS - manager.getBorderWeight();
            }
        } else {
            if (pos.x < 0 + RADIUS + manager.getBorderWeight()) {
                dx *= -1;
                pos.x = 0 + RADIUS + manager.getBorderWeight();
                markedForDelete = true;
            } else if (pos.x > manager.width - RADIUS - manager.getBorderWeight()) {
                dx *= -1;
                pos.x = manager.width - RADIUS - manager.getBorderWeight();
                markedForDelete = true;
            }
            if (pos.y < 0 + RADIUS + manager.getBorderWeight()) {
                dy *= -1;
                pos.y = 0 + RADIUS + manager.getBorderWeight();
                markedForDelete = true;
            } else if(pos.y > manager.height - RADIUS - manager.getBorderWeight()) {
                dy *= -1;
                pos.y = manager.height - RADIUS - manager.getBorderWeight();
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
            float sat = PApplet.map(velocity, 0, 100, 10, 10000);
            sat = PApplet.sqrt(sat);
            manager.fill(manager.color(hue, sat, BRIGHT));
        } else {
            manager.noFill();
        }

        if (STROKE) {
            manager.strokeWeight(STROKE_WEIGHT);
            manager.stroke(manager.color(0));
        } else {
            manager.noStroke();
        }

        //  Set basic drawing properties


        //  No need for rotation as we are simple drawing a circle
        manager.ellipse(pos.x, pos.y, RADIUS, RADIUS);

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

    private float getAngleFromPoint(PVector v) {
        // get distances from location to cursor
        float dx = PApplet.abs(v.x - pos.x);
        float dy = PApplet.abs(v.y - pos.y);

        // Which quadrant is the cursor in relative to us?
        boolean left = v.x < pos.x;
        boolean top = v.y < pos.y;

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

    private float getDistFromPoint(PVector v) {
        float dx = PApplet.abs(v.x - pos.x);
        float dy = PApplet.abs(v.y - pos.y);

        float result = dx*dx + dy*dy;

        return PApplet.sqrt(result);
    }

}
