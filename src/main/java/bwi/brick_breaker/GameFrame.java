package bwi.brick_breaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameFrame extends JFrame {

    private final Ball ball;
    private final Paddle paddle;
    private final int boardWidth = 800;
    private final int boardHeight = 600;
    private ArrayList<Brick> bricks = new ArrayList<>();
    private final Panel panel;
    private BoardComponent view = null;


    public GameFrame() {
        this.panel = new Panel();

        setSize(boardWidth, boardHeight);
        setTitle("Brick Breaker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //create the ball and paddle
        paddle = new Paddle(350, 550, 20, 100, Color.MAGENTA);
        int x = (int)paddle.getX() + ((int)paddle.getWidth() / 2) - 10;
        int y = (int) paddle.getY() - 20;
        ball = new Ball(45, 3, x, y, 20, Color.CYAN);

        BrickBreakerModel model = new BrickBreakerModel(ball, bricks, panel);
        Controller controller = new Controller(ball, paddle, bricks, model, view);

        view = new BoardComponent(ball, paddle, bricks, controller);
        add(view);
        view.setPreferredSize(new Dimension(boardWidth, boardHeight - 100));

        bricks = view.layBricksOnGrid();
        controller.setBricks(bricks);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.startGame();
            }
        });
        add(startButton, BorderLayout.SOUTH);
    }



}
