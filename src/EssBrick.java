/* EssBrick subclass for TetrisBrick superclass
/* Aashish Subedi
/* 10/05/2023
 */

public class EssBrick extends TetrisBrick
{
    public EssBrick(int centerCol)
    {
        colorNum=3;
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
                if (row == 0) 
                {
                    posIndex = 1;
                }

                position[row * 2 + col][0] = row;
                position[row * 2 + col][1] = (centerCol -1)+ col + posIndex;
            }
        }
    }

    @Override
    public void rotate() 
    {
        int pivotRow = position[3][0];
        int pivotCol = position[3][1];

        for (int[] position1 : position) {
            int oldRow = position1[0];
            int oldCol = position1[1];
            position1[0] = pivotRow - (oldCol - pivotCol);
            position1[1] = pivotCol + (oldRow - pivotRow);
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
