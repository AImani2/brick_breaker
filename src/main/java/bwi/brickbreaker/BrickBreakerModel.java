package bwi.brickbreaker;

import java.util.List;

public class BrickBreakerModel {

    // How to do this.
    // Bricks are a list
    // should bricks be 1 or 0?
    // change brick to 0 when it is hit?
    // only the 1 bricks will show
    // when all bricks are 0 then the game is over

    private Ball ball;
    private List<Brick> bricks;
    // or should we just do this with an array?
    private int[] bricksV2;
    private Panel panel;

    // what methods do I need here?
    public BrickBreakerModel(Ball ball, List<Brick> bricks, Panel panel) {
        this.ball = ball;
        this.bricks = bricks;
        this.panel = panel;
    }

    public void endGame() {
        //figure out the logic here on how to end the game
    }


}
