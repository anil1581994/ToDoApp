package com.bridgelabz.user.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.user.ResponseDTO.CustomerResponse;
import com.bridgelabz.user.ResponseDTO.RegisterErrors;
import com.bridgelabz.user.model.UserDto;
import com.bridgelabz.user.service.UserService;
import com.bridgelabz.user.util.TokenUtils;
import com.bridgelabz.user.validation.UserValidator;

@RestController
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private TokenUtils tokenObj;
	RegisterErrors response = new RegisterErrors();

	// .................register api...............

	@RequestMapping(value = "register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@Validated @RequestBody UserDto userDto, BindingResult bindingResult,
			HttpServletRequest request) throws Exception {

		userValidator.validate(userDto, bindingResult);
		List<FieldError> errors = bindingResult.getFieldErrors();

		RegisterErrors response = new RegisterErrors();
		if (bindingResult.hasErrors()) {
			logger.info("This is an info log entry");
			response.setMsg("fill your details properly");
			response.setErrors(errors);
			response.setStatus(400);

			return new ResponseEntity<RegisterErrors>(response, HttpStatus.CONFLICT);
		}

		String url = request.getRequestURL().toString().substring(0, request.getRequestURL().lastIndexOf("/"));
		userService.register(userDto, url);

		response.setMsg("user register successfully");
		response.setStatus(200);
		logger.info("This is info message");

		return new ResponseEntity<RegisterErrors>(response, HttpStatus.CREATED);

	}
	// ........login api...........

	@RequestMapping(value = "login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletResponse response) {

		CustomerResponse customRes = new CustomerResponse();
		String token = userService.login(userDto);
		if (token != null) {
			response.setHeader("Authorization", token);
			customRes.setMessage("user login successfully");
			customRes.setStatusCode(100);

			return new ResponseEntity<CustomerResponse>(customRes, HttpStatus.OK);
		} else {
			customRes.setMessage("login fail");
			customRes.setStatusCode(410);
			return new ResponseEntity<CustomerResponse>(customRes, HttpStatus.CONFLICT);
		}

	}

	// .............forgot password api.............
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ResponseEntity<Void> forgotPassword(@RequestBody UserDto userDto, HttpServletRequest request) {
		try {
			System.out.println(userDto.getEmail());
			String url = request.getRequestURL().toString().substring(0, request.getRequestURL().lastIndexOf("/"));
			if (userService.forgetPassword(userDto.getEmail(), url)) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			} else {
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	// ........reset password api................

	@RequestMapping(value = "/resetPassword/{randomUUID}", method = RequestMethod.POST)
	public ResponseEntity<CustomerResponse> resetPassword(@RequestBody UserDto userDto,
			@PathVariable("randomUUID") String randomUUID) {

		CustomerResponse customRes = new CustomerResponse();
		String email = userService.getUserEmailId(randomUUID);
		userDto.setEmail(email);

		if (userService.resetPassword(userDto)) {
			customRes.setMessage("Reset Password Sucessfully........");

			return new ResponseEntity<CustomerResponse>(customRes, HttpStatus.OK);

		} else {
			customRes.setMessage("Password Not Updated.......");

			return new ResponseEntity<CustomerResponse>(customRes, HttpStatus.BAD_REQUEST);
		}
	}

	// ............./isActivUser Api............................
	@RequestMapping(value = "/RegistrationConfirm/{randomUUID}", method = RequestMethod.POST)
	public ResponseEntity<CustomerResponse> isActiveUser(@PathVariable("randomUUID") String randomUUID) {

		CustomerResponse customRes = new CustomerResponse();

		if (userService.userActivation(randomUUID)) {
			customRes.setMessage("user activation done successfully");
			customRes.setStatusCode(200);
			return new ResponseEntity<CustomerResponse>(customRes, HttpStatus.CREATED);
		} else {
			customRes.setMessage("activation fail");
			customRes.setStatusCode(409);
			return new ResponseEntity<CustomerResponse>(customRes, HttpStatus.CONFLICT);
		}

	}
	// ......................./tokenGenartion Api
}
