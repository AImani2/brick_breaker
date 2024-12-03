package bwi.brickbreaker;

import java.awt.geom.Ellipse2D;

public class BallNN extends Ellipse2D.Double
{
    private double angle;
    private double dx;
    private double dy;

    public BallNN(double angle, double x, double y, double dx, double dy)
    {
        this.angle = angle;
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    boolean collides(Paddle paddle)
    {
        // TODO: is this supposed to be same as controller?
        return false;
    }
    // TODO: are these methods supposed to be in the controller or the ball class?
    // not sure if these are correct:

    void collideRightWall()
    {
        dx = -dx;
    }
    void collideLeftWall()
    {
        dx = -dx;
    }

    void collideTopWall()
    {
        dy = -dy;
    }

}
