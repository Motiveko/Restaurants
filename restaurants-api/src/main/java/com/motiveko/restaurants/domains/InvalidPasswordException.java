package com.motiveko.restaurants.domains;

public class InvalidPasswordException extends RuntimeException {

	public InvalidPasswordException() {
		super("Invalid Password!");
	}
}
