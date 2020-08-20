package com.motiveko.restaurants.interfaces;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.motiveko.restaurants.applictaions.UserService;
import com.motiveko.restaurants.domains.EmailNotExistedException;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

	
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	// 존재하지 않는 email로 로그인시도
	@Test(expected=EmailNotExistedException.class)
	public void loginWithInvalidEmail() throws Throwable {
		
		given(userService.authenticate(any(), any())).willThrow(new EmailNotExistedException());
		
		String email = "xxx";
		String password ="1234";
		mvc.perform(post("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"email\":\""+email+"\",\"password\":\""+password+"\"}"))
			.andExpect(status().isBadRequest());
			
		verify(userService).authenticate(eq(email), eq(password));
	}

}
