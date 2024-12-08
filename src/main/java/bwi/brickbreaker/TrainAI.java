package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TrainAI {

    public static void main(String[] args) {

        List<NetworkAndScore> population = new ArrayList<>();
        int AGENTS = 1000;
        int TOP_PERFORMERS = 10;
        Paddle paddle = new Paddle(350, 500, 20, 100, Color.MAGENTA);
        int x = (int) paddle.getX() + ((int) paddle.getWidth() / 2) - 10;
        int y = (int) paddle.getY() - 20;
        Ball ball = new Ball(x, y, 20,2.5,-2, Color.CYAN);
        int viewWidth = 400;
        int viewHeight = 700;
        int generations = 5;
        List<NetworkAndScore> topPerformers = new ArrayList<>();



        // Creates the first arrayList of agents
        for (int i = 0; i < AGENTS; i++) {
            NeuralNetwork nn = new NeuralNetwork(2, 2, 4, 2);
            NetworkAndScore networkAndScore = new NetworkAndScore(nn, 0);
            population.add(networkAndScore);
        }

        for (int i = 0; i < generations; i++) {
            System.out.println("Generation: " + (i + 1));

            //Have the neural networks play the game
            for (int j = 0; j < population.size(); j++) {
                Simulation simulation = new Simulation(population.get(i).getNetwork(), ball, paddle, viewWidth, viewHeight);
                simulation.simulate();
                population.get(i).setScore(simulation.getScore());
                System.out.println("Score: " + simulation.getScore());
                simulation.reset();
            }

            population.sort(Comparator.comparingInt(NetworkAndScore::getScore).reversed());

            topPerformers = new ArrayList<>(population.subList(0, TOP_PERFORMERS));

            population.clear();

            while (population.size() < AGENTS) {
                for (int j = 0; j < TOP_PERFORMERS; j++) {
                    for (int k = 0; k < TOP_PERFORMERS; k++) {
                        NeuralNetwork parent1 = topPerformers.get(i).getNetwork();
                        NeuralNetwork parent2 = topPerformers.get(j).getNetwork();

                        NeuralNetwork offspring = parent1.merge(parent2);
                        offspring.mutate(0.1);
                        population.add(new NetworkAndScore(offspring, 0));
                        if (population.size() == AGENTS) {
                            break;
                        }
                    }
                }
            }
        }

        topPerformers.get(0).getNetwork().writeToFile();
        System.out.println("Top score: " + topPerformers.get(0).getScore());

    }


}

