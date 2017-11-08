package me.paul.triangles;

import processing.core.*;

import java.util.ArrayList;

/**
 * @author Paul Wrubel - VoxaelFox
 * <p>
 * Holds a reference to a Triangle Object.
 * This is what is drawn on the window.
 * <p>
 * Also holds the list of bullets that belongs to this Triangle
 * @version 1.0
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
    private TriangleManager manager;

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
     *
     * @param manager_ Triangles reference needed to draw to the screen
     * @param x_       x coordinate location of this triangle
     * @param y_       y coordinate location of this triangle
     */

    Triangle(TriangleManager manager_, float x_, float y_) {

        manager = manager_;

        x = x_;
        y = y_;

        //  Initialization
        bullets = new ArrayList<>();
        bulletsToRemove = new ArrayList<>();

        // Create framework for triangle geometry
        tri = manager.createShape(PConstants.TRIANGLE, 0, -45, -30f, 36f, 30f, 36f);
    }

    /**
     * Getter method for x coordinate
     *
     * @return x coordinate location of this triangle
     */
    // TODO: Redo with PVector class
    float getX() {
        return x;
    }

    /**
     * Getter method for y coordinate
     *
     * @return y coordinate location of this triangle
     */
    // TODO: Redo with PVector class
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
        if (manager.getKeyCodes()[manager.UP]) {
            x += MAG * PApplet.sin(heading);
            y += -MAG * PApplet.cos(heading);
        }
        if (manager.getKeyCodes()[manager.DOWN]) {
            x += -MAG * PApplet.sin(heading);
            y += MAG * PApplet.cos(heading);
        }
        if (manager.getKeyCodes()[manager.LEFT]) {
            x += MAG * PApplet.cos(heading);
            y += MAG * PApplet.sin(heading);
        }
        if (manager.getKeyCodes()[manager.RIGHT]) {
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
        tri.setFill(manager.color(hue, SAT, BRIGHT));
        manager.fill(manager.color(hue, SAT, BRIGHT));
        tri.setStroke(manager.color(0));
        manager.stroke(manager.color(0));
        tri.setStrokeWeight(STROKE_WEIGHT);

        // Move origin to our location and rotate so up is our heading
        manager.translate(x, y);
        manager.rotate(heading);

        // Draw our preset geometry to screen
        manager.shape(tri);

        // Un-rotate and move origin back to reset
        manager.rotate(-heading);
        manager.translate(-x, -y);
    }

    /**
     * Adds a bullet to this Triangle's bullet list
     */
    void addBullet() {
        //  Calculate starting locations
        PVector pos = new PVector(x + (40 * PApplet.sin(heading)), y - (40 * PApplet.cos(heading)));

        //  Add to list
        bullets.add(new Bullet(manager, pos, heading, manager.getBounceMode()));
    }

    void clearBullets() {
        bullets.clear();
    }

    /**
     * Getter method for the bullet list of this Triangle
     *
     * @return This Triangles bullet list
     */
    ArrayList<Bullet> bullets() {
        return bullets;
    }

    /**
     * Calculates this Triangles heading based on its location
     * relative to the cursor position
     *
     * @return This Triangle's most recent heading
     */
    // TODO: Redo with PVector class
    private float getAngle() {
        // get distances from location to cursor
        double dx = PApplet.abs(manager.mouseX - x);
        double dy = PApplet.abs(manager.mouseY - y);

        // Which quadrant is the cursor in relative to us?
        boolean left = manager.mouseX < x;
        boolean top = manager.mouseY < y;

        // Choose which atan formula to use based on quadrant
        float rot;
        if (manager.mouseX == x && manager.mouseY == y) {
            rot = 0;
        } else if (top && !left) {
            rot = PApplet.atan((float) (dx / dy));
        } else if (!top && left) {
            rot = PApplet.atan((float) (dx / dy)) + manager.PI;
        } else if (top) {
            rot = PApplet.atan((float) (dy / dx)) - manager.PI / 2;
        } else {
            rot = PApplet.atan((float) (dy / dx)) + manager.PI / 2;
        }

        return rot;
    }

}
