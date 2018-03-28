package com.bridgelabz.user.service;

import java.util.UUID;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.exceptions.EmailAlreadyExistsException;
import com.bridgelabz.user.controller.UserController;
import com.bridgelabz.user.dao.UserDao;
import com.bridgelabz.user.model.User;
import com.bridgelabz.user.model.UserDto;
import com.bridgelabz.user.service.MailService;
import com.bridgelabz.user.util.TokenUtils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	public MailService mailService;
	@Autowired
	TokenUtils tokenObj;
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Transactional(rollbackFor = Exception.class)
	public void register(UserDto userDto, String requestURL) {

		User userFromDB = userDao.getUserByEmailId(userDto.getEmail());
		if (userFromDB != null) {
			throw new EmailAlreadyExistsException();
		}

		User user = new User(userDto);

		String hashCode = passwordEncoder.encode(userDto.getPassword());
		user.setPassword(hashCode);

		String randomUUID = UUID.randomUUID().toString();
		user.setRandomID(randomUUID);

		userDao.register(user);

		String to = user.getEmail();
		// String to="anilrspatil1992@gmail.com";
		String subject = "FundooPay registration verification";
		String message = requestURL + "/RegistrationConfirm/" + randomUUID;
		mailService.sendMail(to, subject, message);
	}

	public User getUserByEmail(User user) {
		return userDao.getUserByEmail(user);
	}

	public String login(UserDto userDto) {
		User user = new User();
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		System.out.println(user.getEmail());
		User userDbObj = userDao.getUserByEmailId(user.getEmail());

		if (userDbObj != null && userDbObj.getIsActive() == true
				&& BCrypt.checkpw(user.getPassword(), userDbObj.getPassword())) {
			int id = userDbObj.getId();
			String token = TokenUtils.generateToken(id);
			// String token=tokenObj.generateToken(id);
			System.out.println("toekn genrated :" + token);
			logger.info("token genrate" + token);
			// String hashCode = passwordEncoder.encode(user.getPassword());

			// user.setPassword(hashCode);
			return token;
		}
		return null;
	}

	@Override
	public boolean forgetPassword(String email, String requestURL) {
		boolean flag = false;
		User user = userDao.getUserByEmailId(email);
		if (user != null) {
			String emailID = user.getEmail();
			String randomUUID = UUID.randomUUID().toString();

			String to = "anilrspatil1992@gmail.com";
			String subject = "Link to reset password";
			String message = requestURL + "/resetPassword/" + randomUUID;
			mailService.sendMail(to, subject, message);
			flag = true;

			userDao.saveUsernameUid(emailID, randomUUID);
		}
		return flag;
	}

	public boolean resetPassword(UserDto userDto) {
		User user = new User();
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		String hascode = passwordEncoder.encode(userDto.getPassword());
		user.setPassword(hascode);
		boolean status = userDao.resetPassword(user.getPassword(), user.getEmail());
		return status;
	}

	@Override
	public boolean userActivation(String randomUUID) {
		User user = userDao.getUserByUIID(randomUUID);
		user.setActive(true);
		return userDao.activeUser(user);
	}

	@Override
	public String getUserEmailId(String randomUUID) {
		String email = userDao.getUserEmailId(randomUUID);
		return email;
	}

	public int getIdByEmail(String email) {
		int id = userDao.getIdByEmail(email);
		return id;
	}

	@Override
	public User getUserById(int userId) {
		User user=userDao.getUserById(userId);
		return user;
	}


}
