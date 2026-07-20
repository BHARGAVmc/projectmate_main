package com.projectmate.main.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projectmate.main.dto.ApiResponse;
import com.projectmate.main.dto.ProjectRequestCreateRequest;
import com.projectmate.main.dto.ProjectRequestResponse;
import com.projectmate.main.entity.User;
import com.projectmate.main.exception.InvalidCredentialsException;
import com.projectmate.main.service.ProjectService;
import com.projectmate.main.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class ProjectRequestController {

    private final ProjectService projectService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectRequestResponse>> sendRequest(
            @Valid @RequestBody ProjectRequestCreateRequest request,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        ProjectRequestResponse response = projectService.sendRequest(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ProjectRequestResponse>builder()
                .success(true)
                .message("Request sent successfully")
                .data(response)
                .build());
    }

    @GetMapping("/sent")
    public ResponseEntity<ApiResponse<Page<ProjectRequestResponse>>> getMySentRequests(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User currentUser = getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectRequestResponse> response = projectService.getMySentRequests(currentUser, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<ProjectRequestResponse>>builder()
                .success(true)
                .message("Sent requests fetched successfully")
                .data(response)
                .build());
    }

    @GetMapping("/received")
    public ResponseEntity<ApiResponse<Page<ProjectRequestResponse>>> getReceivedRequests(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User currentUser = getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectRequestResponse> response = projectService.getReceivedRequests(currentUser, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<ProjectRequestResponse>>builder()
                .success(true)
                .message("Received requests fetched successfully")
                .data(response)
                .build());
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<ProjectRequestResponse>> acceptRequest(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        ProjectRequestResponse response = projectService.acceptRequest(id, currentUser);
        return ResponseEntity.ok(ApiResponse.<ProjectRequestResponse>builder()
                .success(true)
                .message("Request accepted successfully")
                .data(response)
                .build());
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ProjectRequestResponse>> rejectRequest(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        ProjectRequestResponse response = projectService.rejectRequest(id, currentUser);
        return ResponseEntity.ok(ApiResponse.<ProjectRequestResponse>builder()
                .success(true)
                .message("Request rejected successfully")
                .data(response)
                .build());
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new InvalidCredentialsException("Authentication required");
        }
        return userService.findByEmail(authentication.getName());
    }
}
