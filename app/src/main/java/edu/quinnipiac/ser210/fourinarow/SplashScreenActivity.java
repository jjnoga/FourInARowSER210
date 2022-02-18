package edu.quinnipiac.ser210.fourinarow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
Class: SplashScreenActivity.java
Author: Joseph Noga
Professor: Rehab ElKharboutly
Class: SER210
Date: 2/18/2022
Description: Presents the introductory screen that is summoned when the app is first opened. Contains
the rules and the ability to input a username, as well as the start button.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private String value;

    //used when activity is first summoned
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (savedInstanceState != null)
        {
            value = savedInstanceState.getString("value");
        }
    }

    //used for the start button; sends an intent to the GameBoardActivity
    public void onClick(View view) {
        EditText text = (EditText) findViewById(R.id.inputName);
        value = text.getText().toString();

        if (text.length() > 0 && !text.equals(null))
        {
            Intent intent = new Intent(this, GameBoardActivity.class);
            intent.putExtra("playerName", value);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Please enter a name.", Toast.LENGTH_LONG).show();
        }
    }

    //to ensure that the inputted name is not lost upon changing the orientation of the phone
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("value", value);
    }
}