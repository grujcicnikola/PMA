package com.example.pawfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.pawfinder.R;
import com.example.pawfinder.fragments.MapsFragment;
import com.example.pawfinder.fragments.ViewOnMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewOnMapActivity extends AppCompatActivity {

    public static FragmentManager fm;
    public ViewOnMapFragment viewOnMapFragment;
    private Double lon;
    private Double lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_view_on_map);
        setTitle(R.string.title_activity_view_on_map);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lon = bundle.getDouble("lon_view");
            lat = bundle.getDouble("lat_view");
        }


        fm = getSupportFragmentManager();
        if (findViewById(R.id.pet_location_map) != null) {
            //fragment je vec dodat - activiy je resumed
            if (savedInstanceState != null) {
                return;
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            viewOnMapFragment = ViewOnMapFragment.newInstance(lon, lat);

            ft.add(R.id.pet_location_map, viewOnMapFragment, null);
            ft.commit();
        }
    }

}

