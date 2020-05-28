package com.example.pma.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
    private String additionalInfo;

	@Column
	private String image;
	
	@Column
    private Date missingSince;
	@Column
    private String ownersPhone;
	@Column
    private boolean isFound;
	
	@ManyToOne
	private User owner;
	
	@OneToOne
	private Address address;

    public Pet() {}

    
    public Pet(PetType type, String name, PetGender gender, String additionalInfo, String image,
			Date missingSince, String ownersPhone, boolean isFound, User owner) {
		super();
		this.type = type;
		this.name = name;
		this.gender = gender;
		this.additionalInfo = additionalInfo;
		this.image = image;
		this.missingSince = missingSince;
		this.ownersPhone = ownersPhone;
		this.isFound = isFound;
		this.owner = owner;
	}
    
    public Pet(PetType type, String name, PetGender gender, String description, String image, Date dateOfLost,
			String contact, boolean isFound, User owner, Address address) {
		this.type = type;
		this.name = name;
		this.gender = gender;
		this.additionalInfo = description;
		this.image = image;
		this.missingSince = dateOfLost;
		this.ownersPhone = contact;
		this.isFound = isFound;
		this.owner = owner;
		this.address = address;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public PetType getType() {
		return type;
	}


	public void setType(PetType type) {
		this.type = type;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public PetGender getGender() {
		return gender;
	}


	public void setGender(PetGender gender) {
		this.gender = gender;
	}


	public String getAdditionalInfo() {
		return additionalInfo;
	}


	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public Date getMissingSince() {
		return missingSince;
	}


	public void setMissingSince(Date missingSince) {
		this.missingSince = missingSince;
	}


	public String getOwnersPhone() {
		return ownersPhone;
	}


	public void setOwnersPhone(String ownersPhone) {
		this.ownersPhone = ownersPhone;
	}


	public boolean isFound() {
		return isFound;
	}


	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}


	public User getOwner() {
		return owner;
	}


	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
    
}


	

