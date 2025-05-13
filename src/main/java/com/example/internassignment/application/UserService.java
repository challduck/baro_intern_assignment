package com.example.internassignment.application;

import com.example.internassignment.application.dto.CreateUserCommand;
import com.example.internassignment.application.dto.CreateUserInfo;

public interface UserService {

    CreateUserInfo createUser(CreateUserCommand createUserCommand);

}
