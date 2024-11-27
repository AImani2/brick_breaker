package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

public class NetworkAndScore {
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
