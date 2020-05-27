package com.example.pma.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.pma.domain.Pet;
import com.example.pma.domain.User;
import com.example.pma.dto.PetDTO;
import com.example.pma.services.PetService;
import com.example.pma.services.UserService;

@RestController
@RequestMapping("pet")
public class PetController {
	
	@Autowired
	PetService petService;
	
	@Autowired
	private UserService userService;
	
	Converter converter = new Converter();

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
}
