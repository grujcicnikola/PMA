package com.example.pawfinder.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Switch;

import com.example.pawfinder.model.User;
import com.example.pawfinder.service.MyFirebaseInstanceService;
import com.example.pawfinder.service.ServiceUtils;

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationUtils {

    private static SharedPreferences sharedPreferences;
    private Context context;
    private static PrefConfig prefConfig;

    public NotificationUtils(SharedPreferences sharedPreferences, Context context) {
        super();
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }


    public void setNotification() {
        Boolean notification = sharedPreferences.getBoolean("notification", true);
        Log.i("notificaaaa", String.valueOf(notification));
        saveNotification(notification);

    }

    public void saveNotification(Boolean notification) {
        String notificationPref = "notification";

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(notificationPref, notification);
        editor.commit();

        if(notification){
            Log.d("sendToken", "Meesage recieved");
            String token = MyFirebaseInstanceService.getToken(context);
            sendTokenToServer(token);
        }else{
            Log.d("sendToken", "empty");
            sendTokenToServer("");
        }
    }
    private void sendTokenToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        prefConfig = new PrefConfig(context);
        User user = new User();

        if (prefConfig.readUserEmail()!=null) {
            user.setEmail(prefConfig.readUserEmail());
            user.setToken(token);
            final Call<ResponseBody> call = ServiceUtils.userService.token(user);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {
                        Log.i("TOKENFIREBASE","send");
                    } else if (response.code() == 400) {
                        Log.i("TOKENFIREBASE","error");
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("TOKENFIREBASE","error");
                }
            });

        }

    }
}