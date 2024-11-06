package bwi.brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

public class BoardComponent extends JComponent implements MouseMotionListener {

    private final Ball ball;
    private final Paddle paddle;
    private final ArrayList<Brick> bricks;
    private final Controller controller;
    private boolean gameStarted = false;
    Timer timer = new Timer(2, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            startBall();
        }
    });

    public BoardComponent(Ball ball, Paddle paddle, ArrayList<Brick> bricks, Controller controller) {
        this.ball = ball;
        this.paddle = paddle;
        this.bricks = bricks;
        this.controller = controller;
        addMouseMotionListener(this);
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
        updateBoard();

    }
    public void updateBoard() {

        checkBricks();
        checksPaddle();
        checkBounds();

        repaint();
    }

    public void checksPaddle() {
        if (ball.getBounds2D().intersects(paddle.getBounds())) {
            System.out.println("hitting the paddle!!!!!! current angle: " + ball.getAngle());
            controller.hitPaddle();
        }
    }

    public void checkBricks() {
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            //does 0 mean broken?
            if (!brick.getBroken() && ball.getBounds2D().intersects(brick.getBounds())) {
                System.out.println("hitting a brick!!");
                controller.hitBrick(brick);
            }
        }
    }

    //TODO fix this method so that it checks that the ball doesn't go too high or to the left
    public void checkBounds() {
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        // Check left and right boundaries
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= screenWidth) {
            controller.hitWall("vertical"); // Left or right wall
        }

        // Check top boundary
        if (ball.getY() <= 0) {
            controller.hitWall("horizontal"); // Top wall
        }

        // handle bottom boundary for game over
        if (ball.getY() + ball.getHeight() >= screenHeight) {
            controller.fallBall(); // Player loses if ball goes below screen
        }
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
        while(bricks.size() < numOfBricks) {
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
                System.out.println("added a new brick at " + brick.getX() + " , " + brick.getY());
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


    public void startGame() {
        gameStarted = true;
        timer.start();
    }

    public void startBall() {
        ball.setY(ball.getY() - ball.getVelocity());
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameStarted) {
            int x = e.getX();

            if (x < 0) {
                x = 0;
            } else if (x > getWidth() - paddle.getWidth()) {
                x = getWidth() - (int)paddle.getWidth();
            }
            paddle.setxVal(x);
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    //need to add a method that hits the wall
}