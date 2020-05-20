package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;
import com.example.pawfinder.adapters.PetsListAdapter;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.example.pawfinder.model.User;
import com.example.pawfinder.tools.MockupComments;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PetDetailActivity extends AppCompatActivity {

    private ArrayList<Pet> pets = new ArrayList<Pet>();
    private int position_of_pet;
    private Button petsLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_pet_detail);


        PetsListAdapter adapter = new PetsListAdapter(this, MockupComments.getPets());

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        ImageView imgView = (ImageView) findViewById(R.id.pet_details_image);
        TextView txtView = (TextView) findViewById(R.id.pet_details_text);
        ImageButton imageButton= (ImageButton) findViewById(R.id.buttonViewComments);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {

            int position = bundle.getInt("pets_position");
            position_of_pet = position;
            Pet pet = (Pet) adapter.getItem(position);
            toolbar.setTitle(pet.getName());
            imgView.setImageResource(pet.getImage());
            txtView.setText(pet.toString());
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ViewCommentsActivity.class);
                    i.putExtra("position_of_pet",String.valueOf(position_of_pet));
                    startActivity(i);
                }
            });

        }

        petsLocation = findViewById(R.id.bView_on_map);
        petsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewOnMapActivity.class);
                startActivity(intent);
            }
        });
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
