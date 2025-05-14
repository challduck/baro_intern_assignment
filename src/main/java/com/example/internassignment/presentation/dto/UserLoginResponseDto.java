package com.example.internassignment.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {
    private String token;
}
