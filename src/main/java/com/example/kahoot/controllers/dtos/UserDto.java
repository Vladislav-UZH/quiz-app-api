package com.example.kahoot.controllers.dtos;

import com.example.kahoot.enums.UserRole;

public record UserDto(String username, String email, UserRole role) {
}
