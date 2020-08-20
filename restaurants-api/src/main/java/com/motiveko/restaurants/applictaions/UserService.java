package com.motiveko.restaurants.applictaions;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motiveko.restaurants.domains.EmailExistedException;
import com.motiveko.restaurants.domains.User;
import com.motiveko.restaurants.domains.UserRepository;

@Service
public class UserService {

	
	UserRepository userRepository;
	
//	PasswordEncoder passwordEncoder;
	public UserService(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	public User registerUser(String email, String name, String password) {
		
		Optional<User> existed = userRepository.findByEmail(email);
		
		if(existed.isPresent()) throw new EmailExistedException(email);
		
		User user = User.builder()
						.email(email)
						.name(name)
						.password(password)
						.level(1) // 기본은 일반회
						.isDeleted(0)
						.build();
		
		return userRepository.save(user);
	}

}
