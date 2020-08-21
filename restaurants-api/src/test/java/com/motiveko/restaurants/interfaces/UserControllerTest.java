package com.motiveko.restaurants.interfaces;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

import com.motiveko.restaurants.applications.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	MockMvc mvc;
	
	@MockBean
	UserService userService;
	
	@Test
	public void register() throws Exception {
		String email = "admin@restaurant.com";
		String name = "motiveko";
		String password = "1234";
		
		mvc.perform(post("/registerUser")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"email\":\"" + email 
							+ "\",\"name\":\"" + name
							+"\",\"password\":\""+ password+"\"}\n"))
			.andExpect(status().isCreated());
	
		verify(userService).registerUser(email,name,password);
	}
	

	@Test
	public void updateUser() throws Exception {
		Long userId = 1L;
		String email = "admin@restaurant.com";
		String name = "motiveko";
		String password = "1234";
		
		mvc.perform(patch("/updateUser/"+userId)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"email\":\"" + email 
							+ "\",\"name\":\"" + name
							+"\",\"password\":\""+ password+"\"}\n"))
			.andExpect(status().isCreated());
		
		verify(userService).updateUser(userId,email,name,password);
	}
	
	@Test
	public void deactiveUser() throws Exception {
		Long userId = 1L;
		
		mvc.perform(delete("/deleteUser/"+userId))
			.andExpect(status().isOk());
		
		verify(userService).deactiveUser(userId);
		
	}
	

	
}
