package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.CreateUserCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@AllArgsConstructor
public class CreateUserRequestDto {
    @Schema(description = "회원 아이디", example = "testUserName")
    @NotNull
    @Length(min = 6, max = 255)
    private String username;
    @Schema(description = "회원 비밀번호", example = "testPassword1234!")
    @NotNull
    @Length(min = 11, max = 255)
    private String password;
    @Schema(description = "회원 닉네임", example = "testNickname")
    @NotNull
    @Length(min = 4, max = 100)
    private String nickname;

    public CreateUserCommand toCommand() {
        return CreateUserCommand.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
