package com.example.kahoot.services;

import com.example.kahoot.controllers.dtos.SignInDto;
import com.example.kahoot.controllers.dtos.SignUpDto;
import com.example.kahoot.models.User;
import com.example.kahoot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signUp(SignUpDto data) {
        if (userRepository.findByUsername(data.username()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }
        User user = new User();
        user.setUsername(data.username());
        user.setEmail(data.email());
        user.setPassword(passwordEncoder.encode(data.password()));
        user.setRole(data.role());
        return userRepository.save(user);
    }

//    public User signIn(SignInDto data) {
//        User user = userRepository.findByUsername(data.username())
//                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
//        if (!passwordEncoder.matches(data.password(), user.getPassword())) {
//            throw new RuntimeException("Invalid username or password");
//        }
//        return user;
//    }

    public User getCurrentUser() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new UsernameNotFoundException("No user currently authenticated");
    }
}
