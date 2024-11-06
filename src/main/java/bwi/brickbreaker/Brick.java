package bwi.brickbreaker;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Brick extends Rectangle2D.Double {

    private boolean brickState;
    private double height;
    private double width;
    private double valX;
    private double valY;
    private Color color;

    public Brick(boolean brickState, double height, double width, double valX, double valY, Color color) {
        super(valX, valY, width, height);
        this.brickState = brickState;
        this.height = height; // set the height and width
        this.width = width;
        this.valX = valX;
        this.valY = valY;
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
        return valX;
    }

    public void setValX(double valX) {
        this.valX = valX;
    }

    public double getY() {
        return valY;
    }

    public void setValY(double valY) {
        this.valY = valY;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }


}
