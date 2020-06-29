package com.example.pawfinder.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pawfinder.model.Pet;

public class DBContentProvider extends ContentProvider {

    private PetSQLHelper database;

    private static final int PET = 10;
    private static final int PET_ID = 20;
    private static final int PET_FALSE = 30;

    private static final String AUTHORITY = "com.example.pawfinder";

    private static final String PET_PATH = "pet";

    public static final Uri CONTENT_URI_PET = Uri.parse("content://" + AUTHORITY + "/" + PET_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, PET_PATH, PET);
        sURIMatcher.addURI(AUTHORITY, PET_PATH + "/#", PET_ID);
        //sURIMatcher.addURI(AUTHORITY, PET_PATH + "/#", PET_FALSE);

    }

    @Override
    public boolean onCreate() {
        database = new PetSQLHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exist
        //checkColumns(projection);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case PET_ID:
                // Adding the ID to the original query
                queryBuilder.appendWhere(PetSQLHelper.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                //$FALL-THROUGH$
            case PET:
                // Set the table
                queryBuilder.setTables(PetSQLHelper.TABLE_PET);
            /*case PET_FALSE:
                // not sent
                queryBuilder.appendWhere(PetSQLHelper.COLUMN_SYNCSTATUS + "="
                        + "false");*/
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri retVal = null;
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case PET:
                id = sqlDB.insert(PetSQLHelper.TABLE_PET, null, values);
                retVal = Uri.parse(PET_PATH + "/" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        int rowsDeleted = 0;
        switch (uriType) {
            case PET:
                rowsDeleted = sqlDB.delete(PetSQLHelper.TABLE_PET,
                        selection,
                        selectionArgs);
                break;
            case PET_ID:
                String idPet = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(PetSQLHelper.TABLE_PET,
                            PetSQLHelper.COLUMN_SERVER_ID + "=" + idPet,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(PetSQLHelper.TABLE_PET,
                            PetSQLHelper.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        int rowsUpdated = 0;
        switch (uriType) {
            case PET:
                Log.d("updatePET", " usao ");
                rowsUpdated = sqlDB.update(PetSQLHelper.TABLE_PET,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PET_ID:
                Log.d("updatePETID", " usao ");
                String idPet = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(PetSQLHelper.TABLE_PET,
                            values,
                            PetSQLHelper.COLUMN_SERVER_ID + "=" + idPet,
                            null);
                    Log.d("updatePETID", String.valueOf(rowsUpdated));
                } else {
                    rowsUpdated = sqlDB.update(PetSQLHelper.TABLE_PET,
                            values,
                            PetSQLHelper.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    public void delete(Pet pet){
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        String table = PetSQLHelper.TABLE_PET;
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(pet.getId()) };
        sqlDB.delete(table, whereClause, whereArgs);
    }
}
