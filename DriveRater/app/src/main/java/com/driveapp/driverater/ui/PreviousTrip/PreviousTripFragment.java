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

public class PreviousTripFragment extends Fragment {

    private FragmentPreviousTripBinding binding;
    private TextView textScore;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPreviousTripBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Prepare text fields

        this.textScore = binding.textPreviousTripScore;

        // Set score
        MutableLiveData<String> score = new MutableLiveData<>();
        score.setValue(Integer.toString((int) MainActivity.GetPrevTripScore()));
        score.observe(getViewLifecycleOwner(), textScore::setText);

        return root;
    }

    public void SetScore(int score) {
        this.textScore.setText(score);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
