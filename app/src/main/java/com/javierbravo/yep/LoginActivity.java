package com.javierbravo.yep;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    protected EditText username, password;
    protected Button logIn;
    protected TextView mSignUpTextView;
    protected MenuItem miActionProgressItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.UserMainField);
        password = (EditText) findViewById(R.id.PasswordField);
        logIn = (Button) findViewById(R.id.LoginButton);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInClick();
            }
        });

        getSupportActionBar().hide();

        mSignUpTextView = (TextView)findViewById(R.id.SignUpText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        //Store instance of the menu item containing progress.
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        //Extract the action-view from the menu item.
        ProgressBar v = (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        //Return to finish.
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar(){
        //Show progress item.
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar(){
        //Hide progress item.
        try {
            miActionProgressItem.wait(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        miActionProgressItem.setVisible(false);
    }
    private void logInClick() {
        showProgressBar();
        final String usr = username.getText().toString();
        final String pass = password.getText().toString();

        ParseUser.logInInBackground(usr, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    hideProgressBar();
                }else{
                    String wrongLogInTitle = getResources().getString(R.string.user_title);
                    String wrongLogInMessage = getResources().getString(R.string.wrong_login_message);
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(wrongLogInTitle)
                            .setMessage(wrongLogInMessage)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });
    }
}
