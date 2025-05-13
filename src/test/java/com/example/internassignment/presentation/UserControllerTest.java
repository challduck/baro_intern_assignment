package com.example.internassignment.presentation;

import com.example.internassignment.application.UserService;
import com.example.internassignment.application.dto.CreateUserCommand;
import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.config.TestSecurityConfig;
import com.example.internassignment.domain.entity.Role;
import com.example.internassignment.presentation.dto.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 API 성공시 201과 함께 회원정보 응답에 성공한다.")
    void test1 () throws Exception {
        // given
        String username = "testUsername";
        String password = "testPassword1234!";
        String nickname = "testNickname";
        Role role = Role.USER;

        CreateUserRequest request = CreateUserRequest.builder()
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

        CreateUserRequest request = CreateUserRequest.builder()
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
}