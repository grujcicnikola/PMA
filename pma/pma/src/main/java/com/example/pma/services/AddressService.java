package com.example.pma.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pma.domain.Address;
import com.example.pma.repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	AddressRepository addressRepo;

	public void add(Address address) {
		// TODO Auto-generated method stub
		addressRepo.save(address);
	}

}
