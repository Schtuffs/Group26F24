package com.driveapp.driverater.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.driveapp.driverater.R;
import com.driveapp.driverater.databinding.LoginBinding;

public class Login extends Fragment {

    private LoginBinding binding;
    Button login;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Prepare text fields
        final EditText textUsername = root.findViewById(R.id.text_username);
        String editUsername = textUsername.getText().toString();

        // Set Username

        // Set Password

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
