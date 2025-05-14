package com.example.internassignment.presentation.dto;

import com.example.internassignment.application.dto.CreateUserCommand;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@AllArgsConstructor
public class CreateUserRequestDto {
    @NotNull
    @Length(min = 6, max = 255)
    private String username;
    @NotNull
    @Length(min = 11, max = 255)
    private String password;
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
