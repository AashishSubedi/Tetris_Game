/* Width, height, rows and column of the game window
 * Aashish Subedi
 * 10/05/2023
 */
import java.io.File;
import javax.swing.*;

public class TetrisWindow extends JFrame 
{
    private TetrisGame game;
    private TetrisDisplay display;
    private int win_width = 612;
    private int win_height = 632;
    private int game_rows  = 20;
    private int game_cols = 12;

    public  TetrisWindow()
    {
        this.setTitle("My Tetris Game                     Aashish Subedi");
        this.setSize(win_width,win_height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        menuItem();
        game = new TetrisGame(game_rows, game_cols);
        display = new TetrisDisplay(game);
        this.add(display);
        
        this.setVisible(true);   
    }
        
    public void menuItem()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem gameItem = new JMenuItem("New Game");
        JMenuItem gameItem1 = new JMenuItem("Save Game");
        JMenuItem gameItem2 = new JMenuItem("Open Saved Game");

        gameItem.addActionListener(e -> 
        {
            game.newGame();
        });

        gameItem1.addActionListener(e -> 
        {
            game.saveToFile();
        });

        gameItem2.addActionListener(e -> 
        {
           openFiles();
        });

        gameMenu.add(gameItem);
        gameMenu.add(gameItem1);
        gameMenu.add(gameItem2);
       
        JMenu exitGame = new JMenu("Quit");
        JMenuItem quitItem = new JMenuItem("Exit");

        quitItem.addActionListener(e -> 
        {
            int response = JOptionPane.showConfirmDialog(gameMenu, 
                    "Do you want to save game before exiting?", "Save Game?", 
                    JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
            
            if(response == JOptionPane.YES_OPTION)
            {
                game.saveToFile();               
            }
            System.exit(0); 
        });

        exitGame.add(quitItem);

        JMenu leaderboardMenu = new JMenu("Leaderboard");
        JMenuItem showLeaderboardItem = new JMenuItem("Show Leaderboard");
        JMenuItem resetScoresItem = new JMenuItem("Reset High Scores");

        showLeaderboardItem.addActionListener(e ->
        {
            game.showLeaderboard();
        });
        resetScoresItem.addActionListener(e ->
        { 
            game.resetLeaderBoard();
        });

        leaderboardMenu.add(showLeaderboardItem);
        leaderboardMenu.add(resetScoresItem);
        setJMenuBar(menuBar);

        menuBar.add(gameMenu);
        menuBar.add(leaderboardMenu);
        menuBar.add(exitGame);

        this.setJMenuBar(menuBar);
    }
    

    
   private void openFiles() 
   {
        JFileChooser fileChooser = new JFileChooser();
        int fileClicked = fileChooser.showOpenDialog(this);

        if (fileClicked == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            if (!selectedFile.exists()) {
                JOptionPane.showMessageDialog(this, 
                        "File not found. Please select a valid file.", 
                        "File Not Found", JOptionPane.ERROR_MESSAGE);
                return; 
            }

            try {
                game.retrieveFromFile(selectedFile);
                JOptionPane.showMessageDialog(this, 
                        "File loaded successfully!", "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to load file: " + 
                        e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                    "File selection cancelled.", "Cancelled", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }


    public static void main(String[] args)
    {
        TetrisWindow tetrisWindow = new TetrisWindow();   
    } 
}