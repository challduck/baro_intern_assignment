package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.ProcessUserCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserLoginRequestDto {
    @Schema(description = "관리자 회원 아이디", example = "testAdminName")
    @NotBlank
    private String username;
    @Schema(description = "관리자 회원 비밀번호", example = "testAdminPassword1234!")
    @NotBlank
    private String password;

    public ProcessUserCommand toCommand() {
        return ProcessUserCommand.builder()
                .username(username)
                .password(password)
                .build();
    }
}
