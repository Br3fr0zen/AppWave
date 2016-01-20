package com.javierbravo.yep;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText email;
    private String usr = "";
    private String pass = "";
    private String em = "";
    protected TextView mLogInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        username = (EditText) findViewById(R.id.usernameField);
        password = (EditText) findViewById(R.id.passwordField);
        email = (EditText) findViewById(R.id.emailField);

        usr = username.getText().toString();
        pass = password.getText().toString();
        em = email.getText().toString();

        mLogInTextView = (TextView) findViewById(R.id.signupButton);
        mLogInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpClick();
            }
        });
    }

    protected void signUpClick(){
        ParseUser newUser = new ParseUser();

        newUser.setUsername(usr);
        newUser.setPassword(String.valueOf(pass));
        newUser.setEmail(String.valueOf(em));

        newUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast2 =
                            Toast.makeText(getApplicationContext(),
                                    "Username or email already in use, please introduce a new one", Toast.LENGTH_SHORT);
                    toast2.show();
                }
            }
        });
    }
}
