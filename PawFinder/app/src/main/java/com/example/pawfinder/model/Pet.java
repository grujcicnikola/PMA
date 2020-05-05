package com.example.pawfinder.model;

import androidx.annotation.NonNull;

import com.example.pawfinder.R;

import java.util.Date;

public class Pet {

    private Long id;
    private PetType type;
    private String name;
    private PetGender gender;
    private String description;
    private int image;
    private String dateOfLost;
    private String contact;
    private boolean isFound;

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
