package com.example.pawfinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;
import java.util.Timer;
import java.util.TimerTask;

public class ScannedDataActivity extends Activity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanned_data_layout);
        TextView txtView = (TextView) findViewById(R.id.scanned_data);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String text = bundle.getString("scanned_data");
            txtView.setText(text);
        }

    }
}