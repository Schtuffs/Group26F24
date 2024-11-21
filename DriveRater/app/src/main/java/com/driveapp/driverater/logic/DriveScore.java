package com.driveapp.driverater.logic;

import java.util.ArrayList;

public class DriveScore {
    // Holds the actual driver score
    private int driverScoreValue;

    // This is required to adjust the weighting of trips in the future
    ArrayList<Trip> allTrips;

    public DriveScore() {
        // User starts with a drive score of 50
        this.driverScoreValue = 50;
    }

    // Trip is not implemented at the moment, however its functionality will be needed to calculate
    // updates for the drivers score
    // This will update the score based on the newly added trip
    public void UpdateScore(Trip trip) {
        // Add trip to the total trip count, lets each successive trip influence the total score less
        this.allTrips.add(trip);

        // Updates the driver score
        int scoreUpdate = 0;
        // Update value with diminishing returns so that all trips are weighted fairly equally
        // This method of calculating will be changed in the future
        this.driverScoreValue += scoreUpdate / this.allTrips.size();
    }

    public int GetScore() {
        return this.driverScoreValue;
    }
}
