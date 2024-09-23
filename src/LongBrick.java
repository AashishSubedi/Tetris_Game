/* LongBrick subclass for TetrisBrick superclass
/* Aashish Subedi
/* 10/05/2023
 */
public class LongBrick extends TetrisBrick
{
    public LongBrick(int centerCol)
    {
        colorNum=5;
        initPosition(centerCol);
    }
    
    @Override
    public void initPosition(int centerCol)
    {
        position = new int[4][2]; 
        
        int row = 0;
        for (int col = 0; col < 4; col++) 
        {
            position[col][0] = row;
            position[col][1] = (centerCol -1)+ col -1;
        }
    } 
    

    public void rotate()
    {
        int pivotRow = position[1][0];
        int pivotCol = position[1][1];

        for (int row = 0; row < position.length; row++)
        {
            int oldRow = position[row][0];
            int oldCol = position[row][1];
            position[row][0] = pivotRow + (oldCol - pivotCol);
            position[row][1] = pivotCol - (oldRow - pivotRow);
        }
        
    }
    
    @Override
    public void unrotate()
    {
        rotate();
        rotate();
        rotate();
    }
}
