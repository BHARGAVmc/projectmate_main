package com.projectmate.main.mapper;

import org.springframework.stereotype.Component;

import com.projectmate.main.dto.RegisterResponse;
import com.projectmate.main.entity.User;

@Component
public class UserMapper {

    public RegisterResponse toRegisterResponse(User user) {
        return RegisterResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
