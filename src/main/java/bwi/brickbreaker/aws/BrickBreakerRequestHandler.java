package bwi.brickbreaker.aws;

import basicneuralnetwork.NeuralNetwork;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;
import java.io.InputStream;


public class BrickBreakerRequestHandler
        implements RequestHandler<BrickBreakerRequestHandler.BrickBreakerRequest, BrickBreakerRequestHandler.BrickBreakerResponse>
{
    @Override
    public BrickBreakerResponse handleRequest(BrickBreakerRequest request, Context context)
    {
        try
        {
            InputStream in = getClass().getClassLoader().getResourceAsStream("trained.json");
            NeuralNetwork network = NeuralNetwork.readFromFile("nn_data.json");
            double guess[] = new double[4];
            guess[0] = request.xBall(); // x ball
            guess[1] = request.xPaddle(); // x paddle
            guess[2] = request.xBrick(); // x brick
            guess[3] = request.yBrick(); // y brick
            double[] output = network.guess(guess);

            BrickBreakerResponse response = new BrickBreakerResponse(
                    output[0], output[1]
            );
            return response;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }


    // record has a contructor, all varibales are private and final and they all have getters
    // a quicker way of creating a class with that
    // it creates some methods for you: toString, ...
    // only for classes that get created and don't get changed
    // POJOs: Plain Old Java Object
    record BrickBreakerRequest
    (
        double xBall,
        double xPaddle,
        double xBrick,
        double yBrick
    ) {

    }

    record BrickBreakerResponse
    (
        // want to see what the difference in these values are.
        double right,
        double left
    ) {
        public String moveString()
        {
            if (right > left)
            {
                return "RIGHT";
            }
            else
            {
                return "LEFT";
            }
        }
    }

}

