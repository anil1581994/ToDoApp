package com.bridgelabz.exceptions;

import com.bridgelabz.user.ResponseDTO.Response;

public class UnAuthorizedAccessUser extends RuntimeException {

	public UnAuthorizedAccessUser() {
		super("login denied");
	}
}
