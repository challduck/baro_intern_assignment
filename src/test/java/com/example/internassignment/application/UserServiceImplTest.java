package com.example.internassignment.application;

import com.example.internassignment.application.dto.CreateUserCommand;
import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.application.dto.ProcessUserCommand;
import com.example.internassignment.application.dto.ProcessUserResult;
import com.example.internassignment.common.exception.InvalidCredentialsException;
import com.example.internassignment.common.exception.UsernameAlreadyException;
import com.example.internassignment.domain.UserRepository;
import com.example.internassignment.domain.entity.Role;
import com.example.internassignment.domain.entity.User;
import com.example.internassignment.infrastructure.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private UserServiceImpl userServiceImpl;


    private final String username = "testUser";
    private final String rawPassword = "testPassword";
    private final String encodedPassword = "encodedPassword";
    private final String nickname = "testUserNickname";
    private final String token = "jwt.token.value";

    @Test
    @DisplayName("회원가입: 이미 존재하는 회원 아이디로 예외가 발생한다.")
    void test1 () {
        // given
        CreateUserCommand createUserCommand = CreateUserCommand.builder()
                .username(username)
                .password(rawPassword)
                .nickname(nickname)
                .build();

        given(userRepository.isExistUsername(username)).willReturn(true);

        // when & then
        assertThatThrownBy(()-> userServiceImpl.createUser(createUserCommand))
                .isInstanceOf(UsernameAlreadyException.class)
                .hasMessage("이미 존재하는 아이디입니다.");

        // verify
        then(userRepository).should(times(0)).save(any());
    }

    @Test
    @DisplayName("회원가입: 새로운 회원 아이디로 회원가입 테스트에 성공한다.")
    void test2 () {
        // given
        Role role = Role.USER;

        CreateUserCommand createUserCommand = CreateUserCommand.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .build();

        given(userRepository.isExistUsername(username)).willReturn(false);
        given(passwordEncoder.encode(encodedPassword)).willReturn(encodedPassword);
        given(userRepository.save(any(User.class))).willAnswer(invocation -> {
            return invocation.<User>getArgument(0);
        });

        // when
        CreateUserInfo createUserInfo = userServiceImpl.createUser(createUserCommand);

        // then
        assertThat(createUserInfo.getUsername()).isEqualTo(username);
        assertThat(createUserInfo.getNickname()).isEqualTo(nickname);
        assertThat(createUserInfo.getRoles()).contains(role);

        // verify
        then(userRepository).should(times(1)).save(any(User.class));
        then(passwordEncoder).should(times(1)).encode(encodedPassword);
    }

    @Test
    @DisplayName("로그인: 로그인에 성공한다.")
    void test3(){
        // given
        ProcessUserCommand command = ProcessUserCommand.builder()
                .username(username)
                .password(rawPassword)
                .build();

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .build();

        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);
        given(tokenProvider.generateToken(eq(user), any(Duration.class))).willReturn(token);

        // when
        ProcessUserResult result = userServiceImpl.signin(command);

        // then
        assertThat(result.getToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("로그인: 올바르지 않은 아이디 혹은 비밀번호를 입력받아 로그인에 실패한다.")
    void test4(){
        // given
        ProcessUserCommand command = ProcessUserCommand.builder()
                .username(username)
                .password(rawPassword)
                .build();

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .build();

        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(false);

        // when & then
        assertThatThrownBy(()-> userServiceImpl.signin(command))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("아이디 혹은 비밀번호가 올바르지 않습니다.");
    }
}