package com.driveapp.driverater.logic;

import java.util.ArrayList;

public class User {
    private double mDriveScore;

    private ArrayList<Double[]> mDriveScores;
    private String firstName, lastName;
    public User(String first, String last) {
        this.firstName = first;
        this.lastName = last;
        this.mDriveScore = 50.;
        this.mDriveScores = new ArrayList<>();
        this.mDriveScores.add(new Double[] {1., this.mDriveScore} );
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

    public void AddScore(Double[] newScore) {
        // Don't add weightless scores
        if (newScore[0] <= 0.) {
            return;
        }

        if (0 <= newScore[1] && newScore[1] <= 100) {
            this.mDriveScores.add(newScore);
            double totalScore = 0, totalWeight = 0;
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
