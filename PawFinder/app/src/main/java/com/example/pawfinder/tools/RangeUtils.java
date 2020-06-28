package com.example.pawfinder.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pawfinder.R;
import com.example.pawfinder.service.MyFirebaseInstanceService;

public class RangeUtils {

    private SharedPreferences sharedPreferences;
    private Context context;

    public RangeUtils(SharedPreferences sharedPreferences, Context context) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    public void setRange() {
        Integer range = sharedPreferences.getInt("range_number", 2);
        saveRange(range);
    }

    public void saveRange(Integer range) {
        String pref = "range_number";

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(pref, range);
        editor.commit();

    }

    //metoda za citanje login statusa iz sharedPreferences
    public int readRange() {
        return sharedPreferences.getInt("range_number", 2);
    }
}
