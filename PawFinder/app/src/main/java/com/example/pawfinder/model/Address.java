package com.example.pawfinder.model;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("id")
    private Long id;
    @SerializedName("city")
    private String city;
    @SerializedName("street")
    private String street;
    @SerializedName("number")
    private int number;
    @SerializedName("lon")
    private Double lon;
    @SerializedName("lat")
    private Double lat;

    public Address() {
    }

    public Address(String city, String street, int number, Double lon, Double lat) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.number = number;
        this.lon = lon;
        this.lat = lat;
    }

    public Address(Double lon, Double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getNumber() {
        return number;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
