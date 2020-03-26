package com.example.roadcross;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

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

    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
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
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(StartActivity.this);

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
                if (ActivityCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(StartActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            locationRequestCode);

                } else {
                    // already permission granted
                    // get location here
                    //
                    //now THIS fusedLocationClient doesn't work but at least we have permission up there
                    //
                    fusedLocationClient.getLastLocation().addOnSuccessListener(StartActivity.this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            String loc=String.format("%s -- %s",wayLatitude,wayLongitude);
                            Toast.makeText(StartActivity.this, loc, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            Toast.makeText(this, "%s -- %s", Toast.LENGTH_SHORT).show();                        }
                    });
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

}
