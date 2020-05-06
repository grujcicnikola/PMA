package com.example.pma.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

import com.example.pma.domain.Comment;
import com.example.pma.domain.Pet;
import com.example.pma.domain.User;

public class CommentDTO {
	
	private Long id;
    private String message;
    private Date date;
	private UserDTO user;
	private PetDTO pet;
	
	
	
	public CommentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CommentDTO(Long id, String message, Date date, UserDTO user, PetDTO pet) {
		super();
		this.id = id;
		this.message = message;
		this.date = date;
		this.user = user;
		this.pet = pet;
	}
	
	public CommentDTO(Comment comment) {
		this.id = comment.getId();
		this.message = comment.getMessage();
		this.date = comment.getDate();
		this.user = new UserDTO(comment.getUser());
		this.pet = new PetDTO(comment.getPet());
    }
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	public PetDTO getPet() {
		return pet;
	}
	public void setPet(PetDTO pet) {
		this.pet = pet;
	}
	
	

}
