package com.projectmate.main.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projectmate.main.dto.ApiResponse;
import com.projectmate.main.dto.MemberResponse;
import com.projectmate.main.dto.ProjectCreateRequest;
import com.projectmate.main.dto.ProjectRequestCreateRequest;
import com.projectmate.main.dto.ProjectRequestResponse;
import com.projectmate.main.dto.ProjectResponse;
import com.projectmate.main.entity.User;
import com.projectmate.main.exception.InvalidCredentialsException;
import com.projectmate.main.service.ProjectService;
import com.projectmate.main.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody ProjectCreateRequest request,
            Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        ProjectResponse response = projectService.createProject(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ProjectResponse>builder()
                .success(true)
                .message("Project published successfully")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProjectResponse>>> searchProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> response = projectService.searchProjects(category, skill, keyword, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<ProjectResponse>>builder()
                .success(true)
                .message("Projects fetched successfully")
                .data(response)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Long id) {
        ProjectResponse response = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.<ProjectResponse>builder()
                .success(true)
                .message("Project fetched successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        projectService.deleteProject(id, currentUser);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Project deleted successfully")
                .build());
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<ProjectResponse>>> getMyProjects(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User currentUser = getCurrentUser(authentication);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> response = projectService.getMyProjects(currentUser, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<ProjectResponse>>builder()
                .success(true)
                .message("My projects fetched successfully")
                .data(response)
                .build());
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<ApiResponse<Page<MemberResponse>>> getProjectMembers(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MemberResponse> response = projectService.getProjectMembers(id, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<MemberResponse>>builder()
                .success(true)
                .message("Project members fetched successfully")
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
