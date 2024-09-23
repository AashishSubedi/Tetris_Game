/* Managing the size and position of the display
 *  Aashish Subedi 
 *  10/05/2023
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class TetrisDisplay extends JPanel
{
    private TetrisGame game ;
    private int start_X ;
    private int start_Y ;
    private int cellSize = 15;
    private Timer time;
    private int speed ;
    private Color[] colors ;
    private boolean pause;
    private boolean hasPromptedForName = false;

//    public void speedChanger()
//    {
//        
//    }
    
    public  TetrisDisplay(TetrisGame game)
    {
       
        this.game = game;
        
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke)
            {
                translateKey(ke);
            }
        });
        
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        
        
        speed = 350;
        colors =new Color[]{Color.BLUE, Color.ORANGE, Color.GREEN, Color.YELLOW,
                Color.CYAN, Color.RED, Color.MAGENTA};
        time = new Timer(speed, (ActionEvent ae) -> {
            cycleMove();
        });
        
        time.start();      
    }
 
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawWell(g);
        drawFallingBrick(g);
        smallWell(g);
        drawBackground(g);
        scoreDisplay(g);
        gameOverDisplay(g);
        repaint();
        
    }
    
    private void drawWell(Graphics g)
    {
        g.setColor(Color.WHITE);

        int wellWidth = cellSize * game.getCols();
        int wellHeight = cellSize * game.getRows();
        int wellStartX = (this.getWidth() - wellWidth) / 2;
        int wellStartY = (this.getHeight() - wellHeight) / 2;
        g.fillRect(wellStartX, wellStartY, wellWidth, wellHeight);

        g.setColor(Color.BLACK);
        
        int rect_corner_x = wellStartX;
        int rect_corner_y = wellStartY - cellSize;
        int rect_width = wellWidth;
        int rect_height = cellSize;

        // Bottom rectangle
        rect_corner_x = wellStartX - cellSize;
        rect_corner_y = wellStartY + wellHeight;
        rect_width = cellSize * game.getCols() + cellSize * 2;
        g.fillRect(rect_corner_x, rect_corner_y, rect_width, rect_height);

        // Left rectangle 
        rect_corner_x = wellStartX - cellSize; 
        rect_corner_y = wellStartY ; 
        rect_width = cellSize;
        rect_height = game.getRows() * cellSize;
        g.fillRect(rect_corner_x, rect_corner_y, rect_width, rect_height);

        // Right rectangle 
        rect_corner_x = wellStartX + game.getCols() * cellSize; 
        rect_corner_y = wellStartY ; 
        rect_width = cellSize;
        rect_height = game.getRows() * cellSize; 
        g.fillRect(rect_corner_x, rect_corner_y, rect_width, rect_height);
    }
    
    
    private void drawFallingBrick(Graphics g)
    {
        start_X = (this.getWidth()-cellSize*game.getCols())/2;
        start_Y = (this.getHeight()-cellSize*game.getRows())/2;
        int numSegments = game.getNumSegs(); // Get the actual number of segments
        for (int segment = 0; segment < numSegments; segment++) 
        {
            int yPosition = game.getSegRow(segment);
            int xPosition = game.getSegCol(segment);

            if (yPosition >= 0 && xPosition >= 0)
            {
                g.setColor(colors[game.getFallingBrickColor()]);
                g.fillRect(start_X + xPosition * cellSize, start_Y + yPosition * cellSize,
                           cellSize, cellSize);

                g.setColor(Color.BLACK);
                g.drawRect(start_X + xPosition * cellSize, 
                           start_Y + yPosition * cellSize,
                           cellSize, cellSize);
            }  
            
        } 
    }
    
    private void smallWell(Graphics g) 
    {
        int[][] nextBrickShape = game.getNextBrickShape();
        int nextBrickColor = game.getNextBrickColor();

        int previewWindowWidth = cellSize * 5;
        int previewWindowHeight = cellSize * 4;
        int previewWindowStartX = (this.getWidth() - cellSize * game.getCols()) 
                / 2 + cellSize * game.getCols() + 20;
        int previewWindowStartY = (this.getHeight() - cellSize * game.getRows()) / 2;

        g.setColor(Color.WHITE);
        g.fillRect(previewWindowStartX, previewWindowStartY, 
                previewWindowWidth, previewWindowHeight);
        g.setColor(Color.BLACK);
        g.drawRect(previewWindowStartX, previewWindowStartY, 
                previewWindowWidth, previewWindowHeight);

        int offsetX = (previewWindowWidth - (getMaxBrickWidth(nextBrickShape) + 5)
                * cellSize) / 2;
        int offsetY = (previewWindowHeight - (getMaxBrickHeight(nextBrickShape) )
                * cellSize) / 2;

        g.setColor(colors[nextBrickColor]);
        for (int[] segment : nextBrickShape) 
        {
            int segmentDrawX = previewWindowStartX + offsetX + segment[1] * cellSize;
            int segmentDrawY = previewWindowStartY + offsetY + segment[0] * cellSize;
            g.fillRect(segmentDrawX, segmentDrawY, cellSize, cellSize);
        }
    }
    
    private int getMaxBrickWidth(int[][] brickShape)
    {
        int maxWidth = 0;
        for (int[] segment : brickShape)
        {
            maxWidth = Math.max(maxWidth, segment[1]);
        }
        return maxWidth + 1; 
    }

    private int getMaxBrickHeight(int[][] brickShape) {
        int maxHeight = 0;
        for (int[] segment : brickShape) {
            maxHeight = Math.max(maxHeight, segment[0]);
        }
        return maxHeight + 1; 
    }

 
   private void drawBackground(Graphics g) 
   {      
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                if(game.fetchBoardPosition(row, col) != -1){
                    g.setColor(colors[game.fetchBoardPosition(row, col)]);
                    g.fillRect(start_X + col*cellSize , start_Y + 
                            row *cellSize, cellSize, cellSize);
                    g.setColor(Color.black);
                    g.drawRect(start_X + cellSize * col, start_Y + 
                            cellSize * row, cellSize, cellSize);

                }
            }
        }
    }
   
   private void scoreDisplay(Graphics g) 
   {

        int xPos = 10;
        int yPos = 9;
        Font scoreFont = new Font("Arial", Font.BOLD, 22);
        g.setFont(scoreFont);
        
        g.setColor(Color.WHITE);
        g.drawRect(xPos, yPos, cellSize * yPos, cellSize*2);
        g.fillRect(xPos, yPos, cellSize * yPos, cellSize*2);
        
        g.setColor(Color.BLACK);
        String scoreText = "Score: " + game.getScore();
        g.drawString(scoreText, cellSize, cellSize*2);

    }

    private void translateKey(KeyEvent ke)
    { 
        switch (ke.getKeyCode()) {
        case KeyEvent.VK_UP -> game.makeMove("rotate");
        case KeyEvent.VK_DOWN -> game.makeMove("down");
        case KeyEvent.VK_RIGHT -> game.makeMove("right");
        case KeyEvent.VK_LEFT -> game.makeMove("left");
        case KeyEvent.VK_N -> 
        {
            pause = true;
            time.stop();
            int result = JOptionPane.showConfirmDialog(null,
                    "Do you want to save the current Game?", 
                    "Save Game", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) 
            {
                game.saveToFile();
            }
            game.newGame();
            pause = false;
            time.start();
        }
        case KeyEvent.VK_SPACE -> {
            pause = !pause;
            if (pause) {
                time.stop();
            } else {
                time.start();
            }
            }
        default -> {
            }

        }
    }

    private void gameOverDisplay(Graphics g) {
        if (game.isGameOver()) 
        {
            int centerX = start_X + (cellSize * game.getCols()) / 2;
            int centerY = start_Y + (cellSize * game.getRows()) / 2;
           
            g.setColor(Color.WHITE);
                int rectWidth = cellSize * game.getCols() *2; 
                g.fillRect(centerX - rectWidth / 2, centerY - 3 * cellSize, rectWidth, 6 * cellSize);

                Font gameOverFont = new Font("Arial", Font.BOLD, 30);
                g.setFont(gameOverFont);
                g.setColor(Color.BLUE);
                gameOverMessage(g, "Game Over!", centerX, centerY, gameOverFont);

                Font scoreFont = new Font("Arial", Font.BOLD, 20);
                g.setFont(scoreFont);
                gameOverMessage(g, "Score: " + game.getScore(), centerX, centerY + 2 * cellSize, scoreFont);
                
            if (!hasPromptedForName) 
            {
                hasPromptedForName = true;

                String playerName = JOptionPane.showInputDialog("Enter your name for the leaderboard:");
                if (playerName != null && !playerName.trim().isEmpty()) 
                {
//                    game.updateLeaderboard(playerName, game.getScore());
                }
            }
        } 
        
        else {
            hasPromptedForName = false;
        }
    }

    private void gameOverMessage(Graphics g, String text, int x, int y, Font font) 
    {
        FontMetrics metrics = g.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);
        g.drawString(text, x - textWidth / 2, y);
    }
 
    private void cycleMove()
    {
        if(!pause)
        {
            game.makeMove("down");
        }  
        repaint();
    }
    
}