package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
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

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_pet_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imgView = (ImageView) findViewById(R.id.pet_details_image);
        ImageButton imageButton = (ImageButton) findViewById(R.id.buttonViewComments);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

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

            //lokalizacija tipa
            String[] type_values= getResources().getStringArray(R.array.type_values);
            int index_type = Arrays.asList(type_values).indexOf(type);
            String[] type_entries = getResources().getStringArray(R.array.type_entries);
            String type_entry = String.valueOf(type_entries[index_type]);

            //lokalizacija pola
            String[] gender_values= getResources().getStringArray(R.array.genders_values);
            int index_gender = Arrays.asList(gender_values).indexOf(gender);
            String[] gender_entries = getResources().getStringArray(R.array.genders_entries);
            String gender_entry = String.valueOf(gender_entries[index_gender]);

            TextView name_txt = (TextView) findViewById(R.id.pet_details_text_name);
            name_txt.setText(name);
            TextView type_txt = (TextView) findViewById(R.id.pet_details_text_type);
            type_txt.setText(type_entry);
            TextView gender_txt = (TextView) findViewById(R.id.pet_details_text_gender);
            gender_txt.setText(gender_entry);
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
                    Intent intent = new Intent(getApplicationContext(), ViewCommentsActivity.class);
                    //i.putExtra("position_of_pet",String.valueOf(position_of_pet));
                    intent.putExtra("view_comments_petsName", bundle.getString("petsName"));
                    intent.putExtra("view_comments_additionalInfo", bundle.getString("additionalInfo"));
                    intent.putExtra("view_comments_id", bundle.getLong("id_of_pet"));
                    Log.d("PETSID ", "ima ih" + bundle.getLong("id_of_pet"));
                    Log.d("PETSI* ", "ima ih" + bundle.getString("id_of_pet"));
                    intent.putExtra("view_comments_image", bundle.getString("image"));
                    startActivity(intent);
                }
            });

            petsLocation = findViewById(R.id.bView_on_map);
            petsLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ViewOnMapActivity.class);
                    intent.putExtra("lon_view", lon);
                    intent.putExtra("lat_view", lat);
                    startActivity(intent);
                }
            });

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish(); //close this activity and return to preview activity
                break;
        }

        return true;
    }

}
