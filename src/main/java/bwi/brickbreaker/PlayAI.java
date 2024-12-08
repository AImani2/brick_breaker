package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

import java.io.IOException;

public class PlayAI
{

    public static void main(String[] args)
    {
        //read from file here

        NeuralNetwork topNetwork = null;

        try
        {
            topNetwork = NeuralNetwork.readFromFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        GameFrame gameFrame = new GameFrame(topNetwork);
        gameFrame.setVisible(true);
    }

}
