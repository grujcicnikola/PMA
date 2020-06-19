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
	
	public double distance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}

}
