
package com.example.pma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pma.domain.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

	List<Pet> findAllByIsFound(boolean b);
	
	List<Pet> findAllByOwnerIdOrderByMissingSinceDesc(Long ownerId);
	
	List<Pet> findAllByIsFoundOrderByMissingSinceDesc(boolean b);

}
