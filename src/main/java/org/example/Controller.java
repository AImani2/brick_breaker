package org.example;

public class Controller
{
    // testing making a change
    private Ball ball;
    private Brick brick;
    BrickBreakerModel model;

    private Component view;

    public Controller(Ball ball, Brick brick, BrickBreakerModel model, Component view)
    {
        this.ball = ball;
        this.brick = brick;
        this.model = model;
        this.view = view;
    }

    public void hitWall()
    {
        // get current angle of ball:
        double angle = ball.getAngle();
        angle = 180 - angle;
        ball.setAngle(angle);
    }

    public void hitBrick()
    {
        // bounce direction of ball:
        double angle = ball.getAngle();
        angle = -angle;
        ball.setAngle(angle);

        // break the brick
        //TODO: we need to figure out if the Brick class
        // holds a single brick or multiple
        // Should we put the bricks in a grid? (kind of similar to the sand / conways project?)
        brick.setBroken(0);
    }

    public void hitPaddle()
    {
        // bounce direction of ball:
        double angle = ball.getAngle();
        angle = -angle;
        ball.setAngle(angle);

    }

    // when ball falls below screen and player loses
    public void fallBall()
    {
        model.endGame();
    }

    /*
    methods needed:
    	- Hit wall
	- Hit brick
	- Hit padel
Fall and person loses
     */



}
