package com.bridgelabz.exceptions;

import com.bridgelabz.user.ResponseDTO.Response;

public class EmailAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException() {
		super("Email already registered");
	}

	public Response getResponse() {
		Response response = new Response();
		response.setMsg(this.getMessage());
		response.setStatus(-1);
		return response;
	}

}
