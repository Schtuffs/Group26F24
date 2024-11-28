package com.driveapp.driverater.logic;

import java.util.ArrayList;

public class User {
    private double mDriveScore;
    private double mPrevTripScore;

    private ArrayList<Double[]> mDriveScores;
    private String firstName, lastName;
    public User(String first, String last) {
        this.firstName = first;
        this.lastName = last;
        this.mDriveScore = 50.;
        this.mDriveScores = new ArrayList<>();
        this.mDriveScores.add(new Double[] {1., this.mDriveScore} );
        this.mPrevTripScore = 0;
    }

    public String GetFirst() {
        return this.firstName;
    }

    public String GetLast() {
        return this.lastName;
    }

    public double GetScore() {
        return this.mDriveScore;
    }

    public double GetPrevTripScore() {
        return this.mPrevTripScore;
    }

    public void AddScore(Double[] newScore) {
        // Don't add weightless scores
        if (newScore[0] <= 0.) {
            return;
        }

        if (0 <= newScore[1] && newScore[1] <= 100) {
            // Change previous trip score to the new trip score
            this.mPrevTripScore = newScore[1];

            // Add score to list of scores
            this.mDriveScores.add(newScore);
            double totalScore = 0, totalWeight = 0;

            // Loop through all scores to calculate average
            for (Double[] score : this.mDriveScores) {
                // Add weight to variable to divide score later
                totalWeight += score[0];
                // Adjust score based on its weight
                totalScore += score[1] * score[0];
            }
            // Set score to the score / extra weight values
            this.mDriveScore = totalScore / totalWeight;
        }
    }
}
