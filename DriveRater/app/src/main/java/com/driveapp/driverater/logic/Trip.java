package com.driveapp.driverater.logic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.driveapp.driverater.MainActivity;
import com.driveapp.driverater.R;
import com.driveapp.driverater.databinding.FragmentTripBinding;
import com.driveapp.driverater.ui.trip.TripFragment;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;

public class Trip extends AppCompatActivity {

    private FragmentTripBinding binding;
    private final int CODE_REQUEST_LOCATION = 1;

    // For storing the speed limit and the users speed
    private class SpeedStorage {
        int userSpeed, speedLimit;
        public SpeedStorage(int uSpeed, int sLimit) {
            this.userSpeed = uSpeed;
            this.speedLimit = sLimit;
        }

        public int UserSpeed() {
            return this.userSpeed;
        }

        public int SpeedLimit() {
            return this.speedLimit;
        }
    }

    private ArrayList<SpeedStorage> speedsAndLimits;
    private FusedLocationProviderClient fusedLocationClient;

    // Loop for getting location repeatedly
    private Handler mHandler;

    private Runnable mDriveDataGetter;

    private void add(int uSpeed, int lSpeed) {
        SpeedStorage s = new SpeedStorage(uSpeed, lSpeed);
        this.speedsAndLimits.add(s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fragment fragment = new TripFragment();
        setContentView(R.layout.fragment_trip);

        super.onCreate(savedInstanceState);

        binding = FragmentTripBinding.inflate(getLayoutInflater());
        setContentView(R.layout.fragment_trip);

        // Request permissions if permissions not already granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    CODE_REQUEST_LOCATION);
        }
        // Has permissions, start
        else {
            this.Run();
        }

        this.speedsAndLimits = new ArrayList<>();

        Button returnButton = findViewById(R.id.tripHomeButton);
        returnButton.setOnClickListener(v -> {
            this.finish();
            Intent i = new Intent(Trip.this, MainActivity.class);
            v.getContext().startActivity(i);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Request permissions if permissions not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE_REQUEST_LOCATION);
        }
        else {
            this.Run();
        }
    }

    @Override
    protected void onDestroy() {
        this.mHandler.removeCallbacks(this.mDriveDataGetter);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Once user selects an option
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Location Request", "Permissions granted");
                this.Run();
            } else {
                Log.e("Location Request", "Permissions denied");
            }
        }
    }

    // For running location loop
    private void Run() {
        // Setup location accessing
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Ensure handler
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        }

        // Create the status checker
        if (this.mDriveDataGetter != null) {
            return;
        }

        this.mDriveDataGetter = new Runnable() {
            private final int mInterval = 1000;
            private final CurrentLocationRequest req = new CurrentLocationRequest.Builder().setGranularity(Granularity.GRANULARITY_FINE).setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY).build();

            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(Trip.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.e("Location Tracking", "No location permissions");
                        return;
                    }

                    // Logs an error, can be ignored
                    Trip.this.fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(Trip.this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d("Trip", ("Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude()));
                            String tex = ("Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
//                            Trip.this.locationText.setText(tex);
                        }
                        else {
                            Log.e("Location Tracking", "Location was NULL");
                        }
                    });
                } finally {
                    Trip.this.mHandler.postDelayed(Trip.this.mDriveDataGetter, this.mInterval);
                }
            }
        };

        // Begin running the location checker
        this.mDriveDataGetter.run();
    }

    public String GetLocation() {
        int size = this.speedsAndLimits.size() - 1;
        return ("Speed: " + this.speedsAndLimits.get(size) + ", Lat: " + this.speedsAndLimits.get(size).UserSpeed() + ", Long: " + this.speedsAndLimits.get(size).SpeedLimit());
    }
}
