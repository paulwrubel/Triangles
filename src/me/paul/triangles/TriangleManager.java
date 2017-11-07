package me.paul.triangles;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;

/**
 *  @version 1.0
 *  @author Paul Wrubel - VoxaelFox
 *
 *  Creates Triangles on a PApplet window supported by processing libraries.
 *
 *  Features include:
 *      Triangles aim at mouse
 *      Triangles can move toward, away from, and orbit the mouse
 *      Trianges can shoot bullets.
 *      Color is based on mouse position (Background) and heading (Triangles/Bullets)
 *      Perspective Movement! (Alpha)
 *
 *  Controls:
 *      RIGHT CLICK:    Create Triangle at cursor
 *                          (can be held in dynamic mode)
 *      LEFT CLICK:     Shoot Bullet from all Triangles on screen at cursor
 *                          (can be held in dynamic mode)
 *      CENTRE CLICK:   Remove oldest Triangle
 *                          (can be held in dynamic mode)
 *
 *      UP ARROW:       Move all Triangles towards cursor (can be held)
 *      DOWN ARROW:     Move all Triangles away from cursor (can be held)
 *      LEFT ARROW:     All Triangles orbit cursor anti-clockwise (can be held)
 *      RIGHT ARROW:    All Triangles orbit cursor clockwise (can be held)
 *
 *      i:              Change perspective up
 *                          (can be held in dynamic mode / blocky in static mode)
 *      k:              Change perspective down
 *                          (can be held in dynamic mode / blocky in static mode)
 *      j:              Change perspective left
 *                          (can be held in dynamic mode / blocky in static mode)
 *      l:              Change perspective right
 *                          (can be held in dynamic mode / blocky in static mode)
 *      u:              Change perspective out
 *                          (can be held in dynamic mode / blocky in static mode)
 *      o:              Change perspective in
 *                          (can be held in dynamic mode / blocky in static mode)
 *
 *      SPACE:          Clears all triangles and bullets from screen
 *
 *      ENTER:          Toggles dynamic / static mode
 *
 */

public class TriangleManager extends PApplet {

    /**
     *  Controls default window size
     *  Ranges from 0 - screen width / height
     */
    private static final int WINDOW_WIDTH = 1600;
    private static final int WINDOW_HEIGHT = 800;

    /**
     *  Controls Saturation and Brightness values of screen background
     *  Ranges from 0 - 100
     */
    private static final float SAT = 20;
    private static final float BRIGHT = 100;

    /** Stroke weight for the window border and crosshairs
     *  Ranges from 0 - INF
     */
    private static final float BORDER_WEIGHT = 12;
    private static final float CROSSHAIRS_WEIGHT = 5;

    /** Limit of the amount of Triangles and Bullets in the window
     * Ranges from 0 - INF
     */
    private static final int TRIANGLE_LIMIT = 500;
    private static final int BULLET_LIMIT = 10000;

    /** Frequency of the creation (and removal) of Triangles and Bullets in dynamic mode
     *  1 writes every frame, 5 every 5 frames, and so on.
     *  Lower values are faster
     *  Ranges from 1 - INF
     */
    private static final int TRIANGLE_ADD_FREQ = 4;
    private static final int TRIANGLE_REMOVE_FREQ= 4;
    private static final int BULLET_FREQ = 4;

    /**  Controls mode of program
     *  Dynamic mode:
     *      Continuous creation and removal of Triangles and Bullets
     *      Smooth and constant movement of window when changing perspective
     *  Static mode:
     *      Every creation requires a mouse click or key press
     *      Perspective movement jumps to preset values and is block-like
     */
    private boolean dynamic;
    private boolean bounce;
    private int gravityMode;
    private float gravPointX;
    private float gravPointY;
    private float decay;

    private int prevWidth;
    private int prevHeight;

    /** Saves camera state for changing perspective */
    private float cameraX;
    private float cameraY;
    private float cameraZ;

    /** References to all on-screen Triangles */
    private ArrayList<Triangle> triangles;

