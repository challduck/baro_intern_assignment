package com.example.internassignment.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {
    @Schema(description = "회원 Token", example = "header.payload.signature")
    private String token;
}
