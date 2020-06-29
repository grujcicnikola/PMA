
package com.example.pma.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.pma.domain.Pet;
import com.example.pma.repository.PetRepository;

@Service
public class PetService {
	
	@Autowired
	PetRepository petRepo;

	public List<Pet> findAll() {
		// TODO Auto-generated method stub
		return petRepo.findAllByIsFoundOrderByMissingSinceDesc(false);
	}

	public List<Pet> findAllByIsFound(boolean b) {
		// TODO Auto-generated method stub
		return petRepo.findAllByIsFound(b);
	}

	public List<Pet> findAllByOwnerId(Long ownerId) {
		// TODO Auto-generated method stub
		return petRepo.findAllByOwnerIdOrderByMissingSinceDesc(ownerId);
	}

	public Pet addNewPet(Pet pet) {
		
		return petRepo.save(pet);
	}

	
	public Pet findById(Long id) {
		// TODO Auto-generated method stub
		return petRepo.findById(id).get();
	}
	
	public void deleteItem(Pet pet) {
		
		petRepo.delete(pet);
	}
}
