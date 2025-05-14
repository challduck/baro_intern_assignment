package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.CreateAdminCommand;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAdminRequestDto {
    private String username;
    private String password;
    private String nickname;

    public CreateAdminCommand toCommand() {
        return CreateAdminCommand.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
