package com.example.internassignment.common.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundException(){
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("USER_NOT_FOUND_EXCEPTION")
                        .message("사용자를 찾을 수 없습니다.")
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UsernameAlreadyException.class)
    public ResponseEntity<ErrorResponse> usernameAlreadyException(){
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("USER_ALREADY_EXISTS")
                        .message("이미 가입된 사용자입니다.")
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> invalidCredentialsException(){
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("INVALID_CREDENTIALS")
                        .message("아이디 또는 비밀번호가 올바르지 않습니다.")
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(){
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("ACCESS_DENIED")
                        .message("접근 권한이 없습니다.")
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(){
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("INVALID_REQUEST")
                        .message("잘못된 요청입니다.")
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
