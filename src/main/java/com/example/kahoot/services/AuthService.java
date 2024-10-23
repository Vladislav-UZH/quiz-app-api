package com.example.kahoot.services;

import com.example.kahoot.controllers.dtos.JwtDto;
import com.example.kahoot.controllers.dtos.SignInDto;
import com.example.kahoot.controllers.dtos.SignUpDto;
import com.example.kahoot.models.User;
import com.example.kahoot.repositories.UserRepository;
import com.example.kahoot.security.token.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.Date;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;

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

    public JwtDto signIn(SignInDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var authUser = authenticationManager.authenticate(usernamePassword);

        // Додаємо автентифікованого користувача до SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authUser);

        // Отримуємо користувача з контексту
        var user = (User) authUser.getPrincipal();

        // Генерація access token з часом закінчення дії
        String accessToken = tokenProvider.generateAccessToken(user);
        Date expirationTime = Date.from(Instant.now().plusMillis(3600000)); // життя токена - 1 година

        return new JwtDto(accessToken, expirationTime);
    }

    // method doesn't work
//    public User getCurrentUser() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            return (User) authentication.getPrincipal();
//        }
//        throw new UsernameNotFoundException("No user currently authenticated");
//    }
}
