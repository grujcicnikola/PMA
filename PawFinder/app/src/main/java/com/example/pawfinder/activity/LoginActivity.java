
package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.PrefConfig;

import android.content.Intent;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private EditText emailEdit, passwordEdit;
    private static PrefConfig prefConfig;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_login);

        prefConfig = new PrefConfig(this);
        Button loginBtn = (Button) findViewById(R.id.bLogin);
        loginBtn.setOnClickListener(this);
        emailEdit = (EditText) findViewById(R.id.l_edit_email);
        passwordEdit = (EditText) findViewById(R.id.l_edit_password);
        setTitle(R.string.btn_login);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        TextView createAccountView = (TextView) findViewById(R.id.lCreateAccount);
        createAccountView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.lCreateAccount:
                intent = new Intent(this, CreateAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.bLogin:
                login(v.getContext());
                break;
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

    public void login(final Context context) {
        final String emailTxt = emailEdit.getText().toString();
        String passwordTxt = passwordEdit.getText().toString();

        if (TextUtils.isEmpty(emailTxt)) {
            Toast.makeText(this, R.string.create_account_email_toast, Toast.LENGTH_SHORT);
        } else if(TextUtils.isEmpty(passwordTxt)){
            Toast.makeText(this, R.string.create_account_password_toast, Toast.LENGTH_SHORT);
        }
        else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(LoginActivity.this.getResources().getString(R.string.login_dialog_title));
            progressDialog.setMessage(LoginActivity.this.getResources().getString(R.string.dialog_message));
            progressDialog.setCancelable(false);
            progressDialog.show();

            User user = new User(emailTxt, passwordTxt);
            Call<ResponseBody> call = ServiceUtils.userService.login(user);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    progressDialog.dismiss();
                    if (response.code() == 200) //ok
                    {
                        prefConfig.writeUserEmail(emailTxt);
                        Log.d("loggeduser",emailTxt);
                        prefConfig.writeLoginStatus(true);
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (response.code() == 403) {
                        Toast.makeText(context, R.string.login_email_error, Toast.LENGTH_LONG).show();
                    } else if (response.code() == 400) {
                        Toast.makeText(context, R.string.login_password_error, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZULTAT", t.getMessage() != null ? t.getMessage() : "error");
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
}
