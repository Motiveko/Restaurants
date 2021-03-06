package com.motiveko.restaurants.interfaces;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.motiveko.restaurants.applications.UserService;
import com.motiveko.restaurants.domains.SessionRequestDto;
import com.motiveko.restaurants.domains.User;

//@CrossOrigin
@RestController
public class SessionController {

	
	@Autowired
	UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody SessionRequestDto resource
						,HttpSession session
						) throws Throwable {
		String email = resource.getEmail();
		String password = resource.getPassword();
		User user = userService.authenticate(email,password);
		
		// 문제없으면 session에 사용자정보 입력쓰
		if(user!=null) {
			session.setAttribute("sEmail", user.getEmail());
			session.setAttribute("sName", user.getName());
			session.setAttribute("sLevel", user.getLevel());
			System.out.println("session에 할당완료");
		}

		// ajax 에서 success로 걸리려면 .ok여야한다. .created는 error로 가버린다
		return ResponseEntity.created(new URI("/index")).body("{\"result\":\"success\"}");
	}
	
	@GetMapping("/authentication")
	public String authentication(HttpSession session) throws JsonProcessingException {
				
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> modelMap = new HashMap<>();
		System.out.println(session.getAttribute("sEmail"));
		if(session.getAttribute("sEmail")==null) {
			modelMap.put("result", "FAILED");
			
		} else {
			modelMap.put("result", "SUCCESS");
			modelMap.put("email", session.getAttribute("sEmail"));
		}
		
		return mapper.writeValueAsString(modelMap);
	}
	
	
}
