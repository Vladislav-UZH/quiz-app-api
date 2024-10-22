package com.example.kahoot.controllers.dtos;

public record JwtDto(String accessToken, java.util.Date expirationTime) {
}