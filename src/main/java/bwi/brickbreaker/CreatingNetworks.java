package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CreatingNetworks {

    public static void main(String[] args) {
        Paddle paddle = new Paddle(350, 500, 20, 100, Color.MAGENTA);
        int x = (int) paddle.getX() + ((int) paddle.getWidth() / 2) - 10;
        int y = (int) paddle.getY() - 20;
        Ball ball = new Ball(45, 5, x, y, 20, Color.CYAN);

        ArrayList<Brick> bricks = new ArrayList<>();
        BoardComponent view = new BoardComponent(ball, paddle, bricks);
        view.setSize(500, 500);
        bricks = view.layBricksOnGrid();

        BrickBreakerModel model = new BrickBreakerModel(ball, bricks);
        Controller controller = new Controller(ball, paddle, bricks, model, view);

        NeuralNetworkTakeTwo neuralNetworkTakeTwo = new NeuralNetworkTakeTwo(ball, paddle, controller, bricks, view);

        int generations = 5;

        List<NetworkAndScore> topPerformers = new ArrayList<>();

        for (int i = 0; i < generations; i++) {
            //System.out.println("Generation: " + (i + 1));
            topPerformers = neuralNetworkTakeTwo.evaluatePerformance();
            //System.out.println("Finished generation " + (i + 1));
        }

        topPerformers.get(0).getNetwork().writeToFile();

    }

}

