package com.bridgelabz.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.exceptions.UnAuthorizedAccessUser;
import com.bridgelabz.user.ResponseDTO.CustomResponse;
import com.bridgelabz.user.ResponseDTO.RegisterErrors;
import com.bridgelabz.user.model.EmailProperties;
import com.bridgelabz.user.model.User;
import com.bridgelabz.user.model.UserDto;
import com.bridgelabz.user.service.UserService;
import com.bridgelabz.user.util.TokenUtils;
import com.bridgelabz.user.validation.UserValidator;

/**<p>This is RestController class map the requestUrl to respected API <p>
 * @author bridgeit
 *
 */
@RestController
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private TokenUtils tokenUtils;
	RegisterErrors response = new RegisterErrors();
	
	@Autowired
	EmailProperties emailProperties;


	/**
	 * <p>
	 * The API is register to the new User TODOApplication
	 * </p>
	 * 
	 * @param userDto
	 * @param bindingResult
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@Validated @RequestBody UserDto userDto, BindingResult bindingResult,
			HttpServletRequest request) throws Exception {

		userValidator.validate(userDto, bindingResult);
		List<FieldError> errors = bindingResult.getFieldErrors();

		RegisterErrors response = new RegisterErrors();
		CustomResponse customRes = new CustomResponse();
		if (bindingResult.hasErrors()) {
			logger.info("This is an info log entry");
			response.setMsg("registrtion fail");
			response.setStatus(-200);

			return new ResponseEntity<RegisterErrors>(response, HttpStatus.CONFLICT);
		}

		String url = request.getRequestURL().toString().substring(0, request.getRequestURL().lastIndexOf("/"));
		userService.register(userDto, url);

		response.setMsg("user register successfully");
		response.setStatus(200);

		logger.info("This is info message");

		return new ResponseEntity<RegisterErrors>(response, HttpStatus.CREATED);

	}

	/**
	 * <p>
	 * This API is used to login purpose user is going to login in TODOApplication
	 * and sucessfully login jwtToken is going to return to user
	 * </p>
	 * 
	 * @param userDto
	 * @param response
	 * @return JwtToken
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletResponse response) {

		CustomResponse customRes = new CustomResponse();
		String token = userService.login(userDto);
		if (token != null) {
			response.setHeader("Authorization", token);
			customRes.setMessage("user login successfully");
			customRes.setStatusCode(100);

			return new ResponseEntity<CustomResponse>(customRes, HttpStatus.OK);
		} else {

			throw new UnAuthorizedAccessUser();
		}

	}

	/**
	 * <p>
	 * This API handle the forgotPassword request of user and redirect to him to reset his password
	 * </p>
	 * 
	 * @param userDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> forgotPassword(@RequestBody UserDto userDto, HttpServletRequest request) {
		CustomResponse customRes = new CustomResponse();
		try {
			System.out.println(userDto.getEmail());
			String url = request.getRequestURL().toString().substring(0, request.getRequestURL().lastIndexOf("/"));
			if (userService.forgetPassword(userDto.getEmail(), url))

			{
				customRes.setMessage("forgot password");
				customRes.setStatusCode(100);
				return new ResponseEntity<CustomResponse>(customRes, HttpStatus.OK);
			} else {
				return new ResponseEntity<CustomResponse>(HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomResponse>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * <p>
	 * This is resetPassword API is used to reset password
	 * </p>
	 * 
	 * @param userDto
	 * @param jwtToken
	 * @param request
	 * @return updated password
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> resetPassword(@RequestBody UserDto userDto,
			@RequestParam("jwtToken") String jwtToken) {

		CustomResponse customRes = new CustomResponse();
		int id = TokenUtils.verifyToken(jwtToken);
		User user = userService.getUserById(id);
		userDto.setEmail(user.getEmail());

		if (userService.resetPassword(userDto)) {
			customRes.setMessage("Reset Password Sucessfully........");
			customRes.setStatusCode(100);
			return new ResponseEntity<CustomResponse>(customRes, HttpStatus.OK);

		} else {
			customRes.setMessage("Password Not Updated.......");
			return new ResponseEntity<CustomResponse>(customRes, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * <p>
	 * This is resetPasswordLink API is used redirect the request to front resetPassword view component
	 * </p>
	 * 
	 * @param jwtToken
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value = "/resetPasswordLink/{jwtToken:.+}", method = RequestMethod.GET)
	public void resetPasswordLink(@PathVariable("jwtToken") String jwtToken, HttpServletResponse response,
			HttpServletRequest request) throws IOException {

		logger.info("In side reset password link");
		System.out.print("url for front end-->" + request.getHeader("origin"));
		System.out.print("your fronENd url "+emailProperties.getFrontEndHost());
		response.sendRedirect(emailProperties.getFrontEndHost()+"/resetpassword?jwtToken=" + jwtToken);

	}

	/**
	 * <p>
	 * This is isActivUser API to Activate the user when user is register successfully in TODOApplication
	 * </p>
	 * 
	 * @param randomUUID
	 * @return Active state of user
	 */
	@RequestMapping(value = "/RegistrationConfirm/{randomUUID}", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> isActiveUser(@PathVariable("randomUUID") String randomUUID) {

		CustomResponse customRes = new CustomResponse();

		if (userService.userActivation(randomUUID)) {

			customRes.setMessage("user activation done successfully");
			customRes.setStatusCode(200);
			return new ResponseEntity<CustomResponse>(customRes, HttpStatus.CREATED);
		} else {

			customRes.setMessage("activation fail");
			customRes.setStatusCode(409);
			return new ResponseEntity<CustomResponse>(customRes, HttpStatus.CONFLICT);
		}

	}

	/**
	 * <p>
	 * This is loggedUser API used to get the currently logged user
	 * </p>
	 * 
	 * @param userId
	 * @return LoggedUser
	 */
	@RequestMapping(value = "/user/loggeduser", method = RequestMethod.GET)
	public ResponseEntity<?> getLoggeddUser(@RequestAttribute(name = "userId") int userId) {
		CustomResponse customRes = new CustomResponse();
		User user = userService.getUserById(userId);
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		customRes.setMessage("no logged user");
		customRes.setStatusCode(409);
		return new ResponseEntity<CustomResponse>(customRes, HttpStatus.CONFLICT);
	}

}
