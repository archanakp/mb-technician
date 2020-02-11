package com.battmobile.battmobiletechnician.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.battmobile.battmobiletechnician.home.HomeActivity;
import com.battmobile.battmobiletechnician.R;
import com.battmobile.battmobiletechnician.login.LoginActivity;
import com.battmobile.battmobiletechnician.utility.SessionManager;

public class SplashActivity extends Activity {
    SessionManager sessionManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManage = new SessionManager(SplashActivity.this);
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (sessionManage.isLoggedIn()) {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    } else
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        timerThread.start();
    }
}