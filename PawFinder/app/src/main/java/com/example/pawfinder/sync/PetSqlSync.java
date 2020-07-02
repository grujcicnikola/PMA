package com.example.pawfinder.sync;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pawfinder.R;
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

    //fill sqlite with server data
    public static void fillDatabase(ArrayList<Pet> listPets, Context context, int fleg) {
       /* if (fleg == 0 && listPets.size()>0) {
            activity.getContentResolver().delete(DBContentProvider.CONTENT_URI_PET, null, null);
        }*/

           PetSQLHelper dbHelper = new PetSQLHelper(context);
           SQLiteDatabase db = dbHelper.getWritableDatabase();
           int all = 0;

           {
               for (Pet p : listPets) {
                   all++;
                   ContentValues entry = new ContentValues();
                   fillContent(p, entry);
                   if (fleg == 3) {            //MissingReport third page
                       entry.put(PetSQLHelper.COLUMN_SYNCSTATUS, "false");
                   } else {
                       entry.put(PetSQLHelper.COLUMN_SYNCSTATUS, "true");
                   }

                   //nema id znaci nije sa servera-nego dodat offline
                   if (p.getId() == null) {
                       Log.d("fillDatabase", p.getName() + " null");
                       context.getContentResolver().insert(DBContentProvider.CONTENT_URI_PET, entry);
                   } else {
                       Log.d("fillDatabase", p.getName() + " else");
                       if (context.getContentResolver().update(Uri.parse(DBContentProvider.CONTENT_URI_PET + "/" + p.getId()), entry, "", null) == 0) {
                           entry.put(PetSQLHelper.COLUMN_ID, p.getId().toString());
                           Log.d("fillDatabase", p.getName() + " insert " + p.getId());
                           context.getContentResolver().insert(DBContentProvider.CONTENT_URI_PET, entry);
                       }
                   }

                   if (fleg == 3) {
                       break;
                   }

               }
           }

           db.close();

    }

    public static void sendUnsaved(final Activity activity){
        //potrebno prilakoditi datume ako ovo omogucimo

        String[] allColumns = { PetSQLHelper.COLUMN_ID,
                PetSQLHelper.COLUMN_NAME, PetSQLHelper.COLUMN_TYPE, PetSQLHelper.COLUMN_GENDER,
                PetSQLHelper.COLUMN_ADDITIONALINFO, PetSQLHelper.COLUMN_IMAGE, PetSQLHelper.COLUMN_MISSINGSINCE,
                PetSQLHelper.COLUMN_OWNERSPHONE, PetSQLHelper.COLUMN_ISFOUND, PetSQLHelper.COLUMN_USER,
                PetSQLHelper.COLUMN_LON, PetSQLHelper.COLUMN_LAT, PetSQLHelper.COLUMN_SYNCSTATUS};  //PetSQLHelper.COLUMN_SERVER_ID
        String selection = "syncstatus = ?";
        String[] selectionArgs = {"false"};

        //Cursor cursor = activity.getContentResolver().query(DBContentProvider.CONTENT_URI_PET, allColumns, null, null,
         //       null);
        Cursor cursor = activity.getContentResolver().query(DBContentProvider.CONTENT_URI_PET, allColumns, selection, selectionArgs,
              null);


        if (cursor != null) {
            cursor.moveToFirst();
            Pet c = new Pet();
            for (int i = 0; i < cursor.getCount(); i++) {
                Boolean isSent = Boolean.valueOf(cursor.getString(12));
                if (isSent != null) {
                    if (isSent == false) {
                        Long id = Long.parseLong(cursor.getString(0));
                        PetType type = PetType.valueOf(cursor.getString(2));
                        String name = cursor.getString(1);
                        PetGender gender = PetGender.valueOf(cursor.getString(3));
                        String additional = cursor.getString(4);
                        String image = cursor.getString(5);
                        String missingSince = cursor.getString(6);
                        String ownersPhone = cursor.getString(7);
                        boolean isFound = Boolean.valueOf(cursor.getString(8));

                        User userObject = new User();
                        userObject.setEmail(cursor.getString(9));

                        Address address = new Address(Double.parseDouble(cursor.getString(10)), Double.parseDouble(cursor.getString(11)));

                        Log.d("petList SYNC", "ima ih" + " " + type + " " + name + " " + userObject.getEmail());

                        c = new Pet(type, name, gender, additional, image, missingSince, ownersPhone, isFound, userObject, address, isSent);
                        c.setSent(true);       //otisao na back

                        Call<Pet> call = ServiceUtils.petService.postMissing(c);
                        Log.d("PETSSYNC", "usao");
                        call.enqueue(new Callback<Pet>() {
                            @Override
                            public void onResponse(Call<Pet> call, Response<Pet> response) {
                                Log.d("PETADDSYNC", "ima ih" + response.code());
                                if (response.code() == 200) {
                                    Toast.makeText(activity, response.body().getName() + " " + activity.getText(R.string.sync_pet), Toast.LENGTH_SHORT).show();

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

        /*if (brojac == null) {

       }else if (brojac == 1 && cursor.isClosed()){*/
            //obrisem sada celu
            Log.d("BROJAC", "dropujem");

        //}

       // if (cursor.isClosed()) {
        activity.getContentResolver().delete(DBContentProvider.CONTENT_URI_PET, null, null);
        //}


    }

    public static ContentValues fillContent(Pet p, ContentValues entry){
        entry.put(PetSQLHelper.COLUMN_NAME, p.getName());
        entry.put(PetSQLHelper.COLUMN_TYPE, p.getType().toString());
        entry.put(PetSQLHelper.COLUMN_GENDER, p.getGender().toString());
        entry.put(PetSQLHelper.COLUMN_ADDITIONALINFO, p.getAdditionalInfo());
        entry.put(PetSQLHelper.COLUMN_IMAGE, p.getImage());
        String[] parts =  p.getMissingSince().split("/");
        String date = "";
        if (parts.length == 1) {
            date = p.getMissingSince();
        }else{
            date = parts[2] + "-" + parts[1] + "-" + parts[0];
        }
        entry.put(PetSQLHelper.COLUMN_MISSINGSINCE, date);
        entry.put(PetSQLHelper.COLUMN_OWNERSPHONE, p.getOwnersPhone());
       // entry.put(PetSQLHelper.COLUMN_ISFOUND, p.isFound());
        entry.put(PetSQLHelper.COLUMN_USER, p.getUser().getEmail());
        entry.put(PetSQLHelper.COLUMN_LON, p.getAddress().getLon());
        entry.put(PetSQLHelper.COLUMN_LAT, p.getAddress().getLat());
        entry.put(PetSQLHelper.COLUMN_USER, p.getUser().getEmail());
        //entry.put(PetSQLHelper.COLUMN_DELETED, p.isDeleted());
        if (p.isDeleted() == true) {
            entry.put(PetSQLHelper.COLUMN_DELETED, "true");
        }else{
            entry.put(PetSQLHelper.COLUMN_DELETED, "false");
        }
        if (p.isFound() == true) {
            entry.put(PetSQLHelper.COLUMN_ISFOUND, "true");
        }else{
            entry.put(PetSQLHelper.COLUMN_ISFOUND, "false");
        }

        Log.d("FILL", p.toString());
        return entry;
    }
}
