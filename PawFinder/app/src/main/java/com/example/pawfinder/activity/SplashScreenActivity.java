package com.example.pawfinder.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import org.json.JSONException;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;
import com.example.pawfinder.tools.PrefConfig;
import com.example.pawfinder.tools.LocaleUtils;
import com.example.pawfinder.tools.ThemeUtils;

import org.json.JSONObject;

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


        prefConfig = new PrefConfig(this);

        if (prefConfig.readLoginStatus()) //
        {
            setContentView(R.layout.splash);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    if(getIntent()!=null){
                        Bundle bundle = getIntent().getExtras();
                        if (bundle != null) {
                            buildABackstack(bundle);
                            finish();


                        }
                    }
                    finish(); // da ne bi mogao da ode back na splash
                }
            }, SPLASH_TIME_OUT);

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

    public void buildABackstack(Bundle bundle){
        Intent resultIntent = new Intent(this, ReportDetailActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        if(bundle.getString("name")!=null) {
            resultIntent.putExtra("report_pet_name", bundle.getString("name"));
            resultIntent.putExtra("report_pet_type", bundle.getString("type"));
            resultIntent.putExtra("report_pet_date", bundle.getString("missing_since"));
            resultIntent.putExtra("report_pet_image", bundle.getString("image"));
            resultIntent.putExtra("report_pet_additionalInfo", bundle.getString("additionalInfo"));
            resultIntent.putExtra("report_pet_of_pet", bundle.getString("id"));

            Intent secondIntent = new Intent(this, ViewCommentsActivity.class);
            secondIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            secondIntent.putExtra("view_comments_petsName", bundle.getString("name"));
            secondIntent.putExtra("view_comments_additionalInfo", bundle.getString("additionalInfo"));
            secondIntent.putExtra("view_comments_id", Long.valueOf(bundle.getString("id")));
            secondIntent.putExtra("view_comments_image", bundle.getString("image"));
            ;
            //Log.d("PETSIMAAGE", pets.get(position).getImage());
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(ReportDetailActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            stackBuilder.addNextIntent(secondIntent);
            // Get the PendingIntent containing the entire back stack
            stackBuilder.startActivities();
        }
    }
}

