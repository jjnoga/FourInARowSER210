package edu.quinnipiac.ser210.fourinarow;

import android.widget.GridLayout;
import android.widget.ImageButton;

/*
Class: FourInARow.java
Author: Joseph Noga
Professor: Rehab ElKharboutly
Class: SER210
Date: 2/18/2022
Description: The bread and butter for the AI portion of the game. It's a rather amateur AI, and
one with many flaws, but it does work to some capacity. Use to go through all the buttons and
attempt to strategically place a marker to either block off the player's attempt to win or
to get the CPU the victory. Also used to determine who won the round.
 */
public class FourInARow implements IGame {
    // instance variables
    private static final int ROWS = 6, COLS = 6, TURN_MAX = 18; // number of rows and columns
    private ImageButton[][] board = new ImageButton[ROWS][COLS]; // game board in 2D array
    private String currPlayer, userColor, cpuColor;
    private int turnCount = 0;
    private boolean canPlace;
    private int[] buttonIds;
    private GameBoardActivity gba;

    //constructor
    //@param buttonIds - reference for which button represents what location
    //@param newGba - used to get the views of the button ids
    public FourInARow(int[] buttonIds, GameBoardActivity newGba){
        //clearBoard();
        canPlace = true;
        this.buttonIds = buttonIds;
        this.gba = newGba;

        int count = 0;
        for (int row = 0; row < ROWS; ++row)
        {
            for (int col = 0; col < COLS; ++col)
            {
                board[row][col] = (ImageButton) gba.findViewById(buttonIds[count]);
                count++;
            }
        }
    }

