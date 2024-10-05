package com.example.kahoot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login() {
        log.info("Logging in");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register() {
        log.info("Registering");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public void logout() {
        log.info("Logging out");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/current-user")
    public void currentUser() {
        log.info("Getting current user");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh-token")
    public void refreshToken() {
        log.info("Refreshing token");
    }

}
