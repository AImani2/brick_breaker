package bwi.brickBreaker;

import java.awt.*;

public class Paddle extends Rectangle {

    private double xVal;
    private double yVal;
    private double width;
    private double height;
    private Color color;

    public Paddle(double xVal, double yVal, double height, double width, Color color) {
        super((int) xVal, (int) yVal, (int) width, (int) height);
        this.xVal = xVal;
        this.yVal = yVal;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void move(double amountToMove, double totalWidth) {
        xVal += amountToMove;

        if (xVal < 0) {
            xVal = 0;
        } else if (xVal + width > totalWidth) {
            xVal = totalWidth;
        }

        setBounds((int) xVal, (int) yVal, (int) width, (int) height);
    }

    @Override
    public double getX() {
        return xVal;
    }

    @Override
    public double getY() {
        return yVal;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setxVal(double xVal) {
        this.xVal = xVal;
    }

    public void setyVal(double yVal) {
        this.yVal = yVal;
    }
}
