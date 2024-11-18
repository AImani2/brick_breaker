//package bwi.brickbreaker;
//
//import basicneuralnetwork.NeuralNetwork;
//
//public class OurNeuralNetwork {
//
//    NeuralNetwork neuralNetwork = new NeuralNetwork(1, 2, 4, 2);
//
//    private Ball ball;  // Reference to the ball
//    private Paddle paddle;  // Reference to the paddle
//
//    // Constructor or method to initialize ball and paddle references
//    public OurNeuralNetwork(Ball ball, Paddle paddle) {
//        this.ball = ball;
//        this.paddle = paddle;
//    }
//
//    public void update() { // Should this whole update happen in the controller class?
//        // Get the angle to the paddle
//        double angle = ball.calculateAngleToPaddle(paddle); // this method is in the ball class.
//
//        // Neural network input
//        double[] input = new double[1];
//        input[0] = angle;  // Use angle as input
//
//        // Get the network's prediction
//        double[] answer = neuralNetwork.guess(input);
//
//        // Use the output of the neural network to control the paddle
//        if (answer[0] > answer[1]) {
//            //paddle.move(); right
//        } else {
//            // Move paddle left
//        }
//
//    }
//
//
//    // values to store after it runs - how many bricks broken.
//    //                               - the time that the game lasts.
//
//    // what is the input?
//    // the angle of the center of the ball to the center of the paddle
//
//    // the output:
//    // Move Right
//    // Move Left
//
//    // create 1000. put them in an array
//    // merge them
//
//    // Make an exact and "independent" copy of a Neural Network
//    //NeuralNetwork nn2 = nn1.copy();
//
//    // Merge the weights and biases of two Neural Networks with a ratio of 50:50
//    //NeuralNetwork merged = nnA.merge(nnB);
//
//    // Merge the weights and biases of two Neural Networks with a custom ratio (here: 20:80)
//    //NeuralNetwork merged = nnA.merge(nnB, 0.2);
//
//// Mutate the weights and biases of a Neural Network with custom probability
////nn.mutate(0.1);
//}

package bwi.brickbreaker;

        import basicneuralnetwork.NeuralNetwork;
        import java.util.ArrayList;
        import java.util.Comparator;
        import java.util.List;

public class OurNeuralNetwork {

    private static final int POPULATION_SIZE = 1000;
    private static final int TOP_PERFORMERS = 10;

    private List<NeuralNetwork> population = new ArrayList<>();
    private Ball ball;
    private Paddle paddle;

    // Constructor to initialize the ball, paddle, and population
    public OurNeuralNetwork(Ball ball, Paddle paddle) {
        this.ball = ball;
        this.paddle = paddle;

        // Initialize population with random neural networks
        for (int i = 0; i < POPULATION_SIZE; i++) {
            NeuralNetwork nn = new NeuralNetwork(1, 2, 4, 2);
            population.add(nn);
        }
    }

    // Method to evaluate the performance of each neural network
    public void evaluatePerformance() {
        // For each neural network, simulate a game and evaluate performance
        List<NetworkPerformance> performanceList = new ArrayList<>();

        for (NeuralNetwork nn : population) {
            double gameDuration = 0.0;
            int bricksBroken = 0;

            // Simulate game with the current neural network
            // Add logic here to calculate how long the game lasts and how many bricks are broken
            // For example, let's assume we have a method simulateGame(nn) that returns these values
            gameDuration = simulateGame(nn);
            bricksBroken = simulateBricksBroken(nn);

            performanceList.add(new NetworkPerformance(nn, gameDuration, bricksBroken));
        }

        // Sort by performance (longer game duration and more bricks broken are better)
        performanceList.sort(Comparator.comparingDouble(NetworkPerformance::getScore).reversed());

        // Get the top performers
        List<NeuralNetwork> topPerformers = new ArrayList<>();
        for (int i = 0; i < TOP_PERFORMERS; i++) {
            topPerformers.add(performanceList.get(i).getNeuralNetwork());
        }

        // Create new generation by merging top performers
        List<NeuralNetwork> newGeneration = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            NeuralNetwork parent1 = topPerformers.get(i % TOP_PERFORMERS);
            NeuralNetwork parent2 = topPerformers.get((i + 1) % TOP_PERFORMERS);

            // Merge two parent networks to create a new network
            NeuralNetwork offspring = parent1.merge(parent2);  // Custom merge logic
            newGeneration.add(offspring);
        }

        // Replace the old population with the new generation
        population = newGeneration;
    }

    // Simulate a game and return the game duration (this is just a placeholder, implement the actual game logic)
    private double simulateGame(NeuralNetwork nn) {
        // Implement the game simulation using the given neural network
        return Math.random() * 100;  // Placeholder logic
    }

    // Simulate the number of bricks broken (this is just a placeholder, implement the actual game logic)
    private int simulateBricksBroken(NeuralNetwork nn) {
        // Implement the bricks broken simulation using the given neural network
        return (int) (Math.random() * 50);  // Placeholder logic
    }

    // Nested class to hold neural network and its performance
    private static class NetworkPerformance {
        private NeuralNetwork neuralNetwork;
        private double gameDuration;
        private int bricksBroken;

        public NetworkPerformance(NeuralNetwork neuralNetwork, double gameDuration, int bricksBroken) {
            this.neuralNetwork = neuralNetwork;
            this.gameDuration = gameDuration;
            this.bricksBroken = bricksBroken;
        }

        public NeuralNetwork getNeuralNetwork() {
            return neuralNetwork;
        }

        public double getScore() {
            return gameDuration + bricksBroken;  // Combine performance metrics (you can adjust the weights)
        }
    }

    // The rest of your neural network-related code...

    public void update() {
        // Get the angle to the paddle
        double angle = ball.calculateAngleToPaddle(paddle); // this method is in the ball class.

        // Neural network input
        double[] input = new double[1];
        input[0] = angle;  // Use angle as input

        // Get the network's prediction
        double[] answer = neuralNetwork.guess(input);

        // Use the output of the neural network to control the paddle
        if (answer[0] > answer[1]) {
            paddle.moveRight();
        } else {
            paddle.moveLeft();
        }
    }
}
