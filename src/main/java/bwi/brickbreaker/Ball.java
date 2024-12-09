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

    public Ball(double x, double y, double diameter, double dx, double dy, Color color) {
        super(x, y, diameter, diameter);
        //TODO do we want the angle to be 45 or 30?
        //because we are now dealing with dx and dy - i dont know if we need an angle or velocity
        this.angle = 45;
        this.velocity = 1; // should velocity always be the same?
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
        if(paddle.getBounds().intersects(this.getBounds())) {
            dy = -dy;
            dx = (paddle.getCenterX() - this.getCenterX()) / (paddle.getWidth() / 2);
            collision = true;
        }

        return collision;

        /*boolean collision = false;

        double bottomOfBall = y + height;
        double topOfBall = y;
        double leftOfBall = x;
        double rightOfBall = x + width;

        double topOfPaddle = paddle.getY();
        double bottomOfPaddle = paddle.getY() + paddle.getHeight();
        double leftOfPaddle = paddle.getX();
        double rightOfPaddle = paddle.getX() + paddle.getWidth();

        *//*double bufferZone = 1;

        boolean intersectY = (bottomOfBall + bufferZone) >= topOfPaddle && bottomOfBall <= (topOfPaddle + bufferZone);
        boolean intersectX = rightOfBall >= (leftOfPaddle - bufferZone) && leftOfBall <= (rightOfPaddle + bufferZone);
        *//*

        // Check for overlap
        boolean intersectX = rightOfBall > leftOfPaddle && leftOfBall < rightOfPaddle;
        boolean intersectY = bottomOfBall >= topOfPaddle && topOfBall < bottomOfPaddle;

        if (intersectX && intersectY) {
            dy = -(dy);
            dx = (paddle.getCenterX()- this.getCenterX()) / ((double) paddle.width / 2);
            collision = true;
            y = topOfPaddle - height;
        }
        return collision;*/
    }

    public void collideWall() {
        dx = -dx;
    }

    public void collideTopWall() {
        dy = -dy;
    }

}
