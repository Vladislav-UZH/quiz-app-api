package com.example.kahoot.controllers;

import com.example.kahoot.models.Follower;
import com.example.kahoot.models.User;
import com.example.kahoot.repositories.UserRepository;
import com.example.kahoot.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository; // For retrieving user by username

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{ownerId}/follow")
    public ResponseEntity<String> followUser(@PathVariable UUID ownerId, Principal principal) {
        UUID followerId = getCurrentUserId(principal);
        try {
            userService.followUser(followerId, ownerId);
            return ResponseEntity.ok("Successfully followed the user.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{ownerId}/unfollow")
    public ResponseEntity<String> unfollowUser(@PathVariable UUID ownerId, Principal principal) {
        UUID followerId = getCurrentUserId(principal);
        try {
            userService.unfollowUser(followerId, ownerId);
            return ResponseEntity.ok("Successfully unfollowed the user.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<Follower>> getFollowers(@PathVariable UUID userId) {
        try {
            List<Follower> followers = userService.getFollowers(userId);
            return ResponseEntity.ok(followers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<Follower>> getFollowing(@PathVariable UUID userId) {
        try {
            List<Follower> following = userService.getFollowing(userId);
            return ResponseEntity.ok(following);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Method to get the current user ID
    private UUID getCurrentUserId(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found."));
    }
}
