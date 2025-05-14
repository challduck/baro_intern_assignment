package com.example.internassignment.application.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateAdminCommand {
    private String username;
    private String password;
    private String nickname;
}
