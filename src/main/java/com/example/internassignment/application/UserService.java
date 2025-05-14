package com.example.internassignment.application;

import com.example.internassignment.application.dto.CreateUserCommand;
import com.example.internassignment.application.dto.CreateUserInfo;
import com.example.internassignment.application.dto.ProcessUserCommand;

public interface UserService {

    CreateUserInfo createUser(CreateUserCommand createUserCommand);

    String signin(ProcessUserCommand command);

}
