
package com.example.pawfinder.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.pawfinder.R;
import com.example.pawfinder.adapters.CommentAdapter;
import com.example.pawfinder.adapters.PetsListAdapter;
import com.example.pawfinder.model.Comment;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.MockupComments;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCommentsActivity extends AppCompatActivity implements View.OnClickListener {

    private CommentAdapter commentAdapter;
    private ListView listView;
    private Toolbar toolbar;
    private Pet pet = null;
    private List<Comment> comments = new ArrayList<>();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setTitle(R.string.comments);
        setContentView(R.layout.view_comments_pet);
        activity = this;
        Long petsId = null;
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            petsId = bundle.getLong("view_comments_id");
            Log.d("PETSID ", "ima ih" + petsId);
            String petsName = bundle.getString("view_comments_petsName");
            String additionalInfo = bundle.getString("view_comments_additionalInfo");
            String petsImage = bundle.getString("view_comments_image");
            TextView pet_name_text = (TextView) findViewById(R.id.pet_name);
            pet_name_text.setText(petsName);

            TextView additionalInfo_text = (TextView) findViewById(R.id.pet_additional);
            //MockupComments.getPets().get(Integer.parseInt(position))
            additionalInfo_text.setText(additionalInfo);
            Log.d("PETSIMAAGE", petsImage);
            ImageView image = (ImageView) findViewById(R.id.imageView);
            Picasso.get().load(ServiceUtils.IMAGES_URL + petsImage).into(image);
            final Call<List<Comment>> call1 = ServiceUtils.commentService.getCommentsByPetsId(petsId);
            Button commentBtn = (Button) findViewById(R.id.comment_button);
            commentBtn.setOnClickListener(this);
            call1.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    Log.d("VIEW_COMMENTS", "ima ih" + response.body().size());
                    comments = response.body();
                    CommentAdapter adapter = new CommentAdapter(activity, comments);
                    listView = (ListView) findViewById(R.id.shop_comments_list);
                    listView.setAdapter(adapter);
                    if (response.code() == 200) {
                        Log.d("VIEW_COMMENTS", "Meesage recieved");

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


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.comment_button:
                Log.d("commentbutton", "clickedd");
                break;

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
}