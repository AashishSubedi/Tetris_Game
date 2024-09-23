/*  Implementing the logic for tetris game
/*  Aashish Subedi
/*  10/05/2023 
 */ 

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;


public class TetrisGame  
{
    private TetrisBrick fallingBrick;
    private TetrisBrick nextBrick;
    private int rows;
    private int cols;
    private int numbBrickTypes = 7;
    private Random randGen;
    public int[][] background;
    private int state = 0;
    private int score = 0;
    private boolean gameOver;
    private int minScore;


    public TetrisGame(int row, int col) 
    {
        rows = row;
        cols = col;
        gameOver = false;
        randGen = new Random();
        initBoard();
        spawnBrick();

    }
    
    public String toString()
    {
        String backgroundStr = "";
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                backgroundStr += background[row][col] + ",";
            }
            backgroundStr += "\n";
        }
        return  fallingBrick.toString() +
                "\n" + rows + "," + cols + "," + score + "," + state +
                "\n" + backgroundStr;
    }
    
    
    private void initBoard()
    {
        background = new int[rows][cols];
        for (int row = 0; row < background.length; row++) {
            for (int col = 0; col < background[row].length; col++) {
                background[row][col] = -1;
            }
        }
    }
    
    
    public void newGame()
    {
        initBoard();
        spawnBrick();
        score = 0;
        state = 0;
        gameOver = false;
    }
    
    
    public int fetchBoardPosition(int row, int col)
    {
        if (row >= 0 && row < background.length && col>= 0 
                && col < background[row].length)
        {
            return background[row][col];
        } 
        else 
        {
            return -1;
        }  
    }
    
    
    public void transferColor()
    {
        for (int numSeg = 0; numSeg < getNumSegs(); numSeg++) {
            int row = fallingBrick.position[numSeg][0];
            int col = fallingBrick.position[numSeg][1];
            if (row >= 0 && row < rows && col >= 0 && col < cols)
            {
                background[row][col] = fallingBrick.getColorNumber();
            }           
       }
    }
    
    private void spawnNextBrick() {
        int typeOfBricks = randGen.nextInt(numbBrickTypes);
        int centerCol = cols / 2;

        switch (typeOfBricks) {
            case 0:
                nextBrick = new ElBrick(centerCol);
                break;
            case 1:
                nextBrick = new EssBrick(centerCol);
                break;
            case 2:
                nextBrick = new JayBrick(centerCol);
                break;
            case 3:
                nextBrick = new LongBrick(centerCol);
                break;
            case 4:
                nextBrick = new SquareBrick(centerCol);
                break;
            case 5:
                nextBrick = new StackBrick(centerCol);
                break;
            case 6:
                nextBrick = new ZeeBrick(centerCol);
                break;
        }
    }

    public void spawnBrick() {
        if (nextBrick == null) {
            spawnNextBrick();
        }
        fallingBrick = nextBrick;
        spawnNextBrick();
    }
    
    
    public void makeMove(String move) {
        if ("down".equals(move)) {
            fallingBrick.moveDown();

            if (!validateMove()) {
                fallingBrick.moveUp(); 
                transferColor();
                checkClearAndShiftRows();
                
                for (int segNum = 0; segNum < fallingBrick.numSegments; segNum++) {
                    if (fallingBrick.position[segNum][0] <= 0) {
                        gameOver = true; 
                        break;                       
                    }                   
                }

                if (!gameOver) {
                    spawnBrick(); 
                }
            }
        }

        else if ("rotate".equals(move))
        {
            fallingBrick.rotate();
            if (!validateMove()) {
                fallingBrick.unrotate();
            }
            
        } 
        else if ("left".equals(move)) 
        {
            fallingBrick.moveLeft();
            if(!validateMove())
            {
                fallingBrick.moveRight();
                
            }
        } 
        else if ("right".equals(move))
        {
            fallingBrick.moveRight();
            if(!validateMove())
            {
                fallingBrick.moveLeft();
                
            }
        } 
    } 
    public boolean isGameOver()
    {
        return gameOver;
    }

    
    private boolean validateMove()
    {
        for (int segNum = 0; segNum < fallingBrick.position.length; segNum++)
        {            
            int bound = 0;
            int rightBound =cols;
            int newRow = fallingBrick.position[segNum][0];
            int newCol = fallingBrick.position[segNum][1];
            
            if (newRow<bound || newRow >= rows)
            {
                return false; 
            }
            
            if(newCol < bound || newCol >= rightBound)
            {
                return false;
            }
            
            if (background[getSegRow(segNum)][getSegCol(segNum)]>=0) 
            { 
                
                return false; 
            }
        }    
        return true;      
    }
    
    // Checks if a row has a space
    private boolean rowHasSpace(int rowNumber) {
        for (int col = 0; col < cols; col++) {
            if (background[rowNumber][col] == -1) {
                return true;
            }
        }
        return false;
    }

    // Copies row content to the row below
    private void copyRow(int rowNumber) {
        if (rowNumber > 0) {
            System.arraycopy(background[rowNumber - 1], 0, background[rowNumber], 0, cols);
        }
    }

    // Copies all rows above a given row downwards
    private void copyAllRows(int rowNumber) {
        for (int shiftRow = rowNumber; shiftRow > 0; shiftRow--) {
            copyRow(shiftRow);
        }
    }
    public void checkClearAndShiftRows() {
        int linesCleared = 0;
        for (int row = 0; row < rows; row++) {
            if (!rowHasSpace(row)) {
                linesCleared++;
                clearRow(row);
                copyAllRows(row);
                resetTopRow();

                rowClearSound();
            }
        }

        if (linesCleared > 0) {
            updateScore(linesCleared);
        }
    }

    // Clears a single row
    private void clearRow(int row) {
        for (int col = 0; col < cols; col++) {
            background[row][col] = -1;
        }
    }

    // Resets the top row
    private void resetTopRow() {
        for (int col = 0; col < cols; col++) {
            background[0][col] = -1;
        }
    }
   
    private void updateScore(int linesCleared)
    {
        switch (linesCleared) 
        {
        case 1:
            score += 100;
            break;
        case 2:
            score += 300;
            break;
        case 3:
            score += 600;
            break;
        case 4:
            score += 1200;
            break;
        default:
            break;
        }
    
    }
        
    
    private void rowClearSound() {
        String filePath = "/wossh.wav"; 
        try {
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            if (inputStream != null) {
                AudioInputStream audio = AudioSystem.getAudioInputStream(inputStream);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
            } else {
                System.err.println("Sound file not found: " + filePath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(TetrisGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveToFile()
    {
        JFileChooser chooser = new JFileChooser();
        int retrieve = chooser.showSaveDialog(null);

        if (retrieve == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            if (selectedFile.exists()) {
               
                int result = JOptionPane.showConfirmDialog(null, 
                    "File already exists. Overwrite?", 
                    "Existing file", 
                    JOptionPane.YES_NO_CANCEL_OPTION);
                

                if (result != JOptionPane.YES_OPTION) 
                {
                    return; 
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                for (int seg = 0; seg < fallingBrick.numSegments; seg++) {
                    writer.write(fallingBrick.position[seg][0] + "," + fallingBrick.position[seg][1] + ",");
                }
                writer.newLine();
                writer.write(fallingBrick.getColorNumber() + ",");
                writer.newLine();
                writer.write(rows + "," + cols + "," + score + "," + state);
                writer.newLine();
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        writer.write(background[row][col] + ",");
                    }
                    writer.newLine();
                }

                JOptionPane.showMessageDialog(null, "File saved successfully!");
            }
            catch (IOException e) 
            {
                JOptionPane.showMessageDialog(null, "Error saving file: " 
                        + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

     public void retrieveFromFile(File myFile) throws IOException
     {
        try (Scanner inScan = new Scanner(myFile)) {
            if (!inScan.hasNextLine()) {
                throw new IOException("File is empty or improperly formatted");
            }

            String firstLine = inScan.nextLine();
            Scanner lineScan = new Scanner(firstLine).useDelimiter("[\n,]");

            int numSeg = 0;
            while (lineScan.hasNextInt()) {
                fallingBrick.setSegPosition(numSeg, 0, lineScan.nextInt());
                fallingBrick.setSegPosition(numSeg, 1, lineScan.nextInt());
                numSeg += 1;
            }
            lineScan.close();

            if (!inScan.hasNextLine()) {
                throw new IOException("Incomplete file content");
            }
            String secondLine = inScan.nextLine();
            lineScan = new Scanner(secondLine).useDelimiter("[\n,]");
            fallingBrick.colorNum = lineScan.nextInt();
            lineScan.close();

            if (!inScan.hasNextLine()) {
                throw new IOException("Incomplete file content");
            }
            String thirdLine = inScan.nextLine();
            lineScan = new Scanner(thirdLine).useDelimiter("[\n,]");
            rows = lineScan.nextInt();
            cols = lineScan.nextInt();
            score = lineScan.nextInt();
            state = lineScan.nextInt();
            lineScan.close();

            int row = 0;
            while (inScan.hasNextLine()) {
                String nextLine = inScan.nextLine();
                lineScan = new Scanner(nextLine).useDelimiter("[\n,]");

                int col = 0;
                while (lineScan.hasNextInt()) {
                    background[row][col] = lineScan.nextInt();
                    col += 1;
                }
                lineScan.close();
                row += 1;
            }
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "File format error: " + e.getMessage(),
                                          "File Format Error", JOptionPane.ERROR_MESSAGE);
        } 
    } 
     
    public int[][] getNextBrickShape() {
        return nextBrick.position;
    }

    public int getNextBrickColor() {
        return nextBrick.getColorNumber();
    }
    
    public int getFallingBrickColor()
    {
        
        return fallingBrick.getColorNumber();
    }
    
    
    public int getNumSegs()
    {
        return fallingBrick.numSegments;
    }
    
    
    public int getSegRow(int segNum)
    {
        return fallingBrick.position[segNum][0];
    }

    
    public int getSegCol(int segNum)
    {
        return fallingBrick.position[segNum][1];
    }

    
    public int getRows() 
    {
        return rows;
    }

    public int getCols() 
    {
        return cols;
    }
    
    public void setRows(int newRows){
        
         rows = newRows;
    }
    
    public void setCols(int newCols) 
    {
         cols = newCols;
    }
    
    public int getScore()
    {
      return score;  
    }

    
    public String displayLeaderboard(ArrayList<String> leaderNames, ArrayList<Integer> scoreList) {
        StringBuilder leaderBoard = new StringBuilder();

        int size = scoreList.size();
        if (size == 0) 
        {
            
        }

        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indices.add(i);
        }
        indices.sort((i, j) -> scoreList.get(j) - scoreList.get(i));
        int topLeaders = Math.min(size, 10);

        for (int i = 0; i < topLeaders; i++) {
            int index = indices.get(i);
            leaderBoard.append(i + 1).append(".   ")
                       .append(leaderNames.get(index)).append(":   ")
                       .append(scoreList.get(index)).append(" points.\n");
        }

        return leaderBoard.toString();
    }

    
    public void resetLeaderBoard()
    {
        try (FileWriter writer = new FileWriter("leaderboard.csv")) {
            writer.write("");
            writer.close();
        } catch (IOException ioe) 
        {
            JOptionPane.showMessageDialog(null, "Error resetting leaderboard: " + ioe.getMessage(), 
                                          "File Reset Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void storeScore(String playerName, int score) {
        String LEADERBOARD_FILE = "leaderboard.csv";
        File file = new File(LEADERBOARD_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(playerName + "," + score + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to leaderboard file: " + e.getMessage());
        }
    }
    
    public void showLeaderboard() {
        File scoreFile = new File("leaderboard.csv");
        ArrayList<Integer> scoreList = new ArrayList<>();
        ArrayList<String> leaderNames = new ArrayList<>();

        try {
            Scanner inScan = new Scanner(scoreFile);

            while (inScan.hasNextLine()) {
                String[] parts = inScan.nextLine().split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score;
                    try {
                        score = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    leaderNames.add(name);
                    scoreList.add(score);
                }
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error occurred while displaying leaderboard: " + ioe.getMessage(), 
                                          "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String leaderboardDisplay = displayLeaderboard(leaderNames, scoreList);
        JOptionPane.showMessageDialog(null, leaderboardDisplay,
                "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }
    
}

