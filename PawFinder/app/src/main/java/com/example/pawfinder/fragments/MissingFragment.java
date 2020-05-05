package com.example.pawfinder.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MissingFragment extends Fragment {

    private ListView list;
    private ArrayList<Pet> pets = new ArrayList<Pet>();

    public MissingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fillPetsList();

        View view = inflater.inflate(R.layout.fragment_missing, container, false);
        PetsListAdapter adapter = new PetsListAdapter(getContext(), pets);

        list = (ListView) view.findViewById(R.id.pets_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), PetDetailActivity.class);
                intent.putExtra("pets_position",position);
                startActivity(intent);
            }
        });

        return view;
    }

    public void fillPetsList(){

        Date date = Calendar.getInstance().getTime();

        // Display a date in day, month, year format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(date);

        pets.add(new Pet((long) 1, PetType.DOG, "Dzeki", PetGender.MALE, "Pas ima zelenu ogrlicu", R.drawable.puppydog, today, "021/1234",false));
        pets.add(new Pet((long) 2, PetType.CAT, "Djura", PetGender.MALE, "Ne prilazi nepoznatima", R.drawable.cat, today, "021/1234",false));
        pets.add(new Pet((long) 3, PetType.DOG, "Lara", PetGender.FEMALE, "Druzeljubiva, ima cip", R.drawable.dog2, today, "021/1234",false));
        pets.add(new Pet((long) 4, PetType.CAT, "Kiki", PetGender.FEMALE, "Ruska plava macka", R.drawable.russiancat, today, "021/1234",false));
        pets.add(new Pet((long) 5, PetType.DOG, "Aleks", PetGender.FEMALE, "opis 1", R.drawable.labrador, today, "021/1234",false));
        pets.add(new Pet((long) 6, PetType.DOG, "Bobi", PetGender.MALE, "opis 2", R.drawable.samojedjpg, today, "021/1234",false));
    }


}
