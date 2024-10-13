package com.example.kahoot.controllers.dtos;

import com.example.kahoot.enums.UserRole;

public record SignUpDto(String login, String email, String password, UserRole role) {

}