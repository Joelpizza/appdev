package com.example.skattjakt;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
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
import android.widget.TextView;


import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String EXTRA_MESSAGE = "com.example.skattjakt.MESSAGE";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    public DatabaseHandler db;
    LocationRequest mLocationRequest;
    public int difficulty;
    boolean icons;
    final Context context = this;
    public static Activity firstActivity;
    public boolean clickedinfo = false;
    public boolean newPin = true;
    public double randomLat;
    public double randomLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String message = getIntent().getStringExtra(infopage.DIFFICULTY);
        if(message==null){
            difficulty = 2;
            icons = true;
        }
        else{
            String[] messarr = message.split(",",2);
            difficulty = parseInt(messarr[0]);
            icons=parseBoolean(messarr[1]);
        }
        firstActivity = this;
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
        if(clickedinfo==false){
            clickedinfo=true;
            Intent intent = new Intent ( this,infopage.class);
            List<score> scores = db.getAllscores();
            int totalscore = 0;
            int pins = 0;
            for(score sc : scores) {
                totalscore += sc.getScore();
                pins++;
            }
            String message = "Score: "+totalscore+" pinnar: "+pins+","+difficulty+","+icons;
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng start = new LatLng(1, 1);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((start),12));
        if(icons==false){
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style));
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000); // 1 sec interval
            mLocationRequest.setFastestInterval(1000);
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
            clickedinfo=false;
            List<score> scores = db.getAllscores();
            int totalscore = 0;
            for(score sc : scores) {
                totalscore += sc.getScore();
            }
            TextView textView = findViewById(R.id.textView5);
            textView.setText("Score: "+totalscore);
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);
                if(newPin==true) {
                    double distance;
                    if(difficulty==1){
                        distance = 0.002;
                    }
                    else if(difficulty==2){
                        distance = 0.005;
                    }
                    else if(difficulty==3){
                        distance = 0.008;
                    }
                    else{
                        distance = 0.05;
                    }
                    randomLat =  ThreadLocalRandom.current().nextDouble(location.getLatitude()-distance,location.getLatitude()+distance);
                    randomLong = ThreadLocalRandom.current().nextDouble(location.getLongitude()-distance,location.getLongitude()+distance);
                    LatLng target = new LatLng(randomLat, randomLong);
                    mMap.addMarker(new MarkerOptions().position(target).title("target location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(target));
                    newPin=false;
                }
                if(location.getLatitude()>randomLat-0.0001&&location.getLatitude()<randomLat+0.0001&&location.getLongitude()>randomLong-0.0001&&location.getLongitude()<randomLong+0.0001){
                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    mMap.clear();
                    newPin=true;
                    int score;
                    if(difficulty==1){
                        score=50;
                    }
                    else if(difficulty==2){
                        score=100;
                    }
                    else if(difficulty==3){
                        score=200;
                    }
                    else{
                        score=1000;
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Du hittade platsen");
                    alertDialogBuilder
                            .setMessage("din poäng har ökats med "+score)
                            .setCancelable(false)
                            .setPositiveButton("uppfattat!",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    db.addscore(new score(score));


                }





            }
        }
    };
}
