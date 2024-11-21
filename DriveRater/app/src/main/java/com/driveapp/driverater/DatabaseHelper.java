package com.driveapp.driverater;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "Password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }

    //First time accessing the database object
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + USER_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTableStatement);
    }

    //Whenever a new version is of the database is made, allows backward compatibility
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUser(UserModel userModel){

        //SQLITE function allows writing to the database
        SQLiteDatabase db = this.getWritableDatabase();

        //Content values is a hashmap that allows associative pairing of data, perfect for databases
        ContentValues uv = new ContentValues();
        uv.put(COLUMN_USERNAME, userModel.getUsername());
        uv.put(COLUMN_PASSWORD, userModel.getPassword());

        //Putting NULL value as the column hack, as the program should not allow adding empty data to the database
        long insert = db.insert(USER_TABLE, null,uv);
        return insert != -1;
    }
}
