package me.paul.triangles;

import processing.core.*;

class Bullet {

    private static final boolean FILL = true;
    private static final float STROKE_WEIGHT = 2;
    private static final float RADIUS = 8;
    private static final float MAG = 10;

    private static final float SAT = 60;
    private static final float BRIGHT = 85;

    private Triangles parent;

    private float x;
    private float y;
    private float heading;

    Bullet(Triangles parent_, float x_, float y_, float heading_) {
        parent = parent_;
        x= x_;
        y = y_;
        heading = heading_;
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    void update() {
        x += MAG * PApplet.sin(heading);
        y += -MAG * PApplet.cos(heading);

        if (FILL) {
            float hue = (PApplet.degrees(heading) + 360) % 360;
            parent.fill(parent.color(hue, SAT, BRIGHT));
        } else {
            parent.noFill();
        }
    }

    void draw() {
        parent.strokeWeight(STROKE_WEIGHT);
        parent.stroke(parent.color(0));
        parent.ellipse(x, y, RADIUS, RADIUS);
    }

}
