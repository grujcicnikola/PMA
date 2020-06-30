
package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pawfinder.R;
import com.example.pawfinder.fragments.MapsFragment;
import com.example.pawfinder.tools.NetworkTool;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends AppCompatActivity {

    public static FragmentManager fm;
    public MapsFragment mapsFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent help = getIntent();
       /* Toast.makeText(this, help.getStringExtra("PET_NAME"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, help.getStringExtra("PET_GENDER"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, help.getStringExtra("PET_TYPE"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, help.getStringExtra("PET_DATE_LOST"), Toast.LENGTH_SHORT).show();*/


        fm = getSupportFragmentManager();
        if (findViewById(R.id.map) != null) {
            //fragment je vec dodat - activiy je resumed
            if (savedInstanceState != null) {
                return;
            }

        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_near_you, supportMapFragment).commit();*/

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            mapsFragment = MapsFragment.newInstance();

            ft.add(R.id.map, mapsFragment, null);
            ft.commit();
        }

        Button next = findViewById(R.id.btn_missing_report_second);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkTool.getConnectivityStatus(getApplicationContext()) != NetworkTool.TYPE_NOT_CONNECTED) {
                    if (mapsFragment.getPet() == null) {
                        Toast.makeText(getApplicationContext(), getText(R.string.location_blank), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MissingReportThirdPage.class);
                        intent.putExtra("PET_LOST_LON", mapsFragment.getPet().getPosition().longitude);
                        intent.putExtra("PET_LOST_LAT", mapsFragment.getPet().getPosition().latitude);

                        intent.putExtra("PET_NAME", help.getStringExtra("PET_NAME"));
                        intent.putExtra("PET_GENDER", help.getStringExtra("PET_GENDER"));
                        intent.putExtra("PET_TYPE", help.getStringExtra("PET_TYPE"));
                        intent.putExtra("PET_DATE_LOST", help.getStringExtra("PET_DATE_LOST"));
                        startActivityForResult(intent,2);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), getText(R.string.network), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2){
            finish();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()== android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

