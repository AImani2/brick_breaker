package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Simulation
{
    private NeuralNetwork nn;
    private Ball ball;
    private Paddle paddle;
    private int width;
    private int height;
    private int distanceToMove = 10;
    private int score;
    private boolean hitPaddle;
    private Brick brick;
    private BrickFactory brickFactory;
    private boolean justHitPaddle = true;
    private boolean justHitBrick = false;
    private Random rand = new Random();

    public Simulation(NeuralNetwork nn, Ball ball, Paddle paddle, int width, int height, BrickFactory brickFactory)
    {
        this.nn = nn;
        this.ball = ball;
        this.paddle = paddle;
        this.width = width;
        this.height = height;
        score = 0;
        this.brickFactory = brickFactory;
        brick = brickFactory.newBrick();
    }

    public void simulate()
    {
        int maxNumOfRounds = 10_000;

        for (int i = 0; i < maxNumOfRounds; i++) {

            if (!advance()) {
                break;
            }
        }
    }

    // return true if the ball is still above the floor, otherwise false
    public boolean advance()
    {
        moveBall();

        double bottomOfBall = ball.getY() + ball.getHeight();

        if (bottomOfBall > height) {
            return false;
        }

        movePaddle(nn);

        checkBrick();
        checkWall();
        checkCeiling();
        if (!hitPaddle) {
            checkPaddle();
        }
        return true;
    }

    public void movePaddle(NeuralNetwork neuralNetwork) {
        double[] input = { ball.getCenterX(), ball.getCenterY(), brick.getCenterX(), brick.getCenterY() };
        double[] output = neuralNetwork.guess(input);

        if (output[0] > output[1]) {
            movePaddleLeft();
        } else {
            movePaddleRight();
        }

    }

    // getters:
    public Ball getBall() {
        return ball;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Brick getBrick() {
        return brick;
    }
    public int getScore() {
        return score;
    }

    // move methods:
    public void moveBall() {
        ball.move();
    }

    public void movePaddleLeft() {
        if ((paddle.getX() - distanceToMove) > 0)
        {
            paddle.setValX((int) paddle.getX() - distanceToMove);
        }
    }

    public void movePaddleRight() {
        if (paddle.getX() + distanceToMove + paddle.getWidth() < width)
        {
            paddle.setValX((int) (paddle.getX() + distanceToMove));
        }
    }


    // check collision methods:
    public void checkWall() {
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= width)
        {
            ball.collideWall();
            hitPaddle = false;
        }

    }

    public void checkCeiling() {
        if (ball.getY() <= 0)
        {
            ball.collideTopWall();
            hitPaddle = false;
        }
    }

    public void checkPaddle() {
        if (ball.collides(paddle))
        {
            hitPaddle = true;
            // adjust points only if just hit a brick:
            if (justHitBrick)
            {
                score++;
                justHitPaddle = true;
                justHitBrick = false;
            }
        }
    }

    public void checkBrick() {
        if (ball.collidesWithBrick(brick)) {
            hitPaddle = false;
            brick = brickFactory.newBrick();

            if (justHitPaddle) {
                score++;
                justHitBrick = true;
                justHitPaddle = false;
            }
        }
    }

    public void reset() {
        //Rivka's code
        paddle.setValY((int) paddle.getInitialY());
        paddle.setValX((int) (Math.random() * (width - paddle.getWidth()))); // Randomize paddle X position

        ball.setAngle(Math.random() > 0.5 ? 45 : 30);

        ball.setX(paddle.getX() + (paddle.getWidth() / 2) - 10);
        ball.setY(paddle.getY() - 20);

        /*ball.setX(paddle.getX() + (paddle.getWidth() / 2) - (ball.getWidth() / 2));
        ball.setY(paddle.getY() - ball.getHeight());*/

        /*ball.setDx((rand.nextDouble() * 4) - 2);
        ball.setDy(-1);*/

        brick = brickFactory.newBrick();


        //Aliza's Code
        paddle.setValY((int) paddle.getInitialY());
        paddle.setValX((int) (Math.random() * (width - paddle.getWidth()))); // Randomize paddle X position

        ball.setAngle(Math.random() > 0.5 ? 45 : 30);
        ball.setX(paddle.getX() + (paddle.getWidth() / 2) - 10);
        ball.setY(paddle.getY() - 20);

    }

}
