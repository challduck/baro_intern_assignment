package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.CreateAdminCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
public class CreateAdminRequestDto {
    @Schema(description = "관리자 회원 아이디", example = "testAdminName")
    @NotBlank
    @Length(min = 6, max = 255)
    private String username;
    @Schema(description = "관리자 회원 비밀번호", example = "testAdminPassword1234!")
    @NotBlank
    @Length(min = 11, max = 255)
    private String password;
    @Schema(description = "관리자 회원 닉네임", example = "testAdminNickname")
    @NotBlank
    @Length(min = 4, max = 100)
    private String nickname;

    public CreateAdminCommand toCommand() {
        return CreateAdminCommand.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
