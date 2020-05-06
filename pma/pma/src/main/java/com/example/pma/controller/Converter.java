package com.example.pma.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.pma.domain.Comment;
import com.example.pma.domain.Pet;
import com.example.pma.dto.CommentDTO;
import com.example.pma.dto.PetDTO;

public class Converter {
	
	public List<PetDTO> convertToPetDTO(List<Pet> pets){
		List<PetDTO> petsDTO = new ArrayList<PetDTO>();
		for(int i = 0; i< pets.size(); i++) {
			petsDTO.add(new PetDTO(pets.get(i)));
		}
		return petsDTO;
	}
	
	public List<CommentDTO> convertToCommentDTO(List<Comment> comments){
		List<CommentDTO> commentsDTO = new ArrayList<CommentDTO>();
		for(int i = 0; i< comments.size(); i++) {
			commentsDTO.add(new CommentDTO(comments.get(i)));
		}
		return commentsDTO;
	}

}
