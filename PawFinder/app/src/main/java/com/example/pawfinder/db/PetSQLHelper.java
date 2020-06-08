package com.example.pawfinder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


public class PetSQLHelper extends SQLiteOpenHelper {

    //Dajemo ime bazi
    private static final String DATABASE_NAME = "pma.db";
    //i pocetnu verziju baze. Obicno krece od 1
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PET = "petdb";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SERVER_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_ADDITIONALINFO = "additionalInfo";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_MISSINGSINCE = "missingSince";
    public static final String COLUMN_OWNERSPHONE = "ownersPhone";
    public static final String COLUMN_ISFOUND = "isFound";
    public static final String COLUMN_USER = "user";            //PISACU OVDE EMAIL USERA
    public static final String COLUMN_LON = "lon";              //lon i lat umesto adrese
    public static final String COLUMN_LAT = "lat";

    public static final String COLUMN_SYNCSTATUS = "syncstatus"; //za sinhronizaciju kada se upali net


    private static final String DB_CREATE = "create table "
            + TABLE_PET + "("
            + COLUMN_ID  + " integer primary key autoincrement , "
            + COLUMN_NAME + " text, "
            + COLUMN_TYPE + " text, "
            + COLUMN_GENDER + " text, "
            + COLUMN_ADDITIONALINFO + " text, "
            + COLUMN_IMAGE + " text, "
            + COLUMN_MISSINGSINCE + " text, "
            + COLUMN_OWNERSPHONE + " text, "
            + COLUMN_USER + " text, "
            + COLUMN_ISFOUND + " text, "
            + COLUMN_LAT + " text, "
            + COLUMN_LON + " text, "
            + COLUMN_SYNCSTATUS + " text, "
            + COLUMN_SERVER_ID + "text"
            + ")";

    private static final String DB_DROP = "drop table if exists " + TABLE_PET;

    public PetSQLHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("onCreate", "Meesage recieved");
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DB_DROP);
        onCreate(db);
    }


}
