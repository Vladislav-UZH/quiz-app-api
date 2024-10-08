package com.example.kahoot.security.auth;

import com.example.kahoot.controller.dto.LoginRequest;
import com.example.kahoot.controller.dto.RegisterRequest;
import com.example.kahoot.model.User;
import com.example.kahoot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // Authentication successful
                // Implement logic for successful login, e.g., generating a token
            } else {
                // Handle invalid password
                throw new RuntimeException("Invalid password");
            }
        } else {
            // Handle user not found
            throw new RuntimeException("User not found");
        }
    }

    public void register(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("USER"); // Default role
        userRepository.save(user);
    }

    public void logout() {
        // Implement logout logic, e.g., invalidating a token
    }

    public void getCurrentUser() {
        // Implement logic to get the current user, e.g., from the security context
    }

    public void refreshToken() {
        // Implement logic to refresh a token
    }
}