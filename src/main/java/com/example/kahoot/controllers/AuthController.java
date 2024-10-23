package com.example.kahoot.controllers;

import com.example.kahoot.controllers.dtos.JwtDto;
import com.example.kahoot.controllers.dtos.SignInDto;
import com.example.kahoot.controllers.dtos.SignUpDto;
import com.example.kahoot.controllers.dtos.UserDto;
import com.example.kahoot.models.User;
import com.example.kahoot.security.token.TokenService;
import com.example.kahoot.services.AuthService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService service;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AuthService service, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.tokenService = tokenService;
    }

    private static final Log log = LogFactory.getLog(AuthController.class);

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto data) {
        try {
            User createdUser = service.signUp(data);
            UserDto userDto = new UserDto(createdUser.getUsername(), createdUser.getEmail(), createdUser.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> signIn(@RequestBody SignInDto data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var authUser = authenticationManager.authenticate(usernamePassword);
            var user = (User) authUser.getPrincipal();

            // Генерація access token з часом закінчення дії
            String accessToken = tokenService.generateAccessToken(user);
            Date expirationTime = Date.from(Instant.now().plusSeconds(3600)); // інформація про життя Токена

            // for testing purposes
            return ResponseEntity.ok(new JwtDto(accessToken, expirationTime));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@RequestHeader("Authorization") String token) {
        try {
            // Remove the Bearer prefix if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            // Invalidate the token (for example, by adding it to a blacklist)
            tokenService.invalidateToken(token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestHeader("Authorization") String token) {
        try {
            // Remove the Bearer prefix if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            var newAccessToken = tokenService.refreshAccessToken(token);
            Date expirationTime = Date.from(Instant.now().plusSeconds(3600)); // New token valid for 1 hour

            return ResponseEntity.ok(new JwtDto(newAccessToken, expirationTime));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JwtDto("Invalid username or password.", null));
        }
    }

    // for testing purposes
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(service.getCurrentUser());
    }
}
