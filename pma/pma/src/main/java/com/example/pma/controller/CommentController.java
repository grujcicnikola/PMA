package com.example.pma.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.gson.JsonObject;
import  com.example.pma.firebase.*;

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
	public ResponseEntity<?> getAll(@PathVariable Long petId) {
		// System.out.println("pogodio comments");

		List<Comment> comments = commentService.findAllByPetId(petId);
		List<CommentDTO> commentsDTO = converter.convertToCommentDTO(comments);

		return new ResponseEntity<>(commentsDTO, HttpStatus.OK);
	}


	

	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addComment(@RequestBody CommentDTO commentDTO) {
		if (commentDTO.getUser().getEmail() == null || commentDTO.getPet().getId() == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		User user = this.userService.getByEmail(commentDTO.getUser().getEmail());
		Pet pet = this.petService.findById(commentDTO.getPet().getId());
		Comment comment = new Comment(commentDTO.getMessage(), new Date(), user, pet);
		Comment saved = this.commentService.save(comment);
		if (pet.getOwner().getEmail() != user.getEmail()) {
			//FCMHelper.getInstance().sendNotification(type, typeParameter, notificationObject)
			FCMHelper fcm = FCMHelper.getInstance();
			JsonObject notificationObject = new JsonObject(); 
			notificationObject.addProperty("body", commentDTO.getMessage());
			notificationObject.addProperty("title", commentDTO.getUser().getEmail());
			
			JsonObject dataObject = new JsonObject(); 
			dataObject.addProperty("name", pet.getName());
			dataObject.addProperty("type", pet.getType().toString());
			dataObject.addProperty("missing_since", pet.getMissingSince().toString().split(" ")[0]);
			dataObject.addProperty("image", pet.getImage());
			dataObject.addProperty("id", pet.getId());
			dataObject.addProperty("additionalInfo", pet.getAdditionalInfo());
			
			try {
				fcm.sendNotifictaionAndData(FCMHelper.TYPE_TO, pet.getOwner().getToken(), notificationObject,dataObject);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} 
		}
		
		return new ResponseEntity(new CommentDTO(saved), HttpStatus.OK);
	}

	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteComment(@PathVariable Long id) {
		if (id == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		Comment comment = this.commentService.findById(id);
		Long petId = comment.getPet().getId();
		this.commentService.remove(id);
		List<Comment> comments = commentService.findAllByPetId(petId);
		List<CommentDTO> commentsDTO = converter.convertToCommentDTO(comments);

		return new ResponseEntity<>(commentsDTO, HttpStatus.OK);
	}

}
