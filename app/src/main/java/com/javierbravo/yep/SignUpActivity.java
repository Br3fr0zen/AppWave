package com.javierbravo.yep;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseObject;

public class SignUpActivity extends AppCompatActivity {

    private int score;
    private int playerName;
    private int cheatMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        ParseObject gameScore = new ParseObject("GameScore");

        gameScore.put("score", 1337);
        gameScore.put("playerName", "Sean Plott");
        gameScore.put("cheatMode", false);

        gameScore.saveInBackground();
    }
}
