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

        ParseObject user = new ParseObject("Users");

        user.put("name", "Manu");
        user.put("password", "1234");
        user.put("email", "magno@hotmilio.com");

        user.saveInBackground();
    }
}
