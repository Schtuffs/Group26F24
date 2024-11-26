package com.driveapp.driverater.logic;

import static android.location.provider.ProviderProperties.ACCURACY_FINE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

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

    private void add(Location loc) {
        // Return if the location does not have a speed
        if (!loc.hasSpeed()) {
            return;
        }
        // Adding the user speed
        int userSpeed = (int)loc.getSpeed();

        // Check if the location is confident with the accuracy of the speed
        if (loc.hasSpeedAccuracy()) {
            // If the speed is lower than the potential accuracy, then the user is most likely not moving
            if (userSpeed < loc.getSpeedAccuracyMetersPerSecond()) {
                userSpeed = 0;
            }
        }
        // Convert to KM/H
        userSpeed *= 3.6;

        String tex = ("Latitude: " + loc.getLatitude() + ", Longitude: " + loc.getLongitude() + ", Speed: " + userSpeed);
        Trip.this.displayText.setText(tex);

        // Speed limit
        // Will most likely use Google Maps API
        int limitSpeed = 0;

        // Adding new location data
        this.speedsAndLimits.add(new SpeedStorage(userSpeed, limitSpeed));
    }

    private TextView displayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        TripFragment fragment = new TripFragment();
        transaction.add(fragment, null);
        transaction.commitNow();

        this.displayText = findViewById(R.id.locationText);
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
            private final CurrentLocationRequest req = new CurrentLocationRequest.Builder().setGranularity(Granularity.GRANULARITY_FINE).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();

            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(Trip.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.e("Location Tracking", "No location permissions");
                        return;
                    }

                    // Logs an error, can be ignored
                    Trip.this.fusedLocationClient.getCurrentLocation(req, null).addOnSuccessListener(Trip.this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d("Trip", ("Accuracy: " + (location.getAccuracy()) + ", Speed: " + location.getSpeed()));
                            Trip.this.add(location);
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
        if (size < 0) {
            return "Size: " + size;
        }
        return ("Speed: " + this.speedsAndLimits.get(size) + ", Lat: " + this.speedsAndLimits.get(size).UserSpeed() + ", Long: " + this.speedsAndLimits.get(size).SpeedLimit());
    }
}
