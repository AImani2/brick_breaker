package bwi.brickbreaker;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball extends Ellipse2D.Double {

    private double angle;
    //set velocity to a value
    private double velocity;
    private final Color color;
    private final double initialVelocity;
    private final double initialAngle;

    public Ball(double angle, double velocity, double x, double y, double diameter, Color color) {
        super(x, y, diameter, diameter);
        this.angle = angle;
        this.velocity = velocity; // should velocity always be the same?
        this.color = color;
        initialVelocity = velocity;
        initialAngle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getInitialVelocity() {
        return initialVelocity;
    }

    public double getInitialAngle() {
        return initialAngle;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void move() {
        x += velocity * Math.cos(Math.toRadians(angle));
        y -= velocity * Math.sin(Math.toRadians(angle)); // Subtract for upward direction
    }

}
