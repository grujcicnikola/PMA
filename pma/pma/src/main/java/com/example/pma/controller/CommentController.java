package com.example.pma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.pma.domain.Comment;
import com.example.pma.domain.Pet;
import com.example.pma.dto.CommentDTO;
import com.example.pma.dto.PetDTO;
import com.example.pma.services.CommentService;

@RestController
@RequestMapping("comment")
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	Converter converter = new Converter();
	
	@RequestMapping(value = "/getAllByPet/{petId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll(@PathVariable Long petId){
		List<Comment> comments = commentService.findAllByPetId(petId);
		List<CommentDTO> commentsDTO = converter.convertToCommentDTO(comments);
		
		return new ResponseEntity<>(commentsDTO,HttpStatus.OK);
	}

}
