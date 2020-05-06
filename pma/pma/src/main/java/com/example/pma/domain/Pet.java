package com.example.pma.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Pet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Enumerated(EnumType.STRING)
	@Column
	private PetType type;
	@Column
    private String name;
	
	@Enumerated(EnumType.STRING)
	@Column
    private PetGender gender;
	@Column
    private String description;

	private byte[] image;
	
	@Column
    private Date dateOfLost;
	@Column
    private String contact;
	@Column
    private boolean isFound;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private User owner;

    public Pet() {}

    public Pet(PetType type, String name, PetGender gender, String description, byte[] image, Date dateOfLost, String contact, boolean isFound, User owner) {
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
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
    
    
    
}
