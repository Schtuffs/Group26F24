package com.driveapp.driverater.logic;

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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Trip extends AppCompatActivity {
    // For how much time is in between each lookup
    // Static so that if changed during runtime, it carries into the next trip
    public static int mInterval = 1000, mAddInterval = 8;

    private FragmentTripBinding binding;
    private final int CODE_REQUEST_LOCATION = 1;

    private ArrayList<SpeedStorage> speedsAndLimits;
    private FusedLocationProviderClient fusedLocationClient;

    // Loop for getting location repeatedly
    private Handler mHandler;

    private Runnable mDriveDataGetter;

    private TextView displayText;

    private void add(Location loc) {
        // Gets the most recently added speed object
        int index = this.speedsAndLimits.size() - 1;
        if (index >= 0) {
            this.speedsAndLimits.add(new SpeedStorage(loc, this.speedsAndLimits.get(index)));
        }
        else {
            this.speedsAndLimits.add(new SpeedStorage(loc, null));
        }
        this.displayText.setText(this.GetRecentSpeedData());
    }

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
            private int totalRuns = 0;
            private final CurrentLocationRequest req = new CurrentLocationRequest.Builder().setGranularity(Granularity.GRANULARITY_FINE).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();

            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(Trip.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.e("Location Tracking", "No location permissions");
                        return;
                    }

                    this.totalRuns++;

                    // Logs an error, can be ignored
                    Trip.this.fusedLocationClient.getCurrentLocation(req, null).addOnSuccessListener(Trip.this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            if (totalRuns >= 8) {
                                Trip.this.add(location);
                                this.totalRuns = 0;
                            }
                        }
                        else {
                            Log.e("Location Tracking (Run)", "Location was NULL");
                        }
                    });
                } finally {
                    Trip.this.mHandler.postDelayed(Trip.this.mDriveDataGetter, Trip.Interval());
                }
            }
        };

        // Begin running the location checker
        this.mDriveDataGetter.run();
    }

    public static int Interval() {
        return mInterval;
    }

    public static int AddInterval() {
        return mAddInterval;
    }

    public String GetRecentSpeedData() {
        int size = this.speedsAndLimits.size() - 1;
        if (size < 0) {
            return "No data";
        }
        DecimalFormat format = new DecimalFormat("#0.0");
        return ("Speed: " + format.format(this.speedsAndLimits.get(size).UserSpeed()) + "KM/H, Acceleration: " + format.format(this.speedsAndLimits.get(size).Acceleration()) + "km/h^2");
    }
}
