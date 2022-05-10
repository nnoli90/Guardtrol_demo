package com.example.guardtroldemo;

public class Location {

    public Double lon;
    public Double lat;
    public String address;
    public  String name;
    public  String imageUrl;
    public  String user_name;

    public Location(Double lon, Double lat, String address, String name,String imageUr,String user_name) {
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.name = name;
        this.imageUrl = imageUr;
        this.user_name = user_name;
    }
}
