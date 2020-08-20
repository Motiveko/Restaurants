package com.motiveko.restaurants.applictaions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.motiveko.restaurants.domains.EmailExistedException;
import com.motiveko.restaurants.domains.User;
import com.motiveko.restaurants.domains.UserRepository;

public class UserServiceTest {

	
	UserService userService;
	
	@Mock
	UserRepository userRepository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		userService = new UserService(userRepository);
		
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

}
