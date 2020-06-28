package com.example.pma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pma.domain.Comment;
import com.example.pma.domain.Pet;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findAllByPetIdOrderByDateAsc(Long petId);

	

}
