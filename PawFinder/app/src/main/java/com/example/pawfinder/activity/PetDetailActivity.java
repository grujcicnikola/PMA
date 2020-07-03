package com.example.pawfinder.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawfinder.R;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.User;
import com.example.pawfinder.service.ServiceUtils;
import com.example.pawfinder.tools.NetworkTool;
import com.example.pawfinder.tools.PrefConfig;
import com.example.pawfinder.tools.RangeUtils;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetDetailActivity extends AppCompatActivity {

    private ArrayList<Pet> pets = new ArrayList<Pet>();
    private int position_of_pet;
    private Button petsLocation;
    private DrawerLayout mDrawerLayout;
    private Double lon;
    private Double lat;
    private static PrefConfig prefConfig;
    public static Integer nearYouRange;
    private RangeUtils rangeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_pet_detail);

        prefConfig = new PrefConfig(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        rangeUtils = new RangeUtils(sharedPreferences, this);
        rangeUtils.setRange();
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imgView = (ImageView) findViewById(R.id.pet_details_image);
        ImageButton imageButton = (ImageButton) findViewById(R.id.buttonViewComments);
        if(savedInstanceState ==null){
            Log.i("SAVEDD","null");
        }else{
            Log.i("SAVEDD","nije null");
        }
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String name = bundle.getString("petsName");
            String type = bundle.getString("petsType");
            String gender = bundle.getString("petsGender");
            String email = bundle.getString("ownersEmail");
            String phone = bundle.getString("ownersPhone");
            String info = bundle.getString("additionalInfo");
            String image = bundle.getString("image");
            String date = bundle.getString("date");
            lon = bundle.getDouble("lon_pets");
            lat = bundle.getDouble("lat_pets");

            //lokalizacija tipa
            String[] type_values= getResources().getStringArray(R.array.type_values);
            int index_type = Arrays.asList(type_values).indexOf(type);
            String[] type_entries = getResources().getStringArray(R.array.type_entries);
            String type_entry = String.valueOf(type_entries[index_type]);

            //lokalizacija pola
            String[] gender_values= getResources().getStringArray(R.array.genders_values);
            int index_gender = Arrays.asList(gender_values).indexOf(gender);
            String[] gender_entries = getResources().getStringArray(R.array.genders_entries);
            String gender_entry = String.valueOf(gender_entries[index_gender]);

            TextView name_txt = (TextView) findViewById(R.id.pet_details_text_name);
            name_txt.setText(name);
            TextView type_txt = (TextView) findViewById(R.id.pet_details_text_type);
            type_txt.setText(type_entry);
            TextView gender_txt = (TextView) findViewById(R.id.pet_details_text_gender);
            gender_txt.setText(gender_entry);
            TextView missing_txt = (TextView) findViewById(R.id.pet_details_text_missing);
            missing_txt.setText(date);
            TextView email_txt = (TextView) findViewById(R.id.pet_details_text_o_email);
            email_txt.setText(email);
            TextView phone_txt = (TextView) findViewById(R.id.pet_details_text_o_phone);
            phone_txt.setText(phone);
            TextView additional_txt = (TextView) findViewById(R.id.pet_details_text_additional);
            additional_txt.setText(info);

            toolbar.setTitle(name);
            Picasso.get().load(image).into(imgView);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                        Log.d("ima", " interneta if");
                        Intent intent = new Intent(getApplicationContext(), ViewCommentsActivity.class);
                        //i.putExtra("position_of_pet",String.valueOf(position_of_pet));
                        intent.putExtra("view_comments_petsName", bundle.getString("petsName"));
                        intent.putExtra("view_comments_additionalInfo", bundle.getString("additionalInfo"));
                        intent.putExtra("view_comments_id", bundle.getLong("id_of_pet"));
                        Log.d("PETSID ", "ima ih" + bundle.getLong("id_of_pet"));
                        Log.d("PETSI* ", "ima ih" + bundle.getString("id_of_pet"));
                        intent.putExtra("view_comments_image", bundle.getString("image"));
                        startActivity(intent);
                    }else{
                        Log.d("ima", "nema interneta if");
                        Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            petsLocation = findViewById(R.id.bView_on_map);
            petsLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                        Log.d("ima", " interneta if");
                        Intent intent = new Intent(getApplicationContext(), ViewOnMapActivity.class);
                        intent.putExtra("lon_view", lon);
                        intent.putExtra("lat_view", lat);
                        startActivity(intent);
                    }else{
                        Log.d("ima", "nema interneta if");
                        Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_viewDetail);
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
                        //Toast.makeText(getApplicationContext(), getText(R.string.logout_success), Toast.LENGTH_SHORT).show();
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
        boolean googleLogin = prefConfig.readUserGoogleStatus();
        if(!googleLogin)
        {
            Menu menuNav = navigationView.getMenu();
            MenuItem item = menuNav.findItem(R.id.navigation_item_change_password);
            item.setVisible(true);
        }
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




    public void showNumberPickerDialog(){
        NumberPicker picker = new NumberPicker(PetDetailActivity.this);
        picker.setMinValue(1);
        picker.setMaxValue(50);
        picker.setValue(rangeUtils.readRange());

        FrameLayout layout = new FrameLayout(PetDetailActivity.this);
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        new AlertDialog.Builder( PetDetailActivity.this)
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
                    //Toast.makeText(getApplicationContext(),
                    //       "selected number " + numberPicker.getValue(), Toast.LENGTH_SHORT);
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
