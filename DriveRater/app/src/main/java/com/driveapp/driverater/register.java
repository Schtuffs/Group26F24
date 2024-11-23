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

    //Variables needed to track the text in any entry fields, as well as the registry button itself
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

        buttonRegister.setOnClickListener(new View.OnClickListener() {//When registry button is clicked
            @Override
            public void onClick(View view) {
                // Retrieve entered username and password (trimming username spaces on both ends)
                String user = editTextUsername.getText().toString();
                String username = user.trim();
                String password = editTextPassword.getText().toString();

                //Declare a userModel class (used for the database)
                UserModel userModel;

                try {

                    //Check for spaces in username/password
                    if (username.contains(" ")||password.contains(" ")) {
                        Exception e = null;
                        Toast.makeText(register.this, "Spaces are not allowed for your username or password", Toast.LENGTH_SHORT).show();
                        throw e;
                    }

                    //Check for null username
                    if (username.equals("")){
                        Exception e = null;
                        Toast.makeText(register.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                        throw e;
                    }

                    //Check for null password
                    if (password.equals("")){
                        Exception e = null;
                        Toast.makeText(register.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                        throw e;
                    }

                    userModel = new UserModel(-1, username, password);
                }

                //If exception thrown, do NOT add info to database
                catch(Exception e){
                    return;
                }

                //Declare a DatabaseHelper class variable (provides many functions that operate on a database)
                DatabaseHelper databaseHelper = new DatabaseHelper(register.this);

                //Check if the username provided already exists within the database (no duplicate usernames allowed)
                boolean existingUser = databaseHelper.checkForExistingUser(username);

                //Ensure that if the username is taken, the user needs to choose another username
                if (existingUser){
                    Toast.makeText(register.this, "This user already exists!", Toast.LENGTH_SHORT).show();
                }

                //If everything is in place
                else if(!existingUser){

                    //Check if the user registry data was able to be added to the database
                    boolean worked = databaseHelper.addUser(userModel);

                    //Early implementation for checking that the user data was registered
                    Toast.makeText(register.this, "Registered: " + worked, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}