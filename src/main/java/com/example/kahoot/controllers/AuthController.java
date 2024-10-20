package com.example.kahoot.controllers;

//import jakarta.validation.Valid;
import com.example.kahoot.controllers.dtos.JwtDto;
import com.example.kahoot.controllers.dtos.SignInDto;
import com.example.kahoot.controllers.dtos.SignUpDto;
import com.example.kahoot.models.User;
import com.example.kahoot.security.token.TokenProvider;
import com.example.kahoot.services.AuthService;
import jakarta.websocket.OnError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService service;
    @Autowired
    private TokenProvider tokenService;

    private static final Log log = LogFactory.getLog(AuthController.class);

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto data) {
        try {
            service.signUp(data);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> signIn(@RequestBody SignInDto data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var authUser = authenticationManager.authenticate(usernamePassword);
            var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());
            return ResponseEntity.ok(new JwtDto(accessToken));
//            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok().build();
    }
}