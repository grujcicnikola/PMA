package com.example.pawfinder.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pawfinder.R;
import com.example.pawfinder.dialogs.LocationDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewOnMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOnMapFragment extends Fragment implements OnMapReadyCallback {

        private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
        private GoogleMap mMap;
        private SupportMapFragment supportMapFragment;
        private Marker pet;
        private Double lon;
        private Double lat;


    public ViewOnMapFragment() {
        // Required empty public constructor
    }

    public static ViewOnMapFragment newInstance(Double lon, Double lat) {
        ViewOnMapFragment fragment = new ViewOnMapFragment();
        Bundle args = new Bundle();
        args.putDouble("lon_view_fragment", lon);
        args.putDouble("lat_view_fragment", lat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //preuzmi iz intenta lon lat i ime
        lon = getArguments().getDouble("lon_view_fragment");
        lat = getArguments().getDouble("lat_view_fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_view_on_map, container, false);
        return view;
    }

    private void createMapFragmentAndInflate() {

        supportMapFragment = SupportMapFragment.newInstance();

        //lepnjenje fragmenta na view group
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_view_on_map, supportMapFragment).commit();

        supportMapFragment.getMapAsync(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        createMapFragmentAndInflate();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //callback koji poziva getMapAsync, kada je mapa spremna
        // poziva se samo jednom, a lociranje n puta
        mMap = googleMap;

        addMarker(lon, lat);


        //ako zelmo da reagujemo na klik markera koristimo marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });



    }

    private void addMarker(Double lon, Double lat) {
        LatLng loc = new LatLng(lat, lon);


        pet = mMap.addMarker(new MarkerOptions()
                .title((String) getText(R.string.pet_lost_here))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(loc));
        pet.setFlat(true);
        pet.isVisible();
        pet.showInfoWindow();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc).zoom(14).build();

        //u pozadini ove metode se desava matematika za pomeranje pozicije kamere da gleda u nasu lokaciju
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}
