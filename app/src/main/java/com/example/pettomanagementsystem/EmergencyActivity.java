package com.example.pettomanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class EmergencyActivity extends AppCompatActivity {
    private Button contactButton, vetButton;

    public static final int DEFAULT_UPDATE_INTERVAL = 30000; // 30 seconds
    private static final int FAST_UPDATE_INTERVAL = 5000; // 5 seconds
    private static final int PERMISSION_FINE_LOCATION = 1;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        contactButton = findViewById(R.id.buttonContact);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyActivity.this, ContactListActivity.class);
                startActivity(intent);
            }
        });

        vetButton = findViewById(R.id.buttonVet);
        vetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request location permission if not already granted
                if (ActivityCompat.checkSelfPermission(EmergencyActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
                    }
                } else {
                    // Permission already granted, get location
                    getDeviceLocation();
                }
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Create location request
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(DEFAULT_UPDATE_INTERVAL); // Time interval for updates
        locationRequest.setFastestInterval(FAST_UPDATE_INTERVAL); // Fastest possible update interval
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Request high accuracy
    }

    // Get the device's location
    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, handle this case gracefully (e.g., show a message to the user)
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            // Proceed to open VetListActivity
                            Intent intent = new Intent(EmergencyActivity.this, VetListActivity.class);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            Toast.makeText(EmergencyActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            // Handle case where location is null, perhaps show a message to the user
                            Toast.makeText(EmergencyActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, get location
                    getDeviceLocation();
                } else {
                    // Permission denied, handle this case gracefully (e.g., show a message to the user)
                    Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
                    // You might consider finishing the activity or disabling relevant functionality
                }
                break;
        }
    }
}