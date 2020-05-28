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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawfinder.R;
import com.example.pawfinder.adapters.PetsListAdapter;
import com.example.pawfinder.dialogs.LocationDialog;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PinData;
import com.example.pawfinder.service.ServiceUtils;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearYouFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private LocationManager locationManager;
    private GoogleMap mMap;
    private String provider;
    private SupportMapFragment supportMapFragment;
    private AlertDialog alertDialog;
    private Marker myMarker;
    private List<Marker> petsLocation;
    private List<PinData> petsMarkerPinData;


   public static  NearYouFragment newInstance(){
       return new NearYouFragment();
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //spona izmedju android apija i case app, vraca info za lokacije
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_near_you, container, false);
        return view;
    }

    private void createMapFragmentAndInflate() {
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, true);

        supportMapFragment = SupportMapFragment.newInstance();

        //lepnjenje fragmenta na view group
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_near_you, supportMapFragment).commit();

        supportMapFragment.getMapAsync(this);
    }

    private void showLocatonDialog() {
        if (alertDialog == null) {
            alertDialog = new LocationDialog(getActivity()).prepareDialog(1);
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

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps && !wifi) {
            showLocatonDialog();    //korisnik nije dopustio ni gps ni wifi i dajemo mu objasnjenje zasto nama to treba
        } else {
            if (checkLocationPermission()) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates: - pokretanje procesa lociranja
                    locationManager.requestLocationUpdates(provider, 180, 100, this);
                    Toast.makeText(getContext(), "ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                }else if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    //Request location updates:
                    locationManager.requestLocationUpdates(provider, 180, 100, this);
                    Toast.makeText(getContext(), "ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                }
            }
        }

   }

    @Override
    public void onPause() {
        super.onPause();
        //otkacinjemo lisener da ne bi baterija curila
        locationManager.removeUpdates(this);
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
                        locationManager.requestLocationUpdates(provider, 180, 100, this);
                    }

                } else if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 180, 100, this);
                    }

                }
                return;
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       //callback koji poziva getMapAsync, kada je mapa spremna
        // poziva se samo jednom, a lociranje n puta
        mMap = googleMap;

        Location location = null;

        if (mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    if (marker.getId() == myMarker.getId()) {
                        return null;
                    }

                    if (petsMarkerPinData != null) {
                        for (PinData p : petsMarkerPinData) {
                            if (marker.getId().equals(p.getMarkerId())) {
                                View v = getLayoutInflater().inflate(R.layout.pin_data, null);

                                TextView name = v.findViewById(R.id.name_pin);
                                TextView phone = v.findViewById(R.id.phone_pin);
                                ImageView image = v.findViewById(R.id.pin_image);

                                image.setImageResource(R.drawable.avatar);
                                Picasso.get().load(ServiceUtils.IMAGES_URL + p.getImageURL()).into(image);

                                name.setText(p.getName());
                                phone.setText(p.getPhone());
                                return v;
                            }
                        }
                    }

                    return null;
                }
            });
        }

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                location = locationManager.getLastKnownLocation(provider);
            }else if(ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                //Request location updates:
                location = locationManager.getLastKnownLocation(provider);
            }
        }



        //ako zelmo da reagujemo na klik markera koristimo marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });



        if (location != null) {
            addMarker(location);
            callForPets(location);
        }

    }

    @Override
    public void onLocationChanged(final Location location) {
       //poziva se svaki put kada manager proracuna novu vrednost i vrati novu lokaciju
       //imamo referencu na mapu, proveravamo da li je spremna i ako jeste iscrtamo marker
        if (mMap != null) {
            //dodavanje kucica i macica
            addMarker(location);
            petsLocation = new ArrayList<>();
            petsMarkerPinData = new ArrayList<>();
            callForPets(location);
        }
    }

    private void addMarker(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        if (myMarker != null) {
            myMarker.remove();
        }


        myMarker = mMap.addMarker(new MarkerOptions()
                .title((String) getText(R.string.your_location))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(loc));
        myMarker.setFlat(true);

        myMarker.isVisible();
        myMarker.showInfoWindow();


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc).zoom(14).build();

        //u pozadini ove metode se desava matematika za pomeranje pozicije kamere da gleda u nasu lokaciju
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addMarkerPet(Double lat, Double lon, String urlImage, String name, String phone) {
        LatLng loc = new LatLng(lat, lon);

        /*if (petsLocation.size() != 0) {
            petsLocation.clear();
        }*/

        if(petsLocation == null){
            petsLocation = new ArrayList<>();
            petsMarkerPinData = new ArrayList<>();
        }

        Marker m = mMap.addMarker(new MarkerOptions()
                .title(name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(loc));
        m.setFlat(true);
        m.isVisible();
        petsLocation.add(m);
        petsMarkerPinData.add(new PinData(m.getId(), urlImage, name, phone));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //poziva se kada se status provajdera promeni
    }

    //ako korisnik u toku razda ugasi odredjeni provajder ove dve se zovu
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getContext(), "UPALILI STE PROVAJDER HVALA", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getContext(), R.string.near_you_message, Toast.LENGTH_SHORT).show();
    }

    public void callForPets(final Location location){
        Call<List<Pet>> call = ServiceUtils.petService.getMissing();
        Log.d("PETS","usao");
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                Log.d("NESTALI","ima ih" + response.body().size());
                for (Pet pet : response.body()) {
                    float[] results = new float[1];
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), pet.getAddress().getLat(), pet.getAddress().getLon(), results);
                    float distanceInMeters = results[0];
                    if (distanceInMeters < 10000) {
                        addMarkerPet(pet.getAddress().getLat(),pet.getAddress().getLon(), pet.getImage(), pet.getName(), pet.getOwnersPhone());

                    }
                }
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}
