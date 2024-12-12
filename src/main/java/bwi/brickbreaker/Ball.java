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

    public void setDx(double dx)
    {
        this.dx = dx;
    }

    public void setDy(double dy)
    {
        this.dy = dy;
    }

    public Color getColor() {
        return color;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public boolean collides(Paddle paddle) {

        boolean collision = false;

        int maxAngle = 60;
        if (paddle.getBounds().intersects(this.getBounds())) {

            // Determine how far along the paddle the ball hit
            double hitPosition = (x - paddle.getX()) / paddle.getWidth();

            // Split the paddle into three sections: left, middle, and right
            double leftBoundary = 0.33; // Left section ends at 33%
            double rightBoundary = 0.67; // Right section starts at 67%

            // Define angles for each section
            double leftAngle = 135; // Ball hits left section
            double centerAngle = 180; // Ball hits middle section
            double rightAngle = 225; // Ball hits right section

            // Determine the bounce angle based on the section hit
            double bounceAngle;
            if (hitPosition < leftBoundary) {
                setAngle(leftAngle);
            } else if (hitPosition < rightBoundary) {
                setAngle(centerAngle);
            } else {
                setAngle(rightAngle);
            }


            // new code - based on instructions
            dy = -dy;
            dx = (paddle.getCenterX() - this.getCenterX()) / (paddle.getWidth() / 2);

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

    public boolean collidesWithBrick(Brick brick) {
        boolean collision = false;

        if (brick.getBounds().intersects(this.getBounds())) {
            if (getCollisionSide(brick) == 0) {
                dy = -dy; // Horizontal bounce (top or bottom)
            } else {
                dx = -dx; // Vertical bounce (left or right)
            }
            collision = true;
            brick.setBroken(true);
        }

        return collision;
    }

    // 0 is horizontal & 1 is vertical
    private int getCollisionSide(Brick brick)
    {
        double ballCenterX = getX() + getWidth() / 2;
        double ballCenterY = getY() + getHeight() / 2;
        double brickLeft = brick.getX();
        double brickRight = brick.getX() + brick.getWidth();
        double brickTop = brick.getY();
        double brickBottom = brick.getY() + brick.getHeight();

        // Determine the minimum distance to each side of the brick
        double distanceToLeft = Math.abs(ballCenterX - brickLeft);
        double distanceToRight = Math.abs(ballCenterX - brickRight);
        double distanceToTop = Math.abs(ballCenterY - brickTop);
        double distanceToBottom = Math.abs(ballCenterY - brickBottom);

        // Check if the collision is vertical or horizontal by finding the smallest distance
        if (distanceToTop < distanceToLeft && distanceToTop < distanceToRight && distanceToTop < distanceToBottom) {
            return 0; // Top side collision
        } else if (distanceToBottom < distanceToLeft && distanceToBottom < distanceToRight
                && distanceToBottom < distanceToTop) {
            return 0; // Bottom side collision
        } else {
            return 1; // Left or right side collision
        }
    }

}
