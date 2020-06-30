package com.example.pawfinder.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.pawfinder.R;

public class PrefConfig {

    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefConfig(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status), status);
        editor.commit();

    }

    public void writePetsName(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_pet_name), name);
        editor.commit();
    }

    //metoda za citanje login statusa iz sharedPreferences
    public boolean readLoginStatus() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status), false);
    }

    //metoda za cuvanje email-a user-a nakon uspesne prijave

    public void writeUserEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_email), email);
        editor.commit();
    }

    public String readUserEmail() {
        return sharedPreferences.getString(context.getString(R.string.pref_user_email), "User");
    }

    public boolean logout() {
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.pref_user_email), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        this.writeLoginStatus(false);

        return true;
    }

    public boolean deleteReportData()
    {
        SharedPreferences sp1 = context.getSharedPreferences(context.getString(R.string.pref_pet_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp1.edit();
        editor1.clear();
        editor1.apply();
        editor1.commit();
//        SharedPreferences sp1 = context.getSharedPreferences("petGender", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor1 = sp1.edit();
//        editor1.clear();
//        editor1.commit();
//
//        SharedPreferences sp2 = context.getSharedPreferences("petType", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor2 = sp2.edit();
//        editor2.clear();
//        editor2.commit();
//
//        SharedPreferences sp3 = context.getSharedPreferences("petDate", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor3 = sp3.edit();
//        editor3.clear();
//        editor3.commit();

        return true;
    }

    public void saveMissingReportsFirstPage(String name, int gender, int type, String date){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("petName", name);
        editor.putInt("petGender", gender);
        editor.putInt("petType", type);
        editor.putString("petDate", date);
        editor.commit();
    }

    public String readPetsName()
    {
        return sharedPreferences.getString("petName", "");
    }

    public int readPetsGender()
    {
        return sharedPreferences.getInt("petGender", 0);
    }

    public int readPetsType()
    {
        return sharedPreferences.getInt("petType", 0);
    }

    public String readPetsMissingDate()
    {
        return sharedPreferences.getString("petDate", "");
    }


}

