package com.example.kahoot.controllers;

import com.example.kahoot.controllers.dtos.JwtDto;
import com.example.kahoot.controllers.dtos.SignInDto;
import com.example.kahoot.controllers.dtos.SignUpDto;
import com.example.kahoot.controllers.dtos.UserDto;
import com.example.kahoot.models.User;
import com.example.kahoot.security.token.TokenProvider;
import com.example.kahoot.services.AuthService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService service;
    private final TokenProvider tokenProvider;
    private final AuthService authService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AuthService service, TokenProvider tokenProvider, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.tokenProvider = tokenProvider;
        this.authService = authService;
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
            JwtDto jwt = service.signIn(data);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@RequestHeader("Authorization") String accessToken) {
        try {
            if (accessToken.startsWith("Bearer ")) {
                accessToken = accessToken.substring(7);
            }
            String username = tokenProvider.validateAccessToken(accessToken);
            service.signOut(username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestHeader("Authorization") String refreshToken) {
        try {
            if (refreshToken.startsWith("Bearer ")) {
                refreshToken = refreshToken.substring(7);
            }
            JwtDto jwt = service.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JwtDto("Invalid refresh token.", null, null, null));
        }
    }



    // method doesn't work
    // for testing purposes
//    @GetMapping("/current")
//    public ResponseEntity<User> getCurrentUser() {
//        try {
//            return ResponseEntity.ok(service.getCurrentUser());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }
}
