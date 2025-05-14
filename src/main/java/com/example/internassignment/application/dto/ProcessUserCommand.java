package com.example.internassignment.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProcessUserCommand {
    private String username;
    private String password;
}
