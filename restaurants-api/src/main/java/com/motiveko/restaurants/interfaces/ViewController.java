package com.motiveko.restaurants.interfaces;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.motiveko.restaurants.aop.CheckValidUser;

@Controller
@CheckValidUser
public class ViewController {


	@GetMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping("/login")
	
	public String login(HttpSession session) {
		if(session.getAttribute("sEmail")!=null) return "index";
		return "login";
	}
	@GetMapping("/forgotPassword")
	public String loginForgotPassword() {
		return "forgot_password";
	}
	@GetMapping("/registerUser")
	public String loginRegisterUser() {
		return "register";
	}
	
}
