package com.example.pawfinder.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawfinder.R;
import com.example.pawfinder.adapters.MyReportsListAdapter;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.MockupComments;
import com.example.pawfinder.tools.NetworkTool;
import com.example.pawfinder.tools.PrefConfig;
import com.example.pawfinder.tools.RangeUtils;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetailActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private static PrefConfig prefConfig;
    private DrawerLayout mDrawerLayout;
    public static Integer nearYouRange;
    private RangeUtils rangeUtils;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }

        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_report_detail);
        prefConfig = new PrefConfig(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        rangeUtils = new RangeUtils(sharedPreferences, this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.report_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imgView = (ImageView) findViewById(R.id.pet_report_details_image);
        Button buttonFound = (Button) findViewById(R.id.found_btn);

        ImageButton commentsButton = (ImageButton) findViewById(R.id.report_comments);

        MyReportsListAdapter adapter = new MyReportsListAdapter(this, MockupComments.getReports());

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            Long id = bundle.getLong("report_pet_id");
            String name = bundle.getString("report_pet_name");
            String type = bundle.getString("report_pet_type");
            String date = bundle.getString("report_pet_date");
            String image = bundle.getString("report_pet_image");
            Boolean is_Found = bundle.getBoolean("is_found");
            if(is_Found)
            {
               buttonFound.setVisibility(View.INVISIBLE);
            }

            buttonFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.setMessage(getResources().getString(R.string.dialog_message));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Call<ResponseBody> call = ServiceUtils.petService.petFound(id);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code() == 200)
                            {
                                progressDialog.dismiss();
                                setResult(Activity.RESULT_CANCELED);
                                finish();
                            }

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            });

            //lokalizacija tipa
            String[] type_values= getResources().getStringArray(R.array.type_values);
            int index_type = Arrays.asList(type_values).indexOf(type);
            String[] type_entries = getResources().getStringArray(R.array.type_entries);
            String type_entry = String.valueOf(type_entries[index_type]);

            toolbar.setTitle(name);
            TextView name_txt = (TextView) findViewById(R.id.report_text_view_name);
            name_txt.setText(name);
            TextView type_txt = (TextView) findViewById(R.id.report_text_view_type);
            type_txt.setText(type_entry);
            TextView date_txt = (TextView) findViewById(R.id.report_text_view_date);
            date_txt.setText(date);

            Picasso.get().load(ServiceUtils.IMAGES_URL + image).into(imgView);


            commentsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                        Intent i = new Intent(getApplicationContext(), ViewCommentsActivity.class);
                        i.putExtra("view_comments_petsName", bundle.getString("report_pet_name"));
                        i.putExtra("view_comments_additionalInfo", bundle.getString("report_pet_additionalInfo"));
                        i.putExtra("view_comments_id", bundle.getLong("report_pet_of_pet"));
                        Log.d("PETSID ", "ima ih" + bundle.getLong("id_of_pet"));
                        i.putExtra("view_comments_image", bundle.getString("report_pet_image"));
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_viewReport);
        /*Menu menuNav = navigationView.getMenu();
        MenuItem mi = menuNav.findItem(R.id.item_number);
        np = (SeekBar) mi.getActionView();
        /*np.setMinValue(2);
        np.setMaxValue(20);
        np.setOnValueChangedListener(onValueChangeListener);*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // set item as selected to persist highlight
                Intent i;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_qr_code:
                        i = new Intent(getApplicationContext(), BarCodeActivity.class);
                        startActivity(i);
                        break;

                    case R.id.navigation_item_item:
                        if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                            Intent missingReport = new Intent(getApplicationContext(), MissingReportFirstPage.class);
                            startActivity(missingReport);
                        }else{
                            Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.navigation_item_change_password:
                        if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                            i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.navigation_item_settings:
                        i = new Intent(getApplicationContext(), PreferenceActivity.class);
                        startActivity(i);
                        break;

                    case R.id.navigation_item_range:
                        showNumberPickerDialog();
                        //Log.d("RANGENUMBER", String.valueOf(nearYouRange));
                        break;

                    case R.id.navigation_item_logout:
                        prefConfig.logout();
                        sendTokenToServer("");//problem ako nema neta
                        Toast.makeText(getApplicationContext(), "User successfully logged out", Toast.LENGTH_SHORT).show();
                        i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finishAffinity();
                        break;

                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                //Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.drawer_user);
        if (prefConfig.readLoginStatus()) {
            navUsername.setText(prefConfig.readUserEmail());
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }

        return true;
    }





    public void showNumberPickerDialog(){
        NumberPicker picker = new NumberPicker(ReportDetailActivity.this);
        picker.setMinValue(1);
        picker.setMaxValue(50);
        picker.setValue(rangeUtils.readRange());

        FrameLayout layout = new FrameLayout(ReportDetailActivity.this);
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        new AlertDialog.Builder( ReportDetailActivity.this)
                .setTitle(getText(R.string.range_title))
                .setView(layout)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    // do something with picker.getValue()
                    picker.getValue();
                    nearYouRange = picker.getValue();
                    rangeUtils.saveRange(nearYouRange);

                    //Log.d("ONRESUME", "tu sam");
                    //tabLayout.getTabAt(1).select();
                    //tabLayout.getTabAt(0).select();
                   /* NearYouFragment frg = (NearYouFragment) viewPagerAdapter.getItem(0);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.attach(frg);
                    ft.commit();*/
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    NumberPicker.OnValueChangeListener onValueChangeListener =
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(getApplicationContext(),
                            "selected number " + numberPicker.getValue(), Toast.LENGTH_SHORT);
                    nearYouRange = numberPicker.getValue();
                }
            };

    private void sendTokenToServer(String token) {
        // TODO: Implement this method to send token to your app server.


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

