package com.bridgelabz.user.service;

import com.bridgelabz.user.model.User;
import com.bridgelabz.user.model.UserDto;

public interface UserService {
	public void register(UserDto userDto,String requestURL);//register
	public String login(UserDto userDto);//login
	public User getUserByEmail(User user);//login
	
	//public void send(String subject, String msg, String [] toAddress, String [] ccAddress, String [] fromAddress);
 
	 boolean resetPassword(UserDto userDto);
	 public String getUserEmailId(String randomUUID);
	// String getUserNameById(String randomUUID);
	  boolean userActivation(String randomUUID);
	 public boolean forgetPassword(String email, String url);
	
	 
	public int getIdByEmail(String email);
	//public boolean saveToken(String email, String token);
	public User getUserById(int userId);
		
	

}
