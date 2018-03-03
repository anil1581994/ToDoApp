package com.bridgelabz.user.validation;

import java.util.regex.Pattern;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.bridgelabz.user.model.User;
import com.bridgelabz.user.model.UserDto;

public class UserValidator implements Validator {
   /*@Autowired
	ResourceBundleMessageSource messageSource;*/
	public boolean supports(Class<?> clazz) {
		
		return User.class.equals(clazz);
	}

	public void validate(Object obj, Errors err)
	{

	     // ValidationUtils.rejectIfEmpty(err, "id", "user.id.empty");
	      ValidationUtils.rejectIfEmpty(err, "name", "user.name.empty");
	      ValidationUtils.rejectIfEmpty(err, "email", "user.email.empty");
	      ValidationUtils.rejectIfEmpty(err, "password", "user.password.empty");
	      ValidationUtils.rejectIfEmpty(err, "mobileNumber", "user.mobileNumber.empty");

	      UserDto userDto = (UserDto) obj;
	     
	      Pattern pattern = Pattern.compile( "^[a-zA-Z\\s]+",Pattern.CASE_INSENSITIVE);
		      if (!(pattern.matcher(userDto.getName()).matches())) 
		      {
		         err.rejectValue("name", "user name invalid");
		      }

	      Pattern pattern1 = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
	            Pattern.CASE_INSENSITIVE);
	      if (!(pattern1.matcher(userDto.getEmail()).matches())) 
	      {
	         err.rejectValue("email", "user email invalid");
	      }
           
	      Pattern pattern2 = Pattern.compile("[a-zA-Z0-9]{8,16}");
	      if (!(pattern2.matcher(userDto.getPassword()).matches())) 
	      {
	         err.rejectValue("password", "user password empty");
	      }
	      Pattern pattern3 = Pattern.compile("^[0-9]{10}$");
	      if (!(pattern3.matcher(userDto.getMobileNumber()).matches())) 
	      {
	         err.rejectValue("mobileNumber", "user mobileNumber empty");
	      }
	      
	      
	   }//m()
		
	}//class


