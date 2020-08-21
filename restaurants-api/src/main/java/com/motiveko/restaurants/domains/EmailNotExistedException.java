package com.motiveko.restaurants.domains;

import java.util.function.Supplier;

public class EmailNotExistedException extends RuntimeException {


	public EmailNotExistedException(String email) {
		super("Email do not exists : " + email);
	}

}
