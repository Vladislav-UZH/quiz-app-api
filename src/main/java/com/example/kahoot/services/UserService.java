package com.example.kahoot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS app_user (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        jdbcTemplate.execute(sql);
        System.out.println("User table created successfully");
    }
}
