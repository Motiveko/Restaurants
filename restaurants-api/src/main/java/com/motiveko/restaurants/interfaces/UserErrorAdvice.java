package com.motiveko.restaurants.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.motiveko.restaurants.domains.EmailExistedException;
import com.motiveko.restaurants.domains.EmailNotExistedException;
import com.motiveko.restaurants.domains.InvalidPasswordException;

@ControllerAdvice
public class UserErrorAdvice {

	@ExceptionHandler(EmailExistedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleEmailExisted() {
		return "Email Already Exists!";
	}
	
	// 보안을 위해서 email, password 모두 return값을 똑같이 할 필요가 있다. 나중문제
	@ExceptionHandler(EmailNotExistedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleEmaliNotExisted() {
		
		return "Email does not exist!";
	}
	
	@ExceptionHandler(InvalidPasswordException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleInvalidPassword() {
		
		return "Invalid Password!";
	}
	

}
