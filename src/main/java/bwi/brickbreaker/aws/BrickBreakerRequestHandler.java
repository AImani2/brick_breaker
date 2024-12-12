package bwi.brickbreaker.aws;

import basicneuralnetwork.NeuralNetwork;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStream;


public class BrickBreakerRequestHandler
        implements RequestHandler<BrickBreakerRequestHandler.BrickBreakerRequest,
        BrickBreakerRequestHandler.BrickBreakerResponse>
{
    private final S3Client s3Client;

    public BrickBreakerRequestHandler() {
        // Initialize the s3 client
        s3Client = S3Client.create();
    }

    @Override
    public BrickBreakerResponse handleRequest(BrickBreakerRequest request, Context context)
    {
        try
        {
            double guess[] = new double[4];
            guess[0] = request.ballX(); // x ball
            guess[1] = request.paddleX(); // x paddle
            guess[2] = request.brickX(); // x brick
            guess[3] = request.brickY(); // y brick

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket("bral.bbnn")
                    .key("nn_data.json")
                    .build();
            InputStream in = s3Client.getObject(getObjectRequest);
            NeuralNetwork network = NeuralNetwork.readFromFile(in);
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
    record BrickBreakerRequest(
        double ballX,
        double paddleX,
        double brickX,
        double brickY) {
    }

    record BrickBreakerResponse(
        // want to see what the difference in these values are.
        double right,
        double left) {
        public String moveString()
        {
            if (right > left)
            {
                return "RIGHT";
            } else {
                return "LEFT";
            }
        }
    }

}
