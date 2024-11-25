package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NeuralNetworkTakeTwo {

    private static final int AGENTS = 1000;

    private static final int TOP_PERFORMERS = 10;

    private List<NetworkAndScore> population = new ArrayList<>();

    public Ball ball;
    public Paddle paddle;
    private Controller controller;
    public ArrayList<Brick> bricks;
    private BoardComponent view;

    public NeuralNetworkTakeTwo(Ball ball, Paddle paddle, Controller controller, ArrayList<Brick> bricks, BoardComponent view) {
        this.ball = ball;
        this.paddle = paddle;
        this.controller = controller;
        this.bricks = bricks;
        this.view = view;

        // Initialize population with random neural networks
        for (int i = 0; i < AGENTS; i++) {
            NeuralNetwork nn = new NeuralNetwork(1, 2, 4, 2);
            NetworkAndScore networkAndScore = new NetworkAndScore(nn, 0);
            population.add(networkAndScore);
        }
    }

    private static class NetworkAndScore {
        private NeuralNetwork network;
        private int score;

        public NetworkAndScore(NeuralNetwork network, int score) {
            this.network = network;
            this.score = score;
        }

        public NeuralNetwork getNetwork() {
            return network;
        }

        public int getScore() {
            return score;
        }

        public void setNetwork(NeuralNetwork network) {
            this.network = network;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }

    public void evaluatePerformance() {
        for (NetworkAndScore nas : population) {
            reset();
            int score = simulateGame(nas.getNetwork());
            nas.setScore(score);
        }

        population.sort(Comparator.comparingInt(NetworkAndScore::getScore).reversed());

        List<NetworkAndScore> topPerformers = new ArrayList<>(population.subList(0, TOP_PERFORMERS));

        population = regenerate(topPerformers);
    }

    public List<NetworkAndScore> regenerate(List<NetworkAndScore> topPerformers) {
        List<NetworkAndScore> newGeneration = new ArrayList<>();

        while (newGeneration.size() < AGENTS) {
            for (int i = 0; i < TOP_PERFORMERS; i++) {
                for (int j = 0; j < TOP_PERFORMERS; j++) {
                    NeuralNetwork parent1 = topPerformers.get(i).getNetwork();
                    NeuralNetwork parent2 = topPerformers.get(j).getNetwork();

                    NeuralNetwork offspring = parent1.merge(parent2);
                    offspring.mutate(0.1);
                    newGeneration.add(new NetworkAndScore(offspring, 0));
                    if (newGeneration.size() == AGENTS) {
                        break;
                    }
                }
            }
        }

        return newGeneration;
    }

    public void reset() {
        paddle.setValY(paddle.getInitialY());
        paddle.setValX(paddle.getInitialX());

        ball.setX(paddle.getX() + (paddle.getWidth() / 2) - 10);
        ball.setY(paddle.getY() - 20);
    }

    public int simulateGame(NeuralNetwork nn) {
        int score = 0;
        boolean gameOver = false;
        int distance = 20;
        int numOfRounds = 0;
        int maxNumOfRounds = 10000;

        while (!gameOver && numOfRounds < maxNumOfRounds) {
            //ball.move();
            controller.moveBall();

            double ballX = ball.getX();

            double[] input = { ballX };
            double[] output = nn.guess(input);

            //output[0] = left
            if (output[0] > output[1]) {
                controller.movePaddleLeft();
            } else {
                controller.movePaddleRight();
            }

            if (controller.checkPaddleCollision()) {
                score++;
            }
            if (ball.getY() + ball.getHeight() < view.getHeight()) {
                gameOver = true;
            }

            //this checks if the ball hits the walls or ceiling
            checkBounds();
            numOfRounds++;
        }
        return score;
    }

    /*private boolean checkPaddleCollision(Ball ball, Paddle paddle) {
        boolean collision = false;
        double bottomOfBall = ball.getY() + ball.getHeight();
        double leftOfBall = ball.getX();
        double rightOfBall = ball.getX() + ball.getWidth();

        double topOfPaddle = paddle.getY();
        double leftOfPaddle = paddle.getX();
        double rightOfPaddle = paddle.getX() + paddle.getWidth();

        // Define a small buffer zone (e.g., a few pixels)
        double bufferZone = 1; // Adjust as needed for your game44

        // Check if the ball's bottom is near the top of the paddle and if the ball is horizontally near the paddle
        boolean intersectY = (bottomOfBall + bufferZone) >= topOfPaddle && bottomOfBall <= topOfPaddle + bufferZone;
        boolean intersectX = rightOfBall >= leftOfPaddle - bufferZone && leftOfBall <= rightOfPaddle + bufferZone;

        if (intersectY && intersectX) {
            ball.setAngle(180 - ball.getAngle()); // Reverse vertical direction
            collision = true;
        }
        return collision;
    }*/

    private void checkBounds() {
        int screenWidth = view.getWidth();
        double angle = ball.getAngle();

        //if the ball hits the side wall
        if (ball.getX() <= 0) {
            ball.setAngle(180 - angle);
            ball.setX(0);
        } else if (ball.getX() + ball.getWidth() >= screenWidth) {
            ball.setAngle(-angle);
            ball.setX(screenWidth - ball.getWidth());
        }
    }



}
