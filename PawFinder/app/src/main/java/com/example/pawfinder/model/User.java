package com.example.pawfinder.model;

import java.util.Collection;

public class User {

    private Long id;

    private String email;

    private String password;

    private String token;

    //public java.util.Collection<Pet> pet;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
//        this.pet = pet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public java.util.Collection<Pet> getPet() {
//        if (pet == null)
//            pet = new java.util.HashSet<Pet>();
//        return pet;
//    }
//
//    public java.util.Iterator getIteratorPet() {
//        if (pet == null)
//            pet = new java.util.HashSet<Pet>();
//        return pet.iterator();
//    }
//
//    /** @pdGenerated default setter
//     * @param newPet */
//    public void setPet(java.util.Collection<Pet> newPet) {
//        removeAllPet();
//        for (java.util.Iterator iter = newPet.iterator(); iter.hasNext();)
//            addPet((Pet)iter.next());
//    }
//
//    /** @pdGenerated default add
//     * @param newPet */
//    public void addPet(Pet newPet) {
//        if (newPet == null)
//            return;
//        if (this.pet == null)
//            this.pet = new java.util.HashSet<Pet>();
//        if (!this.pet.contains(newPet))
//            this.pet.add(newPet);
//    }
//
//    /** @pdGenerated default remove
//     * @param oldPet */
//    public void removePet(Pet oldPet) {
//        if (oldPet == null)
//            return;
//        if (this.pet != null)
//            if (this.pet.contains(oldPet))
//                this.pet.remove(oldPet);
//    }
//
//    /** @pdGenerated default removeAll */
//    public void removeAllPet() {
//        if (pet != null)
//            pet.clear();
//    }



    public java.util.Collection<Pet> pet;


    public User(Long id, String email, String password, Collection<Pet> pet) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.pet = pet;
    }





    public java.util.Collection<Pet> getPet() {
        if (pet == null)
            pet = new java.util.HashSet<Pet>();
        return pet;
    }

    public java.util.Iterator getIteratorPet() {
        if (pet == null)
            pet = new java.util.HashSet<Pet>();
        return pet.iterator();
    }

    /** @pdGenerated default setter
     * @param newPet */
    public void setPet(java.util.Collection<Pet> newPet) {
        removeAllPet();
        for (java.util.Iterator iter = newPet.iterator(); iter.hasNext();)
            addPet((Pet)iter.next());
    }

    /** @pdGenerated default add
     * @param newPet */
    public void addPet(Pet newPet) {
        if (newPet == null)
            return;
        if (this.pet == null)
            this.pet = new java.util.HashSet<Pet>();
        if (!this.pet.contains(newPet))
            this.pet.add(newPet);
    }

    /** @pdGenerated default remove
     * @param oldPet */
    public void removePet(Pet oldPet) {
        if (oldPet == null)
            return;
        if (this.pet != null)
            if (this.pet.contains(oldPet))
                this.pet.remove(oldPet);
    }

    /** @pdGenerated default removeAll */
    public void removeAllPet() {
        if (pet != null)
            pet.clear();
    }

}