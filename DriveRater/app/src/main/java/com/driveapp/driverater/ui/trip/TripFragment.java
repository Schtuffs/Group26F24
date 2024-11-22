package com.driveapp.driverater.ui.trip;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.driveapp.driverater.databinding.FragmentTripBinding;

public class TripFragment extends Fragment {

    private FragmentTripBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTripBinding.inflate(inflater, container, false);
        for (int i = 0; i < 100; i++) {
            Log.d("TAG", "DA MESSAGE");
        }
        View root = binding.getRoot();

        // Prepare text fields
        final TextView textLocation = binding.locationText;

        // Set location on device screen
        MutableLiveData<String> locTex = new MutableLiveData<>();
        locTex.setValue("String");
        locTex.observe(getViewLifecycleOwner(), textLocation::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
