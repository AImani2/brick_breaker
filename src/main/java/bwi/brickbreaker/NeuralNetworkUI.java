package bwi.brickbreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NeuralNetworkUI extends JFrame {
    private NeuralNetworkTakeTwo gameSimulation;
    private GamePanel gamePanel;
    private JButton startButton, stopButton, resetButton;
    private JTable performanceTable;
    private Timer gameTimer;

    public NeuralNetworkUI(NeuralNetworkTakeTwo simulation) {
        this.gameSimulation = simulation;

        // Set up the main JFrame
        setTitle("Neural Network BrickBreaker");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Game visualization panel
        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel();
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        resetButton = new JButton("Reset");

        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(resetButton);

        add(controlPanel, BorderLayout.SOUTH);

        // Performance panel
        String[] columnNames = {"Rank", "Score"};
        Object[][] data = new Object[10][2]; // Display top 10 performers
        performanceTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(performanceTable);

        JPanel performancePanel = new JPanel(new BorderLayout());
        performancePanel.add(new JLabel("Top Performers"), BorderLayout.NORTH);
        performancePanel.add(scrollPane, BorderLayout.CENTER);

        add(performancePanel, BorderLayout.EAST);

        // Button actions
        startButton.addActionListener(new StartAction());
        stopButton.addActionListener(new StopAction());
        resetButton.addActionListener(new ResetAction());

        // Timer for game updates
        gameTimer = new Timer(16, e -> updateGame());
    }

    private void updateGame() {
        // Update game state and repaint the game panel
        gameSimulation.evaluatePerformance(); // Simulate a single step
        gamePanel.repaint(); // Re-render the game visualization
    }

    private class StartAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameTimer.start();
        }
    }

    private class StopAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameTimer.stop();
        }
    }

    private class ResetAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameTimer.stop();
            gameSimulation.reset(); // Reset the simulation
            gamePanel.repaint();
        }
    }

    // Custom JPanel to visualize the game
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Draw paddle
            g2d.setColor(Color.BLUE);
            g2d.fillRect((int) gameSimulation.paddle.getX(),
                    (int) gameSimulation.paddle.getY(),
                    (int) gameSimulation.paddle.getWidth(),
                    (int) gameSimulation.paddle.getHeight());

            // Draw ball
            g2d.setColor(Color.RED);
            g2d.fillOval((int) gameSimulation.ball.getX(),
                    (int) gameSimulation.ball.getY(),
                    (int) gameSimulation.ball.getWidth(),
                    (int) gameSimulation.ball.getHeight());

            // Draw bricks
            g2d.setColor(Color.GREEN);
            for (Brick brick : gameSimulation.bricks) {
                g2d.fillRect((int) brick.getX(),
                        (int) brick.getY(),
                        (int) brick.getWidth(),
                        (int) brick.getHeight());
            }
        }
    }

    public static void main(String[] args) {
        // Example initialization with mock objects

        Paddle paddle = new Paddle(350, 500, 20, 100, Color.MAGENTA);
        int x = (int) paddle.getX() + ((int) paddle.getWidth() / 2) - 10;
        int y = (int) paddle.getY() - 20;
        Ball ball = new Ball(45, 5, x, y, 20, Color.CYAN);

        ArrayList<Brick> bricks = new ArrayList<>();
        BoardComponent view = new BoardComponent(ball, paddle, bricks);
        view.setSize(800, 5000);
        bricks = view.layBricksOnGrid();

        BrickBreakerModel model = new BrickBreakerModel(ball, bricks);
        Controller controller = new Controller(ball, paddle, bricks, model, view);

        NeuralNetworkTakeTwo simulation = new NeuralNetworkTakeTwo(ball, paddle, controller, bricks, view);
        SwingUtilities.invokeLater(() -> {
            NeuralNetworkUI ui = new NeuralNetworkUI(simulation);
            ui.setVisible(true);

            // Add a ComponentListener to ensure the view is fully initialized
            ui.gamePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    int viewWidth = ui.gamePanel.getWidth();
                    int viewHeight = ui.gamePanel.getHeight();

                    if (viewWidth > 0 && viewHeight > 0) {
                        controller.startGame(); // Adjust your game initialization method here
                        System.out.println("Game initialized with dimensions: " + viewWidth + "x" + viewHeight);
                        ui.gamePanel.removeComponentListener(this); // Remove the listener after initialization
                    } else {
                        System.err.println("View dimensions not available yet.");
                    }
                }
            });
        });

    }
}
