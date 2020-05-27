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
    @SerializedName("additionalInfo")
    private String additionalInfo;

    @SerializedName("image")
    private String image;
    @SerializedName("missingSince")
    private String missingSince;
    @SerializedName("ownersPhone")
    private String ownersPhone;
    @SerializedName("found")
    private boolean isFound;

    @SerializedName("owner")
    private User user;

    public Pet() {}

    public Pet(Long id, PetType type, String name, PetGender gender, String additionalInfo, String image, String missingSince, String ownersPhone, boolean isFound, User user) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.gender = gender;
        this.additionalInfo = additionalInfo;
        this.image = image;
        this.missingSince = missingSince;
        this.ownersPhone = ownersPhone;
        this.isFound = isFound;
        this.user = user;
    }

    public Pet(PetType type, String name, PetGender gender, String additionalInfo, String image, String missingSince, String ownersPhone, boolean isFound, User user) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.gender = gender;
        this.additionalInfo = additionalInfo;
        this.image = image;
        this.missingSince = missingSince;
        this.ownersPhone = ownersPhone;
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

    public String getImage() {
        return image;
    }

    public String getAdditionalInfo() { return additionalInfo; }

    public String getMissingSince() { return missingSince; }

    public String getOwnersPhone() { return ownersPhone; }

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

    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMissingSince(String missingSince) { this.missingSince = missingSince; }

    public void setOwnersPhone(String ownersPhone) { this.ownersPhone = ownersPhone; }

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
                "Missing since: " + this.getMissingSince() + "\n"+
                "Last seen: " + "\n"+
                "Owners email: " + "pera@gmail.com" + "\n" +
                "Owners phone: "  + this.getOwnersPhone() + "\n" +
                "Additional information: " + ": " + this.getAdditionalInfo();
    }
}
