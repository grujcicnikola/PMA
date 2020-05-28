package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.MockupComments;
import com.squareup.picasso.Picasso;

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

    private Double lon;
    private Double lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_pet_detail);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        ImageView imgView = (ImageView) findViewById(R.id.pet_details_image);
        ImageButton imageButton= (ImageButton) findViewById(R.id.buttonViewComments);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {

            String name = bundle.getString("petsName");
            String type = bundle.getString("petsType");
            String gender = bundle.getString("petsGender");
            String email = bundle.getString("ownersEmail");
            String phone = bundle.getString("ownersPhone");
            String info = bundle.getString("additionalInfo");
            String image = bundle.getString("image");
            String date = bundle.getString("date");
            lon = bundle.getDouble("lon_pets");
            lat = bundle.getDouble("lat_pets");

            TextView name_txt = (TextView) findViewById(R.id.pet_details_text_name);
            name_txt.setText(name);
            TextView type_txt = (TextView) findViewById(R.id.pet_details_text_type);
            type_txt.setText(type);
            TextView gender_txt = (TextView) findViewById(R.id.pet_details_text_gender);
            gender_txt.setText(gender);
            TextView missing_txt = (TextView) findViewById(R.id.pet_details_text_missing);
            missing_txt.setText(date);
            TextView location_txt = (TextView) findViewById(R.id.pet_details_text_location);
            TextView email_txt = (TextView) findViewById(R.id.pet_details_text_o_email);
            email_txt.setText(email);
            TextView phone_txt = (TextView) findViewById(R.id.pet_details_text_o_phone);
            phone_txt.setText(phone);
            TextView additional_txt = (TextView) findViewById(R.id.pet_details_text_additional);
            additional_txt.setText(info);

            toolbar.setTitle(name);
            Picasso.get().load(ServiceUtils.IMAGES_URL + image).into(imgView);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ViewCommentsActivity.class);
                    i.putExtra("position_of_pet",String.valueOf(position_of_pet));
                    startActivity(i);
                }
            });

            petsLocation = findViewById(R.id.bView_on_map);
            petsLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ViewOnMapActivity.class);
                    intent.putExtra("lon_view",lon);
                    intent.putExtra("lat_view",lat);
                    startActivity(intent);
                }
            });

        }


    }

}
