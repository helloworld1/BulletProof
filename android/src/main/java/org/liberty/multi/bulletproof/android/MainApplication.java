package org.liberty.multi.bulletproof.android;

import android.app.Application;

import com.google.android.gms.games.PlayGamesSdk;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PlayGamesSdk.initialize(this);
    }
}
