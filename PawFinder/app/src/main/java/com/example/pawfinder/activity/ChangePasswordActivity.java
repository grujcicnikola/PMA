package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pawfinder.R;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.PrefConfig;

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
                        finish();
                        /*int callingActivity = getIntent().getIntExtra("calling-activity", 0);
                        Intent intent;
                        switch (callingActivity) {
                            case ActivityConstants.ACTIVITY_MAIN:
                                intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                            case ActivityConstants.ACTIVITY_REPORT_DETAIL:
                                intent = new Intent(ChangePasswordActivity.this, ReportDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                            case ActivityConstants.ACTIVITY_MISSING_DETAIL:
                                intent = new Intent(ChangePasswordActivity.this, PetDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                        }
    */
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }

        return true;
    }
}
