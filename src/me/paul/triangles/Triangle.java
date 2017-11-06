package me.paul.triangles;

import processing.core.*;

import java.util.ArrayList;

class Triangle {

    private static final float SAT = 85;
    private static final float BRIGHT = 100;
    private static final float STROKE_WEIGHT = 5;
    private static final float MAG = 5;

    private Triangles parent;

    private float x;
    private float y;
    private float heading;

    private ArrayList<Bullet> bullets;
    private ArrayList<Bullet> bulletsToRemove;

    private PShape tri;

    Triangle(Triangles parent_, float x_, float y_) {

        parent = parent_;

        x = x_;
        y = y_;

        bullets = new ArrayList<>();
        bulletsToRemove = new ArrayList<>();

        tri = parent.createShape(PConstants.TRIANGLE, 0, -45, -30f, 36f, 30f, 36f);
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    void update() {

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

        for (Bullet b : bullets) {
            if (b.getX() < 0 || b.getX() > parent.width || b.getY() < 0 || b.getY() > parent.height) {
                bulletsToRemove.add(b);
            }
        }

        bullets.removeAll(bulletsToRemove);
        bulletsToRemove.clear();

        heading = getAngle(x, y);
        float hue = (PApplet.degrees(heading) + 360) % 360;
        tri.setFill(parent.color(hue, SAT, BRIGHT));
        parent.fill(parent.color(hue, SAT, BRIGHT));
        tri.setStroke(parent.color(0));
        parent.stroke(parent.color(0));
        tri.setStrokeWeight(STROKE_WEIGHT);
    }

    void draw() {

        parent.translate(x, y);
        parent.rotate(heading);

        parent.shape(tri);

        parent.rotate(-heading);
        parent.translate(-x, -y);
    }

    void addBullet() {
        float xPos = x + (40 * PApplet.sin(heading));
        float yPos = y - (40 * PApplet.cos(heading));
        bullets.add(new Bullet(parent, xPos, yPos, heading));
    }

    ArrayList<Bullet> bullets() {
        return bullets;
    }

    private float getAngle(float x, float y) {
        double dx = PApplet.abs(parent.mouseX - x);
        double dy = PApplet.abs(parent.mouseY - y);

        boolean left = parent.mouseX < x;
        boolean top = parent.mouseY < y;

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
