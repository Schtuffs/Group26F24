package com.driveapp.driverater.logic;

import android.location.Location;
import android.util.Log;

// For storing the speed limit and the users speed
public class SpeedStorage {
    // Stores minimum allowed speed/acceleration before defaulting to 0
    private final double minSpeed = 1.0, minAcceleration = 0.1;

    // Both stored in KM/H
    private final int mSpeedLimit;
    private double mUserSpeed, mAcceleration;
    public SpeedStorage(Location loc, SpeedStorage previous) {
        // Create empty object if location does not have speed data
        if (!loc.hasSpeed()) {
            this.mUserSpeed = 0;
            this.mAcceleration = 0;
            this.mSpeedLimit = 50;
            return;
        }

        // Adding the user speed
        double userSpeed = loc.getSpeed();

        // Convert to KM/H
        userSpeed *= 3.6;

        // Speed limit, currently always 50
        int limitSpeed = 50;

        // Calculating acceleration
        if (previous != null) {
            try {
                // Convert to seconds
                this.mAcceleration = (userSpeed - previous.mUserSpeed) / (Trip.Interval() / 1000.0);
            } catch (ArithmeticException e) {
                Log.d("SpeedStorageConstructor", "SpeedStorage: Cannot divide by 0");
                this.mAcceleration = 0;
            }
        }
        else {
            this.mAcceleration = 0;
        }

        // Adding other new location data
        this.mUserSpeed = userSpeed;
        this.mSpeedLimit = limitSpeed;
    }

    public double UserSpeed() {
        return this.mUserSpeed;
    }

    public double SpeedLimit() {
        return this.mSpeedLimit;
    }

    public double Acceleration() {
        return this.mAcceleration;
    }
}
