package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

}
