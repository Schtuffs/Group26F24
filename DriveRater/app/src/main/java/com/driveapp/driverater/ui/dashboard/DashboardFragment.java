package com.driveapp.driverater.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.driveapp.driverater.MainActivity;
import com.driveapp.driverater.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Prepare text fields
        final TextView textScore = binding.textDashboardScore;
        final TextView textName = binding.textDashboardName;

        // Set score
        MutableLiveData<String> score = new MutableLiveData<>();
        score.setValue(Integer.toString(MainActivity.GetScore()));
        score.observe(getViewLifecycleOwner(), textScore::setText);

        // Set name
        MutableLiveData<String> name = new MutableLiveData<>();
        name.setValue("Hello, " + MainActivity.GetFirstname() + ".");
        name.observe(getViewLifecycleOwner(), textName::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}