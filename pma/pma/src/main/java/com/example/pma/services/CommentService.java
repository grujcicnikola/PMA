package com.example.pma.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pma.domain.Comment;
import com.example.pma.domain.Pet;
import com.example.pma.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	CommentRepository commentRepo;

	public List<Comment> findAllByPetId(Long petId) {
		// TODO Auto-generated method stub
		return commentRepo.findAllByPetId(petId);
	}
	
	public Comment save(Comment comment) {
		
		return commentRepo.save(comment);
	}

	
}
