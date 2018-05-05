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
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.exceptions.UnAuthorizedAccessUser;
import com.bridgelabz.user.ResponseDTO.CustomResponse;
import com.bridgelabz.user.ResponseDTO.RegisterErrors;
import com.bridgelabz.user.model.User;
import com.bridgelabz.user.model.UserDto;
import com.bridgelabz.user.service.UserService;
import com.bridgelabz.user.util.TokenUtils;
import com.bridgelabz.user.validation.UserValidator;

@RestController
//@RequestMapping("/user")
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private TokenUtils tokenUtils;
	RegisterErrors response = new RegisterErrors();

	// .................register api...............

	@RequestMapping(value = "register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUser(@Validated @RequestBody UserDto userDto, BindingResult bindingResult,
			HttpServletRequest request) throws Exception {

		userValidator.validate(userDto, bindingResult);
		List<FieldError> errors = bindingResult.getFieldErrors();

		RegisterErrors response = new RegisterErrors();
		CustomResponse customRes = new CustomResponse();
		if (bindingResult.hasErrors()) {
			logger.info("This is an info log entry");
			//response.setMsg("fill your details properly");
			//customRes.setMessage("fill your details properly");
			//customRes.setStatusCode(100);
			response.setMsg("registrtion fail");
			response.setStatus(-200);
			//response.setErrors(errors);
		//	response.setStatus(400);
		
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

	// .............forgot password api.............
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> forgotPassword(@RequestBody UserDto userDto, HttpServletRequest request) {
		CustomResponse customRes = new CustomResponse();
		try {
			System.out.println(userDto.getEmail());
			String url = request.getRequestURL().toString().substring(0, request.getRequestURL().lastIndexOf("/"));
			if (userService.forgetPassword(userDto.getEmail(), url))
				
			{   customRes.setMessage("forgot password");
			    customRes.setStatusCode(100);
		       return new ResponseEntity<CustomResponse> (customRes, HttpStatus.OK);
			} else {
				return new ResponseEntity<CustomResponse>( HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomResponse>(HttpStatus.NO_CONTENT);
		}
	}
	// ........reset password api................

	@RequestMapping(value = "/resetPassword/{jwtToken}", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> resetPassword(@RequestBody UserDto userDto,
			@PathVariable("jwtToken") String jwtToken,HttpServlet request) {

		CustomResponse customRes = new CustomResponse();
		 int id=TokenUtils.verifyToken(jwtToken);
		//int userId = TokenUtils.verifyToken(request.);
        //	int userId = (int) request).getAttribute("userId");

		//String email = userService.getUserEmailId(randomUUID);
		 User user= userService.getUserById(id);
		 userDto.setEmail( user.getEmail());

		if (userService.resetPassword(userDto)) {
			customRes.setMessage("Reset Password Sucessfully........");
			customRes.setStatusCode(100);
			return new ResponseEntity<CustomResponse>(customRes, HttpStatus.OK);

		} else {
			customRes.setMessage("Password Not Updated.......");
		/*	customRes.setMsg("Password Not Updated.......");*/

			return new ResponseEntity<CustomResponse>(customRes, HttpStatus.BAD_REQUEST);
		}
	}
   //new api for front side 
	@RequestMapping(value = "/resetPasswordLink/{jwtToken:.+}", method = RequestMethod.GET)
	public void resetPasswordLink(@PathVariable("jwtToken") String jwtToken, HttpServletResponse response) throws IOException  {
		
    // int id=TokenUtils.verifyToken(jwtToken);
	
     System.out.println("token is null or not");
	System.out.println("In side reset password link");
	response.sendRedirect("http://localhost:4200/resetpassword/"+jwtToken);
	
	}
	// ............./isActivUser Api............................
	@RequestMapping(value = "/RegistrationConfirm/{randomUUID}", method = RequestMethod.POST)
	public ResponseEntity<CustomResponse> isActiveUser(@PathVariable("randomUUID") String randomUUID) {

		CustomResponse customRes = new CustomResponse();

		if (userService.userActivation(randomUUID)) 
		{
			
			customRes.setMessage("user activation done successfully");
			customRes.setStatusCode(200);
			return new ResponseEntity<CustomResponse>(customRes, HttpStatus.CREATED);
		} else {
			
			customRes.setMessage("activation fail");
			customRes.setStatusCode(409);
			return new ResponseEntity<CustomResponse>(customRes, HttpStatus.CONFLICT);
		}

	}
	
	//.................loggedUser..APi....................................
	
	@RequestMapping(value="/user/loggeduser" , method = RequestMethod.GET)
	public ResponseEntity<?> getLoggeddUser(@RequestAttribute(name="userId") int userId) {
	     CustomResponse customRes = new CustomResponse();
		User user =userService.getUserById(userId); 
			if(user!=null) 
			{
				return new ResponseEntity<User>(user,HttpStatus.OK);
			}
		customRes.setMessage("no logged user");
		customRes.setStatusCode(409);
		return new ResponseEntity<CustomResponse>(customRes,HttpStatus.CONFLICT); 
	}
	
}
