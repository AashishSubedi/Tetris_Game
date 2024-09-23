/* Abstract TetrisBrick class for implementing bricks in the game
/* Aashish Subedi
/* 10/05/2023
*/
import java.awt.*;

public abstract class TetrisBrick 
{
    protected int numSegments =4;
    protected int[][] position;
    protected Color color;
    protected int colorNum;

    public TetrisBrick()
    {

    }
    
    @Override
    public String toString(){
        return null;
    }
    
    
    public void moveDown() 
    {
        for (int[] position1 : position) 
        {
            position1[0]++;
            
        }
    }
    
    
    public void moveUp()
    {
        for (int[] position1 : position) 
        {
            position1[0]--;
        }
    }
    
    
    public int getColorNumber()
    {
        return colorNum;
    } 
    
    
    public abstract void rotate();
    
    
    
    public abstract void unrotate();
    
    
    public void moveLeft()
    {
        for (int[] position1 : position) {
            position1[1]--;
        }

    }
        
    public void moveRight()
    {
        
        for (int[] position1 : position)
        {
            position1[1]++;
        }  
    }
    public void setSegPosition(int numSeg, int num, int val)
    {
        position[numSeg][num] = val;
    }
    
    public abstract void initPosition(int centerCol);
 
}
