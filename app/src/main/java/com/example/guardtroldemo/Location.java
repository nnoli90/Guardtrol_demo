package com.example.guardtroldemo;

public class Location {

    public Double lon;
    public Double lat;
    public String address;
    public  String name;
    public  String imageUrl;

    public Location(Double lon, Double lat, String address, String name,String imageUr) {
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.name = name;
        this.imageUrl = imageUr;
    }
}
