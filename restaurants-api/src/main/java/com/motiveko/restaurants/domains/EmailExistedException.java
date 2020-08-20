package com.motiveko.restaurants.domains;

public class EmailExistedException extends RuntimeException {

	public EmailExistedException(String email) {
		super("Email is Already Existed : " + email );
	}

}
