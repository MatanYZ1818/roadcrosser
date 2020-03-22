package com.example.roadcross;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.roadcross.ui.login.LoginActivity;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class StartActivity extends AppCompatActivity {

    private Button register;
    private Button login;
    private Button protect;
    private FusedLocationProviderClient fusedLocationClient;

    LocationRequest lRequest;

    int successCode;

    LocationSettingsRequest.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        protect=findViewById(R.id.protect);

        lRequest=new LocationRequest();
        lRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        lRequest.setInterval(3000);

        successCode=GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        builder = new LocationSettingsRequest.Builder().addLocationRequest(lRequest );
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                finish();
            }
        });

        protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationClient= LocationServices.getFusedLocationProviderClient(StartActivity.this);
                getLocation();
            }
        });

    }

    private void getLocation() {
        try {
            //fusedLocationClient is never successful
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Location loc = (Location) location;
                    // Got last known location. In some rare situations this can be null.
                    protect.setText(location.toString());
                    if (location != null) {
                        // Logic to handle location object
                        protect.setText("not found");
                    }
                }
            });
        }   finally {
            protect.setText(String.valueOf( fusedLocationClient.getLastLocation().isSuccessful()));
        }
    }
}
