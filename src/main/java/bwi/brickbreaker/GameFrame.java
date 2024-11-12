package bwi.brickbreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class GameFrame extends JFrame {

    private final Ball ball;
    private final Paddle paddle;
    private final int boardWidth = 800;
    private final int boardHeight = 600;
    private ArrayList<Brick> bricks = new ArrayList<>();
    private BoardComponent view = null;


    public GameFrame() {
        setSize(boardWidth, boardHeight);
        setTitle("Brick Breaker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //create the ball and paddle
        paddle = new Paddle(350, 500, 20, 100, Color.MAGENTA);
        int x = (int) paddle.getX() + ((int) paddle.getWidth() / 2) - 10;
        int y = (int) paddle.getY() - 20;
        ball = new Ball(45, .5, x, y, 20, Color.CYAN);

        view = new BoardComponent(ball, paddle, bricks);
        bricks = view.layBricksOnGrid();

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



}
