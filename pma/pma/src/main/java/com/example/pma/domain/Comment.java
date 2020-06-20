package com.example.pma.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column
    private String message;
	@Column
    private Date date;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Pet pet;
	
	

	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Comment(String message, Date date, User user, Pet pet) {
		super();
		this.message = message;
		this.date = date;
		this.user = user;
		this.pet = pet;
		
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	
	

}
