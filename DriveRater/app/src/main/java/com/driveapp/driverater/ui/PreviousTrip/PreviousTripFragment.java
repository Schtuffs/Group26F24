package com.driveapp.driverater.ui.PreviousTrip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.driveapp.driverater.MainActivity;
import com.driveapp.driverater.databinding.FragmentPreviousTripBinding;
import com.driveapp.driverater.logic.SpeedStorage;

import java.util.ArrayList;

public class PreviousTripFragment extends Fragment {

    private final double ACCELERATION_CAP = 2.0;
    private final double DECELERATION_CAP = -2.0;

    private FragmentPreviousTripBinding binding;
    private TextView textScore, textOverSpeed, textAcceleration, textDeceleration;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPreviousTripBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Prepare text fields

        this.textScore = binding.textPreviousTripScore;
        this.textOverSpeed = binding.textOverSpeed;
        this.textAcceleration = binding.textFastAccelerate;
        this.textDeceleration = binding.textFastDecelerate;

        // Set score text
        this.SetScore();

        // Set over speed text
        this.SetOverLimit();

        // Set acceleration
        this.SetAcceleration();

        // Set deceleration
        this.SetDeceleration();

        return root;
    }

    private void SetScore() {
        MutableLiveData<String> score = new MutableLiveData<>();
        double userScore = MainActivity.GetPrevTripScore();
        // If no score, print it
        if (userScore == -1) {
            score.setValue("No score");
        } else {
            score.setValue(Integer.toString((int) MainActivity.GetPrevTripScore()));
        }
        score.observe(getViewLifecycleOwner(), textScore::setText);
    }

    private void SetOverLimit() {
        MutableLiveData<String> overSpeed = new MutableLiveData<>();
        ArrayList<SpeedStorage> speedData = MainActivity.GetPrevTripData();
        if (speedData != null) {
            // Loop to find total cases of speed over limit
            int countAboveLimit = 0;
            for (SpeedStorage speed : speedData) {
                if (speed.UserSpeed() > speed.SpeedLimit()) {
                    countAboveLimit++;
                }
            }
            overSpeed.setValue("Times above limit: " + countAboveLimit);
        }
        // No speeds, was null
        else {
            overSpeed.setValue("Times above limit: No data");
        }
        overSpeed.observe(getViewLifecycleOwner(), textOverSpeed::setText);
    }

    private void SetAcceleration() {
        MutableLiveData<String> acceleration = new MutableLiveData<>();
        ArrayList<SpeedStorage> speedData = MainActivity.GetPrevTripData();
        if (speedData != null) {
            // Loop to find total cases of speed over limit
            int countAboveThreshold = 0;
            for (SpeedStorage speed : speedData) {
                if (speed.Acceleration() > this.ACCELERATION_CAP) {
                    countAboveThreshold++;
                }
            }
            acceleration.setValue("Fast Accelerations: " + countAboveThreshold);
        }
        // No speeds, was null
        else {
            acceleration.setValue("Fast Accelerations: No data");
        }
        acceleration.observe(getViewLifecycleOwner(), this.textAcceleration::setText);
    }

    private void SetDeceleration() {
        MutableLiveData<String> deceleration = new MutableLiveData<>();
        ArrayList<SpeedStorage> speedData = MainActivity.GetPrevTripData();
        if (speedData != null) {
            // Loop to find total cases of speed over limit
            int countAboveThreshold = 0;
            for (SpeedStorage speed : speedData) {
                if (speed.Acceleration() < this.DECELERATION_CAP) {
                    countAboveThreshold++;
                }
            }
            deceleration.setValue("Fast Decelerations: " + countAboveThreshold);
        }
        // No speeds, was null
        else {
            deceleration.setValue("Fast Decelerations: No data");
        }
        deceleration.observe(getViewLifecycleOwner(), this.textDeceleration::setText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
