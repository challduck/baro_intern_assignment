package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.CreateAdminInfo;
import com.example.internassignment.domain.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CreateAdminResponseDto {
    @Schema(description = "관리자 회원 아이디", example = "testAdminName")
    private String username;
    @Schema(description = "관리자 회원 닉네임", example = "testAdminNickname")
    private String nickname;
    @Schema(description = "관리자 회원 권한", examples = {"ADMIN"})
    private List<Role> roles;

    public static CreateAdminResponseDto from(CreateAdminInfo user) {
        return CreateAdminResponseDto.builder()
                .nickname(user.getNickname())
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }
}
