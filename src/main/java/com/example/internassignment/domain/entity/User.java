package com.example.internassignment.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "p_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", length = 100, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = Role.USER;
    }

    public void updateRole(){
        this.role = Role.ADMIN;
    }
}
