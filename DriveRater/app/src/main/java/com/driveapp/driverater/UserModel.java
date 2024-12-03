package com.driveapp.driverater;

import com.driveapp.driverater.logic.DriveScore;
import com.driveapp.driverater.logic.SpeedStorage;

import java.util.ArrayList;

//UserModel class that is input into a database
public class UserModel {

    //Declare variables that will be used for user storage and authentication
    private int id;
    private String preferredName;
    private String username;
    private String password;
    private double mDriveScore;
    private double mPrevTripScore;
    private ArrayList<SpeedStorage> mPrevTripData;
    private final int DRIVE_WEIGHT = 0, DRIVE_SCORE = 1;
    private ArrayList<Double[]> mDriveScores;

    //Constructors
    public UserModel(int id, String prefName, String username, String password, double driveScore, double scoreWeight) {//Parameterized constructor
        this.id = id;
        this.preferredName = prefName;
        this.username = username;
        this.password = password;
        this.mDriveScore = driveScore;
        this.mDriveScores = new ArrayList<>();
        this.mDriveScores.add(new Double[] {scoreWeight, this.mDriveScore} );
        this.mPrevTripScore = 0;
        this.mPrevTripData = new ArrayList<>();
    }

    //Return the id
    public int getId() {
        return id;
    }

    //Set the id
    public void setId(int id) {
        this.id = id;
    }

    // Get the preferred name
    public String getPreferredName() {
        return this.preferredName;
    }

    //Get the username
    public String getUsername() {
        return this.username;
    }

    //Get the driver score
    public double getDriveScore() { return this.mDriveScore; }

    //Get the score weighting
    public double getScoreWeight() {
        double weight = 0;
        for (Double[] values : this.mDriveScores) {
            weight += values[DRIVE_WEIGHT];
        }
        return weight;
    }

    //Get the password
    public String getPassword() {
        return password;
    }

    public double GetScore() {
        // Recalculate score
        this.CalculateScore();
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

            this.CalculateScore();
        }
    }

    private void CalculateScore() {
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