    /** Arrays to save state of all key and mouse presses concurrently
     *  (currently not supported by processing, unfortunately)
     */
    private boolean[] mouseButtons;
    private boolean[] keys;
    private boolean[] keyCodes;

    /**
     *  Called once, only to set window size and render method
     *  P3D is used to utilize OpenGL's graphics card integration
     *  Otherwise there is big lag!
     */
    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, P3D);
    }

    /**
     *  Called once, for initialization purposes. Nothing is drawn here
     */
    public void setup() {

        //  Initial values
        dynamic = false;
        bounce = false;
        gravityMode = 0;
        decay = 0.99f;
        gravPointX = width / 2;
        gravPointY = height / 2;

        prevWidth = width;
        prevHeight = height;

        triangles = new ArrayList<>();
        mouseButtons = new boolean[40];
        keys = new boolean[128];
        keyCodes = new boolean[41];

        cameraX = width/2.0f;
        cameraY = height/2.0f;
        cameraZ = (height/2.0f) / tan(PI*30.0f / 180.0f);

        //  Can resize window (alpha)
        surface.setResizable(true);

        //  Hue, Saturation, Brightness, and ranges
        colorMode(HSB, 360, 100, 100);
        //  Ellipses are drawn from centre and distances are radii
        ellipseMode(RADIUS);
        //  Background begins black
        background(color(0));
        //  To avoid rendering artifacts from 3D mode
        //  Essentially tells renderer to just ignore z-dimension
        hint(DISABLE_DEPTH_TEST);
    }

    /**
     *  Called once every frame.
     *  All drawing to window buffer occurs here, either directly or indirectly (from supporting classes called)
     */

    public void draw() {

        if (prevWidth != width) {
            cameraX += (width - prevWidth)/2f;
        }
        if (prevHeight != height) {
            cameraY += (height - prevHeight)/2f;
            cameraZ += ((height - prevHeight)/2f) / tan(PI*30.0f / 180.0f);
        }


        //  Fix camera
        camera(cameraX, cameraY, cameraZ, width/2.0f, height/2.0f, 0f, 0f, 1.0f, 0f);



        //  Set window title based on current mode
        if (dynamic) {
            surface.setTitle("Triangles - Dynamic");
        } else {
            surface.setTitle("Triangles - Static");
        }

        //  Set hue based on horizontal mouse position
        float hue = map(mouseX, 0, width, 0, 360);
        background(hue, SAT, BRIGHT);

        //  Draw border around effective "window"
        //  To keep track of original perspective
        stroke(color(0));
        strokeWeight(BORDER_WEIGHT);
        line(0, 0 + BORDER_WEIGHT/2, width, 0 + BORDER_WEIGHT/2);
        line(0 + BORDER_WEIGHT/2, 0, 0 + BORDER_WEIGHT/2, height);
        line(0, height - BORDER_WEIGHT/2, width, height - BORDER_WEIGHT/2);
        line(width - BORDER_WEIGHT/2, 0, width - BORDER_WEIGHT/2, height);

        if (gravityMode == 2) {
            //  Draw Gravity Point
            stroke(color(0));
            strokeWeight(2);
            fill(color(0, 0, 100));
            ellipse(gravPointX, gravPointY, 4, 4);
        }

        //  Draw crosshairs
        stroke(color(0, 100, 100));
        strokeWeight(CROSSHAIRS_WEIGHT);
        line(mouseX, mouseY-10, mouseX, mouseY+10);
        line(mouseX-10, mouseY, mouseX+10, mouseY);

        //  Initial values for timing vars
        double triangleTime = -1;
        double bulletTime = -1;
        long start;
        long end;

        int bulletCount = 0;

        //  Iterate over Triangle List
        for (Triangle t : triangles) {

            //  Start Timer and operate on Bullets
            //  Iterates over all bullets created and managed by a Triangle
            start = System.nanoTime();
            for (Bullet b : t.bullets()) {

                b.update();
                b.draw();

            }
            end = System.nanoTime();
            bulletTime = (end - start)/1000000d;

            //  Start Timer and operate on Triangles
            //  Simple update and draw execution
            start = System.nanoTime();
            t.update();
            t.draw();
            end = System.nanoTime();
            triangleTime = (end - start)/1000000d;

            //  Add to total bullet count
            bulletCount += t.bullets().size();

        }

        //  Only if in dynamic mode
        //  Check for mouse buttons and key presses and perform actions accordingly
        if (dynamic) {
            //  Add Triangles
            if (mouseButtons[RIGHT] && (frameCount % TRIANGLE_ADD_FREQ) == 0) {
                handleAdd();
            }
            //  Remove Triangles
            if (keys[(int)BACKSPACE] && (frameCount % TRIANGLE_REMOVE_FREQ) == 0) {
                if (triangles.size() != 0) {
                    triangles.remove(0);
                }
            }
            //  Add Bullets
            if (mouseButtons[LEFT] && (frameCount % BULLET_FREQ) == 0) {
                for (Triangle t : triangles) {
                    if (bulletCount < BULLET_LIMIT) {
                        t.addBullet();
                    }
                }
            }
            //  Move perspective up
            if (keys[(int)('i')]) {
                cameraY -= 10f;
            }
            //  Move perspective left
            if (keys[(int)('j')]) {
                cameraX -= 10f;
            }
            //  Move perspective down
            if (keys[(int)('k')]) {
                cameraY += 10f;
            }
            //  Move perspective right
            if (keys[(int)('l')]) {
                // RIGHT
                cameraX += 10f;
            }
            //  Move perspective out
            if (keys[(int)('u')]) {
                cameraZ += 10f / tan(PI * 30.0f / 180.0f);
            }
            //  Move perspective in
            if (keys[(int)('o')]) {
                cameraZ -= 10f / tan(PI * 30.0f / 180.0f);
            }
        }

        //  Print basic debug text to screen
        //  Text is written to top left corner of window
        fill(0);
        text("X: " + mouseX, 50, 50);
        text("Y: " + mouseY, 50, 70);
        text("Triangle Count: " + triangles.size(), 50, 90);
        text("Bullet Count: " + bulletCount, 50, 110);
        String triangleText = String.format("Triangle Time: ~%.4fms", triangles.size()*triangleTime);
        String bulletText = String.format("Bullet Time: ~%.4fms", triangles.size()*bulletTime);
        String FPSText = String.format("FPS: %d", (int)frameRate);
        text(triangleText, 50, 130);
        text(bulletText, 50, 150);
        text(FPSText, 50, 170);
        text("Bounce: " + bounce, 50, 190);
        text("Decay: " + decay, 50, 210);
        String mode = gravityMode == 0 ? "OFF" : gravityMode == 1 ? "MOUSE" : "POINT";
        text("Gravity Mode: " + mode, 50, 230);

        prevWidth = width;
        prevHeight = height;
    }

    /**
     *  Handles adding of Triangles to screen
     *  Will erase oldest if over TRIANGLE_LIMIT already on screen
     */
    private void handleAdd() {
        if (triangles.size() != 0) {
            //  Make sure the mouse is in a different position
            if (mouseX != triangles.get(triangles.size() - 1).getX() || mouseY != triangles.get(triangles.size() - 1).getY()) {
                triangles.add(new Triangle(this, mouseX, mouseY));
                //  Remove oldest
                if (triangles.size() > TRIANGLE_LIMIT) {
                    triangles.remove(0);
                }
            }
        } else {
            //  If brand-new, just add one!
            triangles.add(new Triangle(this, mouseX, mouseY));
        }
    }

    float getGravityX() {
        if (gravityMode == 1) {
            return mouseX;
        } else {
            return gravPointX;
        }
    }

    float getGravityY() {
        if (gravityMode == 1) {
            return mouseY;
        } else {
            return gravPointY;
        }
    }

    int getGravityMode() {
        return gravityMode;
    }

    boolean getBounceMode() {
        return bounce;
    }

    float getBorderWeight() {
        return BORDER_WEIGHT;
    }

    float getDecay() {
        return decay;
    }

    /**
     * Getter method for keyCodes
     * @return the keyCodes array
     */

    boolean[] getKeyCodes() {
        return keyCodes;
    }

    /**
     * Getter method for keys
     * @return the keys array
     */

    boolean[] getKeys() {
        return keys;
    }

    /**
     * Getter method for mouseButtons
     * @return the mouseButtons array
     */

    boolean[] getMouseButtons() {
        return mouseButtons;
    }

    /**
     * Is called when a key is pressed down.
     * @param event event linked to which key was pressed
     */

    public void keyPressed(KeyEvent event) {
        //  Get key data
        char k = event.getKey();
        int kc = event.getKeyCode();

        if (k < keys.length) {
            keys[k] = true;
        } else {
            keyCodes[kc] = true;
        }


        // Handle key data
        if (kc == ENTER) {
            dynamic = !dynamic;
        }
        if (k == 'r') {
            cameraX = width / 2.0f;
            cameraY = height / 2.0f;
            cameraZ = (height / 2.0f) / tan(PI * 30.0f / 180.0f);
            //  Defaults:
            //
            //  camera(
            //      width/2.0,
            //      height/2.0,
            //      (height/2.0) / tan(PI*30.0 / 180.0),
            //      width/2.0, height/2.0,
            //      0,
            //      0,
            //      1,
            //      0)
        }
        if (k == ' ') {
            triangles.clear();
        }
        if (k == 'b') {
            bounce = !bounce;
        }
        if (k == 'g') {
            gravityMode = (gravityMode + 1) % 3;
        }
        if (!dynamic) {
            if (k == 'i') {
                // UP
                cameraY -= height / 2.0f;
            }
            if (k == 'j') {
                // LEFT
                cameraX -= width / 2.0f;
            }
            if (k == 'k') {
                // DOWN
                cameraY += height / 2.0f;
            }
            if (k == 'l') {
                // RIGHT
                cameraX += width / 2.0f;
            }
            if (k == 'u') {
                // OUT
                cameraZ += (height / 2.0) / tan(PI * 30.0f / 180.0f);
            }
            if (k == 'o') {
                // IN
                cameraZ -= (height / 2.0) / tan(PI * 30.0f / 180.0f);
            }
            if (k == BACKSPACE) {
                if (triangles.size() != 0) {
                    triangles.remove(0);
                }
            }
        }
    }

    /**
     * Is called once when a key is released
     * @param event event holding data about the released key
     */
    public void keyReleased(KeyEvent event) {
        char k = event.getKey();
        int kc = event.getKeyCode();

        //  Simply set corresponding array pos to false;
        if (k < keys.length) {
            keys[k] = false;
        } else {
            keyCodes[kc] = false;
        }
    }

    /**
     * Is called once when a mouse button is pressed
     * @param event event containing mouse button info
     */

    public void mousePressed(MouseEvent event) {
        int mb = event.getButton();

        //  Set array position to true
        mouseButtons[mb] = true;

        if (mb == CENTER) {
            gravPointX = mouseX;
            gravPointY = mouseY;
        }

        //  Handle mouse button actions
        if (!dynamic) {
            if (mb == LEFT) {
                for (Triangle t : triangles) {
                    t.addBullet();
                }
            }
            if (mb == RIGHT) {
                handleAdd();
            }
        }
    }

    /**
     * Is called once when a mouse button is released
     * @param event event containing data about the mouse button released
     */

    public void mouseReleased(MouseEvent event) {
        int mb = event.getButton();

        //  Simply set position to false
        mouseButtons[mb] = false;
    }

    /**
     * backup method for PApplet to gain execution
     * @param _args arguments to main
     */

    public static void main(String _args[]) {
        PApplet.main(new String[]{me.paul.triangles.TriangleManager.class.getName()});
    }
}
