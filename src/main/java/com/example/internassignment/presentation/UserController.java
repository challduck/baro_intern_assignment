package com.example.internassignment.presentation;

import com.example.internassignment.application.UserService;
import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.application.dto.ProcessUpdateUserRoleResult;
import com.example.internassignment.presentation.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CreateUserResponseDto> signUp(@Valid @RequestBody CreateUserRequestDto request){
        CreateUserInfo createUserInfo = userService.createUser(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateUserResponseDto.from(createUserInfo));
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<CreateAdminResponseDto> adminSignUp(@Valid @RequestBody CreateAdminRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateAdminResponseDto
                .from(userService.createAdmin(request.toCommand())));
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

    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<UserRoleUpdateResponseDto> roleUpdateAdmin(
            @PathVariable(name = "userId") Long userId
    ){
        ProcessUpdateUserRoleResult result = userService.updateUserRole(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserRoleUpdateResponseDto.from(result));
    }
}
