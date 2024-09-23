/* StackBrick subclass for TetrisBrick superclass
/* Aashish Subedi
/* 10/05/2023
 */
public class StackBrick extends TetrisBrick
{
    public StackBrick(int centerCol)
    {
        colorNum=6;
        initPosition(centerCol);
    }
    
    @Override
    public void initPosition(int centerCol) 
    {
        position = new int[4][2]; 

        position[0][0] = 0;
        position[0][1] = centerCol;

        for (int col = 0; col < 3; col++)
        {
            position[col + 1][0] = 1;
            position[col + 1][1] = centerCol + col - 1; 
        }
        
    }

    
    public void rotate()
    {
        int pivotRow = position[2][0];
        int pivotCol = position[2][1];

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
