package com.driveapp.driverater.logic;

import android.location.Location;
import android.util.Log;

// For storing the speed limit and the users speed
public class SpeedStorage {
    // Stores minimum allowed speed/acceleration before defaulting to 0
    private final double minSpeed = 1., minAcceleration = 1.;

    // Both stored in KM/H
    private int mSpeedLimit;
    private double mUserSpeed, mAcceleration;

    // Stores location for now, just in case
    private Location mLocation;

    public SpeedStorage(Location loc, SpeedStorage previous) {
        this.mLocation = loc;

        // Return if the location does not have a speed
        if (!loc.hasSpeed()) {
            this.mUserSpeed = 0;
            this.mAcceleration = 0;
            this.mSpeedLimit = 0;
            this.mLocation = loc;
            return;
        }
        // Adding the user speed
        double userSpeed = loc.getSpeed();

        // Check if the location is confident with the accuracy of the speed
        if (loc.hasSpeedAccuracy()) {
            // If the speed is lower than 1 m/s, then they are most likely not moving
            if (userSpeed < this.minSpeed) {
                userSpeed = 0;
            }
        }
        // Convert to KM/H
        userSpeed *= 3.6;

        // Speed limit
        // Still searching for a library
        int limitSpeed = 0;

        // Calculating acceleration
        if (previous != null) {
            try {
                this.mAcceleration = (userSpeed - previous.mUserSpeed) / Trip.AddInterval();
                Log.v("Constructor", "Prev: " + previous.mUserSpeed + ", Current: " + userSpeed + ", Accel: " + this.mAcceleration);
            } catch (ArithmeticException e) {
                Log.d("SpeedStorageConstructor", "SpeedStorage: Cannot divide by 0");
                this.mAcceleration = 0;
            }
            // Default the min acceleration if too slow
            if (this.mAcceleration < this.minAcceleration) {
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
        // Downcast to int for display purposes
        return this.mUserSpeed;
    }

    public double SpeedLimit() {
        // Downcast to int for display purposes
        return this.mSpeedLimit;
    }

    public double Acceleration() {
        // Downcast to int for display purposes
        return this.mAcceleration;
    }
}
