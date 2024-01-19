package com.example.carrentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Intent intent = getIntent();
        Car selectedCar = (Car) intent.getSerializableExtra("selectedCar");

        double totalPrice = selectedCar.calculateTotalPrice();

        TextView receiptDetailsTextView = findViewById(R.id.receiptDetailsTextView);
        receiptDetailsTextView.setText("Manufacturer: " + selectedCar.getManufacturer() +
                "\nVehicle Name: " + selectedCar.getVehicleName() +
                "\nRental Price/Day: RM" + selectedCar.getRentalPrice() +
                "\nBooking Date: " + selectedCar.getBookingDate() +
                "\nBooking Duration: " + selectedCar.getBookingDuration() + " days" +
                "\nTotal Price: RM" + totalPrice);


        /* Button backToLobbyButton = findViewById(R.id.backToLobbyButton);
        backToLobbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click to go back to MainActivity
                Intent intent = new Intent(ReceiptActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity if needed
            }
        }); */

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the logoutUser method from UserRepository
                UserRepository userRepository = new UserRepository(ReceiptActivity.this);
                userRepository.logoutUser();
                userRepository.close();

                // Redirect to MainActivity
                startActivity(new Intent(ReceiptActivity.this, MainActivity.class));
                finish(); // Finish the current activity to prevent going back to it from the MainActivity
            }
        });
    }
}