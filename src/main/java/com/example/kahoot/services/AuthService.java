package com.example.kahoot.services;

import com.example.kahoot.controllers.dtos.SignUpDto;
import com.example.kahoot.enums.UserRole;
import com.example.kahoot.models.User;
import com.example.kahoot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // For password hashing

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signUp(SignUpDto data) {
        if (userRepository.findByUsername(data.username()).isPresent()) {
            throw new IllegalArgumentException("A user with this username already exists.");
        }
        if (userRepository.findByEmail(data.email()).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        User user = new User(
                data.username(),
                data.email(),
                passwordEncoder.encode(data.password()),
                data.role()
        );

        userRepository.save(user);
    }
}
