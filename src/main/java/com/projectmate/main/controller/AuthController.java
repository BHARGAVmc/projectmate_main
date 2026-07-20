package com.projectmate.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectmate.main.dto.ApiResponse;
import com.projectmate.main.dto.LoginRequest;
import com.projectmate.main.dto.LoginResponse;
import com.projectmate.main.dto.RegisterRequest;
import com.projectmate.main.dto.RegisterResponse;
import com.projectmate.main.entity.User;
import com.projectmate.main.mapper.UserMapper;
import com.projectmate.main.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        RegisterResponse registerResponse = userMapper.toRegisterResponse(user);
        ApiResponse<RegisterResponse> response = ApiResponse.<RegisterResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(registerResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = userService.login(request);
        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login successful")
                .data(loginResponse)
                .build();
        return ResponseEntity.ok(response);
    }
}
