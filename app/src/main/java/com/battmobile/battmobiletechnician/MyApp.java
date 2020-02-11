package com.battmobile.battmobiletechnician;

import android.app.Application;
import android.content.Context;


import timber.log.Timber;

public class MyApp extends Application {
    public static MyApp context;
    public static Context mContext;

    public static MyApp getInstance() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mContext = getApplicationContext();
        Timber.plant(new Timber.DebugTree());
    }


}
