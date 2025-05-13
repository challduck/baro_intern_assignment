package com.example.internassignment.application.dto;

import com.example.internassignment.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserCommand {
    private String username;
    private String password;
    private String nickname;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
