package com.motiveko.restaurants.interfaces;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.motiveko.restaurants.applictaions.UserService;
import com.motiveko.restaurants.domains.SessionRequestDto;
import com.motiveko.restaurants.domains.User;

@RestController
public class LoginController {

	
	@Autowired
	UserService userService;
	
	@PostMapping("/login")
	public String login(@RequestBody SessionRequestDto resource
						,HttpSession session
						) throws Throwable {
		
		String email = resource.getEmail();
		String password = resource.getPassword();
		System.out.println("무슨일이야?");
		User user = userService.authenticate(email,password);
		
		// 문제없으면 session에 사용자정보 입력쓰
		if(user!=null) {
			session.setAttribute("sEmail", user.getEmail());
			session.setAttribute("sName", user.getName());
			session.setAttribute("sLevel", user.getLevel());
		}
		
		return "";
	}
	
}
