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
    private final int distanceToMove = 10;
    private boolean gameWon = true;

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
        checkIfGameOver();
        view.repaint();
    }

    private void checkIfGameOver() {
        for (Brick brick : bricks) {
            if (!brick.getBroken()) {
                gameWon = false;
                break;
            }
        }
        if (gameWon) {
            model.endGame();
            System.out.println("You won!");
            timer.stop();
            gameStarted = false;

            String message = "You won!\nDo you want to start again?";

            int start = JOptionPane.showConfirmDialog(view, message, "Game Over", JOptionPane.YES_NO_OPTION);
            if (start == JOptionPane.YES_NO_OPTION) {
                resetGame();
                startGame();
            } else {
                SwingUtilities.getWindowAncestor(view).dispose();
            }
        }
    }

    // add a checkifGameOver method and call it in gameUpdate
    // when all balls are broken then endgame

    public void moveBall() {
        //ball.setY(ball.getY() - ball.getVelocity());
        ball.move();
    }

    public void checkCollisions() {
        checkPaddleCollision();
        checkBrickCollision();
        checkBounds();
    }

    public boolean checkPaddleCollision() {
        boolean collision = false;

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
            collision = true;
            System.out.println("Hit paddle");
        } else if (bottomOfBall > view.getHeight()) {
//            System.out.println("ball fell, bottom of ball: " + bottomOfBall + " height of view: " + view.getHeight());
            fallBall();
        }
        return collision;

    }


    public void checkBrickCollision() {
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);

            if (!brick.getBroken() && ball.getBounds2D().intersects(brick.getBounds())) {

                String collisionSide = getCollisionSide(ball, brick);
                hitBrick(collisionSide);

                brick.setBroken(true);
            }
        }
    }

    private String getCollisionSide(Ball ball, Brick brick)
    {
        double ballCenterX = ball.getX() + ball.getWidth() / 2;
        double ballCenterY = ball.getY() + ball.getHeight() / 2;
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
            return "horizontal"; // Top side collision
        } else if (distanceToBottom < distanceToLeft && distanceToBottom < distanceToRight
                && distanceToBottom < distanceToTop) {
            return "horizontal"; // Bottom side collision
        } else {
            return "vertical"; // Left or right side collision
        }
    }

    public void checkBounds() {
        int screenWidth = view.getWidth();
        int screenHeight = view.getHeight();
        // new attempt:
        if (ball.getX() <= 0)
        {
            hitWall(1);
            ball.setX(0);
        } else if (ball.getX() + ball.getWidth() >= screenWidth)
        {
            hitWall(1);
            ball.setX(screenWidth - ball.getWidth());
        }


        // old code:
        // Check left and right boundaries
        /*if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= screenWidth) {
            hitWall("vertical"); // Left or right wall
        }*/

        // Check top boundary
        if (ball.getY() <= 0) {
            hitWall(0); // Top wall
        }
    }

    public void hitWall(int side)
    {
        // get current angle of ball:
        double angle = ball.getAngle();

        if (side == 0) { // horizontal side
            // Bounce off top wall by reversing the vertical direction
            ball.setAngle(-angle);
            System.out.println("Resetting horizontal angle: " + angle);
        } else if (side == 1) { // vertical side
            // Bounce off side walls by reversing the horizontal direction
            ball.setAngle(180 - angle);
            System.out.println("Resetting angle: " + angle);
        }
    }


    public void hitBrick(String side)
    {
        // bounce direction of ball:
        double angle = ball.getAngle();

        if ("horizontal".equals(side)) {
            // Bounce off top wall by reversing the vertical direction
            ball.setAngle(-angle);
        } else if ("vertical".equals(side)) {
            // Bounce off side walls by reversing the horizontal direction
            ball.setAngle(180 - angle);
        }
        /*ball.setAngle(-angle);
        ball.setVelocity(ball.getVelocity() * -1);*/

    }


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
        paddle.setValX((int) paddle.getInitialX());
        paddle.setValY((int) paddle.getInitialY());
        int valX = (int) paddle.getX() + ((int) paddle.getWidth() / 2) - 10;
        int valY = (int) paddle.getY() - 20;
        ball.setAngle(ball.getInitialAngle());
        ball.setVelocity(ball.getInitialVelocity());
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
                    movePaddleRight();
                    break;
                case KeyEvent.VK_LEFT:
                    movePaddleLeft();
                    break;
                default:
            }
        }
        view.repaint();
    }

    public void movePaddleLeft()
    {
        if (paddle.getX() - distanceToMove < 0) {
            paddle.setValX(0);
            System.out.println("reset the x value to be: " + paddle.getX());
        } else {
            paddle.setValX((int) paddle.getX() - distanceToMove);
            System.out.println("reset the x value to be: " + paddle.getX());
        }
        view.repaint();
    }

    public void movePaddleRight()
    {
        if (paddle.getX() + distanceToMove + paddle.getWidth() >= view.getWidth()) {
            paddle.setValX((int) (view.getWidth() - paddle.getWidth()));
            System.out.println("reset the x value to be: " + paddle.getX());
        } else {
            paddle.setValX((int) paddle.getX() + distanceToMove);
            System.out.println("reset the x value to be: " + paddle.getX());
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
