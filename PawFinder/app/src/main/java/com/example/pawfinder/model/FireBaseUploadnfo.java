package com.example.pawfinder.model;

public class FireBaseUploadnfo {

    private String name;
    private String imageUrl;

    public FireBaseUploadnfo(String mName, String imageUrl){
        if (mName.trim().equals(""))
        {
            mName= "No name";
        }

        this.name = mName;
        this.name = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
