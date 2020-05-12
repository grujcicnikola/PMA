package com.example.pma.dto;

import java.util.Date;

import com.example.pma.domain.Pet;
import com.example.pma.domain.PetGender;
import com.example.pma.domain.PetType;

public class PetDTO {

    private Long id;
    private PetType type;
    private String name;
    private PetGender gender;
    private String description;
    private byte[] image;
    private Date dateOfLost;
    private String contact;
    private boolean isFound;
    
    private UserDTO owner;

    public PetDTO() {}

    public PetDTO(Long id, PetType type, String name, PetGender gender, String description, byte[] image, Date dateOfLost, String contact, boolean isFound, UserDTO owner) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.gender = gender;
        this.description = description;
        this.image = image;
        this.dateOfLost = dateOfLost;
        this.contact = contact;
        this.isFound = isFound;
        this.owner = owner;
    }
    
    public PetDTO(Pet pet) {
    	this.id = pet.getId();
        this.type = pet.getType();
        this.name = pet.getName();
        this.gender = pet.getGender();
        this.description = pet.getDescription();
        this.image = pet.getImage();
        this.dateOfLost = pet.getDateOfLost();
        this.contact = pet.getContact();
        this.isFound = pet.isFound();
        this.owner = new UserDTO(pet.getOwner());
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

	public void setImage(byte[] image) {
		this.image = image;
	}

	public void setDateOfLost(Date dateOfLost) {
		this.dateOfLost = dateOfLost;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}

	public UserDTO getOwner() {
		return owner;
	}

	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}
    
    
    
}

