package com.example.carrentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.util.Log;

import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class CarListings extends AppCompatActivity {

    private static final String TAG = "CarListings";
    private ArrayList<Car> carList;
    private ArrayAdapter<Car> adapter;
    private EditText searchEditText;
    private ArrayList<Car> originalCarList;
    private Spinner filterManufacturerSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_listings);

        try{

        // Declare an array of object...
        ///ArrayList<Car> carList = new ArrayList<>();
            carList = new ArrayList<>();

        // Create a list of cars
        carList.add(new Car("Perodua", "Alza 1.5(A)", 245));
        carList.add(new Car("Perodua", "Aruz 1.5(A)", 290));
        carList.add(new Car("Perodua", "Ativa 1.0(A)", 310));
        carList.add(new Car("Perodua", "Axia 2023 1.0(A)", 140));
        carList.add(new Car("Perodua", "Bezza 1.3(A)", 170));
        carList.add(new Car("Proton", "Ertiga 1,4(A)", 300));
        carList.add(new Car("Proton", "Perdana 2.0(A)", 460));
        carList.add(new Car("Proton", "X50 1.5(A)", 400));
        carList.add(new Car("Proton", "X70 1.5(A)", 475));
        carList.add(new Car("Proton", "X90 1.5(A)", 550));
        carList.add(new Car("Mazda", "CX-5 2.0(A)", 599));
        carList.add(new Car("Mazda", "CX-6 2.5(A)", 640));
        carList.add(new Car("Mazda", "CX3 2.0(A)", 460));
        carList.add(new Car("Toyota", "Alphard 2.4(A)", 729));
        carList.add(new Car("Toyota", "C-HR 1.8(A)", 599));
        carList.add(new Car("Toyota", "Camry 2.0(A)", 450));
        carList.add(new Car("Toyota", "Corolla Altis 1.8(A)", 480));
        carList.add(new Car("Honda", "Accord 2.0(A)", 550));
        carList.add(new Car("Honda", "City Hatchback 1.5(A)", 300));
        carList.add(new Car("Honda", "Civic 1.5(A)", 550));

            originalCarList = new ArrayList<>(carList);
        // Create an ArrayAdapter to display the list in the ListView
        ///ArrayAdapter<Car> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carList);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carList);

        // Get the ListView from the layout
        ListView listView = findViewById(R.id.listView);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);

            // Initialize the Spinner for filtering by manufacturer
            filterManufacturerSpinner = findViewById(R.id.manufacturerSpinner);

            // Set up the filter spinner
            String[] manufacturerOptions = {"All", "Perodua", "Proton", "Mazda", "Honda", "Toyota"};
            ArrayAdapter<String> manufacturerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, manufacturerOptions);
            manufacturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filterManufacturerSpinner.setAdapter(manufacturerAdapter);

            // Set up the filter functionality for the manufacturer Spinner
            filterManufacturerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedManufacturer = manufacturerOptions[position];
                    filterCarsSpinner(searchEditText.getText().toString(), selectedManufacturer);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            // Initialize the car list
            initializeCarList();

            // Initialize the EditText for search
            searchEditText = findViewById(R.id.searchEditText);

            // Add a TextWatcher to filter the list based on user input
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    filterCars(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

        // User select the car
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Car selectedCar = carList.get(position);
            Intent intent = new Intent(CarListings.this, BookingActivity.class);
            intent.putExtra("selectedCar", selectedCar);
            startActivity(intent);
        });
        } catch (Exception e) {
            // Log the exception for debugging purposes
            Log.e(TAG, "An error occurred in onCreate", e);
        }
    }

    private void filterCars(String query) {
        carList.clear(); // Clear the current list

        String selectedManufacturer = getSelectedManufacturer();

        if (query.isEmpty() && selectedManufacturer.equals("All")) {
            carList.addAll(originalCarList); // If both query and manufacturer filter are empty, restore the original data
        } else {
            for (Car car : originalCarList) {
                boolean matchesQuery = car.getManufacturer().toLowerCase().contains(query.toLowerCase())
                        || car.getVehicleName().toLowerCase().contains(query.toLowerCase());

                boolean matchesManufacturer = selectedManufacturer.equals("All") || car.getManufacturer().equals(selectedManufacturer);

                if (matchesQuery && matchesManufacturer) {
                    carList.add(car);
                }
            }
        }

        adapter.notifyDataSetChanged(); // Notify the adapter about changes
    }

    // Helper method to get the selected manufacturer from the spinner
    private String getSelectedManufacturer() {
        return filterManufacturerSpinner.getSelectedItem().toString();
    }

    private void filterCarsSpinner(String query, String selectedManufacturer) {
        carList.clear(); // Clear the current list

        if (query.isEmpty() && selectedManufacturer.equals("All")) {
            carList.addAll(originalCarList); // If the query is empty and All manufacturers are selected, restore the original data
        } else {
            for (Car car : originalCarList) {
                boolean matchesQuery = car.getManufacturer().toLowerCase().contains(query.toLowerCase())
                        || car.getVehicleName().toLowerCase().contains(query.toLowerCase());

                boolean matchesManufacturer = selectedManufacturer.equals("All") || car.getManufacturer().equals(selectedManufacturer);

                if (matchesQuery && matchesManufacturer) {
                    carList.add(car);
                }
            }
        }

        adapter.notifyDataSetChanged(); // Notify the adapter about changes
    }

    private void initializeCarList() {
        // Clear the existing carList
        carList.clear();

        // Populate the car list with your car details
        carList.add(new Car("Perodua", "Alza 1.5(A)", 245));
        carList.add(new Car("Perodua", "Aruz 1.5(A)", 290));
        carList.add(new Car("Perodua", "Ativa 1.0(A)", 310));
        carList.add(new Car("Perodua", "Axia 2023 1.0(A)", 140));
        carList.add(new Car("Perodua", "Bezza 1.3(A)", 170));
        carList.add(new Car("Proton", "Ertiga 1,4(A)", 300));
        carList.add(new Car("Proton", "Perdana 2.0(A)", 460));
        carList.add(new Car("Proton", "X50 1.5(A)", 400));
        carList.add(new Car("Proton", "X70 1.5(A)", 475));
        carList.add(new Car("Proton", "X90 1.5(A)", 550));
        carList.add(new Car("Mazda", "CX-5 2.0(A)", 599));
        carList.add(new Car("Mazda", "CX-6 2.5(A)", 640));
        carList.add(new Car("Mazda", "CX3 2.0(A)", 460));
        carList.add(new Car("Toyota", "Alphard 2.4(A)", 729));
        carList.add(new Car("Toyota", "C-HR 1.8(A)", 599));
        carList.add(new Car("Toyota", "Camry 2.0(A)", 450));
        carList.add(new Car("Toyota", "Corolla Altis 1.8(A)", 480));
        carList.add(new Car("Honda", "Accord 2.0(A)", 550));
        carList.add(new Car("Honda", "City Hatchback 1.5(A)", 300));
        carList.add(new Car("Honda", "Civic 1.5(A)", 550));

        // Save a copy of the original list for filtering
        originalCarList.clear();  // Clear the existing originalCarList
        originalCarList.addAll(carList);

        // Notify the adapter about changes
        adapter.notifyDataSetChanged();
    }

}