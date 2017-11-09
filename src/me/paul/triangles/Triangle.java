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
    private PVector pos;
    private PVector mouse;
    private PVector velocity;

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
     * @param pos_     coordinate location of this triangle
     */

    Triangle(TriangleManager manager_, PVector pos_) {

        manager = manager_;
        pos = pos_;
        mouse = new PVector(manager.mouseX, manager.mouseY);
        velocity = new PVector(0, -1).mult(MAG);

        //  Initialization
        bullets = new ArrayList<>();
        bulletsToRemove = new ArrayList<>();

        // Create framework for triangle geometry
        tri = manager.createShape(manager.TRIANGLE, 0, -45, -30f, 36f, 30f, 36f);
    }

    /**
     * Getter method for coordinate
     *
     * @return coordinate location of this triangle
     */
    PVector getPos() {
        return pos;
    }

    /**
     * Updates Triangle data, such as
     * location, heading, color, and bullet list
     */
    void update() {

        mouse.set(manager.mouseX, manager.mouseY);

        System.out.println("VELOCITY - 1: " + velocity.toString() + ", MAG: " + velocity.mag());
        System.out.println("POSITION - 1: " + pos.toString() + ", MAG: " + pos.mag());

        //  Perform trigonometric operation to get new location from heading
        //  Applicable if keys are pressed
        if (pos.dist(mouse) > velocity.mag()/2) {

            if (manager.getKeyCodes()[manager.LEFT]) {
                pos.add(velocity.copy().rotate(-PApplet.acos(velocity.mag() / (2 * PVector.dist(pos, mouse)))));
            }
            if (manager.getKeyCodes()[manager.RIGHT]) {
                pos.add(velocity.copy().rotate(PApplet.acos(velocity.mag() / (2 * PVector.dist(pos, mouse)))));
            }
            if (manager.getKeyCodes()[manager.UP]) {
                pos.add(velocity);
            }
            if (manager.getKeyCodes()[manager.DOWN]) {
                pos.sub(velocity);
            }

            if (pos.dist(mouse) > 0) {
                velocity.set(PVector.sub(mouse, pos).normalize().mult(MAG));
            } else {
                velocity.set(0, -1).mult(MAG);
            }
        } else {
            velocity.set(0, -1).mult(MAG);
        }

        System.out.println("VELOCITY - 2: " + velocity.toString() + ", MAG: " + velocity.mag());
        System.out.println("POSITION - 2: " + pos.toString() + ", MAG: " + pos.mag());

        //  Add off-screen bullets to the remove copy list
        for (Bullet b : bullets) {
            if (b.getDeleteStatus()) {
                bulletsToRemove.add(b);
            }
        }

        // Remove bullets from main list and clear copy list
        bullets.removeAll(bulletsToRemove);
        bulletsToRemove.clear();
    }

    /**
     * Draws a Triangle to the screen
     */
    void draw() {

        // Set color and drawing properties
        float hue = 180 + PApplet.degrees(velocity.copy().rotate(PApplet.radians(-90)).heading());
        tri.setFill(manager.color(hue, SAT, BRIGHT));
        manager.fill(manager.color(hue, SAT, BRIGHT));
        tri.setStroke(manager.color(0));
        manager.stroke(manager.color(0));
        tri.setStrokeWeight(STROKE_WEIGHT);

        // Move origin to our location and rotate so up is our heading
        manager.translate(pos.x, pos.y);
        manager.rotate(velocity.copy().rotate(PApplet.radians(90)).heading());

        // Draw our preset geometry to screen
        manager.shape(tri);

        // Un-rotate and move origin back to reset
        manager.rotate(-velocity.copy().rotate(PApplet.radians(90)).heading());
        manager.translate(-pos.x, -pos.y);
    }

    /**
     * Adds a bullet to this Triangle's bullet list
     */
    void addBullet() {
        //  Calculate starting locations
        PVector bulletPos = pos.copy().add(velocity.copy().mult(10));

        //  Add to list
        bullets.add(new Bullet(manager, bulletPos, velocity.copy().normalize(), manager.getBounceMode()));
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
}
