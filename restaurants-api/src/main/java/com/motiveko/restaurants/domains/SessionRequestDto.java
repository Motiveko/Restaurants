package com.motiveko.restaurants.domains;

import lombok.Data;

@Data
public class SessionRequestDto {

	private String email;
	
	private String password;
}
