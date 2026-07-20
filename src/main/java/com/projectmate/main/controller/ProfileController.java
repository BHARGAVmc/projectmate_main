package com.projectmate.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectmate.main.dto.ApiResponse;
import com.projectmate.main.dto.ProfileRequest;
import com.projectmate.main.dto.ProfileResponse;
import com.projectmate.main.entity.User;
import com.projectmate.main.exception.InvalidCredentialsException;
import com.projectmate.main.service.ProfileService;
import com.projectmate.main.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> createProfile(
            @Valid @RequestBody ProfileRequest request,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        ProfileResponse profileResponse = profileService.createProfile(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Profile created successfully")
                .data(profileResponse)
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        ProfileResponse profileResponse = profileService.getProfile(currentUser);
        return ResponseEntity.ok(ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Profile fetched successfully")
                .data(profileResponse)
                .build());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @Valid @RequestBody ProfileRequest request,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        ProfileResponse profileResponse = profileService.updateProfile(request, currentUser);
        return ResponseEntity.ok(ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(profileResponse)
                .build());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteProfile(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        profileService.deleteProfile(currentUser);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Profile deleted successfully")
                .build());
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new InvalidCredentialsException("Authentication required");
        }
        return userService.findByEmail(authentication.getName());
    }
}
