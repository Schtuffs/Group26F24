package com.driveapp.driverater.logic;

import java.util.ArrayList;

public class DriveScore {
    private final static double capAcceleration = 2.0, capDeceleration = -2.0;
    public static Double[] CalculateScore(ArrayList<SpeedStorage> speedData) {
        // Score starts at 100 and goes down
        double score = 100.;

        // Store total points of data to change how much each affects the score
        double weight = 100. / speedData.size();

        // This allows the value to affect the weighting more or less based on how many points of data it has
        double totalWeight = speedData.size() / 100.;
        if (totalWeight > 1) {
            totalWeight = 1.;
        }

        // Score affecting values in percentages, affected by weighting
        double aboveLimit = 0.85 * weight, fastAcceleration = 0.1 * weight, fastDeceleration = 0.05 * weight;

        // Loop through each set of data
        for (SpeedStorage speed : speedData) {
            // Tests if the user was going above the speed limit
            if (speed.UserSpeed() > speed.SpeedLimit()) {
                score -= aboveLimit;
            }
            // Test if users acceleration is above the threshold for accelerating too fast
            if (speed.Acceleration() > capAcceleration) {
                score -= fastAcceleration;
            }
            // Test if users deceleration is above the threshold for breaking too hard
            if (speed.Acceleration() < capDeceleration) {
                score -= fastDeceleration;
            }
        }
        return new Double[] { totalWeight, score };
    }
}
