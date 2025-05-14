package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.CreateAdminInfo;
import com.example.internassignment.domain.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CreateAdminResponseDto {
    private String username;
    private String nickname;
    private List<Role> roles;

    public static CreateAdminResponseDto from(CreateAdminInfo user) {
        return CreateAdminResponseDto.builder()
                .nickname(user.getNickname())
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }
}
