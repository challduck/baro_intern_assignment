package com.example.internassignment.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Test
    @DisplayName("회원 엔티티 생성 테스트 성공한다.")
    void createUserEntitySuccess() {
        // given
        String username = "testUser";
        String password = "testPassword1234";
        Role role = Role.USER;
        String nickname = "testUserNickname";

        // when
        User user = User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();

        // then
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("권한 변경에 성공한다.")
    void updateRoleSuccess(){
        // given
        String username = "testUser";
        String password = "testPassword1234";
        Role role = Role.ADMIN;
        String nickname = "testUserNickname";

        // when
        User user = User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();

        user.updateRole();

        // then
        assertThat(user.getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("관리자 엔티티 생성 테스트 성공한다.")
    void createAdminEntitySuccess() {

        //given
        String username = "testUser";
        String password = "testPassword1234";
        Role role = Role.ADMIN;
        String nickname = "testUserNickname";

        // when
        User admin = User.createAdmin(username, password, nickname);

        // then
        assertThat(admin.getRole()).isEqualTo(role);
    }
}