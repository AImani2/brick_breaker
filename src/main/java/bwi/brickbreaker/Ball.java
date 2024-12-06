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

    private double dx;
    private double dy;

    public Ball(double velocity, double x, double y, double diameter, double dx, double dy, Color color) {
        super(x, y, diameter, diameter);
        //TODO do we want the angle to be 45 or 30?
        this.angle = 45;
        this.velocity = velocity; // should velocity always be the same?
        this.dx = dx;
        this.dy = dy;
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
        x += dx;
        y += dy;
//        System.out.println("DX: " + dx + " DY: " + dy);
    }

    public boolean collides(Paddle paddle) {
        boolean collision = false;

        double bottomOfBall = y + height;
        double leftOfBall = x;
        double rightOfBall = x + width;

        double topOfPaddle = paddle.getY();
        double leftOfPaddle = x;
        double rightOfPaddle = x + width;

        double bufferZone = 1;

        boolean intersectY = (bottomOfBall + bufferZone) >= topOfPaddle && bottomOfBall <= (topOfPaddle + bufferZone);
        boolean intersectX = rightOfBall >= (leftOfPaddle - bufferZone) && leftOfBall <= (rightOfPaddle + bufferZone);

        if (intersectX && intersectY) {
            dy = -(dy);
            dx = (paddle.getCenterX()- this.getCenterX()) / ((double) paddle.width / 2);
            collision = true;
        }
        return collision;
    }

    public void collideWall() {
        dx = -dx;
    }

    public void collideTopWall() {
        dy = -dy;
    }

}
