package com.example.user_service.service.loginServices;

import com.example.user_service.entity.User;
import com.example.user_service.request.UserRequest;
import com.example.user_service.response.AuthResponse;
import com.example.user_service.response.UserResponse;

public interface LoginServices {

    String authenticate(String login, String pwd);
    UserResponse register(UserRequest request);
    AuthResponse checkTokenValidity (String token);
}
