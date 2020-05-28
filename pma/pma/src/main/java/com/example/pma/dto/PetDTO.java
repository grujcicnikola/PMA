package com.example.pma.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.pma.domain.Pet;
import com.example.pma.domain.PetGender;
import com.example.pma.domain.PetType;

public class PetDTO {

    private Long id;
    private PetType type;
    private String name;
    private PetGender gender;
    private String additionalInfo;
    private String image;
    private String missingSince;
    private String ownersPhone;
    private boolean isFound;
    
    private UserDTO owner;
    private AddressDTO address;

    public PetDTO() {}

    public PetDTO(Long id, PetType type, String name, PetGender gender, String additionalInfo, String image,
			String missingSince, String ownersPhone, boolean isFound, UserDTO owner) {
		super();
		this.id = id;
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

    public PetDTO(PetType type, String name, PetGender gender, String additionalInfo, String image,
			String missingSince, String ownersPhone, boolean isFound, UserDTO owner) {
		super();
		this.id = id;
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


	public PetDTO(Pet pet) {
    	this.id = pet.getId();
        this.type = pet.getType();
        this.name = pet.getName();
        this.gender = pet.getGender();
        this.additionalInfo = pet.getAdditionalInfo();
        this.image = pet.getImage();
        this.missingSince = convertDate(pet.getMissingSince());
        this.ownersPhone = pet.getOwnersPhone();
        this.isFound = pet.isFound();
        this.owner = new UserDTO(pet.getOwner());
        this.address = new AddressDTO(pet.getAddress());
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

    public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getImage() {
        return image;
    }

    public String getMissingSince() {
		return missingSince;
	}

	public void setMissingSince(String missingSince) {
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

	public void setImage(String image) {
		this.image = image;
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
	
	
    

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public String convertDate(Date date) {
		
		String pattern = "dd/MM/yyyy";
		
		DateFormat df = new SimpleDateFormat(pattern);
		
		return df.format(date);
	}
}
