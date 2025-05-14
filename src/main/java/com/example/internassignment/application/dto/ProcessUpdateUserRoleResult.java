package com.example.internassignment.application.dto;

import com.example.internassignment.domain.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProcessUpdateUserRoleResult {
    private String username;
    private String nickname;
    private List<Role> roles;
}
