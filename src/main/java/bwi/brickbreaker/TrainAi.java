package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class TrainAi
{

    public static void main(String[] args) {
        Random rand = new Random();
        List<NetworkAndScore> population = new ArrayList<>();
        int agents = 1000;
        int topTen = 10;
        Paddle paddle = new Paddle(350, 500, 20, 100, Color.MAGENTA);
        int x = (int) paddle.getX() + ((int) paddle.getWidth() / 2) - 10;
        int y = (int) paddle.getY() - 20;
        Ball ball = new Ball(x, y, 20, rand.nextInt(175), -1, Color.CYAN);
        int viewWidth = 800;
        int viewHeight = 600;
        int brickWidth = 100;
        int brickHeight = 20;
        int generations = 100;
        List<NetworkAndScore> topPerformers = new ArrayList<>();
        BrickFactory brickFactory = new BrickFactory(viewWidth, viewHeight, brickWidth, brickHeight);



        // Creates the first arrayList of agents
        for (int i = 0; i < agents; i++) {
            NeuralNetwork nn = new NeuralNetwork(4, 2, 4, 2);
            NetworkAndScore networkAndScore = new NetworkAndScore(nn, 0);
            population.add(networkAndScore);
        }

        for (int i = 0; i < generations; i++) {

            //Have the neural networks play the game
            for (int j = 0; j < population.size(); j++) {
                Simulation simulation = new Simulation(population.get(j).getNetwork(),
                        ball, paddle, viewWidth, viewHeight, brickFactory);
                simulation.simulate();
                population.get(j).setScore(simulation.getScore());
                simulation.reset();
            }

            population.sort(Comparator.comparingInt(NetworkAndScore::getScore).reversed());

            topPerformers = new ArrayList<>(population.subList(0, topTen));

            population.clear();

            while (population.size() < agents) {
                for (int j = 0; j < topTen; j++) {
                    for (int k = 0; k < topTen; k++) {
                        NeuralNetwork parent1 = topPerformers.get(k).getNetwork();
                        NeuralNetwork parent2 = topPerformers.get(j).getNetwork();

                        NeuralNetwork offspring = parent1.merge(parent2);
                        offspring.mutate(0.1);
                        population.add(new NetworkAndScore(offspring, 0));
                        if (population.size() == agents) {
                            break;
                        }
                    }
                }
            }
        }

        topPerformers.get(0).getNetwork().writeToFile("nn_data");

    }


}

