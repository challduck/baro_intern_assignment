package com.example.internassignment.infrastructure.jwt;

import com.example.internassignment.domain.entity.Role;
import com.example.internassignment.domain.entity.User;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", RandomString.make(128));
    }


    @Test
    @DisplayName("JWT에 User Info를 담아 생성할 수 있다.")
    void test1 () {
        // given
        User user = User.builder()
                .username("testUser")
                .build();

        ReflectionTestUtils.setField(user, "userId", 1L);
        ReflectionTestUtils.setField(user, "role", Role.USER);

        Duration expiredAt = Duration.ofMillis(1000 * 60 * 15);

        // when
        String token = tokenProvider.generateToken(user, expiredAt);
        System.out.println(token);
        // then
        assertThat(token).isNotBlank();
        assertThat(tokenProvider.validToken(token)).isTrue();
        assertThat(tokenProvider.extractClaims(token).getSubject()).isEqualTo("testUser");
        assertThat(tokenProvider.extractClaims(token).get("id")).isEqualTo(1);
        assertThat(tokenProvider.extractClaims(token).get("auth")).isEqualTo(List.of(Role.USER.name()));
    }

    @Test
    @DisplayName("유효하지 않은 JWT는 validToken() false를 반환한다.")
    void test2 (){
        // given
        String invalidToken = "is.invalid.token";

        // when
        boolean result = tokenProvider.validToken(invalidToken);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("만료된 JWT는 validToken() false를 반환한다.")
    void test3 (){
        // given
        User user = User.builder()
                .username("testUser")
                .build();

        ReflectionTestUtils.setField(user, "userId", 1L);
        ReflectionTestUtils.setField(user, "role", Role.USER);

        Duration expiredAt = Duration.ofMillis(-1);
        String expiredToken = tokenProvider.generateToken(user, expiredAt);

        // when
        boolean result = tokenProvider.validToken(expiredToken);
        System.out.println(expiredToken);
        // then
        assertThat(result).isFalse();
    }
}