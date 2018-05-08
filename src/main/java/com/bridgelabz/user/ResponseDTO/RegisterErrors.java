package com.bridgelabz.user.ResponseDTO;

import java.util.List;

import org.springframework.validation.FieldError;

public class RegisterErrors extends Response {
	List<FieldError> errors;

	public List<FieldError> getErrors() {
		return errors;
	}

	public void setErrors(List<FieldError> errors) {
		this.errors = errors;
	}

}
