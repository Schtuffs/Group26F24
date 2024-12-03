package com.driveapp.driverater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Objects;

//Class that provides functions that operate on a database
public class DatabaseHelper extends SQLiteOpenHelper {

    //Declare static variables, including the table, and columns in use for user storage
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_PREFNAME = "PreferredName";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_DRIVESCORE = "DriveScore";
    public static final String COLUMN_SCOREWEIGHT = "Weight";

    //Constructor for the DatabaseHelper class (Nullable dictates that there can be no context upon initialization
    public DatabaseHelper(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }

    //First time accessing the database object
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + USER_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_PREFNAME + " TEXT, " + COLUMN_DRIVESCORE + " DOUBLE, " + COLUMN_SCOREWEIGHT + " DOUBLE)";
        db.execSQL(createTableStatement);
    }

    //Whenever a new version is of the database is made, allows backward compatibility
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Add a user to the database
    public boolean addUser(UserModel userModel){

        //SQLITE function allows writing to the database
        SQLiteDatabase db = this.getWritableDatabase();//Opening in writable (because we need to write here)

        //Content values is a hashmap that allows associative pairing of data, perfect for databases
        ContentValues uv = new ContentValues();
        uv.put(COLUMN_PREFNAME, userModel.getPreferredName());
        uv.put(COLUMN_USERNAME, userModel.getUsername());//Put the username from passed userModel into a column
        uv.put(COLUMN_PASSWORD, userModel.getPassword());//Put the password from passed userModel into a column
        uv.put(COLUMN_DRIVESCORE, userModel.getDriveScore());//Put the driver score from passes userModel into a column
        uv.put(COLUMN_SCOREWEIGHT, userModel.getScoreWeight());//Put the scoring weight from passes userModel into a column

        //Putting NULL value as the "column hack", as the program should not allow adding empty data to the database
        long insert = db.insert(USER_TABLE, null, uv);
        return insert != -1;//Only return if the insert is not a negative (invalid row/column)
    }

    //Check for valid login info using a provided username and password
    public UserModel checkLoginData (String username, String password){

        //Declare a query, selecting all data from the existing database
        String queryString = "SELECT * FROM " + USER_TABLE;

        //Initialize an SQLiteDatabase type as a readable database (Readable because we're not writing to check data)
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor type is the result set of data returned from a database when using a query
        Cursor cursor = db.rawQuery(queryString, null);//Using the above-defined query. Defining the selection arguments as null, because we are not working with prepared statements

        //Start the cursor values at the first index/row of the database, if possible
        if (cursor.moveToFirst()){

            //if not at end of table, loop, iterating through data
            do{
                //Here is where code would go for the checking of the user ID, which starts at the first column (0) and is not needed at the moment
                String currentUser = cursor.getString(1);//Get the username of the current index (specified by the second column)
                String currentPass = cursor.getString(2);//Get the password of the current index (specified by the third column)
                String currentName = cursor.getString(3);

                //If the current indexed username matches the login username
                if (Objects.equals(currentUser, username)){

                    //If the current indexed password also matches the login password
                    if (Objects.equals(currentPass, password)) {

                        UserModel user = new UserModel(cursor.getInt(0), currentName, currentUser, currentPass, cursor.getDouble(4), cursor.getDouble(5));
                        cursor.close();//Close the cursor for memory safety

                        return user;//return users preferred name
                    }
                }
            }while(cursor.moveToNext());//Continue across the database, until there are no longer any next indexes

            cursor.close();//Close the cursor for memory safety
        }
        //Return null if the database is empty
        return null;
    }
    public boolean updateUserScore (UserModel user){

        //SQLITE function allows writing to the database
        SQLiteDatabase db = this.getWritableDatabase();//Opening in writable (because we need to write here)

        //Content values is a hashmap that allows associative pairing of data, perfect for databases
        ContentValues uv = new ContentValues();


        try{
            uv.put(COLUMN_DRIVESCORE, user.GetScore());//Update the score
            uv.put(COLUMN_SCOREWEIGHT, user.getScoreWeight());//Update the weight
            db.update(USER_TABLE, uv, "ID = " + user.getId(), null);//Update the score where firstName matches passed
            return true;
        }

        catch (Exception e){
            return false;
        }
    }
    //Check for existing usernames, preventing duplicates
    public boolean checkForExistingUser (String username) {

        //Declare a query, selecting all data from the existing database
        String queryString = "SELECT * FROM " + USER_TABLE;

        //Initialize an SQLiteDatabase type as a readable database (Readable because we're not writing to check data)
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor type is the result set of data returned from a database when using a query
        Cursor cursor = db.rawQuery(queryString, null);//Using the above-defined query. Defining the selection arguments as null, because we are not working with prepared statements

        //Start the cursor values at the first index/row of the database, if possible
        if (cursor.moveToFirst()) {

            //if not at end of table, loop, iterating through data
            do {

                //When iterating, only check usernames
                String currentUser = cursor.getString(1);//Get the username of the current index (specified by the second column)

                //If the username in the database at the current index matches the newly registered username
                if (Objects.equals(currentUser, username)) {

                    cursor.close();//Close the cursor for memory safety

                    return true;//return true, indicating that the provided username when registering is a duplicate
                }
            } while (cursor.moveToNext());//Continue across the database, until there are no longer any next indexes

            cursor.close();//Close the cursor for memory safety
        }
        //Return false if the database is empty
        return false;
    }
}
