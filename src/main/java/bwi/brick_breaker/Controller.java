package bwi.brick_breaker;
import java.util.ArrayList;

public class Controller
{
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;
    private BrickBreakerModel model;

    private BoardComponent view;

    public Controller(Ball ball, Paddle paddle, ArrayList<Brick> brick, BrickBreakerModel model, BoardComponent view)
    {
        this.ball = ball;
        this.paddle = paddle;
        this.bricks = brick;
        this.model = model;
        this.view = view;
    }

    public void hitWall(String side)
    {
        // get current angle of ball:
        double angle = ball.getAngle();
        /*angle = 180 - angle;
        ball.setAngle(angle);*/

        /*if ("horizontal".equals(side)) {
            angle = -angle; // Reflect angle vertically for top/bottom walls
        } else if ("vertical".equals(side)) {
            angle = 180 - angle; // Reflect angle horizontally for left/right walls
        }*/
        ball.setVelocity(ball.getVelocity() * -1);
        ball.setAngle(-angle);

    }

    public void hitBrick(Brick brick)
    {
        // bounce direction of ball:
        double angle = ball.getAngle();
        angle = -angle;
        ball.setAngle(angle);

        ball.setVelocity(ball.getVelocity() * -1);

        brick.setBroken(true);
    }

    public void hitPaddle()
    {
        // bounce direction of ball:
        double angle = ball.getAngle();
        ball.setAngle(-angle);
        ball.setVelocity(ball.getVelocity() * -1);

        // Adjust angle based on where ball hits the paddle
        /*double paddleCenter = paddle.getX() + (paddle.getWidth() / 2);
        double ballCenter = ball.getX() + (ball.getWidth() / 2);
        angle += (ballCenter - paddleCenter) * 0.1; // Adjust angle based on offset from paddle center
        */

    }

    // when ball falls below screen and player loses
    public void fallBall()
    {
        model.endGame();
    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }
}
