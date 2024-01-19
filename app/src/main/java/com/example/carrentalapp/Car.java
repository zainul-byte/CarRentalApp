package com.example.carrentalapp;

import java.io.Serializable;

public class Car implements Serializable{

    //instance members...
    private String manufacturer;
    private String vehicleName;
    private int rentalPrice;
    private String bookingDate;
    private int bookingDuration; //in days

    //default constructors...
    public Car(String manufacturer, String vehicleName, int rentalPrice) {
        this.manufacturer = manufacturer;
        this.vehicleName = vehicleName;
        this.rentalPrice = rentalPrice;
    }

    //getter and setter for car details...
    public String getManufacturer(){
        return manufacturer;
    }
    public void setManufacturer(String manufacturer){
        this.manufacturer = manufacturer;
    }
    public String getVehicleName(){
        return vehicleName;
    }
    public void setVehicleName(String vehicleName){
        this.vehicleName = vehicleName;
    }
    public int getRentalPrice(){
        return rentalPrice;
    }
    public void setRentalPrice(int rentalPrice){
        this.rentalPrice = rentalPrice;
    }

    //getter and setter for booking details...
    public String getBookingDate(){
        return bookingDate;
    }
    public void setBookingDate(String bookingDate){
        this.bookingDate = bookingDate;
    }
    public int getBookingDuration(){
        return bookingDuration;
    }
    public void setBookingDuration(int bookingDuration){
        this.bookingDuration = bookingDuration;
    }

    // Method to calculate total price based on booking duration
    public double calculateTotalPrice() {
        return rentalPrice * bookingDuration;
    }

    @Override
    public String toString() {
        return String.format("%-20s%-20sRM%-10d", manufacturer, vehicleName, rentalPrice);
    }

}
