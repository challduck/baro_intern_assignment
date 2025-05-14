package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.ProcessUpdateUserRoleResult;
import com.example.internassignment.domain.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserRoleUpdateResponseDto {
    private String username;
    private String nickname;
    private List<Role> roles;

    public static UserRoleUpdateResponseDto from(ProcessUpdateUserRoleResult result) {
        return UserRoleUpdateResponseDto.builder()
                .nickname(result.getNickname())
                .username(result.getUsername())
                .roles(result.getRoles())
                .build();
    }
}
