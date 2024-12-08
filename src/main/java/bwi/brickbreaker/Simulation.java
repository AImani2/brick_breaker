package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

public class Simulation
{
    private NeuralNetwork nn;
    private Ball ball;
    private Paddle paddle;
    private int width;
    private int height;
    private int distanceToMove = 10;
    private int score;

    public Simulation(NeuralNetwork nn, Ball ball, Paddle paddle, int width, int height)
    {
        this.nn = nn;
        this.ball = ball;
        this.paddle = paddle;
        this.width = width;
        this.height = height;
        score = 0;
    }

    public void simulate()
    {
        int maxNumOfRounds = 10_000;
        System.out.println("New ball");
//        System.out.println("New ball Position: " + ball.getX() + ", " + ball.getY());

        for (int i = 0; i < maxNumOfRounds; i++) {
            moveBall();
//            System.out.println("Position of paddle: " + paddle.getX() + ", " + paddle.getY());

//            System.out.println("ball position: " + ball.getX() + ", " + ball.getY());
            double centerOfBall = ball.getCenterX();
            double centerOfPaddle = paddle.getCenterX();

            double[] input = { centerOfPaddle, centerOfBall };
            double[] output = nn.guess(input);

            if (output[0] > output[1]) {
                movePaddleLeft();
            } else {
                movePaddleRight();
            }

            if (!advance()) {
                System.out.println("Break");
                break;
            }
        }
    }

    // moves the ball
    public void moveBall() {
        ball.move();
    }

    // moves the paddle
    public void movePaddleLeft() {
        if (paddle.getX() - distanceToMove < 0) {
            paddle.setValX(0);
        } else {
            paddle.setValX((int) paddle.getX() - distanceToMove);
        }
    }

    public void movePaddleRight() {
        if (paddle.getX() + distanceToMove + paddle.getWidth() >= width) {
            paddle.setValX((int) (width - paddle.getWidth()));
        } else {
            paddle.setValX((int) paddle.getX() + distanceToMove);
        }
    }


    // checks for collisions with walls
    public void checkWall() {
        if (ball.getX() <= 0 || ball.getWidth() >= width)
        {
            System.out.println("ht the wall");
            ball.collideWall();
        }

    }

    // checks for collisions with top
    public void checkCeiling() {
        if (ball.getY() <= 0)
        {
            ball.collideTopWall();
        }
    }

    // checks for collisions with bricks (eventually)

    // checks for collisions with paddle (increases score)
    public void checkPaddle() {
        if (ball.collides(paddle)) {
            System.out.println("hit paddle: Ball position: " + ball.getX() + ", " + (ball.getY() + ball.getHeight()) + " Paddle position: " + paddle.getX() + ", " + paddle.getY());
            score++;
        }
    }

    // return true is the ball is still above the floor, otherwise false
    public boolean advance()
    {
        double bottomOfBall = ball.getY() + ball.getHeight();

        if (bottomOfBall > height) {
            System.out.println("ball fell ViewHeight:" + height + "Ball: " + bottomOfBall);
            return false;
        }

        checkWall();
        checkCeiling();
        checkPaddle();
        return true;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        paddle.setValY((int) paddle.getInitialY());
        paddle.setValX((int) paddle.getInitialX());

        ball.setAngle(45);
        ball.setX(paddle.getX() + (paddle.getWidth() / 2) - 10);
        ball.setY(paddle.getY() - 20);
    }

}
