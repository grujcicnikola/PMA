package com.example.pma.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.pma.domain.Address;
import com.example.pma.domain.Pet;
import com.example.pma.domain.User;
import com.example.pma.dto.PetDTO;
import com.example.pma.services.AddressService;
import com.example.pma.services.PetService;
import com.example.pma.services.UserService;

@RestController
@RequestMapping("pet")
public class PetController {
	
	@Autowired
	PetService petService;
	@Autowired
	AddressService addressService;
	
	@Autowired
	private UserService userService;
	
	Converter converter = new Converter();
	
	private final Path fileLocation;
	
	public PetController() {
		
		this.fileLocation = Paths.get("src/main/resources/static/images")
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileLocation);
        } catch (Exception ex) {
            System.out.println("Errorrr while creating dir");
        }
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll(){
		
		List<Pet> pets = petService.findAll();
		
		List<PetDTO> petsDTO = converter.convertToPetDTO(pets);
		
		return new ResponseEntity<>(petsDTO,HttpStatus.OK);
	}

	@RequestMapping(value = "/getMissing", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMissing(){
		List<Pet> pets = petService.findAllByIsFound(false);
		List<PetDTO> petsDTO = converter.convertToPetDTO(pets);
		
		return new ResponseEntity<>(petsDTO,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getByOwner/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getByOwner(@PathVariable String email){
		
		
		User user = userService.getByEmail(email);
		
		List<Pet> pets = petService.findAllByOwnerId(user.getId());
		
		List<PetDTO> petsDTO = converter.convertToPetDTO(pets);
		
		return new ResponseEntity<>(petsDTO,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/postMissing", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postMissing(@RequestBody PetDTO pet){
		System.out.println("Stigao u post");
		
		if (pet.getAddress() == null) {
			System.out.println("Nema adreseeee");
			return new ResponseEntity<>(pet,HttpStatus.BAD_REQUEST);
		}
		Address address = new Address(pet.getAddress());
		addressService.add(address);
		
		 DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
         Date d = null;
         try {
             d = format.parse(pet.getMissingSince());
         } catch (ParseException e) {
             e.printStackTrace();
         } catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Ovde je trulo");
			return new ResponseEntity<>(pet,HttpStatus.BAD_REQUEST);
		}
		Pet a = new Pet(pet.getType(), pet.getName(), pet.getGender(), pet.getAdditionalInfo(), pet.getImage(), d,
				pet.getOwnersPhone(), false, userService.getByEmail(pet.getOwner().getEmail()), address);
		
		Pet p = petService.addNewPet(a);
		
		return new ResponseEntity<>(p,HttpStatus.OK);
	}
	
//	@PostMapping(value = "/postTry")
//	public ResponseEntity<?> postMissing(@RequestPart("newImage") MultipartFile file, @RequestBody PetDTO pet){
//	
//		System.out.println("Zahtev stigaoooooooooo, fajl: " + file.getName());
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//	
	@PostMapping(value = "/uploadPhoto")
	public ResponseEntity<?> postMissing(@RequestPart("newImage") MultipartFile file){
	
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		System.out.println("IMESLIKE: " + fileName);
		try {
            
        	Path targetLocation = this.fileLocation.resolve(fileName);
            
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new ResponseEntity<>(targetLocation.toString(), HttpStatus.OK);
            
        } catch (IOException ex) {
            System.out.println("Could not store file " + fileName + ". Please try again!");
            
        }
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	}
}

	
	
