package bwi.brickbreaker;

import java.util.List;

public class BrickBreakerModel {

    private Ball ball;
    private List<Brick> bricks;
    // or should we just do this with an array?
    private int[] bricksV2;
    //private Panel panel;

    // what methods do I need here?
    public BrickBreakerModel(Ball ball, List<Brick> bricks) {
        this.ball = ball;
        this.bricks = bricks;
        //this.panel = panel;
    }

    public void endGame() {
        //figure out the logic here on how to end the game
    }


}
