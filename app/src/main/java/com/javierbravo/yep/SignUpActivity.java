package com.javierbravo.yep;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    protected EditText username, password, email;
    protected Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        username = (EditText) findViewById(R.id.usernameField);
        password = (EditText) findViewById(R.id.passwordField);
        email = (EditText) findViewById(R.id.emailField);

        btnSignUp = (Button) findViewById(R.id.signupButton);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpClick();
            }
        });
    }

    protected void signUpClick(){

        String usr = username.getText().toString().replace(" ", "");
        String pass = password.getText().toString().replace(" ", "");
        String em = email.getText().toString().replace(" ", "");

        final ParseUser newUser = new ParseUser();

        //Method to set data.
        trimSpaces(newUser, usr, pass, em);

        Log.d("prueba", "string:" + newUser.getUsername());
        Log.d("prueba", "string:" + newUser.getEmail());

        if(ParseUser.getCurrentUser()!=null) {
            Log.d("prueba", "logueado con el usuario: usuario" + ParseUser.getCurrentUser().getUsername());
            ParseUser.logOut();
        }
        newUser.signUpInBackground(new SignUpCallback() {
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast =
                            Toast.makeText(getApplicationContext(),
                                    "Sign up failed", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    protected ParseUser trimSpaces(ParseUser newUser, String usr, String pass, String em){
        newUser.setUsername(usr);
        newUser.setPassword(pass);
        newUser.setEmail(em);
        return newUser;
    }
}
