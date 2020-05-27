package com.example.pma.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pma.domain.User;
import com.example.pma.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRep;
	
	public User saveUser(User user)
	{
		return userRep.save(user);
	}
	
	public User getByEmail(String email) {
		
		return userRep.findByEmail(email);
	}

}
