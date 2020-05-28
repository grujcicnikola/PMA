
package com.example.pawfinder.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.example.pawfinder.R;
import com.example.pawfinder.activity.MapsActivity;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class MissingReportFirstPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //dodati zbog error poruka
    private TextInputLayout layoutName;
    private TextInputLayout layoutDate;
    private Toolbar toolbar;
    private TextView dateText;
    private EditText name;
    private Spinner gender;
    private Spinner type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_missing_report_first_page);
        setTitle(R.string.title_missing_first);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        name = findViewById(R.id.enter_pet_name);
        gender = findViewById(R.id.gender);
        type = findViewById(R.id.type);
        dateText = findViewById(R.id.text_view_date);

        layoutName = findViewById(R.id.text_input_layout_name);
        layoutDate = findViewById(R.id.text_input_layout_date);

        Spinner spinnerGender = findViewById(R.id.gender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<PetGender> adapterGender = new ArrayAdapter<PetGender>(this, R.layout.spinner_item, PetGender.values());
        // Specify the layout to use when the list of choices appears
        adapterGender.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinnerGender.setAdapter(adapterGender);


        Spinner spinnerType = findViewById(R.id.type);
        ArrayAdapter<PetType> adapterType = new ArrayAdapter<PetType>(this, R.layout.spinner_item, PetType.values());
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spinnerType.setAdapter(adapterType);


        findViewById(R.id.btn_get_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!name.getText().toString().isEmpty()) {
                    layoutName.setError(null);
                }
            }

        });

        dateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!dateText.getText().toString().isEmpty()) {
                    layoutDate.setError(null);
                }
            }

        });

        Button next = findViewById(R.id.btn_missing_report_first);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() || name.getText().toString() == null) {
                    layoutName.setError((getText(R.string.name_blank)));
                } else if (dateText.getText().toString().isEmpty() || dateText.getText().equals("dd/mm/yyyy") || dateText.getText().toString() == null) {
                    layoutDate.setError((getText(R.string.date_blank)));
                } else {
                    name.setError(null);
                    dateText.setError(null);
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("PET_NAME", name.getText().toString());
                    intent.putExtra("PET_GENDER", gender.getSelectedItem().toString());
                    intent.putExtra("PET_GENDER_ID", gender.getSelectedItemId());
                    intent.putExtra("PET_TYPE", type.getSelectedItem().toString());
                    intent.putExtra("PET_DATE_LOST", dateText.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //uzima datum koji je selektovan
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        dateText.setText(date);
    }

   /* @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            name = findViewById(R.id.enter_pet_name);
            gender = findViewById(R.id.gender);
            type = findViewById(R.id.type);
            dateText = findViewById(R.id.text_view_date);
            name.setText(savedInstanceState.getBundle("PET_NAME").toString());
            dateText.setText(savedInstanceState.getBundle("PET_DATE_LOST").toString());
            Toast.makeText(this, savedInstanceState.getBundle("PET_NAME").toString() + " " +savedInstanceState.getBundle("PET_DATE_LOST").toString(), Toast.LENGTH_SHORT).show();
            //gender.setSelection((int) savedInstanceState.getBinder("PET_GENDER_ID"));
        }
    }*/

}
