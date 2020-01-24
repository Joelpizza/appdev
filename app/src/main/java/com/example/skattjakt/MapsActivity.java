package com.example.skattjakt;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    public static final String EXTRA_MESSAGE = "com.example.skattjakt.MESSAGE";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    public DatabaseHandler db;
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
        db = new DatabaseHandler(this);
    }

    public void surrender(View view){
        mMap.clear();
        newPin=true;
    }
    public void info(View view){
        Intent intent = new Intent ( this,infopage.class);
        //EditText editText = (EditText) findViewById(R.id.editText2);
        List<score> scores = db.getAllscores();
        int totalscore = 0;
        for(score sc : scores) {
            totalscore += sc.getScore();
        }
        String message = "Score: "+totalscore;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(2000); // 2 sec interval
            mLocationRequest.setFastestInterval(2000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        this.recreate();
                    }
                } else {
                    this.finish();
                }
                return;
            }
        }
    }
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);

                if(newPin==true) {
                    randomLat =  ThreadLocalRandom.current().nextDouble(location.getLatitude()-0.008,location.getLatitude()+0.008);
                    randomLong = ThreadLocalRandom.current().nextDouble(location.getLongitude()-0.008,location.getLongitude()+0.008);
                    LatLng target = new LatLng(randomLat, randomLong);
                    //58.3972
                    //13.877
                    mMap.addMarker(new MarkerOptions().position(target).title("target location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(target));
                    newPin=false;
                }
                if(location.getLatitude()>randomLat-0.0001&&location.getLatitude()<randomLat+0.0001&&location.getLongitude()>randomLong-0.0001&&location.getLongitude()<randomLong+0.0001){
                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    mMap.clear();
                    newPin=true;

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Du hittade platsen");
                    alertDialogBuilder
                            .setMessage("din poäng har ökats med 200")
                            .setCancelable(false)
                            .setPositiveButton("uppfattat!",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    db.addscore(new score(200));
                }





            }
        }

    };
}
