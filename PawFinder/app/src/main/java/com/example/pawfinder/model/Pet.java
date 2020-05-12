package com.example.pawfinder.model;

import androidx.annotation.NonNull;

import com.example.pawfinder.R;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Pet {

    @SerializedName("id")
    private Long id;
    @SerializedName("type")
    private PetType type;
    @SerializedName("name")
    private String name;
    @SerializedName("gender")
    private PetGender gender;
    @SerializedName("description")
    private String description;

    private int image;
    @SerializedName("dateOfLost")
    private String dateOfLost;
    @SerializedName("contact")
    private String contact;
    @SerializedName("found")
    private boolean isFound;

    @SerializedName("owner")
    private User user;

    public Pet() {}

    public Pet(Long id, PetType type, String name, PetGender gender, String description, int image, String dateOfLost, String contact, boolean isFound) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.gender = gender;
        this.description = description;
        this.image = image;
        this.dateOfLost = dateOfLost;
        this.contact = contact;
        this.isFound = isFound;
    }


    public Pet(Long id, PetType type, String name, PetGender gender, String description, int image, String dateOfLost, String contact, boolean isFound, User user) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.gender = gender;
        this.description = description;
        this.image = image;
        this.dateOfLost = dateOfLost;
        this.contact = contact;
        this.isFound = isFound;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public PetType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public PetGender getGender() {
        return gender;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    public String getDateOfLost() {
        return dateOfLost;
    }

    public String getContact() {
        return contact;
    }

    public boolean isFound() {
        return isFound;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(PetGender gender) {
        this.gender = gender;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setDateOfLost(String dateOfLost) {
        this.dateOfLost = dateOfLost;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setFound(boolean found) {
        isFound = found;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: "  + this.getName() + "\n"+
                "Type: " + this.getType() + "\n"+
                "Gender: " + this.getGender() + "\n" +
                "Missing since: " + this.getDateOfLost() + "\n"+
                "Last seen: " + "\n"+
                "Owners email: " + "pera@gmail.com" + "\n" +
                "Owners phone: "  + this.getContact() + "\n" +
                "Additional information: " + ": " + this.getDescription();
    }
}
