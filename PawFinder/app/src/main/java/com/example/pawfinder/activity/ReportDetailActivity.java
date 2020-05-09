package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawfinder.R;
import com.example.pawfinder.adapters.MyReportsListAdapter;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.tools.MockupComments;

public class ReportDetailActivity extends AppCompatActivity {

    private Pet pet;
    private int reports_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.report_toolbar);
        ImageView imgView = (ImageView) findViewById(R.id.pet_report_details_image);
        TextView txtView = (TextView) findViewById(R.id.report_text_view);
        Button buttonFound = (Button) findViewById(R.id.found_btn);
        ImageButton commentsButton= (ImageButton) findViewById(R.id.report_comments);

        MyReportsListAdapter adapter = new MyReportsListAdapter(this, MockupComments.getReports());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {

            reports_position = bundle.getInt("reports_position");
            pet = (Pet) adapter.getItem(reports_position);
            toolbar.setTitle(pet.getName());
            imgView.setImageResource(pet.getImage());

            String text = "Name: " + pet.getName() + "\n"+
                    "Type: " + pet.getType() + "\n" +
                    "Missing since: " + pet.getDateOfLost() + "\n" +
                    "Last seen: ";

            txtView.setText(text);

            buttonFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("tagg", "Pronadjen je  " + pet.getName());
                }
            });

            commentsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ViewCommentsActivity.class);
                    i.putExtra("position_of_pet_report",String.valueOf(reports_position));
                    startActivity(i);
                }
            });
        }
    }
}
