package com.example.pawfinder.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pawfinder.R;
import com.example.pawfinder.activity.PetDetailActivity;
import com.example.pawfinder.activity.ReportDetailActivity;
import com.example.pawfinder.adapters.MyReportsListAdapter;
import com.example.pawfinder.adapters.PetsListAdapter;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.example.pawfinder.model.User;
import com.example.pawfinder.tools.MockupComments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyReportsFragment extends Fragment {

    private ListView list;

    public MyReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_reports, container, false);
        MyReportsListAdapter adapter = new MyReportsListAdapter(getContext(), MockupComments.getReports());

        list = (ListView) view.findViewById(R.id.reports_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), ReportDetailActivity.class);
                intent.putExtra("reports_position",position);
                startActivity(intent);
            }
        });

        return view;
    }


}
