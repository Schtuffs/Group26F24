package com.driveapp.driverater.logic;

import android.location.Location;

// For storing the speed limit and the users speed
public class SpeedStorage {
    // Both stored in KM/H
    private int mSpeedLimit;
    private double mUserSpeed, mAcceleration;

    // Stores location for now, just in case
    private Location mLocation;

    public SpeedStorage(Location loc, SpeedStorage previous) {
        this.mLocation = loc;

        // Return if the location does not have a speed
        if (!loc.hasSpeed()) {
            return;
        }
        // Adding the user speed
        double userSpeed = loc.getSpeed();

        // Check if the location is confident with the accuracy of the speed
        if (loc.hasSpeedAccuracy()) {
            // If the speed is lower than the potential accuracy, then the user is most likely not moving
            if (userSpeed < loc.getSpeedAccuracyMetersPerSecond()) {
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
            this.mAcceleration = (previous.UserSpeed() - loc.getSpeed() ) / Trip.Interval;
        }
        else {
            this.mAcceleration = 0;
        }

        // Adding other new location data
        this.mUserSpeed = userSpeed;
        this.mSpeedLimit = limitSpeed;
    }

    public int UserSpeed() {
        // Downcast to int for display purposes
        return (int)this.mUserSpeed;
    }

    public int SpeedLimit() {
        // Downcast to int for display purposes
        return (int)this.mSpeedLimit;
    }

    public int Acceleration() {
        // Downcast to int for display purposes
        return (int)this.mAcceleration;
    }
}
