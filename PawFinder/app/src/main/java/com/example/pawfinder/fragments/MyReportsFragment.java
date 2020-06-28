package com.example.pawfinder.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.example.pawfinder.db.DBContentProvider;
import com.example.pawfinder.db.PetSQLHelper;
import com.example.pawfinder.model.Address;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.MockupComments;
import com.example.pawfinder.tools.NetworkTool;
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
    private MyReportsListAdapter adapter;
    TextView text;

    public static MyReportsFragment newInstance(Bundle bundle) {
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkTool.getConnectivityStatus(getContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
            if (prefConfig.readLoginStatus()) {
                final Call<List<Pet>> call = ServiceUtils.petService.getPetsByUser(prefConfig.readUserEmail());
                call.enqueue(new Callback<List<Pet>>() {
                    @Override
                    public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                        if (response.code() == 200) {
                            pets = response.body();

                            if (pets.size() > 0) {
                                adapter = new MyReportsListAdapter(getContext(), pets);
                                list.setAdapter(adapter);
                            } else {
                                text.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Pet>> call, Throwable t) {

                    }
                });



            }
        }else{
            fillView();
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), ReportDetailActivity.class);
                intent.putExtra("report_pet_name", pets.get(position).getName());
                intent.putExtra("report_pet_type", pets.get(position).getType().toString());
                intent.putExtra("report_pet_date", pets.get(position).getMissingSince());
                intent.putExtra("report_pet_image", pets.get(position).getImage());
                intent.putExtra("report_pet_additionalInfo", pets.get(position).getAdditionalInfo());
                intent.putExtra("report_pet_of_pet", pets.get(position).getId());
                Log.d("PETSIMAAGE", pets.get(position).getImage());
                startActivity(intent);
            }
        });
    }

    public void fillView(){
        String[] allColumns = { PetSQLHelper.COLUMN_ID,
                PetSQLHelper.COLUMN_NAME, PetSQLHelper.COLUMN_TYPE, PetSQLHelper.COLUMN_GENDER,
                PetSQLHelper.COLUMN_ADDITIONALINFO, PetSQLHelper.COLUMN_IMAGE, PetSQLHelper.COLUMN_MISSINGSINCE,
                PetSQLHelper.COLUMN_OWNERSPHONE, PetSQLHelper.COLUMN_ISFOUND, PetSQLHelper.COLUMN_USER,
                PetSQLHelper.COLUMN_LON, PetSQLHelper.COLUMN_LAT, PetSQLHelper.COLUMN_SYNCSTATUS};
        /*Cursor cursor = getActivity().getContentResolver().query(DBContentProvider.CONTENT_URI_PET, allColumns, null, null,
                null);*/
        String selection = "user = ?";
        String[] selectionArgs = {prefConfig.readUserEmail()};
        Cursor cursor = getActivity().getContentResolver().query(DBContentProvider.CONTENT_URI_PET, allColumns, selection, selectionArgs,
                "date("+PetSQLHelper.COLUMN_MISSINGSINCE+")" + " DESC");

        List<Pet> petView = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            Pet c = new Pet();
            for (int i = 0; i < cursor.getCount(); i++){
                String email =  cursor.getString(9);
                Log.d("REP", email);
               // if (email.equals(prefConfig.readUserEmail())) {
                    Long id = Long.parseLong(cursor.getString(0));
                    PetType type = PetType.valueOf(cursor.getString(2));
                    String name = cursor.getString(1);
                    PetGender gender = PetGender.valueOf(cursor.getString(3));
                    String additional = cursor.getString(4);
                    String image = cursor.getString(5);
                    String missingSince = cursor.getString(6);
                    String ownersPhone = cursor.getString(7);
                    boolean isFound = Boolean.valueOf(cursor.getString(8));

                    User user = new User();
                    user.setEmail(email);

                    Address address = new Address(Double.parseDouble(cursor.getString(10)), Double.parseDouble(cursor.getString(11)));
                    boolean isSent = Boolean.valueOf(cursor.getString(12));

                    Log.d("petList ", "ima ih" + " " + type + " " + name + " " + missingSince);
                    // if (isSent != false) {    ovo ako ne budemo hteli da prikazujemo
                    c = new Pet(type, name, gender, additional, image, missingSince, ownersPhone, isFound, user, address, isSent);
                    petView.add(c);
                    //}


              //  }
                cursor.moveToNext();
            }
            // always close the cursor
            cursor.close();
        }

        pets = petView;
        adapter = new MyReportsListAdapter(getContext(), pets);
        list.setAdapter(adapter);

    }

}

