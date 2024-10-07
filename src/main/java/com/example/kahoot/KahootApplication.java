package com.example.kahoot;

import com.example.kahoot.model.User;
import com.example.kahoot.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KahootApplication {

	public static void main(String[] args) {
		SpringApplication.run(KahootApplication.class, args);
	}

	@Bean
	public User user(UserRepository userRepository) {
		User user = new User();
		userRepository.save(user);
		return user;
	}

}
