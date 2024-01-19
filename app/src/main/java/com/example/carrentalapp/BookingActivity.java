package com.example.carrentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

public class BookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Intent intent = getIntent();
        Car selectedCar = (Car) intent.getSerializableExtra("selectedCar");

        // Display the selected car's details
        TextView carDetailsTextView = findViewById(R.id.carDetailsTextView);
        carDetailsTextView.setText(selectedCar.toString());

        EditText dateEditText = findViewById(R.id.dateEditText);
        EditText durationEditText = findViewById(R.id.durationEditText);

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(v -> {
            String bookingDate = dateEditText.getText().toString();
            int bookingDuration = Integer.parseInt(durationEditText.getText().toString());

            selectedCar.setBookingDate(bookingDate);
            selectedCar.setBookingDuration(bookingDuration);

            Intent receiptIntent = new Intent(BookingActivity.this, ReceiptActivity.class);
            receiptIntent.putExtra("selectedCar", selectedCar);
            startActivity(receiptIntent);
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the back button in the toolbar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}