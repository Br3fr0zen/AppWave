package com.javierbravo.yep;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class RecipientsActivity extends AppCompatActivity {

    public MenuItem mSendMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container1, new RecipientsFragment())
                    .commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        mSendMenuItem =  menu.getItem(0);
        setmSendMenuItem(mSendMenuItem);
        return true;
    }

    public void setmSendMenuItem(MenuItem mSendMenuItem) {
        this.mSendMenuItem = mSendMenuItem;
    }

    public MenuItem getmSendMenuItem() {
        return mSendMenuItem;
    }
}
