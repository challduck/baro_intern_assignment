package com.example.internassignment.domain;

import com.example.internassignment.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUserId(Long userId);
    Optional<User> findByUsername(String username);
    boolean isExistUsername(String username);
}
