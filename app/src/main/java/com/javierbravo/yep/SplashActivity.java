package com.javierbravo.yep;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity {

    public static final int LOADING_COMPLETED = 100;
    protected ProgressBar progressBar = null;
    protected TextView textViewProgress = null;
    protected TextView textTitle;
    protected TextView textSubtitle;
    protected Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textTitle = (TextView) findViewById(R.id.titulo);
        textSubtitle = (TextView) findViewById(R.id.subtitulo);

        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/NexaScript-Free.otf");
        textTitle.setTypeface(myFont);
        textSubtitle.setTypeface(myFont);

        startProgress(handler);
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

                    handler.post(new Runnable() {
                        public void run() {
                            finalProgressBar.setProgress(progressStatus[0]);
                            finalTextView.setText(progressStatus[0] + "/" + finalProgressBar.getMax());
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    protected void checkUser() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
