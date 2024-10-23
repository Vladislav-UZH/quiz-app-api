package com.example.kahoot.security.token;

import com.example.kahoot.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Service
public class TokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String JWT_SECRET;

    private Key key;

    @PostConstruct
    public void init() {
        // Переконайтесь, що секретний ключ достатньо довгий (не менше 32 байти)
        this.key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 година
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Error while validating token", e);
        }
    }

  // maybe not needed
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
  //////////
}
