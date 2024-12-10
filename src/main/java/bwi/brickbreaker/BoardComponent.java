package bwi.brickbreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

public class BoardComponent extends JComponent {

    private final Ball ball;
    private final Paddle paddle;
    private Brick brick;
    private Simulation simulation;

    public BoardComponent(Ball ball, Paddle paddle, ArrayList<Brick> bricks) {
        this.ball = ball;
        this.paddle = paddle;
        //this.bricks = bricks;

        setFocusable(true);
        requestFocusInWindow();
    }

    public BoardComponent(Simulation simulation) {
        this.simulation = simulation;
        ball = simulation.getBall();
        paddle = simulation.getPaddle();
        brick = simulation.getBrick();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //draw the ball
        g2.setColor(ball.getColor());
        g2.fill(ball);

        //draw the paddle
        g2.setColor(paddle.getColor());
        g2.fill(paddle);

        g2.setColor(Color.RED);
        if (!brick.getBroken()) {
            g2.fill(brick);
        } else {
            brick = simulation.getBrick();
        }

    }

}