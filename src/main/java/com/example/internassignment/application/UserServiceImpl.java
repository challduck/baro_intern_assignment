package com.example.internassignment.application;

import com.example.internassignment.application.dto.*;
import com.example.internassignment.common.exception.InvalidCredentialsException;
import com.example.internassignment.common.exception.UserNotFoundException;
import com.example.internassignment.common.exception.UsernameAlreadyException;
import com.example.internassignment.domain.UserRepository;
import com.example.internassignment.domain.entity.User;
import com.example.internassignment.infrastructure.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Duration expiredAt = Duration.ofMillis(1000 * 60 * 60 * 2);

    @Transactional
    @Override
    public CreateUserInfo createUser(CreateUserCommand createUserCommand) {
        if (userRepository.isExistUsername(createUserCommand.getUsername())) {
            throw new UsernameAlreadyException("이미 존재하는 아이디입니다.");
        }
        User user = User.builder()
                .username(createUserCommand.getUsername())
                .password(passwordEncoder.encode(createUserCommand.getPassword()))
                .nickname(createUserCommand.getNickname())
                .build();

        User save = userRepository.save(user);

        return CreateUserInfo.builder()
                .username(save.getUsername())
                .nickname(save.getNickname())
                .role(save.getRole())
                .build();
    }

    @Override
    public ProcessUserResult signin(ProcessUserCommand command) {
        User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("아이디 혹은 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("아이디 혹은 비밀번호가 올바르지 않습니다.");
        }

        return ProcessUserResult.builder()
                .token(jwtTokenProvider.generateToken(user, expiredAt))
                .build();
    }

    @Transactional
    @Override
    public ProcessUpdateUserRoleResult updateUserRole(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        user.updateRole();

        return ProcessUpdateUserRoleResult.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .roles(List.of(user.getRole()))
                .build();
    }

    @Transactional
    @Override
    public CreateAdminInfo createAdmin(CreateAdminCommand command) {
        if (userRepository.isExistUsername(command.getUsername())) {
            throw new UsernameAlreadyException("이미 존재하는 아이디입니다.");
        }
        User user = User.createAdmin(
                    command.getUsername(),
                    passwordEncoder.encode(command.getPassword()),
                    command.getNickname()
                );

        User save = userRepository.save(user);

        return CreateAdminInfo.builder()
                .username(save.getUsername())
                .nickname(save.getNickname())
                .roles(List.of(save.getRole()))
                .build();
    }
}
