package me.paul.triangles;

import processing.core.PVector;

class ControlsBox {

    private static final float BORDER_WEIGHT = 20;
    private static final float LINE_WEIGHT = 10;
    private static final float PADDING = 100;

    private TriangleManager manager;

    private PVector topLeft;
    private PVector topCenter;
    private PVector topRight;
    private PVector centerLeft;
    private PVector center;
    private PVector centerRight;
    private PVector bottomLeft;
    private PVector bottomCenter;
    private PVector bottomRight;

    ControlsBox(TriangleManager manager_) {
        manager = manager_;

        topLeft = new PVector(PADDING, PADDING);
        topCenter = new PVector(manager.width / 2, PADDING);
        topRight = new PVector(manager.width - PADDING, PADDING);
        centerLeft = new PVector(PADDING, manager.height / 2);
        center = new PVector(manager.width / 2, manager.height / 2);
        centerRight = new PVector(manager.width - PADDING, manager.height / 2);
        bottomLeft = new PVector(PADDING, manager.height - PADDING);
        bottomCenter = new PVector(manager.width / 2, manager.height - PADDING);
        bottomRight = new PVector(manager.width - PADDING, manager.height - PADDING);

    }

    void update() {
        topLeft = new PVector(PADDING, PADDING);
        topCenter = new PVector(manager.width / 2, PADDING);
        topRight = new PVector(manager.width - PADDING, PADDING);
        centerLeft = new PVector(PADDING, manager.height / 2);
        center = new PVector(manager.width / 2, manager.height / 2);
        centerRight = new PVector(manager.width - PADDING, manager.height / 2);
        bottomLeft = new PVector(PADDING, manager.height - PADDING);
        bottomCenter = new PVector(manager.width / 2, manager.height - PADDING);
        bottomRight = new PVector(manager.width - PADDING, manager.height - PADDING);
    }

    // TODO: Finish drawing
    void draw() {
        manager.rectMode(manager.CORNERS);
        manager.strokeWeight(BORDER_WEIGHT);
        manager.fill(manager.color(0, 0, 100));
        manager.rect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y, 1f);

        manager.fill(0);
        manager.textMode(manager.SHAPE);

        manager.textAlign(manager.CENTER, manager.TOP);
        manager.textSize(100);
        manager.text("CONTROLS / HELP", topCenter.x, topCenter.y);

        manager.strokeWeight(LINE_WEIGHT);
        manager.line(topLeft.x + 200, topLeft.y + 110, topRight.x - 200, topRight.y + 110);

        float xLoc = topLeft.x + 15;
        float yLoc = topLeft.y + 150;
        float textSize = 25;
        manager.textSize(textSize);
        manager.textAlign(manager.LEFT);
        manager.text("Right Click:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("Left Click:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("Center Click:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("Up Arrow:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("Down Arrow:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("Left Arrow:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("Right Arrow:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("Space:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("Enter / Return:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("h:", xLoc, yLoc);
        xLoc = topLeft.x + 500;
        yLoc = topLeft.y + 150;

        manager.text("i:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("k:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("j:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("l:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("u:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("o:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("c:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("b:", xLoc, yLoc);
        yLoc += textSize + 10;
        manager.text("d:", xLoc, yLoc);
        yLoc += textSize + 10;
    }
}
