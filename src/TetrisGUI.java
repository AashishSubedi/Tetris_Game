import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TetrisGUI extends JFrame {

    private TetrisGame game;
    private TetrisDisplay gameDisplay;
    private JPanel mainMenu;
    private Image backgroundImage;

    public TetrisGUI() {
        setTitle("Tetris - Aashish Subedi");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        loadBackgroundImage();
        initMainMenu();
    }

    private void loadBackgroundImage() {
        backgroundImage = new ImageIcon("tetrisdispBack.png").getImage();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void initMainMenu() {
        mainMenu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.Y_AXIS));
        mainMenu.setOpaque(false);

        JLabel titleLabel = new JLabel("Tetris - Aashish Subedi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> startGame());

        JButton leaderboardButton = new JButton("View Leaderboard");
        leaderboardButton.setFont(new Font("Arial", Font.BOLD, 24));
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardButton.addActionListener(e -> showLeaderboard());

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> System.exit(0));

        mainMenu.add(Box.createVerticalStrut(100));
        mainMenu.add(titleLabel);
        mainMenu.add(Box.createVerticalStrut(50));
        mainMenu.add(startButton);
        mainMenu.add(Box.createVerticalStrut(20));
        mainMenu.add(leaderboardButton);
        mainMenu.add(Box.createVerticalStrut(20));
        mainMenu.add(exitButton);

        add(mainMenu, BorderLayout.CENTER);
        setVisible(true);
    }

    private void startGame() {
        game = new TetrisGame(20, 12); // Initialize a new game
        gameDisplay = new TetrisDisplay(game);

        // Transition to game display
        getContentPane().removeAll();
        getContentPane().add(gameDisplay, BorderLayout.CENTER);
        revalidate();
        repaint();

        gameDisplay.requestFocusInWindow();
    }

    private void showLeaderboard() {
        if (game != null) {
            game.showLeaderboard();
        } else {
            JOptionPane.showMessageDialog(this, "No leaderboard data available.", "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TetrisGUI::new);
    }
}
