package com.driveapp.driverater;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class register extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve entered username and password
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                UserModel userModel;
                try {
                    userModel = new UserModel(-1, username, password);
                }

                catch(Exception e){
                    Toast.makeText(register.this, "Please enter valid info", Toast.LENGTH_SHORT).show();
                    userModel = new UserModel(-1, "error", "error");
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(register.this);

                boolean worked = databaseHelper.addUser(userModel);

                Toast.makeText(register.this, "Registered: " + worked, Toast.LENGTH_SHORT).show();
                // Implement authentication logic here
                if (username.equals("Admin") && password.equals("123")) {
                    // Successful login
                    Toast.makeText(register.this, "Login successful", Toast.LENGTH_SHORT).show();
                } else {
                    // Failed login
                    Toast.makeText(register.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}