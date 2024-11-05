package bwi.brick_breaker;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Brick extends Rectangle2D.Double{

    private boolean brickState;
    private double height;
    private double width;
    private double x, y;
    private Color color;

    // should this be a list or a single brick?

    public Brick(boolean brickState, double height, double width, double x, double y, Color color) {
        super(x, y, width, height);
        this.brickState = brickState;
        this.height = height; // set the height and width
        this.width = width;
        this.x = x;
        this.y = y;
        this.color = color;
        // set the color
    }

    /**
     * I changed this method to be a boolean so if its broken it is true, if its not then
     *  its false
     */
    public boolean getBroken() {
        return brickState;
    }

    public void setBroken(boolean brickState) {
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

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }


}
