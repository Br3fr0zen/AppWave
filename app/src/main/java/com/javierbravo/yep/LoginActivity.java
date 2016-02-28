package com.javierbravo.yep;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    protected EditText username, password;
    protected Button logIn;
    protected TextView mSignUpTextView;

    protected TextView textTitle;
    protected TextView textSubtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textTitle = (TextView) findViewById(R.id.titulo);
        textSubtitle = (TextView) findViewById(R.id.subtitulo);

        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/NexaScript-Free.otf");
        textTitle.setTypeface(myFont);
        textSubtitle.setTypeface(myFont);

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
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        return super.onPrepareOptionsMenu(menu);
    }

    private void logInClick() {
        final String usr = username.getText().toString();
        final String pass = password.getText().toString();
        hiddingKeyboard();

        ParseUser.logInInBackground(usr, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    mSignUpTextView.setVisibility(View.INVISIBLE);
                    checkUser();
                } else {
                    errorMessage();

                }
            }
        });
    }

    protected void hiddingKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = this.getCurrentFocus();
        if (view == null)
            return;

        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void checkUser() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    private void errorMessage() {
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
