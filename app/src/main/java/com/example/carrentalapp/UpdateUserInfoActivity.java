package com.example.carrentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone, editTextPassword;
    private Button btnUpdate, btnBack;

    private int userId;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        editTextName = findViewById(R.id.editTextUpdateName);
        editTextEmail = findViewById(R.id.editTextUpdateEmail);
        editTextPhone = findViewById(R.id.editTextUpdatePhone);
        editTextPassword = findViewById(R.id.editTextUpdatePassword);
        btnUpdate = findViewById(R.id.btnUpdateProfile);
        btnBack = findViewById(R.id.btnBackToProfile);

        // Retrieve user id from the intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Initialize UserRepository
        userRepository = new UserRepository(this);

        // Retrieve user details and populate the EditText fields
        populateFields();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get updated user details from the EditText fields
                String updatedName = editTextName.getText().toString().trim();
                String updatedEmail = editTextEmail.getText().toString().trim();
                String updatedPhone = editTextPhone.getText().toString().trim();
                String updatedPassword = editTextPassword.getText().toString().trim();

                // Check if any field is empty
                if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedPhone.isEmpty() || updatedPassword.isEmpty()) {
                    // Display an error message or toast indicating that all fields are required
                    Toast.makeText(UpdateUserInfoActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update user details in the database
                UserRepository userRepository = new UserRepository(UpdateUserInfoActivity.this);
                userRepository.open();

                // Get user ID from the intent
                Intent intent = getIntent();
                if (intent.hasExtra("USER_ID")) {
                    int userId = intent.getIntExtra("USER_ID", -1);

                    // Perform the update logic
                    boolean updateSuccessful = userRepository.updateUserDetails(userId, updatedName, updatedEmail, updatedPhone, updatedPassword);

                    // Handle the result of the update attempt
                    if (updateSuccessful) {
                        // Update successful
                        Toast.makeText(UpdateUserInfoActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                    } else {
                        // Update failed
                        Toast.makeText(UpdateUserInfoActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                }

                userRepository.close();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to UserProfileActivity
                Intent userProfileIntent = new Intent(UpdateUserInfoActivity.this, UserProfileActivity.class);
                userProfileIntent.putExtra("USER_ID", userId);
                startActivity(userProfileIntent);
                finish(); // Finish this activity to prevent going back to it from the next screen
            }
        });
    }

    private void populateFields() {
        userRepository.open();
        User user = userRepository.getUserById(userId);
        userRepository.close();

        if (user != null) {
            editTextName.setText(user.getName());
            editTextEmail.setText(user.getEmail());
            editTextPhone.setText(user.getPhoneNumber());
            // You might choose not to display the password for security reasons
        }
    }
}