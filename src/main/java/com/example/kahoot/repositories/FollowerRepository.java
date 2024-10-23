package com.example.kahoot.repositories;

import com.example.kahoot.models.Follower;
import com.example.kahoot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowerRepository extends JpaRepository<Follower, UUID> {
    List<Follower> findByOwner(User owner);
    List<Follower> findByFollower(User follower);
    boolean existsByFollowerAndOwner(User follower, User owner);
    Optional<Follower> findByFollowerAndOwner(User follower, User owner);
}
