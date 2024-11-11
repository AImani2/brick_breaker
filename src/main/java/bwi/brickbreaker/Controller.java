package bwi.brickbreaker;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
    private final int distanceToMove = 5;

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
        double bottomOfBall = ball.getY() + ball.getHeight();
        double leftOfBall = ball.getX();
        double rightOfBall = ball.getX() + ball.getWidth();

        double topOfPaddle = paddle.getY();
        double leftOfPaddle = paddle.getX();
        double rightOfPaddle = paddle.getX() + paddle.getWidth();

        boolean intersectY = bottomOfBall >= topOfPaddle;
        boolean intersectX = rightOfBall >= leftOfPaddle && leftOfBall <= rightOfPaddle;

        if (intersectY && intersectX) {
            hitPaddle();
        } else if (bottomOfBall > view.getHeight()) {
            fallBall();
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

        String message = "Game over!\nDo you want to start again?";

        int start = JOptionPane.showConfirmDialog(view, message, "Game Over", JOptionPane.YES_NO_OPTION);
        if (start == JOptionPane.YES_NO_OPTION) {
            resetGame();
            startGame();
        } else {
            SwingUtilities.getWindowAncestor(view).dispose();
        }
    }

    public void resetGame() {
        paddle.setValX(paddle.getInitialX());
        paddle.setValY(paddle.getInitialY());
        int valX = (int) paddle.getX() + ((int) paddle.getWidth() / 2) - 10;
        int valY = (int) paddle.getY() - 20;
        ball.setY(valY);
        ball.setX(valX);
        view.repaint();
    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameStarted) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    if (paddle.getX() + distanceToMove + paddle.getWidth() >= view.getWidth()) {
                        paddle.setValX(view.getWidth() - paddle.getWidth());
                    } else {
                        paddle.setValX(paddle.getX() + distanceToMove);
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (paddle.getX() - distanceToMove < 0) {
                        paddle.setValX(0);
                    } else {
                        paddle.setValX(paddle.getX() - distanceToMove);
                    }
                    break;
                default:
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
