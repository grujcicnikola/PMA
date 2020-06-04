package com.example.pawfinder.sync;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.pawfinder.db.DBContentProvider;
import com.example.pawfinder.db.PetSQLHelper;
import com.example.pawfinder.model.Address;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.NetworkTool;
import com.example.pawfinder.tools.PrefConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetSqlSync {

    private static PrefConfig prefConfig;

    //fill sqlite with server data
    public static void fillDatabase(ArrayList<Pet> listPets, Activity activity, int fleg) {
        PetSQLHelper dbHelper = new PetSQLHelper(activity);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        {
            for (Pet p : listPets) {
                ContentValues entry = new ContentValues();
                fillContent(p, entry);
                if (fleg == 3) {            //MissingReport third page
                    entry.put(PetSQLHelper.COLUMN_SYNCSTATUS, "false");
                }else{
                    entry.put(PetSQLHelper.COLUMN_SYNCSTATUS, "true");
                }

                if (p.getId() == null) {
                    activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_PET, entry);
                }else{
                    if (activity.getContentResolver().update(Uri.parse(DBContentProvider.CONTENT_URI_PET+"/" + p.getId()), entry,  "", null) == 0){
                        activity.getContentResolver().insert(DBContentProvider.CONTENT_URI_PET, entry);
                    }
                }


            }
        }

        db.close();
    }

    public static void sendUnsaved(final Activity activity){
        String[] allColumns = { PetSQLHelper.COLUMN_ID,
                PetSQLHelper.COLUMN_NAME, PetSQLHelper.COLUMN_TYPE, PetSQLHelper.COLUMN_GENDER,
                PetSQLHelper.COLUMN_ADDITIONALINFO, PetSQLHelper.COLUMN_IMAGE, PetSQLHelper.COLUMN_MISSINGSINCE,
                PetSQLHelper.COLUMN_OWNERSPHONE, PetSQLHelper.COLUMN_ISFOUND, PetSQLHelper.COLUMN_USER,
                PetSQLHelper.COLUMN_LON, PetSQLHelper.COLUMN_LAT, PetSQLHelper.COLUMN_SYNCSTATUS};
        Cursor cursor = activity.getContentResolver().query(DBContentProvider.CONTENT_URI_PET, allColumns, null, null,
                null);

        int brojac = 0;
        List<Long> ids = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            Pet c = new Pet();
            for (int i = 0; i < cursor.getCount(); i++) {
                Boolean isSent = Boolean.valueOf(cursor.getString(12));
                if (isSent != null) {
                    if (isSent == false) {
                        ++brojac;
                        Long id = Long.parseLong(cursor.getString(0));
                        PetType type = PetType.valueOf(cursor.getString(2));
                        String name = cursor.getString(1);
                        PetGender gender = PetGender.valueOf(cursor.getString(3));
                        String additional = cursor.getString(4);
                        String image = cursor.getString(5);
                        String missingSince = cursor.getString(6);
                        String ownersPhone = cursor.getString(7);
                        boolean isFound = Boolean.valueOf(cursor.getString(9));

                        //nece lepo iz sqlite da procita email
                        User userObject = new User();
                        String user = "";
                        user = cursor.getString(8);
                        userObject.setEmail(user);
                        prefConfig = new PrefConfig(activity.getApplicationContext());

                        if (prefConfig.readLoginStatus()) {
                            userObject.setEmail(prefConfig.readUserEmail());
                        }

                        Address address = new Address(Double.parseDouble(cursor.getString(10)), Double.parseDouble(cursor.getString(11)));


                        Log.d("petList SYNC", "ima ih" + " " + type + " " + name + " " + user);

                        c = new Pet(type, name, gender, additional, image, missingSince, ownersPhone, isFound, userObject, address, isSent);
                        c.setSent(true);       //otisao na back

                        Call<Pet> call = ServiceUtils.petService.postMissing(c);
                        Log.d("PETSSYNC", "usao");
                        call.enqueue(new Callback<Pet>() {
                            @Override
                            public void onResponse(Call<Pet> call, Response<Pet> response) {
                                Log.d("PETADDSYNC", "ima ih" + response.body());
                                if (response.code() == 200) {
                                } else {
                                }
                            }
                            @Override
                            public void onFailure(Call<Pet> call, Throwable t) {
                                Log.d("PETADDSYNC", t.getMessage() != null ? t.getMessage() : "error");
                            }
                        });
                    }

                    cursor.moveToNext();
                }
            }
            // always close the cursor
            cursor.close();
        }

        Log.d("BROJAC", "brojac " + brojac);
        if (brojac>0) {
            //obrisem sada celu
            //activity.getContentResolver().delete(DBContentProvider.CONTENT_URI_PET, null, null);
       }


    }

    public static ContentValues fillContent(Pet p, ContentValues entry){
        entry.put(PetSQLHelper.COLUMN_NAME, p.getName());
        entry.put(PetSQLHelper.COLUMN_TYPE, p.getType().toString());
        entry.put(PetSQLHelper.COLUMN_GENDER, p.getGender().toString());
        entry.put(PetSQLHelper.COLUMN_ADDITIONALINFO, p.getAdditionalInfo());
        entry.put(PetSQLHelper.COLUMN_IMAGE, p.getImage());
        entry.put(PetSQLHelper.COLUMN_MISSINGSINCE, p.getMissingSince());
        entry.put(PetSQLHelper.COLUMN_OWNERSPHONE, p.getOwnersPhone());
        entry.put(PetSQLHelper.COLUMN_ISFOUND, p.isFound());
        entry.put(PetSQLHelper.COLUMN_USER, p.getUser().getEmail());
        entry.put(PetSQLHelper.COLUMN_LON, p.getAddress().getLon());
        entry.put(PetSQLHelper.COLUMN_LAT, p.getAddress().getLat());
        entry.put(PetSQLHelper.COLUMN_USER, p.getUser().getEmail());
        return entry;
    }
}
