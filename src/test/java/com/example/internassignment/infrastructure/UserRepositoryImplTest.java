package com.example.internassignment.infrastructure;

import com.example.internassignment.domain.entity.Role;
import com.example.internassignment.domain.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class UserRepositoryImplTest {
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("userRepositoryImpl.class findByUserId 메서드 성공한다.")
    void test1 (){
        // given
        String username = "testUser";
        String password = "testPassword1234";
        Role role = Role.USER;
        String nickname = "testUserNickname";

        User user = User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();

        User save = userJpaRepository.save(user);

        // when
        Optional<User> optionalUser = userRepositoryImpl.findByUserId(save.getUserId());

        // then
        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get().getUsername()).isEqualTo(username);
        assertThat(optionalUser.get().getPassword()).isEqualTo(password);
        assertThat(optionalUser.get().getNickname()).isEqualTo(nickname);
        assertThat(optionalUser.get().getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("userRepositoryImpl.class findByUserId 메서드 실패한다.")
    void test2 (){
        // given & when
        Optional<User> optionalUser = userRepositoryImpl.findByUserId(1L);

        // then
        assertThat(optionalUser).isNotPresent();
    }

    @Test
    @DisplayName("userRepositoryImpl.class findByUsername 메서드 성공한다.")
    void test3 (){
        // given
        String username = "testUser";
        String password = "testPassword1234";
        Role role = Role.USER;
        String nickname = "testUserNickname";

        User user = User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();

        User save = userJpaRepository.save(user);

        // when
        Optional<User> optionalUser = userRepositoryImpl.findByUsername(save.getUsername());

        // then
        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get().getUsername()).isEqualTo(username);
        assertThat(optionalUser.get().getPassword()).isEqualTo(password);
        assertThat(optionalUser.get().getNickname()).isEqualTo(nickname);
        assertThat(optionalUser.get().getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("userRepositoryImpl.class findByUsername 메서드 실패한다.")
    void test4 (){
        // given & when
        Optional<User> optionalUser = userRepositoryImpl.findByUsername("testUsername");

        // then
        assertThat(optionalUser).isNotPresent();
    }

    @Test
    @DisplayName("userRepositoryImpl.class isExistUsername 메서드 true를 반환한다")
    void test5 (){
        // given
        String username = "testUser";
        String password = "testPassword1234";
        String nickname = "testUserNickname";

        User user = User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();

        User save = userJpaRepository.save(user);

        // when
        boolean isExistUsername = userRepositoryImpl.isExistUsername(save.getUsername());

        // then
        assertThat(isExistUsername).isTrue();
    }

    @Test
    @DisplayName("userRepositoryImpl.class isExistUsername 메서드 false를 반환한다")
    void test6 (){
        // given & when
        boolean isExistUsername = userRepositoryImpl.isExistUsername("testUsername");

        // then
        assertThat(isExistUsername).isFalse();
    }
}