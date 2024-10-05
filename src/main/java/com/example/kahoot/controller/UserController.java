package com.example.kahoot.controller;

import com.example.kahoot.model.User;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Getting all users");
        return List.of(new User(), new User());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{id}")
    public User getUser(@PathParam("id") String id) {
        log.info("Getting user with id: {}", id);
        return new User();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/user/{id}")
    public void updateUser(@PathParam("id") String id, User user) {
        log.info("Updating user with id: {}", id);
    }

}
