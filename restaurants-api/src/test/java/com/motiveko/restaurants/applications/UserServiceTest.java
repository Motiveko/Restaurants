package com.motiveko.restaurants.applications;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.motiveko.restaurants.applications.UserService;
import com.motiveko.restaurants.domains.EmailExistedException;
import com.motiveko.restaurants.domains.EmailNotExistedException;
import com.motiveko.restaurants.domains.InvalidPasswordException;
import com.motiveko.restaurants.domains.User;
import com.motiveko.restaurants.domains.UserRepository;

public class UserServiceTest {

	
	UserService userService;
	
	@Mock
	UserRepository userRepository;
	@Mock
	PasswordEncoder passwordEncoder;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		userService = new UserService(userRepository,passwordEncoder);
		
	}
	
	@Test
	public void ValidRegister() {
		
		String email = "admin@restaurant.com";
		String name = "motiveko";
		String password = "1234";
		
		User user = User.builder()
							.email(email)
							.name(name)
							.password(password)
							.build();
		
		given(userRepository.findByEmail(email)).willReturn(Optional.empty());
		
		User createdUser = userService.registerUser(email, name, password);
		
		verify(userRepository).findByEmail(email);
		verify(userRepository).save(any());
	}	
	
	@Test(expected = EmailExistedException.class)
	public void InvalidRegister() {
		
		String email = "admin@restaurant.com";
		String name = "motiveko";
		String password = "1234";
		
		User mockUser = User.builder()
							.email(email)
							.name(name)
							.password(password)
							.build();
		
		given(userRepository.findByEmail(email)).willReturn(Optional.of(mockUser));
		
		User user = userService.registerUser(email, name, password);
		
		verify(userRepository).findByEmail(email);		
	}
	
	@Test 
	public void updateUser() {
		Long id = 1L;
		String email = "admin@restaurant.com";
		String name = "motiveko";
		String password = "1234";
		
		User mockUser = User.builder()
							.id(id)
							.email(email)
							.name(name)
							.password(password)
							.build();
		
		given(userRepository.findById(id)).willReturn(Optional.of(mockUser));

		User user = userService.updateUser(id, email, name, password);
		
		assertThat(user.getName(), is(name));
	}
	
	@Test
	public void deactiveUser() {
		
		Long id = 1L;
		
		User mockUser = User.builder()
							.id(id)
							.isDeleted(1)
							.build();
		
		given(userRepository.findById(id)).willReturn(Optional.of(mockUser));
		
		User user = userService.deactiveUser(id);
		
		assertThat(user.isActive(),is(true));
	}

	// 잘못된 이메일로 로그인시도
	@Test(expected = EmailNotExistedException.class)
	public void authenticateWithInvalidEmail() throws Throwable {
		
		given(userRepository.findByEmail(any())).willReturn(Optional.empty());

		User user = userService.authenticate("rhehdrla@naver.com", "1234");
	}
	
	// 잘못된 password로 로그인시도
	@Test(expected = InvalidPasswordException.class)
	public void authenticateWithInvalidPassword() throws Throwable {
		
		String email = "rhehdrla@naver.com";
		String password = "1234";
		String encodedPassword = passwordEncoder.encode(password);
		
		User mockUser = User.builder()
							.email(email)
							.password(encodedPassword)
							.build();
		given(userRepository.findByEmail(email)).willReturn(Optional.of(mockUser));
		given(passwordEncoder.encode(any())).willReturn("1234");
		
		User user = userService.authenticate(email, password);
		
		assertThat(user.getPassword(),is(encodedPassword));
		
	}
	
	
	
}
