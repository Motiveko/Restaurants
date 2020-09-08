package com.motiveko.restaurants.interfaces;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.motiveko.restaurants.aop.CheckValidUser;

@Controller
@CheckValidUser
public class ViewController {


	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	@GetMapping("/new_restaurant")
	public ModelAndView newRestaurant() {
		ModelAndView mav = new ModelAndView("new_restaurant");
		return mav;
	}
	
	
	@GetMapping("/login")
	
	public String login(HttpSession session) {
		if(session.getAttribute("sEmail")!=null) return "index";
		return "login";
	}

	@GetMapping("/registerUser")
	public String loginRegisterUser() {
		return "register";
	}
	
}
