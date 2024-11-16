package com.driveapp.driverater.logic;

public class User {
    DriveScore driveScore;
    String firstName, lastName;
    public User(String first, String last) {
        this.firstName = first;
        this.lastName = last;
        this.driveScore = new DriveScore();
    }

    public String GetFirst() {
        return this.firstName;
    }

    public String GetLast() {
        return this.lastName;
    }

    public int GetScore() {
        return this.driveScore.GetScore();
    }
}
