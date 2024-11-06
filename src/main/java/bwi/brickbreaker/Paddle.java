package bwi.brickbreaker;

import java.awt.*;

public class Paddle extends Rectangle {

    private double valX;
    private double valY;
    private double width;
    private double height;
    private Color color;

    public Paddle(double valX, double valY, double height, double width, Color color) {
        super((int) valX, (int) valY, (int) width, (int) height);
        this.valX = valX;
        this.valY = valY;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void move(double amountToMove, double totalWidth) {
        valX += amountToMove;

        if (valX < 0) {
            valX = 0;
        } else if (valX + width > totalWidth) {
            valX = totalWidth;
        }

        setBounds((int) valX, (int) valY, (int) width, (int) height);
    }

    @Override
    public double getX() {
        return valX;
    }

    @Override
    public double getY() {
        return valY;
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

    public void setValX(double valX) {
        this.valX = valX;
    }

    public void setValY(double valY) {
        this.valY = valY;
    }
}
