package com.example.pma;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.pma.domain.Pet;
import com.example.pma.domain.PetGender;
import com.example.pma.domain.PetType;
import com.example.pma.domain.User;
import com.example.pma.services.PetService;
import com.example.pma.services.UserService;

@Component
public class FillData {
	
	@Autowired
	private PetService petService;
	
	@Autowired
	private UserService userService;

	@PostConstruct
    public void init() throws URISyntaxException, IOException {
		
		//USERS
		User user = new User("jova@gmail.com", "123");
		userService.saveUser(user);
		
		User user1 = new User("pera@gmail.com", "123");
		userService.saveUser(user1);
		
		User user2 = new User("marko@gmail.com", "123");
		userService.saveUser(user2);
		
		User user3 = new User("milica@gmail.com", "123");
		userService.saveUser(user3);
		
		User user4 = new User("jelena@gmail.com", "123");
		userService.saveUser(user4);
		
		User user5 = new User("dragana@gmail.com", "123");
		userService.saveUser(user5);
		
		
		//PETS
		Pet pet = new Pet(PetType.DOG,"Dzeki",PetGender.MALE, "Pas ima zelenu ogrlicu", "puppydog.jpg", new Date(), "123-456", false, user);
		petService.addNewPet(pet);
		
		Pet pet1 = new Pet(PetType.DOG,"Aleks",PetGender.MALE, "Opis neki", "labrador.jpg", new Date(), "123-456", false, user1);
		petService.addNewPet(pet1);
		
		Pet pet2 = new Pet(PetType.CAT,"Djura",PetGender.MALE, "Ne prilazi nepoznatima", "cat.jpg", new Date(), "123-456", false, user2);
		petService.addNewPet(pet2);
		
		Pet pet3 = new Pet(PetType.CAT,"Kiki",PetGender.FEMALE, "Ruska plava macka", "russiancat.jpg", new Date(), "021/444-444", false, user);
		petService.addNewPet(pet3);
		
		Pet pet4 = new Pet(PetType.DOG,"Bobi",PetGender.MALE, "Drustven, prilazi deci", "samojedjpg.jpg", new Date(), "021/123-456", false, user3);
		petService.addNewPet(pet4);
		
		Pet pet5 = new Pet(PetType.DOG,"Moksi",PetGender.FEMALE, "Laje ali ne ujeda", "pup.jpg", new Date(), "123-456", false, user4);
		petService.addNewPet(pet5);
		
		Pet pet6 = new Pet(PetType.DOG,"Reks",PetGender.MALE, "Vucjak, star godinu dana, pobegao u blizini marketa", "download.jpg", new Date(), "123456789", false, user5);
		petService.addNewPet(pet6);
		
		Pet pet7 = new Pet(PetType.DOG,"Mona",PetGender.FEMALE, "Pas ima crvenu ogrlicu sa kodom", "dalmatian.jpg", new Date(), "123456789", false, user2);
		petService.addNewPet(pet7);
		
	}
}
