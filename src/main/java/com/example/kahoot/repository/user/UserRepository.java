package com.example.kahoot.repository.user;

import com.example.kahoot.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(UUID id);

    void save(User user);

    void update(UUID id, User user);

    void deleteById(UUID id);
}