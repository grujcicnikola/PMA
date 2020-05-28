package com.example.pawfinder.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawfinder.R;
import com.example.pawfinder.activity.PetDetailActivity;
import com.example.pawfinder.activity.ReportDetailActivity;
import com.example.pawfinder.adapters.MyReportsListAdapter;
import com.example.pawfinder.adapters.PetsListAdapter;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.MockupComments;
import com.example.pawfinder.tools.PrefConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyReportsFragment extends Fragment {

    private ListView list;
    private List<Pet> pets = new ArrayList<Pet>();
    private static PrefConfig prefConfig;
    TextView text;

    public static  MyReportsFragment newInstance(Bundle bundle){
        MyReportsFragment fragment = new MyReportsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_reports, container, false);
        prefConfig = new PrefConfig(view.getContext());
        list = (ListView) view.findViewById(R.id.reports_list);

        text = (TextView) view.findViewById(R.id.reports_message);

        if(prefConfig.readLoginStatus())
        {
            final Call<List<Pet>> call = ServiceUtils.petService.getPetsByUser(prefConfig.readUserEmail());
            call.enqueue(new Callback<List<Pet>>() {
                @Override
                public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                    if(response.code() == 200)
                    {
                        pets = response.body();

                        if(pets.size() > 0)
                        {
                            MyReportsListAdapter adapter = new MyReportsListAdapter(getContext(), pets);
                            list.setAdapter(adapter);
                        }else
                        {
                            text.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Pet>> call, Throwable t) {

                }
            });


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getContext(), ReportDetailActivity.class);
                    intent.putExtra("report_pet_name",pets.get(position).getName());
                    intent.putExtra("report_pet_type",pets.get(position).getType().toString());
                    intent.putExtra("report_pet_date",pets.get(position).getMissingSince());
                    intent.putExtra("report_pet_image",pets.get(position).getImage());
                    startActivity(intent);
                }
            });
        }

        return view;
    }


}

