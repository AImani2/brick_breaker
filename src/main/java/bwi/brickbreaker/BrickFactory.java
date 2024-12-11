package bwi.brickbreaker;

import java.awt.*;
import java.util.Random;

public class BrickFactory {

    int screenWidth;
    int screenHeight;
    int brickWidth;
    int brickHeight;

    public BrickFactory(int screenWidth, int screenHeight, int brickWidth, int brickHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight / 2;
        this.brickWidth = brickWidth;
        this.brickHeight = brickHeight;
    }

    public Brick newBrick() {
        Random rand = new Random();

        int x = rand.nextInt(screenWidth - brickWidth);
        int y = rand.nextInt(screenHeight);

        return new Brick(false, brickHeight, brickWidth, x, y, Color.RED);
    }
}