    //used to clear the board, although the way I have it restart doesn't require this (from interface)
    @Override
    public void clearBoard() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col].setImageResource(R.drawable.blank);
            }
        }
    }

    //determines whether or not a marker can legally be placed on the board, and does so accordingly
    // if it is legal (from interface)
    @Override
    public void setMove(int player, int location) {
        canPlace = true;

        int currCol = location % COLS;
        int currRow = 0;
        for (int i = location; i >= ROWS; i -= ROWS)
        {
            currRow++;
        }

        String imageName = (String) board[currRow][currCol].getTag();

        if (!imageName.equals("R.drawable.blank"))
        {
            canPlace = false;
        }
        else
        {
            board[currRow][currCol].setImageResource(player);
            String newName = "";
            if (player == R.drawable.bluex) { newName = "R.drawable.bluex"; }
            else newName = "R.drawable.redo";
            board[currRow][currCol].setTag(newName);
            turnCount++;
        }
    }

    //judges what move would be best for the CPU to take (from interface)
    @Override
    public int getComputerMove() {
        //int location = (int)(Math.random() * 36);
        int location = 0;

        //see if there is some way to block user victory vertically
        location = verticalComputerAnalysis(userColor);
        if (location == 0) {
            location = verticalComputerAnalysis(cpuColor);
        }
        if (location == 0) {
            //see if there is some way to block user victory horizontally
            location = horizontalComputerAnalysis(userColor);
        }
        if (location == 0) {
            //see if horizontal victory is in sight
            location = horizontalComputerAnalysis(cpuColor);
        }
        if (location == 0) {
            //see if there is some way to block user victory upwards-diagonally
            location = upDiagComputerAnalysis(userColor);
        }
        if (location == 0) {
            //see if upwards diagonal victory is in sight
            location = upDiagComputerAnalysis(cpuColor);
        }
        if (location == 0) {
            //see if there is some way to block user victory downwards-diagonally
            location = downDiagComputerAnalysis(userColor);
        }
        if (location == 0) {
            //see if downwards diagonal victory is in sight
            location = downDiagComputerAnalysis(cpuColor);
        }

        while (location == 0)
        {
            int randomLocation = (int) (Math.random() * 36);
            location = randomLocation;

            int col = location % COLS;
            int row = 0;
            for (int i = location; i >= ROWS; i -= ROWS)
            {
                row++;
            }

            String imageName = (String) board[row][col].getTag();
            if(!imageName.equals("R.drawable.blank"))
            {
                location = 0;
            }
        }

        return location - 1;
    }

    //sees if anyone has met the conditions to win
    @Override
    public int checkForWinner() {
        int gameState = PLAYING;
        boolean blueFlag = false;
        boolean redFlag = false;
        //vertical
        for(int row = 0; row < ROWS; row++)
        {
            for (int col = 0;col < COLS - 3;col++)
            {
                String imageName = (String) board[row][col].getTag();
                String imageName2 = (String) board[row][col+1].getTag();
                String imageName3 = (String) board[row][col+2].getTag();
                String imageName4 = (String) board[row][col+3].getTag();
                if (imageName.equals("R.drawable.bluex") && imageName2.equals("R.drawable.bluex") && imageName3.equals("R.drawable.bluex") && imageName4.equals("R.drawable.bluex"))
                {
                    blueFlag = true;
                }
                else if (imageName.equals("R.drawable.redo") && imageName2.equals("R.drawable.redo") && imageName3.equals("R.drawable.redo") && imageName4.equals("R.drawable.redo"))
                {
                    redFlag = true;
                }
            }
        }
        //horizontal
        for(int row = 0; row < ROWS - 3; row++)
        {
            for(int col = 0; col < COLS; col++)
            {
                String imageName = (String) board[row][col].getTag();
                String imageName2 = (String) board[row+1][col].getTag();
                String imageName3 = (String) board[row+2][col].getTag();
                String imageName4 = (String) board[row+3][col].getTag();
                if (imageName.equals("R.drawable.bluex") && imageName2.equals("R.drawable.bluex") && imageName3.equals("R.drawable.bluex") && imageName4.equals("R.drawable.bluex"))
                {
                    blueFlag = true;
                }
                else if (imageName.equals("R.drawable.redo") && imageName2.equals("R.drawable.redo") && imageName3.equals("R.drawable.redo") && imageName4.equals("R.drawable.redo"))
                {
                    redFlag = true;
                }
            }
        }
        //upwards diagonal
        for(int row = 3; row < ROWS; row++)
        {
            for(int col = 0; col < COLS - 3; col++)
            {
                String imageName = (String) board[row][col].getTag();
                String imageName2 = (String) board[row-1][col+1].getTag();
                String imageName3 = (String) board[row-2][col+2].getTag();
                String imageName4 = (String) board[row-3][col+3].getTag();

                if (imageName.equals("R.drawable.bluex") && imageName2.equals("R.drawable.bluex") && imageName3.equals("R.drawable.bluex") && imageName4.equals("R.drawable.bluex"))
                {
                    blueFlag = true;
                }
                else if (imageName.equals("R.drawable.redo") && imageName2.equals("R.drawable.redo") && imageName3.equals("R.drawable.redo") && imageName4.equals("R.drawable.redo"))
                {
                    redFlag = true;
                }
            }
        }

        //downwards diagonal
        for(int row = 0; row < ROWS - 3; row++)
        {
            for(int col = 0; col < COLS - 3; col++)
            {
                String imageName = (String) board[row][col].getTag();
                String imageName2 = (String) board[row+1][col+1].getTag();
                String imageName3 = (String) board[row+2][col+2].getTag();
                String imageName4 = (String) board[row+3][col+3].getTag();

                if (imageName.equals("R.drawable.bluex") && imageName2.equals("R.drawable.bluex") && imageName3.equals("R.drawable.bluex") && imageName4.equals("R.drawable.bluex"))
                {
                    blueFlag = true;
                }
                else if (imageName.equals("R.drawable.redo") && imageName2.equals("R.drawable.redo") && imageName3.equals("R.drawable.redo") && imageName4.equals("R.drawable.redo"))
                {
                    redFlag = true;
                }
            }
        }

        if (blueFlag == true)
        {
            gameState = BLUE_WON;
        }
        else if (redFlag == true)
        {
            gameState = RED_WON;
        }
        else if (turnCount == TURN_MAX)
        {
            gameState = TIE;
        }

        return gameState;
    }

    //the following three methods are getters/setters
    public boolean legalMoveCheck()
    {
        return canPlace;
    }

    public void setCPUColor(String color)
    {
        this.cpuColor = color;
    }

    public void setPlayerColor(String color)
    {
        this.userColor = color;
    }

    //sees if theres a strategically-good place to put a marker vertically
    public int verticalComputerAnalysis(String color)
    {
        int newLocation = 0;
        for (int col = 0; col < COLS; col++)
        {
            for (int row = 0; row < ROWS - 3; row++)
            {
                String imageName = (String) board[row][col].getTag();
                String imageName2 = (String) board[row+1][col].getTag();
                String imageName3 = (String) board[row+2][col].getTag();
                String imageName4 = (String) board[row+3][col].getTag();
                if ((imageName.equals("R.drawable.blank") && imageName2.equals(color) && imageName3.equals(color) && imageName4.equals(color))
                        || (imageName.equals("R.drawable.blank") && imageName2.equals(color) && imageName3.equals(color)))
                {
                    switch (col)
                    {
                        case (5): newLocation = (row+1) * 6; break;
                        case (4): newLocation = ((row+1) * 6) - 1; break;
                        case (3): newLocation = ((row+1) * 6) - 2; break;
                        case (2): newLocation = ((row+1) * 6) - 3; break;
                        case (1): newLocation = ((row+1) * 6) - 4; break;
                        default: newLocation = ((row+1) * 6) - 5;
                    }
                }
                else if ((imageName.equals(color) && imageName2.equals("R.drawable.blank") && imageName3.equals(color) && imageName4.equals(color))
                        || (imageName.equals(color) && imageName2.equals("R.drawable.blank") && imageName3.equals(color)))
                {
                    switch (col)
                    {
                        case (5): newLocation = (row+2) * 6; break;
                        case (4): newLocation = ((row+2) * 6) - 1; break;
                        case (3): newLocation = ((row+2) * 6) - 2; break;
                        case (2): newLocation = ((row+2) * 6) - 3; break;
                        case (1): newLocation = ((row+2) * 6) - 4; break;
                        default: newLocation = ((row+2) * 6) - 5;
                    }
                }
                else if ((imageName.equals(color) && imageName2.equals(color) && imageName3.equals("R.drawable.blank") && imageName4.equals(color))
                        || (imageName.equals(color) && imageName2.equals(color)&& imageName3.equals("R.drawable.blank")))
                {
                    switch (col)
                    {
                        case (5): newLocation = (row+3) * 6; break;
                        case (4): newLocation = ((row+3) * 6) - 1; break;
                        case (3): newLocation = ((row+3) * 6) - 2; break;
                        case (2): newLocation = ((row+3) * 6) - 3; break;
                        case (1): newLocation = ((row+3) * 6) - 4; break;
                        default: newLocation = ((row+3) * 6) - 5;
                    }
                }
                else if (imageName.equals(color) && imageName2.equals(color) && imageName3.equals(color) && imageName4.equals("R.drawable.blank"))
                {
                    switch (col)
                    {
                        case (5): newLocation = (row+4) * 6; break;
                        case (4): newLocation = ((row+4) * 6) - 1; break;
                        case (3): newLocation = ((row+4) * 6) - 2; break;
                        case (2): newLocation = ((row+4) * 6) - 3; break;
                        case (1): newLocation = ((row+4) * 6) - 4; break;
                        default: newLocation = ((row+4) * 6) - 5;
                    }
                }
            }
        }
        return newLocation;
    }

    //sees if theres a strategically-good place to put a marker horizontally
    public int horizontalComputerAnalysis(String color)
    {
        int newLocation = 0;
        for (int row = 0; row < ROWS; row++)
        {
            for (int col = 0; col < COLS - 3; col++)
            {
                String imageName = (String) board[row][col].getTag();
                String imageName2 = (String) board[row][col+1].getTag();
                String imageName3 = (String) board[row][col+2].getTag();
                String imageName4 = (String) board[row][col+3].getTag();
                if ((imageName.equals("R.drawable.blank") && imageName2.equals(color) && imageName3.equals(color) && imageName4.equals(color))
                        || (imageName.equals("R.drawable.blank") && imageName2.equals(color) && imageName3.equals(color)))
                {
                    switch (col)
                    {
                        case (2): newLocation = ((row+1) * 6) - 3; break;
                        case (1): newLocation = ((row+1) * 6) - 4; break;
                        default: newLocation = ((row+1) * 6) - 5;
                    }
                }
                else if ((imageName.equals(color) && imageName2.equals("R.drawable.blank") && imageName3.equals(color) && imageName4.equals(color))
                        || ((imageName.equals(color) && imageName2.equals("R.drawable.blank") && imageName3.equals(color))))
                {
                    switch (col)
                    {
                        case (2): newLocation = ((row+1) * 6) - 2; break;
                        case (1): newLocation = ((row+1) * 6) - 3; break;
                        default: newLocation = ((row+1) * 6) - 4;
                    }
                }
                else if ((imageName.equals(color) && imageName2.equals(color) && imageName3.equals("R.drawable.blank") && imageName4.equals(color))
                        || (imageName.equals(color) && imageName2.equals(color) && imageName3.equals("R.drawable.blank")))
                {
                    switch (col)
                    {
                        case (2): newLocation = ((row+1) * 6) - 1; break;
                        case (1): newLocation = ((row+1) * 6) - 2; break;
                        default: newLocation = ((row+1) * 6) - 3;
                    }
                }
                else if (imageName.equals(color) && imageName2.equals(color) && imageName3.equals(color) && imageName4.equals("R.drawable.blank"))
                {
                    switch (col)
                    {
                        case (2): newLocation = ((row+1) * 6); break;
                        case (1): newLocation = ((row+1) * 6) - 1; break;
                        default: newLocation = ((row+1) * 6) - 2;
                    }
                }
            }
        }
        //System.out.println(newLocation);
        return newLocation;
    }

    //sees if theres a strategically-good place to put a marker upwards-diagonally
    public int upDiagComputerAnalysis(String color)
    {
        int newLocation = 0;
        for (int row = 3; row < ROWS; row++)
        {
            for (int col = 0; col < COLS - 3; col++)
            {
                String imageName = (String) board[row][col].getTag();
                String imageName2 = (String) board[row-1][col+1].getTag();
                String imageName3 = (String) board[row-2][col+2].getTag();
                String imageName4 = (String) board[row-3][col+3].getTag();
                if ((imageName.equals("R.drawable.blank") && imageName2.equals(color) && imageName3.equals(color) && imageName4.equals(color))
                        || (imageName.equals("R.drawable.blank") && imageName2.equals(color) && imageName3.equals(color)))
                {
                    switch (col)
                    {
                        case (2): newLocation = ((row+1) * 6) - 3; break;
                        case (1): newLocation = ((row+1) * 6) - 4; break;
                        default: newLocation = ((row+1) * 6) - 5;
                    }
                }
                else if ((imageName.equals(color) && imageName2.equals("R.drawable.blank") && imageName3.equals(color) && imageName4.equals(color))
                        || (imageName.equals(color) && imageName2.equals("R.drawable.blank") && imageName3.equals(color)))
                {
                    switch (col)
                    {
                        case (2): newLocation = (row * 6) - 2; break;
                        case (1): newLocation = (row * 6) - 3; break;
                        default: newLocation = (row * 6) - 4;
                    }
                }
                else if ((imageName.equals(color) && imageName2.equals(color) && imageName3.equals("R.drawable.blank") && imageName4.equals(color))
                        || (imageName.equals(color) && imageName2.equals(color) && imageName3.equals("R.drawable.blank")))
                {
                    switch (col)
                    {
                        case (2): newLocation = ((row-1) * 6) - 1; break;
                        case (1): newLocation = ((row-1) * 6) - 2; break;
                        default: newLocation = ((row-1) * 6) - 3;
                    }
                }
                else if (imageName.equals(color) && imageName2.equals(color) && imageName3.equals(color) && imageName4.equals("R.drawable.blank"))
                {
                    switch (col)
                    {
                        case (2): newLocation = (((row-2)+1) * 6); break;
                        case (1): newLocation = (((row-2)+1) * 6) - 1; break;
                        default: newLocation = (((row-2)+1) * 6) - 2;
                    }
                }
            }
        }
        //System.out.println(newLocation);
        return newLocation;
    }

    //sees if theres a strategically-good place to put a marker downwards-diagonally
    public int downDiagComputerAnalysis(String color)
    {
        int newLocation = 0;
        for (int row = 0; row < ROWS - 3; row++)
        {
            for (int col = 0; col < COLS - 3; col++)
            {
                String imageName = (String) board[row][col].getTag();
                String imageName2 = (String) board[row+1][col+1].getTag();
                String imageName3 = (String) board[row+2][col+2].getTag();
                String imageName4 = (String) board[row+3][col+3].getTag();
                if ((imageName.equals("R.drawable.blank") && imageName2.equals(color) && imageName3.equals(color) && imageName4.equals(color))
                        || (imageName.equals("R.drawable.blank") && imageName2.equals(color) && imageName3.equals(color)))
                {
                    switch (col)
                    {
                        case (2): newLocation = ((row+1) * 6) - 3; break;
                        case (1): newLocation = ((row+1) * 6) - 4; break;
                        default: newLocation = ((row+1) * 6) - 5;
                    }
                }
                else if ((imageName.equals(color) && imageName2.equals("R.drawable.blank") && imageName3.equals(color) && imageName4.equals(color))
                        || (imageName.equals(color) && imageName2.equals("R.drawable.blank") && imageName3.equals(color)))
                {
                    switch (col)
                    {
                        case (2): newLocation = (((row+1)+1) * 6) - 2; break;
                        case (1): newLocation = (((row+1)+1) * 6) - 3; break;
                        default: newLocation = (((row+1)+1) * 6) - 4;
                    }
                }
                else if ((imageName.equals(color) && imageName2.equals(color) && imageName3.equals("R.drawable.blank") && imageName4.equals(color))
                        || (imageName.equals(color) && imageName2.equals(color) && imageName3.equals("R.drawable.blank")))
                {
                    switch (col)
                    {
                        case (2): newLocation = (((row+2)+1) * 6) - 1; break;
                        case (1): newLocation = (((row+2)+1) * 6) - 2; break;
                        default: newLocation = (((row+2)+1) * 6) - 3;
                    }
                }
                else if (imageName.equals(color) && imageName2.equals(color) && imageName3.equals(color) && imageName4.equals("R.drawable.blank"))
                {
                    switch (col)
                    {
                        case (2): newLocation = (((row+3)+1) * 6); break;
                        case (1): newLocation = (((row+3)+1) * 6) - 1; break;
                        default: newLocation = (((row+3)+1) * 6) - 2;
                    }
                }
            }
        }
        //System.out.println(newLocation);
        return newLocation;
    }
}
