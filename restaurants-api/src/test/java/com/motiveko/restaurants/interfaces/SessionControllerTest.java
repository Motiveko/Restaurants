package com.motiveko.restaurants.interfaces;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.motiveko.restaurants.applications.UserService;
import com.motiveko.restaurants.domains.EmailNotExistedException;
import com.motiveko.restaurants.domains.InvalidPasswordException;

@RunWith(SpringRunner.class)
@WebMvcTest(SessionController.class)
public class SessionControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	// 올바른 로그인 시도
	@Test
	public void loginWithValidData() throws Exception {
		
		String email = "rhehdrla@naver.com";
		String password = "1234";
		
		mvc.perform(get("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"email\":\""+email+"\",\"password\":\""+password+"\"}"))
			.andExpect(status().isOk());
		}
	
	// 존재하지 않는 email로 로그인시도
	@Test
	public void loginWithInvalidEmail() throws Throwable {
		
		given(userService.authenticate(any(), any())).willThrow(new EmailNotExistedException("somethig"));
		
		String email = "xxx";
		String password ="1234";
		mvc.perform(get("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"email\":\""+email+"\",\"password\":\""+password+"\"}"))
			.andExpect(status().isBadRequest());
			
		verify(userService).authenticate(eq(email), eq(password));
	}
	
	// 올바르지 않은 비로 로그인시도.
	@Test
	public void loginWithInvalidPassword() throws Throwable {
		
		given(userService.authenticate(any(), any())).willThrow(new InvalidPasswordException());
		
		String email = "rhehdrla@naver.com";
		String password ="xxxx";
		mvc.perform(get("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"email\":\""+email+"\",\"password\":\""+password+"\"}"))
			.andExpect(status().isBadRequest());
			
		verify(userService).authenticate(eq(email), eq(password));	
	}
	
	// 세션검증테스트..?
	@Test
	public void authentication() throws Exception {
		
		mvc.perform(get("/authentication"))
			.andExpect(content().string(containsString("result")));
	}

}
