package com.example.pawfinder.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.pawfinder.MainActivity;

import java.util.Locale;

public class ThemeUtils {

    private SharedPreferences sharedPreferences;
    private Context context;


    public ThemeUtils(SharedPreferences sharedPreferences, Context context) {
        super();
        this.sharedPreferences=sharedPreferences;
        this.context = context;
    }

    public void setTheme(){
        String themeString = sharedPreferences.getString("theme","white");
        if(themeString.equals("white")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
    public void saveTheme(String lang) {
        String langPref = "theme";

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }
}
