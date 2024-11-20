package com.driveapp.driverater;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.driveapp.driverater.logic.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.driveapp.driverater.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static User user;
    public Button but;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create the user
        user = new User("NameFirst", "NameLast");

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        but = findViewById(R.id.loginButton);

        but.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i("Login Button", "login button clicked");

                startActivity(new Intent(MainActivity.this, Login.class ) );
            }
        });

       // but.setOnClickListener(v -> {
         //   Intent i = new Intent(MainActivity.this, Login.class);
           // startActivity(i);
      //  });

        //Find Registry button
        Button rgbtn = findViewById(R.id.registerButton);

        //Set registry button behavior
        rgbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i("Registry Button", "registry button clicked");

                startActivity(new Intent(MainActivity.this, register.class ) );
            }
        });

    }

    public static int GetScore() {
        return user.GetScore();
    }

    public static String GetFirstname() {
        return user.GetFirst();
    }
}