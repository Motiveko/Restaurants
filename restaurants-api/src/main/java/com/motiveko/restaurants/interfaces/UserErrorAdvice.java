package com.motiveko.restaurants.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.motiveko.restaurants.domains.EmailExistedException;

@ControllerAdvice
public class UserErrorAdvice {

	@ExceptionHandler(EmailExistedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleEmailExisted() {
		return "Email Already Existed";
	}
	
}
