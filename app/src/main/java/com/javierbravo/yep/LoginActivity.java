package com.javierbravo.yep;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final int LOADING_COMPLETED = 100;
    protected EditText username, password;
    protected Button logIn;
    protected TextView mSignUpTextView;

    protected ProgressBar progressBar = null;
    protected TextView textViewProgress = null;
    protected TextView textTitle;
    protected TextView textSubtitle;
    protected Handler handler = new Handler();


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
        // Inflate the menu; this adds items to the action bar if it is present..
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

        buttonAnimationHide(logIn);

        ParseUser.logInInBackground(usr, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    mSignUpTextView.setVisibility(View.INVISIBLE);
                    startProgress(handler);
                } else {
                    String wrongLogInTitle = getResources().getString(R.string.user_title);
                    String wrongLogInMessage = getResources().getString(R.string.wrong_login_message);
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(wrongLogInTitle)
                            .setMessage(wrongLogInMessage)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    buttonAnimationShow(logIn);
                                }
                            }).show();
                }
            }
        });
    }

    protected void hiddingKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        //check if no view has focus:
        View view = this.getCurrentFocus();
        if (view == null)
            return;

        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void buttonAnimationHide(Button logIn) {
        Animation btnRight = AnimationUtils.loadAnimation(this, R.anim.button_anim_bounce_right);
        logIn.startAnimation(btnRight);
    }

    protected void checkUser() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }
    protected void buttonAnimationShow(Button logIn) {
        Animation btnLeft = AnimationUtils.loadAnimation(this,R.anim.button_anim_bounce_left);
        logIn.startAnimation(btnLeft);
    }

    protected void startProgress(final Handler handler) {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        textViewProgress = (TextView) findViewById(R.id.progressText);

        final int[] progressStatus = {0};
        final TextView finalTextView = textViewProgress;
        final ProgressBar finalProgressBar = progressBar;

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus[0] <= 100) {
                    progressStatus[0] += 10;
                    if(progressStatus[0] == LOADING_COMPLETED)
                        checkUser();

                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            finalProgressBar.setProgress(progressStatus[0]);
                            finalTextView.setText(progressStatus[0] + "/" + finalProgressBar.getMax());
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        //Just to display the progress slowly
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
