package com.example.internassignment.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserCommand {
    private String username;
    private String password;
    private String nickname;
}
