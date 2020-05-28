package com.example.pawfinder.model;

public class PinData {

    private String markerId;
    private String imageURL;
    private String name;
    private String phone;

    public PinData(String markerId, String imageURL, String name, String phone) {
        this.markerId = markerId;
        this.imageURL = imageURL;
        this.name = name;
        this.phone = phone;
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
