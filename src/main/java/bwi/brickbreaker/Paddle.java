package bwi.brickbreaker;

import java.awt.*;

public class Paddle extends Rectangle {

    private Color color;
    private double initialX;
    private double initialY;

    public Paddle(double valX, double valY, double height, double width, Color color) {
        super((int) valX, (int) valY, (int) width, (int) height);

        this.color = color;
        initialX = valX;
        initialY = valY;
    }

    public double getInitialX() {
        return initialX;
    }

    public double getInitialY() {
        return initialY;
    }

    public Color getColor() {
        return color;
    }

    public void setValX(int valX) {
        this.x = valX;
    }

    public void setValY(int valY) {
        this.x = valY;
    }
}
