package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.pawfinder.R;
import android.content.Intent;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_login);

        TextView createAccountView = (TextView) findViewById(R.id.lCreateAccount);
        createAccountView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case  R.id.lCreateAccount:
                intent = new Intent(this, CreateAccountActivity.class);
                startActivity(intent);
                break;
        }

    }
}
