package com.example.pawfinder.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleUtils {

    private Locale locale;
    private SharedPreferences sharedPreferences;
    private Context context;

    public LocaleUtils(SharedPreferences sharedPreferences, Context context) {
        super();
        this.sharedPreferences=sharedPreferences;
        this.context = context;
    }



    public void setLocale(){
        String localeString = sharedPreferences.getString("language","en");
        locale = new Locale(localeString);
        Locale.setDefault(locale);
        saveLocale(localeString);
        // Create a new configuration object
        Configuration config = new Configuration();
        // Set the locale of the new configuration
        config.locale = locale;
        // Update the configuration of the Application context
        context.getResources().updateConfiguration(
                config,
                context.getResources().getDisplayMetrics()
        );
        //finish();
        //startActivity(getIntent());
    }
    public void saveLocale(String lang) {
        String langPref = "language";

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }
}