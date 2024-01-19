package com.example.carrentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone, editTextPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if any field is empty
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    // Display an error message or toast indicating that all fields are required
                    // For simplicity, you can add a toast message here
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a User object with the input data
                User newUser = new User(name, email, phone, password);

                // Insert the user into the database
                UserRepository userRepository = new UserRepository(RegisterActivity.this);
                userRepository.open();
                long userId = userRepository.addUser(newUser);
                userRepository.close();

                // Check if the user was successfully added
                if (userId == -1) {
                    // Registration failed due to email already registered
                    Toast.makeText(RegisterActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                } else {
                    // Registration successful
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    // Optionally, navigate to another screen or perform other actions
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish(); // Finish this activity to prevent going back to it from the login screen
                }

                // Close the UserRepository after use
                userRepository.close();
            }
        });
    }
}