package com.mahdi.test.comp304_001_assignment04;
/*
 * Author: Mahdi Moradi - 300951014
 * Final Project - CINEPLEX Ticket Service
 * Date: 18 April 2019
 *
 */

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class AreaActivity extends AppCompatActivity implements
        OnMapReadyCallback, AdapterView.OnItemSelectedListener, GoogleMap.OnInfoWindowClickListener {

    ProgressDialog pd;
    List<String> uniqueProvinces, uniqueCities;
    List<Theatre> filtered;
    String provinceCode, city;
    Spinner spProvinces, spCities;
    Button btnUpdateMap;
    ProgressBar progressBar;
    int progress = 1;
    GoogleMap map;
    BroadRequestReceiver receiver;
    Intent jSONDownload;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        //set Toolbar properties
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarArea);
        toolbar.setTitle("Find Locations");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        checkForInternetPermission();

        movie = (Movie) getIntent().getSerializableExtra("movie");

        //this progress bar is for experiment only, not working
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //register broadcast receiver for service broadcasts
        IntentFilter filter = new IntentFilter("progress_action");
        receiver = new BroadRequestReceiver();
        registerReceiver( receiver, filter);

        spProvinces= findViewById(R.id.spProvinces);
        spCities = findViewById(R.id.spCities);
        spProvinces.setOnItemSelectedListener(this);
        spCities.setOnItemSelectedListener(this);
        btnUpdateMap = findViewById(R.id.btnUpdateMap);
        btnUpdateMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spProvinces.getSelectedItem() != null)
                onResume();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrag);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //it gets the Data from Cineplex API jSON
        //and create a list of Theatres in Statics Class
        //this includes all information about cineplex Theatre locations
        //start the jsonDownload Service
        jSONDownload = new Intent(this, JsonDownloadService.class);
        this.startService(jSONDownload);

    }//end of onCreate Method

    //method to check and request permissions
    private void checkForInternetPermission() {
        boolean internetNotGranted = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED;

        if (internetNotGranted) {
            Log.d("AreaActivity", getString(R.string.read_permission_not_granted));
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }else {
            Toast.makeText(this, "Have Permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.INTERNET)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission denied.
                    Log.d("AreaActivity", getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    //this method loads Theatre data after downloading
    public void loadData(){

        //get unique list of provinces from Theatre list
        List<String> provinces = new ArrayList<>();
        for(Theatre x : Statics.THEATRES){
            provinces.add(x.provinceCode);
        }
        //Hash set will make the list unique
        uniqueProvinces = new ArrayList<String>(new HashSet<String>(provinces));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, uniqueProvinces);
        spProvinces.setAdapter(adapter);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //zoom on canada
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(57.776639, -101.689796), 4f));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.spProvinces:
                //get unique list of cities from Theatre list
                List<String> cities = new ArrayList<>();
                provinceCode = uniqueProvinces.get(position);
                for (Theatre x : Statics.THEATRES) {
                    if (x.provinceCode.equals(provinceCode))
                        cities.add(x.city);
                }
                //Hash set will make the list unique
                uniqueCities = new ArrayList<String>(new HashSet<String>(cities));

                ArrayAdapter<String> adapterCities = new ArrayAdapter<String>(
                        AreaActivity.this, android.R.layout.simple_spinner_item, uniqueCities);
                spCities.setAdapter(adapterCities);

                Toast.makeText(this, provinceCode, Toast.LENGTH_SHORT).show();
                break;

            case R.id.spCities:
                city = uniqueCities.get(position);
                filtered = new ArrayList<>();
                for (Theatre x : Statics.THEATRES) {
                    if (x.provinceCode.equals(provinceCode) && x.city.equals(city)) {
                        filtered.add(x);
                    }
                }//end of for
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(map != null){
            map.clear();
            //add markers
            List<Marker> markers = new ArrayList<>();
            //build bounding area to zoom on the map
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //adding markers and bounding using loop
            for (Theatre x : filtered) {
                    markers.add(map.addMarker(
                            new MarkerOptions()
                                    .position(new LatLng(x.latitude, x.longitude))
                                    .title(x.name)
                                    .snippet("Address: " + x.address1 + x.address2)
                    ));
                    //add markers to bounding area
                    builder.include(new LatLng(x.latitude, x.longitude));
            }//end of for
            LatLngBounds bounds = builder.build();
            int padding = 200; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            //zoom camera to bounding area
            map.animateCamera(cu);

            map.getUiSettings().setZoomControlsEnabled(true);
            map.setInfoWindowAdapter(new CustomInfoWindowAdapter(getLayoutInflater()));
            markers.get(0).showInfoWindow();
            map.setOnInfoWindowClickListener(this);
            map.getUiSettings().setMapToolbarEnabled(false);

        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Theatre selected = null;
        for(Theatre x : filtered){
            if(x.name.equals(marker.getTitle())){
                selected = x;
            }
        }

        Intent infoIntent = new Intent(this, TheatreInfoActivity.class);
        infoIntent.putExtra("theatreInfo", selected);
        infoIntent.putExtra("movie", movie);
        startActivityForResult(infoIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    //back button on action bar doing same as back button press on phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.onBackPressed();
        return true;
    }


    //this class handles the responses from jSon Download Service
    public class BroadRequestReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {
            //get status of process from broadcast by service
            int status = -1;
             status= intent.getIntExtra("status", status );

             switch (status){
                 case 0:
                     //show please wait window when service is going to start
                     if (pd == null) {
                         pd = new ProgressDialog(context);
                         pd.setMessage("Please wait");
                         pd.setCancelable(false);
                         pd.show();
                     }
                     break;
                 case 1:
                     //show please wait window when service is going to start
                     progressBar.setVisibility(View.VISIBLE);
                     progress = intent.getIntExtra("progress", progress);
                     progressBar.setProgress(progress/20);
                     break;
                 case 2:
                     //load data and dismiss please wait after loading all data
                     loadData();
                     progressBar.setVisibility(View.GONE);
                     if (pd.isShowing()){
                         pd.dismiss();
                     }
                     context.stopService(jSONDownload);
                     break;
                 case 3:
                     if (pd.isShowing()){
                         pd.dismiss();
                     }
                     context.stopService(jSONDownload);
                     new AlertDialog.Builder(context)
                             .setTitle("Error")
                             .setMessage("No Internet Connection, Can't load location data")
                             .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int which) {
                                     finish();
                                 }
                             }).show();
                     break;
             }//end of switch
        }//end of onReceive()
    }//end of broadcast receiver

}//end of activity
