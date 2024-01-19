package com.example.carrentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if any field is empty
                if (email.isEmpty() || password.isEmpty()) {
                    // Display an error message or toast indicating that all fields are required
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check the user's credentials in the database
                UserRepository userRepository = new UserRepository(LoginActivity.this);
                userRepository.open();

                // Perform the login logic (e.g., check if the user exists and the password is correct)
                boolean loginSuccessful = userRepository.checkUserCredentials(email, password);

                userRepository.close();

                // Handle the result of the login attempt
                if (loginSuccessful) {
                    // Login successful
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                    // Retrieve user id
                    int userId = userRepository.getUserIdByEmail(email);

                    // Save user ID to shared preferences
                    userRepository.saveUserIdToPreferences(userId);

                    // Create an intent and pass user details
                    Intent userProfileIntent = new Intent(LoginActivity.this, UserProfileActivity.class);
                    userProfileIntent.putExtra("USER_ID", userId);

                    startActivity(userProfileIntent);
                    finish(); // Finish this activity to prevent going back to it from the next screen
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}