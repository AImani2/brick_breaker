package bwi.brickbreaker.aws;

import basicneuralnetwork.NeuralNetwork;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;


public class BrickBreakerRequestHandler implements RequestHandler<String, String>
{
    @Override
    public String handleRequest(String input, Context context)
    {
        try
        {
            NeuralNetwork network = NeuralNetwork.readFromFile("nn_data.json");
            double guess[] = new double[4];
            guess[0] = 10; // x ball
            guess[1] = 20; // x paddle
            guess[2] = 40; // x brick
            guess[3] = 80; // y brick
            double[] output = network.guess(guess);
            if (output[0] > output[1])
            {
                return "RIGHT";
            }
            else
            {
                return "LEFT";
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}

