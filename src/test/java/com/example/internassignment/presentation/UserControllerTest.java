package com.example.internassignment.presentation;

import com.example.internassignment.application.UserService;
import com.example.internassignment.application.dto.*;
import com.example.internassignment.common.exception.InvalidCredentialsException;
import com.example.internassignment.domain.entity.Role;
import com.example.internassignment.infrastructure.config.SecurityConfig;
import com.example.internassignment.infrastructure.jwt.JwtTokenProvider;
import com.example.internassignment.infrastructure.security.CustomAccessDeniedHandler;
import com.example.internassignment.infrastructure.security.UserDetailsServiceImpl;
import com.example.internassignment.presentation.dto.CreateAdminRequestDto;
import com.example.internassignment.presentation.dto.CreateUserRequestDto;
import com.example.internassignment.presentation.dto.UserLoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfig.class, CustomAccessDeniedHandler.class})
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("회원가입 API 성공시 201과 함께 회원정보 응답에 성공한다.")
    void test1 () throws Exception {
        // given
        String username = "testUsername";
        String password = "testPassword1234!";
        String nickname = "testNickname";
        Role role = Role.USER;

        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();

        CreateUserInfo createUserInfo = CreateUserInfo.builder()
                .username(username)
                .nickname(nickname)
                .role(role)
                .build();

        given(userService.createUser(any(CreateUserCommand.class))).willReturn(createUserInfo);

        // when & then
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.roles[0].role").value(role.name()));
    }

    @Test
    @DisplayName("회원가입 API 요청시 필드가 올바르지 않으면 400 Bad Request를 응답한다.")
    void test2 () throws Exception {
        // given
        String username = "a";
        String password = "testPassword1234!";
        String nickname = "testNickname";

        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();

        // when & then
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인: 필드 누락 등 유효성 실패 시 400 Bad Request를 응답한다.")
    void test3 () throws Exception {
        // given
        String username = "testUsername";

        UserLoginRequestDto dto = UserLoginRequestDto.builder()
                .username(username)
                .build();

        // when & then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인: 로그인 요청시 정보가 올바르지 않으면 401 Unauthorized를 응답한다.")
    void test4 () throws Exception {
        // given
        String username = "testUsername";
        String password = "testPassword1234!";

        UserLoginRequestDto dto = UserLoginRequestDto.builder()
                .username(username)
                .password(password)
                .build();

        given(userService.signin(any())).willThrow(new InvalidCredentialsException("아이디 혹은 비밀번호가 올바르지 않습니다."));

        // when & then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인: 로그인 요청시 올바른 요청이라면 200과 함께 token을 return 한다.")
    void test5 () throws Exception {
        // given
        String username = "testUsername";
        String password = "testPassword1234!";
        String token = "jwt.token.value";

        UserLoginRequestDto dto = UserLoginRequestDto.builder()
                .username(username)
                .password(password)
                .build();

        ProcessUserResult result = ProcessUserResult.builder()
                .token(token)
                .build();

        given(userService.signin(any())).willReturn(result);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(header().string("Authorization", "Bearer " + token));
    }

    @Test
    @DisplayName("권한 변경 API: ADMIN 권한으로 요청 시 권한 변경에 성공한다.")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void test6 () throws Exception{
        // given
        final Long userId = 1L;
        final String username = "testUser";
        final String nickname = "testNickname";
        final List<Role> roles = List.of(Role.ADMIN);

        ProcessUpdateUserRoleResult result = ProcessUpdateUserRoleResult.builder()
                .username(username)
                .nickname(nickname)
                .roles(roles)
                .build();

        given(userService.updateUserRole(userId)).willReturn(result);

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId).with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.roles[0]").value(roles.get(0).name()));
    }

    @Test
    @DisplayName("권한 변경 API: ADMIN 권한이 아니라면 권한 변경에 실패한다.")
    @WithAnonymousUser()
    void test7 () throws Exception{
        // given
        final Long userId = 1L;
        final String username = "testUser";
        final String nickname = "testNickname";
        final List<Role> roles = List.of(Role.ADMIN);

        ProcessUpdateUserRoleResult result = ProcessUpdateUserRoleResult.builder()
                .username(username)
                .nickname(nickname)
                .roles(roles)
                .build();

        given(userService.updateUserRole(userId)).willReturn(result);

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("권한 변경 API: USER는 권한 부족으로 권한 변경에 실패한다.")
    @WithMockUser(username = "testUser", roles = "USER")
    void test8 () throws Exception{
        // given
        final Long userId = 1L;

        // when & then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId).with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("ACCESS_DINED"))
                .andExpect(jsonPath("$.message").value("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."));
    }

    @Test
    @DisplayName("관리자 회원가입 API 성공시 201과 함께 관리자정보 응답에 성공한다.")
    void test9 () throws Exception {
        // given
        String username = "testAdminName";
        String password = "testPassword1234!";
        String nickname = "testNickname";
        Role role = Role.ADMIN;

        CreateAdminRequestDto request = CreateAdminRequestDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();

        CreateAdminInfo createAdminInfo = CreateAdminInfo.builder()
                .username(username)
                .nickname(nickname)
                .roles(List.of(role))
                .build();

        given(userService.createAdmin(any(CreateAdminCommand.class))).willReturn(createAdminInfo);

        // when & then
        mockMvc.perform(post("/admin/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.roles[0]").value(role.name()));
    }
}