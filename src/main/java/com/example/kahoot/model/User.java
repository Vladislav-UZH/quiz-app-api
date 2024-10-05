package com.example.kahoot.model;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
}
