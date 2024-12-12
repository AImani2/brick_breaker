package bwi.brickbreaker;

import basicneuralnetwork.NeuralNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class GameFrame extends JFrame {

    private Ball ball;
    private Paddle paddle;
    private final int boardWidth = 800;
    private final int boardHeight = 600;
    private ArrayList<Brick> bricks = new ArrayList<>();
    private BoardComponent view = null;
    private final int brickHeight = 40;
    private final int brickWidth = 100;
    private BrickFactory brickFactory = new BrickFactory(boardWidth, boardHeight, brickWidth, brickHeight);

    private NeuralNetwork neuralNetwork;


    public GameFrame() {
        this(null);

        setSize(boardWidth, boardHeight);
        setTitle("Brick Breaker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //create the ball and paddle
        paddle = new Paddle(350, 500, 20, 100, Color.MAGENTA);
        int x = (int) paddle.getX() + ((int) paddle.getWidth() / 2) - 10;
        int y = (int) paddle.getY() - 20;
        Ball ball = new Ball(x, y, 20, 2.5, -2, Color.CYAN);

        view = new BoardComponent(ball, paddle, bricks);

        BrickBreakerModel model = new BrickBreakerModel(ball, bricks);
        Controller controller = new Controller(ball, paddle, bricks, model, view);

        controller.setBricks(bricks);

        add(view);
        view.setPreferredSize(new Dimension(boardWidth, boardHeight - 100));

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startGame();
            }
        });
        add(startButton, BorderLayout.SOUTH);

    }

    public GameFrame(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;

        setSize(boardWidth, boardHeight);
        setTitle("Brick Breaker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //create the ball and paddle
        paddle = new Paddle(350, 500, 20, 100, Color.MAGENTA);
        int x = (int) paddle.getX() + ((int) paddle.getWidth() / 2) - 10;
        int y = (int) paddle.getY() - 20;
        Ball ball = new Ball(x, y, 20, 2.5, -2, Color.CYAN);


        if (neuralNetwork != null) {
            Simulation simulation = new Simulation(neuralNetwork, ball, paddle, 800, 600, brickFactory);


            JLabel scoreLabel = new JLabel("Score: \n" + simulation.getScore());
            add(scoreLabel, BorderLayout.NORTH);
            view = new BoardComponent(simulation);
            add(view);
            view.setPreferredSize(new Dimension(boardWidth, boardHeight - 100));


            Timer timer1 = new Timer(1, e -> {
                if (simulation.advance()) {
                    scoreLabel.setText("Score:\n" + simulation.getScore());
                    view.repaint();
                } else {
                    String message = "Game over!\nDo you want to start again?";
                    int start = JOptionPane.showConfirmDialog(view, message, "Game Over", JOptionPane.YES_NO_OPTION);
                    if (start == JOptionPane.YES_NO_OPTION) {
                        simulation.reset();
                        view.repaint();
                    } else {
                        SwingUtilities.getWindowAncestor(view).dispose();
                        ((Timer) e.getSource()).stop();
                    }
                }
            });
            timer1.start();

            /*
            Timer timer = new Timer(15, e -> {
                simulation.advance();
                scoreLabel.setText("Score: \n" + simulation.getScore());
                view.repaint();
            });

            timer.start();
             */
        }
    }


}
