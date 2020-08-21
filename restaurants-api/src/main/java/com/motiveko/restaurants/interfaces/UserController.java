package com.motiveko.restaurants.interfaces;

import java.net.URI;
import java.net.URISyntaxException;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.motiveko.restaurants.applications.UserService;
import com.motiveko.restaurants.domains.User;

@CrossOrigin
@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("/registerUser")
	public ResponseEntity<?> registerUser(@RequestBody User resource) throws URISyntaxException{
		
		String email = resource.getEmail();
		String name = resource.getName();
		String password = resource.getPassword();
		
		
		User user = userService.registerUser(email,name,password);
		String url = "/"; // uri 만들어줘야하는가? 이게 redirect되는건가?	
		return ResponseEntity.created(new URI(url)).body("{}");
	}
	
	// 회원정보 수정
	@PatchMapping("/updateUser/{userId}")
	public ResponseEntity<?> updateUser(
							@PathVariable("userId") Long userId,
							@RequestBody User resource) throws URISyntaxException{
		
		String email = resource.getEmail();
		String name = resource.getName();
		String password = resource.getPassword();
		String url = "/" + userId;
		
		userService.updateUser(userId,email,name,password);
		
		
		return ResponseEntity.created(new URI(url)).body("{}");
	}
	
	// 회원정보 삭제
	@DeleteMapping("/deleteUser/{userId}")
	public String deactiveUser(@PathVariable("userId")Long userId) {
		
		userService.deactiveUser(userId);
		
		return "userDeleted";
	}
	
	// 로긴은 따로만들자. 세션을이용한다.
}
