package bwi.brick_breaker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardComponent extends JComponent {

    private final Ball ball;
    private final Paddle paddle;
    private final ArrayList<Brick> bricks;
    private final Controller controller;

    public BoardComponent(Ball ball, Paddle shooter, ArrayList<Brick> bricks) {
        this.ball = ball;
        this.paddle = shooter;
        this.bricks = bricks;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //draw the ball
        g2.setColor(Color.BLUE);
        g2.fill(ball);

        //draw the shooter
        g2.setColor(Color.BLACK);
        g2.fill(paddle);

        //draw the bricks
        g2.setColor(Color.MAGENTA);

        //draw the bricks
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            if (!brick.isBroken()) {
                g2.fill(brick);
            }
        }

    }
    public void updateBoard() {
        ball.move();

        checkBricks();
        checksPaddle();

        //check walls
        repaint();
    }

    public void checksPaddle() {
        if (ball.getBounds2D().intersects(paddle.getBounds())) {
            controller.hitPaddle();
        }
    }

    public void checkBricks() {
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            //does 0 mean broken?
            if (brick.getBroken() == 1 && ball.getBounds2D().intersects(brick.getBounds())) {
                controller.hitBrick();
            }
        }
    }

    //need to add a method that hits the wall
}
/**
 * methods that we need
 * in brick:
 *  - isBroken() to check if the brick is already broken
 *  - breakBrick() to break the brick
 * in ball:
 *  - move() to move the ball
 */