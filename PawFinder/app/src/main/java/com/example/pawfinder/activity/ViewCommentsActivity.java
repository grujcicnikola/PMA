
package com.example.pawfinder.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.pawfinder.R;
import com.example.pawfinder.adapters.CommentAdapter;
import com.example.pawfinder.model.Comment;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.NetworkTool;
import com.example.pawfinder.tools.PrefConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import hossamscott.com.github.backgroundservice.RunService;


public class ViewCommentsActivity extends AppCompatActivity implements View.OnClickListener {

    private CommentAdapter commentAdapter;
    private ListView listView;
    private Toolbar toolbar;
    private Pet pet = null;
    private List<Comment> comments = new ArrayList<>();
    private Activity activity;
    private static PrefConfig prefConfig;
    private Pet petInfo = new Pet();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        progressDialog = new ProgressDialog(this);
        setTitle(R.string.comments);
        setContentView(R.layout.view_comments_pet);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity = this;
        prefConfig = new PrefConfig(this);
        Long petsId = null;
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            petsId = bundle.getLong("view_comments_id");
            petInfo.setId(petsId);
            Log.d("PETSID ", "ima ih" + petsId);
            String petsName = bundle.getString("view_comments_petsName");
            String additionalInfo = bundle.getString("view_comments_additionalInfo");
            String petsImage = bundle.getString("view_comments_image");
            TextView pet_name_text = (TextView) findViewById(R.id.pet_name);
            pet_name_text.setText(petsName);

            TextView additionalInfo_text = (TextView) findViewById(R.id.pet_additional);
            //MockupComments.getPets().get(Integer.parseInt(position))
            additionalInfo_text.setText(additionalInfo);
            //Log.d("PETSIMAAGE", petsImage);
            ImageView image = (ImageView) findViewById(R.id.imageView);
            Picasso.get().load(petsImage).into(image);


            Button commentBtn = (Button) findViewById(R.id.comment_button);
            commentBtn.setOnClickListener(this);

            commentAdapter = new CommentAdapter(activity, comments);
            listView = (ListView) findViewById(R.id.shop_comments_list);
            listView.setAdapter(commentAdapter);

            updateComments();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                    Comment comment = (Comment) commentAdapter.getItem(position);

                    final long positionToRemove = comment.getId();
                    if (comment.getPet().getUser().getEmail().equals(prefConfig.readUserEmail()) ||
                            comment.getUser().getEmail().equals(prefConfig.readUserEmail())) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(ViewCommentsActivity.this);
                        adb.setIcon(R.drawable.iconsdelete);
                        adb.setTitle(getResources().getString(R.string.comment_delete_dialog_title));
                        adb.setMessage(getResources().getString(R.string.comment_delete_dialog_text));
                        adb.setNegativeButton(getResources().getString(R.string.no), null);
                        adb.setPositiveButton(getResources().getString(R.string.comment_delete_dialog_ok), new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                deleteComment(positionToRemove);

                            }
                        });
                        adb.show();
                    }
                }
            });


        }
    }

    private void deleteComment(Long positionToRemove) {
        final Call<List<Comment>> call = ServiceUtils.commentService.deleteComment(positionToRemove);
        progressDialog.setTitle(getResources().getString(R.string.comment_progress_title));
        progressDialog.setMessage(getResources().getString(R.string.comment_progress_text));
        progressDialog.setCancelable(false);

        int status = NetworkTool.getConnectivityStatus(getApplicationContext());
        if (status != NetworkTool.TYPE_NOT_CONNECTED) {
            progressDialog.show();
            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                    if (response.code() == 200) {
                        Log.d("VIEW_COMMENTS", "Meesage recieved");
                        comments = response.body();
                        commentAdapter.updateResults(comments);
                        progressDialog.dismiss();
                    } else {
                        Log.d("VIEW_COMMENTS", "Meesage recieved: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    Log.d("VIEW_COMMENTS rez", t.getMessage() != null ? t.getMessage() : "error");
                    progressDialog.dismiss();

                }


            });
        } else {
            Toast.makeText(getApplicationContext(), R.string.noInternet, Toast.LENGTH_LONG).show();

        }
    }

    BroadcastReceiver alarm_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // your logic here
            Log.i("alarm_received", "logic");
            int status = NetworkTool.getConnectivityStatus(getApplicationContext());
            if (status != NetworkTool.TYPE_NOT_CONNECTED) {
                Log.i("alarm_received", "success");
                updateComments();
            } else {
                Log.i("alarm_received", "not connected to internet");
            }
        }
    };


    public void updateComments() {
        final Call<List<Comment>> call = ServiceUtils.commentService.getCommentsByPetsId(petInfo.getId());
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                //Log.d("VIEW_COMMENTS", "ima ih" + response.body().size());

                if (response.code() == 200) {
                    Log.d("VIEW_COMMENTS", "Meesage recieved");
                    comments = response.body();
                    commentAdapter.updateResults(comments);
                } else {
                    Log.d("VIEW_COMMENTS", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.d("VIEW_COMMENTS rez", t.getMessage() != null ? t.getMessage() : "error");

            }


        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.comment_button:
                Log.d("commentbutton", "clickedd");
                EditText messageEditText = (EditText) findViewById(R.id.add_comment_shop);
                String message = String.valueOf(messageEditText.getText());
                addComment(message);
                break;

        }

    }

    public void addComment(String message) {
        //Log.d("messageComment", message);
        int status = NetworkTool.getConnectivityStatus(getApplicationContext());
        if (status != NetworkTool.TYPE_NOT_CONNECTED) {
            if(!message.equals("")) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                EditText messageEditText = (EditText) findViewById(R.id.add_comment_shop);
                //messageEditText.clearFocus();
                messageEditText.getText().clear();
                //ong id, String message, Date date, User user, Pet pet
                User user = new User();
                if (prefConfig.readLoginStatus()) {
                    user.setEmail(prefConfig.readUserEmail());
                    //Log.i("userkkkk", user.getEmail());
                    final Comment comment = new Comment(message, null, user, petInfo);
                    Call<Comment> call = ServiceUtils.commentService.addComment(comment);
                    //Log.d("COMMENTADD", "usao");
                    call.enqueue(new Callback<Comment>() {
                        @Override
                        public void onResponse(Call<Comment> call, Response<Comment> response) {
                            comments.add(response.body());
                            commentAdapter.updateResults(comments);
                            //commentAdapter.notifyDataSetChanged();


                        }

                        @Override
                        public void onFailure(Call<Comment> call, Throwable t) {
                            Log.d("COMMENTADD", t.getMessage() != null ? t.getMessage() : "error");
                            // Toast.makeText(getApplicationContext(), R.string.comment_problem, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.noInternet, Toast.LENGTH_LONG).show();

        }

    }

    public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        /*
        Register BroadcastReceiver to get notification when service is over
         */

        IntentFilter intentFilter = new IntentFilter("alaram_received");
        registerReceiver(alarm_receiver, intentFilter);

        /*
        If you want to repeat the alarm every X sec just add true in your call
        */
        RunService repeat = new RunService(this);
        repeat.call(5, true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(alarm_receiver); // to stop the broadcast when the app is killed
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