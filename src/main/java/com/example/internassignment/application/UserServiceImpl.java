package com.example.internassignment.application;

import com.example.internassignment.application.dto.CreateUserCommand;
import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.application.dto.ProcessUserCommand;
import com.example.internassignment.domain.UserRepository;
import com.example.internassignment.domain.entity.User;
import com.example.internassignment.infrastructure.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

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
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
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
    public String signin(ProcessUserCommand command) {
        User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 혹은 비밀번호가 올바르지 않습니다.");
        }

        return jwtTokenProvider.generateToken(user, expiredAt);
    }
}
