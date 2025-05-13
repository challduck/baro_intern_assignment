package com.example.internassignment.application.dto;

import com.example.internassignment.domain.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateUserInfo {
    private String username;
    private String nickname;
    private List<Role> roles;

    @Builder
    public CreateUserInfo(String username, String nickname, Role role){
        this.username = username;
        this.nickname = nickname;
        this.roles = List.of(role);
    }
}
