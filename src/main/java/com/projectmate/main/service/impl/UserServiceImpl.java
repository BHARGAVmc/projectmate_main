package com.projectmate.main.service.impl;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projectmate.main.dto.LoginRequest;
import com.projectmate.main.dto.LoginResponse;
import com.projectmate.main.dto.RegisterRequest;
import com.projectmate.main.entity.User;
import com.projectmate.main.exception.DuplicateEmailException;
import com.projectmate.main.exception.InvalidCredentialsException;
import com.projectmate.main.repository.UserRepository;
import com.projectmate.main.security.JwtUtil;
import com.projectmate.main.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = findByEmail(request.getEmail());
            String token = jwtUtil.generateToken(authentication.getName());
            return LoginResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        } catch (Exception ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return user.get();
    }
}
