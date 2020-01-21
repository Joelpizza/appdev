package com.example.skattjakt;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.Manifest;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import java.util.concurrent.ThreadLocalRandom;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    LocationRequest mLocationRequest;

    final Context context = this;

    public boolean newPin = true;
    public double randomLat;
    public double randomLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
    public void surrender(View view){
        mMap.clear();
        newPin=true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        mMap.setMyLocationEnabled(true);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // 5 sec interval
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());



    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);

                if(newPin==true) {
                    randomLat =  ThreadLocalRandom.current().nextDouble(location.getLatitude()-0.005,location.getLatitude()+0.005);
                    randomLong = ThreadLocalRandom.current().nextDouble(location.getLongitude()-0.005,location.getLongitude()+0.005);
                    LatLng target = new LatLng(randomLat, randomLong);
                    //58.3972
                    //13.877
                    mMap.addMarker(new MarkerOptions().position(target).title("target location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(target));
                    newPin=false;
                }
                if(location.getLatitude()>randomLat-0.0001&&location.getLatitude()<randomLat+0.0001&&location.getLongitude()>randomLong-0.0001&&location.getLongitude()<randomLong+0.0001){
                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    newPin=true;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                    alertDialogBuilder.setTitle("Success");


                    alertDialogBuilder
                            .setMessage("du hittade platsen")
                            .setCancelable(false)
                            .setPositiveButton("yippie!",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("hurra!",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    dialog.cancel();
                                }
                            });


                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }





            }
        }

    };
}
