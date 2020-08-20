package com.motiveko.restaurants.interfaces;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.motiveko.restaurants.applictaions.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	MockMvc mvc;
	
	@MockBean
	UserService userService;
	
	@Test
	public void register() throws Exception {
		
		mvc.perform(post("/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"email\":\"admin@restaurant.com\",\"name\":\"motiveko\",\"password\":\"1234\"}\n"))
			.andExpect(status().isCreated());
	}
	

}
