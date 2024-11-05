package bwi.brick_breaker;

import bwi.brick_breaker.Ball;
import bwi.brick_breaker.Brick;
import bwi.brick_breaker.BrickBreakerModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controller
{
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;
    private BrickBreakerModel model;

    private BoardComponent view;

    public Controller(Ball ball, Paddle paddle, ArrayList<Brick> brick, BrickBreakerModel model, BoardComponent view)
    {
        this.ball = ball;
        this.paddle = paddle;
        this.bricks = brick;
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

    public void hitBrick(Brick brick)
    {
        // bounce direction of ball:
        double angle = ball.getAngle();
        angle = -angle;
        ball.setAngle(angle);

        ball.setVelocity(ball.getVelocity() * -1);

        brick.setBroken(true);


        // break the brick
        //TODO: we need to figure out if the Brick class
        // holds a single brick or multiple
        // Should we put the bricks in a grid? (kind of similar to the sand / conways project?)
//        brick.setBroken(false);
    }

    public void hitPaddle()
    {
        // bounce direction of ball:
        double angle = ball.getAngle();
        ball.setAngle(-angle);
        ball.setVelocity(ball.getVelocity() * -1);
    }

    // when ball falls below screen and player loses
    public void fallBall()
    {
        model.endGame();
    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }

    /*
    methods needed:
    	- Hit wall
	- Hit brick
	- Hit padel
Fall and person loses

hitPaddle
double angle = ball.getAngle();
        angle = -angle;
        ball.setAngle(angle);
        ball.setVelocity(ball.getVelocity() * -1);
     */



}
