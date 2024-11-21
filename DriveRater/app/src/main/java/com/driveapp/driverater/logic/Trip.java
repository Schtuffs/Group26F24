package com.driveapp.driverater.logic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.driveapp.driverater.R;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;

public class Trip extends AppCompatActivity {
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

    Runnable mStatusChecker = new Runnable() {
        private LocationManager fusedLocationClient;
        private int mInterval = 1000;

        @Override
        public void run() {
            try {
                CurrentLocationRequest req = new CurrentLocationRequest.Builder().setGranularity(Granularity.GRANULARITY_FINE).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();
                if (ActivityCompat.checkSelfPermission(Trip.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Trip.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("Location Tracking", "No location permissions");
                    return;
                }

                Trip.this.fusedLocationClient.getCurrentLocation(req, null).addOnSuccessListener(Trip.this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        Log.d("Trip", ("Long: " + location.getLongitude() + ", Lat: " + location.getLatitude()));
                        Trip.this.add(location);
                    }
                    else {
                        Log.e("Location Tracking", "Location was NULL");
                    }
                });
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    private void add(Location l) {
        SpeedStorage s = new SpeedStorage((int)l.getLongitude(), (int)l.getLatitude());
        this.speedsAndLimits.add(s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request permissions if permissions not already granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    CODE_REQUEST_LOCATION);
        }

        // Change view to the view screen
        setContentView(R.layout.fragment_trip);

        this.speedsAndLimits = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Once user selects an option
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Location Request", "Permissions granted");
                this.mHandler = new Handler(Looper.getMainLooper());
                this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mStatusChecker.run();
            } else {
                Log.e("Location Request", "Permissions denied");
            }
        }
    }

    public String GetRecentLocation() {
        return ("Long: " + (this.speedsAndLimits.get(this.speedsAndLimits.size() - 1).userSpeed) + ", Lat: " + (this.speedsAndLimits.get(this.speedsAndLimits.size() - 1).speedLimit));
    }
}
