package me.paul.triangles;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * @author Paul Wrubel - VoxaelFox
 * <p>
 * Holds a reference to a Bullet Object.
 * This is what is drawn on the window.
 * @version 1.0
 */

class Bullet {

    /**
     * Defaults for drawing properties
     * such as if the bullet is filled with color or hollow, and
     * stroke weight, radius, and magnitude of movement
     * <p>
     * Also defaults for Saturation and Brightness values
     */
    private static final boolean FILL = true;
    private static final boolean STROKE = true;
    private static final float STROKE_WEIGHT = 2;
    private static final float RADIUS = 10;

    private static final float MAG = 8;
    private static final float GRAVITY_CONST = 10000;

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
    private PVector velocity;
    private PVector acceleration;

    private PShape circ;

    /**
     * Constructor for a Bullet object
     *
     * @param manager_  Reference to a PApplet class to draw to
     * @param pos_      PVector describing position of this Bullet
     * @param velocity_ heading (in radians) of this Bullet
     */

    Bullet(TriangleManager manager_, PVector pos_, PVector velocity_, boolean bounce_) {
        manager = manager_;
        pos = pos_;
        velocity = velocity_.mult(MAG);
        acceleration = new PVector(0, 0);

        markedForDelete = false;
        bounce = bounce_;

        gravity = 1;

        circ = manager.createShape(manager.ELLIPSE, 0, 0, RADIUS, RADIUS);
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

            acceleration.set(0, 0);

        } else if (gm == Gravity.SIMPLE) {
            PVector gravityPoint = manager.getGravityPoint();
            gravity = 1;

            acceleration.set(PVector.sub(gravityPoint, pos).setMag(gravity));

        } else if (gm == Gravity.MULTI_POINT) {
            ArrayList<PVector> gravForces = new ArrayList<>();

            for (PVector v : manager.getGravityList()) {
                float gravDist = pos.dist(v);

                if (gravDist > PApplet.sqrt(GRAVITY_CONST)) {
                    gravity = GRAVITY_CONST / PApplet.sq(gravDist);
                } else {
                    gravity = 1;
                }

                gravForces.add(PVector.sub(v, pos).setMag(gravity));
            }

            acceleration.set(0, 0);
            for (PVector v : gravForces) {
                acceleration.add(v);
            }
        } else {
            PVector gravityPoint = manager.getGravityPoint();
            float gravDist = pos.dist(gravityPoint);

            if (gravDist > PApplet.sqrt(GRAVITY_CONST)) {
                gravity = GRAVITY_CONST / PApplet.sq(gravDist);
            } else {
                gravity = 1;
            }

            acceleration.set(PVector.sub(gravityPoint, pos).setMag(gravity));
        }

        velocity.add(acceleration);

        if (manager.getGravityMode() != Gravity.OFF) {
            velocity.mult(manager.getDecay());
        }

        //  Update location based on heading

        pos.add(velocity);

        if (bounce) {
            if (pos.x < 0 + RADIUS + manager.getBorderWeight()) {
                velocity.x *= -1;
                pos.x = 0 + RADIUS + manager.getBorderWeight();
            } else if (pos.x > manager.width - RADIUS - manager.getBorderWeight()) {
                velocity.x *= -1;
                pos.x = manager.width - RADIUS - manager.getBorderWeight();
            }
            if (pos.y < 0 + RADIUS + manager.getBorderWeight()) {
                velocity.y *= -1;
                pos.y = 0 + RADIUS + manager.getBorderWeight();
            } else if (pos.y > manager.height - RADIUS - manager.getBorderWeight()) {
                velocity.y *= -1;
                pos.y = manager.height - RADIUS - manager.getBorderWeight();
            }
        } else {
            if (pos.x < 0 - RADIUS) {
                markedForDelete = true;
            } else if (pos.x > manager.width + RADIUS) {
                markedForDelete = true;
            }
            if (pos.y < 0 - RADIUS) {
                markedForDelete = true;
            } else if (pos.y > manager.height + RADIUS) {
                markedForDelete = true;
            }
        }
    }

    /**
     * Draws this Bullet object to the screen
     */
    void draw() {
        // Decide of color for Bullet, or if hollow
        if (STROKE) {
            circ.setStrokeWeight(STROKE_WEIGHT);
            manager.strokeWeight(STROKE_WEIGHT);
            circ.setStroke(manager.color(0, 0, 0));
            manager.stroke(manager.color(0, 0, 0));
        } else {
            circ.noStroke();
            manager.noStroke();
        }

        if (FILL) {
            float hue = 180 + PApplet.degrees(velocity.copy().rotate(PApplet.radians(-90)).heading());
            float sat = PApplet.sqrt(PApplet.map(velocity.mag(), 0, 50, 10, 10000));
            circ.setFill(manager.color(hue, sat, BRIGHT));
            manager.fill(manager.color(hue, sat, BRIGHT));
        } else {
            circ.noFill();
            manager.noFill();
        }
        //  No need for rotation as we are simple drawing a circle
        //circ.disableStyle();
        //circ.enableStyle();
        //manager.shape(circ, pos.x, pos.y);
        manager.ellipse(pos.x, pos.y, RADIUS, RADIUS);

    }

}
