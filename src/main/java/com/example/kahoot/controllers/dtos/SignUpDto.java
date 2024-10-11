package com.example.kahoot.controllers.dtos;

import com.example.kahoot.enums.UserRole;

public record SignUpDto(String login, String password, UserRole role) {

}