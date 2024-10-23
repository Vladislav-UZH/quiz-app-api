package com.example.kahoot.services;

import com.example.kahoot.models.Follower;
import com.example.kahoot.models.User;
import com.example.kahoot.repositories.FollowerRepository;
import com.example.kahoot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    public UserService(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void followUser(UUID followerId, UUID ownerId) {
        if (followerId.equals(ownerId)) {
            throw new IllegalArgumentException("A user cannot follow themselves.");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found."));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found."));

        boolean alreadyFollowing = followerRepository.existsByFollowerAndOwner(follower, owner);
        if (alreadyFollowing) {
            throw new IllegalStateException("You are already following this user.");
        }

        Follower follow = new Follower();
        follow.setFollower(follower);
        follow.setOwner(owner);
        followerRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(UUID followerId, UUID ownerId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found."));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found."));

        Follower follow = followerRepository.findByFollowerAndOwner(follower, owner)
                .orElseThrow(() -> new IllegalStateException("You are not following this user."));

        followerRepository.delete(follow);
    }

    @Transactional
    public List<Follower> getFollowers(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        return followerRepository.findByOwner(user);
    }

    @Transactional
    public List<Follower> getFollowing(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        return followerRepository.findByFollower(user);
    }
}
