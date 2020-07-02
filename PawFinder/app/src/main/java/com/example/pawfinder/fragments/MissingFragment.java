
package com.example.pawfinder.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;
import com.example.pawfinder.activity.PetDetailActivity;
import com.example.pawfinder.adapters.PetsListAdapter;
import com.example.pawfinder.db.DBContentProvider;
import com.example.pawfinder.db.PetSQLHelper;
import com.example.pawfinder.model.Address;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.sync.PetSqlSync;
import com.example.pawfinder.tools.NetworkTool;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import hossamscott.com.github.backgroundservice.RunService;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MissingFragment extends Fragment{

    public static final String SYNC_DATA = "SYNC_DATA";
    public static final String SEND_DATA = "SEND_DATA" ;
    private ListView list;
    public static  List<Pet> pets = new ArrayList<Pet>();
    public static PetsListAdapter adapter;
    //private NetworkMonitor sync;
    

    public static MissingFragment newInstance() {
        MissingFragment fragment = new MissingFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // missingFragmentInstance = this;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_missing, container, false);
        list = (ListView) view.findViewById(R.id.pets_list);

        //DANICa

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //sendAndSyncData();
        Log.d("ima", "pre");
        if (NetworkTool.getConnectivityStatus(getContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
            Log.d("ima", " interneta if");
            //Intent intentFilter = new Intent(SEND_DATA);
            // getActivity().sendBroadcast(intentFilter);


            final Call<List<Pet>> call = ServiceUtils.petService.getAll();
            call.enqueue(new Callback<List<Pet>>() {
                @Override
                public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                    // Log.d("Dobijeno", response.body().toString());
                    Log.d("BROJ", "ima ih" + response.body().size());
                    List<Pet> good = new ArrayList<Pet>();
                    for (Pet p :response.body()) {
                        if (p.isDeleted() == false&& p.isFound() == false) {
                            good.add(p);
                        }
                    }
                    pets = good;
                    adapter = new PetsListAdapter(getContext(), good);
                    list.setAdapter(adapter);
                    PetSqlSync.fillDatabase((ArrayList<Pet>) response.body(), getContext(), 0);

                    Log.d("POSLEFORA", " - " + pets);
                    if (response.code() == 200) {
                        Log.d("REZ", "Meesage recieved");
                    } else {
                        Log.d("REZ", "Meesage recieved: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Pet>> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");

                }
            });
        }else{
            //kada nema neta - prikaz iz sqlite
            Log.d("nema", " interneta else");
            fillView();
        }

        //PetsListAdapter adapter = new PetsListAdapter(getContext(), MockupComments.getPets());
        Log.d("PREADAPTERA", " - " + pets.size());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), PetDetailActivity.class);
                intent.putExtra("petsName", pets.get(position).getName());
                intent.putExtra("petsType", pets.get(position).getType().toString());
                intent.putExtra("petsGender", pets.get(position).getGender().toString());
                intent.putExtra("ownersEmail", pets.get(position).getUser().getEmail());
                intent.putExtra("ownersPhone", pets.get(position).getOwnersPhone());
                intent.putExtra("additionalInfo", pets.get(position).getAdditionalInfo());
                intent.putExtra("image", pets.get(position).getImage());
                intent.putExtra("date", pets.get(position).getMissingSince());
                intent.putExtra("id_of_pet", pets.get(position).getId());
                intent.putExtra("lon_pets", pets.get(position).getAddress().getLon());
                intent.putExtra("lat_pets", pets.get(position).getAddress().getLat());
                Log.d("PETSID ", "ima ih" + pets.get(position).getId());

                startActivity(intent);
            }
        });

        /*
        Register BroadcastReceiver to get notification when service is over
         */

        //IntentFilter intentFilter = new IntentFilter("alaram_received");
        //LocalBroadcastManager.getInstance(getActivity()).registerReceiver(alarm_receiver, intentFilter);

        /*
        If you want to repeat the alarm every X sec just add true in your call
        */
        //LocalBroadcastManager.getInstance(getContext()).sendBroadcast();
        //RunService repeat = new RunService(getActivity());
       // repeat.call(5, true);

    }


    /*BroadcastReceiver alarm_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // your logic here
            Log.i("alarm_received", "logic");
            int status = NetworkTool.getConnectivityStatus(getContext());
            if (status != NetworkTool.TYPE_NOT_CONNECTED) {
                Log.i("alarm_received", "success");

            } else {
                Log.i("alarm_received", "not connected to internet");
            }
        }
    };*/
    //popunjava se prikaz kada je offline korisnik
    public void fillView(){
        String[] allColumns = { PetSQLHelper.COLUMN_ID,
                PetSQLHelper.COLUMN_NAME, PetSQLHelper.COLUMN_TYPE, PetSQLHelper.COLUMN_GENDER,
                PetSQLHelper.COLUMN_ADDITIONALINFO, PetSQLHelper.COLUMN_IMAGE, PetSQLHelper.COLUMN_MISSINGSINCE,
                PetSQLHelper.COLUMN_OWNERSPHONE, PetSQLHelper.COLUMN_ISFOUND, PetSQLHelper.COLUMN_USER,
                PetSQLHelper.COLUMN_LON, PetSQLHelper.COLUMN_LAT, PetSQLHelper.COLUMN_SYNCSTATUS, PetSQLHelper.COLUMN_DELETED};
       /* Cursor cursor = getActivity().getContentResolver().query(DBContentProvider.CONTENT_URI_PET, allColumns, null, null,
                null);*/
        String selection = "";
        String[] selectionArgs = {};

       Cursor cursor = getActivity().getContentResolver().query(DBContentProvider.CONTENT_URI_PET, allColumns, selection, selectionArgs,
               "date("+PetSQLHelper.COLUMN_MISSINGSINCE+")" + " DESC");
        List<Pet> petView = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            Pet c = new Pet();
            for (int i = 0; i < cursor.getCount(); i++){
                Long id = Long.parseLong(cursor.getString(0));
                PetType type = PetType.valueOf(cursor.getString(2));
                String name = cursor.getString(1);
                PetGender gender = PetGender.valueOf(cursor.getString(3));
                String additional = cursor.getString(4);
                String image = cursor.getString(5);

                String missingSince = cursor.getString(6);
                String[] parts =  missingSince.split("-");
                String date = "";
                if (parts.length == 3) {
                    date = parts[2] + "/" + parts[1] + "/" + parts[0];
                }

                String ownersPhone = cursor.getString(7);
                boolean isFound = Boolean.valueOf(cursor.getString(8));

                User user = new User();
                String email =  cursor.getString(9);
                user.setEmail(email);

                Address address = new Address(Double.parseDouble(cursor.getString(10)), Double.parseDouble(cursor.getString(11)));
                boolean isSent = Boolean.valueOf(cursor.getString(12));
                boolean isDeleted = Boolean.valueOf(cursor.getString(13));


                Log.d("petList ", "ima ih"  +" " + type +" "+ name +" "+ missingSince + " " );
               // if (isSent != false) {    ovo ako ne budemo hteli da prikazujemo
                if (isDeleted == false && isFound == false ) {
                    //c = new Pet(type, name, gender, additional, image, missingSince, ownersPhone, isFound, user, address, isSent);
                    c.setId(id);
                    c = new Pet(type, name, gender, additional, image, date, ownersPhone, isFound, user, address, isSent);
                    petView.add(c);
                }
                //}

                cursor.moveToNext();
            }
            // always close the cursor
            cursor.close();
        }

        pets = petView;
        adapter = new PetsListAdapter(getContext(), pets);
        list.setAdapter(adapter);

    }

    @Override
    public void onPause() {
        super.onPause();
        //osloboditi resurse
        /*if(sync != null){
            getActivity().unregisterReceiver(sync);
        }*/
       /* if(broadcastReceiver != null){
            try {
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
            }catch(Exception e){
                Log.d("onPause", "broadcast");
            }

        }
*/

    }

}
