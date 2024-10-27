package com.example.kahoot.controllers.dtos;

import java.util.Date;

public record JwtDto(
        String accessToken,
        Date accessTokenExpiration,
        String refreshToken,
        Date refreshTokenExpiration
) {}
