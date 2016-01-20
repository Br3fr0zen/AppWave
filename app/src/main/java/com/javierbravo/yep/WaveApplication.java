package com.javierbravo.yep;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Javier Bravo on 14/01/2016.
 */
public class WaveApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
    }
}
