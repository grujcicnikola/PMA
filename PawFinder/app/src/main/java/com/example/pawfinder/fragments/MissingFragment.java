
package com.example.pawfinder.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.List;

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
    private List<Pet> pets = new ArrayList<Pet>();
    //private NetworkMonitor sync;

    private BroadcastReceiver broadcastReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (NetworkTool.getConnectivityStatus(context) != NetworkTool.TYPE_NOT_CONNECTED) {
                //ovde pristupamo bazi i gledamo da li treba nesto sinhronizovati
                Log.i("REZBR", "onReceive");
                if(intent.getAction().equals(MissingFragment.SYNC_DATA)){
                    PetSqlSync.fillDatabase((ArrayList<Pet>) pets, getActivity(), 0);
                }else if (intent.getAction().equals(MissingFragment.SEND_DATA)) {
                    PetSqlSync.sendUnsaved(getActivity());
                    getActivity().getContentResolver().delete(DBContentProvider.CONTENT_URI_PET, null, null);
                }
            }
        }
    };
    /*private static MissingFragment missingFragmentInstance;
    public static MissingFragment  getInstace(){
        return missingFragmentInstance;
    }*/

    public static MissingFragment newInstance(Bundle bundle) {
        MissingFragment fragment = new MissingFragment();
        fragment.setArguments(bundle);
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

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

        if (NetworkTool.getConnectivityStatus(getContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
            Log.d("ima", " interneta if");
            //registrujem broadcast
           // sync = new NetworkMonitor();

            IntentFilter filterData = new IntentFilter();
            filterData.addAction(SEND_DATA);
            //getActivity().registerReceiver(sync, filterData);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, filterData);

            IntentFilter filter = new IntentFilter();
            filter.addAction(SYNC_DATA);
            //getActivity().registerReceiver(sync, filter);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, filter);


            //saljemo lokalne reportove
            Intent intsData = new Intent(SEND_DATA);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intsData);
            //getContext().sendBroadcast(intsData);

            final Call<List<Pet>> call = ServiceUtils.petService.getAll();
            call.enqueue(new Callback<List<Pet>>() {
                @Override
                public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                    // Log.d("Dobijeno", response.body().toString());
                    Log.d("BROJ", "ima ih" + response.body().size());

                    pets = response.body();
                    PetsListAdapter adapter = new PetsListAdapter(getContext(), response.body());
                    list.setAdapter(adapter);

                    Log.d("POSLEFORA", " - " + pets);
                    if (response.code() == 200) {
                        Log.d("REZ", "Meesage recieved");
                        //sinhronizujemo server sa sqlite
                        Intent ints = new Intent(SYNC_DATA);
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(ints);
                        //getContext().sendBroadcast(ints);
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
    }

    //popunjava se prikaz kada je offline korisnik
    public void fillView(){
        String[] allColumns = { PetSQLHelper.COLUMN_ID,
                PetSQLHelper.COLUMN_NAME, PetSQLHelper.COLUMN_TYPE, PetSQLHelper.COLUMN_GENDER,
                PetSQLHelper.COLUMN_ADDITIONALINFO, PetSQLHelper.COLUMN_IMAGE, PetSQLHelper.COLUMN_MISSINGSINCE,
                PetSQLHelper.COLUMN_OWNERSPHONE, PetSQLHelper.COLUMN_ISFOUND, PetSQLHelper.COLUMN_USER,
                PetSQLHelper.COLUMN_LON, PetSQLHelper.COLUMN_LAT, PetSQLHelper.COLUMN_SYNCSTATUS};
        Cursor cursor = getActivity().getContentResolver().query(DBContentProvider.CONTENT_URI_PET, allColumns, null, null,
                null);

        String[] from = new String[]{PetSQLHelper.COLUMN_NAME, PetSQLHelper.COLUMN_MISSINGSINCE};
        int[] to = new int[]{R.id.pet_name, R.id.pet_dateOfLost};
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
                String ownersPhone = cursor.getString(7);
                boolean isFound = Boolean.valueOf(cursor.getString(9));

                User user = new User();
                String email =  cursor.getString(8);
                user.setEmail(email);

                Address address = new Address(Double.parseDouble(cursor.getString(10)), Double.parseDouble(cursor.getString(11)));
                boolean isSent = Boolean.valueOf(cursor.getString(12));

                Log.d("petList ", "ima ih"  +" " + type +" "+ name +" "+ missingSince);
               // if (isSent != false) {    ovo ako ne budemo hteli da prikazujemo
                    c = new Pet(type, name, gender, additional, image, missingSince, ownersPhone, isFound, user, address, isSent);
                    petView.add(c);
                //}

                cursor.moveToNext();
            }
            // always close the cursor
            cursor.close();
        }

        pets = petView;
        PetsListAdapter adapter = new PetsListAdapter(getContext(), pets);
        list.setAdapter(adapter);

    }

    @Override
    public void onPause() {
        super.onPause();
        //osloboditi resurse
        /*if(sync != null){
            getActivity().unregisterReceiver(sync);
        }*/
        if(broadcastReceiver != null){
            try {
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
            }catch(Exception e){
                Log.d("onPause", "broadcast");
            }

        }


    }

}
