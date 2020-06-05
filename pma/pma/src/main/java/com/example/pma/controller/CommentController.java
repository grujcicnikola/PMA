package com.example.pma.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.pma.domain.Comment;
import com.example.pma.domain.Pet;
import com.example.pma.domain.User;
import com.example.pma.dto.CommentDTO;
import com.example.pma.dto.PetDTO;
import com.example.pma.dto.UserDTO;
import com.example.pma.services.CommentService;
import com.example.pma.services.PetService;
import com.example.pma.services.UserService;

@RestController
@RequestMapping("comment")
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PetService petService;
	
	Converter converter = new Converter();
	
	@RequestMapping(value = "/getAllByPet/{petId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll(@PathVariable Long petId){
		//System.out.println("pogodio comments");
		
		
		List<Comment> comments = commentService.findAllByPetId(petId);
		List<CommentDTO> commentsDTO = converter.convertToCommentDTO(comments);
		
		return new ResponseEntity<>(commentsDTO,HttpStatus.OK);
	}
	
	@RequestMapping(value= "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@RequestBody CommentDTO commentDTO) {
		if(commentDTO.getUser().getEmail()==null || commentDTO.getPet().getId()==null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		User user =this.userService.getByEmail(commentDTO.getUser().getEmail());
		Pet pet = this.petService.findById(commentDTO.getPet().getId());
		Comment comment = new Comment(commentDTO.getMessage(), new Date(), user, pet);
		Comment saved =this.commentService.save(comment);
		return new ResponseEntity(new CommentDTO(saved), HttpStatus.OK);
	}

}
