package com.example.pawfinder.model;

import java.util.Date;

public class Comment {
    private Long id;

    private String message;

    private Date date;

    public User user;

    public Pet pet;

    public Comment() {
    }

    public Comment(Long id, String message, Date date, User user, Pet pet) {
        this.id = id;
        this.message=message;
        this.date=date;
        this.pet=pet;
        this.user=user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
