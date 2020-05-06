package com.example.pawfinder.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pawfinder.R;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;

import java.util.Calendar;

public class MissingReportFirstPage extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{



    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_report_first_page);

        Spinner spinnerGender = findViewById(R.id.gender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<PetGender> adapterGender = new ArrayAdapter<PetGender>(this, android.R.layout.simple_spinner_item, PetGender.values());
        // Specify the layout to use when the list of choices appears
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerGender.setAdapter(adapterGender);


        Spinner spinnerType = findViewById(R.id.type);
        ArrayAdapter<PetType> adapterType = new ArrayAdapter<PetType>(this, android.R.layout.simple_spinner_item, PetType.values());
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);


        dateText = findViewById(R.id.text_view_date);
        findViewById(R.id.btn_get_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });



        Button next = findViewById(R.id.btn_missing_report_first);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                //intent.putExtra("EXTRA_MESSAGE",message);
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //uzima datum koji je selektovan
        String date = month + "/" + dayOfMonth + "/" + year;
        dateText.setText(date);
    }



}
