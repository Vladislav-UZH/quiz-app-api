package com.example.kahoot.repositories;

//import com.example.kahoot.models.User;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface UserRepository {
//    User save(User user);
//    Optional<User> findById(Long id);
//    User findByLogin(String login);
//    List<User> findAll();
//    User update(User user);
//    void delete(Long id);
//}
import com.example.kahoot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    UserDetails findByLogin(String login);
}