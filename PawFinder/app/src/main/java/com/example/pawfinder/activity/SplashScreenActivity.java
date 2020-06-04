package com.example.pawfinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;
import com.example.pawfinder.tools.PrefConfig;
import com.example.pawfinder.tools.LocaleUtils;
import com.example.pawfinder.tools.ThemeUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {

    private static PrefConfig prefConfig;
    private ThemeUtils themeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        themeUtils = new ThemeUtils(sharedPreferences, this);
        themeUtils.setTheme();
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.splash);
        setTitle(R.string.app_name);
        int SPLASH_TIME_OUT = 1000;
        //zakomentarisala da mi se 2 puta ne bi otvarao maain
        /*new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish(); // da ne bi mogao da ode back na splash
            }
        }, SPLASH_TIME_OUT);*/

        prefConfig = new PrefConfig(this);

        if (prefConfig.readLoginStatus()) //
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();
        } else {
            setContentView(R.layout.splash);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish(); // da ne bi mogao da ode back na splash
                }
            }, SPLASH_TIME_OUT);
        }

    }
}

