package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.domain.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateUserResponseDto {
    @Schema(description = "회원 아이디", example = "testUserName")
    private String username;
    @Schema(description = "회원 닉네임", example = "testNickname")
    private String nickname;
    @Schema(description = "회원 권한", examples = {"USER"})
    private List<RoleDto> roles;

    @Builder
    public CreateUserResponseDto(String username, String nickname, List<Role> roles) {
        this.username = username;
        this.nickname = nickname;
        this.roles = roles.stream()
                .map(RoleDto::new).toList();
    }

    public static CreateUserResponseDto from(CreateUserInfo createUserInfo) {
        return CreateUserResponseDto.builder()
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
