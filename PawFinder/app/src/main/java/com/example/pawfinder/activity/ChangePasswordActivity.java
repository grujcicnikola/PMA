package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.sync.PetSqlSync;
import com.example.pawfinder.tools.NetworkTool;
import com.example.pawfinder.tools.PrefConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private static PrefConfig prefConfig;
    private EditText currentPassword;
    private EditText newPassword;
    private EditText newPasswordAgain;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_change_password);

        prefConfig = new PrefConfig(this);
        currentPassword = findViewById(R.id.enter_current_password);
        newPassword = findViewById(R.id.new_password);
        newPasswordAgain = findViewById(R.id.new_password_again);
        button = findViewById(R.id.btn_change_password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassword.getText().toString().equals(newPasswordAgain.getText().toString()) == true) {
                    sendPassword();
                }else{
                    Toast.makeText(getApplicationContext(), getText(R.string.same_passwords), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void sendPassword() {
            User user = new User(prefConfig.readUserEmail(), currentPassword.getText().toString(), newPassword.getText().toString());
            Call<ResponseBody> call = ServiceUtils.userService.changePassword(user);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getApplicationContext(), getText(R.string.success_change_password), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), getText(R.string.old_passwords), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Error", t.getMessage() != null ? t.getMessage() : "error");
                }
            });

    }
}
