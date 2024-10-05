package com.example.kahoot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public void login() {
    }

    @PostMapping("/register")
    public void register() {
    }

    @PostMapping("/logout")
    public void logout() {
    }

    @PostMapping("/current-user")
    public void currentUser() {
    }

    @PostMapping("/refresh-token")
    public void refreshToken() {

    }

}
