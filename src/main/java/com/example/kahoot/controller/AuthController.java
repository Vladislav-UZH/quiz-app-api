package com.example.kahoot.controller;

import com.example.kahoot.controller.dto.LoginRequest;
import com.example.kahoot.controller.dto.RegisterRequest;
import com.example.kahoot.security.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        log.info("Logging in user with email: {}", loginRequest.getEmail());
        authService.login(loginRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest registerRequest) {
        log.info("Registering user with email: {}", registerRequest.getEmail());
        authService.register(registerRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public void logout() {
        log.info("Logging out");
        authService.logout();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/current-user")
    public void currentUser() {
        log.info("Getting current user");
        authService.getCurrentUser();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh-token")
    public void refreshToken() {
        log.info("Refreshing token");
        authService.refreshToken();
    }
}