/* JayBrick subclass for TetrisBrick superclass
/* Aashish Subedi
/* 10/05/2023
 */

public class JayBrick extends TetrisBrick
{
    public JayBrick(int centerCol)
    {
        colorNum=2;
        initPosition(centerCol);    
    }
    
    @Override
    public void initPosition(int centerCol)
    {
        position = new int[4][2];
        for (int row = 0; row < 3; row++) 
        {
            position[row][0] = row;
            position[row][1] = centerCol;

            if (row == 2) {
                position[row + 1][0] = row;
                position[row + 1][1] = centerCol - 1;
            }
        }
    }
    
    @Override
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
