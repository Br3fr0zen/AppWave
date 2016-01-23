package com.javierbravo.yep;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String EMAIL_VERIFICATION = "/^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$/";
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

        final String usr = username.getText().toString().replace(" ", "");
        final String pass = password.getText().toString().replace(" ", "");
        final String em = email.getText().toString().replace(" ", "");

        final ParseUser newUser = new ParseUser();

        //Method to set dataa.
        trimSpaces(newUser, usr, pass, em);

        if(ParseUser.getCurrentUser()!=null) {
            Log.d("prueba", "logueado con el usuario: usuario" + ParseUser.getCurrentUser().getUsername());
            ParseUser.logOut();
        }
        newUser.signUpInBackground(new SignUpCallback() {
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if ((usr.isEmpty() || pass.isEmpty()) || em.isEmpty()) {
                    String emptyTitle = getResources().getString(R.string.empty_title);
                    String emptyMessage = getResources().getString(R.string.empty_field_message);
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle(emptyTitle)
                            .setMessage(emptyMessage)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else if (!em.contentEquals(EMAIL_VERIFICATION)) {
                    String userTitle = getResources().getString(R.string.user_title);
                    String userMessage = getResources().getString(R.string.email_fail_message);
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle(userTitle)
                            .setMessage(userMessage)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
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



