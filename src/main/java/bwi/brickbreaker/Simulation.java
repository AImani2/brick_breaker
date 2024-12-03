package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

public class Simulation
{
    NeuralNetwork nn;
    Ball ball;
    Paddle paddle;
    int width;
    int height;

    public Simulation(NeuralNetwork nn, Ball ball, Paddle paddle, int width, int height)
    {
        this.nn = nn;
        this.ball = ball;
        this.paddle = paddle;
        this.width = width;
        this.height = height;
    }

    public void simulate()
    {

    }
    // moves the ball
    // moves the paddle
    // checks for collisions with walls
    // checks for collisions with top
    // checks for collisions with bricks (eventually)
    // checks for collisions with paddle (increases score)
    // return true is the ball is still above the floor, otherwise false
    //boolean advance()
    //int getScore()
    //how many times the ball hit the paddle
}
