package com.example.user_service.service.loginServices;

import com.example.user_service.request.UserRequest;
import com.example.user_service.response.AuthResponse;
import com.example.user.UserResponse;

public interface LoginServices {

    AuthResponse authenticate(String login, String pwd);
    UserResponse register(UserRequest request);
    AuthResponse checkTokenValidity (String token);
}
