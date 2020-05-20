package com.example.pawfinder.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.pawfinder.R;
import com.example.pawfinder.adapters.CommentAdapter;
import com.example.pawfinder.model.Comment;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.tools.MockupComments;

import java.util.ArrayList;
import java.util.List;

public class ViewCommentsActivity extends AppCompatActivity {

    private CommentAdapter commentAdapter;
    private ListView listView;
    private Toolbar toolbar;
    private Pet pet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darktheme);
        }
        setTitle(R.string.comments);
        setContentView(R.layout.view_comments_pet);


        Intent intent = getIntent();
        String position = intent.getStringExtra("position_of_pet");

        if(position == null)
        {
            position = intent.getStringExtra("position_of_pet_report");
            pet = MockupComments.getReports().get(Integer.parseInt(position));
        }else
        {
            pet = MockupComments.getPets().get(Integer.parseInt(position));
        }

        TextView pet_name_text = (TextView) findViewById(R.id.pet_name);
        pet_name_text.setText(pet.getName());

        TextView pet_dateOfLost_text = (TextView) findViewById(R.id.pet_additional);
        //MockupComments.getPets().get(Integer.parseInt(position))
        pet_dateOfLost_text.setText(pet.getDescription());

        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(pet.getImage());

        //dodavanje komentara
        //pribaljanje za kog ljubmica se uzimaju komentari
//        Pet pet = MockupComments.getPets().get(Integer.parseInt(position));
        List<Comment> comments = MockupComments.getThisPetsComments(pet);
        CommentAdapter adapter = new CommentAdapter(this, comments);
        listView = (ListView) findViewById(R.id.shop_comments_list);
        listView.setAdapter(adapter);

    }


}