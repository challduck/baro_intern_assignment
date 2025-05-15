package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.ProcessUpdateUserRoleResult;
import com.example.internassignment.domain.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserRoleUpdateResponseDto {
    @Schema(description = "회원 아이디", example = "testUserName")
    private String username;
    @Schema(description = "회원 닉네임", example = "testNickname")
    private String nickname;
    @Schema(description = "회원 권한", examples = {"ADMIN"})
    private List<Role> roles;

    public static UserRoleUpdateResponseDto from(ProcessUpdateUserRoleResult result) {
        return UserRoleUpdateResponseDto.builder()
                .nickname(result.getNickname())
                .username(result.getUsername())
                .roles(result.getRoles())
                .build();
    }
}
