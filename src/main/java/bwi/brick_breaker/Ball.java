package bwi.brick_breaker;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball extends Ellipse2D.Double {

    private double angle;
    private double velocity;
    private double x, y;
    private double diameter;
    private Color color;

    public Ball(double angle, double velocity, double x, double y, double diameter, Color color) {
        this.angle = angle;
        this.velocity = velocity; // should velocity always be the same?
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.color = color;
        // set the color and diameter here
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // do we need the following methods?

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
