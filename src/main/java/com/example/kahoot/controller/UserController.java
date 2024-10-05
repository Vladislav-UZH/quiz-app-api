package com.example.kahoot.controller;

import com.example.kahoot.model.User;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Getting all users");
        return List.of(new User(), new User());
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathParam("id") String id) {
    }

    @PutMapping("/user/{id}")
    public void updateUser(@PathParam("id") String id, User user) {
    }

}
