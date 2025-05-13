package com.example.internassignment.application;

import com.example.internassignment.application.dto.CreateUserCommand;
import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.domain.UserRepository;
import com.example.internassignment.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CreateUserInfo createUser(CreateUserCommand createUserCommand) {
        if (userRepository.isExistUsername(createUserCommand.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User save = userRepository.save(createUserCommand.toEntity());

        return CreateUserInfo.builder()
                .username(save.getUsername())
                .nickname(save.getNickname())
                .role(save.getRole())
                .build();
    }
}
