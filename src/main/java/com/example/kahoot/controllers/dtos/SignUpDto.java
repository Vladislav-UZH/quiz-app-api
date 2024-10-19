package com.example.kahoot.controllers.dtos;

import com.example.kahoot.enums.UserRole;

public record SignUpDto(String username, String email, String password, UserRole role) {

}