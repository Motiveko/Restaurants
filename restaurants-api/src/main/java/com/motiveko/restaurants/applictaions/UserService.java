package com.motiveko.restaurants.applictaions;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.motiveko.restaurants.domains.EmailExistedException;
import com.motiveko.restaurants.domains.EmailNotExistedException;
import com.motiveko.restaurants.domains.InvalidPasswordException;
import com.motiveko.restaurants.domains.User;
import com.motiveko.restaurants.domains.UserRepository;

@Service
@Transactional // javax.transaction
public class UserService {

	
	UserRepository userRepository;
	
	PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder; // BCryptPasswordEncoder 로 Bean등록
	}
	
	public User registerUser(String email, String name, String password) {
		
		Optional<User> existed = userRepository.findByEmail(email);
		
		if(existed.isPresent()) throw new EmailExistedException(email);
		
		String encodedPassword = passwordEncoder.encode(password);
		System.out.println(encodedPassword);
		
		User user = User.builder()
						.email(email)
						.name(name)
						.password(encodedPassword)
						.level(1) // 기본은 일반회
						.isDeleted(0)
						.build();
		
		return userRepository.save(user);
	}

	public User updateUser(Long userId, String email, String name, String password) {
		
		User user = userRepository.findById(userId).orElse(null);

		String encodedPassword = passwordEncoder.encode(password);

		user.setEmail(email);
		user.setName(name);
		user.setPassword(encodedPassword);
	
		// Transactional이 자동으로 save를 해주는가??
		
		
		return user;
	}

	public User deactiveUser(Long userId) {

		User user = userRepository.findById(userId).orElse(null);
		
		user.setIsDeleted(1);
		
		return user;
	}

	public User authenticate(String email, String password) throws Throwable {
		
		// 이메일 검증
		User user = userRepository.findByEmail(email)
									.orElseThrow( () -> new EmailNotExistedException());
		//패스워드 검증
		String encodedPassword = passwordEncoder.encode(password);
		if( !encodedPassword.equals(user.getPassword())) {
			throw new InvalidPasswordException();
		}
		
		return user;
	}
	
	

}
