package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TrainAI {
    private static List<NetworkAndScore> population = new ArrayList<>();
    private int AGENTS = 1000;
    private int TOP_PERFORMERS = 10;
    private static Paddle paddle = new Paddle(350, 500, 20, 100, Color.MAGENTA);
    private static int x = (int) paddle.getX() + ((int) paddle.getWidth() / 2) - 10;
    private static int y = (int) paddle.getY() - 20;
    private static Ball ball = new Ball(45, x, y, 20,1,1, Color.CYAN);
    private static int viewWidth = 400;
    private static int viewHeight = 400;
    private static int generations = 5;
    private static List<NetworkAndScore> topPerformers;
    private Simulation simulation;


    Simulation simulation;
//    ArrayList<Brick> bricks = new ArrayList<>();
//
//    bricks = view.layBricksOnGrid();
//
//    BrickBreakerModel model = new BrickBreakerModel(ball, bricks);


    public TrainAI() {
        for (int i = 0; i < AGENTS; i++) {
            NeuralNetwork nn = new NeuralNetwork(2, 2, 4, 2);
            NetworkAndScore networkAndScore = new NetworkAndScore(nn, 0);
            population.add(networkAndScore);
        }
    }

    public static void main(String[] args) {


        int generations = 5;

        List<NetworkAndScore> topPerformers = new ArrayList<>();
//        NeuralNetworkTakeTwo neuralNetwork = new NeuralNetworkTakeTwo(ball, paddle, simulation);

        for (int i = 0; i < generations; i++) {

            //TODO make a variable for the 10000 agents
            for (int j = 0; j < population.size(); j++) {
                Simulation simulation = new Simulation(population.get(i).getNetwork(), ball, paddle, viewWidth, viewHeight);
                simulation.simulate();
                population.get(i).setScore(simulation.getScore());
            }

            //System.out.println("Generation: " + (i + 1));
            topPerformers = neuralNetworkTakeTwo.evaluatePerformance();
            //System.out.println("Finished generation " + (i + 1));
        }

        topPerformers.get(0).getNetwork().writeToFile();

    }

    public List<NetworkAndScore> evaluatePerformance() {
        for (NetworkAndScore nas : population) {
            Simulation simulation = new Simulation(nas.getNetwork(), ball, paddle, viewWidth, viewHeight);
            simulation.reset();
            int score = simulateGame(nas.getNetwork());
            nas.setScore(score);
        }

        population.sort(Comparator.comparingInt(NetworkAndScore::getScore).reversed());

        List<NetworkAndScore> topPerformers = new ArrayList<>(population.subList(0, TOP_PERFORMERS));

        population = regenerate(topPerformers);

        return topPerformers;
    }

    public int simulateGame(NeuralNetwork nn) {
        int score = 0;
        boolean gameOver = false;
        int maxNumOfRounds = 10000;

        for (int i = 0; i < maxNumOfRounds; i++) {
            simulation.moveBall();

            double centerOfBall = ball.getCenterX();
            double centerOfPaddle = paddle.getCenterX();

            double[] input = { centerOfBall, centerOfPaddle };
            double[] output = nn.guess(input);

            if (output[0] > output[1]) {
                simulation.movePaddleLeft();
            } else {
                simulation.movePaddleRight();
            }

            if (!simulation.advance()) {
                break;
            }

            score += simulation.getScore();
        }

        return score;
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

        return  newGeneration;
    }



}

