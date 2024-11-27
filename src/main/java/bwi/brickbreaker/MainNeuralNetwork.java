package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

import java.io.IOException;

public class MainNeuralNetwork {

    public static void main(String[] args)
    {
        //read from file here

        NeuralNetwork topNetwork = null;

        try
        {
            topNetwork = NeuralNetwork.readFromFile("nn_data.json");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        GameFrame gameFrame = new GameFrame(topNetwork);
        gameFrame.setVisible(true);
    }

}
