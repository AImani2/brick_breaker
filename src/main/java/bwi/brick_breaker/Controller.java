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

    public void hitWall()
    {
        // get current angle of ball:
        double angle = ball.getAngle();
        angle = 180 - angle;
        ball.setAngle(angle);
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
