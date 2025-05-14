package com.example.internassignment.application;

import com.example.internassignment.application.dto.*;

public interface UserService {

    CreateUserInfo createUser(CreateUserCommand createUserCommand);

    ProcessUserResult signin(ProcessUserCommand command);

    ProcessUpdateUserRoleResult updateUserRole(Long userId);

    CreateAdminInfo createAdmin(CreateAdminCommand command);
}
