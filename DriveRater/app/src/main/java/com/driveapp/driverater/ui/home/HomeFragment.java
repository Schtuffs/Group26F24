package com.driveapp.driverater.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.driveapp.driverater.Login;
import com.driveapp.driverater.databinding.FragmentHomeBinding;
import com.driveapp.driverater.logic.Trip;
import com.driveapp.driverater.register;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Creating the buttons

        // Trip button
        Button startTripButton = binding.startTripButton;
        startTripButton.setOnClickListener(v -> {
            startActivity(new Intent(v.getContext(), Trip.class));
        });

        // Login button
        Button loginButton = binding.loginButton;
        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(v.getContext(), Login.class));
        });

        // Register button
        Button registerButton = binding.registerButton;
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(v.getContext(), register.class));
        });

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}