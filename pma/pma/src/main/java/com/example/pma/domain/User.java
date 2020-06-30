package com.example.pma.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column
	private String email;
	
	@Column(nullable = true)
    private String password;
	
	@Column
    private String token;
	
	@Column
	private boolean googleLogin;
	
	@OneToMany
	private List<Pet> pets = new ArrayList<Pet>();
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public User(String email, String password, boolean googleLogin) {
		super();
		this.email = email;
		this.password = password;
		this.googleLogin = googleLogin;
	}
	
	public User(Long id, String email, String password, boolean googleLogin) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
	}



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isGoogleLogin() {
		return googleLogin;
	}

	public void setGoogleLogin(boolean googleLogin) {
		this.googleLogin = googleLogin;
	}
	

}

