package com.example.pma.dto;

import com.example.pma.domain.User;

public class UserDTO {
	
	private Long id;
	private String email;
    private String password;
    private String passwordNew;
    private String token;
    private boolean googleLogin;
    
    
	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserDTO(Long id, String email, String password, boolean googleLogin) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.googleLogin  = googleLogin;
	}
	
	public UserDTO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPasswordNew() {
		return passwordNew;
	}
	public void setPasswordNew(String passwordNew) {
		this.passwordNew = passwordNew;
	}
	public boolean isGoogleLogin() {
		return googleLogin;
	}
	public void setGoogleLogin(boolean googleLogin) {
		this.googleLogin = googleLogin;
	}
	
}
