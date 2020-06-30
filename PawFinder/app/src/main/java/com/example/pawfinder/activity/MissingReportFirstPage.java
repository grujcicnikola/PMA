
package com.example.pawfinder.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.pawfinder.tools.NetworkTool;
import com.example.pawfinder.tools.PrefConfig;
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

//        if(savedInstanceState != null)
//        {
//            name.setText(savedInstanceState.getString("petName"));
//        }else
//        {
//            Log.d("NULTAG", "NUL jeeeeeee");
//        }

        setContentView(R.layout.activity_missing_report_first_page);
        setTitle(R.string.title_missing_first);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        name = findViewById(R.id.enter_pet_name);
        gender = findViewById(R.id.gender);
        type = findViewById(R.id.type);
        dateText = findViewById(R.id.text_view_date);

        layoutName = findViewById(R.id.text_input_layout_name);
        layoutDate = findViewById(R.id.text_input_layout_date);

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
                if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                    if (name.getText().toString().isEmpty() || name.getText().toString() == null) {
                        layoutName.setError((getText(R.string.name_blank)));
                    } else if (dateText.getText().toString().isEmpty() || dateText.getText().equals("dd/mm/yyyy") || dateText.getText().toString() == null) {
                        layoutDate.setError((getText(R.string.date_blank)));
                    } else {
                        name.setError(null);
                        dateText.setError(null);
                        //lokalizacija pola
                        int spinner_pos = gender.getSelectedItemPosition();
                        String[] gender_values = getResources().getStringArray(R.array.genders_values);
                        String gender_value = String.valueOf(gender_values[spinner_pos]);
                        //lokalizacija tipa
                        int spinner_type = type.getSelectedItemPosition();
                        String[] type_values = getResources().getStringArray(R.array.type_values);
                        String type_value = String.valueOf(type_values[spinner_type]);

                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("PET_NAME", name.getText().toString());
                        intent.putExtra("PET_GENDER", gender_value);
                        intent.putExtra("PET_TYPE", type_value);
                        intent.putExtra("PET_DATE_LOST", dateText.getText().toString());
                        startActivityForResult(intent,2);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
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


   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       Log.i("ACTIVITYRESULT","firstpage"+resultCode);
       if(resultCode==2){
           finish();
       }
   }

@Override
    protected void onDestroy() {
        setResult(2);
        Log.i("ACTIVITYRESULT","firstpage destroy");
        super.onDestroy();
    }
}
