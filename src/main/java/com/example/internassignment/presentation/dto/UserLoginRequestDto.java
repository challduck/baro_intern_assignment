package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.ProcessUserCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserLoginRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public ProcessUserCommand toCommand() {
        return ProcessUserCommand.builder()
                .username(username)
                .password(password)
                .build();
    }
}
