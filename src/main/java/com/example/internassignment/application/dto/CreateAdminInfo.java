package com.example.internassignment.application.dto;

import com.example.internassignment.domain.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreateAdminInfo {
    private String username;
    private String nickname;
    private List<Role> roles;
}
