package com.example.pawfinder.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import okhttp3.ResponseBody;
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
    private TextView text;
    private TextView textLogin;
    private ProgressDialog progressDialog;

    public static MyReportsFragment newInstance() {
        MyReportsFragment fragment = new MyReportsFragment();
        //fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_reports, container, false);
        prefConfig = new PrefConfig(view.getContext());
        progressDialog = new ProgressDialog(view.getContext());

        list = (ListView) view.findViewById(R.id.reports_list);
        text = (TextView) view.findViewById(R.id.reports_message);
        textLogin = (TextView) view.findViewById(R.id.reports_login_message);

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
                            List<Pet> good = new ArrayList<Pet>();
                            for (Pet p : response.body()) {
                                if (p.isDeleted() == false) {
                                    good.add(p);
                                }
                            }
                            pets = good;

                            if (pets.size() > 0) {
                                adapter = new MyReportsListAdapter(getContext(), pets);
                                list.setAdapter(adapter);

                                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                                       new AlertDialog.Builder(getContext())
                                               .setIcon(R.drawable.iconsdelete)
                                               .setTitle(R.string.comment_delete_dialog_title)
                                               .setMessage(R.string.comment_delete_dialog_text)
                                               .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {

                                                       //Brisanjeeeeeee
                                                       Pet pet = (Pet) adapter.getItem(position);
                                                       deleteReport(pet.getId(), prefConfig.readUserEmail());
                                                   }
                                               }).setNegativeButton(R.string.no, null)
                                                .show();

                                        return true;
                                    }
                                });
                            } else {
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
                        intent.putExtra("report_pet_id", pets.get(position).getId());
                        intent.putExtra("report_pet_name", pets.get(position).getName());
                        intent.putExtra("report_pet_type", pets.get(position).getType().toString());
                        intent.putExtra("report_pet_date", pets.get(position).getMissingSince());
                        intent.putExtra("report_pet_image", pets.get(position).getImage());
                        intent.putExtra("report_pet_additionalInfo", pets.get(position).getAdditionalInfo());
                        intent.putExtra("report_pet_of_pet", pets.get(position).getId());
                        intent.putExtra("is_found", pets.get(position).isFound());
                        Log.d("PETSIMAAGE", pets.get(position).getImage());
                        startActivity(intent);
                    }
                });
            }else
            {
                pets.clear();
                textLogin.setVisibility(View.VISIBLE);
            }
        }else{
            fillView();
        }

    }

    public void fillView(){
        String[] allColumns = { PetSQLHelper.COLUMN_ID,
                PetSQLHelper.COLUMN_NAME, PetSQLHelper.COLUMN_TYPE, PetSQLHelper.COLUMN_GENDER,
                PetSQLHelper.COLUMN_ADDITIONALINFO, PetSQLHelper.COLUMN_IMAGE, PetSQLHelper.COLUMN_MISSINGSINCE,
                PetSQLHelper.COLUMN_OWNERSPHONE, PetSQLHelper.COLUMN_ISFOUND, PetSQLHelper.COLUMN_USER,
                PetSQLHelper.COLUMN_LON, PetSQLHelper.COLUMN_LAT, PetSQLHelper.COLUMN_SYNCSTATUS, PetSQLHelper.COLUMN_DELETED};
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
                    String[] parts =  missingSince.split("-");
                    String date = "";
                    if (parts.length == 3) {
                        date = parts[2] + "/" + parts[1] + "/" + parts[0];
                    }


                    String ownersPhone = cursor.getString(7);
                    boolean isFound = Boolean.valueOf(cursor.getString(8));

                    User user = new User();
                    user.setEmail(email);

                    Address address = new Address(Double.parseDouble(cursor.getString(10)), Double.parseDouble(cursor.getString(11)));
                    boolean isSent = Boolean.valueOf(cursor.getString(12));
                    boolean isDeleted = Boolean.valueOf(cursor.getString(13));

                    Log.d("petList ", "ima ih" + " " + type + " " + name + " " + missingSince);
                    // if (isSent != false) {    ovo ako ne budemo hteli da prikazujemo
                    if (isDeleted == false) {
                        c = new Pet(type, name, gender, additional, image, date, ownersPhone, isFound, user, address, isSent);
                        petView.add(c);
                    }
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

    public void deleteReport(Long id, String email)
    {
        progressDialog.setTitle(getResources().getString(R.string.comment_progress_title));
        progressDialog.setMessage(getResources().getString(R.string.comment_progress_text));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<List<Pet>> call = ServiceUtils.petService.deleteReport(id, email);
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();

                    List<Pet> newList = new ArrayList<Pet>();
                    for (Pet p : response.body()) {
                        if (p.isDeleted() == false) {
                            newList.add(p);
                        }
                    }
                    pets = newList;
                    adapter.updateResults(pets);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {

            }
        });

    }

}

