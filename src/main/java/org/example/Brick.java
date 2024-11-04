package org.example;

import java.awt.*;

public class Brick {

    private int brickState;
    private double height;
    private double width;
    private double x, y;
    private Color color;

    // should this be a list or a single brick?

    public Brick(int brickState, double height, double width, double x, double y, Color color) {
        this.brickState = brickState;
        this.height = height; // set the height and width
        this.width = width;
        this.x = x;
        this.y = y;
        this.color = color;
        // set the color
    }

    public int getBroken() {
        return brickState;
    }

    public void setBroken(int brickState) {
        this.brickState = brickState;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}