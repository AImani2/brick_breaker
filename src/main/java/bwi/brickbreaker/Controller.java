package bwi.brickbreaker;

import java.util.ArrayList;

import javax.swing.*;

public class Controller
{
    private final Ball ball;
    private final Paddle paddle;
    private ArrayList<Brick> bricks;
    private final BrickBreakerModel model;
    private final Timer timer;

    private final BoardComponent view;

    public Controller(Ball ball, Paddle paddle, ArrayList<Brick> bricks, BrickBreakerModel model, BoardComponent view)
    {
        this.ball = ball;
        this.paddle = paddle;
        this.bricks = bricks;
        this.model = model;
        this.view = view;

        timer = new Timer(2, e -> gameUpdate());
    }

    public void startGame() {
        timer.start();
        view.setGameStarted(true);
        view.repaint();
    }

    public void gameUpdate() {
        moveBall();
        checkCollisions();
        view.repaint();
    }

    public void moveBall() {
        ball.setY(ball.getY() - ball.getVelocity());
    }

    public void checkCollisions() {
        checkPaddleCollision();
        checkBrickCollision();
        checkBounds();
    }

    public void checkPaddleCollision() {
        if (ball.getY() + ball.getHeight() >= view.getHeight()) {
            if (ball.getY() + ball.getHeight() <= paddle.getY()) {
                fallBall();
            } else if (ball.getBounds2D().intersects(paddle.getBounds())) {
                hitPaddle();
            }
        }
    }

    public void checkBrickCollision() {
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);

            if (!brick.getBroken() && ball.getBounds2D().intersects(brick.getBounds())) {
                hitBrick();
                brick.setBroken(true);
            }
        }
    }

    public void checkBounds() {
        int screenWidth = view.getWidth();
        int screenHeight = view.getHeight();

        // Check left and right boundaries
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= screenWidth) {
            hitWall("vertical"); // Left or right wall
        }

        // Check top boundary
        if (ball.getY() <= 0) {
            hitWall("horizontal"); // Top wall
        }

        // handle bottom boundary for game over
        if (ball.getY() + ball.getHeight() >= screenHeight) {
            if (!ball.getBounds2D().intersects(paddle.getBounds())) {
                fallBall(); // Player loses if ball goes below screen
            } else {
                hitPaddle();
            }

        }
//
//        if (ball.getY() > paddle.getY() + paddle.getHeight()) {
//            fallBall();
//        }
    }

    public void hitWall(String side)
    {
        // get current angle of ball:
        double angle = ball.getAngle();

        ball.setVelocity(ball.getVelocity() * -1);
        ball.setAngle(-angle);

    }

    public void hitBrick()
    {
        // bounce direction of ball:
        double angle = ball.getAngle();
        ball.setAngle(-angle);
        ball.setVelocity(ball.getVelocity() * -1);
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
        System.out.println("end game");
        timer.stop();
        view.setGameStarted(false);
    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }
}
