/* ZeeBrick subclass for TetrisBrick superclass
/* Aashish Subedi
/* 10/05/2023
 */

public class ZeeBrick extends TetrisBrick
{
    public ZeeBrick(int centerCol)
    {
       colorNum=4;
       initPosition(centerCol);
        
    }
    
    @Override
    public void initPosition(int centerCol)
    {
        position = new int[4][2];

        for (int row = 0; row < 2; row++) 
        {
            for (int col = 0; col < 2; col++) {
                int posIndex = 0;
                if (row == 0) {
                    posIndex = -1; 
                } else {
                    posIndex = 0; 
                }

                position[row * 2 + col][0] = row;
                position[row * 2 + col][1] = centerCol + col + posIndex;
            }
        }
    }

    
    @Override
    public void rotate()
    {
        int pivotRow = position[1][0];
        int pivotCol = position[1][1];

        int[][] newPosition = new int[position.length][2];

        for (int block = 0; block < position.length; block++) 
        {
            int oldRow = position[block][0];
            int oldCol = position[block][1];

            newPosition[block][0] = pivotRow + (oldCol - pivotCol);
            newPosition[block][1] = pivotCol - (oldRow - pivotRow);
        }
        
        for (int block = 0; block < position.length; block++) 
           {
            position[block][0] = newPosition[block][0];
            position[block][1] = newPosition[block][1];
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
