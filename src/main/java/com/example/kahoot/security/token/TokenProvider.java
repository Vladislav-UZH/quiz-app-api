package com.example.kahoot.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.kahoot.models.User;
import com.example.kahoot.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String JWT_SECRET;

    private final UserService userService;

    public TokenProvider(UserService userService) {
        this.userService = userService;
    }

    public String generateAccessToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("username", user.getUsername())
                    .withExpiresAt(genAccessExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generating access token", exception);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("username", user.getUsername())
                    .withExpiresAt(genRefreshExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generating refresh token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
//            if (JWT.require(algorithm).build().verify(token).getExpiresAt().isBefore(Instant.now())) {
//                throw new JWTVerificationException("Token expired");
//            }
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating token", exception);
        }
    }

    public String validateRefreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating refresh token", exception);
        }
    }

    public String refreshToken(String refreshToken) {
        String username = validateRefreshToken(refreshToken);
        User user = userService.findByUsername(username);
        return generateAccessToken(user);
    }

    private Instant genAccessExpirationDate() {
        return LocalDateTime.now().plusSeconds(10).toInstant(ZoneOffset.of("+02:00"));
    }

    private Instant genRefreshExpirationDate() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("+02:00"));
    }
}