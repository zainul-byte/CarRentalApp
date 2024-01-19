package com.example.carrentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewName, textViewEmail, textViewPhone, textViewWelcome;
    private Button btnUpdateDetails, btnRentCar;
    private static final String TAG = "UserProfileActivity";
    private UserRepository userRepository;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        try {
            textViewName = findViewById(R.id.textViewName);
            textViewEmail = findViewById(R.id.textViewEmail);
            textViewPhone = findViewById(R.id.textViewPhone);
            btnUpdateDetails = findViewById(R.id.btnUpdateDetails);
            btnRentCar = findViewById(R.id.btnRentCar);
            textViewWelcome = findViewById(R.id.textViewWelcome);

            // Get user ID from the intent
            Intent intent = getIntent();
            if (intent.hasExtra("USER_ID")) {
                userId = intent.getIntExtra("USER_ID", -1);

                // Log for debugging
                Log.d(TAG, "Received USER_ID: " + userId);

                // Retrieve individual user details from the database using UserRepository
                userRepository = new UserRepository(this);
                userRepository.open();

                String userName = userRepository.getUserNameById(userId);
                String userEmail = userRepository.getUserEmailById(userId);
                String userPhone = userRepository.getUserPhoneById(userId);
                String userPassword = userRepository.getUserPasswordById(userId);

                userRepository.close();

                // Display user details in TextViews
                textViewWelcome.setText("Good Day, " + userEmail);
                textViewName.setText("Name: " + userName);
                textViewEmail.setText("Email: " + userEmail);
                textViewPhone.setText("Phone: " + userPhone);
                // textViewPassword.setText("Password: " + userPassword);
            }

            btnUpdateDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Navigate to UpdateUserInfoActivity
                    Intent updateIntent = new Intent(UserProfileActivity.this, UpdateUserInfoActivity.class);
                    updateIntent.putExtra("USER_ID", userId);
                    startActivity(updateIntent);
                }
            });

            btnRentCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Navigate to CarListingsActivity
                    startActivity(new Intent(UserProfileActivity.this, CarListings.class));
                }
            });

            Button btnLogout = findViewById(R.id.btnLogout);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call the logoutUser method from UserRepository
                    UserRepository userRepository = new UserRepository(UserProfileActivity.this);
                    userRepository.logoutUser();
                    userRepository.close();

                    // Redirect to MainActivity
                    startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
                    finish(); // Finish the current activity to prevent going back to it from the MainActivity
                }
            });
        } catch (Exception e) {
            // Log the exception for debugging purposes
            Log.e(TAG, "An error occurred in onCreate", e);
        }
    }
}