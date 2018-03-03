package com.bridgelabz.user.dao;

import com.bridgelabz.user.model.User;
import com.bridgelabz.user.model.UserDto;

public interface UserDao 
{
	public void register(User user);

	public User getUserByEmail(User user);

	User getUserByEmailId(String email);

	boolean saveUsernameUid(String name, String randomUUID);

	String getUserEmailId(String randomUUID);

	public boolean resetPassword(String name, String password);

	public boolean activeUser(User user);

	public User getUserByUIID(String randomUUID);
	
	public int getIdByEmail(String email);
	

	public  User getUserById(int userId); 
		
	//public boolean saveToken(String email, String token);
	
}
