package com.example.pawfinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        int SPLASH_TIME_OUT = 1000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish(); // da ne bi mogao da ode back na splash
            }
        }, SPLASH_TIME_OUT);
    }
}
