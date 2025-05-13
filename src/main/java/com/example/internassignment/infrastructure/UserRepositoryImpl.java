package com.example.internassignment.infrastructure;

import com.example.internassignment.domain.UserRepository;
import com.example.internassignment.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public boolean isExistUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }
}
