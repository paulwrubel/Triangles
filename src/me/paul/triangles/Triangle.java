package me.paul.triangles;

import processing.core.*;
import java.util.ArrayList;

/**
 *  @version 1.0
 *  @author Paul Wrubel - VoxaelFox
 *
 *  Holds a reference to a Triangle Object.
 *  This is what is drawn on the window.
 *
 *  Also holds the list of bullets that belongs to this Triangle
 */

class Triangle {

    /**
     * Default values for Saturation, Brightness
     * Of triangles
     */
    private static final float SAT = 85;
    private static final float BRIGHT = 100;

    /**
     * Stroke weight default
     * And Magnitude of movement with arrow keys
     */
    private static final float STROKE_WEIGHT = 5;
    private static final float MAG = 4;

    /**
     * A reference to a Triangles Object
     * Needed to draw to the buffer from PApplet
     */
    private Triangles parent;

    /**
     * Location and heading of triangles on window
     */
    private float x;
    private float y;
    private float heading;

    /**
     * bullet list, along with a copy list to remove off-screen bullets
     */
    private ArrayList<Bullet> bullets;
    private ArrayList<Bullet> bulletsToRemove;

    /**
     * Shape for triangle geometry
     */
    private PShape tri;

    /**
     * Constructor for a Triangle object
     * @param parent_ Triangles reference needed to draw to the screen
     * @param x_ x coordinate location of this triangle
     * @param y_ y coordinate location of this triangle
     */

    Triangle(Triangles parent_, float x_, float y_) {

        parent = parent_;

        x = x_;
        y = y_;

        //  Initialization
        bullets = new ArrayList<>();
        bulletsToRemove = new ArrayList<>();

        // Create framework for triangle geometry
        tri = parent.createShape(PConstants.TRIANGLE, 0, -45, -30f, 36f, 30f, 36f);
    }

    /**
     * Getter method for x coordinate
     * @return x coordinate location of this triangle
     */
    float getX() {
        return x;
    }

    /**
     * Getter method for y coordinate
     * @return y coordinate location of this triangle
     */
    float getY() {
        return y;
    }

    /**
     * Updates Triangle data, such as
     * location, heading, color, and bullet list
     */
    void update() {

        //  Perform trigonometric operation to get new location from heading
        //  Applicable if keys are pressed
        if (parent.getKeyCodes()[parent.UP]) {
            x += MAG * PApplet.sin(heading);
            y += -MAG * PApplet.cos(heading);
        }
        if (parent.getKeyCodes()[parent.DOWN]) {
            x += -MAG * PApplet.sin(heading);
            y += MAG * PApplet.cos(heading);
        }
        if (parent.getKeyCodes()[parent.LEFT]) {
            x += MAG * PApplet.cos(heading);
            y += MAG * PApplet.sin(heading);
        }
        if (parent.getKeyCodes()[parent.RIGHT]) {
            x += -MAG * PApplet.cos(heading);
            y += -MAG * PApplet.sin(heading);
        }

        //  Add off-screen bullets to the remove copy list
        for (Bullet b : bullets) {
            if (b.getDeleteStatus()) {
                bulletsToRemove.add(b);
            }
        }

        // Remove bullets from main list and clear copy list
        bullets.removeAll(bulletsToRemove);
        bulletsToRemove.clear();

        // Update heading from private method
        heading = getAngle();
    }

    /**
     * Draws a Triangle to the screen
     */
    void draw() {

        // Set color and drawing properties
        float hue = (PApplet.degrees(heading) + 360) % 360;
        tri.setFill(parent.color(hue, SAT, BRIGHT));
        parent.fill(parent.color(hue, SAT, BRIGHT));
        tri.setStroke(parent.color(0));
        parent.stroke(parent.color(0));
        tri.setStrokeWeight(STROKE_WEIGHT);

        // Move origin to our location and rotate so up is our heading
        parent.translate(x, y);
        parent.rotate(heading);

        // Draw our preset geometry to screen
        parent.shape(tri);

        // Un-rotate and move origin back to reset
        parent.rotate(-heading);
        parent.translate(-x, -y);
    }

    /**
     * Adds a bullet to this Triangle's bullet list
     */
    void addBullet() {
        //  Calculate starting locations
        float xPos = x + (40 * PApplet.sin(heading));
        float yPos = y - (40 * PApplet.cos(heading));

        //  Add to list
        bullets.add(new Bullet(parent, xPos, yPos, heading, parent.getBounceMode()));
    }

    /**
     * Getter method for the bullet list of this Triangle
     * @return This Triangles bullet list
     */
    ArrayList<Bullet> bullets() {
        return bullets;
    }

    /**
     * Calculates this Triangles heading based on its location
     * relative to the cursor position
     * @return This Triangle's most recent heading
     */
    private float getAngle() {
        // get distances from location to cursor
        double dx = PApplet.abs(parent.mouseX - x);
        double dy = PApplet.abs(parent.mouseY - y);

        // Which quadrant is the cursor in relative to us?
        boolean left = parent.mouseX < x;
        boolean top = parent.mouseY < y;

        // Choose which atan formula to use based on quadrant
        float rot;
        if (parent.mouseX == x && parent.mouseY == y) {
            rot = 0;
        } else if (top && !left) {
            rot = PApplet.atan((float)(dx / dy));
        } else if (!top && left) {
            rot = PApplet.atan((float)(dx / dy)) + parent.PI;
        } else if (top) {
            rot = PApplet.atan((float)(dy / dx)) - parent.PI / 2;
        } else {
            rot = PApplet.atan((float)(dy / dx)) + parent.PI / 2;
        }

        return rot;
    }

}
