package bwi.brickbreaker;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball extends Ellipse2D.Double {

    private double angle;
    private double velocity;
    private Color color;

    public Ball(double angle, double velocity, double x, double y, double diameter, Color color) {
        super(x, y, diameter, diameter);
        this.angle = angle;
        this.velocity = velocity; // should velocity always be the same?
        this.color = color;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void move(int screenWidth, int screenHeight) {
        // Update ball position based on angle and velocity
        x += velocity * Math.cos(Math.toRadians(angle));
        y += velocity * Math.sin(Math.toRadians(angle));

        // Check if ball is going off the left or right side of the screen
        if (x <= 0 || x + getWidth() >= screenWidth) {
            angle = 180 - angle; // Reflect angle horizontally
        }

        // Check if ball is going off the top of the screen
        if (y <= 0) {
            angle = -angle; // Reflect angle vertically
        }
    }

}
