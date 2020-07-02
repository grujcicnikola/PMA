package com.example.pawfinder.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.pawfinder.fragments.MissingFragment;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.NetworkTool;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
// your logic here
        Log.i("alarm_received", "logic");

        int status = NetworkTool.getConnectivityStatus(context);
        if (status != NetworkTool.TYPE_NOT_CONNECTED) {
            Log.i("alarm_received", "success");
            //Log.i("stanje", String.valueOf(MissingFragment.pets.size()));
            //MissingFragment.updatelist();
            //getUnseenComments();
            final Call<List<Pet>> call = ServiceUtils.petService.getAll();
            call.enqueue(new Callback<List<Pet>>() {
                @Override
                public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                    // Log.d("Dobijeno", response.body().toString());
                    //Log.d("BROJ", "ima ih" + response.body().size());

                    MissingFragment.pets = response.body();
                    if (MissingFragment.pets != null) {
                        if (MissingFragment.adapter != null) {
                            MissingFragment.adapter.updateResults(MissingFragment.pets);
                        }

                        PetSqlSync.fillDatabase((ArrayList<Pet>) MissingFragment.pets, context, 0);
                    }
                }

                @Override
                public void onFailure(Call<List<Pet>> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                }
            });
        } else {
            Log.i("alarm_received", "not connected to internet");
        }
    }
}
