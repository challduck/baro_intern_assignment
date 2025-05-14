package com.example.internassignment.application;

import com.example.internassignment.application.dto.CreateUserCommand;
import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.application.dto.ProcessUserCommand;
import com.example.internassignment.application.dto.ProcessUserResult;

public interface UserService {

    CreateUserInfo createUser(CreateUserCommand createUserCommand);

    ProcessUserResult signin(ProcessUserCommand command);

}
