package me.paul.triangles;

import processing.core.PVector;

class ControlsBox {

    private static final float BORDER_WEIGHT = 20;
    private static final float PADDING = 100;

    private TriangleManager manager;

    private PVector topLeft;
    private PVector bottomRight;
    private PVector center;

    ControlsBox(TriangleManager manager_) {
        manager = manager_;

        topLeft = new PVector(PADDING, PADDING);
        bottomRight = new PVector(manager.width - PADDING, manager.height - PADDING);
        center = topLeft.copy().add(bottomRight);
        center.div(2);

    }

    void update() {
        bottomRight.set(manager.width - PADDING, manager.height - PADDING);
        center = topLeft.copy().add(bottomRight);
        center.div(2);
    }

    // TODO: Finish drawing
    void draw() {
        manager.rectMode(manager.CORNERS);
        manager.strokeWeight(BORDER_WEIGHT);
        manager.fill(manager.color(0, 0, 100));
        manager.rect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y, 1f);

        manager.textSize(100);
        manager.fill(0);
        manager.textMode(manager.SHAPE);
        manager.textAlign(manager.CENTER, manager.TOP);
        manager.text("CONTROLS \nHERE", manager.width / 2, manager.height / 2);
    }
}
