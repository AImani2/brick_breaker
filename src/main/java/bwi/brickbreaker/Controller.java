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
    private final int distanceToMove = 20;

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
        //ball.setY(ball.getY() - ball.getVelocity());
        ball.move();
    }

    public void checkCollisions() {
        checkPaddleCollision();
        checkBrickCollision();
        checkBounds();
    }

//    public void checkPaddleCollision() {
//        double bottomOfBall = ball.getY() + ball.getHeight();
//        double leftOfBall = ball.getX();
//        double rightOfBall = ball.getX() + ball.getWidth();
//
//        double topOfPaddle = paddle.getY();
//        double leftOfPaddle = paddle.getX();
//        double rightOfPaddle = paddle.getX() + paddle.getWidth();
//
//        boolean intersectY = bottomOfBall >= topOfPaddle;
//        boolean intersectX = rightOfBall >= leftOfPaddle && leftOfBall <= rightOfPaddle;
//
//        if (intersectY && intersectX) {
//            hitPaddle();
//        } else if (bottomOfBall > view.getHeight()) {
//            fallBall();
//        }
//    }

    public void checkPaddleCollision() {
        double bottomOfBall = ball.getY() + ball.getHeight();
        double leftOfBall = ball.getX();
        double rightOfBall = ball.getX() + ball.getWidth();

        double topOfPaddle = paddle.getY();
        double leftOfPaddle = paddle.getX();
        double rightOfPaddle = paddle.getX() + paddle.getWidth();

        // Define a small buffer zone (e.g., a few pixels)
        double bufferZone = 1; // Adjust as needed for your game

        // Check if the ball's bottom is near the top of the paddle and if the ball is horizontally near the paddle
        boolean intersectY = (bottomOfBall + bufferZone) >= topOfPaddle && bottomOfBall <= topOfPaddle + bufferZone;
        boolean intersectX = rightOfBall >= leftOfPaddle - bufferZone && leftOfBall <= rightOfPaddle + bufferZone;

        // If ball is near the paddle and intersects, consider it a collision
        if (intersectY && intersectX) {
            hitPaddle(); // Bounce ball off paddle
        } else if (bottomOfBall > view.getHeight()) {
            fallBall(); // Ball has fallen off screen
        }
    }


    public void checkBrickCollision() {
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);

            if (!brick.getBroken() && ball.getBounds2D().intersects(brick.getBounds())) {
                hitBrick(brick);
                brick.setBroken(true);
            }
        }
    }

    public void checkBounds() {
        int screenWidth = view.getWidth();
        int screenHeight = view.getHeight();
        // new attempt:
        if (ball.getX() <= 0)
        {
            hitWall("vertical");
            ball.setX(0);
        } else if (ball.getX() + ball.getWidth() >= screenWidth)
        {
            hitWall("vertical");
            ball.setX(screenWidth - ball.getWidth());
        }


        // old code:
        // Check left and right boundaries
        /*if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= screenWidth) {
            hitWall("vertical"); // Left or right wall
        }*/

        // Check top boundary
        if (ball.getY() <= 0) {
            hitWall("horizontal"); // Top wall
        }
    }

    public void hitWall(String side)
    {
        // get current angle of ball:
        double angle = ball.getAngle();

        /*ball.setVelocity(ball.getVelocity() * -1);
        ball.setAngle(45);*/

        if ("horizontal".equals(side)) {
            // Bounce off top wall by reversing the vertical direction
            ball.setAngle(-angle);
        } else if ("vertical".equals(side)) {
            // Bounce off side walls by reversing the horizontal direction
            ball.setAngle(180 - angle);
        }
    }

//    public void hitBrick()
//    {
//        // bounce direction of ball:
//        double angle = ball.getAngle();
//        ball.setAngle(-angle);
//        ball.setVelocity(ball.getVelocity() * -1);
//    }

public void hitBrick(Brick brick) {
    // Ball's center position and brick’s position
    double ballCenterX = ball.getX() + (ball.getWidth() / 2);
    double brickCenterX = brick.getX() + (brick.getWidth() / 2);

    // If the ball hits the top or bottom of the brick, reverse the vertical direction
    if (ball.getY() + ball.getHeight() <= brick.getY() || ball.getY() >= brick.getY() + brick.getHeight()) {
        ball.setAngle(-ball.getAngle()); // Reverse the vertical direction
    } else {
        // If it hits the sides of the brick, reverse the horizontal direction
        ball.setAngle(180 - ball.getAngle()); // Reverse the horizontal direction
    }

    // Optionally, increase the velocity for more challenge
    ball.setVelocity(ball.getVelocity() * 1.1);
}

//    public void hitPaddle()
//    {
//        // bounce direction of ball:
//        double angle = ball.getAngle();
//        ball.setAngle(-angle);
//        ball.setVelocity(ball.getVelocity() * -1);
//        ball.setY(paddle.getY() - ball.getHeight() - 1);
//    }

//    public void hitPaddle() {
//        double ballCenterX = ball.getX() + (ball.getWidth() / 2); // Center of the ball
//        double paddleCenterX = paddle.getX() + (paddle.getWidth() / 2); // Center of the paddle
//
//        // Calculate the difference between the ball's center and the paddle's center
//        double deltaX = ballCenterX - paddleCenterX;
//
//        // Normalize deltaX to a range between -1 and 1 (the ball can hit anywhere on the paddle)
//        double normalizedDeltaX = deltaX / (paddle.getWidth() / 2);
//
//        // Adjust the angle based on where the ball hits the paddle
//        double newAngle = normalizedDeltaX * 45; // The bounce angle can range from -45 to 45 degrees
//
//        // Reverse the ball's vertical direction
//        ball.setAngle(-newAngle); // Negate the angle to reflect the bounce
//        ball.setVelocity(ball.getVelocity() * 1.05); // Optionally increase speed slightly after bouncing
//        ball.setY(paddle.getY() - ball.getHeight() - 1); // Ensure the ball stays above the paddle
//    }

    public void hitPaddle() {
        // Get the ball's center
        double ballCenterX = ball.getX() + (ball.getWidth() / 2);

        // Get the paddle's left and right boundaries
        double paddleLeftX = paddle.getX();
        double paddleRightX = paddle.getX() + paddle.getWidth();

        // Determine the collision point relative to the paddle
        double collisionPoint = ballCenterX - paddleLeftX;
        double paddleCenter = paddle.getWidth() / 2;

        // Normalize the collision point as a percentage of the paddle's width
        double hitPercentage = collisionPoint / paddle.getWidth();

        // Adjust the ball's angle based on where it hit the paddle (left or right)
        double angle = ball.getAngle();

        // Adjust the angle based on where on the paddle the ball hit
        // Center hits cause a straight bounce, edge hits cause an angle bounce
        double newAngle = 90 - (hitPercentage * 45); // Varying angle based on where the ball hits

        // Ensure the angle is within a reasonable range
        newAngle = Math.max(30, Math.min(newAngle, 150)); // Keep the angle between 30 and 150 degrees

        ball.setAngle(newAngle);

        // Set the ball's Y position to just above the paddle
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
