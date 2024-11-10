package bwi.brickbreaker;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;

public class Controller implements KeyListener
{
    private final Ball ball;
    private final Paddle paddle;
    private ArrayList<Brick> bricks;
    private final BrickBreakerModel model;
    private final Timer timer;
    private boolean gameStarted = false;
    private final int DISTANCE_TO_MOVE = 5;

    private final BoardComponent view;

    public Controller(Ball ball, Paddle paddle, ArrayList<Brick> bricks, BrickBreakerModel model, BoardComponent view)
    {
        this.ball = ball;
        this.paddle = paddle;
        this.bricks = bricks;
        this.model = model;
        this.view = view;

        timer = new Timer(2, e -> gameUpdate());
        view.setFocusable(true);
        view.requestFocusInWindow();
        view.addKeyListener(this);
    }

    public void startGame() {
        timer.start();
        gameStarted = true;
        view.setFocusable(true);
        view.requestFocusInWindow();
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

        if (ball.getY() + ball.getHeight() >= paddle.getY()) {
            if (ball.getY() + ball.getHeight() >= view.getHeight()) {
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
    }

    public void hitWall(String side)
    {
        // get current angle of ball:
        double angle = ball.getAngle();

        ball.setVelocity(ball.getVelocity() * -1);
        ball.setAngle(45);

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
        ball.setY(paddle.getY() - ball.getHeight() - 1);
    }

    // when ball falls below screen and player loses
    public void fallBall()
    {
        model.endGame();
        System.out.println("end game");
        timer.stop();
        gameStarted = false;
    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameStarted) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    if (paddle.getX() + DISTANCE_TO_MOVE + paddle.getWidth() >= view.getWidth()) {
                        paddle.setValX(view.getWidth() - paddle.getWidth());
                    } else {
                        paddle.setValX(paddle.getX() + DISTANCE_TO_MOVE);
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (paddle.getX() - DISTANCE_TO_MOVE < 0) {
                        paddle.setValX(0);
                    } else {
                        paddle.setValX(paddle.getX() - DISTANCE_TO_MOVE);
                    }
                    break;
            }
        }
        view.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }
}
