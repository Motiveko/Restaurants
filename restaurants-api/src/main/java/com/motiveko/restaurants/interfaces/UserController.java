package com.motiveko.restaurants.interfaces;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.motiveko.restaurants.applictaions.UserService;
import com.motiveko.restaurants.domains.User;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User resource) throws URISyntaxException{
		
		String email = resource.getEmail();
		String name = resource.getName();
		String password = resource.getPassword();
		
		
		User user = userService.registerUser(email,name,password);
		String url = "/"; // uri 만들어줘야하는가? 이게 redirect되는건가?	
		return ResponseEntity.created(new URI(url)).body("{}");
	}
	
}
