package com.example.pettomanagementsystem;

public class VetModel {
    String name;
    double latitude;
    double longitude;

    public VetModel() {}
    public VetModel(String name, int latitude, int longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

}
