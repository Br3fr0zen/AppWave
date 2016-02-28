package com.javierbravo.yep;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String EMAIL_VERIFICATION = "/^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$/";
    protected EditText username, password, email;
    protected Button btnSignUp, btnCancel;
    protected TextView textTitle;
    protected TextView textSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        textTitle = (TextView) findViewById(R.id.titulo);
        textSubtitle = (TextView) findViewById(R.id.subtitulo);

        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/NexaScript-Free.otf");
        textTitle.setTypeface(myFont);
        textSubtitle.setTypeface(myFont);

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

        btnCancel = (Button) findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void signUpClick() {

        final String usr = username.getText().toString().replace(" ", "");
        final String pass = password.getText().toString().replace(" ", "");
        final String em = email.getText().toString().replace(" ", "");

        hiddingKeyboard();

        final ParseUser newUser = new ParseUser();

        trimSpaces(newUser, usr, pass, em);

        newUser.signUpInBackground(new SignUpCallback() {
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    checkUser();
                } else if ((usr.isEmpty() || pass.isEmpty()) || em.isEmpty()) {
                    errorEmptyFields();

                } else if (!em.contentEquals(EMAIL_VERIFICATION)) {
                    errorEmailVerification();
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
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    protected ParseUser trimSpaces(ParseUser newUser, String usr, String pass, String em) {
        newUser.setUsername(usr);
        newUser.setPassword(pass);
        newUser.setEmail(em);
        return newUser;
    }

    private void errorEmailVerification() {
        String userTitle = getResources().getString(R.string.user_title);
        String userMessage = getResources().getString(R.string.email_fail_message);
        new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(userTitle)
                .setMessage(userMessage)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private void errorEmptyFields() {
        String emptyTitle = getResources().getString(R.string.empty_title);
        String emptyMessage = getResources().getString(R.string.empty_field_message);
        new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(emptyTitle)
                .setMessage(emptyMessage)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}



