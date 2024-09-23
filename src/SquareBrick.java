/* SquareBrick subclass for TetrisBrick superclass
/* Aashish Subedi
/* 10/05/2023
 */
public class SquareBrick extends TetrisBrick
{
    public SquareBrick(int centerCol)
    {
        colorNum=0;
        initPosition(centerCol);
    }
    
    @Override
    public void initPosition(int centerCol) 
    {
        position = new int[4][2];
        int posIndex  = 0;
        for (int row = 0; row < 2; row++) 
        {
            for (int col = 0; col < 2; col++) 
            {
                position[posIndex ][0] = row;
                position[posIndex ][1] = (centerCol-1) + col;
                posIndex ++;
            }
        }
    }

    
    @Override
    public void rotate()
    {
        
    }
    
    @Override
    public void unrotate()
    {
        
    }
}
