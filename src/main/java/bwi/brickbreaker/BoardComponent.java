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
    private final ArrayList<Brick> bricks;
    private boolean gameStarted = false;

    public BoardComponent(Ball ball, Paddle paddle, ArrayList<Brick> bricks) {
        this.ball = ball;
        this.paddle = paddle;
        this.bricks = bricks;

        setFocusable(true);
        requestFocusInWindow();
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


        //draw the bricks
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            g2.setColor(brick.getColor());
            if (!brick.getBroken()) {
                g2.fill(brick);
            }
        }
    }

    public void setGameStarted(boolean started) {
        this.gameStarted = started;
    }

    public ArrayList<Brick> layBricksOnGrid() {
        final int numOfBricks = 10;
        final int brickWidth = 100;
        final int brickHeight = 20;
        final int boardWidth = 800;
        final int boardHeight = 600;
        Random rand = new Random();

        int x = 0;
        int y = 0;
        while (bricks.size() < numOfBricks) {
            x = rand.nextInt(boardWidth - brickWidth);
            y = rand.nextInt(boardHeight / 2);

            Brick brick = new Brick(false, brickHeight, brickWidth, x, y, createRandomBrickColor());

            boolean intersect = false;
            for (int j = 0; j < bricks.size(); j++) {
                if (brick.intersects(bricks.get(j))) {
                    intersect = true;
                    break;
                }
            }

            if (!intersect) {
                bricks.add(brick);
            }
        }
        return bricks;
    }

    public Color createRandomBrickColor() {
        Random rand = new Random();

        int red = rand.nextInt(256);
        int green = rand.nextInt(256);
        int blue = rand.nextInt(256);

        return new Color(red, green, blue);
    }

}