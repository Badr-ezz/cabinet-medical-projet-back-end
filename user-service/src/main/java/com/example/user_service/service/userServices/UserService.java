package com.example.user_service.service.userServices;

import com.example.user_service.request.UserRequest;
import com.example.user.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse addUser(UserRequest userReq);
    UserResponse updateUser(UserRequest userReq);
    void deleteUser(long id);
    UserResponse getUser(long id);
    List<UserResponse> getAllUsers();
    UserResponse getUserByLogin(String login);
}
