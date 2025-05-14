package com.example.internassignment.presentation;

import com.example.internassignment.application.UserService;
import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.presentation.dto.CreateUserRequest;
import com.example.internassignment.presentation.dto.CreateUserResponse;
import com.example.internassignment.presentation.dto.UserLoginRequestDto;
import com.example.internassignment.presentation.dto.UserLoginResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CreateUserResponse> signUp(@Valid @RequestBody CreateUserRequest request){
        CreateUserInfo createUserInfo = userService.createUser(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateUserResponse.from(createUserInfo));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto request){
        HttpHeaders headers = new HttpHeaders();
        UserLoginResponseDto response = UserLoginResponseDto.builder()
                .token(userService.signin(request.toCommand()).getToken())
                .build();
        headers.set("Authorization", "Bearer " + response.getToken());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(response);
    }
}
