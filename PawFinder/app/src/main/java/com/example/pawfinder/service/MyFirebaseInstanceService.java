package com.example.pawfinder.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.pawfinder.MainActivity;
import com.example.pawfinder.R;
import com.example.pawfinder.activity.LoginActivity;
import com.example.pawfinder.activity.PetDetailActivity;
import com.example.pawfinder.activity.ReportDetailActivity;
import com.example.pawfinder.activity.ViewCommentsActivity;
import com.example.pawfinder.model.Comment;
import com.example.pawfinder.model.User;
import com.example.pawfinder.tools.PrefConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseInstanceService extends FirebaseMessagingService {

    private static PrefConfig prefConfig;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        prefConfig = new PrefConfig(this);
        if (prefConfig.readLoginStatus()) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TOKENFIREBASE",s);
        //sendRegistrationToServer(s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    private void showNotification(String title, String body, Map<String, String> data){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID="com.example.pawfinder";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("PawFinder");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }



        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, ReportDetailActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        resultIntent.putExtra("report_pet_name", data.get("name"));
        resultIntent.putExtra("report_pet_type", data.get("type"));
        resultIntent.putExtra("report_pet_date", data.get("missing_since"));
        resultIntent.putExtra("report_pet_image", data.get("image"));
        resultIntent.putExtra("report_pet_additionalInfo", data.get("additionalInfo"));
        resultIntent.putExtra("report_pet_of_pet", data.get("id"));

        Intent secondIntent = new Intent(this, ViewCommentsActivity.class);
        secondIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        secondIntent.putExtra("view_comments_petsName", data.get("name"));
        secondIntent.putExtra("view_comments_additionalInfo", data.get("additionalInfo"));
        secondIntent.putExtra("view_comments_id",Long.valueOf(data.get("id")));
        secondIntent.putExtra("view_comments_image", data.get("image"));
;
        //Log.d("PETSIMAAGE", pets.get(position).getImage());
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ReportDetailActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        stackBuilder.addNextIntent(secondIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.user)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentInfo("Info")
                .setContentIntent(resultPendingIntent);;
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build()); // 0 is the request code, it should be unique id

    }



    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }



}
