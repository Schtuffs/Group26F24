package com.driveapp.driverater.logic;

import java.util.ArrayList;

public class User {
    private double mDriveScore;
    private double mPrevTripScore;

    private ArrayList<SpeedStorage> mPrevTripData;

    private final int DRIVE_WEIGHT = 0, DRIVE_SCORE = 1;

    private ArrayList<Double[]> mDriveScores;
    private String firstName;
    public User(String first, double score) {
        this.firstName = first;
        this.mDriveScore = score;
        this.mDriveScores = new ArrayList<>();
        this.mDriveScores.add(new Double[] {1., this.mDriveScore} );
        this.mPrevTripScore = 0;
        this.mPrevTripData = new ArrayList<>();
    }

    public String GetFirst() {
        return this.firstName;
    }

    public double GetScore() {
        return this.mDriveScore;
    }

    public double GetPrevTripScore() {
        return this.mPrevTripScore;
    }

    public ArrayList<SpeedStorage> GetPreviousTripData() {
        return this.mPrevTripData;
    }

    public void AddTrip(ArrayList<SpeedStorage> speedData) {
        this.mPrevTripData = speedData;
        Double[] calculatedScoreAdjustment = DriveScore.CalculateScore(this.mPrevTripData);
        this.AddScore(calculatedScoreAdjustment);
    }

    private void AddScore(Double[] newScore) {
        // Don't add weightless scores
        if (newScore[DRIVE_WEIGHT] <= 0.) {
            return;
        }

        if (0 <= newScore[DRIVE_SCORE] && newScore[DRIVE_SCORE] <= 100) {
            // Change previous trip score to the new trip score
            this.mPrevTripScore = newScore[DRIVE_SCORE];

            // Add score to list of scores
            this.mDriveScores.add(newScore);
            double totalScore = 0, totalWeight = 0;

            // Loop through all scores to calculate average
            for (Double[] score : this.mDriveScores) {
                // Add weight to variable to divide score later
                totalWeight += score[DRIVE_WEIGHT];
                // Adjust score based on its weight
                totalScore += score[DRIVE_SCORE] * score[DRIVE_WEIGHT];
            }
            // Set score to the score / extra weight values
            this.mDriveScore = totalScore / totalWeight;
        }
    }
}
