package com.driveapp.driverater;

//UserModel class that is input into a database
public class UserModel {

    //Declare variables that will be used for user storage and authentication
    private int id;
    private String preferredName;
    private String username;
    private String password;

    //Constructors
    public UserModel(int id, String prefName, String username, String password) {//Parameterized constructor, given an id, username, and password
        this.id = id;
        this.preferredName = prefName;
        this.username = username;
        this.password = password;

    }
    public UserModel() {//Default constructor

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
        return username;
    }

    //Set the username
    public void setUsername(String username) {
        this.username = username;
    }

    //Get the password
    public String getPassword() {
        return password;
    }

    //Set the password
    public void setPassword(String password) {
        this.password = password;
    }
}

