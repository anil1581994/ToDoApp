package com.bridgelabz.exceptions.handlers;

import org.apache.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.exceptions.DatabaseException;
import com.bridgelabz.exceptions.EmailAlreadyExistsException;
import com.bridgelabz.exceptions.UnAuthorizedAccessUser;
import com.bridgelabz.note.controller.NoteController;
import com.bridgelabz.user.ResponseDTO.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = EmailAlreadyExistsException.class)
	public ResponseEntity<Response> emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException e) {
		Response response = e.getResponse();
		logger.error(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(value = DatabaseException.class)
	public ResponseEntity<Response> databaseExceptionHandler(DatabaseException e) {
		return new ResponseEntity<>(e.getResponse(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = UnAuthorizedAccessUser.class)
	public ResponseEntity<Response> unAuthorizedAccessUser(UnAuthorizedAccessUser e) {
		Response response = new Response();
		response.setMsg(e.getMessage());
		response.setStatus(-1);
		logger.error(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<Response> runtimeHandler(RuntimeException e) {
		e.printStackTrace();
		Response response = new Response();
		response.setMsg("Something went wrong");
		response.setStatus(-1);
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

}
