// TokenProvider.java
package com.example.kahoot.security.token;

import com.example.kahoot.enums.TokenValidity;
import com.example.kahoot.models.User;
import com.example.kahoot.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Service
public class TokenProvider {
    private final UserRepository userRepository;
    private Key key;

    @Value("${security.jwt.token.secret-key}")
    private String JWT_SECRET;

    public TokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    // Термін дії токенів у мілісекундах
    private final long ACCESS_TOKEN_VALIDITY = TokenValidity.ACCESS_TOKEN_VALIDITY.getValidity();
    private final long REFRESH_TOKEN_VALIDITY = TokenValidity.REFRESH_TOKEN_VALIDITY.getValidity();

    // Генерація Access токена
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("lastLogoutAt", user.getLastLogoutAt() != null ? user.getLastLogoutAt().toEpochMilli() : 0)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    // Генерація Refresh токена
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    public String validateAccessToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            // Отримуємо користувача з бази даних
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Перевірка lastLogoutAt
            Long lastLogoutAtInToken = claims.get("lastLogoutAt", Long.class);
            if (user.getLastLogoutAt() != null && user.getLastLogoutAt().toEpochMilli() > lastLogoutAtInToken) {
                throw new RuntimeException("Token issued before last logout");
            }

            return username;
        } catch (Exception e) {
            throw new RuntimeException("Error while validating access token", e);
        }
    }

    public String validateRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("type", String.class);
            if (!"refresh".equals(tokenType)) {
                throw new RuntimeException("Invalid token type");
            }

            String username = claims.getSubject();

            // Отримуємо користувача з бази даних
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return username;
        } catch (Exception e) {
            throw new RuntimeException("Error while validating refresh token", e);
        }
    }

}
