package com.driveapp.driverater;

import android.os.Bundle;

import com.driveapp.driverater.logic.SpeedStorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.driveapp.driverater.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static UserModel user;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_previous_trip)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public static void SetUser(UserModel um) {
        user = um;
    }

    public static UserModel GetUser() {
        return user;
    }

    public static void UpdateUserTrips(ArrayList<SpeedStorage> speeds) {
        if (user != null) {
            user.AddTrip(speeds);
        }
    }

    public static double GetScore() {
        if (user != null)
            return user.GetScore();
        return 0;
    }

    public static double GetPrevTripScore() {
        if (user != null) {
            return user.GetPrevTripScore();
        }
        return -1;
    }

    public static ArrayList<SpeedStorage> GetPrevTripData() {
        if (user != null) {
            return user.GetPreviousTripData();
        }
        return null;
    }

    public static String GetFirstname() {
        if (user != null) {
            return user.getPreferredName();
        }
        return "No Name";
    }
}