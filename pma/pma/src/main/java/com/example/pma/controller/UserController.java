package com.example.pma.controller;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.pma.domain.User;
import com.example.pma.dto.UserDTO;
import com.example.pma.services.UserService;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllUsers(){
		
		return null;
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loginUser(@RequestBody UserDTO userLogin){
		
		System.out.println("Pogodio login!");
		User user = userService.getByEmail(userLogin.getEmail());
		
		if(user == null)
		{
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		
		if(!user.getPassword().equals(userLogin.getPassword()))
		{
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(value= "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		
		if(userService.getByEmail(userDTO.getEmail()) != null)
		{
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		User user =  new User(userDTO.getEmail(), userDTO.getPassword());
		userService.saveUser(user);
		
		return new ResponseEntity(HttpStatus.OK);
	}
}
