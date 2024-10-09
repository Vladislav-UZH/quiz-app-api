package com.example.kahoot.service;

import com.example.kahoot.model.User;
import com.example.kahoot.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(UUID id, User user) {
        if (userRepository.findById(id).isPresent()) {
//            user.setId(id);
            userRepository.save(user);
        }
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}