package com.example.internassignment.presentation;

import com.example.internassignment.application.UserService;
import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.application.dto.ProcessUpdateUserRoleResult;
import com.example.internassignment.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "사용자 회원 가입", description = "USER 회원 가입 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "USER 회원가입 성공",
                    content = @Content(schema = @Schema(implementation = CreateUserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "InvalidRequest",
                                    summary = "필드 유효성 검사 실패",
                                    value = """
                            {
                              "code": "INVALID_REQUEST",
                              "message": "잘못된 요청입니다."
                            }
                            """
                            ))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 회원 아이디입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "UserExists",
                                    summary = "중복된 사용자",
                                    value = """
                            {
                              "code": "USER_ALREADY_EXISTS",
                              "message": "이미 가입된 사용자입니다."
                            }
                            """
                            )))
    })
    @PostMapping("/signup")
    public ResponseEntity<CreateUserResponseDto> signUp(@Valid @RequestBody CreateUserRequestDto request){
        CreateUserInfo createUserInfo = userService.createUser(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateUserResponseDto.from(createUserInfo));
    }

    @Operation(summary = "관리자 회원 가입", description = "ADMIN 회원 가입 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "ADMIN 회원가입 성공",
                    content = @Content(schema = @Schema(implementation = CreateAdminResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "필드값이 유효하지 않음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(
                            name = "InvalidRequest",
                            summary = "입력값 유효성 검증 실패",
                            value = """
                        {
                          "code": "INVALID_REQUEST",
                          "message": "잘못된 요청입니다."
                        }
                    """
                    ))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 회원 아이디",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(
                            name = "UserExists",
                            summary = "중복된 사용자",
                            value = """
                        {
                          "code": "USER_ALREADY_EXISTS",
                          "message": "이미 가입된 사용자입니다."
                        }
                    """
                    )))
    })
    @PostMapping("/admin/signup")
    public ResponseEntity<CreateAdminResponseDto> adminSignUp(@Valid @RequestBody CreateAdminRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateAdminResponseDto
                .from(userService.createAdmin(request.toCommand())));
    }

    @Operation(summary = "사용자 로그인", description = "USER 로그인 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = UserLoginResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 올바르지 않습니다.",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(
                            name = "InvalidCredentials",
                            summary = "로그인 실패",
                            value = """
                        {
                          "code": "INVALID_CREDENTIALS",
                          "message": "아이디 또는 비밀번호가 올바르지 않습니다."
                        }
                    """
                    )))
    })
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto request){
        HttpHeaders headers = new HttpHeaders();
        UserLoginResponseDto response = UserLoginResponseDto.builder()
                .token(userService.signin(request.toCommand()).getToken())
                .build();
        headers.set("Authorization", "Bearer " + response.getToken());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(response);
    }

    @Operation(summary = "사용자 권한 변경", description = "USER 권한 변경 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "USER 권한 변경 성공",
                    content = @Content(schema = @Schema(implementation = UserRoleUpdateResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(
                            name = "AccessDenied",
                            summary = "관리자 권한 부족",
                            value = """
                        {
                          "code": "ACCESS_DENIED",
                          "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
                        }
                    """
                    ))),
            @ApiResponse(responseCode = "404", description = "사용자 없음",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(
                            name = "UserNotFound",
                            summary = "존재하지 않는 사용자",
                            value = """
                        {
                          "code": "USER_NOT_FOUND_EXCEPTION",
                          "message": "사용자를 찾을 수 없습니다."
                        }
                    """
                    )))
    })
    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<UserRoleUpdateResponseDto> roleUpdateAdmin(
            @Parameter(description = "권한을 변경할 User PK (userId)")
            @PathVariable(name = "userId") Long userId
    ){
        ProcessUpdateUserRoleResult result = userService.updateUserRole(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(UserRoleUpdateResponseDto.from(result));
    }
}
