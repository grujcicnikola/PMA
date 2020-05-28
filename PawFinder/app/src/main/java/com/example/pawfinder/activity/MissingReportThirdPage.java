
package com.example.pawfinder.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;
import com.example.pawfinder.model.Address;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.PrefConfig;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissingReportThirdPage extends AppCompatActivity {

    private int numberOfSelected = 1;

    private ImageView imageView;
    private Button uploadImage;
    private Button finish;
    private EditText phoneNumberET;
    private TextInputLayout layoutPhone;
    private EditText infoET;

    private Uri uri;

    private Double lon;
    private Double lat;
    private String name;
    private PetGender gender;
    private PetType type;
    private String date;

    private String phone;
    private String info;

    private Pet pet;
    private static PrefConfig prefConfig;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_missing_report_third_page);
        prefConfig = new PrefConfig(this);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_missing_third);
        setTitle(R.string.title_missing_third);
        imageView = findViewById(R.id.upload_image_view);
        uploadImage = findViewById(R.id.choose_pet_image);
        phoneNumberET = findViewById(R.id.enter_phone_number);
        layoutPhone = findViewById(R.id.text_input_layout_phone);
        infoET = findViewById(R.id.enter_add_info);
        finish = findViewById(R.id.btn_missing_report_third);

        Intent help = getIntent();
       /* Toast.makeText(this, help.getStringExtra("PET_NAME"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, help.getStringExtra("PET_GENDER"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, help.getStringExtra("PET_TYPE"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, help.getStringExtra("PET_DATE_LOST"), Toast.LENGTH_SHORT).show();
        Log.d("LONt","Meesage recieved: "+help.getDoubleExtra("PET_LOST_LON",0));
        Log.d("LATT","Meesage recieved: "+help.getDoubleExtra("PET_LOST_LAT",0));*/

        lat = help.getDoubleExtra("PET_LOST_LAT", 0);
        lon = help.getDoubleExtra("PET_LOST_LON", 0);
        name = help.getStringExtra("PET_NAME");
        gender = PetGender.valueOf(help.getStringExtra("PET_GENDER"));
        type = PetType.valueOf(help.getStringExtra("PET_TYPE"));
        date = help.getStringExtra("PET_DATE_LOST");


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, numberOfSelected);
            }
        });

        phoneNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!phoneNumberET.getText().toString().isEmpty()) {
                    layoutPhone.setError(null);
                }
            }

        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumberET.getText().toString().isEmpty() || phoneNumberET.getText().toString() == null) {
                    layoutPhone.setError((getText(R.string.phone_blank)));
                } else {
                    Address address = new Address(lon, lat);
                    ;
                    User user = new User();
                    Geocoder geocoder;
                    List<android.location.Address> fullAddressFromMap;
                    geocoder = new Geocoder(MissingReportThirdPage.this, Locale.getDefault());
                    try {
                        String sPlace;
                        fullAddressFromMap = geocoder.getFromLocation(lat, lon, 1);
                        String street = fullAddressFromMap.get(0).getAddressLine(0);
                        String city = fullAddressFromMap.get(0).getAddressLine(1);
                        String country = fullAddressFromMap.get(0).getAddressLine(2);

                        /*String[] splitAddress = fullAddressFromMap.split(",");
                        sPlace = splitAddress[0] + "\n";
                        if(city != null && !city.isEmpty()) {
                            String[] splitCity = city.split(",");
                            sPlace += splitCity[0];
                        }*/
                        address = new Address(city, street, 1, lon, lat);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (prefConfig.readLoginStatus()) {
                        user.setEmail(prefConfig.readUserEmail());
                    }
                    Log.d("DATUM", "STRING " + date);
                    pet = new Pet(type, name, gender, infoET.getText().toString(), date, phoneNumberET.getText().toString(), false, user, address);
                    addPet(pet);
                    Intent intent = new Intent(MissingReportThirdPage.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //(resultCode == numberOfSelected &&???
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPet(Pet petAdd) {
        Call<Pet> call = ServiceUtils.petService.postMissing(petAdd);
        Log.d("PETS", "usao");
        call.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                Log.d("PETADD", "ima ih" + response.body());
                if (response.code() == 200) {
                    Log.d("REZ", "Meesage recieved");
                    Toast.makeText(getApplicationContext(), R.string.add_pet_success, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                    Toast.makeText(getApplicationContext(), R.string.add_pet_failure, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {
                Log.d("PETADD", t.getMessage() != null ? t.getMessage() : "error");
                Toast.makeText(getApplicationContext(), R.string.add_pet_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
