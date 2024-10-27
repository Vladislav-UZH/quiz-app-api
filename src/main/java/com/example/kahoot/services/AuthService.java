package com.example.kahoot.services;

import com.example.kahoot.controllers.dtos.JwtDto;
import com.example.kahoot.controllers.dtos.SignInDto;
import com.example.kahoot.controllers.dtos.SignUpDto;
import com.example.kahoot.enums.TokenValidity;
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

    private final long ACCESS_TOKEN_VALIDITY = TokenValidity.ACCESS_TOKEN_VALIDITY.getValidity();
    private final long REFRESH_TOKEN_VALIDITY = TokenValidity.REFRESH_TOKEN_VALIDITY.getValidity();

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

        SecurityContextHolder.getContext().setAuthentication(authUser);

        var user = (User) authUser.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);

        Date accessTokenExpiration = new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY);
        Date refreshTokenExpiration = new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY);

        return new JwtDto(accessToken, accessTokenExpiration, refreshToken, refreshTokenExpiration);
    }


    public JwtDto refreshAccessToken(String refreshToken) {
        String username = tokenProvider.validateRefreshToken(refreshToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = tokenProvider.generateAccessToken(user);
        Date accessTokenExpiration = new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY);

        return new JwtDto(newAccessToken, accessTokenExpiration, null, null);
    }


    public void signOut(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLastLogoutAt(Instant.now());
        userRepository.save(user);
    }


}