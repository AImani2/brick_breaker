package bwi.brickbreaker;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GameTests {

    @Test
    public void ballMoves() throws InterruptedException {
        //given
        Ball ball = new Ball(45, 5, 350, 530, 20, Color.MAGENTA);
        Paddle paddle = new Paddle(350, 500, 20, 100, Color.BLUE);
        ArrayList<Brick> bricks = new ArrayList<>();
        BoardComponent view = new BoardComponent(ball, paddle, bricks);
        BrickBreakerModel model = new BrickBreakerModel(ball, bricks);
        Controller controller = new Controller(ball, paddle, bricks, model, view);

        //when
        double startX = ball.getX();
        double startY = ball.getY();
        controller.startGame();
        Thread.sleep(50);

        //then
        assertNotEquals(startX, ball.getX());
        assertNotEquals(startY, ball.getY());

    }


    @Test
    public void hitVerticalWall() throws InterruptedException
    {
        // given
        Ball ball = new Ball(45, 5, 350, 530, 20, Color.MAGENTA);
        Paddle paddle = new Paddle(350, 500, 20, 100, Color.BLUE);
        ArrayList<Brick> bricks = new ArrayList<>();
        BoardComponent view = new BoardComponent(ball, paddle, bricks);
        BrickBreakerModel model = new BrickBreakerModel(ball, bricks);
        Controller controller = new Controller(ball, paddle, bricks, model, view);

        // when
        double beforeAngle = ball.getAngle();
        controller.hitWall(1);

        // then
        assertNotEquals(beforeAngle, ball.getAngle());
    }

    @Test
    public void hitHorizontalWall() throws InterruptedException
    {
        // given
        Ball ball = new Ball(45, 5, 350, 530, 20, Color.MAGENTA);
        Paddle paddle = new Paddle(350, 500, 20, 100, Color.BLUE);
        ArrayList<Brick> bricks = new ArrayList<>();
        BoardComponent view = new BoardComponent(ball, paddle, bricks);
        BrickBreakerModel model = new BrickBreakerModel(ball, bricks);
        Controller controller = new Controller(ball, paddle, bricks, model, view);

        // when
        double beforeAngle = ball.getAngle();
        controller.hitWall(0);
        // then
        assertNotEquals(beforeAngle, ball.getAngle());
    }

    @Test
    public void hitPaddle() throws InterruptedException
    {
        // given
        Ball ball = new Ball(45, 5, 350, 530, 20, Color.MAGENTA);
        Paddle paddle = new Paddle(350, 500, 20, 100, Color.BLUE);
        ArrayList<Brick> bricks = new ArrayList<>();
        BoardComponent view = new BoardComponent(ball, paddle, bricks);
        BrickBreakerModel model = new BrickBreakerModel(ball, bricks);
        Controller controller = new Controller(ball, paddle, bricks, model, view);

        // when
        double beforeAngle = ball.getAngle();
        controller.hitPaddle();

        // then
        assertNotEquals(beforeAngle, ball.getAngle());
        assertEquals(ball.getY(), paddle.getY() - ball.getHeight() - 1);
    }

    @Test
    public void hitBrickHorizontal() throws InterruptedException
    {
        // given
        Ball ball = new Ball(45, 5, 350, 530, 20, Color.MAGENTA);
        Paddle paddle = new Paddle(350, 500, 20, 100, Color.BLUE);
        ArrayList<Brick> bricks = new ArrayList<>();
        BoardComponent view = new BoardComponent(ball, paddle, bricks);
        BrickBreakerModel model = new BrickBreakerModel(ball, bricks);
        Controller controller = new Controller(ball, paddle, bricks, model, view);

        // when
        double beforeAngle = ball.getAngle();
        controller.hitBrick("horizontal");

        // then
        assertNotEquals(beforeAngle, ball.getAngle());
    }

    @Test
    public void hitBrickVertical() throws InterruptedException
    {
        // given
        Ball ball = new Ball(45, 5, 350, 530, 20, Color.MAGENTA);
        Paddle paddle = new Paddle(350, 500, 20, 100, Color.BLUE);
        ArrayList<Brick> bricks = new ArrayList<>();
        BoardComponent view = new BoardComponent(ball, paddle, bricks);
        BrickBreakerModel model = new BrickBreakerModel(ball, bricks);
        Controller controller = new Controller(ball, paddle, bricks, model, view);

        // when
        double beforeAngle = ball.getAngle();
        controller.hitBrick("vertical");

        // then
        assertNotEquals(beforeAngle, ball.getAngle());
    }

}
