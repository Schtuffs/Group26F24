package com.driveapp.driverater.logic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class Trip extends AppCompatActivity {
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
                if (ActivityCompat.checkSelfPermission(Trip.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Trip.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Trip.this.fusedLocationClient.getCurrentLocation(req, null).addOnSuccessListener(Trip.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.v("Granularity", "Gran: " + req.getGranularity());
                            Log.v("Trip", ("Long: " + location.getLongitude() + ", Lat: " + location.getLatitude()));
                            Trip.this.add(location);
                        }
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
        this.speedsAndLimits = new ArrayList<>();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mStatusChecker.run();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacks(mStatusChecker);
    }
}
