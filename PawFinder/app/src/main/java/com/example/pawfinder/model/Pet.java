package com.example.pawfinder.model;

import java.util.Date;

public class Pet {

    private Long id;
    private PetType type;
    private String name;
    private PetGender gender;
    private String description;
    private byte[] image;
    private Date dateOfLost;
    private String contact;
    private boolean isFound;

    public Pet() {}

    public Pet(Long id, PetType type, String name, PetGender gender, String description, byte[] image, Date dateOfLost, String contact, boolean isFound) {
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

    public byte[] getImage() {
        return image;
    }

    public Date getDateOfLost() {
        return dateOfLost;
    }

    public String getContact() {
        return contact;
    }

    public boolean isFound() {
        return isFound;
    }
    
}
