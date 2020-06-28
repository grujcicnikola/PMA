package com.example.pawfinder.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pawfinder.R;
import com.example.pawfinder.dialogs.LocationDialog;
import com.example.pawfinder.tools.NetworkTool;
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
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private LocationManager locationManager;
    private GoogleMap mMap;
    private String provider;
    private SupportMapFragment supportMapFragment;
    private AlertDialog alertDialog;
    private Marker marker;
    private Marker pet;

    private ImageView gps_center;
    private Location location;

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        gps_center = view.findViewById(R.id.map_center_icon);

        return view;
    }

    private void createMapFragmentAndInflate() {
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, true);

        supportMapFragment = SupportMapFragment.newInstance();

        //lepnjenje fragmenta na view group
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map, supportMapFragment).commit();

        supportMapFragment.getMapAsync(this);
    }

    private void showLocatonDialog() {
        if (alertDialog == null) {
            alertDialog = new LocationDialog(getActivity()).prepareDialog(2);
        } else {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }

        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        createMapFragmentAndInflate();  //vraca nam najbolji dostupan provajder

        checkAndLocate();

    }

    @Override
    public void onPause() {
        super.onPause();
        //otkacinjemo lisener da ne bi baterija curila
       // locationManager.removeUpdates(this);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle(getText(R.string.location_alert_title))
                        .setMessage(getText(R.string.location_alert_message))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //ovo je callback koji ce biti pozvan kada se proces trazenja permisija zavrsi
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        if (provider != null) {
                            locationManager.requestLocationUpdates(provider, 1, 50, this);
                        }
                    }

                } else if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        if (provider != null) {
                            locationManager.requestLocationUpdates(provider, 1, 50, this);
                        }
                    }

                }
                return;
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //callback koji poziva getMapAsync, kada je mapa spremna
        // poziva se samo jednom, a lociranje n puta
        mMap = googleMap;

        location = null;

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                if(provider!=null) {
                    location = locationManager.getLastKnownLocation(provider);
                }
            } else if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                if(provider != null) {
                    location = locationManager.getLastKnownLocation(provider);
                }
            }
        }

        //ako zelimo da rucno postavljamo markere to radimo
        //dodavajuci click listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (NetworkTool.getConnectivityStatus(getContext()) != NetworkTool.TYPE_NOT_CONNECTED) {

                    if (pet != null) {
                        pet.remove();
                    }

                    pet = mMap.addMarker(new MarkerOptions()
                            .title((String) getText(R.string.pet_lost_here))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            //.icon(BitmapDescriptorFactory.fromBitmap(bm))
                            .position(latLng));
                    pet.setFlat(true);
                    pet.isVisible();
                    pet.showInfoWindow();

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng).zoom(17).build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else{
                    Toast.makeText(getContext(), getText(R.string.map_network), Toast.LENGTH_LONG).show();
                }
            }
        });

        //ako zelmo da reagujemo na klik markera koristimo marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getActivity(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        if(gps_center!=null) {
            gps_center.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (location!=null) {
                        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(loc).zoom(17).build();

                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        addMarker(location);
                    }else{
                        checkAndLocate();
                    }
                }
            });
        }


        if (location != null) {
            addMarker(location);
        }else{
            LatLng loc = new LatLng(43.821111, 21.022447);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(loc).zoom(7).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

    public void checkAndLocate(){

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps && !wifi) {
            showLocatonDialog();    //korisnik nije dopustio ni gps ni wifi i dajemo mu objasnjenje zasto nama to treba
        } else {
            if (checkLocationPermission()) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates: - pokretanje procesa lociranja
                    if (provider != null) {
                        locationManager.requestLocationUpdates(provider, 180, 50, this);
                    }
                    //Toast.makeText(getContext(), "ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                } else if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    if (provider != null) {
                        locationManager.requestLocationUpdates(provider, 180, 50, this);
                        //Toast.makeText(getContext(), "ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    private void addMarker(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        if (marker != null) {
            marker.remove();
        }

        IconGenerator icg = new IconGenerator(getContext());
        icg.setColor(Color.rgb(239, 187, 64)); // green background
        icg.setTextAppearance(R.style.BlackText); // black text
        Bitmap bm = icg.makeIcon(getText(R.string.your_location));

        marker = mMap.addMarker(new MarkerOptions()
                .title((String) getText(R.string.your_location))
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .icon(BitmapDescriptorFactory.fromBitmap(bm))
                .position(loc));
        marker.setFlat(true);
        //marker.isVisible();
        //marker.showInfoWindow();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc).zoom(16).build();

        //u pozadini ove metode se desava matematika za pomeranje pozicije kamere da gleda u nasu lokaciju
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //poziva se kada se status provajdera promeni
        // Toast.makeText(getContext(), "STATUS CHANGED UPALILI STE PROVAJDER HVALA", Toast.LENGTH_SHORT).show();
        checkAndLocate();
    }

    //ako korisnik u toku razda ugasi odredjeni provajder ove dve se zovu
    @Override
    public void onProviderEnabled(String provider) {
        //Toast.makeText(getContext(), "UPALILI STE PROVAJDER HVALA", Toast.LENGTH_SHORT).show();
        checkAndLocate();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //otkacinjemo lisener da ne bi baterija curila
        locationManager.removeUpdates(this);
    }

    public Marker getPet() {
        return pet;
    }
}
