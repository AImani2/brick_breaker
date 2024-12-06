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
    private Simulation simulation;
    private int viewHeight = 400;
    private int viewWidth = 400;

    public NeuralNetworkTakeTwo(Ball ball, Paddle paddle, Simulation simulation) {
        this.ball = ball;
        this.paddle = paddle;
        this.simulation = simulation;

        // Initialize population with random neural networks
        for (int i = 0; i < AGENTS; i++) {
            NeuralNetwork nn = new NeuralNetwork(2, 2, 4, 2);
            NetworkAndScore networkAndScore = new NetworkAndScore(nn, 0);
            population.add(networkAndScore);
        }
    }


    public List<NetworkAndScore> evaluatePerformance() {
        for (NetworkAndScore nas : population) {
            reset();
            int score = simulateGame(nas.getNetwork());
            nas.setScore(score);
        }

        population.sort(Comparator.comparingInt(NetworkAndScore::getScore).reversed());

        List<NetworkAndScore> topPerformers = new ArrayList<>(population.subList(0, TOP_PERFORMERS));

        population = regenerate(topPerformers);

        return topPerformers;

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
        paddle.setValY((int) paddle.getInitialY());
        paddle.setValX((int) paddle.getInitialX());

        ball.setAngle(45);
        ball.setX(paddle.getX() + (paddle.getWidth() / 2) - 10);
        ball.setY(paddle.getY() - 20);
    }

    public int simulateGame(NeuralNetwork nn) {
        int score = 0;
        boolean gameOver = false;
        int maxNumOfRounds = 10000;


        for (int i = 0; i < maxNumOfRounds; i++) {
            simulation.moveBall();
            //System.out.println("New ball position: " + ball.getX() + ", " + ball.getY());

            double centerOfBall = ball.getCenterX();
            double centerOfPaddle = paddle.getCenterX();

            double[] input = { centerOfBall, centerOfPaddle };
            double[] output = nn.guess(input);

            //output[0] = left
            if (output[0] > output[1]) {
                simulation.movePaddleLeft();
                //System.out.println("moved to the left, new position: " + paddle.getX() + " Y: " + paddle.getY());
            } else {
                simulation.movePaddleRight();
                //System.out.println("moved to the right, new position: " + paddle.getX() + " Y: " + paddle.getY());
            }


            if (checkPaddleCollision(ball, paddle)) {
                score++;
                //System.out.println("Increase score: " + score);
            }

            if (ball.getY() + ball.getHeight() > viewHeight) {
                gameOver = true;
                //System.out.println("Game over!! " + view.getHeight() + ", " + (ball.getY() + ball.getHeight()));
            }



            //this checks if the ball hits the walls or ceiling
            checkBounds();
            checkCeiling();

            /*System.out.println("Ball position: " + ball.getX() + " " + ball.getY()
                    + " at the round: " + i + " Score: " + score + " Angle: " + ball.getAngle());
            System.out.println("Paddle position: " + paddle.getX() + " " + paddle.getY());*/
            if (gameOver) {
                break;
            }
        }
        return score;
    }


    private boolean checkPaddleCollision(Ball ball, Paddle paddle) {
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
            //ball.setAngle(180 - ball.getAngle()); // Reverse vertical direction
            //ball.setAngle(-ball.getAngle());

            double newAngle = 180 - ball.getAngle();
            ball.setAngle(normalizeAngle(newAngle));

            ball.setY(paddle.getY() - ball.getHeight() - 1);

            collision = true;
        } else if (bottomOfBall > viewHeight) {
            //System.out.println("ball fell, bottom of ball: " + bottomOfBall + " height of view: " + view.getHeight());
        }
        return collision;
    }

    private double normalizeAngle(double angle) {
        if (angle < 0) {
            angle += 180;
        } else if (angle > 180) {
            angle -= 180;
        }
        return angle;
    }

    private void checkBounds() {
        //System.out.println("screenWidth: " + viewWidth);
        double angle = ball.getAngle();

        //if the ball hits the side wall
        if (ball.getX() <= 0) {
            ball.setAngle(180 - angle);
            ball.setX(0);
        } else if (ball.getX() + ball.getWidth() >= viewWidth) {
            ball.setAngle(180 - angle);
            ball.setX(viewWidth - ball.getWidth());
            //System.out.println("ball wants to go out of bounds, angle: " + angle);
        }
    }

    private void checkCeiling() {
        if (ball.getY() <= 0) {
            ball.setAngle(-ball.getAngle());
        }
    }



}
