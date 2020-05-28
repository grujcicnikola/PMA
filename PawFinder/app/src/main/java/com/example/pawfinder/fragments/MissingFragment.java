
package com.example.pawfinder.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pawfinder.R;
import com.example.pawfinder.activity.PetDetailActivity;
import com.example.pawfinder.adapters.PetsListAdapter;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.MockupComments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MissingFragment extends Fragment {

    private ListView list;
    private List<Pet> pets = new ArrayList<Pet>();

    public static  MissingFragment newInstance(Bundle bundle){
        MissingFragment fragment = new MissingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_missing, container, false);
        list = (ListView) view.findViewById(R.id.pets_list);

        final Call<List<Pet>> call = ServiceUtils.petService.getAll();
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {

               // Log.d("Dobijeno", response.body().toString());
                Log.d("BROJ","ima ih" + response.body().size());
                /*for (Pet pet : response.body()) {
                    pets.add(pet);
                }*/
                //view[0] = generateDataList(response.body(), view[0]);
                pets = response.body();
                PetsListAdapter adapter = new PetsListAdapter(getContext(), response.body());
                list.setAdapter(adapter);
                Log.d("POSLEFORA"," - " + pets);
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {

                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");

            }
        });



        //PetsListAdapter adapter = new PetsListAdapter(getContext(), MockupComments.getPets());
        Log.d("PREADAPTERA"," - " + pets.size());

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
                intent.putExtra("lon_pets", pets.get(position).getAddress().getLon());
                intent.putExtra("lat_pets", pets.get(position).getAddress().getLat());
                startActivity(intent);
            }
        });


        return view;
    }


}
