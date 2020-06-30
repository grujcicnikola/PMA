
package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pawfinder.R;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signUpButton;
    private EditText email, password;
    private ProgressDialog progressDialog;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_create_account);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(R.string.btn_signUp);

        email = (EditText) findViewById(R.id.c_email_edit);
        password = (EditText) findViewById(R.id.c_password_edit);
        signUpButton = (Button) findViewById(R.id.c_signup_btn);
        signUpButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v == signUpButton) {
            registerUser(v.getContext());
        }

    }

    public void registerUser(final Context context) {

        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if (TextUtils.isEmpty(emailText)) {
            Toast.makeText(this, R.string.create_account_email_toast, Toast.LENGTH_SHORT).show();
        } else if (!this.validateEmail(emailText)) {
            Toast.makeText(this, R.string.create_account_email_notvalid_toast, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(passwordText)) {
            Toast.makeText(this, R.string.create_account_password_toast, Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(CreateAccountActivity.this.getResources().getString(R.string.create_account_dialog_title));
            progressDialog.setMessage(CreateAccountActivity.this.getResources().getString(R.string.dialog_message));
            progressDialog.setCancelable(false);
            progressDialog.show();
            User user = new User(emailText, passwordText);
            final Call<ResponseBody> call = ServiceUtils.userService.register(user);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    progressDialog.dismiss();
                    if (response.code() == 200) {


                        Toast.makeText(context, R.string.create_account_success_message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);

                    } else if (response.code() == 400) {
                        Toast.makeText(context, R.string.create_account_email_already_exists, Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });

        }
    }

    public boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
