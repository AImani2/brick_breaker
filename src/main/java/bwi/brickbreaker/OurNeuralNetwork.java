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
    private static final int POPULATION_SIZE = 100;

    //    private static final int POPULATION_SIZE = 1000;
    private static final int TOP_PERFORMERS = 10;

    private List<NeuralNetwork> population = new ArrayList<>();
    private Ball ball;
    private Paddle paddle;
    private Controller controller;
    private ArrayList<Brick> bricks;
    private BoardComponent view;

    // Constructor to initialize the ball, paddle, and population
    public OurNeuralNetwork(Ball ball, Paddle paddle, Controller controller, ArrayList<Brick> bricks, BoardComponent view) {
        this.ball = ball;
        this.paddle = paddle;
        this.controller = controller;
        this.bricks = bricks;
        this.view = view;

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

            paddle.setValY(paddle.getInitialY());
            paddle.setValX(paddle.getInitialX());

            ball.setX(paddle.getX() + (paddle.getWidth() / 2) - 10);
            ball.setY(paddle.getY() - 20);


            // Simulate game with the current neural network
            // Add logic here to calculate how long the game lasts and how many bricks are broken
            // For example, let's assume we have a method simulateGame(nn) that returns these values
            gameDuration = simulateGame(nn);
//            System.out.println("Game duration : " + gameDuration);
            //bricksBroken = simulateBricksBroken(nn);



            performanceList.add(new NetworkPerformance(nn, gameDuration, bricksBroken));
            System.out.println("added a new network: " + gameDuration);
        }

        // Sort by performance (longer game duration and more bricks broken are better)
        performanceList.sort(Comparator.comparingDouble(NetworkPerformance::getScore).reversed());

        // Get the top performers
        List<NeuralNetwork> topPerformers = new ArrayList<>();
        for (int i = 0; i < TOP_PERFORMERS; i++) {
            topPerformers.add(performanceList.get(i).getNeuralNetwork());
            System.out.println("New neural network: Played for " + performanceList.get(i).getScore());
        }

        // Create new generation by merging top performers
        List<NeuralNetwork> newGeneration = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            NeuralNetwork parent1 = topPerformers.get(i % TOP_PERFORMERS);
            NeuralNetwork parent2 = topPerformers.get((i + 1) % TOP_PERFORMERS);

            // Merge two parent networks to create a new network
            NeuralNetwork offspring = parent1.merge(parent2);  // Custom merge logic

            // Mutate the offspring network (10% mutation rate)
            offspring.mutate(0.1);

            // Add the offspring to the new generation
            newGeneration.add(offspring);
        }
        // Replace the old population with the new generation
        population = newGeneration;
    }


    // Simulate a game and return the game duration (this is just a placeholder, implement the actual game logic)
    private double simulateGame(NeuralNetwork nn) {
        // Implement the game simulation using the given neural network

        double gameDuration = 0.0; // Time the game lasts in seconds
        int updatesPerSecond = 60; // Assume 60 updates per second
        int totalUpdates = 0; // Track updates to calculate time
        int totalBricks = 0;

        // Create a temporary ball, paddle, and bricks to simulate the game
        Ball simBall = new Ball(ball.getAngle(), ball.getVelocity(), ball.getX(), ball.getY(), ball.getWidth(), ball.getColor());
        System.out.println("ball angle: " + simBall.getAngle() + " Velocity: " + simBall.getVelocity());
        Paddle simPaddle = new Paddle(paddle.getX(), paddle.getY(), paddle.getHeight(), paddle.getWidth(), paddle.getColor());
        ArrayList<Brick> simBricks = new ArrayList<>();
        for (Brick brick : bricks) {
            simBricks.add(new Brick(brick.getBroken(), brick.getHeight(), brick.getWidth(), brick.getX(), brick.getY(), brick.getColor()));
        }

        boolean gameOver = false;

        long startTime = System.currentTimeMillis();

        while (!gameOver) {
            // Simulate ball movement
            simBall.move();

            // Simulate paddle movement based on neural network decision
            double angle = simBall.calculateAngleToPaddle(simPaddle);
//            System.out.println("Angle measure: " + angle);
            double[] input = { angle };
            double[] output = nn.guess(input);

//            System.out.println("Output 0: " + output[0] + " Output 1: " + output[1]);

            if (output[0] > output[1]) {
                if (simPaddle.getX() + 10 + paddle.getWidth() < view.getWidth()) {
                    simPaddle.move(paddle.getX() + 10, 800); // Move paddle right
                    //                System.out.println("moved right");
                }
            } else {
                if (simPaddle.getX() > 0) {
                    simPaddle.move(paddle.getX() - 10, 800); // Move paddle left
//                System.out.println("moved left");

                }
            }

            // Check collisions
            checkPaddleCollision(simBall, simPaddle);
            totalBricks += checkBrickCollision(simBall, simBricks);

            // Check if game is over
            if (simBall.getY() > 600 || allBricksBroken(simBricks)) {
                gameOver = true;
            }

        }
        long endTime = System.currentTimeMillis();

//        System.out.println("Duration of game simulation: " + (endTime - startTime));
        System.out.println("Total bricks hit: " + totalBricks + " total time: " + (endTime - startTime));
        return (endTime - startTime) + totalBricks;
    }

    // Helper to check if all bricks are broken
    private boolean allBricksBroken(ArrayList<Brick> bricks)
    {
        boolean allBroken = true;
        for (Brick brick : bricks) {
            if (!brick.getBroken()) {
                allBroken = false;
            }
        }
        return allBroken;
    }

    private void checkPaddleCollision(Ball ball, Paddle paddle) {
        double bottomOfBall = ball.getY() + ball.getHeight();
        double leftOfBall = ball.getX();
        double rightOfBall = ball.getX() + ball.getWidth();

        double topOfPaddle = paddle.getY();
        double leftOfPaddle = paddle.getX();
        double rightOfPaddle = paddle.getX() + paddle.getWidth();

        if (bottomOfBall >= topOfPaddle && rightOfBall >= leftOfPaddle && leftOfBall <= rightOfPaddle) {
            ball.setAngle(-ball.getAngle()); // Reverse vertical direction
            System.out.println("hit the paddle");
        }
    }

    // Check brick collision and return the count of bricks broken
    private int checkBrickCollision(Ball ball, ArrayList<Brick> bricks) {
        int count = 0;
        for (Brick brick : bricks) {
            if (!brick.getBroken() && ball.getBounds2D().intersects(brick.getBounds())) {
                brick.setBroken(true);
                ball.setAngle(-ball.getAngle()); // Reverse vertical direction on collision
                System.out.println("Hit a brick new angle: " + ball.getAngle());
                count++;
            }
        }

//        for (int i = 0; i < bricks.size(); i++) {
//            System.out.println("Brick " + i + " Broken? " + bricks.get(i).getBroken());
//        }
//        System.out.println("Hit " + count + " bricks");
        return count;
    }


    // Simulate the number of bricks broken (this is just a placeholder, implement the actual game logic)

    // not sure if we need this yet? (so I commented it out)
    // because should we just first make the paddle's goal only
    // to catch the ball and the aiming at bricks part comes later?
    /*private int simulateBricksBroken(NeuralNetwork nn) {
        // Implement the bricks broken simulation using the given neural network
        int bricksBroken = 0;

        // Create a temporary ball, paddle, and bricks to simulate the game
        Ball simBall = new Ball(ball.getAngle(), ball.getVelocity(), ball.getX(), ball.getY(), ball.getWidth(), ball.getColor());
        Paddle simPaddle = new Paddle(paddle.getX(), paddle.getY(), paddle.getHeight(), paddle.getWidth(), paddle.getColor());
        ArrayList<Brick> simBricks = new ArrayList<>();
        for (Brick brick : bricks) {
            simBricks.add(new Brick(brick.getBroken(), brick.getHeight(), brick.getWidth(), brick.getX(), brick.getY(), brick.getColor()));
        }

        boolean gameOver = false;

        while (!gameOver) {
            // Simulate ball movement
            simBall.move();

            // Simulate paddle movement based on neural network decision
            double angle = simBall.calculateAngleToPaddle(simPaddle);
            double[] input = { angle };
            double[] output = nn.guess(input);

            if (output[0] > output[1]) {
                simPaddle.move(5, 800); // Move paddle right
            } else {
                simPaddle.move(-5, 800); // Move paddle left
            }

            // Check collisions
            bricksBroken += checkBrickCollision(simBall, simBricks);

            // Check if game is over
            if (simBall.getY() > 600 || allBricksBroken(simBricks)) {
                gameOver = true;
            }
        }

        return bricksBroken;
    }*/

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
            return gameDuration * 0.7 + bricksBroken * 0.3;  // Combine performance metrics (you can adjust the weights)
        }
    }

    // The rest of your neural network-related code...

//    public void update() {
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
//            controller.movePaddleRight();
//            //paddle.moveRight();
//        } else {
//            controller.movePaddleLeft();
//            //paddle.moveLeft();
//        }
//    }
}
