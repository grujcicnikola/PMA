
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
		//return petRepo.findAllByIsFoundAndIsDeletedOrderByMissingSinceDesc(false, false);
		//return petRepo.findAllByIsFoundOrderByMissingSinceDesc(false);
		return petRepo.findByOrderByMissingSinceDesc();
	}

	public List<Pet> findAllByIsFoundAndIsDeleted(boolean f, boolean d) {
		// TODO Auto-generated method stub
		return petRepo.findAllByIsFoundAndIsDeleted(f, d);
	}

	public List<Pet> findAllByOwnerIdAndIsDeleted(Long ownerId, boolean deleted) {
		// TODO Auto-generated method stub
		return petRepo.findAllByOwnerIdAndIsDeletedOrderByMissingSinceDesc(ownerId, deleted);
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

	public void deletePet(Pet pet) {
		pet.setDeleted(true);
		petRepo.save(pet);
	}

	public List<Pet> findAllByOwnerId(Long id) {
		// TODO Auto-generated method stub
		return petRepo.findAllByOwnerIdOrderByMissingSinceDesc(id);
	}

	public List<Pet> findAllByIsFound(boolean b) {
		// TODO Auto-generated method stub
		return petRepo.findAllByIsFound(b);
	}
}
