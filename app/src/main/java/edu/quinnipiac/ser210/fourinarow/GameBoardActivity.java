package edu.quinnipiac.ser210.fourinarow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
Class: GameBoardActivity.java
Author: Joseph Noga
Professor: Rehab ElKharboutly
Class: SER210
Date: 2/18/2022
Description: Provides the framework and operations of the four-in-a-row game, complete with changing
the appearance of the grid spaces via usage of buttons, to the ability to call the FourInARow class
so that the computer can make decisions.
 */

public class GameBoardActivity extends AppCompatActivity {

    //instance variables
    private List<ImageButton> buttons;
    private int playerIcon, cpuIcon, currIcon, currState, validMoveFlag, validCPUFlag, cpuLocation, winsUser, winsCPU;
    private FourInARow firBoard;
    //private TextView statusText;
    private String name, userWon, cpuWon, tie;
    private TextView statusText;
    private static final int[] BUTTON_IDS = {
            R.id.button0,
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
            R.id.button10,
            R.id.button11,
            R.id.button12,
            R.id.button13,
            R.id.button14,
            R.id.button15,
            R.id.button16,
            R.id.button17,
            R.id.button18,
            R.id.button19,
            R.id.button20,
            R.id.button21,
            R.id.button22,
            R.id.button23,
            R.id.button24,
            R.id.button25,
            R.id.button26,
            R.id.button27,
            R.id.button28,
            R.id.button29,
            R.id.button30,
            R.id.button31,
            R.id.button32,
            R.id.button33,
            R.id.button34,
            R.id.button35
    };

    //called when activity is first summoned
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        //carried over from restart/splash screen intents
        name = getIntent().getStringExtra("playerName");
        winsUser = getIntent().getIntExtra("playerWins", 0);
        winsCPU = getIntent().getIntExtra("cpuWins", 0);
        statusText = (TextView) findViewById(R.id.userWins);

        //displays how many wins you and the cpu have
        statusText.setText(name + ": " + winsUser + " win(s)\nCPU: " + winsCPU + " win(s)");

        firBoard = new FourInARow(BUTTON_IDS, this);

        currState = IGame.PLAYING;
        final CharSequence[] items = {"Red", "Blue"};

        //allows you to select which symbol you want to represent: blue x or red o
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 1:
                        playerIcon = R.drawable.bluex;
                        cpuIcon = R.drawable.redo;
                        currIcon = playerIcon;
                        firBoard.setPlayerColor("R.drawable.bluex");
                        firBoard.setCPUColor("R.drawable.redo");
                        dialog.dismiss();
                        break;
                    default:
                        playerIcon = R.drawable.redo;
                        cpuIcon = R.drawable.bluex;
                        currIcon = playerIcon;
                        firBoard.setPlayerColor("R.drawable.redo");
                        firBoard.setCPUColor("R.drawable.bluex");
                        dialog.dismiss();
                }
            }
        });
        AlertDialog alert = builder.create();
        if (savedInstanceState == null)
        {
            alert.show();
        }

        //used to give function to the buttons so that they can represent the symbols you/the cpu place
        buttons = new ArrayList<ImageButton>();
        for (int id : BUTTON_IDS) {
            ImageButton button = (ImageButton) findViewById(id);
            button.setImageResource(R.drawable.blank);
            button.setTag("R.drawable.blank");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setSelected(!button.isSelected());

                    String imageName = (String) button.getTag();

                    if (button.isSelected() && imageName.equals("R.drawable.blank")) {
                        button.setImageResource(currIcon);
                        if (currIcon == R.drawable.bluex) { button.setTag("R.drawable.bluex"); }
                        else button.setTag("R.drawable.redo");
                        validMoveFlag++;
                    }

                    //imageName = (String) button.getTag();
                    //statusText.setText(imageName);

                    currState = firBoard.checkForWinner();
                    runResults();


                    if (validMoveFlag == 1 && currState == IGame.PLAYING) {
                        validCPUFlag = 0;
                        while (validCPUFlag < 1)
                        {
                            cpuLocation = firBoard.getComputerMove();

                            //int random = (int) (Math.random() * 36);
                            //cpuLocation = random - 1;
                            firBoard.setMove(cpuIcon, cpuLocation);
                            //statusText.setText("" + cpuLocation);
                            if (firBoard.legalMoveCheck() == true) {
                                validCPUFlag++;
                            }
                        }
                    }

                    validMoveFlag = 0;
                    validCPUFlag = 0;

                    currState = firBoard.checkForWinner();
                    runResults();
                }
            });
            buttons.add(button);
        }

        //does its best to keep information across changing screen orientation
        if (savedInstanceState != null)
        {
            playerIcon = savedInstanceState.getInt("playerIcon");
            cpuIcon = savedInstanceState.getInt("cpuIcon");
            currState = savedInstanceState.getInt("currState");
            currIcon = playerIcon;
            winsUser = savedInstanceState.getInt("winsUser");
            winsCPU = savedInstanceState.getInt("winsCPU");

            if (playerIcon == R.drawable.bluex) {
                firBoard.setPlayerColor("R.drawable.bluex");
                firBoard.setCPUColor("R.drawable.redo");
            }
            else
            {
                firBoard.setPlayerColor("R.drawable.redo");
                firBoard.setCPUColor("R.drawable.bluex");
            }

            for (int i = 0; i < 36; i++)
            {
                String tag = savedInstanceState.getString("button" + i + "State");
                buttons.get(i).setTag(tag);
                String currentTag = (String) buttons.get(i).getTag();
                if (currentTag.equals("R.drawable.bluex")) { buttons.get(i).setImageResource(R.drawable.bluex); }
                else if (currentTag.equals("R.drawable.redo")) { buttons.get(i).setImageResource(R.drawable.redo); }
            }

        }
    }

    //saves some variable values in the event the screen orientation shifts
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("playerIcon", playerIcon);
        outState.putInt("cpuIcon", cpuIcon);
        outState.putInt("currState", currState);
        outState.putInt("winsUser", winsUser);
        outState.putInt("winsCPU", winsCPU);

        for (int i = 0; i < 36; i++)
        {
            outState.putString("button" + i + "State", (String) buttons.get(i).getTag());
        }

    }

    //used to determine is anyone met the conditions to win
    private void runResults() {
        if (currState != IGame.PLAYING) {
            userWon = name + " won! Congrats! :)";
            cpuWon = "Too bad! The computer won. :(";
            tie = "Seems there was a tie...";
            switch (currState) {
                case FourInARow.BLUE_WON: {
                    if(playerIcon == R.drawable.bluex) {
                        winAlert(userWon);
                    }
                    else if (cpuIcon == R.drawable.bluex) {
                        winAlert(cpuWon);
                    }
                    break;
                }
                case FourInARow.RED_WON: {
                    if (playerIcon == R.drawable.redo) {
                        winAlert(userWon);
                    } else if (cpuIcon == R.drawable.redo) {
                        winAlert(cpuWon);
                    }
                    break;
                }
                case FourInARow.TIE: {
                    winAlert(tie);
                    break;
                }
            }
        }
    }

    //pop-up alert to display who wins and present the option to play again or quit
    //@param winner - a special message that changes depending on who won
    private void winAlert(String winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(winner + "\nWould you like to go another round?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = getIntent();
                if (winner.equals(userWon)) { intent.putExtra("playerWins", winsUser + 1); }
                else if (winner.equals(cpuWon)) { intent.putExtra("cpuWins", winsCPU + 1); }
                finish();
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

