package com.bridgelabz.user.model;

public class User {

	private int id;
	private String name;
	private String email;
	private String password;
	private String mobileNumber;
	private String randomID;
	private boolean isActive;
	private String token;

	public User() {

	}

	public User(UserDto userDto) {
		this.setName(userDto.getName());
		this.setEmail(userDto.getEmail());
		this.setMobileNumber(userDto.getMobileNumber());
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getRandomID() {
		return randomID;
	}

	public void setRandomID(String randomID) {
		this.randomID = randomID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}
