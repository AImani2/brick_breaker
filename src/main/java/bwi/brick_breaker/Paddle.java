package bwi.brick_breaker;

import java.awt.*;

public class Paddle extends Rectangle {

    private double x;
    private double y;
    private double width;
    private double height;
    private Color color;

    public Paddle(double x, double y, double height, double width, Color color) {
        super((int) x, (int) y, (int) width, (int) height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void move(double amountToMove, double totalWidth) {
        x += amountToMove;

        if (x < 0) {
            x = 0;
        } else if (x + width > totalWidth) {
            x = totalWidth;
        }

        setBounds((int) x, (int) y, (int) width, (int) height);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
