package com.bridgelabz.exceptions;

import com.bridgelabz.user.ResponseDTO.Response;

public class DatabaseException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public DatabaseException() {
		super("Error in database operation");
	}

	public Response getResponse() {
		Response response = new Response();
		response.setMsg(this.getMessage());
		response.setStatus(-1);
		return response;
	}
}
