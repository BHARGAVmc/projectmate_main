package com.projectmate.main.service;

import com.projectmate.main.dto.LoginRequest;
import com.projectmate.main.dto.LoginResponse;
import com.projectmate.main.dto.RegisterRequest;
import com.projectmate.main.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    User findByEmail(String email);
}
