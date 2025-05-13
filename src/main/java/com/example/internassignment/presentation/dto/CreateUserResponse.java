package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateUserResponse {
    private String username;
    private String nickname;
    private List<RoleDto> roles;

    @Builder
    public CreateUserResponse(String username, String nickname, List<Role> roles) {
        this.username = username;
        this.nickname = nickname;
        this.roles = roles.stream()
                .map(RoleDto::new).toList();
    }

    public static CreateUserResponse from(CreateUserInfo createUserInfo) {
        return CreateUserResponse.builder()
                .username(createUserInfo.getUsername())
                .nickname(createUserInfo.getNickname())
                .roles(createUserInfo.getRoles())
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class RoleDto {
        private Role role;
    }
}
